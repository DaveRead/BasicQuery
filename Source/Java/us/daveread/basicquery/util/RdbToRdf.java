package us.daveread.basicquery.util;

/**
 * RdbToRdf - A proof-of-concept (POC) for converting relational
 * data into RDF triples using inferencing
 * 
 * This program uses Jena and Pellet to allow for the creation of
 * a set of RDF triples based on data from a relational database
 * query.
 * 
 * NOTE: THIS PROGRAM IS SOLELY FOR USE AS A PROOF-OF-CONCEPT.  IT
 * PLACES A DATABASE PASSWORD IN PLAINTEXT WITHIN A CONFIGURATION
 * FILE.  THIS IS AN INSECURE PRACTICE THAT SHOULD NOT BE USED FOR
 * AN APPLICATION THAT ACCESSES PRODUCTION DATA.
 * 
 *    Copyright (C) 2010 David S. Read
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *    
 *    For information on Jena: http://jena.sourceforge.net/
 *    For information on Pellet: http://clarkparsia.com/pellet
 */

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;

/**
 * Export a SQL ResultSet to triples
 * 
 * @author David Read
 */
public class RdbToRdf implements Runnable {
  /**
   * Logging instance
   */
  private static final Logger LOGGER = Logger.getLogger(RdbToRdf.class);

  /**
   * Default namespace for the data loaded from the RDB
   */
  private static final String DEFAULT_NAMESPACE = "http://monead.com/semantic/utility";

  /**
   * Default class for all the exported records
   */
  private static final String DEFAULT_DATA_CLASS = "RdbData";

  /**
   * Used for formatting DB date column values into the ontology
   */
  private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(
      "yyyy-MM-dd");

  /**
   * Used for formatting DB time column values into the ontology
   */
  private static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat(
      "hh:mm:ss");

  /**
   * Used for formatting DB timestamp (date and time) column values into the
   * ontology
   */
  private static final SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat(
      "yyyy-MM-dd'T'hh:mm:ss");

  /**
   * The namespace to use for the generated resources and predicates
   */
  private String rdfNamespace;

  /**
   * The class of the data instances generated
   */
  private String rdfDataClass;

  /**
   * The set of formats that can be output. These are defined by Jena
   */
  private static final String[] FORMATS = {
      "Turtle",
      "N-Triples",
      "RDF/XML",
      "N3"
  };

  /**
   * The reasoning level to use
   * 
   * TODO allow this to be controlled from command line or configuration
   */
  private static final String REASONING_LEVEL = "none";

  /**
   * The output format for the triples
   * 
   * This will default to match the input format
   * 
   * TODO allow control from command line or configuration
   */
  private String outputFormat = "TURTLE";

  /**
   * The reasoning levels available.
   */
  protected static final String[] REASONING_LEVELS = {
      "none",
      "rdfs",
      "owl"
  };

  /**
   * Constant used if a value cannot be found in an array
   */
  private static final int UNKNOWN = -1;

  /**
   * The name (and path if necessary) to the output file for the output
   * triples
   */
  private String outputFileName;

  /**
   * The loaded ontology
   */
  private OntModel ontModel;

  /**
   * The query returning the result set
   */
  private String query;

  /**
   * The result set to be exported
   */
  private ResultSet resultSet;

  /**
   * The metadata associated with the result set
   */
  private ResultSetMetaData resultSetMetaData;

  /**
   * Collection of datatype properties already encountered
   */
  private Map<String, String> datatypeProperties;

  /**
   * Count of DB rows exported as of last execution of this instance
   */
  private long latestNumberOfRowsExported;

  /**
   * Constructor - sets up the input and output file paths and the triples map
   * 
   * @param pOutputFileName
   *          The name (and optional path) for the output
   * @param pQuery
   *          The query associated with the result set
   * @param pResultSet
   *          The database result set to export
   */
  public RdbToRdf(String pOutputFileName, String pQuery, ResultSet pResultSet) {
    LOGGER.info("Startup outputFileName:" + pOutputFileName);
    setOutputFileName(pOutputFileName);
    setQuery(pQuery);
    setResultSet(pResultSet);

    try {
      setResultSetMetaData(pResultSet.getMetaData());
    } catch (SQLException sqlExc) {
      throw new IllegalStateException(
          "Unable to access result set meta data from results", sqlExc);
    }

    rdfNamespace = DEFAULT_NAMESPACE + "#";
    rdfDataClass = DEFAULT_DATA_CLASS;

    datatypeProperties = new HashMap<String, String>();
  }

  /**
   * Perform the steps to load, compare and report on the ontology
   */
  public void run() {
    LOGGER.info("Load model with reasoner: " + REASONING_LEVEL);

    latestNumberOfRowsExported = 0;

    loadModel(REASONING_LEVEL);

    loadModelFromRdb();

    storeModel();
  }

  /**
   * Setup the ontology model from the data anbd metadata in the result set
   */
  private void loadModelFromRdb() {
    ResultSet lResultSet;
    ResultSetMetaData lResultSetMetaData;
    int numColumns;
    String namespace;
    int realColumn;
    String className;
    long recordCount = 0;

    lResultSet = null;

    // Create the class for all the exported data
    ontModel.createClass(rdfNamespace + rdfDataClass);

    // Access the database, run the query and create the triples
    try {
      lResultSet = getResultSet();
      lResultSetMetaData = getResultSetMetaData();
      numColumns = lResultSetMetaData.getColumnCount();

      LOGGER.debug("Catalog:" + lResultSetMetaData.getCatalogName(1)
          + " Schema:" + lResultSetMetaData.getSchemaName(1)
          + " Table:" + lResultSetMetaData.getTableName(1));

      // Default namespace
      namespace = DEFAULT_NAMESPACE;

      // Default class name
      className = rdfDataClass;

      // Create table-specific namespace and class name if possible
      realColumn = 0;
      for (int columnNum = 1; realColumn == 0
          && columnNum <= lResultSetMetaData.getColumnCount(); ++columnNum) {
        if (lResultSetMetaData.getTableName(columnNum).trim().length() > 0) {
          realColumn = columnNum;
        }
      }

      if (realColumn > 0) {
        // Namespace
        if (lResultSetMetaData.getCatalogName(realColumn).trim()
            .length() > 0) {
          namespace += "/"
              + lResultSetMetaData.getCatalogName(realColumn);
        }
        if (lResultSetMetaData.getSchemaName(realColumn).trim().length() > 0) {
          namespace += "/"
              + lResultSetMetaData.getSchemaName(realColumn);
        }
        if (lResultSetMetaData.getTableName(realColumn).trim().length() > 0) {
          namespace += "/"
              + lResultSetMetaData.getTableName(realColumn);
        }

        // Class name
        if (lResultSetMetaData.getTableName(realColumn).trim().length() > 0) {
          className = convertToClassName(lResultSetMetaData
              .getTableName(realColumn));
        }
      } else {
        namespace += "/" + "global";
      }

      // Define a base URI
      // TODO Make this an option to enable/disable
      // ontModel.setNsPrefix("", namespace + "/base" + "#");

      // Define this as an ontology
      // TODO Make this an option to enable/disable
      // ontModel.createOntology(URIref.encode(getOutputFileName().replaceAll("\\\\",
      // "/")));

      namespace += "#";

      // Prefixes
      ontModel.setNsPrefix("bqbase", rdfNamespace);
      ontModel.setNsPrefix("bqexport", namespace);

      // Create the class for this exported table
      ontModel.createClass(namespace + className);

      // Add query as class description
      ontModel.add(ontModel.createResource(namespace + className),
          ontModel.createAnnotationProperty(namespace + "sourceQuery"),
          getQuery(), XSDDatatype.XSDstring);

      // addStatement(rdfDataClass, "a", "owl:Class", namespace);

      while (lResultSet.next()) {
        ++recordCount;
        addInstance(recordCount + "", namespace, className);
        for (int col = 1; col <= numColumns; ++col) {
          addStatement(recordCount + "",
                lResultSetMetaData.getColumnLabel(col),
                lResultSet.getString(col),
              lResultSetMetaData.getColumnType(col),
              namespace, className);
        }
      }
    } catch (SQLException sqlExc) {
      LOGGER.error("Error accessing the database", sqlExc);
      throw new IllegalStateException("Error accessing the database",
          sqlExc);
    }

    latestNumberOfRowsExported = recordCount;
  }

  /**
   * Add the PK value as a new instance of the data class. The name is
   * prepended with the DEFAULT_INSTANCE_NAME_PREFIX to prevent issues with
   * values that start with a digit.
   * 
   * This method also adds an RDF label containing the actual value
   * 
   * @param subject
   *          The subject which will be a class instance
   * @param namespace
   *          The namespace for the exported data
   * @param className
   *          The class name for the individuals representing the rows of data
   */
  private void addInstance(String subject, String namespace,
      String className) {
    Resource resource;
    Property property;
    Resource object;

    // LOGGER.debug("Add subject as class instance: " + subject);

    // subject is instance of the RdbData class
    resource = ontModel.createResource(namespace + className + "_"
        + convertToInstanceName(makeSafeURIValue(subject)));
    property = ontModel
        .createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
    object = ontModel.createResource(rdfNamespace + rdfDataClass);
    ontModel.add(resource, property, object);

    // And an instance of the specific table class
    object = ontModel.createResource(namespace + className);
    ontModel.add(resource, property, object);

    // Add the actual subject value as a label
    property = ontModel
        .createProperty("http://www.w3.org/2000/01/rdf-schema#label");
    ontModel.add(resource, property, subject);
  }

  /**
   * Add the RDB data, treating the column data as a literal value
   * 
   * @param subject
   *          The subject of the triple
   * @param predicate
   *          The predicate of the triple
   * @param objectLiteral
   *          The object of the triple (treated as a literal)
   * @param rdbColumnType
   *          The SQL data type of the column containing this literal value
   * @param namespace
   *          The namespace for the exported data
   * @param className
   *          The class name for the individuals representing the rows of data
   */
  private void addStatement(String subject, String predicate,
      String objectLiteral, int rdbColumnType, String namespace,
      String className) {
    Resource resource;
    Property property;
    String formattedLiteral;
    XSDDatatype xsdDataType;

    if (objectLiteral == null) {
      return; // EARLY RETURN - Configured to Skip Null Columns
    }

    // Add the data as a triple
    resource = ontModel.createResource(namespace + className + "_"
        + convertToInstanceName(makeSafeURIValue(subject)));
    property = ontModel.createProperty(namespace
        + convertToPropertyName(makeSafeURIValue(predicate)));

    // LOGGER.debug("Add statement to model: " + resource + "->" + property
    // + "->" + objectLiteral);

    switch (rdbColumnType) {
      case Types.BIGINT:
      case Types.INTEGER:
      case Types.SMALLINT:
      case Types.TINYINT:
        xsdDataType = XSDDatatype.XSDinteger;
        formattedLiteral = Long.parseLong(objectLiteral) + "";
        break;
      case Types.DECIMAL:
      case Types.DOUBLE:
      case Types.FLOAT:
      case Types.NUMERIC:
      case Types.REAL:
        xsdDataType = XSDDatatype.XSDdouble;
        formattedLiteral = Double.parseDouble(objectLiteral) + "";
        break;
      case Types.DATE:
        xsdDataType = XSDDatatype.XSDdate;
        try {
          formattedLiteral = FORMAT_DATE.format(FORMAT_DATE
              .parse(objectLiteral));
        } catch (Throwable throwable) {
          LOGGER.error("Cannot parse DB date: " + objectLiteral);
          xsdDataType = XSDDatatype.XSDstring;
          formattedLiteral = objectLiteral;
        }
        break;
      case Types.TIME:
        xsdDataType = XSDDatatype.XSDtime;
        try {
          formattedLiteral = FORMAT_TIME.format(FORMAT_TIME
              .parse(objectLiteral));
        } catch (Throwable throwable) {
          LOGGER.error("Cannot parse DB time: " + objectLiteral);
          xsdDataType = XSDDatatype.XSDstring;
          formattedLiteral = objectLiteral;
        }
        break;
      case Types.TIMESTAMP:
        xsdDataType = XSDDatatype.XSDdateTime;
        try {
          formattedLiteral = FORMAT_DATE_TIME.format(FORMAT_DATE_TIME
              .parse(objectLiteral));
        } catch (Throwable throwable) {
          LOGGER.error("Cannot parse DB timestamp (date and time): "
              + objectLiteral);
          xsdDataType = XSDDatatype.XSDstring;
          formattedLiteral = objectLiteral;
        }
        break;
      case Types.CHAR:
      case Types.VARCHAR:
      default:
        xsdDataType = XSDDatatype.XSDstring;
        formattedLiteral = objectLiteral;
        break;
    }

    // Data type identified from DB
    // ontModel.add(resource, ontModel.createProperty(namespace
    // + convertToPropertyName(makeSafeURIValue("colType_" + predicate))),
    // rdbColumnType + "", XSDDatatype.XSDinteger);

    // Literal value from DB record
    ontModel.add(resource, property, formattedLiteral, xsdDataType);

    // Check if this is a new owl:DatatypeProperty
    makeDatatypeProperty(namespace,
        convertToPropertyName(makeSafeURIValue(predicate)));
  }

  /**
   * Check to see if this property URI has been asserted as an
   * owl:DatatypeProperty. If not, assert it.
   * 
   * @param namespace
   *          The namespace of the property
   * @param propertyName
   *          The name of the property
   */
  private void makeDatatypeProperty(String namespace, String propertyName) {
    Resource subject;
    Property predicate;
    Resource object;

    if (datatypeProperties.get(namespace + propertyName) == null) {
      subject = ontModel.createResource(namespace + propertyName);
      predicate = ontModel
          .createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
      object = ontModel
          .createResource("http://www.w3.org/2002/07/owl#DatatypeProperty");
      ontModel.add(subject, predicate, object);

      datatypeProperties.put(namespace + propertyName, "Y");
    }

  }

  /**
   * Very crude method to create a value that will work as an RDF resource -
   * e.g. removes spaces. If the data contains other special characters then
   * this function will need to be fleshed out.
   * 
   * @param value
   *          the value to be made URI-safe
   * 
   * @return a URI-safe value (no spaces)
   */
  private static String makeSafeURIValue(String value) {
    return value.replaceAll(" ", "_");
  }

  /**
   * Writes the triples to a data file.
   * 
   */
  private void storeModel() {
    FileWriter out;

    out = null;

    LOGGER.info("Begin writing loaded data to file, " + outputFileName
        + ", in format: " + outputFormat);

    try {
      out = new FileWriter(outputFileName, false);
      ontModel.write(out, outputFormat);
      LOGGER.info("Completed writing loaded data to file, " + outputFileName
          + ", in format: " + outputFormat);
    } catch (IOException ioExc) {
      LOGGER.error("Unable to write to file: " + outputFileName, ioExc);
      throw new RuntimeException("unable to write output file ("
          + outputFileName + ")", ioExc);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (Throwable throwable) {
          LOGGER.error("Failed to close output file: "
              + outputFileName, throwable);
          throw new RuntimeException("Failed to close output file",
              throwable);
        }
      }
    }
  }

  /**
   * Get the set of defined ontology file formats that the program can load as
   * a CSV list String
   * 
   * @return The known ontology file formats as a CSV list
   */
  public static final String getFormatsAsCSV() {
    return getArrayAsCSV(FORMATS);
  }

  /**
   * Create a CSV list from a String array
   * 
   * @param array
   *          An array
   * @return The array values in a CSV list
   */
  public static final String getArrayAsCSV(String[] array) {
    StringBuffer csv;

    csv = new StringBuffer();

    for (String value : array) {
      if (csv.length() > 0) {
        csv.append(", ");
      }
      csv.append(value);
    }

    return csv.toString();

  }

  /**
   * Set the output file name, where the report should be written
   * 
   * @param pOutputFileName
   *          The output file name
   */
  public void setOutputFileName(String pOutputFileName) {
    outputFileName = pOutputFileName;
  }

  /**
   * Get the output file name for the location of the generated report
   * 
   * @return The output file name
   */
  public String getOutputFileName() {
    return outputFileName;
  }

  /**
   * Set the query returning the result set
   * 
   * @param pQuery
   *          The SQL query
   */
  private void setQuery(String pQuery) {
    query = pQuery;
  }

  /**
   * Get the query returning the result set
   * 
   * @return The SQL Query
   */
  private String getQuery() {
    return query;
  }

  /**
   * Set the result set to be exported
   * 
   * @param pResultSet
   *          The result set
   */
  private void setResultSet(ResultSet pResultSet) {
    resultSet = pResultSet;
  }

  /**
   * Get the result set to be exported
   * 
   * @return The result set
   */
  private ResultSet getResultSet() {
    return resultSet;
  }

  /**
   * Set the metadata for the result set to be exported
   * 
   * @param pResultSetMetaData
   *          The metadata
   */
  private void setResultSetMetaData(ResultSetMetaData pResultSetMetaData) {
    resultSetMetaData = pResultSetMetaData;
  }

  /**
   * Get the metadata for the result set to be exported
   * 
   * @return The metadata
   */
  private ResultSetMetaData getResultSetMetaData() {
    return resultSetMetaData;
  }

  /**
   * Create a model with a reasoner set based on the chosen reasoning level.
   * 
   * @param reasoningLevel
   *          The reasoning level for this model
   * 
   * @return The created ontology model
   */
  private OntModel createModel(String reasoningLevel) {
    OntModel model;
    int reasoningLevelIndex;

    model = null;

    reasoningLevelIndex = getReasoningLevelIndex(reasoningLevel);

    if (reasoningLevelIndex == 0) { // None
      model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
    } else if (reasoningLevelIndex == 1) { // RDFS
      model = ModelFactory
          .createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
    } else if (reasoningLevelIndex == 2) { // OWL
      final Reasoner reasoner = PelletReasonerFactory.theInstance().create();
      final Model infModel = ModelFactory.createInfModel(reasoner,
          ModelFactory.createDefaultModel());
      model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM,
          infModel);
    }

    return model;
  }

  /**
   * Obtain an ontology model set to the chosen reasoning level. Load the
   * ontology file into the model
   * 
   * @param reasoningLevel
   *          The selected reasoning level
   */
  private void loadModel(String reasoningLevel) {
    ontModel = createModel(reasoningLevel);
  }

  /**
   * Get the index position of the supplied reasoning level label
   * 
   * @param reasonerName
   *          A reasoning level label
   * 
   * @return The index position of the reasoning level. Will be equal to the
   *         constant UNKNOWN if the value cannot be found in the collection
   *         of known reasoning levels
   */
  public static final int getReasoningLevelIndex(String reasonerName) {
    return getIndexValue(REASONING_LEVELS, reasonerName);
  }

  /**
   * Get the number of DB rows exported to triples as of the last exeuction of
   * this instance
   * 
   * @return The number of DB rows exported
   */
  public long getLatestNumberOfRowsExported() {
    return latestNumberOfRowsExported;
  }

  /**
   * Find a String value within and array of Strings. Return the index
   * position where the value was found.
   * 
   * @param array
   *          An array of string to search
   * @param name
   *          The value to find in the array
   * 
   * @return The position where the value was found in the array. Will be
   *         equal to the constant UNKNOWN if the value cannot be found in the
   *         collection of known reasoning levels
   */
  public static final int getIndexValue(String[] array, String name) {
    Integer indexValue;

    indexValue = null;

    for (int index = 0; index < array.length && indexValue == null; ++index) {
      if (array[index].toUpperCase().equals(name.toUpperCase())) {
        indexValue = index;
      }
    }

    return indexValue == null ? UNKNOWN : indexValue;
  }

  /**
   * Convert a string to a CamelCase format matching class naming convention
   * 
   * @param name
   *          The text to munge into a class name
   * 
   * @return The munged class name
   */
  private String convertToClassName(String name) {
    return convertToCamelCase(name, true);
  }

  /**
   * Convert a string into a camelCase format matching property naming
   * convention
   * 
   * @param name
   *          The text to munge into a property name
   * 
   * @return The munged property name
   */
  private String convertToPropertyName(String name) {
    return convertToCamelCase(name, false);
  }

  /**
   * Convert a string into a camelCase format matching instance naming
   * convention
   * 
   * @param name
   *          The text to munge into an instance (individual) name
   * 
   * @return The munged instance name
   */
  private String convertToInstanceName(String name) {
    return convertToCamelCase(name, false);
  }

  /**
   * Convert a string into CamelCase or camelCase
   * 
   * @param name
   *          The string to convert to camel case
   * 
   * @param initialUpper
   *          Whether the resulting string should begin with an uppercase
   *          character
   * 
   * @return The camel case string
   */
  private String convertToCamelCase(String name, boolean initialUpper) {
    final StringBuffer inCamelForm = new StringBuffer();
    boolean nextUpper = initialUpper;
    String fixName = name;

    fixName = fixName.replaceAll("['(),]", "_");
    final char[] chars = fixName.toCharArray();

    for (char character : chars) {
      if (character == '_') {
        nextUpper = true;
      } else if (nextUpper) {
        inCamelForm.append(Character.toUpperCase(character));
        nextUpper = false;
      } else {
        inCamelForm.append(Character.toLowerCase(character));
      }
    }

    return inCamelForm.toString();
  }
}
