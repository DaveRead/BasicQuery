package us.daveread.basicquery.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: Configuration management
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006-2014
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author David Read
 */
public class Configuration extends Properties {
  /**
   * Serial UID
   */
  private static final long serialVersionUID = 8439104437125048420L;

  /**
   * Instance of this class - Singleton
   */
  private static Configuration config;

  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(Configuration.class);

  /**
   * Sub-directory for the configuration files
   */
  private static final String PROPERTIES_DIRECTORY = "BasicQuery";

  /**
   * Property file name
   */
  private static final String FILENAME_PROPERTIES = "BasicQuery.Properties";

  /**
   * System property containing the user's home directory
   */
  private static final String PROP_SYSTEM_USERHOMEDIR = "user.home";

  /**
   * The user's home directory
   */
  private static String userHomeDirectory;

  /**
   * Setup the configuration instance
   */
  private Configuration() {
    userHomeDirectory = System.getProperty(PROP_SYSTEM_USERHOMEDIR);

    if (userHomeDirectory == null) {
      userHomeDirectory = PROPERTIES_DIRECTORY;
    } else {
      userHomeDirectory += "/" + PROPERTIES_DIRECTORY;
      new File(userHomeDirectory).mkdirs();
    }
  }

  /**
   * Get the instance of this class
   * 
   * @return The Singleton instance
   */
  public static synchronized Configuration instance() {
    if (config == null) {
      config = new Configuration();
    }

    // TODO Shouldn't this be done in the constructor?
    config.setupProperties();

    return config;
  }

  /**
   * Store the configuration into the configuration file
   */
  public void store() {
    File configFile;

    configFile = getFile(FILENAME_PROPERTIES);

    try {
      store(new FileOutputStream(configFile, false), "BasicQuery Configuration");
    } catch (Throwable any) {
      LOGGER.error("Unable to store properties file ("
          + configFile.getAbsolutePath() + ")", any);
    }
  }

  /**
   * Load the properties from the properties file
   */
  private void setupProperties() {
    File propFile;

    propFile = getFile(FILENAME_PROPERTIES);

    try {
      config.load(new FileInputStream(propFile));
    } catch (Throwable any) {
      LOGGER.error("Unable to load properties file ("
          + propFile.getAbsolutePath() + ")", any);
    }
  }

  /**
   * Setup a file based on the file name
   * 
   * @param fileName
   *          The file to create a file reference for
   * 
   * @see #getFilePath(String)
   * @see #userDefaultFile(String)
   * 
   * @return The file reference to the named file
   */
  public File getFile(String fileName) {
    File configFile;

    configFile = new File(getFilePath(fileName));
    if (!configFile.exists()) {
      userDefaultFile(fileName);
    }

    return configFile;
  }

  /**
   * Prepend the user's home directory path the the file name and create a file
   * instance
   * 
   * @param fileName
   *          The file name
   * 
   * @return The file name prepended with the user's home directory
   */
  private String getFilePath(String fileName) {
    return userHomeDirectory + "/" + fileName;
  }

  /**
   * Prepend the user's home directory path the the file name and create a file
   * instance. If the file is not found, copy the default version of the file
   * into the target file in order to initialize the application
   * 
   * @param fileName
   *          The file name
   */
  private void userDefaultFile(String fileName) {
    File realFile, defaultFile;

    realFile = new File(userHomeDirectory + "/" + fileName);
    if (!realFile.exists()) {
      defaultFile = new File(fileName + ".txt");
      if (defaultFile.exists()) {
        copyFile(defaultFile, realFile);
      }
    }
  }

  /**
   * Copy the content of a text file into another text file
   * 
   * @param source
   *          The source file
   * @param dest
   *          The target file
   */
  private static void copyFile(File source, File dest) {
    BufferedReader in;
    PrintWriter out;
    String data;

    in = null;
    out = null;

    try {
      in = new BufferedReader(new FileReader(source));
      out = new PrintWriter(new FileWriter(dest));

      while ((data = in.readLine()) != null) {
        out.println(data);
      }
    } catch (Throwable any) {
      LOGGER.warn("Unable to copy default file (" + source.getAbsolutePath()
          + ") to configuration file (" + dest.getAbsolutePath() + ")", any);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (Throwable any) {
          LOGGER.warn("Unable to close the input file: " + source, any);
        }
      }
      if (out != null) {
        try {
          out.close();
        } catch (Throwable any) {
          LOGGER.warn("Unable to close the output file: " + dest, any);
        }
      }
    }
  }
}
