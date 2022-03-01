package us.daveread.basicquery;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ProgressMonitor;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;

import us.daveread.basicquery.gui.ColoredCellRenderer;
import us.daveread.basicquery.gui.FlashForeground;
import us.daveread.basicquery.gui.FocusRequestor;
import us.daveread.basicquery.gui.FontChooser;
import us.daveread.basicquery.gui.InsertTime;
import us.daveread.basicquery.gui.MaxHeightJScrollPane;
import us.daveread.basicquery.gui.MessageStyleFactory;
import us.daveread.basicquery.images.ImageUtility;
import us.daveread.basicquery.queries.Query;
import us.daveread.basicquery.queries.QueryHistory;
import us.daveread.basicquery.queries.QueryInfo;
import us.daveread.basicquery.util.CheckLatestVersion;
import us.daveread.basicquery.util.Configuration;
import us.daveread.basicquery.util.FileFilterDefinition;
import us.daveread.basicquery.util.ListTableModel;
import us.daveread.basicquery.util.NewVersionInformation;
import us.daveread.basicquery.util.RdbToRdf;
import us.daveread.basicquery.util.Resources;
import us.daveread.basicquery.util.SuffixFileFilter;
import us.daveread.basicquery.util.TableSorter;
import us.daveread.basicquery.util.Utility;

/**
 * Title: Basic Query Utility
 * <p>
 * Execute arbitrary SQL against database accessible with any JDBC-compliant
 * driver.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2004-2020, David Read
 * </p>
 * 
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * </p>
 * 
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * </p>
 * 
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author David Read
 */

public class BasicQuery extends JFrame implements Runnable, ActionListener,
    WindowListener, Observer, KeyListener {

  /**
   * Serial version id - update if the serialization of this classes changes
   */
  private static final long serialVersionUID = 5162L;

  /**
   * Program version - MUST be in ##.##.## format
   */
  private static final String VERSION = "02.01.00";

  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(BasicQuery.class);

  /**
   * Database drivers file
   */
  private static final String FILENAME_DRIVERS = "BasicQuery.Drivers";

  /**
   * Logging configuration file
   */
  private static final String FILENAME_LOGGER = "BasicQuery.Logger";

  /**
   * Default file for storing queries
   */
  private static final String FILENAME_DEFAULTQUERIES = "BasicQuery.SQL";

  /**
   * File containing connection strings
   */
  private static final String FILENAME_CONNECTSTRINGS = "BasicQuery.Connect";

  /**
   * Name of the DB connection pool
   */
  private static final String DBPOOL_NAME = "BasicQuery.DBPool";

  /**
   * File containing statistics, when enabled
   */
  private static final String DBSTATS_NAME = "BasicQuery.Stats.csv";

  /**
   * File containing query results, when enabled
   */
  private static final String DBRESULTS_NAME = "BasicQuery.Results.csv";

  /**
   * Property name - user id
   */
  private static final String PROP_USERID = "USERID";

  /**
   * Property name - password
   */
  private static final String PROP_PASSWORD = "PASSWORD";

  /**
   * Property name - connection autocommit setting
   */
  private static final String PROP_AUTOCOMMIT = "AUTOCOMMIT";

  /**
   * Property name - connection readonly setting
   */
  private static final String PROP_READONLY = "READONLY";

  /**
   * Property name - connection pooling enabled
   */
  private static final String PROP_POOLING = "POOLING";

  /**
   * Property name - last connection string used
   */
  private static final String PROP_CONNECTION = "CONNECTION";

  /**
   * Property name - last SQL statement executed
   */
  private static final String PROP_SQL = "SQL";

  /**
   * Propert name - last SQL statement file used
   */
  private static final String PROP_SQLFILENAME = "SQLFILE";

  /**
   * Property name - maximum number of display rows setting
   */
  private static final String PROP_MAXROWS = "MAXROWS";

  /**
   * Property name - log statistics setting
   */
  private static final String PROP_LOGSTATS = "LOGSTATS";

  /**
   * Property name - log query results setting
   */
  private static final String PROP_LOGRESULTS = "LOGRESULTS";

  /**
   * Property name - export raw query results setting
   */
  private static final String PROP_EXPORTRAW = "EXPORTRAW";

  /**
   * Property name - export without CR setting
   */
  private static final String PROP_EXPORTNOCR = "EXPORTNOCR";

  /**
   * Property name - associate query and connection setting
   */
  private static final String PROP_ASSOCSQLURL = "ASSOCSQLURL";

  /**
   * Property name - parse queries on semicolon setting
   */
  private static final String PROP_PARSESEMICOLON = "PARSESEMICOLON";

  /**
   * Property name - save password setting
   */
  private static final String PROP_SAVEPASSWORD = "SAVEPASSWORD";

  /**
   * Property name - last window upper left corner X position
   */
  private static final String PROP_UPPERLEFTX = "UPPERLEFTX";

  /**
   * Property name - last window upper left corner Y position
   */
  private static final String PROP_UPPERLEFTY = "UPPERLEFTY";

  /**
   * Property name - last window width
   */
  private static final String PROP_WIDTH = "WIDTH";

  /**
   * Property name - last window height
   */
  private static final String PROP_HEIGHT = "HEIGHT";

  /**
   * Property name - last window maximized setting
   */
  private static final String PROP_MAXIMIZED = "MAXIMIZED";

  /**
   * Property name - directory containing DB driver files
   */
  private static final String PROP_DBDRIVERDIR = "DBDRIVERDIR";

  /**
   * Property name - keywords that should not occur in an update statement
   */
  private static final String PROP_WARNUPDATEPATTERNS = "WARNUPDATEPATTERNS";

  /**
   * Property name - keywords that should not occur in a select statement
   */
  private static final String PROP_WARNSELECTPATTERNS = "WARNSELECTPATTERNS";

  /**
   * Property name - display DB server information setting
   */
  private static final String PROP_DISPLAYDBSERVERINFO = "DISPLAYDBSERVERINFO";

  /**
   * Property name - display client information setting
   */
  private static final String PROP_DISPLAYCLIENTINFO = "DISPLAYCLIENTINFO";

  /**
   * Property name - display column type setting
   */
  private static final String PROP_DISPLAYCOLUMNDATATYPE =
      "DISPLAYCOLUMNDATATYPE";

  /**
   * Property name - row coloring setting
   */
  private static final String PROP_TABLE_COLORING = "TABLECOLORING";

  /**
   * Property name - user defined row coloring setting
   */
  private static final String PROP_TABLE_COLOR_USERDEF = "TABLECOLORUSERDEF";

  /**
   * Property name - interface language setting
   */
  private static final String PROP_LANGUAGE = "LANGUAGE";

  /**
   * Property name - font setting
   */
  private static final String PROP_FONT_FAMILY = "FONTFAMILY";

  /**
   * Property name - fint size setting
   */
  private static final String PROP_FONT_SIZE = "FONTSIZE";

  /**
   * Property name - font bold setting
   */
  private static final String PROP_FONT_BOLD = "FONTBOLD";

  /**
   * Property name - font italic setting
   */
  private static final String PROP_FONT_ITALIC = "FONTITALIC";

  /**
   * Latest directory used when choosing a file
   */
  private static final String PROP_LATEST_DIRECTORY = "LATEST_FILE_DIRECTORY";

  /**
   * Default directory name for DB driver files
   */
  private static final String DEFAULT_DBDRIVERDIR = "DBDrivers";

  /**
   * Default update warning statement keywords
   */
  private static final String DEFAULTWARNUPDATEPATTERNS =
      "SELECT,SHOW,DESCRIBE,DESC";

  /**
   * Default select warning statement keywords
   */
  private static final String DEFAULTWARNSELECTPATTERNS =
      "INSERT,UPDATE,DELETE,ALTER,DROP,CREATE";

  /**
   * Normal text style
   */
  private static final String STYLE_NORMAL = "Normal";

  /**
   * Bold text style
   */
  private static final String STYLE_BOLD = "Bold";

  /**
   * Bold and underlined text style
   */
  private static final String STYLE_BOLD_UL = "BoldUL";

  /**
   * Green text
   */
  private static final String STYLE_GREEN = "Green";

  /**
   * Yellow text
   */
  private static final String STYLE_YELLOW = "Yellow";

  /**
   * Red text
   */
  private static final String STYLE_RED = "Red";

  /**
   * Subtle text style
   */
  private static final String STYLE_SUBTLE = "Subtle";

  /**
   * Subtle and underlined text style
   */
  private static final String STYLE_SUBTLE_UL = "SubtleUL";

  /**
   * Table row alternating green and white
   */
  private static final String TABLECOLORING_GREENBAR = "GREENBAR";

  /**
   * Table row alternating yellow and white
   */
  private static final String TABLECOLORING_YELLOWBAR = "YELLOWBAR";

  /**
   * Table row coloring is user defined
   */
  private static final String TABLECOLORING_USERDEFINED = "USERDEFINED";

  /**
   * Table rows not colored (background white)
   */
  private static final String TABLECOLORING_NONE = "NONE";

  /**
   * Display long form of DB information
   */
  private static final String DBSERVERINFO_LONG = "LONG";

  /**
   * Display brief form of DB information
   */
  private static final String DBSERVERINFO_BRIEF = "BRIEF";

  /**
   * The Yes (True) value for a parameter
   */
  private static final String PARAMVALUE_NO = "NO";

  /**
   * The No (False) value for a parameter
   */
  private static final String PARAMVALUE_YES = "YES";

  /**
   * Query run mode - single statement
   */
  private static final int RUNTYPE_SINGLE = 0;

  /**
   * Query run mode - all statements
   */
  private static final int RUNTYPE_ALL = 1;

  /**
   * Query run mode - export results as triples
   */
  private static final int RUNTYPE_TRIPLE_EXPORT = 2;

  /**
   * System property supplying user's configured language code
   */
  private static final String PROP_SYSTEM_DEFAULTLANGUAGE = "user.language";

  /**
   * System property supplying user's configured country code
   */
  private static final String PROP_SYSTEM_DEFAULTCOUNTRY = "user.country";

  /**
   * Interface: Use default language (e.g. from system property)
   */
  private static final String LANG_DEFAULT = "DEFAULT";

  /**
   * Interface: Use English
   */
  private static final String LANG_ENGLISH = "ENGLISH";

  /**
   * Interface: Use French
   */
  private static final String LANG_FRENCH = "FRENCH";

  /**
   * Interface: Use German
   */
  private static final String LANG_GERMAN = "GERMAN";

  /**
   * Interface: Use Italian
   */
  private static final String LANG_ITALIAN = "ITALIAN";

  /**
   * Interface: Use Portuguese
   */
  private static final String LANG_PORTUGUESE = "PORTUGUESE";

  /**
   * Interface: Use Spanish
   */
  private static final String LANG_SPANISH = "SPANISH";

  /**
   * Prefix to comment-out queries
   */
  private static final String COMMENT_PREFIX = "//";

  /**
   * Rows for the query input text area
   */
  private static final int QUERY_AREA_ROWS = 5;

  /**
   * Minimum width, in characters, for the query input text area
   */
  private static final int QUERY_AREA_COLUMNS = 60;

  /**
   * Fraction of available window for the scroll pane
   */
  private static final int MAX_SCROLL_PANE_DIVISOR_FOR_MAX_HEIGHT = 6;

  /**
   * Colors per user-defined color entry
   */
  private static final int USER_DEFINED_COLORING_COLORS_PER_ENTRY = 2;

  /**
   * Color code length
   */
  private static final int USER_DEFINED_COLORING_COLOR_CODE_LENGTH = 6;

  /**
   * Numeric base for the color coding value
   */
  private static final int COLOR_CODING_NUMERIC_BASE = 16;

  /**
   * Position of the red color value
   */
  private static final int COLOR_CODING_COLOR_1_START = 0;

  /**
   * Position of the green color value
   */
  private static final int COLOR_CODING_COLOR_2_START = 2;

  /**
   * Position of the blue color value
   */
  private static final int COLOR_CODING_COLOR_3_START = 4;

  /**
   * End of the blue color value
   */
  private static final int COLOR_CODING_COLOR_3_END = 6;

  /**
   * Maximum wait time for a connection to be available from the pool
   */
  private static final int CONN_POOL_MAX_WAIT_MS = 5000;

  /**
   * Maximum number of connections to hold in the pool
   */
  private static final int CONN_POOL_MAX_IDLE_CONNECTIONS = 1;

  /**
   * Time to wait between connection pool eviction runs
   */
  private static final int CONN_POOL_TIME_BETWEEN_EVICT_RUNS_MS = 30000;

  /**
   * Number of connections in the connection pool to test per eviction run
   */
  private static final int CONN_POOL_NUM_TESTS_PER_EVICT_RUN = 5;

  /**
   * Maximum age for a connection before evicting it
   */
  private static final int CONN_POOL_EVICT_IDLE_TIME_MS = 60000;

  /**
   * Minimum length for a line before inserting a carriage return
   */
  private static final int TEXT_WRAP_MIN_LINE_LENGTH_BEFORE_WRAP = 60;

  /**
   * Maximum length for a line before inserting a carriage return
   */
  private static final int TEXT_WRAP_MAX_LINE_LENGTH = 80;

  /**
   * Maximum number of digits in a value before storing as a long
   */
  private static final int MAX_DIGITS_FOR_INT = 7;

  /**
   * Number of columns reported for a table describe
   */
  private static final int DESC_TABLE_COLUMN_COUNT = 6;

  /**
   * Table describe: Column containing the column name
   */
  private static final int DESC_TABLE_NAME_COLUMN = 0;

  /**
   * Table describe: Column containing the column name
   */
  private static final int DESC_TABLE_TYPE_COLUMN = 1;

  /**
   * Table describe: Column containing the column length
   */
  private static final int DESC_TABLE_LENGTH_COLUMN = 2;

  /**
   * Table describe: Column containing the column precision
   */
  private static final int DESC_TABLE_PRECISION_COLUMN = 3;

  /**
   * Table describe: Column containing the column scale
   */
  private static final int DESC_TABLE_SCALE_COLUMN = 4;

  /**
   * Table describe: Column containing the nulls-allowed indicator
   */
  private static final int DESC_TABLE_NULLS_OK_COLUMN = 5;

  /**
   * Column data type: string
   */
  private static final int COLUMN_DATA_TYPE_STRING = 0;

  /**
   * Column data type: integer
   */
  private static final int COLUMN_DATA_TYPE_INT = 1;

  /**
   * Column data type: long
   */
  private static final int COLUMN_DATA_TYPE_LONG = 2;

  /**
   * Column data type: double
   */
  private static final int COLUMN_DATA_TYPE_DOUBLE = 3;

  /**
   * Column data type: date
   */
  private static final int COLUMN_DATA_TYPE_DATE = 5;

  /**
   * Column data type: datetime
   */
  private static final int COLUMN_DATA_TYPE_DATETIME = 5;

  /**
   * Column data type: BLOB
   */
  private static final int COLUMN_DATA_TYPE_BLOB = 6;

  /**
   * Token prefix for a parameter
   */
  private static final String PARAM_TOKEN_START = "$PARAM[";

  /**
   * Token suffix for a parameter
   */
  private static final String PARAM_TOKEN_END = "]$";

  /**
   * Length of the parameter token prefix
   */
  private static final int PARAM_TOKEN_START_LENGTH = PARAM_TOKEN_START
      .length();

  /**
   * Length of the parameter token suffix
   */
  private static final int PARAM_TOKEN_END_LENGTH = PARAM_TOKEN_END.length();

  /**
   * Bloxk size used when storing a BLOB field to a file
   */
  private static final int BLOB_LOAD_BLOCK_SIZE = 4096;

  /**
   * Time, in milliseconds, to delay between updates of the query execution
   * timer display
   */
  private static final int QUERY_EXECUTION_TIMER_UPDATE_DELAY_MS = 250;

  /**
   * Number of rows to write between status message updates
   */
  private static final int RESULT_PROCESSING_ROWS_PER_STATUS_MESSAGE = 100000;

  /**
   * Number of seconds in an hour
   */
  private static final int SECONDS_PER_HOUR = 3600;

  /**
   * Number of seconds in a minute
   */
  private static final int SECONDS_PER_MINUTE = 60;

  /**
   * For color green: red component value
   */
  private static final int COLOR_GREEN_R_VALUE = 192;

  /**
   * For color green: green component value
   */
  private static final int COLOR_GREEN_G_VALUE = 255;

  /**
   * For color green: blue component value
   */
  private static final int COLOR_GREEN_B_VALUE = 192;

  /**
   * For color yellow: red component value
   */
  private static final int COLOR_YELLOW_R_VALUE = 255;

  /**
   * For color yellow: green component value
   */
  private static final int COLOR_YELLOW_G_VALUE = 255;

  /**
   * For color yellow: blue component value
   */
  private static final int COLOR_YELLOW_B_VALUE = 192;

  /**
   * File export format: CSV
   */
  private static final int FILE_EXPORT_CSV = 0;

  /**
   * File export format: Triples
   */
  private static final int FILE_EXPORT_TRIPLES = 1;

  /**
   * SQL results table
   */
  private JTable table;

  /**
   * Renderer for result cells
   */
  private ColoredCellRenderer cellRenderer;

  /**
   * Is the select output to render data or metadata
   */
  private int modeOfCurrentTable;

  /**
   * Collection of tables currently used by the latest query
   */
  private Map<String, String> mapOfCurrentTables;

  /**
   * User defined result row coloring pattern
   */
  private String userDefTableColoring;

  /**
   * Set of defined queries
   */
  private JComboBox querySelection;

  /**
   * Filename for current set of queries
   */
  private String queryFilename;

  /**
   * The query file filter description last used by the user
   */
  private String latestChosenQueryFileFilterDescription;

  /**
   * SQL input text area
   */
  private JTextArea queryText;

  /**
   * Set of defined connection strings
   */
  private JComboBox connectString;

  /**
   * User Id to authenticate with the DB server
   */
  private JTextField userId;

  /**
   * Password to authenticate with the DB server
   */
  private JPasswordField password;

  /**
   * Modes of SQL operation
   */
  private JRadioButton asQuery, asUpdate, asDescribe;

  /**
   * Connection feature settings
   */
  private JCheckBox autoCommit, readOnly, poolConnect;

  /**
   * Selection for maximum number of rows to retrieve and display
   */
  private JComboBox maxRows;

  /**
   * Buttons to move through history of executed queries
   */
  private JButton previousQuery, nextQuery;

  /**
   * Collection of message styles used in status output text pane
   */
  private Map<String, AttributeSet> messageStyles;

  /**
   * Execute the currently entered/selected SQL statement
   */
  private JButton execute;

  /**
   * Remove the currently selected SQL statement from the collection of stored
   * statements
   */
  private JButton remove;

  /**
   * Comment or un-comment the currently selected SQL statement
   */
  private JButton commentToggle;

  /**
   * Select the next SQL statement in the list of stored statements
   */
  private JButton nextInList;

  /**
   * The status text pane
   */
  private JTextPane message;

  /**
   * The status document backing the status text pane
   */
  private StyledDocument messageDocument;

  /**
   * Menu item - open a SQL statement file
   */
  private JMenuItem fileOpenSQL;

  /**
   * Menu item - choose whether to log SQL execution statistics
   */
  private JCheckBoxMenuItem fileLogStats;

  /**
   * Menu item - choose whether to log SQL execution results
   */
  private JCheckBoxMenuItem fileLogResults;

  /**
   * Menu item - write current results to a CSV file
   */
  private JMenuItem fileSaveAsCSV;

  /**
   * Menu item - write current results to an ontology file
   */
  private JMenuItem fileSaveAsTriples;

  /**
   * Menu item - choose whether to export BLOBs to files when exporting results
   */
  private JMenuItem fileSaveBLOBs;

  /**
   * Menu item - choose whether to export result raw (unquoted)
   */
  private JCheckBoxMenuItem fileExportsRaw;

  /**
   * Menu item - choose whether to add a carriage return to exported rows
   */
  private JCheckBoxMenuItem fileNoCRAddedToExportRows;

  /**
   * Menu item - exit the program
   */
  private JMenuItem fileExit;
  /**
   * Menu item - sort by selected column(s)
   */
  private JMenuItem editSort;

  /**
   * Menu item - select all displayed rows
   */
  private JMenuItem editSelectAll;

  /**
   * Menu item - copy selected rows to the clipboard
   */
  private JMenuItem editCopy;

  /**
   * Menu item - create a select statement that lists all columns using the
   * current result set
   */
  private JMenuItem queryMakeVerboseSelect;

  /**
   * Menu item - create an insert statement template listing all fields using
   * the current result set
   */
  private JMenuItem queryMakeInsert;

  /**
   * Menu item - create an update statement template listing all fields using
   * the current result set
   */
  private JMenuItem queryMakeUpdate;

  /**
   * Menu item - create a "select *" statement with the table name based on the
   * currently selected cell
   */
  private JMenuItem querySelectStar;

  /**
   * Menu item - create a describe statement with the table name based on the
   * currently selected cell
   */
  private JMenuItem queryDescribeStar;

  /**
   * Menu item - edit the order of the list of currently stored SQL statements
   * (e.g. from the current SQL statement file)
   */
  private JMenuItem querySetOrder;

  /**
   * Menu item - run all stored SQL statements (e.g. from the current SQL
   * statement file)
   */
  private JMenuItem queryRunAll;

  /**
   * Menu item - choose whether SQL statements and connection settings are
   * linked
   */
  private JCheckBoxMenuItem configHistoryAssocSQLAndConnect;

  /**
   * Menu item - choose whether to parse SQL statements on semicolons
   */
  private JCheckBoxMenuItem configParseSemicolons;

  /**
   * Menu item - choose whether to save the password in the properties file
   */
  private JCheckBoxMenuItem configSavePassword;

  /**
   * Menu item - choose whether to display the column data type in the results
   * table
   */
  private JCheckBoxMenuItem configDisplayColumnDataType;

  /**
   * Menu item - choose whether to display the client system information in the
   * status text area
   */
  private JCheckBoxMenuItem configDisplayClientInfo;

  /**
   * Menu item - set the font
   */
  private JMenuItem configFont;

  /**
   * Menu item - choose to display server information in brief format
   */
  private JRadioButtonMenuItem configDisplayDBServerInfoShort;

  /**
   * Menu item - choose to display server information in long format
   */
  private JRadioButtonMenuItem configDisplayDBServerInfoLong;

  /**
   * Menu item - choose to not display any server information
   */
  private JRadioButtonMenuItem configDisplayDBServerInfoNone;

  /**
   * Menu item - choose to leave the result table uncolored (white background)
   */
  private JRadioButtonMenuItem configTableColoringNone;

  /**
   * Menu item - choose to color the result table with alternating green/white
   * background
   */
  private JRadioButtonMenuItem configTableColoringGreenBar;

  /**
   * Menu item - choose to color the result table with alternating yellow/white
   * background
   */
  private JRadioButtonMenuItem configTableColoringYellowBar;

  /**
   * Menu item - choose to color the result table with a user defined pattern
   * background
   */
  private JRadioButtonMenuItem configTableColoringUserDefined;

  /**
   * Menu item - choose to use the system default language
   */
  private JRadioButtonMenuItem configLanguageDefault;

  /**
   * Menu item - choose to use English
   */
  private JRadioButtonMenuItem configLanguageEnglish;

  /**
   * Menu item - choose to use Italian
   */
  private JRadioButtonMenuItem configLanguageItalian;

  /**
   * Menu item - choose to use Spanish
   */
  private JRadioButtonMenuItem configLanguageSpanish;

  /**
   * Menu item - choose to use French
   */
  private JRadioButtonMenuItem configLanguageFrench;

  /**
   * Menu item - choose to use German
   */
  private JRadioButtonMenuItem configLanguageGerman;

  /**
   * Menu item - choose to use Portuguese
   */
  private JRadioButtonMenuItem configLanguagePortuguese;

  /**
   * Menu item - display the About dialog box
   */
  private JMenuItem helpAbout;

  /**
   * Menu item - display help on the parameterized SQL statement syntax
   */
  private JMenuItem helpParameterizedSQL;

  /**
   * Database connection
   */
  private Connection conn;

  /**
   * Last connection string used
   */
  private String lastConnection;

  /**
   * Last user id
   */
  private String lastUserId;

  /**
   * Last password
   */
  private String lastPassword;

  /**
   * Run mode (select, update, describe)
   */
  private int runType;

  /**
   * Currently executing query thread
   */
  private Thread runningQuery;

  /**
   * Indicator for executing query
   */
  private JLabel runIndicator;

  /**
   * Elapsed time for running query
   */
  private JLabel timeIndicator;

  /**
   * Thread to animate the query execution indicator
   */
  private Thread flashRunIndicator;

  /**
   * Thread to maintain the query timer display
   */
  private Thread timeRunIndicator;

  /**
   * Directory containing DB driver files
   */
  private String dbDriverDir;

  /**
   * Indicator that a DB driver was loaded
   */
  private boolean loadedDBDriver;

  // Query mode warning patterns, if the SQL begins with an "incorrect"
  // term for the query type selected, a warning message will be displayed
  /**
   * Query mode warning patterns for an update statement. If the SQL begins with
   * an "incorrect" term for the query type selected, a warning message will be
   * displayed
   */
  private String scWarnUpdatePatterns;

  /**
   * Query mode warning patterns for a select statement. If the SQL begins with
   * an "incorrect" term for the query type selected, a warning message will be
   * displayed
   */
  private String scWarnSelectPatterns;

  /**
   * File for exporting results
   */
  private File exportFile;

  /**
   * Latest directory used when choosing a file
   */
  private File latestFileDirectory;

  /**
   * Constructs a Basic Query instance
   * 
   * @param wantGUI
   *          If true, the GUI is presented.
   */
  public BasicQuery(boolean wantGUI) {
    super();

    setupLogger();

    setupLanguage();

    if (wantGUI) {
      addWindowListener(this);
      setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      setTitle("BasicQuery");
      setIconImage(new ImageIcon(ImageUtility.getImageAsByteArray(
          "BasicQueryLogo32x32.gif")).getImage());
      setup();

      checkForNewVersion();
    }
  }

  /**
   * Setup the correct locale based on the user language choice. If the user
   * has chosen default, then the system language defaults are used.
   */
  private void setupLanguage() {
    String temp;

    temp = Configuration.instance().getProperty(PROP_LANGUAGE, LANG_DEFAULT);

    if (temp.equalsIgnoreCase(LANG_DEFAULT)) {
      if (System.getProperty(PROP_SYSTEM_DEFAULTLANGUAGE) != null) {
        if (System.getProperty(PROP_SYSTEM_DEFAULTCOUNTRY) != null) {
          Locale.setDefault(new Locale(System.getProperty(
              PROP_SYSTEM_DEFAULTLANGUAGE),
              System.getProperty(PROP_SYSTEM_DEFAULTCOUNTRY)));
        } else {
          Locale.setDefault(new Locale(System.getProperty(
              PROP_SYSTEM_DEFAULTLANGUAGE)));
        }
      }
    } else if (temp.equalsIgnoreCase(LANG_ENGLISH)) {
      Locale.setDefault(new Locale("en"));
    } else if (temp.equalsIgnoreCase(LANG_FRENCH)) {
      Locale.setDefault(new Locale("fr"));
    } else if (temp.equalsIgnoreCase(LANG_GERMAN)) {
      Locale.setDefault(new Locale("de"));
    } else if (temp.equalsIgnoreCase(LANG_ITALIAN)) {
      Locale.setDefault(new Locale("it"));
    } else if (temp.equalsIgnoreCase(LANG_PORTUGUESE)) {
      Locale.setDefault(new Locale("pt"));
    } else if (temp.equalsIgnoreCase(LANG_SPANISH)) {
      Locale.setDefault(new Locale("es"));
    }
  }

  /**
   * Does a shutdown and startup of the application so that it will
   * redraw its interface.
   */
  private void changeLanguage() {
    saveConfig();
    cleanUp();
    dispose();
    new BasicQuery(true);
  }

  /**
   * Configure the logger
   */
  private void setupLogger() {
    PropertyConfigurator.configure(Configuration.instance().getFile(
        FILENAME_LOGGER).getAbsolutePath());

    if (LOGGER.isDebugEnabled()) {
      final Properties props = System.getProperties();
      final Set<Object> keys = props.keySet();
      final Iterator<Object> keyItr = keys.iterator();

      while (keyItr.hasNext()) {
        final String key = (String) keyItr.next();
        LOGGER.debug("Prop: [" + key + "]=[" + System.getProperty(key) + "]");
      }
    }
  }

  /**
   * Adds packages (JARs) to the classpath. By default it adds any JARs
   * in the database driver directory (set in the properties file) to the
   * classpath.
   * 
   * @return A DynamicClassLoader containing jars and zip-file based classes
   */
  private DynamicClassLoader getDBClassLoader() {
    File dir;
    File[] files;
    List<File> archives;
    DynamicClassLoader dbClassLoader;
    String archivesAdded;

    dir = new File(dbDriverDir);
    messageOut(Resources.getString("msgDBDriverDir", dir.getAbsolutePath()));
    files = dir.listFiles();
    archives = new ArrayList<File>();
    archivesAdded = "";

    for (int f = 0; files != null && f < files.length; ++f) {
      if (files[f].getName().toLowerCase().endsWith(".jar")
          || files[f].getName().toLowerCase().endsWith(".zip")) {
        archives.add(files[f]);
        archivesAdded += files[f].getName() + ", ";
        loadedDBDriver = true;
      }
    }
    if (archivesAdded.length() > 0) {
      archivesAdded = archivesAdded.substring(0, archivesAdded.length() - 2);
    } else {
      archivesAdded = Resources.getString("errNoJARZIP");
    }
    messageOut(Resources.getString("msgDBDriverLocated", archivesAdded));

    dbClassLoader = new DynamicClassLoader(archives);

    return dbClassLoader;
  }

  /**
   * Sets the Border style and color to black for the interface
   * 
   * @return Border A line border of black lines
   */
  private Border getStandardBorder() {
    return BorderFactory.createLineBorder(Color.black);
  }

  /**
   * Setup instance components
   */
  private void setupComponents() {
    table = new JTable();
    cellRenderer = new ColoredCellRenderer();
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    table.setColumnSelectionAllowed(true);
    table.setRowSelectionAllowed(true);
    table.setCellSelectionEnabled(true);
    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    table.setDefaultRenderer(new Object().getClass(), cellRenderer);
    table.getTableHeader().setToolTipText(Resources.getString(
        "tipClickHeaderToSort"));

    maxRows = new JComboBox();
    maxRows.setEditable(false);
    ((DefaultComboBoxModel) maxRows.getModel()).addElement(Resources.getString(
        "proNoLimit"));
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("50");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("100");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("500");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("1000");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("5000");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("10000");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("20000");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("30000");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("50000");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("100000");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("200000");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("500000");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("1000000");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("1500000");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("2000000");
    ((DefaultComboBoxModel) maxRows.getModel()).addElement("3000000");
    maxRows.setSelectedItem("100");

  }

  /**
   * Builds the GUI for the application
   */
  private void setup() {
    JPanel panel;
    JPanel gridPanel;
    JPanel outerPanel;
    JPanel flowPanel;
    JPanel boxedPanel;
    ButtonGroup bGroup;
    MaxHeightJScrollPane maxHeightJScrollPane;

    setupComponents();

    getContentPane().setLayout(new BorderLayout());

    // table.getTableHeader().setFont(new Font(table.getTableHeader().getFont().
    // getName(), table.getTableHeader().getFont().getStyle(),
    // MessageStyleFactory.instance().getFontSize()));
    getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

    panel = new JPanel();
    panel.setLayout(new BorderLayout());

    outerPanel = new JPanel();
    outerPanel.setLayout(new BorderLayout());

    gridPanel = new JPanel();
    gridPanel.setLayout(new GridLayout(0, 1));

    gridPanel.add(connectString = new JComboBox());
    connectString.setEditable(true);
    gridPanel.add(querySelection = new JComboBox());
    querySelection.setEditable(false);
    querySelection.addActionListener(this);
    outerPanel.add(gridPanel, BorderLayout.NORTH);

    outerPanel.add(new JScrollPane(queryText = new JTextArea(QUERY_AREA_ROWS,
        QUERY_AREA_COLUMNS)),
        BorderLayout.SOUTH);
    queryText.setLineWrap(true);
    queryText.setWrapStyleWord(true);
    queryText.addKeyListener(this);

    panel.add(outerPanel, BorderLayout.CENTER);

    outerPanel = new JPanel();
    outerPanel.setLayout(new BorderLayout());

    boxedPanel = new JPanel();
    boxedPanel.setLayout(new GridLayout(0, 2));
    boxedPanel.add(new JLabel(Resources.getString("proUserId")));
    boxedPanel.add(userId = new JTextField(10));
    boxedPanel.add(new JLabel(Resources.getString("proPassword")));
    boxedPanel.add(password = new JPasswordField(10));
    outerPanel.add(boxedPanel, BorderLayout.WEST);

    // Prev/Next and the checkboxes are all on the flowPanel - Center of
    // outerPanel
    flowPanel = new JPanel();
    flowPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

    // Previous/Next buttons
    boxedPanel = new JPanel();
    boxedPanel.setLayout(new FlowLayout());
    boxedPanel.add(previousQuery = new JButton(Resources.getString("ctlPrev"),
        new ImageIcon(ImageUtility.
            getImageAsByteArray("ArrowLeftGreen.gif"))));
    previousQuery.setToolTipText(
        Resources.getString("tipPrev"));
    previousQuery.addActionListener(this);
    boxedPanel.add(nextQuery = new JButton(Resources.getString("ctlNext"),
        new ImageIcon(ImageUtility.
            getImageAsByteArray("ArrowRightGreen.gif"))));
    nextQuery.setToolTipText(Resources.getString("tipNext"));
    nextQuery.addActionListener(this);
    flowPanel.add(boxedPanel);

    // Checkboxes: Autocommit, Read Only and Pooling
    boxedPanel = new JPanel();
    boxedPanel.setLayout(new FlowLayout());
    boxedPanel.setBorder(getStandardBorder());
    boxedPanel.add(autoCommit = new JCheckBox(Resources.getString(
        "ctlAutoCommit"), true));
    boxedPanel.add(readOnly = new JCheckBox(Resources.getString("ctlReadOnly"),
        false));
    boxedPanel.add(poolConnect = new JCheckBox(Resources.getString(
        "ctlConnPool"), false));
    poolConnect.setEnabled(false);
    flowPanel.add(boxedPanel);
    outerPanel.add(flowPanel, BorderLayout.CENTER);

    boxedPanel = new JPanel();
    boxedPanel.setLayout(new GridLayout(0, 1));
    boxedPanel.setBorder(getStandardBorder());
    boxedPanel.add(runIndicator = new JLabel(Resources.getString("ctlRunning"),
        JLabel.CENTER));
    runIndicator.setForeground(Color.lightGray);
    boxedPanel.add(timeIndicator = new JLabel("", JLabel.RIGHT));
    outerPanel.add(boxedPanel, BorderLayout.EAST);

    panel.add(outerPanel, BorderLayout.NORTH);

    flowPanel = new JPanel();
    flowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

    boxedPanel = new JPanel();
    boxedPanel.setLayout(new FlowLayout());
    boxedPanel.setBorder(getStandardBorder());
    boxedPanel.add(new JLabel(Resources.getString("proQueryType")));
    boxedPanel.add(asQuery = new JRadioButton(Resources.getString("ctlSelect"),
        true));
    boxedPanel
        .add(asUpdate = new JRadioButton(Resources.getString("ctlUpdate")));
    boxedPanel.add(asDescribe = new JRadioButton(Resources.getString(
        "ctlDescribe")));
    bGroup = new ButtonGroup();
    bGroup.add(asQuery);
    bGroup.add(asUpdate);
    bGroup.add(asDescribe);
    asQuery.addActionListener(this);
    asUpdate.addActionListener(this);
    asDescribe.addActionListener(this);
    flowPanel.add(boxedPanel);

    flowPanel.add(new JLabel("     "));

    boxedPanel = new JPanel();
    boxedPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    boxedPanel.setBorder(getStandardBorder());
    boxedPanel.add(new JLabel(Resources.getString("proMaxRows")));
    boxedPanel.add(maxRows);
    flowPanel.add(boxedPanel);

    flowPanel.add(new JLabel("     "));

    flowPanel.add(execute = new JButton(Resources.getString("ctlExecute")));
    execute.addActionListener(this);
    flowPanel.add(remove = new JButton(Resources.getString("ctlRemove")));
    remove.addActionListener(this);
    flowPanel
        .add(commentToggle = new JButton(Resources.getString("ctlComment")));
    commentToggle.addActionListener(this);
    flowPanel.add(nextInList = new JButton(Resources.getString("ctlDown")));
    nextInList.addActionListener(this);

    panel.add(flowPanel, BorderLayout.SOUTH);

    getContentPane().add(panel, BorderLayout.NORTH);
    getRootPane().setDefaultButton(execute);

    messageDocument = new DefaultStyledDocument();
    getContentPane().add(maxHeightJScrollPane = new MaxHeightJScrollPane(
        message = new JTextPane(messageDocument)), BorderLayout.SOUTH);
    message.setEditable(false);

    loadedDBDriver = false;

    loadMenu();

    setupTextStyles();
    loadProperties();
    setupUserDefinedColoring();
    setupResultsTableColoring();
    loadConfig();
    loadConnectStrings();
    loadQueries();

    loadDrivers();

    // Check for avail of pool - enable/disable pooling option as appropriate
    // Not really useful until we get the pooling classes out of this code
    try {
      new GenericObjectPool(null);
      poolConnect.setEnabled(true);
      poolConnect.setSelected(true);
    } catch (Throwable any) {
      // No Apache Commons DB Pooling Library Found (DBCP)
      LOGGER.error(
          Resources.getString("errNoPoolLib"),
          any);
    }

    setDefaults();

    maxHeightJScrollPane.lockHeight(getHeight()
        / MAX_SCROLL_PANE_DIVISOR_FOR_MAX_HEIGHT);

    // Font
    setFontFromConfig(Configuration.instance());

    setVisible(true);
  }

  /**
   * Parse the color definition placed in the properties file, if one
   * is provided.
   * 
   * If the user places a legitimate color definition in the properties
   * file, it can be chosen for rendering by using the "USer Defined" option
   * for the result row coloring.
   * 
   * The colors are defined in pairs using the RGB hex values for foreground
   * and background. Multiple pairs can be defined and the colors will
   * alternate between rows. The foreground and background colors are
   * separated by slashes (/) and the pairs are separated by commas (,).
   * 
   * For example:
   * 
   * TABLECOLORUSERDEF=000000/ffffff,000000/c0ffc0
   * 
   * would lead to a "green bar" appearance with black text (000000) on
   * each row and alternating white (ffffff) and green (c0ffc0) backgrounds.
   * 
   * Another example:
   * 
   * TABLECOLORUSERDEF=000000/ffc0c0,000000/ffffff,000000/c0c0ff,000000/ffffff
   * 
   * would lead to a red, white and blue theme with black text on alternating
   * red, white, blue and again white backgrounds.
   */
  private void setupUserDefinedColoring() {
    String temp;
    String[] groups, colors;
    if (configTableColoringUserDefined.isSelected()
        && userDefTableColoring != null) {
      temp = userDefTableColoring.trim();
      if (temp.length() > 0) {
        groups = temp.split(",");

        LOGGER.debug("User def color group count=" + groups.length);

        for (int group = 0; group < groups.length; ++group) {
          LOGGER.debug("Group[" + group + "] = [" + groups[group] + "]");
          colors = groups[group].split("/");
          LOGGER.debug("User def color count=" + colors.length);
          if (colors.length == USER_DEFINED_COLORING_COLORS_PER_ENTRY
              && colors[0].length() == USER_DEFINED_COLORING_COLOR_CODE_LENGTH
              && colors[1].length() == USER_DEFINED_COLORING_COLOR_CODE_LENGTH) {
            addDisplayRowColor(colors[0], colors[1]);
            configTableColoringUserDefined.setEnabled(true);
          } else {
            LOGGER.warn("Unable to parse the user-defined color pattern: "
                + groups[group]);
            messageOut(Resources.getString("errUserDefinedColorErrorParse",
                groups[group]), STYLE_RED);
          }
        }
      }
    }
  }

  /**
   * Does the actual parsing of foreground and background colors. Colors
   * are supplied as 6 digit (hex) strings (e.g. 000000=black, ffffff=white)
   * 
   * @param foregroundColor
   *          String The foregound color as RGB hex
   * @param backgroundColor
   *          String The background color as RGB hex
   */
  private void addDisplayRowColor(String foregroundColor,
      String backgroundColor) {
    LOGGER.debug("Add user defined display color row FG:" + foregroundColor
        + " BG:" + backgroundColor);

    try {
      cellRenderer.addAlternatingRowColor(
          new Color(Integer.parseInt(
              foregroundColor.substring(COLOR_CODING_COLOR_1_START,
                  COLOR_CODING_COLOR_2_START), COLOR_CODING_NUMERIC_BASE),
              Integer.parseInt(
                  foregroundColor.
                      substring(COLOR_CODING_COLOR_2_START,
                          COLOR_CODING_COLOR_3_START),
                  COLOR_CODING_NUMERIC_BASE),
              Integer.parseInt(
                  foregroundColor.substring(COLOR_CODING_COLOR_3_START,
                      COLOR_CODING_COLOR_3_END), COLOR_CODING_NUMERIC_BASE)),
          new Color(Integer.parseInt(
              backgroundColor.substring(COLOR_CODING_COLOR_1_START,
                  COLOR_CODING_COLOR_2_START), COLOR_CODING_NUMERIC_BASE),
              Integer.parseInt(backgroundColor.
                  substring(COLOR_CODING_COLOR_2_START,
                      COLOR_CODING_COLOR_3_START), COLOR_CODING_NUMERIC_BASE),
              Integer.parseInt(backgroundColor.substring(
                  COLOR_CODING_COLOR_3_START, COLOR_CODING_COLOR_3_END),
                  COLOR_CODING_NUMERIC_BASE))
          );
    } catch (Throwable any) {
      // Probably a bad hex value
      LOGGER.warn("Error setting row coloring for FG(" + foregroundColor
          + ") BG(" + backgroundColor + ")");
      messageOut(Resources.getString("errUserDefinedColorError",
          foregroundColor, backgroundColor, any.getMessage()), STYLE_RED);
    }
  }

  /**
   * Sets-up the collection of text styles that can be used to format the
   * information in the message area.
   */
  private void setupTextStyles() {
    messageStyles = new HashMap<String, AttributeSet>();

    messageStyles.put(STYLE_BOLD,
        MessageStyleFactory.instance().createStyle(Color.black,
            MessageStyleFactory.BOLD));
    messageStyles.put(STYLE_BOLD_UL,
        MessageStyleFactory.instance().createStyle(Color.black,
            MessageStyleFactory.BOLD | MessageStyleFactory.UNDERLINE));
    messageStyles.put(STYLE_GREEN,
        MessageStyleFactory.instance().createStyle(Color.green,
            MessageStyleFactory.BOLD));
    messageStyles.put(STYLE_NORMAL,
        MessageStyleFactory.instance().createStyle(Color.black));
    messageStyles.put(STYLE_RED,
        MessageStyleFactory.instance().createStyle(Color.red,
            MessageStyleFactory.BOLD));
    messageStyles.put(STYLE_SUBTLE,
        MessageStyleFactory.instance().createStyle(Color.blue.
            brighter(), MessageStyleFactory.ITALIC));
    messageStyles.put(STYLE_SUBTLE_UL,
        MessageStyleFactory.instance().
            createStyle(Color.blue.brighter(),
                MessageStyleFactory.ITALIC
                    | MessageStyleFactory.UNDERLINE));
    messageStyles.put(STYLE_YELLOW,
        MessageStyleFactory.instance().createStyle(Color.black,
            Color.yellow, MessageStyleFactory.BOLD));
  }

  /**
   * Saves the current settings into the properties file.
   */
  private void saveProperties() {
    Configuration props;

    // props = new Properties();
    props = Configuration.instance();

    props.setProperty(PROP_USERID, userId.getText());

    if (configSavePassword.isSelected()) {
      props.setProperty(PROP_PASSWORD, new String(password.getPassword()));
    }

    props.setProperty(PROP_AUTOCOMMIT,
        autoCommit.isSelected() ? PARAMVALUE_YES : PARAMVALUE_NO);
    props.setProperty(PROP_READONLY,
        readOnly.isSelected() ? PARAMVALUE_YES : PARAMVALUE_NO);
    props.setProperty(PROP_POOLING,
        poolConnect.isSelected() ? PARAMVALUE_YES : PARAMVALUE_NO);
    props.setProperty(PROP_CONNECTION, connectString.getSelectedIndex() + "");
    props.setProperty(PROP_SQL, querySelection.getSelectedIndex() + "");
    props.setProperty(PROP_SQLFILENAME, queryFilename);
    props.setProperty(PROP_MAXROWS, maxRows.getSelectedIndex() + "");
    props.setProperty(PROP_LOGSTATS,
        fileLogStats.isSelected() ? PARAMVALUE_YES : PARAMVALUE_NO);
    props.setProperty(PROP_LOGRESULTS,
        fileLogResults.isSelected() ? PARAMVALUE_YES : PARAMVALUE_NO);
    props.setProperty(PROP_EXPORTRAW,
        fileExportsRaw.isSelected() ? PARAMVALUE_YES : PARAMVALUE_NO);
    props
        .setProperty(PROP_EXPORTNOCR,
            fileNoCRAddedToExportRows.isSelected() ? PARAMVALUE_YES
                : PARAMVALUE_NO);
    props.setProperty(PROP_SAVEPASSWORD,
        configSavePassword.isSelected() ? PARAMVALUE_YES : PARAMVALUE_NO);
    props.setProperty(PROP_ASSOCSQLURL,
        configHistoryAssocSQLAndConnect.isSelected() ? PARAMVALUE_YES
            : PARAMVALUE_NO);
    props.setProperty(PROP_PARSESEMICOLON,
        configParseSemicolons.isSelected() ? PARAMVALUE_YES
            : PARAMVALUE_NO);
    props.setProperty(PROP_DISPLAYCOLUMNDATATYPE,
        configDisplayColumnDataType.isSelected() ? PARAMVALUE_YES
            : PARAMVALUE_NO);
    props.setProperty(PROP_DISPLAYCLIENTINFO,
        configDisplayClientInfo.isSelected() ? PARAMVALUE_YES
            : PARAMVALUE_NO);
    props.setProperty(PROP_DISPLAYDBSERVERINFO,
        configDisplayDBServerInfoLong.isSelected() ? DBSERVERINFO_LONG
            : configDisplayDBServerInfoShort.isSelected() ? DBSERVERINFO_BRIEF
                : PARAMVALUE_NO);
    props.setProperty(PROP_UPPERLEFTX, getLocation().x + "");
    props.setProperty(PROP_UPPERLEFTY, getLocation().y + "");
    props.setProperty(PROP_WIDTH, getSize().width + "");
    props.setProperty(PROP_HEIGHT, getSize().height + "");
    props.setProperty(PROP_MAXIMIZED,
        getExtendedState() == JFrame.MAXIMIZED_BOTH ? PARAMVALUE_YES
            : PARAMVALUE_NO);
    props.setProperty(PROP_DBDRIVERDIR, dbDriverDir);

    // Add the select and update query type warning patterns to the properties.
    // Technically don't need to do this since the user can't change in app,
    // but want to be sure they are in the property file so the user has a
    // clue that they can be modified.
    props.setProperty(PROP_WARNSELECTPATTERNS, scWarnSelectPatterns);
    props.setProperty(PROP_WARNUPDATEPATTERNS, scWarnUpdatePatterns);

    // Data Row Coloring
    props
        .setProperty(
            PROP_TABLE_COLORING,
            configTableColoringGreenBar.isSelected() ? TABLECOLORING_GREENBAR
                :
                configTableColoringYellowBar.isSelected() ? TABLECOLORING_YELLOWBAR
                    :
                    configTableColoringUserDefined.isSelected() ? TABLECOLORING_USERDEFINED
                        :
                        TABLECOLORING_NONE);

    if (userDefTableColoring != null) {
      props.setProperty(PROP_TABLE_COLOR_USERDEF, userDefTableColoring);
    }

    // Language
    props
        .setProperty(
            PROP_LANGUAGE,
            configLanguageDefault.isSelected() ? LANG_DEFAULT
                :
                configLanguageEnglish.isSelected() ? LANG_ENGLISH
                    :
                    configLanguageFrench.isSelected() ? LANG_FRENCH
                        :
                        configLanguageGerman.isSelected() ? LANG_GERMAN
                            :
                            configLanguageItalian.isSelected() ? LANG_ITALIAN
                                :
                                configLanguagePortuguese.isSelected() ? LANG_PORTUGUESE
                                    :
                                    configLanguageSpanish.isSelected() ? LANG_SPANISH
                                        :
                                        LANG_DEFAULT);

    // Font
    props.setProperty(PROP_FONT_FAMILY, getFont().getFamily());
    props.setProperty(PROP_FONT_SIZE, getFont().getSize() + "");
    props.setProperty(PROP_FONT_BOLD, getFont().isBold() ? "YES" : "NO");
    props.setProperty(PROP_FONT_ITALIC, getFont().isItalic() ? "YES" : "NO");

    // File access
    if (latestFileDirectory != null) {
      if (latestFileDirectory.isDirectory()) {
        props.setProperty(PROP_LATEST_DIRECTORY,
            latestFileDirectory.getAbsolutePath());
      } else {
        props.setProperty(PROP_LATEST_DIRECTORY, latestFileDirectory
            .getParentFile().getAbsolutePath());
      }
    }
    // Write out the properties to the file

    props.store();
  }

  /**
   * Sets the configuration based on the values in the configuration file.
   */
  private void loadProperties() {
    Properties props;
    String temp;

    // props = new Properties();
    props = Configuration.instance();

    userId.setText(props.getProperty(PROP_USERID, ""));
    password.setText(props.getProperty(PROP_PASSWORD, ""));

    if (props.getProperty(PROP_AUTOCOMMIT, PARAMVALUE_NO)
        .equals(PARAMVALUE_YES)) {
      autoCommit.setSelected(true);
    } else {
      autoCommit.setSelected(false);
    }

    if (props.getProperty(PROP_READONLY, PARAMVALUE_NO).equals(PARAMVALUE_YES)) {
      readOnly.setSelected(true);
    } else {
      readOnly.setSelected(false);
    }

    setQueryFilename(props.getProperty(PROP_SQLFILENAME,
        Configuration.instance().getFile(FILENAME_DEFAULTQUERIES).
            getAbsolutePath()));
    fileLogStats.setSelected(props.getProperty(PROP_LOGSTATS,
        PARAMVALUE_NO).equals(PARAMVALUE_YES));
    fileLogResults.setSelected(props.getProperty(PROP_LOGRESULTS,
        PARAMVALUE_NO).equals(PARAMVALUE_YES));
    fileExportsRaw.setSelected(props.getProperty(PROP_EXPORTRAW,
        PARAMVALUE_NO).equals(PARAMVALUE_YES));
    fileNoCRAddedToExportRows.setSelected(props.getProperty(PROP_EXPORTNOCR,
        PARAMVALUE_NO).equals(PARAMVALUE_YES));
    configSavePassword.setSelected(props.getProperty(PROP_SAVEPASSWORD,
        PARAMVALUE_NO).
        equals(PARAMVALUE_YES));
    configHistoryAssocSQLAndConnect.setSelected(props.getProperty(
        PROP_ASSOCSQLURL, PARAMVALUE_NO).equals(PARAMVALUE_YES));
    configParseSemicolons.setSelected(props.getProperty(PROP_PARSESEMICOLON,
        PARAMVALUE_NO).equals(PARAMVALUE_YES));
    configDisplayColumnDataType.setSelected(props.getProperty(
        PROP_DISPLAYCOLUMNDATATYPE, PARAMVALUE_NO).equals(PARAMVALUE_YES));
    configDisplayClientInfo.setSelected(props.getProperty(
        PROP_DISPLAYCLIENTINFO, PARAMVALUE_NO).equals(PARAMVALUE_YES));

    if (props.getProperty(PROP_DISPLAYDBSERVERINFO,
        PARAMVALUE_NO).equals(DBSERVERINFO_LONG)) {
      configDisplayDBServerInfoLong.setSelected(true);
    } else if (props.getProperty(PROP_DISPLAYDBSERVERINFO,
        PARAMVALUE_NO).equals(DBSERVERINFO_BRIEF)) {
      configDisplayDBServerInfoShort.setSelected(true);
    } else {
      configDisplayDBServerInfoNone.setSelected(true);
    }

    // Results table row coloring
    if (props.getProperty(PROP_TABLE_COLORING,
        TABLECOLORING_NONE).equals(TABLECOLORING_GREENBAR)) {
      configTableColoringGreenBar.setSelected(true);
    } else if (props.getProperty(PROP_TABLE_COLORING,
        TABLECOLORING_NONE).equals(TABLECOLORING_YELLOWBAR)) {
      configTableColoringYellowBar.setSelected(true);
    } else if (props.getProperty(PROP_TABLE_COLORING,
        TABLECOLORING_NONE).equals(TABLECOLORING_USERDEFINED)) {
      configTableColoringUserDefined.setSelected(true);
    } else {
      configTableColoringNone.setSelected(true);
    }

    // Language
    temp = props.getProperty(PROP_LANGUAGE, LANG_DEFAULT);
    if (temp.equalsIgnoreCase(LANG_DEFAULT)) {
      configLanguageDefault.setSelected(true);
    } else if (temp.equalsIgnoreCase(LANG_ENGLISH)) {
      configLanguageEnglish.setSelected(true);
    } else if (temp.equalsIgnoreCase(LANG_FRENCH)) {
      configLanguageFrench.setSelected(true);
    } else if (temp.equalsIgnoreCase(LANG_GERMAN)) {
      configLanguageGerman.setSelected(true);
    } else if (temp.equalsIgnoreCase(LANG_ITALIAN)) {
      configLanguageItalian.setSelected(true);
    } else if (temp.equalsIgnoreCase(LANG_PORTUGUESE)) {
      configLanguagePortuguese.setSelected(true);
    } else if (temp.equalsIgnoreCase(LANG_SPANISH)) {
      configLanguageSpanish.setSelected(true);
    } else {
      configLanguageDefault.setSelected(true);
    }

    // Frame's window state (maximized/normal)
    setExtendedState(props.getProperty(PROP_MAXIMIZED,
        PARAMVALUE_NO).equals(PARAMVALUE_YES)
        ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);

    if (!props.getProperty(PROP_MAXIMIZED, PARAMVALUE_NO)
        .equals(PARAMVALUE_YES)) {
      try {
        setLocation(Integer.parseInt(props.getProperty(PROP_UPPERLEFTX,
            "0")),
            Integer.parseInt(props.getProperty(PROP_UPPERLEFTY,
                "0")));
        setSize(Integer.parseInt(props.getProperty(PROP_WIDTH, "800")),
            Integer.parseInt(props.getProperty(PROP_HEIGHT, "600")));
      } catch (Throwable any) {
        LOGGER.info("Missing property?", any);
      }
    }

    userDefTableColoring = props.getProperty(PROP_TABLE_COLOR_USERDEF);

    dbDriverDir = props.getProperty(PROP_DBDRIVERDIR, DEFAULT_DBDRIVERDIR);

    // Query type versus SQL warning patterns
    scWarnSelectPatterns = props.getProperty(PROP_WARNSELECTPATTERNS,
        DEFAULTWARNSELECTPATTERNS);
    scWarnUpdatePatterns = props.getProperty(PROP_WARNUPDATEPATTERNS,
        DEFAULTWARNUPDATEPATTERNS);

    // File access
    temp = props.getProperty(PROP_LATEST_DIRECTORY);
    if (temp != null && temp.trim().length() > 0) {
      try {
        latestFileDirectory = new File(temp);
      } catch (Throwable throwable) {
        LOGGER.warn("Unable to set the default file directory to: " + temp,
            throwable);
      }
    }
  }

  /**
   * Set the fonts for the interface from the configuration in the properties
   * collection
   * 
   * @param props
   *          The properties collection with font information
   */
  private void setFontFromConfig(Properties props) {
    String fontFamily;
    int fontSize, fontStyle;
    boolean bold, italic;

    try {
      fontFamily = props.getProperty(PROP_FONT_FAMILY, "Arial");
      fontSize = Integer.parseInt(props.getProperty(PROP_FONT_SIZE, "12"));
      bold = props.getProperty(PROP_FONT_BOLD, "NO").equals("YES");
      italic = props.getProperty(PROP_FONT_ITALIC, "NO").equals("YES");

      fontStyle = 0;
      if (bold) {
        fontStyle |= Font.BOLD;
      }

      if (italic) {
        fontStyle |= Font.ITALIC;
      }

      setupFont(new Font(fontFamily, fontStyle, fontSize));
    } catch (Throwable any) {
      // Probably font is not configured in property file - just ignore
      // and use system default font
      LOGGER.info("Failed to setup a font from the properties file", any);
    }
  }

  /**
   * Set default values at startup.
   */
  private void setDefaults() {
    Properties props;

    // props = new Properties();
    props = Configuration.instance();

    if (props.getProperty(PROP_POOLING, PARAMVALUE_YES).equals(PARAMVALUE_YES)) {
      poolConnect.setSelected(true);
    } else {
      poolConnect.setSelected(false);
    }

    try {
      connectString.setSelectedIndex(Integer.parseInt(props.getProperty(
          PROP_CONNECTION)));
    } catch (Throwable any) {
      // Probably the list changed
      LOGGER.info("Value not on list", any);
    }

    try {
      querySelection.setSelectedIndex(Integer.parseInt(props.getProperty(
          PROP_SQL)));
    } catch (Throwable any) {
      // Probably the list changed
      LOGGER.info("Value not on list", any);
    }

    try {
      maxRows
          .setSelectedIndex(Integer.parseInt(props.getProperty(PROP_MAXROWS)));
    } catch (Throwable any) {
      // Probably the list changed
      LOGGER.info("Value not on list", any);
    }

    // No queries executed yet, disable prev/next buttons
    // TODO is this working?
    setPrevNextIndication();
    // previousQuery.setEnabled(false);
    // nextQuery.setEnabled(false);
  }

  /**
   * Sets the database connection pool.
   * 
   * @param connectURI
   *          The url specifying the database to which it connects
   * @param pUserId
   *          The user id field
   * @param pPassword
   *          The password field
   */
  private void setupDBPool(String connectURI, String pUserId, String pPassword) {
    removeDBPool();

    try {
      final GenericObjectPool connectionPool = new GenericObjectPool(null);

      configurePool(connectionPool, connectURI, pUserId, pPassword);

      final PoolingDriver driver = new PoolingDriver();
      driver.registerPool(DBPOOL_NAME, connectionPool);
      LOGGER.info("DB Connection Pool setup [" + DBPOOL_NAME + "]");
    } catch (Throwable any) {
      LOGGER.error("Unable to setup database connection pool", any);
      messageOut(Resources.getString("errFailSetupDBPool", any.getMessage()),
          STYLE_RED);
    }
  }

  /**
   * Configures the database connection pool and sets the pool configuration.
   * 
   * @param connectionPool
   *          The ObjectPool
   * @param connectURI
   *          The url specifying the database to which it
   *          connects using JDBC
   * @param pUserId
   *          The user id attribute
   * @param pPassword
   *          The password attribute
   * 
   * @throws java.lang.Exception
   *           Throws an SQL exception that provides
   *           information on a database access error
   *           or other errors
   * 
   * @todo Make the pool access dynamic (use the dynamic class loader?)
   *       so that the application will compile/run without the Apache
   *       commons library.
   */
  private void configurePool(GenericObjectPool connectionPool,
      String connectURI, String pUserId, String pPassword) throws
      Exception {
    String lowerCaseConnectURI;
    String validationQuery;

    lowerCaseConnectURI = connectURI.toLowerCase();
    validationQuery = null;

    if (lowerCaseConnectURI.startsWith("jdbc:sybase")) {
      validationQuery = "select getdate()";
    } else if (lowerCaseConnectURI.startsWith("jdbc:mysql")) {
      validationQuery = "select 1";
    } else if (lowerCaseConnectURI.startsWith("jdbc:oracle")) {
      validationQuery = "select sysdate from dual";
    } else if (lowerCaseConnectURI.startsWith("jdbc:mssql")) {
      validationQuery = "select 1";
    }

    // Pool settings - someday a dialog and persistence should be
    // added to put these under user control
    connectionPool.setMaxActive(1);
    connectionPool.setWhenExhaustedAction(GenericObjectPool.
        WHEN_EXHAUSTED_BLOCK);
    connectionPool.setMaxWait(CONN_POOL_MAX_WAIT_MS);
    connectionPool.setMaxIdle(CONN_POOL_MAX_IDLE_CONNECTIONS);
    connectionPool
        .setTimeBetweenEvictionRunsMillis(CONN_POOL_TIME_BETWEEN_EVICT_RUNS_MS);
    connectionPool.setNumTestsPerEvictionRun(CONN_POOL_NUM_TESTS_PER_EVICT_RUN);
    connectionPool.setMinEvictableIdleTimeMillis(CONN_POOL_EVICT_IDLE_TIME_MS);

    final DriverManagerConnectionFactory connectionFactory = new
        DriverManagerConnectionFactory(connectURI, pUserId, pPassword);
    final PoolableConnectionFactory poolableConnectionFactory = new
        PoolableConnectionFactory(connectionFactory, connectionPool, null,
            null, false, true);

    if (validationQuery != null) {
      connectionPool.setTestOnBorrow(true);
      connectionPool.setTestWhileIdle(true);
      poolableConnectionFactory.setValidationQuery(validationQuery);
    }
  }

  /**
   * Closes the database connection pool
   */
  private void removeDBPool() {
    final ObjectPool dbpool = getDBPool();

    if (dbpool != null) {
      try {
        dbpool.close();
      } catch (Throwable any) {
        LOGGER.error("Unable to close the database connection pool", any);
        messageOut(Resources.getString("errFailCloseDBPool", any.getMessage()),
            STYLE_RED);
      }
    }
  }

  /**
   * Gets the driver for the database pool
   * 
   * @return ObjectPool Returns the driver object for the specific
   *         pool name
   */
  private ObjectPool getDBPool() {
    try {
      return new PoolingDriver().getConnectionPool(DBPOOL_NAME);
    } catch (SQLException excSQL) {
      LOGGER.error("Unable to load DB Pool", excSQL);
      return null;
    }
  }

  /**
   * File menu setup
   * 
   * @return The file menu
   */
  private JMenu fileMenu() {
    JMenu menu;

    // File Menu
    menu = new JMenu(Resources.getString("mnuFileLabel"));
    menu.setMnemonic(Resources.getChar("mnuFileAccel"));
    menu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuFileDesc"));

    // File | Open SQL File
    fileOpenSQL = new JMenuItem(Resources.getString("mnuFileOpenLabel"));
    fileOpenSQL.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuFileOpenAccel"),
        ActionEvent.ALT_MASK));
    fileOpenSQL.setMnemonic(Resources.getChar("mnuFileOpenAccel"));
    fileOpenSQL.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuFileOpenDesc"));
    fileOpenSQL.addActionListener(this);
    fileOpenSQL.setEnabled(true);
    menu.add(fileOpenSQL);

    menu.addSeparator();

    // File | Log Stats
    fileLogStats = new JCheckBoxMenuItem(Resources.getString(
        "mnuFileLogStatsLabel"));
    fileLogStats.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuFileLogStatsAccel"),
        ActionEvent.ALT_MASK));
    fileLogStats.setMnemonic(Resources.getChar("mnuFileLogStatsAccel"));
    fileLogStats.getAccessibleContext().setAccessibleDescription(
        Resources.getString("mnuFileLogStatsAccel", DBSTATS_NAME));
    fileLogStats.setEnabled(true);
    fileLogStats.setSelected(false);
    menu.add(fileLogStats);

    // File | Log Results
    fileLogResults = new JCheckBoxMenuItem(Resources.getString(
        "mnuFileLogResultsLabel"));
    fileLogResults.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuFileLogResultsAccel"),
        ActionEvent.ALT_MASK));
    fileLogResults.setMnemonic(Resources.getString("mnuFileLogResultsAccel").
        charAt(0));
    fileLogResults.getAccessibleContext().setAccessibleDescription(
        Resources.getString("mnuFileLogResultsDesc", DBRESULTS_NAME));
    fileLogResults.setEnabled(true);
    fileLogResults.setSelected(false);
    menu.add(fileLogResults);

    menu.addSeparator();

    // File | Export Results As CSV
    fileSaveAsCSV = new JMenuItem(Resources.getString("mnuFileExportCSVLabel"));
    fileSaveAsCSV.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuFileExportCSVAccel"),
        ActionEvent.ALT_MASK));
    fileSaveAsCSV.setMnemonic(Resources.getChar("mnuFileExportCSVAccel"));
    fileSaveAsCSV.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuFileExportCSVDesc"));
    fileSaveAsCSV.addActionListener(this);
    fileSaveAsCSV.setEnabled(false);
    menu.add(fileSaveAsCSV);

    // File | Export Results As Triples
    fileSaveAsTriples = new JMenuItem(
        Resources.getString("mnuFileExportTriplesLabel"));
    fileSaveAsTriples.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuFileExportTriplesAccel"),
        ActionEvent.ALT_MASK));
    fileSaveAsTriples.setMnemonic(Resources
        .getChar("mnuFileExportTriplesAccel"));
    fileSaveAsTriples.getAccessibleContext().setAccessibleDescription(
        Resources.
            getString("mnuFileExportTriplesDesc"));
    fileSaveAsTriples.addActionListener(new ExportResultsAsTriplesListener());
    fileSaveAsTriples.setEnabled(false);
    menu.add(fileSaveAsTriples);

    // File | Save BLOBs
    fileSaveBLOBs = new JMenuItem(Resources.getString("mnuFileSaveBLOBLabel"));
    fileSaveBLOBs.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuFileSaveBLOBAccel"),
        ActionEvent.ALT_MASK));
    fileSaveBLOBs.setMnemonic(Resources.getChar("mnuFileSaveBLOBAccel"));
    fileSaveBLOBs.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuFileSaveBLOBDesc"));
    fileSaveBLOBs.addActionListener(this);
    fileSaveBLOBs.setEnabled(false);
    menu.add(fileSaveBLOBs);

    menu.addSeparator();

    // File | Raw Export
    fileExportsRaw = new JCheckBoxMenuItem(Resources.getString(
        "mnuFileRawExportLabel"));
    fileExportsRaw.setMnemonic(Resources.getChar("mnuFileRawExportAccel"));
    fileExportsRaw.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuFileRawExportDesc"));
    fileExportsRaw.setEnabled(true);
    fileExportsRaw.setSelected(false);
    menu.add(fileExportsRaw);

    // File | No CR Added to Export
    fileNoCRAddedToExportRows = new JCheckBoxMenuItem(Resources.getString(
        "mnuFileNoCRLabel"));
    fileNoCRAddedToExportRows
        .setMnemonic(Resources.getChar("mnuFileNoCRAccel"));
    fileNoCRAddedToExportRows.getAccessibleContext().setAccessibleDescription(
        Resources.getString("mnuFileNoCRDesc"));
    fileNoCRAddedToExportRows.setEnabled(true);
    fileNoCRAddedToExportRows.setSelected(false);
    menu.add(fileNoCRAddedToExportRows);

    menu.addSeparator();

    fileExit = new JMenuItem(Resources.getString("mnuFileExitLabel"));
    fileExit.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuFileExitAccel"),
        ActionEvent.ALT_MASK));
    fileExit.setMnemonic(Resources.getChar("mnuFileExitAccel"));
    fileExit.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuFileExitDesc"));
    fileExit.addActionListener(this);
    fileExit.setEnabled(true);
    menu.add(fileExit);

    return menu;
  }

  /**
   * Edit menu setup
   * 
   * @return The edit menu
   */
  private JMenu editMenu() {
    JMenu menu;

    // Edit Menu
    menu = new JMenu(Resources.getString("mnuEditLabel"));
    menu.setMnemonic(Resources.getChar("mnuEditAccel"));
    menu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuEditDesc"));

    // Edit | Copy
    editCopy = new JMenuItem(Resources.getString("mnuEditCopyLabel"));
    editCopy.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuEditCopyAccel"),
        ActionEvent.ALT_MASK));
    editCopy.setMnemonic(Resources.getChar("mnuEditCopyAccel"));
    editCopy.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuEditCopyDesc"));
    editCopy.addActionListener(this);
    editCopy.setEnabled(true);
    menu.add(editCopy);

    // Edit | Select All
    editSelectAll = new JMenuItem(Resources.getString("mnuEditSelectAllLabel"));
    editSelectAll.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuEditSelectAllAccel"),
        ActionEvent.ALT_MASK));
    editSelectAll.setMnemonic(Resources.getChar("mnuEditSelectAllAccel"));
    editSelectAll.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuEditSelectAllDesc"));
    editSelectAll.addActionListener(this);
    editSelectAll.setEnabled(true);
    menu.add(editSelectAll);

    menu.addSeparator();

    // Edit | Sort by Selected Columns
    editSort = new JMenuItem(Resources.getString("mnuEditSortLabel"));
    editSort.setMnemonic(Resources.getChar("mnuEditSortAccel"));
    editSort.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuEditSortDesc"));
    editSort.addActionListener(this);
    editSort.setEnabled(true);
    menu.add(editSort);

    return menu;
  }

  /**
   * Query menu set
   * 
   * @return The query menu
   */
  private JMenu queryMenu() {
    JMenu menu;

    // Query Menu
    menu = new JMenu(Resources.getString("mnuQueryLabel"));
    menu.setMnemonic(Resources.getChar("mnuQueryAccel"));
    menu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuQueryDesc"));

    // Query | Select Statement
    queryMakeVerboseSelect = new JMenuItem(Resources.getString(
        "mnuQuerySelectLabel"));
    queryMakeVerboseSelect.setAccelerator(KeyStroke.getKeyStroke(Resources.
        getChar("mnuQuerySelectAccel"),
        ActionEvent.ALT_MASK));
    queryMakeVerboseSelect
        .setMnemonic(Resources.getChar("mnuQuerySelectAccel"));
    queryMakeVerboseSelect.getAccessibleContext().setAccessibleDescription(
        Resources.getString("mnuQuerySelectDesc"));
    queryMakeVerboseSelect.addActionListener(this);
    queryMakeVerboseSelect.setEnabled(true);
    menu.add(queryMakeVerboseSelect);

    // Query | Insert Statement
    queryMakeInsert = new JMenuItem(Resources.getString("mnuQueryInsertLabel"));
    queryMakeInsert.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuQueryInsertAccel"),
        ActionEvent.ALT_MASK));
    queryMakeInsert.setMnemonic(Resources.getChar("mnuQueryInsertAccel"));
    queryMakeInsert.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuQueryInsertDesc"));
    queryMakeInsert.addActionListener(this);
    queryMakeInsert.setEnabled(true);
    menu.add(queryMakeInsert);

    // Query | Update Statement
    queryMakeUpdate = new JMenuItem(Resources.getString("mnuQueryUpdateLabel"));
    queryMakeUpdate.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuQueryUpdateAccel"),
        ActionEvent.ALT_MASK));
    queryMakeUpdate.setMnemonic(Resources.getChar("mnuQueryUpdateAccel"));
    queryMakeUpdate.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuQueryUpdateDesc"));
    queryMakeUpdate.addActionListener(this);
    queryMakeUpdate.setEnabled(true);
    menu.add(queryMakeUpdate);

    menu.addSeparator();

    // Query | Select *
    querySelectStar = new JMenuItem(Resources.getString(
        "mnuQuerySelectStarLabel"));
    querySelectStar.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuQuerySelectStarAccel"),
        ActionEvent.ALT_MASK));
    querySelectStar.setMnemonic(Resources.getChar("mnuQuerySelectStarAccel"));
    querySelectStar.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuQuerySelectStarDesc"));
    querySelectStar.addActionListener(this);
    querySelectStar.setEnabled(true);
    menu.add(querySelectStar);

    // Query | Describe Select *
    queryDescribeStar = new JMenuItem(Resources.getString(
        "mnuQueryDescSelectStarLabel"));
    queryDescribeStar.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuQueryDescSelectStarAccel"),
        ActionEvent.ALT_MASK));
    queryDescribeStar.setMnemonic(Resources.getChar(
        "mnuQueryDescSelectStarAccel"));
    queryDescribeStar.getAccessibleContext().setAccessibleDescription(
        Resources.
            getString("mnuQueryDescSelectStarDesc"));
    queryDescribeStar.addActionListener(this);
    queryDescribeStar.setEnabled(true);
    menu.add(queryDescribeStar);

    menu.addSeparator();

    // Query | Reorder Queries
    querySetOrder = new JMenuItem(Resources.getString("mnuQueryReorderLabel"));
    querySetOrder.setMnemonic(Resources.getChar("mnuQueryReorderAccel"));
    querySetOrder.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuQueryReorderDesc"));
    querySetOrder.addActionListener(this);
    querySetOrder.setEnabled(true);
    menu.add(querySetOrder);

    menu.addSeparator();

    // Query | Run All
    queryRunAll = new JMenuItem(Resources.getString("mnuQueryRunAllLabel"));
    queryRunAll.setAccelerator(KeyStroke.getKeyStroke(Resources.getChar(
        "mnuQueryRunAllAccel"),
        ActionEvent.ALT_MASK));
    queryRunAll.setMnemonic(Resources.getChar("mnuQueryRunAllAccel"));
    queryRunAll.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuQueryRunAllDesc"));
    queryRunAll.addActionListener(this);
    queryRunAll.setEnabled(true);
    menu.add(queryRunAll);

    return menu;
  }

  /**
   * Language selection menu
   * 
   * @return The language selection menu
   */
  private JMenu languageMenu() {
    JMenu subMenu;
    ButtonGroup buttonGroup;

    // Setup | Language
    subMenu = new JMenu(Resources.getString("mnuSetupLanguageLabel"));
    subMenu.setMnemonic(Resources.getChar("mnuSetupLanguageAccel"));
    subMenu.getAccessibleContext().setAccessibleDescription(
        Resources.getString(
            "mnuSetupLanguageDesc"));

    // Setup | Language | System Default
    if (System.getProperty(PROP_SYSTEM_DEFAULTLANGUAGE) != null) {
      if (System.getProperty(PROP_SYSTEM_DEFAULTCOUNTRY) != null) {
        configLanguageDefault = new JRadioButtonMenuItem(
            Resources.getString("mnuSetupLanguageDefaultLabel") + " ("
                + System.getProperty(PROP_SYSTEM_DEFAULTLANGUAGE) + "_"
                + System.getProperty(PROP_SYSTEM_DEFAULTCOUNTRY) + ")");
      } else {
        configLanguageDefault = new JRadioButtonMenuItem(
            Resources.getString("mnuSetupLanguageDefaultLabel") + " ("
                + System.getProperty(PROP_SYSTEM_DEFAULTLANGUAGE) + ")");
      }
    } else {
      configLanguageDefault = new JRadioButtonMenuItem(
          Resources.getString("mnuSetupLanguageDefaultLabel"));
    }
    configLanguageDefault.setMnemonic(Resources.getChar(
        "mnuSetupLanguageDefaultAccel"));
    configLanguageDefault.getAccessibleContext().
        setAccessibleDescription(Resources.getString(
            "mnuSetupLanguageDefaultDesc"));
    configLanguageDefault.addActionListener(this);
    subMenu.add(configLanguageDefault);

    // Setup | Language | Deutsche (German)
    configLanguageGerman = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupLanguageGermanLabel"));
    configLanguageGerman.setMnemonic(Resources.getChar(
        "mnuSetupLanguageGermanAccel"));
    configLanguageGerman.getAccessibleContext().
        setAccessibleDescription(Resources.getString(
            "mnuSetupLanguageGermanDesc"));
    configLanguageGerman.addActionListener(this);
    subMenu.add(configLanguageGerman);

    // Setup | Language | English
    configLanguageEnglish = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupLanguageEnglishLabel"));
    configLanguageEnglish.setMnemonic(Resources.getChar(
        "mnuSetupLanguageEnglishAccel"));
    configLanguageEnglish.getAccessibleContext().
        setAccessibleDescription(Resources.getString(
            "mnuSetupLanguageEnglishDesc"));
    configLanguageEnglish.addActionListener(this);
    subMenu.add(configLanguageEnglish);

    // Setup | Language | Espanola (Spanish)
    configLanguageSpanish = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupLanguageSpanishLabel"));
    configLanguageSpanish.setMnemonic(Resources.getChar(
        "mnuSetupLanguageSpanishAccel"));
    configLanguageSpanish.getAccessibleContext().
        setAccessibleDescription(Resources.getString(
            "mnuSetupLanguageSpanishDesc"));
    configLanguageSpanish.addActionListener(this);
    subMenu.add(configLanguageSpanish);

    // Setup | Language | Francaise (French)
    configLanguageFrench = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupLanguageFrenchLabel"));
    configLanguageFrench.setMnemonic(Resources.getChar(
        "mnuSetupLanguageFrenchAccel"));
    configLanguageFrench.getAccessibleContext().
        setAccessibleDescription(Resources.getString(
            "mnuSetupLanguageFrenchDesc"));
    configLanguageFrench.addActionListener(this);
    subMenu.add(configLanguageFrench);

    // Setup | Language | Italiana (Italian)
    configLanguageItalian = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupLanguageItalianLabel"));
    configLanguageItalian.setMnemonic(Resources.getChar(
        "mnuSetupLanguageItalianAccel"));
    configLanguageItalian.getAccessibleContext().
        setAccessibleDescription(Resources.getString(
            "mnuSetupLanguageItalianDesc"));
    configLanguageItalian.addActionListener(this);
    subMenu.add(configLanguageItalian);

    // Setup | Language | Portugues (Portuguese)
    configLanguagePortuguese = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupLanguagePortugueseLabel"));
    configLanguagePortuguese.setMnemonic(Resources.getChar(
        "mnuSetupLanguagePortugueseAccel"));
    configLanguagePortuguese.getAccessibleContext().
        setAccessibleDescription(Resources.getString(
            "mnuSetupLanguagePortugueseDesc"));
    configLanguagePortuguese.addActionListener(this);
    subMenu.add(configLanguagePortuguese);

    buttonGroup = new ButtonGroup();
    buttonGroup.add(configLanguageDefault);
    buttonGroup.add(configLanguageEnglish);
    buttonGroup.add(configLanguageFrench);
    buttonGroup.add(configLanguageGerman);
    buttonGroup.add(configLanguageItalian);
    buttonGroup.add(configLanguagePortuguese);
    buttonGroup.add(configLanguageSpanish);

    return subMenu;
  }

  /**
   * Configuration menu setup
   * 
   * @return The configuration menu
   */
  private JMenu configurationMenu() {
    JMenu menu;
    JMenu subMenu;
    ButtonGroup buttonGroup;

    // Configuration Menu
    menu = new JMenu(Resources.getString("mnuSetupLabel"));
    menu.setMnemonic(Resources.getChar("mnuSetupAccel"));
    menu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuSetupDesc"));

    menu.add(languageMenu());

    // Setup | Font
    configFont = new JMenuItem(Resources.getString("mnuConfigFontLabel"));
    configFont.setMnemonic(Resources.getChar("mnuConfigFontAccel"));
    configFont.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuConfigFontDesc"));
    configFont.addActionListener(this);
    configFont.setEnabled(true);
    menu.add(configFont);

    // Setup | Display DB Server Info
    subMenu = new JMenu(Resources.getString("mnuSetupDBServerLabel"));
    subMenu.setMnemonic(Resources.getChar("mnuSetupDBServerAccel"));
    subMenu.getAccessibleContext().setAccessibleDescription(
        Resources.getString(
            "mnuSetupDBServerDesc"));
    menu.add(subMenu);

    // Configuration | Display DB Server Info | None
    configDisplayDBServerInfoNone = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupDBServerNoneLabel"));
    configDisplayDBServerInfoNone.setMnemonic(Resources.getChar(
        "mnuSetupDBServerNoneAccel"));
    configDisplayDBServerInfoNone.getAccessibleContext().
        setAccessibleDescription(
            Resources.getString("mnuSetupDBServerNoneDesc"));
    subMenu.add(configDisplayDBServerInfoNone);

    // Configuration | Display DB Server Info | Brief
    configDisplayDBServerInfoShort = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupDBServerBriefLabel"));
    configDisplayDBServerInfoShort.setMnemonic(Resources.getChar(
        "mnuSetupDBServerBriefAccel"));
    configDisplayDBServerInfoShort.getAccessibleContext().
        setAccessibleDescription(Resources.getString(
            "mnuSetupDBServerBriefDesc"));
    subMenu.add(configDisplayDBServerInfoShort);

    // Configuration | Display DB Server Info | Long
    configDisplayDBServerInfoLong = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupDBServerLongLabel"));
    configDisplayDBServerInfoLong.setMnemonic(Resources.getChar(
        "mnuSetupDBServerLongAccel"));
    configDisplayDBServerInfoLong.getAccessibleContext().
        setAccessibleDescription(
            Resources.getString("mnuSetupDBServerLongDesc"));
    subMenu.add(configDisplayDBServerInfoLong);

    buttonGroup = new ButtonGroup();
    buttonGroup.add(configDisplayDBServerInfoNone);
    buttonGroup.add(configDisplayDBServerInfoLong);
    buttonGroup.add(configDisplayDBServerInfoShort);

    // Default is short display of DB server info
    configDisplayDBServerInfoShort.setSelected(true);

    // Setup | Table Row Coloring
    subMenu = new JMenu(Resources.getString("mnuSetupRowColorLabel"));
    subMenu.setMnemonic(Resources.getChar("mnuSetupRowColorAccel"));
    subMenu.getAccessibleContext().setAccessibleDescription(
        Resources.getString(
            "mnuSetupRowColorDesc"));
    menu.add(subMenu);

    // Setup | Table Row Coloring | None
    configTableColoringNone = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupRowColorNoneLabel"));
    configTableColoringNone.setMnemonic(Resources.getChar(
        "mnuSetupRowColorNoneAccel"));
    configTableColoringNone.getAccessibleContext().
        setAccessibleDescription(
            Resources.getString("mnuSetupRowColorNoneDesc"));
    configTableColoringNone.addActionListener(this);
    subMenu.add(configTableColoringNone);

    // Setup | Table Row Coloring | Green Bar
    configTableColoringGreenBar = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupRowColorGreenBarLabel"));
    configTableColoringGreenBar.setMnemonic(Resources.getChar(
        "mnuSetupRowColorGreenBarAccel"));
    configTableColoringGreenBar.getAccessibleContext().
        setAccessibleDescription(Resources.getString(
            "mnuSetupRowColorGreenBarDesc"));
    configTableColoringGreenBar.addActionListener(this);
    subMenu.add(configTableColoringGreenBar);

    // Setup | Table Row Coloring | Yellow Bar
    configTableColoringYellowBar = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupRowColorYellowBarLabel"));
    configTableColoringYellowBar.setMnemonic(Resources.getChar(
        "mnuSetupRowColorYellowBarAccel"));
    configTableColoringYellowBar.getAccessibleContext().
        setAccessibleDescription(Resources.getString(
            "mnuSetupRowColorYellowBarDesc"));
    configTableColoringYellowBar.addActionListener(this);
    subMenu.add(configTableColoringYellowBar);

    subMenu.addSeparator();

    // Setup | Table Row Coloring | User Defined
    configTableColoringUserDefined = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupRowColorUserDefLabel"));
    configTableColoringUserDefined.setMnemonic(Resources.getChar(
        "mnuSetupRowColorUserDefAccel"));
    configTableColoringUserDefined.getAccessibleContext().
        setAccessibleDescription(Resources.getString(
            "mnuSetupRowColorUserDefDesc"));
    configTableColoringUserDefined.addActionListener(this);
    subMenu.add(configTableColoringUserDefined);

    buttonGroup = new ButtonGroup();
    buttonGroup.add(configTableColoringNone);
    buttonGroup.add(configTableColoringGreenBar);
    buttonGroup.add(configTableColoringYellowBar);
    buttonGroup.add(configTableColoringUserDefined);

    // Default is no special coloring of data rows
    configTableColoringNone.setSelected(true);

    menu.addSeparator();

    // Configuration | Associate SQL and Connect URL
    configHistoryAssocSQLAndConnect = new JCheckBoxMenuItem(
        Resources.getString("mnuSetupAssocSQLURLLabel"));
    configHistoryAssocSQLAndConnect.setMnemonic(Resources.getChar(
        "mnuSetupAssocSQLURLAccel"));
    configHistoryAssocSQLAndConnect
        .getAccessibleContext()
        .
        setAccessibleDescription(Resources.getString("mnuSetupAssocSQLURLDesc"));
    configHistoryAssocSQLAndConnect.setEnabled(true);
    menu.add(configHistoryAssocSQLAndConnect);

    // Configuration | Parse SQL at Semi-Colons
    configParseSemicolons = new JCheckBoxMenuItem(
        Resources.getString("mnuSetupParseSQLSemicolonLabel"));
    configParseSemicolons.setMnemonic(Resources.getChar(
        "mnuSetupParseSQLSemicolonAccel"));
    configParseSemicolons.getAccessibleContext().
        setAccessibleDescription(Resources.getString(
            "mnuSetupParseSQLSemicolonDesc"));
    configParseSemicolons.setEnabled(true);
    menu.add(configParseSemicolons);

    menu.addSeparator();

    // Configuration | Display Column Data Type
    configDisplayColumnDataType = new JCheckBoxMenuItem(
        Resources.getString("mnuSetupDispColTypeLabel"));
    configDisplayColumnDataType.setMnemonic(Resources.getChar(
        "mnuSetupDispColTypeAccel"));
    configDisplayColumnDataType.getAccessibleContext()
        .setAccessibleDescription(
            Resources.getString("mnuSetupDispColTypeDesc"));
    configDisplayColumnDataType.setEnabled(true);
    configDisplayColumnDataType.setSelected(false);
    menu.add(configDisplayColumnDataType);

    // Configuration | Display Client Info
    configDisplayClientInfo = new JCheckBoxMenuItem(
        Resources.getString("mnuSetupClientInfoLabel"));
    configDisplayClientInfo.setMnemonic(Resources.getChar(
        "mnuSetupClientInfoAccel"));
    configDisplayClientInfo.getAccessibleContext().setAccessibleDescription(
        Resources.getString("mnuSetupClientInfoDesc"));
    configDisplayClientInfo.setEnabled(true);
    configDisplayClientInfo.setSelected(false);
    menu.add(configDisplayClientInfo);

    menu.addSeparator();

    // Configuration | Save Password
    configSavePassword = new JCheckBoxMenuItem(Resources.getString(
        "mnuSetupSavePasswordLabel"));
    configSavePassword.setMnemonic(Resources.getChar(
        "mnuSetupSavePasswordAccel"));
    configSavePassword.getAccessibleContext().setAccessibleDescription(
        Resources.getString("mnuSetupSavePasswordDesc"));
    configSavePassword.setEnabled(true);
    configSavePassword.setSelected(false);
    menu.add(configSavePassword);

    return menu;
  }

  /**
   * Help menu setup
   * 
   * @return The help menu
   */
  private JMenu helpMenu() {
    JMenu menu;

    // Help Menu
    menu = new JMenu(Resources.getString("mnuHelpLabel"));
    menu.setMnemonic(Resources.getChar("mnuHelpAccel"));
    menu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuHelpDesc"));

    // Help | Parameterized SQL Statement
    helpParameterizedSQL = new JMenuItem(Resources.getString(
        "mnuHelpParamSQLLabel"));
    helpParameterizedSQL.setMnemonic(Resources.getChar("mnuHelpParamSQLAccel"));
    helpParameterizedSQL.getAccessibleContext().setAccessibleDescription(
        Resources.getString("mnuHelpParamSQLDesc"));
    helpParameterizedSQL.addActionListener(this);
    helpParameterizedSQL.setEnabled(true);
    menu.add(helpParameterizedSQL);

    menu.addSeparator();

    // Help | About
    helpAbout = new JMenuItem(Resources.getString("mnuHelpAboutLabel"));
    helpAbout.setMnemonic(Resources.getChar("mnuHelpAboutAccel"));
    helpAbout.getAccessibleContext().setAccessibleDescription(Resources.
        getString("mnuHelpAboutDesc"));
    helpAbout.addActionListener(this);
    helpAbout.setEnabled(true);
    menu.add(helpAbout);

    return menu;
  }

  /**
   * Creates the various menu options and attaches the mnemonics and
   * registers listeners.
   */
  private void loadMenu() {
    JMenuBar menubar;

    menubar = new JMenuBar();
    setJMenuBar(menubar);

    menubar.add(fileMenu());

    menubar.add(editMenu());

    menubar.add(queryMenu());

    menubar.add(configurationMenu());

    menubar.add(helpMenu());
  }

  /**
   * Loads the drivers to allow connection to the database. Driver definitions
   * are read from a driver configuration file.
   */
  private void loadDrivers() {
    boolean isFromFile;
    BufferedReader drivers;
    String driverName;
    int sepPosit;
    final DynamicClassLoader dbClassLoader = getDBClassLoader();

    isFromFile = false;
    drivers = null;

    try {
      drivers = new BufferedReader(new FileReader(Configuration.instance().
          getFile(FILENAME_DRIVERS)));
      while ((driverName = drivers.readLine()) != null) {
        if (driverName.trim().length() > 0) {
          isFromFile = true;
          if ((sepPosit = driverName.lastIndexOf(",")) > 0) {
            loadDriver(driverName.substring(0, sepPosit),
                driverName.substring(sepPosit + 1), dbClassLoader);
          } else {
            loadDriver(driverName, driverName, dbClassLoader);
          }
        }
      }
    } catch (Exception any) {
      /*
       * Keep the output silent when the file doesn't exist, which would be
       * typical for new installation.
       */
      if (!(any instanceof FileNotFoundException)) {
        LOGGER.error("Problem loading the database driver(s)", any);
        messageOut(
            Resources.getString("errFailLoadingDBDrivers",
                Configuration.instance().getFile(FILENAME_DRIVERS)
                    .getAbsolutePath()
                    + ": " + any.getMessage()),
            STYLE_RED);
      }
    } finally {
      if (drivers != null) {
        try {
          drivers.close();
        } catch (Exception any) {
          LOGGER.error("Unable to close the drivers configuration file", any);
        }
      }
    }

    if (!isFromFile) {
      loadDriver("com.sybase.jdbc2.jdbc.SybDriver", "Sybase", dbClassLoader);
      loadDriver("org.gjt.mm.mysql.Driver", "MySQL", dbClassLoader);
      loadDriver("oracle.jdbc.driver.OracleDriver", "Oracle", dbClassLoader);
    }
  }

  /**
   * Writes the output message onto the message document
   * 
   * @param text
   *          The string to be inserted
   * @param style
   *          The message display style
   */
  private void messageOut(String text, String style) {
    messageOut(text, style, true);
  }

  /**
   * Places a message in the message display area using the style supplied.
   * 
   * @param text
   *          String The message to display
   * @param style
   *          String The predefined style to use for the text
   * @param newLine
   *          boolean Whether to force a newline after the message
   */
  private void messageOut(String text, String style, boolean newLine) {
    try {
      messageDocument.insertString(messageDocument.getLength(), text,
          (AttributeSet) messageStyles.get(style));

      if (newLine) {
        messageDocument.insertString(messageDocument.getLength(), "\n",
            (AttributeSet) messageStyles.get(
                STYLE_NORMAL));
      }
    } catch (BadLocationException excLoc) {
      LOGGER.warn("Bad location while inserting message text", excLoc);
    }

    if (messageDocument.getLength() > 0) {
      message.setCaretPosition(messageDocument.getLength() - 1);
    }
  }

  /**
   * writes the string onto the message document, displayed in the lower
   * portion of the GUI.
   * 
   * @param text
   *          The string that is to be inserted
   */
  private void messageOut(String text) {
    LOGGER.info(text);
    if (messageDocument != null) {
      messageOut(text, STYLE_NORMAL);
    }
  }

  /**
   * Loads the driver into the JVM making it available for usage by the
   * driver class
   * 
   * @param driverClass
   *          String specifying the class name for the driver
   * @param productName
   *          String specifying the product name - oracle,sybase or
   *          mySql
   * @param dbClassLoader
   *          The classloader for accessing DB drivers
   */
  private void loadDriver(String driverClass, String productName,
      DynamicClassLoader dbClassLoader) {
    Constructor<?> constructDriver;

    try {
      constructDriver = Class.forName(driverClass, true, dbClassLoader).
          getConstructor();
      DriverManager.registerDriver(new DynamicDriver((Driver) constructDriver.
          newInstance()));

      messageOut(Resources.getString("msgDriverLoaded", productName),
          STYLE_GREEN);
    } catch (Exception any) {
      LOGGER.error("Unable to load DB driver [" + driverClass + "]", any);
      messageOut(Resources.getString("errDriverNotFound", productName),
          STYLE_RED);
    }
  }

  /**
   * Loads the connect strings from an external file
   */
  private void loadConnectStrings() {
    // If no connect strings loaded, supply some defaults
    if (connectString.getModel().getSize() == 0) {
      addToCombo(connectString,
          "jdbc:sybase:Tds:localhost:3144/database?charset=iso_1");
      addToCombo(connectString, "jdbc:mysql://localhost:3306/kyn");
      addToCombo(connectString, "jdbc:oracle:thin:@localhost:1521:DEV1");
      addToCombo(connectString, "jdbc:oracle:thin:@dbprod1:1521:prod1");
    }
  }

  /**
   * Loads default queries (to give a new user some idea of how the interface
   * works) if there are no queries stored in the external query file.
   */
  private void loadQueries() {
    // If no queries loaded, supply some defaults
    if (querySelection.getModel().getSize() == 0) {
      addToCombo(querySelection, new Query("{call sp_help}", Query.MODE_QUERY));
      addToCombo(querySelection,
          new Query(
              "{call stored_proc('ARG1', '', '','ARG4')}",
              Query.MODE_QUERY));
      addToCombo(querySelection,
          new Query("select * from all_tables", Query.MODE_QUERY));
      addToCombo(querySelection,
          new Query("$PARAM[OUT,INTEGER]$ = {call sp_help}",
              Query.MODE_QUERY));
      addToCombo(
          querySelection,
          new Query(
              "$PARAM[OUT,INTEGER]$ = {call function($PARAM[IN,STRING,ARG1]$, '', '', 'ARG4'}",
              Query.MODE_QUERY));
    }
  }

  /**
   * Checks whether an input (query or connect string) is already in its
   * associated combo box. If not, the new information is added to the combo
   * list
   * 
   * @param combo
   *          JComboBox which has a list of the query statements or connect
   *          strings.
   */
  private void checkForNewString(JComboBox combo) {
    // String newString, foundString;
    Object newValue;
    int checkDups, matchAt;
    boolean match;
    // boolean newCommented, foundCommented;

    // newCommented = foundCommented = false;
    matchAt = -1;

    if (combo == querySelection) {
      newValue = new Query(queryText.getText(), whichModeValue());
      // newString = queryText.getText();
      // newString = newString.replace('\n', ' ');
    } else {
      // newString = (String)combo.getEditor().getItem();
      newValue = combo.getEditor().getItem();
    }

    // if (newString.startsWith(COMMENT_PREFIX)) {
    // newCommented = true;
    // newString = newString.substring(2);
    // }

    // if (newString.trim().length() > 0) {
    if (newValue.toString().length() > 0) {
      match = false;
      for (checkDups = 0; checkDups < combo.getItemCount() && !match; ++checkDups) {
        // if (combo == querySelection) {
        // foundString = ((Query)combo.getItemAt(checkDups)).getSQL();
        // } else {
        // foundString = ((String)combo.getItemAt(checkDups));
        // }

        // if (foundString.startsWith(COMMENT_PREFIX)) {
        // foundString = foundString.substring(2);
        // foundCommented = true;
        // } else {
        // foundCommented = false;
        // }

        // if (newString.equals(foundString)) {
        if (newValue.equals(combo.getItemAt(checkDups))) {
          match = true;
          if (combo == querySelection) {
            ((Query) combo.getItemAt(checkDups)).setMode(whichModeValue());
          }
          combo.setSelectedIndex(checkDups);
          matchAt = checkDups;
        }
      }

      // if (newCommented) {
      // newString = COMMENT_PREFIX + newString;
      // }

      if (!match) {
        addToCombo(combo, newValue);
        if (combo == querySelection) {
          // addToCombo(combo, new Query(newString, whichModeValue()));
          combo.setSelectedIndex(combo.getModel().getSize() - 1);
          matchAt = combo.getSelectedIndex();
        }
      }
      // if (foundCommented != newCommented) {
      // if (combo == querySelection) {
      // replaceInCombo(combo, matchAt,
      // new Query(newString, whichModeValue()));
      // } else {
      // replaceInCombo(combo, matchAt, newString);
      // }
      // }
      if (combo == querySelection) {
        if (((Query) newValue).isCommented() != ((Query) combo
            .getSelectedItem()).isCommented()) {
          replaceInCombo(combo, matchAt, newValue);
        }
      }
    }
  }

  /**
   * Finds the Mode Value depending on the query option selected
   * 
   * @return int The integer values for the different modes
   */
  private int whichModeValue() {
    int mode;

    if (asQuery.isSelected()) {
      mode = Query.MODE_QUERY;
    } else if (asUpdate.isSelected()) {
      mode = Query.MODE_UPDATE;
    } else {
      mode = Query.MODE_DESCRIBE;
    }

    return mode;
  }

  /**
   * Selects the Query action that is specified by the mode
   * 
   * @param mode
   *          The integer value for the mode that is selected
   */
  private void selectMode(int mode) {
    if (mode == Query.MODE_QUERY) {
      asQuery.setSelected(true);
    } else if (mode == Query.MODE_UPDATE) {
      asUpdate.setSelected(true);
    } else {
      asDescribe.setSelected(true);
    }
  }

  /**
   * Assumes that selected item in the combo box is a string and returns
   * the query object.Otherwise it converts the string to a query object
   * and returns it.
   * 
   * @return Query The Query Object
   */
  private Query getQuery() {
    LOGGER.debug("getSelectItem[" + querySelection.getSelectedItem() + "]");

    if (querySelection.getSelectedItem() == null) {
      return null;
    } else if (querySelection.getSelectedItem() instanceof Query) {
      return (Query) querySelection.getSelectedItem();
    } else {
      return new Query((String) querySelection.getSelectedItem(),
          whichModeValue());
    }
  }

  /**
   * Adds a new object in the combo box
   * 
   * @param combo
   *          The JComboBox
   * @param newData
   *          The Object that is to be added
   */
  private void addToCombo(JComboBox combo, Object newData) {
    ((DefaultComboBoxModel) combo.getModel()).addElement(newData);
  }

  /**
   * Replaces an existing object with a new one
   * 
   * @param combo
   *          The JComboBox
   * @param position
   *          The int value of the positon where the
   *          repalcement occurs
   * @param newData
   *          The new Object that is to be inserted in the combobox
   */
  private void replaceInCombo(JComboBox combo, int position, Object newData) {
    ((DefaultComboBoxModel) combo.getModel()).removeElementAt(position);
    ((DefaultComboBoxModel) combo.getModel()).insertElementAt(newData,
        position);
    combo.setSelectedIndex(position);
  }

  /**
   * Run the current SQL statement independently
   */
  private void processStatement() {
    processStatement(false);
  }

  /**
   * Run the current SQL statement either independently or as part of a batch of
   * statements
   * 
   * @param batch
   *          Whether the statement is run as part of a batch
   */
  private void processStatement(boolean batch) {
    String rawStatement;

    rawStatement = getQuery().getRawSql();

    if (rawStatement != null) {
      rawStatement = rawStatement.trim();

      if (fileSaveAsCSV.isEnabled() && rawStatement.startsWith(">")
          && rawStatement.length() > 1) {
        saveResultAsCSV(table.getModel(), new File(rawStatement.substring(1)));
      } else {
        displayResultsAsTable(batch);
      }
    } else {
      displayResultsAsTable(batch);
    }
  }

  /**
   * Displays the query results in the JTable.
   * 
   * @param batch
   *          If true, prevents error pop-up dialog boxes from being created
   */
  private void displayResultsAsTable(boolean batch) {
    TableSorter sorter;
    ListTableModel<Object> model;
    String rawSQL, sqlStatement;
    String[] eachQuery;
    int queryIndex;

    // Clear the Message Area
    message.setText("");

    // Parse the SQL for semicolons (separates multiple statements)
    rawSQL = getQuery().getRawSql();

    if (configParseSemicolons.isSelected()) {
      eachQuery = Utility.splitWithQuotes(rawSQL, ";");
      LOGGER.info("Queries embedded in this string: " + eachQuery.length);
      if (LOGGER.isDebugEnabled()) {
        for (int i = 0; i < eachQuery.length; ++i) {
          LOGGER.debug("Embedded Query " + i + ": " + eachQuery[i]);
        }
      }
    } else {
      eachQuery = new String[1];
      eachQuery[0] = rawSQL;
    }

    // Execute each query embedded in the input string
    for (queryIndex = 0; queryIndex < eachQuery.length; ++queryIndex) {
      LOGGER.debug("Embedded query index=" + queryIndex);

      sqlStatement = eachQuery[queryIndex];

      LOGGER.info("Query to execute: " + sqlStatement);

      // For multiple statements executed at once, separate messages
      if (queryIndex > 0) {
        messageOut("\n------------------------------------------------");
      }

      // If old sorter exists, remove its mouse listener(s) from table and
      // temporarily point table to empty model while real model is updated
      try {
        sorter = (TableSorter) table.getModel();
        sorter.removeMouseListenerFromHeaderInTable(table);
      } catch (Throwable any) {
        // Probably table was empty
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Error when sorting results table", any);
        }
      }

      table.setModel(new ListTableModel<Object>());

      getQuery().setMode(whichModeValue());

      /*
       * Initializes the table model - either creates it, or, clears-out the
       * existing one.
       */
      model = new ListTableModel<Object>();
      sorter = new TableSorter(model);

      if (!commentedQuery()) { // if query is not commented-out
        execute(sqlStatement, model, null);
        if (!Thread.currentThread().isInterrupted()) {
          table.setModel(sorter);
          sorter.addMouseListenerToHeaderInTable(table);
        }
      } else if (!batch) {
        userMessage(
            Resources.getString("msgSQLCommentedText"),
            Resources.getString("msgSQLCommentedTitle"),
            JOptionPane.WARNING_MESSAGE);
        // Skip all remaining statements (comments apply to the entire string)
        queryIndex = eachQuery.length;
      }

      Utility.initColumnSizes(table, model);
      histMaintQueryExecuted(model);
    }
  }

  /**
   * Displays a popup message dialog. If the message is likely to be a long
   * string without line breaks, the reformatMessage parameter can be set
   * true and line breaks will be added to allow the message to fit neatly
   * on the screen.
   * 
   * @param pMessage
   *          The message to be displayed to the user.
   * @param title
   *          The title of the message dialog
   * @param messageType
   *          The message type as defined in JOptionPane
   * @param reformatMessage
   *          Whether to insert line breaks within the message
   */
  private void userMessage(String pMessage, String title, int messageType,
      boolean reformatMessage) {
    if (reformatMessage) {
      JOptionPane.showMessageDialog(this,
          Utility.characterInsert(pMessage, "\n",
              TEXT_WRAP_MIN_LINE_LENGTH_BEFORE_WRAP, TEXT_WRAP_MAX_LINE_LENGTH,
              " .,"), title, messageType);
    } else {
      JOptionPane.showMessageDialog(this, pMessage, title, messageType);
    }
  }

  /**
   * Displays a popup message dialog.
   * 
   * @param pMessage
   *          The message to be displayed to the user.
   * @param title
   *          The title of the message dialog
   * @param messageType
   *          The message type as defined in JOptionPane
   */
  private void userMessage(String pMessage, String title, int messageType) {
    userMessage(pMessage, title, messageType, true);
  }

  /**
   * Looks for queries that have been commented out
   * 
   * @return boolean Returns true if there are any commented queries
   */
  private boolean commentedQuery() {
    if (getQuery() != null && getQuery().getSql() != null) {
      return getQuery().getSql().startsWith(COMMENT_PREFIX);
    } else {
      return false;
    }
  }

  /**
   * Test that SQL agrees with query type selected, otherwise warn user.
   * 
   * TODO This needs a lot of work dealing with the parsing of the SQL since
   * it could have $PARAM[...]$ type syntax at the beginning and the
   * simple
   * grabbing of the first set of characters is not useful.
   * 
   * @param saQuery
   *          The SQL being executed
   * @return True is the query type and the SQL are compatible or if the user
   *         agrees to force the query type.
   */
  private boolean isOkayQueryType(String saQuery) {
    boolean blOkay;
    String slFirstWord;
    String slQueryType;

    blOkay = true;
    // slQueryType = "Undefined";
    slQueryType = Resources.getString("proUnknown");

    if (asQuery.isSelected()) {
      // slQueryType = "Select";
      slQueryType = Resources.getString("ctlSelect");
    } else if (asDescribe.isSelected()) {
      // slQueryType = "Describe Result (Select)";
      slQueryType = Resources.getString("ctlDescribe");
    } else if (asUpdate.isSelected()) {
      // slQueryType = "Update";
      slQueryType = Resources.getString("ctlUpdate");
    }

    if (saQuery != null && saQuery.trim().length() > 0) {
      slFirstWord = saQuery.split(" ", 2)[0].toUpperCase();
    } else {
      slFirstWord = null;
    }

    if (slFirstWord != null) {
      // Test is a little weird, don't want to object if we don't recognize the
      // beginning of the SQL. For instance, EXEC and CALL are used for stored
      // proc
      // execution, but they may return a resultset or not, can't warn user in
      // that case. So just test for likely mistakes.
      if (asQuery.isSelected() || asDescribe.isSelected()) {
        blOkay = scWarnSelectPatterns.indexOf(slFirstWord) == -1;
      } else {
        blOkay = scWarnUpdatePatterns.indexOf(slFirstWord) == -1;
      }
    }

    if (!blOkay) {
      messageOut(Resources.getString("msgQueryTypeSuspicious", slQueryType,
          slFirstWord), STYLE_RED);
      blOkay = JOptionPane.showConfirmDialog(this,
          Utility.characterInsert(
              Resources.getString("msgQueryTypeSuspiciousText", slQueryType,
                  slFirstWord),
              "\n", TEXT_WRAP_MIN_LINE_LENGTH_BEFORE_WRAP,
              TEXT_WRAP_MAX_LINE_LENGTH, " "),
          Resources.getString("msgQueryTypeSuspiciousTitle"),
          JOptionPane.YES_NO_OPTION,
          JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
      if (blOkay) {
        messageOut(
            Resources.getString("msgUserForcingQuery"),
            STYLE_RED);
      } else {
        messageOut(Resources.getString("msgQueryCanceled"), STYLE_BOLD);
      }
    }

    return blOkay;
  }

  /**
   * Populates model with the query results. The query executed is the
   * currently selected query in the combo-box.
   * 
   * @param rawSqlStatement
   *          The SQL statement to execute
   * @param model
   *          The model to populate with the results
   * @param tripleFile
   *          The location to write the results to as triples.
   */
  private void execute(String rawSqlStatement, ListTableModel<Object> model,
      File tripleFile) {
    String sqlStatement = rawSqlStatement;
    Statement stmt = null;
    ResultSet result = null;
    ResultSetMetaData meta = null;
    List<Object> rowData = null;
    int retValue = 0;
    SQLWarning warning = null;
    int[] myType;
    Object value;
    String typeName;
    String colName;
    String metaName;
    boolean hasResults = false;
    boolean hasBLOB = false;
    Date connAsk = null;
    Date connGot = null;
    Date stmtGot = null;
    Date queryStart = null;
    Date queryReady = null;
    Date queryRSFetched = null;
    Date queryRSProcessed = null;
    long rows = 0;
    int cols = 0;
    boolean hasParams = false;
    final List<StatementParameter> allParams = new ArrayList<StatementParameter>();
    List<Object> outParams = null;

    modeOfCurrentTable = whichModeValue();
    mapOfCurrentTables = new HashMap<String, String>();

    // Try to prevent incorrect selection of query type by checking
    // beginning of SQL statement for obvious stuff
    // First check "Select" and Describe query types
    if (!isOkayQueryType(getQuery().getSql())) {
      // If the query type is wrong, and the user doesn't override then
      // Get Out Of Here!
      return;
    }

    // If there were BLOB columns included in the last query the connection
    // will have been left open. Since we are executing a new query we
    // can close that old connection now.
    if (conn != null) {
      try {
        conn.close();
      } catch (Throwable any) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Error (expected) closing connection", any);
        }
      }
    }

    conn = null;

    try {
      messageOut(Resources.getString(
          "msgExecuteQuery",
          asQuery.isSelected() ? Resources.getString("msgQuery")
              : asDescribe.isSelected()
                  ? Resources.getString("msgDescribe") : Resources
                      .getString("msgUpdate"),
          sqlStatement), STYLE_BOLD);
      if (poolConnect.isSelected()) {
        messageOut(Resources.getString("msgPoolStats") + " ", STYLE_SUBTLE,
            false);
        if (getDBPool() != null) {
          messageOut(Resources.getString("msgPoolStatsCount",
              getDBPool().getNumActive() + "", getDBPool().getNumIdle() + ""));
          LOGGER.debug("Retrieved existing DB connection pool");
        } else {
          LOGGER.debug("No existing DB pool");
          messageOut(Resources.getString("msgPoolNone"));
        }
      }
      if ((poolConnect.isSelected() && getDBPool() == null) || 
          /* conn == null */
          !((String) connectString.getEditor().getItem()).equals(
          lastConnection) || !userId.getText().equals(lastUserId)
          || !new String(password.getPassword()).equals(lastPassword)) {

        if (poolConnect.isSelected()) {
          removeDBPool();
        }

        lastConnection = (String) connectString.getEditor().getItem();
        lastUserId = userId.getText();
        lastPassword = new String(password.getPassword());

        if (poolConnect.isSelected()) {
          setupDBPool(lastConnection, lastUserId, lastPassword);
        }

        messageOut(Resources.getString("msgConnCreated", lastConnection,
            lastUserId), STYLE_SUBTLE);
      }
      connAsk = new java.util.Date();
      if (poolConnect.isSelected()) {
        conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:"
            + DBPOOL_NAME);
        LOGGER.debug("Got pooled connection");
        messageOut(Resources.getString("msgGotPoolConn"), STYLE_GREEN);
      } else {
        conn = DriverManager.getConnection(lastConnection, lastUserId,
            lastPassword);
        LOGGER.debug("Got non-pooled connection");
        messageOut(Resources.getString("msgGotDirectConn"), STYLE_GREEN);
      }

      if (hasParams = sqlStatement.indexOf("$PARAM[") > -1) {
        sqlStatement = makeParams(sqlStatement, allParams);
      }

      connGot = new java.util.Date();

      conn.setAutoCommit(autoCommit.isSelected());
      conn.setReadOnly(readOnly.isSelected());
      
      reportConnectionStats(conn);

      if (!hasParams) {
        stmt = conn.createStatement();
      } else {
        stmt = conn.prepareCall(sqlStatement);
        setupCall((CallableStatement) stmt, allParams);
      }

      stmtGot = new java.util.Date();

      try {
        if (!maxRows.getSelectedItem()
            .equals(Resources.getString("proNoLimit"))) {
          stmt.setMaxRows(Integer.parseInt((String) maxRows.getSelectedItem()));
          messageOut("\n" + Resources.getString("msgMaxRows",
              stmt.getMaxRows() + ""), STYLE_SUBTLE);
        }
      } catch (Exception any) {
        LOGGER.warn("Unable to set maximum rows", any);
        messageOut(Resources.getString("errFailSetMaxRows",
            (String) maxRows.getSelectedItem(), any.getMessage()), STYLE_YELLOW);
      }

      if (asQuery.isSelected() || asDescribe.isSelected()) {
        queryStart = new java.util.Date();
        if (!hasParams) {
          int updateCount;

          // Execute the query synchronously
          stmt.execute(sqlStatement);
          messageOut(Resources.getString("msgQueryExecutedByDB"), STYLE_GREEN);

          // Process the query results and/or report status
          if ((updateCount = stmt.getUpdateCount()) > -1) {
            do {
              LOGGER.debug("Looking for results [update="
                  + updateCount + "]");
              stmt.getMoreResults();
            } while ((updateCount = stmt.getUpdateCount()) > -1);
          }
          result = stmt.getResultSet();
        } else {
          result = ((PreparedStatement) stmt).executeQuery();
        }
        queryReady = new java.util.Date();
        meta = result.getMetaData();
        cols = meta.getColumnCount();
      } else {
        queryStart = new java.util.Date();
        if (!hasParams) {
          retValue = stmt.executeUpdate(sqlStatement);
        } else {
          retValue = ((PreparedStatement) stmt).executeUpdate();
        }
        queryReady = new java.util.Date();
      }

      if (asQuery.isSelected()) {
        for (int col = 0; col < cols; ++col) {
          colName = meta.getColumnName(col + 1);
          if (colName == null || colName.trim().length() == 0) {
            colName = Resources.getString("msgUnnamedColumn",
                meta.getColumnLabel(col + 1));
          }

          if (configDisplayColumnDataType.isSelected()) {
            metaName = meta.getColumnTypeName(col + 1) + " "
                + meta.getColumnDisplaySize(col + 1) + " (";

            // have had oracle tables report large precision values
            // for BLOB fields that caused exception to be thrown
            // by getPrecision() since the value was beyond int
            try {
              metaName += meta.getPrecision(col + 1);
            } catch (Exception any) {
              metaName += "?";
              LOGGER.warn("Unable to get column precision", any);
            }
            metaName += ".";
            metaName += meta.getScale(col + 1);
            metaName += ")";

            colName += " [" + metaName + "]";
          }

          model.addColumn(colName);
          // Keep collection of tables used for Insert and Update Menu
          // Selections
          try {
            mapOfCurrentTables.put(meta.getTableName(col + 1), null);
          } catch (Exception any) {
            // Probably unimplemented method - Sybase driver
            LOGGER.warn("Failed to obtain table name from metadata", any);
            messageOut(Resources.getString("errFailReqTableName",
                any.getMessage()), STYLE_SUBTLE);
          }
        }

        rowData = new ArrayList<Object>();

        myType = new int[cols];

        for (int col = 0; col < cols; ++col) {
          typeName = meta.getColumnTypeName(col + 1).toUpperCase();
          if (typeName.equals("NUMBER")) {
            if (meta.getScale(col + 1) > 0) {
              myType[col] = COLUMN_DATA_TYPE_DOUBLE; // DOUBLE
            } else if (meta.getPrecision(col + 1) <= MAX_DIGITS_FOR_INT) {
              myType[col] = COLUMN_DATA_TYPE_INT; // INTEGER
            } else {
              myType[col] = COLUMN_DATA_TYPE_LONG; // LONG
            }
          } else if (typeName.equals("LONG")) {
            myType[col] = COLUMN_DATA_TYPE_LONG;
          } else if (typeName.equals("DATETIME")) {
            myType[col] = COLUMN_DATA_TYPE_DATETIME; // Date/Time
          } else if (typeName.equals("DATE")) {
            myType[col] = COLUMN_DATA_TYPE_DATE; // Date/Time
          } else if (typeName.equals("BLOB")) {
            myType[col] = COLUMN_DATA_TYPE_BLOB;
            hasBLOB = true;
          } else {
            myType[col] = 0; // Default - String
          }
        }

        if (tripleFile != null) {
          try {
            final RdbToRdf exporter = new RdbToRdf(
                tripleFile.getAbsolutePath(), getQuery().getSql(), result);
            exporter.run();
            rows = exporter.getLatestNumberOfRowsExported();
            messageOut("");
            messageOut(Resources.getString("msgEndExportToFile"), STYLE_BOLD);
          } catch (Throwable throwable) {
            messageOut(
                Resources.getString("errFailDataSave", throwable.toString()),
                STYLE_RED);
            LOGGER.error(
                "Failed to save data to triples file: "
                    + tripleFile.getAbsolutePath(), throwable);
          }
        } else if (fileLogResults.isSelected()) {
          writeDataAsCSV(sqlStatement, model, DBRESULTS_NAME, result, myType,
              false);
        } else {
          while (result.next()) {
            ++rows;
            rowData = new ArrayList<Object>();

            for (int col = 0; col < cols; ++col) {
              value = getResultField(result, col + 1, myType[col]);
              rowData.add(value);
            }

            model.addRowFast(rowData);
            hasResults = true;
          }
          model.updateCompleted();
        }

        queryRSProcessed = new java.util.Date();
      } else if (asDescribe.isSelected()) {
        String colLabel;

        meta = result.getMetaData();

        myType = new int[DESC_TABLE_COLUMN_COUNT];

        for (int col = 0; col < DESC_TABLE_COLUMN_COUNT; ++col) {
          switch (col) {
            case DESC_TABLE_NAME_COLUMN: // Col Name
              colLabel = Resources.getString("proColumnName");
              myType[col] = COLUMN_DATA_TYPE_STRING;
              break;
            case DESC_TABLE_TYPE_COLUMN: // Col Type
              colLabel = Resources.getString("proColumnType");
              myType[col] = COLUMN_DATA_TYPE_STRING;
              break;
            case DESC_TABLE_LENGTH_COLUMN: // Col Length
              colLabel = Resources.getString("proColumnLength");
              myType[col] = COLUMN_DATA_TYPE_INT;
              break;
            case DESC_TABLE_PRECISION_COLUMN: // Col precision
              colLabel = Resources.getString("proColPrecision");
              myType[col] = COLUMN_DATA_TYPE_INT;
              break;
            case DESC_TABLE_SCALE_COLUMN: // Col scale
              colLabel = Resources.getString("proColScale");
              myType[col] = COLUMN_DATA_TYPE_INT;
              break;
            case DESC_TABLE_NULLS_OK_COLUMN: // Nulls Okay?
              colLabel = Resources.getString("proColNullsAllowed");
              myType[col] = COLUMN_DATA_TYPE_STRING;
              break;
            default: // oops
              colLabel = Resources.getString("proColUndefined");
              break;
          }

          if (configDisplayColumnDataType.isSelected()) {
            colLabel += " [";
            colLabel += myType[col] == 0 ? Resources
                .getString("proColCharType") : Resources
                .getString("proColNumeric");
            colLabel += "]";
          }

          model.addColumn(colLabel);
        }

        rowData = new ArrayList<Object>();

        for (int col = 0; col < cols; ++col) {
          rowData = new ArrayList<Object>();

          for (int row = 0; row < DESC_TABLE_COLUMN_COUNT; ++row) {
            switch (row) {
              case DESC_TABLE_NAME_COLUMN: // Name
                colName = meta.getColumnName(col + 1);
                if (colName == null || colName.trim().length() == 0) {
                  colName = Resources.getString("msgUnnamedColumn",
                      meta.getColumnLabel(col + 1));
                }
                value = colName;
                break;
              case DESC_TABLE_TYPE_COLUMN: // Type
                value = meta.getColumnTypeName(col + 1) + " ("
                    + meta.getColumnType(col + 1) + ")";
                break;
              case DESC_TABLE_LENGTH_COLUMN: // Length
                value = new Integer(meta.getColumnDisplaySize(col + 1));
                break;
              case DESC_TABLE_PRECISION_COLUMN: // Precision
                try {
                  value = new Integer(meta.getPrecision(col + 1));
                } catch (Exception any) {
                  value = "?";
                  LOGGER.warn("Unable to obtain column precision", any);
                }
                break;
              case DESC_TABLE_SCALE_COLUMN: // Scale
                value = new Integer(meta.getScale(col + 1));
                break;
              case DESC_TABLE_NULLS_OK_COLUMN: // Nulls Okay?
                value = meta.isNullable(col + 1) == ResultSetMetaData.columnNullable ? Resources
                    .getString("proYes")
                    : meta.isNullable(col + 1) == ResultSetMetaData.columnNoNulls ? Resources
                        .getString("proNo")
                        : Resources.getString("proUnknown");
                break;
              default:
                value = null;
                break;
            }

            rowData.add(value);

            // Keep collection of tables used for Insert and Update Menu
            // Selections
            try {
              mapOfCurrentTables.put(meta.getTableName(col + 1), null);
            } catch (Exception any) {
              // Probably unimplemented method - Sybase driver
              LOGGER.warn("Failed to obtain table name from metadata", any);
              messageOut(Resources.getString("errFailReqTableName",
                  any.getMessage()), STYLE_SUBTLE);
            }
          }
          model.addRow(rowData);
        }

        while (result.next()) {
          rows++;
          for (int col = 0; col < cols; ++col) {
            result.getObject(col + 1);
          }
        }

        queryRSFetched = new java.util.Date();

      } else {
        messageOut("\n" + Resources.getString("msgReturnValue") + " "
            + retValue, STYLE_BOLD, false);
        rows = stmt.getUpdateCount();
      }

      messageOut("\n" + Resources.getString("msgRows") + " ", STYLE_NORMAL,
          false);
      if (rows == stmt.getMaxRows() && rows > 0) {
        messageOut("" + rows, STYLE_YELLOW);
      } else {
        messageOut("" + rows, STYLE_BOLD);
      }
      messageOut("");
    } catch (SQLException sql) {
      LOGGER.error("Error executing SQL", sql);
      messageOut(Resources.getString("errFailSQL", sql.getClass().getName(),
          sql.getMessage()), STYLE_RED);
      userMessage(Resources.getString("errFailSQLText", sql.getMessage()),
          Resources.getString("errFailSQLTitle"), JOptionPane.ERROR_MESSAGE);
      while ((sql = sql.getNextException()) != null) {
        LOGGER.error("Next Exception", sql);
      }
      modeOfCurrentTable = -1;
    } catch (Throwable any) {
      LOGGER.error("Error executing SQL", any);
      messageOut(Resources.getString("errFailSQL", any.getClass().getName(),
          any.getMessage()), STYLE_RED);
      userMessage(Resources.getString("errFailSQLText", any.getMessage()),
          Resources.getString("errFailSQLTitle"), JOptionPane.ERROR_MESSAGE);
      modeOfCurrentTable = -1;
    } finally {
      fileSaveBLOBs.setEnabled(hasBLOB);
      setExportAvailable((hasResults && model.getRowCount() > 0)
          || tripleFile != null);
      queryMakeInsert.setEnabled(modeOfCurrentTable == Query.MODE_DESCRIBE
          || modeOfCurrentTable == Query.MODE_QUERY);

      if (hasParams) {
        outParams = getOutParams((CallableStatement) stmt, allParams);
      }

      LOGGER.debug("Check for more results");

      try {
        int resultCount = 0;
        while (stmt.getMoreResults()) {
          int updateCount;
          ++resultCount;
          updateCount = stmt.getUpdateCount();
          LOGGER.debug("More results ["
              + resultCount + "][updateCount="
              + updateCount + "]");
        }
      } catch (SQLException sql) {
        LOGGER.error("Failed checking for more results", sql);
        messageOut(Resources.getString("errFailAddlResults",
            sql.getClass().getName(), sql.getMessage()));
      }

      LOGGER.debug("No more results");

      if (result != null) {
        try {
          result.close();
          LOGGER.info("Resultset closed");
        } catch (Throwable any) {
          LOGGER.error("Unable to close resultset", any);
        }
      }

      if (stmt != null) {
        try {
          warning = stmt.getWarnings();
          while (warning != null) {
            LOGGER.warn("Stmt Warning: " + warning.toString());
            messageOut(
                Resources.getString("errStmtWarning", warning.toString()),
                STYLE_YELLOW);
            warning = warning.getNextWarning();
          }
        } catch (Throwable any) {
          LOGGER.warn("Error retrieving statement SQL warnings", any);
        }

        try {
          stmt.close();
          LOGGER.debug("Statement closed");
        } catch (Throwable any) {
          LOGGER.error("Unable to close statement", any);
        }
      }

      if (conn != null) {
        try {
          warning = conn.getWarnings();
          while (warning != null) {
            LOGGER.warn("Connt Warning: " + warning.toString());
            messageOut(
                Resources.getString("errConnWarning", warning.toString()),
                STYLE_YELLOW);
            warning = warning.getNextWarning();
          }
        } catch (Throwable any) {
          LOGGER.warn("Error retrieving connection SQL warnings", any);
        }
      }

      // Close the connection if there are no BLOBs.
      // If the user decides to save a BLOB we will need to DB connection
      // to remain open, hence we only close here if there are no BLOBs
      if (!hasBLOB && conn != null) {
        try {
          conn.close();
          conn = null;
          LOGGER.debug("DB Connection closed");
        } catch (Throwable any) {
          LOGGER.error("Unable to close DB connection", any);
        }
      }

      reportStats(sqlStatement, connAsk, connGot, stmtGot, queryStart,
          queryReady,
          queryRSFetched, queryRSProcessed, rows, cols,
          asDescribe.isSelected() ? model : null, outParams);
      // reportResults(SQL, model);
    }
  }

  /**
   * Outputs Connection Meta Data to the message area. This is controlled by
   * a configuration menu option.
   * 
   * @param pConn
   *          Connection The DB connection
   */
//  @SuppressWarnings("unused")
  private void reportConnectionStats(Connection pConn) {
    Class<?> connectionClass;
    Method[] allMethods;
    Object returnValue;
    int numElements, element;
    int numColumns, column;
    ResultSet resultValues;
    ResultSetMetaData metaData;
    DatabaseMetaData dbMeta;
    StringBuffer msg;
    boolean formatBrief, formatLong;
    String methodName, returnTypeName;

    formatBrief = configDisplayDBServerInfoShort.isSelected();
    formatLong = configDisplayDBServerInfoLong.isSelected();

    if (formatBrief || formatLong) {
      msg = new StringBuffer();
      msg.append('\n');
      msg.append(Resources.getString("msgDBServerInfo"));
      messageOut(msg.toString(), STYLE_SUBTLE);

      try {
        dbMeta = pConn.getMetaData();

        msg = new StringBuffer();
        connectionClass = Class.forName("java.sql.DatabaseMetaData");
        allMethods = connectionClass.getMethods();
        for (int i = 0; i < allMethods.length; ++i) {
          // Only interested in the bean accessor methods with no args
          if (allMethods[i].getName().startsWith("get")
              && allMethods[i].getParameterTypes().length == 0) {
            try {
              // Some methods should not be called - would leak connections
              returnTypeName = allMethods[i].getReturnType().toString();
              if (returnTypeName.indexOf("Connection") > 1
                  || returnTypeName.indexOf("Statement") > -1
                  || returnTypeName.indexOf("ResultSet") > -1) {
                LOGGER.debug("Skipping method [" + allMethods[i].getName()
                    + "] return type [" + allMethods[i].getReturnType() + "]");
                continue;
              }

              // If brief output, skip the more detailed information
              if (formatBrief) {
                methodName = allMethods[i].getName();
                if (methodName.indexOf("Keyword") > -1
                    || methodName.indexOf("Functions") > -1
                    || methodName.indexOf("Max") > -1) {
                  continue;
                }

              }
              // Call the method
              returnValue = allMethods[i].invoke(dbMeta);

              // Check if the returned value is an array
              if (!returnValue.getClass().isArray()) {
                // For nonarray - check if the returned value is a ResultSet
                if (returnValue instanceof ResultSet) {
                  // Only output DB server row-based data for long DB details
                  if (formatLong) {
                    // Have a result set with the information
                    messageOut("  " + fixAccessorName(allMethods[i].getName())
                        + ": " + Resources.getString("msgSetOfRows"),
                        STYLE_SUBTLE);
                    resultValues = (ResultSet) returnValue;
                    metaData = resultValues.getMetaData();
                    numColumns = metaData.getColumnCount();

                    // Output the column names
                    messageOut("    ", STYLE_SUBTLE, false);
                    for (column = 1; column <= numColumns; ++column) {
                      if (column > 1) {
                        messageOut(", ", STYLE_SUBTLE, false);
                      }
                      messageOut(metaData.getColumnName(column),
                          STYLE_SUBTLE_UL, false);
                    }
                    messageOut("", STYLE_SUBTLE);

                    // Output the rows
                    while (resultValues.next()) {
                      msg = new StringBuffer();
                      msg.append("    ");
                      for (column = 1; column <= numColumns; ++column) {
                        if (column > 1) {
                          msg.append(", ");
                        }
                        msg.append(resultValues.getString(column));
                      }
                      messageOut(msg.toString(), STYLE_SUBTLE);
                    }
                  }
                } else {
                  // Single value - simply output the resulting value
                  messageOut("  " + fixAccessorName(allMethods[i].getName())
                      + ": " + returnValue,
                      STYLE_SUBTLE);
                }
              } else if (formatLong) {
                // Have an array - display the values in the array
                // Will output if long format details are requested
                numElements = Array.getLength(returnValue);
                messageOut("  " + fixAccessorName(allMethods[i].getName())
                    + ": " + Resources.getString("msgSetOfValues",
                        numElements + ""),
                    STYLE_SUBTLE);
                for (element = 0; element < numElements; ++element) {
                  messageOut("    " + Array.get(returnValue, element),
                      STYLE_SUBTLE);

                }
              }
            } catch (Throwable any) {
              // Probably an unsupported method exception wrapped in an
              // invocation error
              if (any.getCause() != null) {
                messageOut("  " + Resources.getString("errDBServerMetaError",
                    fixAccessorName(allMethods[i].getName()),
                    any.getCause().getClass().getName(),
                    any.getCause().getMessage()),
                    STYLE_SUBTLE);
              } else {
                messageOut("  " + Resources.getString("errDBServerMetaError",
                    fixAccessorName(allMethods[i].getName()),
                    any.getClass().getName(),
                    any.getMessage()),
                    STYLE_SUBTLE);
              }
            }
          }
        }
      } catch (Throwable any) {
        LOGGER.error("Error retrieving DB Server Metadata", any);
        messageOut("  " + Resources.getString("errFailDBServerMeta",
            any.getClass().getName(), any.getMessage()));
      }
    }
  }

  /**
   * Convert an accessor name (e.g. getSomeValue) into a user friendly
   * label, splitting at uppercase letter boundaries (e.g. Some Value)
   * 
   * @param rawAccessorName
   *          String The name of the accessor method
   * @return String The reformatted label
   */
  private String fixAccessorName(String rawAccessorName) {
    String accessorName = rawAccessorName;
    int posit;
    byte[] characters;
    StringBuffer newName;
    boolean prevWasLC;

    if (accessorName.startsWith("get")) {
      accessorName = accessorName.substring(3);
    }

    newName = new StringBuffer();
    characters = accessorName.getBytes();
    newName.append((char) characters[0]);
    prevWasLC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(characters[0]) == -1;
    for (posit = 1; posit < characters.length; ++posit) {
      if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(characters[posit]) > -1) {
        if (prevWasLC) {
          newName.append(' ');
        }
        prevWasLC = false;
      } else {
        prevWasLC = true;
      }
      newName.append((char) characters[posit]);
    }

    return newName.toString();
  }

  /**
   * Takes a callable statement and retrieves the out parameters, displaying
   * them in the message area of the GUI.
   * 
   * @param stmt
   *          CallableStatement just executed
   * @param params
   *          List of parameters used in the callable statement
   * @return List
   */
  private List<Object> getOutParams(CallableStatement stmt,
      List<StatementParameter> params) {
    List<Object> values;
    int paramIndex;
    StatementParameter param;

    paramIndex = 0;

    values = new ArrayList<Object>();
    try {
      for (paramIndex = 0; paramIndex < params.size(); ++paramIndex) {
        param = (StatementParameter) params.get(paramIndex);
        if (param.getType() == StatementParameter.OUT) {
          messageOut(Resources.getString("msgOutParam") + " ", STYLE_SUBTLE,
              false);
          switch (param.getDataType()) {
            case java.sql.Types.VARCHAR:
              messageOut(Resources.getString("msgParamString",
                  paramIndex + "") + " ",
                  STYLE_SUBTLE, false);
              messageOut(stmt.getString(paramIndex + 1), STYLE_BOLD, true);
              values.add(stmt.getString(paramIndex + 1));
              break;
            case java.sql.Types.INTEGER:
              messageOut(Resources.getString("msgParamInteger",
                  paramIndex + "") + " ",
                  STYLE_SUBTLE, false);
              messageOut("" + stmt.getInt(paramIndex + 1), STYLE_BOLD, true);
              values.add(new Integer(stmt.getInt(paramIndex + 1)));
              break;
            default:
              messageOut(Resources.getString("msgParamDefault",
                  paramIndex + "") + " ",
                  STYLE_SUBTLE, false);
              messageOut("" + stmt.getObject(paramIndex + 1), STYLE_BOLD, true);
              values.add(stmt.getObject(paramIndex + 1));
              break;
          }
        }
      }
    } catch (Throwable any) {
      LOGGER.error("Failed to read output parameter at index (" + paramIndex
          + ")", any);
      messageOut(Resources.getString("errFailReadingOutParam", paramIndex + "",
          any.getMessage()));
    }

    return values;
  }

  /**
   * Takes a SQL statement and determines whether the syntax for using
   * in/out parameters has been used. For each in parameter the SQL
   * is updated to replace the parameter with a "?" and stores the
   * parameter value in the parameter list. Out parameters are simply
   * stored in the parameters list and the specialized parameter syntax
   * removed. The end result is a parameterized SQL statement and a
   * list of parameters.
   * 
   * @param rawSqlStatement
   *          The SQL statement to be processed for parameters
   * @param params
   *          The collection of parameters found in the SQL statement
   * @return The updated SQL staement with the specialized parameter syntax
   *         removed
   */
  private String makeParams(String rawSqlStatement,
      List<StatementParameter> params) {
    String sqlStatement = rawSqlStatement;
    int beginPosit, endPosit;

    while ((beginPosit = sqlStatement.indexOf(PARAM_TOKEN_START)) > -1) {
      endPosit = sqlStatement.substring(beginPosit).indexOf(PARAM_TOKEN_END);
      if (endPosit == -1) {
        endPosit = sqlStatement.length() - PARAM_TOKEN_END_LENGTH;
      } else {
        endPosit += beginPosit;
      }
      processParameter(
          sqlStatement.substring(beginPosit + PARAM_TOKEN_START_LENGTH,
              endPosit).trim(),
          params);
      sqlStatement = Utility
          .replace(
              sqlStatement,
              sqlStatement.substring(beginPosit, endPosit
                  + PARAM_TOKEN_END_LENGTH), "?",
              0, false);
      messageOut(Resources.getString("msgParamSearchLoopBefore", sqlStatement));
    }

    messageOut(Resources.getString("msgParamSearchLoopAfter", sqlStatement));

    return sqlStatement;
  }

  /**
   * Parse a parameter. Determines the parameter direction (In/Out) and
   * type.
   * 
   * @param param
   *          The parameter as supplied in the input SQL statement
   * @param params
   *          The collection of parameters. The parameter decoded from the
   *          param input is added to this list.
   */
  private void processParameter(String param, List<StatementParameter> params) {
    int type;
    String data;

    if (param.toUpperCase().startsWith("IN,")) {
      type = SQLTypes.getSQLTypeId(param.substring(3));
      data = param.substring(3 + param.substring(3).indexOf(',') + 1);
      messageOut(Resources.getString("msgParamInDesc", type + "", data));
      params.add(new StatementParameter(StatementParameter.IN, type, data));
    } else if (param.toUpperCase().startsWith("OUT,")) {
      type = SQLTypes.getSQLTypeId(param.substring(4));
      messageOut(Resources.getString("msgParamOutDesc", type + ""));
      params.add(new StatementParameter(StatementParameter.OUT, type));
    }
  }

  /**
   * Setup a callable statement for execution.
   * 
   * @param stmt
   *          The callable statement
   * @param params
   *          The list of parameters
   */
  private void setupCall(CallableStatement stmt, List<StatementParameter> params) {
    int paramIndex;
    StatementParameter param;

    paramIndex = 0;

    try {
      for (paramIndex = 0; paramIndex < params.size(); ++paramIndex) {
        param = (StatementParameter) params.get(paramIndex);

        if (param.getType() == StatementParameter.IN) {
          switch (param.getDataType()) {
            case java.sql.Types.VARCHAR:
              stmt.setString(paramIndex + 1, param.getDataString());
              break;
            case java.sql.Types.INTEGER:
              stmt.setInt(paramIndex + 1,
                  Integer.parseInt(param.getDataString()));
              break;
            default:
              stmt.setObject(paramIndex + 1, param.getDataString());
              break;
          }
        } else {
          stmt.registerOutParameter(paramIndex + 1, param.getType());
        }
      }
    } catch (Throwable any) {
      LOGGER.error("Failed to register output parameter at index ["
          + paramIndex + "]", any);
      messageOut(Resources.getString("errFailRegisterOutParam",
          paramIndex + "",
          any.getMessage()));
    }
  }

  /**
   * Log the results of executing the SQL to a flat file
   * 
   * @param query
   *          The SQL statement
   * @param model
   *          The table model containing the results
   */
  @SuppressWarnings("unused")
  private void reportResults(String query, TableModel model) {
    if (fileLogResults.isSelected() && model != null) {
      writeDataAsCSV(query, model, DBRESULTS_NAME, true);
    }
  }

  /**
   * Reports statistics on various data such as the date a query was executed
   * and
   * the date the results were fetched. Other statistics are reported on such as
   * the
   * the date a connection was asked for, and the date it was actually received.
   * The report statistics are written to an external text file
   * represented by DBSTATS_NAME.
   * 
   * @param sqlStatement
   *          The SQL statement
   * @param connAsk
   *          The time when the connection was requested
   * @param connGot
   *          The time when the connection was returned
   * @param stmtGot
   *          The time when the statement was returned
   * @param queryStart
   *          The time when query exeution began
   * @param queryReady
   *          The time when the query finished exeuting
   * @param queryRSFetched
   *          The time when the result set had been completely fetched
   * @param queryRSProcessed
   *          The time when the result set had been completely processed
   * @param rows
   *          The number of rows in the result set
   * @param cols
   *          The number of columns in the result set
   * @param model
   *          The table model for the results
   * @param outParams
   *          The output parameters defined for this SQL statement
   */
  private void reportStats(String sqlStatement, java.util.Date connAsk,
      java.util.Date connGot,
      java.util.Date stmtGot,
      java.util.Date queryStart, java.util.Date queryReady,
      java.util.Date queryRSFetched,
      java.util.Date queryRSProcessed,
      long rows, int cols, TableModel model,
      List<Object> outParams) {
    Runtime runtime;
    String runStats;
    PrintWriter out;
    boolean firstEntry;
    final String valueNotApplicable = "\""
        + Resources.getString("proValueNotApplicable") + "\",";

    runStats = "";
    out = null;
    firstEntry = false;

    if (fileLogStats.isSelected()) {
      // Identify whether file exists, if not create and add header row
      try {
        new FileReader(DBSTATS_NAME).close();
      } catch (Exception any) {
        firstEntry = true;
      }

      try {
        out = new PrintWriter(new FileWriter(DBSTATS_NAME, true));
      } catch (Exception any) {
        LOGGER.error("Failed to write the statistics file [" + DBSTATS_NAME
            + "]", any);
        messageOut(Resources.getString("errFailWriteStatsFile", DBSTATS_NAME,
            any.getMessage()), STYLE_RED);
      }
    }

    // Make sure it is always safe to write to "out" -- simplifies logic
    if (out == null) {
      out = new PrintWriter(new StringWriter());
    }

    // Header, if needed
    if (firstEntry) {
      out.print(Resources.getString("proQueryStatsExportHeader"));
      if (outParams != null && outParams.size() > 0) {
        for (int param = 0; param < outParams.size(); ++param) {
          out.print(Resources.getString("proQueryStatsExportHeaderParam",
              param + ""));
        }
      }
      out.println();
    }

    // Output Query Index Number
    out.print(querySelection.getSelectedIndex() + ",");

    // Output SQL, replacing quotes with apostrophes
    out.print("\""
        + sqlStatement.replace('"', '\'')
        + "\",");

    // Output timestamp
    out.print(Utility.formattedDate(new java.util.Date()) + ",");

    // Output time required to get connection to database
    if (connAsk != null && connGot != null) {
      runStats += Resources.getString("proTimeConnOpen",
          (connGot.getTime() - connAsk.getTime()) + "");
      runStats += "  ";
      out.print((connGot.getTime() - connAsk.getTime()) + ",");
    } else {
      out.print(valueNotApplicable);
    }

    // Output time required to get statement object
    if (connGot != null && stmtGot != null) {
      runStats += Resources.getString("proTimeStmtAccess",
          (stmtGot.getTime() - connGot.getTime()) + "");
      runStats += "  ";
      out.print((stmtGot.getTime() - connGot.getTime()) + ",");
    } else {
      out.print(valueNotApplicable);
    }

    // Time it took to configure statement
    if (queryStart != null && stmtGot != null) {
      runStats += Resources.getString("proTimeStmtSetup",
          (queryStart.getTime() - stmtGot.getTime()) + "");
      runStats += "  ";
      out.print((queryStart.getTime() - stmtGot.getTime()) + ",");
    } else {
      out.print(valueNotApplicable);
    }

    runStats += "\n          ";

    // Output time DB took to execute query
    if (queryStart != null && queryReady != null) {
      runStats += Resources.getString("proTimeDBExecute",
          (queryReady.getTime() - queryStart.getTime()) + "");
      runStats += "  ";
      out.print((queryReady.getTime() - queryStart.getTime()) + ",");
    } else {
      out.print(valueNotApplicable);
    }

    // Output time it took to fetch all results
    if (queryReady != null && queryRSFetched != null) {
      runStats += Resources.getString("proTimeResultsFetch",
          (queryRSFetched.getTime() - queryReady.getTime()) + "");
      runStats += "  ";
      out.print((queryRSFetched.getTime() - queryReady.getTime()) + ",");
    } else {
      out.print(valueNotApplicable);
    }

    // Output time it took to process all results
    if (queryReady != null && queryRSProcessed != null) {
      runStats += Resources.getString("proTimeResultSet",
          (queryRSProcessed.getTime() - queryReady.getTime()) + "");
      runStats += "  ";
      out.print((queryRSProcessed.getTime() - queryReady.getTime()) + ",");
    } else {
      out.print(valueNotApplicable);
    }

    if (runStats.length() > 0) {
      messageOut(Resources.getString("proTimeDBStats") + " ", STYLE_SUBTLE,
          false);
      messageOut(runStats, STYLE_NORMAL, false);
      runStats = "";
    }

    // Output total time it took to obtain connection, execute SQL and obtain
    // results
    if (connAsk != null && queryRSFetched != null) {
      runStats += Resources.getString("proTimeTotal",
          (queryRSFetched.getTime() - connAsk.getTime()) + "");
      runStats += "  ";
      out.print((queryRSFetched.getTime() - connAsk.getTime()) + ",");
    } else if (connAsk != null && queryRSProcessed != null) {
      runStats += Resources.getString("proTimeTotal",
          (queryRSProcessed.getTime() - connAsk.getTime()) + "");
      runStats += "  ";
      out.print((queryRSProcessed.getTime() - connAsk.getTime()) + ",");
    } else if (connAsk != null && queryReady != null) {
      runStats += Resources.getString("proTimeTotal",
          (queryReady.getTime() - connAsk.getTime()) + "");
      runStats += "  ";
      out.print((queryReady.getTime() - connAsk.getTime()) + ",");
    } else {
      out.print(valueNotApplicable);
    }

    messageOut(runStats, STYLE_BOLD, true);

    // Output number of columns in resultset
    out.print(cols + ",");

    // Output number of rows returned or modified
    out.print(rows + "," + maxRows.getSelectedItem().toString() + ",");

    runtime = Runtime.getRuntime();

    // Output environment information
    out.print(runtime.totalMemory() + "," + runtime.freeMemory() + ","
        + (runtime.totalMemory() - runtime.freeMemory()) + ","
        + runtime.maxMemory() + "," + runtime.availableProcessors());

    if (configDisplayClientInfo.isSelected()) {
      runStats = Resources.getString("proMemAlloc", runtime.totalMemory() + "");
      runStats += " " + Resources.getString("proMemFree",
          runtime.freeMemory() + "");
      runStats += " " + Resources.getString("proMemUsed",
          (runtime.totalMemory() - runtime.freeMemory()) + "");
      runStats += " " + Resources.getString("proMemMax",
          runtime.maxMemory() + "");
      runStats += " " + Resources.getString("proProcessors",
          runtime.availableProcessors() + "");

      messageOut("");
      messageOut(Resources.getString("proClientEnv") + " ", STYLE_BOLD_UL);
      messageOut(runStats);
      
      // TODO show VM info with correct resources for labeling
      RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
      messageOut("JVM: " + runtimeBean.getName(), STYLE_SUBTLE);
      messageOut("Spec Name: " + runtimeBean.getSpecName(), STYLE_SUBTLE);
      messageOut("Spec Vendor: " + runtimeBean.getSpecVendor(), STYLE_SUBTLE);
      messageOut("Spec Version: " + runtimeBean.getSpecVersion(), STYLE_SUBTLE);
      messageOut("VM Name: " + runtimeBean.getVmName(), STYLE_SUBTLE);
      messageOut("VM Vendor: " + runtimeBean.getVmVendor(), STYLE_SUBTLE);
      messageOut("VM Version: " + runtimeBean.getVmVersion(), STYLE_SUBTLE);
      messageOut("Classpath: " + runtimeBean.getClassPath(), STYLE_SUBTLE);
      messageOut("");
    }

    if (poolConnect.isSelected()) {
      messageOut(Resources.getString("msgPoolStats") + " ", STYLE_SUBTLE, false);
      messageOut(Resources.getString("msgPoolStatsCount",
          getDBPool().getNumActive() + "", getDBPool().getNumIdle() + ""));
    }

    // If output parameters, list them
    if (outParams != null && outParams.size() > 0) {
      for (int param = 0; param < outParams.size(); ++param) {
        out.print(",");
        if (outParams.get(param) instanceof String) {
          out.print("\"");
        }
        out.print(outParams.get(param));
        if (outParams.get(param) instanceof String) {
          out.print("\"");
        }
      }
    }

    // If model given, output describe content
    if (model != null) {
      for (int row = 1; row < model.getRowCount(); ++row) {
        out.print(",\"");

        // Column Name
        out.print(model.getValueAt(row, 0));

        // Type
        out.print(" " + model.getValueAt(row, 1));

        // Size
        out.print(" " + model.getValueAt(row, 2));

        // Precision
        out.print(" (" + model.getValueAt(row, 3) + ","
            + model.getValueAt(row, 4) + ")");

        out.print("\"");
      }
    }

    out.println();
    out.close();
  }

  /**
   * Removes a query from the comboBox
   */
  private void removeSelectedQuery() {
    int index;
    if ((index = querySelection.getSelectedIndex()) >= 0) {
      querySelection.removeItemAt(index);
      histMaintQueryDeleted(index);
    }
  }

  /**
   * Writes the list of queries or connection URLs to the appropriate
   * combo boxes
   */
  private void saveConfig() {
    writeOutCombo(querySelection, queryFilename);
    writeOutCombo(connectString,
        Configuration.instance().getFile(FILENAME_CONNECTSTRINGS).
            getAbsolutePath());
    saveProperties();
  }

  /**
   * Adds a list of queries and connection URLs to the appropriate
   * combo boxes
   */
  private void loadConfig() {
    loadCombo(querySelection, queryFilename);
    loadCombo(connectString,
        Configuration.instance().getFile(FILENAME_CONNECTSTRINGS).
            getAbsolutePath());
  }

  /**
   * Adds the data from the supplied file to the query selection
   * (or connect URLs) combobox.
   * 
   * @param combo
   *          The JComboBox - it can be the combobox that contains
   *          all the queries or the combobox with the URLs
   * @param fileName
   *          The name of the file that contains the queries or the
   *          connect URLs
   */
  private void loadCombo(JComboBox combo, String fileName) {
    BufferedReader in;
    String data;

    in = null;
    try {
      in = new BufferedReader(new FileReader(fileName));
      while ((data = in.readLine()) != null) {
        if (combo == querySelection) {
          addToCombo(combo,
              new Query(data.substring(0, data.lastIndexOf("[")).trim(),
                  new
                  Integer(data.substring(data.lastIndexOf("[") + 1).
                      trim()).intValue()));
        } else {
          addToCombo(combo, data);
        }
      }
    } catch (Throwable any) {
      LOGGER.error("Failed to load combo box with lookup values", any);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (Throwable any) {
          LOGGER.error("Failed to close the input file", any);
        }
      }
    }
  }

  /**
   * Writes all the queries in the query selection (or connection URLS) combobox
   * to the supplied file in a print formatted representation.
   * 
   * @param combo
   *          The JComboBox - it can be the combobox that contains
   *          all the queries or the combobox with the URLs
   * @param fileName
   *          The fileName to be written to contain the queries or the
   *          connect URLs
   */
  private void writeOutCombo(JComboBox combo, String fileName) {
    PrintWriter out;

    if (combo != null) {
      out = null;
      try {
        out = new PrintWriter(new FileWriter(fileName));
        for (int i = 0; i < combo.getModel().getSize(); ++i) {
          if (combo == querySelection) {
            out.println(((Query) combo.getModel().getElementAt(i)).getSql()
                + "["
                + ((Query) combo.getModel().getElementAt(i)).getMode());
          } else {
            out.println((String) combo.getModel().getElementAt(i));
          }
        }
      } catch (Exception any) {
        LOGGER.error("Failed to write out the content of the combobox", any);
      } finally {
        if (out != null) {
          try {
            out.close();
          } catch (Exception any) {
            LOGGER.error("Failed to close the combobox output file", any);
          }
        }
      }
    }
  }

  /**
   * Gets each cell of the selected row - if the object stored in the cell
   * is of type java.sql.Blob, the object is written to disk by calling
   * writeBlob(String, java.sql.Blob) on the DB connection.
   */
  private void saveBLOBs() {
    TableModel model;
    int selectedRow;
    String filesWritten;

    if (table.getSelectedRowCount() != 1) {
      userMessage(Resources.getString("errChooseOneRowText"),
          Resources.getString("errChooseOneRowTitle"),
          JOptionPane.ERROR_MESSAGE);
    } else {
      model = table.getModel();
      selectedRow = table.getSelectedRow();
      filesWritten = "";
      for (int col = 1; col < model.getColumnCount(); col++) {
        if (model.getValueAt(selectedRow, col) instanceof java.sql.Blob) {
          LOGGER.debug("Blob["
              + (java.sql.Blob) model.getValueAt(selectedRow, col) + "]");
          filesWritten +=
              writeBlob(model.getColumnName(col),
                  (java.sql.Blob) model.getValueAt(selectedRow, col))
                  + "\n";
        }
      }
      userMessage(Resources.getString("msgBLOBWrittenText", filesWritten),
          Resources.getString("msgBLOBWrittenTitle"),
          JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * Writes the contents of BLOB to disk. The name of the file created is
   * based on the column name and the string representation of the
   * BLOB object.
   * 
   * @param rawColName
   *          The column that contains the BLOB object
   * @param blob
   *          The BLOB object that is written onto the file
   * 
   * @return The name of the file created
   */
  private String writeBlob(String rawColName, java.sql.Blob blob) {
    String colName = rawColName;
    FileOutputStream out;
    InputStream in;
    byte[] block;
    int bytesRead;
    String fileName;

    out = null;

    if (colName == null || colName.trim().length() == 0) {
      colName = Resources.getString("msgBLOBColNoName");
    }

    // If the option to display column type is on, strip off col type info
    if (colName.indexOf(' ') > 0) {
      colName = colName.substring(0, colName.indexOf(' '));
    }

    fileName = colName + "." + new java.util.Date().getTime() + ".dat";

    try {
      block = new byte[BLOB_LOAD_BLOCK_SIZE];
      out = new FileOutputStream(fileName, false);
      in = blob.getBinaryStream();
      while ((bytesRead = in.read(block)) > 0) {
        out.write(block, 0, bytesRead);
      }
    } catch (Exception any) {
      LOGGER.error("Failed to write BLOB field value to file [" + fileName
          + "]", any);
      fileName += " (" + Resources.getString("errFailBLOBWrite",
          any.getClass().getName(), any.getMessage()) + ")";
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (Exception any) {
          LOGGER.error("Failed to close the BLOB output file", any);
        }
      }
    }

    return fileName;
  }

  /**
   * Create the query 'select * from' for selected the column in the table
   * 
   * @param mode
   *          The integer value for the different modes
   */
  private void queryStar(int mode) {
    String value;

    LOGGER.debug("Row:" + table.getSelectedRow() + " Col:"
        + table.getSelectedColumn());
    LOGGER.debug("Row Count:" + table.getSelectedRowCount()
        + " Col Count:" + table.getSelectedColumnCount());
    if (table.getSelectedRowCount() != 1 || table.getSelectedColumnCount() != 1) {
      userMessage(Resources.getString("errChooseOneCellText"),
          Resources.getString("errChooseOneCellTitle"),
          JOptionPane.ERROR_MESSAGE);
    } else {
      value = table.getModel().getValueAt(table.getSelectedRow(),
          table.getSelectedColumn()).toString();
      LOGGER.debug("Value class: " + value.getClass().getName());
      if (value == null || value.trim().length() == 0) {
        userMessage(Resources.getString("errNoDataInCellText"),
            Resources.getString("errNoDataInCellTitle"),
            JOptionPane.WARNING_MESSAGE);
      } else {
        value = "select * from " + value;
        messageOut(Resources.getString("msgMadeSQL", value));
        queryText.setText(value);
        checkForNewString(querySelection);
        querySelection.setSelectedItem(value);
        selectMode(mode);
      }
    }

  }

  /**
   * Create an insert statement depending on the tables and the columns that
   * were selected.The insert statement is then listed in the querySelection
   * comboBox as the selected item.
   */
  private void makeInsert() {
    String[][] columns;
    String tables;
    String sqlStatement;

    tables = getTablesInQuery();
    columns = getColumnNamesForTable();

    sqlStatement = "insert into " + tables + "(";

    for (int i = 0; i < columns.length; ++i) {
      if (i > 0) {
        sqlStatement += ",";
      }
      sqlStatement += columns[i][0];
    }

    sqlStatement += ") values (";

    for (int i = 0; i < columns.length; ++i) {
      if (columns[i][1].startsWith("VARCHAR")
          || columns[i][1].startsWith("CHAR")) {
        sqlStatement += "''";
      }
      if (i < columns.length - 1) {
        sqlStatement += ",";
      }
    }

    sqlStatement += ")";

    messageOut(Resources.getString("msgMadeSQL", sqlStatement));
    queryText.setText(sqlStatement);
    checkForNewString(querySelection);
    querySelection.setSelectedItem(sqlStatement);
  }

  /**
   * Create an update statement depending on the tables and the columns that
   * were selected.The update statement is then listed in the querySelection
   * comboBox as the selected item.
   */
  private void makeUpdate() {
    String[][] columns;
    String tables;
    String thisPhrase;
    String sqlStatement;
    String where;
    boolean firstColOut;

    tables = getTablesInQuery();
    columns = getColumnNamesForTable();
    firstColOut = true;

    where = "";
    sqlStatement = "update " + tables + " ";

    for (int i = 0; i < columns.length; ++i) {
      thisPhrase = columns[i][0] + "=";
      if (columns[i][1].startsWith("VARCHAR")
          || columns[i][1].startsWith("CHAR")) {
        thisPhrase += "''";
      }

      if (columns[i][2] != null) {
        if (where.length() > 0) {
          where += " and ";
        }
        where += thisPhrase;
      } else {
        if (firstColOut) {
          sqlStatement += "set ";
          firstColOut = false;
        } else {
          sqlStatement += ", ";
        }

        sqlStatement += thisPhrase;
      }
    }

    if (where.length() > 0) {
      sqlStatement += " where " + where;
    }

    messageOut(Resources.getString("msgMadeSQL", sqlStatement));
    queryText.setText(sqlStatement);
    checkForNewString(querySelection);
    querySelection.setSelectedItem(sqlStatement);
  }

  /**
   * Write a select statement depending on the tables and the columns that
   * were selected.The select statement is then listed in the querySelection
   * comboBox as the selected item.
   */
  private void makeVerboseSelect() {
    String[][] columns;
    String tables;
    String thisPhrase;
    String sqlStatement;
    String where;
    boolean firstColOut;

    tables = getTablesInQuery();
    columns = getColumnNamesForTable();
    firstColOut = true;

    where = "";
    sqlStatement = "";

    for (int i = 0; i < columns.length; ++i) {
      thisPhrase = columns[i][0] + "=";
      if (columns[i][1].startsWith("VARCHAR")
          || columns[i][1].startsWith("CHAR")) {
        thisPhrase += "''";
      }

      if (columns[i][2] != null) {
        if (where.length() > 0) {
          where += " and ";
        }
        where += thisPhrase;
      } else {
        if (firstColOut) {
          sqlStatement += "select ";
          firstColOut = false;
        } else {
          sqlStatement += ", ";
        }

        sqlStatement += columns[i][0];
      }
    }

    sqlStatement += " from " + tables;

    if (where.length() > 0) {
      sqlStatement += " where " + where;
    }

    messageOut(Resources.getString("msgMadeSQL", sqlStatement));
    queryText.setText(sqlStatement);
    checkForNewString(querySelection);
    querySelection.setSelectedItem(sqlStatement);
  }

  /**
   * Gets the names of the tables that are referenced during the query
   * 
   * @return tableNames A String that contains the table names
   */
  private String getTablesInQuery() {
    Iterator<String> tables;
    String tableNames;

    tables = mapOfCurrentTables.keySet().iterator();
    tableNames = "";

    while (tables.hasNext()) {
      tableNames += "," + tables.next();
    }

    if (tableNames.length() > 0) {
      tableNames = tableNames.substring(1); // Remove first comma
    }

    return tableNames;
  }

  /**
   * Gets the column name for the column that is being selected in the table
   * 
   * @return columns A string array with the Column Names and Values
   */
  private String[][] getColumnNamesForTable() {
    String[][] columns;
    TableModel model;

    model = table.getModel();

    if (modeOfCurrentTable == Query.MODE_DESCRIBE) {
      columns = new String[model.getRowCount()][3];
      for (int i = 0; i < model.getRowCount(); ++i) {
        columns[i][0] = model.getValueAt(i, 0).toString();
        columns[i][1] = model.getValueAt(i, 1).toString(); // Type
        if (table.isRowSelected(i)) {
          columns[i - 1][2] = "Selected";
        } else {
          columns[i - 1][2] = null;
        }
      }
    } else {
      columns = new String[model.getColumnCount()][3];
      for (int i = 0; i < model.getColumnCount(); ++i) {
        columns[i][0] = model.getColumnName(i);

        // Remove type if present
        if (columns[i][0].indexOf("[") > -1) {
          columns[i][1] = columns[i][0].substring(columns[i][0].indexOf("[")
              + 1);
          columns[i][0] = columns[i][0].substring(0,
              columns[i][0].indexOf("[")).trim();
        } else {
          /**
           * Todo: change column name/type design so that we can easily get the
           * column type.
           */
          columns[i][1] = "Number";
        }

        if (table.isColumnSelected(i)) {
          columns[i][2] = "Selected";
        } else {
          columns[i][2] = null;
        }
      }
    }

    return columns;
  }

  /**
   * Exports the results to a file
   * 
   * @param model
   *          The model for the current results
   * @param fileType
   *          The export file type
   */
  private void chooseFileForSave(TableModel model, int fileType) {
    final JFileChooser chooser = new JFileChooser();
    int returnVal;

    chooser.setDialogTitle(Resources.getString("dlgSaveDataTitle"));
    chooser.setApproveButtonText(Resources.getString("dlgSaveDataButton"));
    chooser.setApproveButtonMnemonic(Resources.getChar(
        "dlgSaveDataButtonMnemonic"));

    if (latestFileDirectory != null) {
      if (latestFileDirectory.isDirectory()) {
        chooser.setCurrentDirectory(latestFileDirectory);
      } else if (latestFileDirectory.getParentFile().isDirectory()) {
        chooser.setCurrentDirectory(latestFileDirectory.getParentFile());
      }
    }

    returnVal = chooser.showOpenDialog(this);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      latestFileDirectory = chooser.getSelectedFile().getParentFile();

      switch (fileType) {
        case FILE_EXPORT_CSV:
          saveResultAsCSV(model, chooser.getSelectedFile().getAbsoluteFile());
          break;
        case FILE_EXPORT_TRIPLES:
          saveResultsAsTriples(model, chooser.getSelectedFile()
              .getAbsoluteFile());
          break;
        default:
          throw new IllegalArgumentException(
              "Unknown file type mode for export: " + fileType);
      }
    }
  }

  /**
   * Saves the Result in a file with the .csv extension
   * 
   * @param model
   *          The model for the current results
   * @param fileToWrite
   *          The file to write the result to
   */
  private void saveResultAsCSV(TableModel model, File fileToWrite) {
    String fileName;
    FileReader testExist;
    boolean okToWrite;

    fileName = fileToWrite.getAbsolutePath();

    okToWrite = false;

    if (fileName == null || fileName.trim().length() == 0) {
      userMessage(Resources.getString("errNoFileNameWriteText"),
          Resources.getString("errNoFileNameWriteTitle"),
          JOptionPane.ERROR_MESSAGE);
    } else {
      fileName = fileName.trim();
      if (!fileName.toLowerCase().endsWith(".csv") && !fileName.endsWith(".")) {
        fileName += ".csv";
      }

      try {
        testExist = new FileReader(fileName);
        testExist.close();
        okToWrite = JOptionPane.showConfirmDialog(this,
            Resources.getString("dlgVerifyOverwriteText", fileName),
            Resources.getString("dlgVerifyOverwriteTitle"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
      } catch (FileNotFoundException fnf) {
        okToWrite = true;
      } catch (Exception any) {
        LOGGER.error("Error preparing to save data", any);
        messageOut(
            Resources.getString("errFailDataSavePrep", any.getMessage()),
            STYLE_RED);
      }
    }

    if (okToWrite) {
      writeDataAsCSV(getQuery().getSql(), model, fileName, false);
    }
  }

  /**
   * Saves the Result in a file to a Turtle file with the .ttl extension
   * 
   * @param model
   *          The model for the current results
   * @param fileToWrite
   *          The file to write the result to
   */
  private void saveResultsAsTriples(TableModel model, File fileToWrite) {
    String fileName;
    FileReader testExist;
    boolean okToWrite;

    fileName = fileToWrite.getAbsolutePath();

    okToWrite = false;

    if (fileName == null || fileName.trim().length() == 0) {
      userMessage(Resources.getString("errNoFileNameWriteText"),
          Resources.getString("errNoFileNameWriteTitle"),
          JOptionPane.ERROR_MESSAGE);
    } else {
      fileName = fileName.trim();
      if (!fileName.toLowerCase().endsWith(".ttl") && !fileName.endsWith(".")) {
        fileName += ".ttl";
      }

      try {
        testExist = new FileReader(fileName);
        testExist.close();
        okToWrite = JOptionPane.showConfirmDialog(this,
            Resources.getString("dlgVerifyOverwriteText", fileName),
            Resources.getString("dlgVerifyOverwriteTitle"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
      } catch (FileNotFoundException fnf) {
        okToWrite = true;
      } catch (Exception any) {
        LOGGER.error("Error preparing to save data", any);
        messageOut(
            Resources.getString("errFailDataSavePrep", any.getMessage()),
            STYLE_RED);
      }
    }

    if (okToWrite) {
      exportFile = new File(fileName);
      runType = RUNTYPE_TRIPLE_EXPORT;
      new Thread(this).start();
      // execute(getQuery().getSql(), new ListTableModel<Object>(), new File(
      // fileName));
    }
  }

  /**
   * Writes the data onto a file that is specified by the filePath
   * 
   * @param query
   *          The SQL statement
   * @param model
   *          The model for the current results
   * @param filePath
   *          A String that denotes the filepath
   * @param append
   *          Whether to append the current results into the file
   * 
   * @todo Make this handle other data types (especially Date/Time) better
   *       for cleaner import to spreadsheet.
   */
  private void writeDataAsCSV(String query, TableModel model, String filePath,
      boolean append) {
    PrintWriter out;
    int row, col;
    boolean[] quote;
    boolean quoteThis;
    String temp;
    Object value;

    out = null;

    this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    (flashRunIndicator = new Thread(new FlashForeground(runIndicator,
        Color.red.brighter(), Color.lightGray, 5))).start();
    (timeRunIndicator = new Thread(new InsertTime(timeIndicator,
        new java.util.Date().getTime(),
        QUERY_EXECUTION_TIMER_UPDATE_DELAY_MS))).start();

    try {
      out = new PrintWriter(new FileWriter(filePath, append));

      messageOut("");
      messageOut(Resources.getString("msgStartExportToFile", filePath));

      // Output query
      if (!fileExportsRaw.isSelected()) {
        // Make it look like a comment using hash comment character
        out.print("#,");

        out.print("\"");
      }
      // out.print(getQuery().getSQL());
      out.print(query.replaceAll("\n", " "));
      if (!fileExportsRaw.isSelected()) {
        out.print("\"");
      }
      out.println();

      // Setup array to hold which columns need to be quoted
      quote = new boolean[model.getColumnCount()];

      // Output column headings - determine if column needs quotes
      for (col = 0; col < model.getColumnCount(); ++col) {
        if (col > 0) {
          out.print(",");
        }
        out.print("\"" + model.getColumnName(col) + "\"");

        /**
         * todo: use column type to set need for quotes
         */
        quote[col] = false;
      }
      out.println();

      // Output data
      for (row = 0; row < model.getRowCount(); ++row) {
        for (col = 0; col < model.getColumnCount(); ++col) {
          if (col > 0) {
            out.print(",");
          }

          // Get the field content
          try {
            value = model.getValueAt(row, col);
            if (value == null) {
              temp = "";
            } else if (value instanceof java.sql.Timestamp) {
              temp = Utility.formattedDate((java.sql.Timestamp) value);
            } else {
              temp = model.getValueAt(row, col).toString();
              if (!fileExportsRaw.isSelected()) {
                temp = temp.replace('"', '\'');
                temp = temp.replace('\n', '~');
              }
            }
          } catch (Exception any) {
            LOGGER.error("Failed to export data", any);

            // Display error and move on
            messageOut(Resources.getString("errFailDuringExport",
                any.getMessage()), STYLE_RED);
            temp = "";
          }

          // Decide if quotes are needed:
          // -If the row is a known character type
          // -If this field contains a comma
          quoteThis = !fileExportsRaw.isSelected()
              && (quote[col] || temp.indexOf(",") > -1);

          // Output the field
          if (quoteThis) {
            out.print("\"");
          }
          out.print(temp);
          if (quoteThis) {
            out.print("\"");
          }
        }
        if (!fileNoCRAddedToExportRows.isSelected()) {
          out.println();
        }
      }

      // Assume that if writing multiple resultsets to file the
      // user would appreciate a separation line between them.
      if (append) {
        out.println("\n********");
      }

      messageOut(Resources.getString("msgEndExportToFile"));
    } catch (Exception any) {
      LOGGER.error("Unable to write data to file", any);
      messageOut(Resources.getString("errFailDataSave", any.toString()),
          STYLE_RED);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (Exception any) {
          LOGGER.error("Failed while writing data to CSV file", any);
        }
      }
    }

    flashRunIndicator.interrupt();
    flashRunIndicator = null;
    timeRunIndicator.interrupt();
    timeRunIndicator = null;
    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
  }

  /**
   * Get a field from the result set
   * 
   * @param result
   *          The result set
   * @param columnNumber
   *          The column to read data from
   * @param columnType
   *          The data type for the column being read
   * 
   * @return An object representing the data in the current row and selected
   *         column
   */
  private Object getResultField(ResultSet result, int columnNumber,
      int columnType) {
    Object value = null;

    try {
      switch (columnType) {
        case COLUMN_DATA_TYPE_INT: // Integer
          value = new Integer(result.getInt(columnNumber));
          break;
        case COLUMN_DATA_TYPE_LONG: // Long
          value = new Long(result.getLong(columnNumber));
          break;
        case COLUMN_DATA_TYPE_DOUBLE: // Double
          value = new Double(result.getDouble(columnNumber));
          break;
        case COLUMN_DATA_TYPE_DATETIME: // Date/Time
          value = result.getTimestamp(columnNumber);
          break;
        case COLUMN_DATA_TYPE_BLOB: // BLOB
          try {
            value = result.getBlob(columnNumber);
          } catch (Throwable any) {
            LOGGER.error("Error retrieving BLOB field key", any);

            // Probably unsupported method (first found in a MySQL Driver)
            value = result.getString(columnNumber);
          }
          break;
        case 0: // Fall through to default
        default:
          value = result.getString(columnNumber);
          break;
      }
    } catch (Exception any) {
      LOGGER.error("Error Column["
          + columnNumber + " Type[" + columnType + "]", any);
      messageOut(Resources.getString("errFailDataRead",
          "" + columnNumber, any.getMessage()),
          STYLE_YELLOW);
      try {
        value = result.getString(columnNumber);
      } catch (Throwable throwable) {
        LOGGER.error("Error Column["
            + columnNumber + " Type[" + columnType + "]", any);
        messageOut(Resources.getString("errFailDataRead",
                  "" + columnNumber, any.getMessage()),
                  STYLE_RED);
        throw new IllegalStateException("Error Column["
            + columnNumber + " Type[" + columnType + "]");
      }
    }

    return value;
  }

  /**
   * Writes the data onto a file that is specified by the filePath
   * This version writes the data from the ResultSet instance
   * so that the results do not need to be loaded into memory.
   * 
   * @param query
   *          The SQL statement for the current data
   * @param model
   *          The model for the current data
   * @param filePath
   *          A String that denotes the filepath
   * @param result
   *          The result set for the query
   * @param myType
   *          The data type ids for the columns in the result set
   * @param append
   *          Whether to append the data in the file
   * 
   * @todo Make this handle other data types (especially Date/Time) better
   *       for cleaner import to spreadsheet.
   */
  private void writeDataAsCSV(String query, TableModel model, String filePath,
      ResultSet result, int[] myType, boolean append) {
    PrintWriter out;
    int row, col;
    boolean[] quote;
    boolean quoteThis;
    String temp;
    Object value;

    out = null;

    /*
     * this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
     * (flashRunIndicator = new Thread(new FlashForeground(runIndicator,
     * Color.red.brighter(), Color.lightGray, 5))).start();
     * (timeRunIndicator = new Thread(new InsertTime(timeIndicator,
     * new java.util.Date().getTime(),
     * 250))).start();
     */

    try {
      out = new PrintWriter(new FileWriter(filePath, append));

      messageOut("");
      messageOut(Resources.getString("msgStartExportToFile", filePath));

      // Output query
      if (!fileExportsRaw.isSelected()) {
        // Make it look like a comment using hash comment character
        out.print("#,");

        out.print("\"");
      }
      // out.print(getQuery().getSQL());
      out.print(query.replaceAll("\n", " "));
      if (!fileExportsRaw.isSelected()) {
        out.print("\"");
      }
      out.println();

      // Setup array to hold which columns need to be quoted
      quote = new boolean[model.getColumnCount()];

      // Output column headings - determine if column needs quotes
      for (col = 0; col < model.getColumnCount(); ++col) {
        if (col > 0) {
          out.print(",");
        }
        out.print("\"" + model.getColumnName(col) + "\"");

        /**
         * todo: use column type to set need for quotes
         */
        quote[col] = false;
      }
      out.println();

      // Output data
      row = 0;
      while (result.next()) {
        if (row % RESULT_PROCESSING_ROWS_PER_STATUS_MESSAGE == 0) {
          messageOut("" + row, STYLE_SUBTLE);
        }
        for (col = 0; col < model.getColumnCount(); ++col) {
          if (col > 0) {
            out.print(",");
          }

          // Get the field content
          value = getResultField(result, col + 1, myType[col]);
          try {
            if (value == null) {
              temp = "";
            } else if (value instanceof java.sql.Timestamp) {
              temp = Utility.formattedDate((java.sql.Timestamp) value);
            } else {
              temp = value.toString();
              if (!fileExportsRaw.isSelected()) {
                temp = temp.replace('"', '\'');
                temp = temp.replace('\n', '~');
              }
            }
          } catch (Exception any) {
            LOGGER.error("Failed to export data", any);

            // Display error and move on
            messageOut(Resources.getString("errFailDuringExport",
                any.getMessage()), STYLE_RED);
            temp = "";
          }

          // Decide if quotes are needed:
          // -If the row is a known character type
          // -If this field contains a comma
          quoteThis = !fileExportsRaw.isSelected()
              && (quote[col] || temp.indexOf(",") > -1);

          // Output the field
          if (quoteThis) {
            out.print("\"");
          }
          out.print(temp);
          if (quoteThis) {
            out.print("\"");
          }
        }
        if (!fileNoCRAddedToExportRows.isSelected()) {
          out.println();
        }
        ++row;
      }

      // Assume that if writing multiple resultsets to file the
      // user would appreciate a separation line between them.
      if (append) {
        out.println("\n********");
      }

      messageOut(Resources.getString("msgEndExportToFile"));
    } catch (Exception any) {
      LOGGER.error("Unable to write data to file", any);
      messageOut(Resources.getString("errFailDataSave", any.toString()),
          STYLE_RED);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (Exception any) {
          LOGGER.error("Failed while writing data to CSV file", any);
        }
      }
    }

    /*
     * flashRunIndicator.interrupt();
     * flashRunIndicator = null;
     * timeRunIndicator.interrupt();
     * timeRunIndicator = null;
     * this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
     */
  }

  /**
   * Sets the selected query as the text in the querytext area
   */
  private void setupSelectedQuery() {
    Query query;

    query = getQuery();

    if (query != null) {
      selectMode(getQuery().getMode());
      queryText.setText(getQuery().getSql());

      if (commentedQuery()) {
        commentToggle.setText(Resources.getString("ctlUncomment"));
        execute.setEnabled(false);
      } else {
        commentToggle.setText(Resources.getString("ctlComment"));
        execute.setEnabled(true);
      }
    } else {
      queryText.setText("");
    }

    setPrevNextIndication();
    setExportAvailable(false);
  }

  /**
   * Executes the query(ies). It is expected that this will be
   * run on its own thread -- hence it is a run method.
   */
  public void run() {

    this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    (flashRunIndicator = new Thread(new FlashForeground(runIndicator,
        Color.red.brighter(), Color.lightGray, 5))).start();
    (timeRunIndicator = new Thread(new InsertTime(timeIndicator,
        new java.util.Date().getTime(),
        QUERY_EXECUTION_TIMER_UPDATE_DELAY_MS))).start();
    try {
      if (runType == RUNTYPE_ALL) {
        runAllQueries();
      } else if (runType == RUNTYPE_TRIPLE_EXPORT) {
        message.setText("");
        messageOut(Resources.getString("msgStartExportToFile",
            exportFile.getAbsolutePath()), STYLE_BOLD);
        messageOut("");
        execute(getQuery().getSql(), new ListTableModel<Object>(), exportFile);
      } else {
        checkForNewString(querySelection);
        checkForNewString(connectString);
        processStatement();
      }
    } catch (Throwable any) {
      LOGGER.error("Failure while trying to execute query", any);
      messageOut(Resources.getString("errFailSQLText", any.getMessage()),
          STYLE_RED);
    }
    flashRunIndicator.interrupt();
    flashRunIndicator = null;
    timeRunIndicator.interrupt();
    timeRunIndicator = null;
    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
  }

  /**
   * Executes all the queries
   */
  private void runAllQueries() {
    int numExecutions, numQueries;
    int execution, query;
    ProgressMonitor monitor;
    boolean canceled;
    java.util.Date start;
    int elapsed;
    long remaining;
    int hours, minutes, seconds;

    numQueries = querySelection.getModel().getSize();
    try {
      numExecutions = Integer.parseInt(JOptionPane.showInputDialog(this,
          Resources.getString("dlgAllQueriesText"),
          Resources.getString("dlgAllQueriesTitle"),
          JOptionPane.QUESTION_MESSAGE));
    } catch (Exception any) {
      numExecutions = 0;
    }

    messageOut(Resources.getString("msgNumExecutions", numExecutions + ""));

    monitor = new ProgressMonitor(this,
        Resources.getString("dlgRunAllQueriesProgressTitle"),
        Resources.getString("dlgRunAllQueriesProgressNote",
            "0", numExecutions + "", "0", numQueries + ""), 0,
        numQueries * Math.abs(numExecutions));

    getContentPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));

    if (numExecutions < 0) {
      numExecutions = Math.abs(numExecutions);

      for (query = 0; !monitor.isCanceled() && query < numQueries; ++query) {
        querySelection.setSelectedIndex(query);
        elapsed = -1;
        for (execution = 0; !monitor.isCanceled() && execution < numExecutions; ++execution) {
          monitor.setProgress(query * numExecutions + execution);
          if (elapsed <= 0) {
            monitor.setNote(Resources.getString("dlgRunAllQueriesProgressNote",
                (execution + 1) + "", numExecutions + "", (query + 1) + "",
                numQueries + ""));
          } else {
            remaining = elapsed
                * ((numExecutions * (numQueries - (query + 1)))
                    + (numExecutions - execution));
            hours = (int) (remaining / SECONDS_PER_HOUR);
            remaining -= hours * SECONDS_PER_HOUR;
            minutes = (int) (remaining / SECONDS_PER_MINUTE);
            remaining -= minutes * SECONDS_PER_MINUTE;
            seconds = (int) remaining;

            monitor.setNote(Resources.getString(
                "dlgRunAllQueriesProgressNoteWithRemainTime",
                (execution + 1) + "", numExecutions + "", (query + 1) + "",
                numQueries + "", Utility.formattedNumber(hours, "00"),
                Utility.formattedNumber(minutes, "00"),
                Utility.formattedNumber(seconds, "00")));
          }
          messageOut(Resources.getString("dlgRunAllQueriesProgressNote",
              (execution + 1) + "", numExecutions + "", (query + 1) + "",
              numQueries + ""));
          start = new java.util.Date();
          processStatement(true);
          elapsed = (int) ((new java.util.Date().getTime()
              - start.getTime()) / 1000);
        }
      }
    } else {
      elapsed = -1;
      for (execution = 0; !monitor.isCanceled() && execution < numExecutions; ++execution) {
        start = new java.util.Date();
        for (query = 0; !monitor.isCanceled() && query < numQueries; ++query) {
          querySelection.setSelectedIndex(query);
          monitor.setProgress(execution * numQueries + query);

          if (elapsed <= 0) {
            monitor.setNote(Resources.getString("dlgRunAllQueriesProgressNote",
                (execution + 1) + "", numExecutions + "", (query + 1) + "",
                numQueries + ""));
          } else {
            remaining = elapsed * (numExecutions - execution);
            hours = (int) (remaining / SECONDS_PER_HOUR);
            remaining -= hours * SECONDS_PER_HOUR;
            minutes = (int) (remaining / SECONDS_PER_MINUTE);
            remaining -= minutes * SECONDS_PER_MINUTE;
            seconds = (int) remaining;

            monitor.setNote(Resources.getString(
                "dlgRunAllQueriesProgressNoteWithRemainTime",
                (execution + 1) + "", numExecutions + "", (query + 1) + "",
                numQueries + "", Utility.formattedNumber(hours, "00"),
                Utility.formattedNumber(minutes, "00"),
                Utility.formattedNumber(seconds, "00")));
          }

          messageOut(Resources.getString("dlgRunAllQueriesProgressNote",
              (execution + 1) + "", numExecutions + "", (query + 1) + "",
              numQueries + ""));
          processStatement(true);
        }
        elapsed = (int) ((new java.util.Date().getTime()
            - start.getTime()) / 1000);
      }
    }

    canceled = monitor.isCanceled();

    monitor.close();

    getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    if (canceled) {
      userMessage(Resources.getString("dlgExecutionsCanceledText"),
          Resources.getString("dlgExecutionsCanceledTitle"),
          JOptionPane.WARNING_MESSAGE);
    } else if (numExecutions != 0) {
      userMessage(Resources.getString("dlgExecutionsCompletedText"),
          Resources.getString("dlgExecutionsCompletedTitle"),
          JOptionPane.INFORMATION_MESSAGE);
    } else {
      userMessage(Resources.getString("dlgExecutionsNoneText"),
          Resources.getString("dlgExecutionsNoneTitle"),
          JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * Uncomments the selected query statement if it is commented or
   * comments the statement if it is uncommented and replaces the query in
   * the combo box.
   */
  private void commentToggle() {
    String newSQL;
    int position;
    Query query;

    if ((position = querySelection.getSelectedIndex()) > -1) {
      query = getQuery();
      newSQL = query.getSql();

      LOGGER.debug("commentToggle::Orig[" + newSQL + "]");

      if (newSQL.startsWith(COMMENT_PREFIX)) {
        newSQL = newSQL.substring(2);
      } else {
        newSQL = COMMENT_PREFIX + newSQL;
      }

      LOGGER.debug("commentToggle:New[" + newSQL + "]");

      replaceInCombo(querySelection, position,
          new Query(newSQL, query.getMode()));
    }
  }

  /**
   * Builds the GUI window title - including the currently open SQL statement
   * file.
   * 
   * @param pathToSQLFile
   *          The path to the currently open SQL statement file
   */
  private void setQueryFilename(String pathToSQLFile) {
    queryFilename = pathToSQLFile;
    setTitle("BasicQuery - " + queryFilename);
  }

  /**
   * Open or create a SQL statement file.
   */
  private void openSQLFile() {
    JFileChooser fileMenu;
    FileFilter defaultFileFilter = null;
    FileFilter preferredFileFilter = null;
    File chosenSQLFile;
    int returnVal;

    chosenSQLFile = null;

    // Save current information, including SQL Statements
    saveConfig();

    // Allow user to choose/create new file for SQL Statements
    fileMenu = new JFileChooser(new File(queryFilename));

    for (FileFilterDefinition filterDefinition : FileFilterDefinition.values()) {
      if (filterDefinition.name().startsWith("QUERY")) {
        final FileFilter fileFilter = new SuffixFileFilter(
            filterDefinition.description(), filterDefinition.acceptedSuffixes());
        if (filterDefinition.isPreferredOption()) {
          preferredFileFilter = fileFilter;
        }
        fileMenu.addChoosableFileFilter(fileFilter);
        if (filterDefinition.description().equals(
            latestChosenQueryFileFilterDescription)) {
          defaultFileFilter = fileFilter;
        }
      }
    }

    if (defaultFileFilter != null) {
      fileMenu.setFileFilter(defaultFileFilter);
    } else if (latestChosenQueryFileFilterDescription != null
        && latestChosenQueryFileFilterDescription.startsWith("All")) {
      fileMenu.setFileFilter(fileMenu.getAcceptAllFileFilter());
    } else if (preferredFileFilter != null) {
      fileMenu.setFileFilter(preferredFileFilter);
    }

    fileMenu.setSelectedFile(new File(queryFilename));
    fileMenu.setDialogTitle(Resources.getString("dlgSQLFileTitle"));
    fileMenu.setDialogType(JFileChooser.OPEN_DIALOG);
    fileMenu.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileMenu.setMultiSelectionEnabled(false);

    if (fileMenu.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      chosenSQLFile = fileMenu.getSelectedFile();

      // Adjust file suffix if necessary
      final FileFilter fileFilter = fileMenu.getFileFilter();
      if (fileFilter != null && fileFilter instanceof SuffixFileFilter
          && !fileMenu.getFileFilter().accept(chosenSQLFile)) {
        chosenSQLFile = ((SuffixFileFilter) fileFilter)
            .makeWithPrimarySuffix(chosenSQLFile);
      }

      if (!chosenSQLFile.exists()) {
        returnVal = JOptionPane.showConfirmDialog(this,
            Resources.getString("dlgNewSQLFileText", chosenSQLFile.getName()),
            Resources.getString("dlgNewSQLFileTitle"),
            JOptionPane.
            YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        if (returnVal == JOptionPane.NO_OPTION) {
          querySelection.removeAllItems();
          queryText.setText("");
          QueryHistory.getInstance().clearAllQueries();

          // Update GUI
          setPrevNextIndication();
        } else if (returnVal == JOptionPane.CANCEL_OPTION) {
          chosenSQLFile = null;
        }
      } else {
        setQueryFilename(chosenSQLFile.getAbsolutePath());
        querySelection.removeAllItems();
        queryText.setText("");
        loadCombo(querySelection, queryFilename);
        QueryHistory.getInstance().clearAllQueries();

        // Update GUI
        setPrevNextIndication();
      }
    }

    try {
      latestChosenQueryFileFilterDescription = fileMenu.getFileFilter()
          .getDescription();
    } catch (Throwable throwable) {
      LOGGER.warn("Unable to determine which ontology file filter was chosen",
          throwable);
    }

    if (chosenSQLFile != null) {
      setQueryFilename(chosenSQLFile.getAbsolutePath());
      saveConfig();
    }
  }

  /**
   * Add latest query index to the history of executed queries
   * The query is added after the currently selected query in the history list.
   * The associated connect URL is also saved, though its use is controlled by a
   * menu checkbox setting.
   * 
   * @param results
   *          The table model with the current query results
   */
  private void histMaintQueryExecuted(TableModel results) {
    final int thisQuery = querySelection.getSelectedIndex();

    if (thisQuery >= 0) {
      QueryHistory.getInstance().addQuery(new QueryInfo(thisQuery,
            connectString.getSelectedIndex(),
            results));
    }

    // Update GUI
    setPrevNextIndication();
  }

  /**
   * Activate/deactivate the previous and next query history indicators based
   * on the number of history entries and the current position within the
   * history
   * list.
   */
  private void setPrevNextIndication() {
    previousQuery
        .setEnabled(QueryHistory.getInstance().hasPrevious()
            || (QueryHistory.getInstance().getNumberOfQueries() > 0 && QueryHistory
                .getInstance().getCurrentQueryInfo().getSQLIndex() != querySelection
                .getSelectedIndex()));
    nextQuery.setEnabled(QueryHistory.getInstance().hasNext());
  }

  /**
   * Maintain the query history list when an entry is deleted from the
   * collection stored of SQL queries. For each entry in the history list the
   * impact of a deletion may be:
   * <ul>
   * <li>None, if the deleted query follows the index of the history entry
   * <li>Require subtracting one from the SQL history index position if it
   * follows the deleted query; or
   * <li>Require removal of the history entry if the SQL index held is the index
   * of the deleted query.
   * </ul>
   * 
   * @param queryIndex
   *          The index of the deleted query in the stored query list.
   */
  private void histMaintQueryDeleted(int queryIndex) {
    QueryHistory.getInstance().deleteQueryAtIndex(queryIndex);

    // Update GUI
    setPrevNextIndication();
  }

  /**
   * Set the currently selected query to the previous SQL index in the history
   * list.
   * If the user has selected to also associate the connect URL, then the last
   * connect
   * URL used for the query will also be selected.
   */
  private void histSelectPrev() {
    int currentSQLIndex, currentConnectIndex;
    int histSQLIndex, histConnectIndex;
    boolean newQuery = false;
    TableModel histModel = null;
    TableSorter sorter;
    QueryInfo queryInfo;

    currentSQLIndex = querySelection.getSelectedIndex();
    currentConnectIndex = connectString.getSelectedIndex();

    /**
     * Two possibilities, the user has changed the selected query and/or
     * connection string without executing and wants to return to the last
     * executed; or the latest executed query is currently selected in which
     * case the user expects to return to a query before this one
     */
    if (QueryHistory.getInstance().getNumberOfQueries() > 0) {
      queryInfo = QueryHistory.getInstance().getCurrentQueryInfo();
      histSQLIndex = queryInfo.getSQLIndex();
      histConnectIndex = queryInfo.getURLIndex();

      if (histSQLIndex == currentSQLIndex
          && histConnectIndex == currentConnectIndex) {
        QueryHistory.getInstance().moveBackward();
        queryInfo = QueryHistory.getInstance().getCurrentQueryInfo();
        histSQLIndex = queryInfo.getSQLIndex();
        histConnectIndex = queryInfo.getURLIndex();
        histModel = queryInfo.getResults();
        newQuery = true;
      }

      querySelection.setSelectedIndex(histSQLIndex);

      if (configHistoryAssocSQLAndConnect.isSelected()) {
        connectString.setSelectedIndex(histConnectIndex);
      }

      if (newQuery) {
        try {
          sorter = (TableSorter) table.getModel();
          sorter.removeMouseListenerFromHeaderInTable(table);
        } catch (Throwable any) {
          // Probably table was empty
          if (LOGGER.isDebugEnabled()) {
            LOGGER
                .debug(
                    "Error when sorting results, probably no results in model",
                    any);
          }
        }
        table.setModel(new ListTableModel<Object>());
        if (histModel != null) {
          sorter = new TableSorter(histModel);
          table.setModel(sorter);
          sorter.addMouseListenerToHeaderInTable(table);
          Utility.initColumnSizes(table, sorter);
        }
      }

      // Update GUI
      setPrevNextIndication();
    }
  }

  /**
   * Set the currently selected query to the next SQL index in the history list.
   * If the user has selected to also associate the connect URL, then the last
   * connect
   * URL used for the query will also be selected.
   */
  private void histSelectNext() {
    int histSQLIndex, histConnectIndex;
    TableModel histModel = null;
    TableSorter sorter;
    QueryInfo queryInfo;

    QueryHistory.getInstance().moveForward();
    queryInfo = QueryHistory.getInstance().getCurrentQueryInfo();
    histSQLIndex = queryInfo.getSQLIndex();
    histConnectIndex = queryInfo.getURLIndex();
    histModel = queryInfo.getResults();

    querySelection.setSelectedIndex(histSQLIndex);

    if (configHistoryAssocSQLAndConnect.isSelected()) {
      connectString.setSelectedIndex(histConnectIndex);
    }

    try {
      sorter = (TableSorter) table.getModel();
      sorter.removeMouseListenerFromHeaderInTable(table);
    } catch (Throwable any) {
      // Probably table was empty
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(
              "Error when sorting results, probably no results in model", any);
      }
    }
    table.setModel(new ListTableModel<Object>());
    if (histModel != null) {
      sorter = new TableSorter(histModel);
      table.setModel(sorter);
      sorter.addMouseListenerToHeaderInTable(table);
      Utility.initColumnSizes(table, sorter);
    }

    // Update GUI
    setPrevNextIndication();
  }

  /**
   * Allow the user to place the queries into a particluar order. A separate
   * dialog is used that permits rearranging the list of queries.
   */
  private void reorderQueries() {
    List<Object> queries;

    queries = new ArrayList<Object>();

    for (int loop = 0; loop < querySelection.getItemCount(); ++loop) {
      queries.add(querySelection.getItemAt(loop));
    }

    final ReorderDialog setOrder = new ReorderDialog(this, queries);

    if (setOrder.isReordered()) {
      rebuildSQLListing(setOrder.getAsOrdered());
    }
  }

  /**
   * Reloads the list of queries in the combobox.
   * 
   * @param queries
   *          List The collection of queries
   */
  private void rebuildSQLListing(List<Object> queries) {
    QueryHistory.getInstance().clearAllQueries();

    querySelection.removeAllItems();
    for (int loop = 0; loop < queries.size(); ++loop) {
      addToCombo(querySelection, queries.get(loop));
    }
  }

  /**
   * Sort a table model based on the selected columns.
   */
  private void sortSelectedColumns() {
    int[] selectedViewColumns;
    int[] selectedModelColumns;
    TableSorter sorter;

    selectedViewColumns = table.getSelectedColumns();
    selectedModelColumns = new int[selectedViewColumns.length];

    for (int column = 0; column < selectedViewColumns.length; ++column) {
      selectedModelColumns[column] =
          table.convertColumnIndexToModel(selectedViewColumns[column]);
    }

    try {
      sorter = (TableSorter) table.getModel();
      sorter.sortByColumns(selectedModelColumns);
    } catch (Throwable any) {
      // Probably table was empty
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(
            "Error when sorting results, probably no results in model", any);
      }
    }
  }

  /**
   * Run a SQL statement on its own thread. If a statement is currently being
   * executed the user may either cancel the new request or the currently
   * running query.
   * 
   * @param statementRunType
   *          The type of query execution -- single or
   *          multiple statement
   */
  private void runIt(int statementRunType) {
    if (runningQuery != null) {
      if (!runningQuery.isAlive()) {
        runningQuery = null;
      } else {
        final int decision = JOptionPane.showConfirmDialog(this,
            Utility.
                characterInsert(Resources.getString("dlgCancelQueryText"),
                    "\n",
                    40, 60, " ."), Resources.getString("dlgCancelQueryTitle"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (decision == JOptionPane.YES_OPTION) {
          runningQuery.interrupt();
          runningQuery = null;
          flashRunIndicator.interrupt();
          flashRunIndicator = null;
          timeRunIndicator.interrupt();
          timeRunIndicator = null;
          this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
      }
    }

    if (runningQuery == null) {
      runType = statementRunType;
      (runningQuery = new Thread(this)).start();
    }
  }

  /**
   * Notify user if no DB drivers have been loaded. Probably no DB JARs
   * installed.
   */
  private void noDBDriverWarning() {
    JOptionPane.showMessageDialog(
        this,
        Resources.getString("dlgNoDBDriversLoadedText",
            new File(dbDriverDir).getAbsolutePath(),
            Configuration.instance().getFile(FILENAME_DRIVERS)
                .getAbsolutePath()),
        Resources.getString("dlgNoDBDriversLoadedTitle"),
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Displays an information message in a dialog box giving details about the
   * BasicQuery that has been executed
   */
  private void about() {
    String slMessage;

    slMessage = "Execute arbitrary SQL statements against"
        + " any database with JDBC library.Records timing"
        + " and memory usage information to assist in"
        + " performance tuning of database interactions.\n\n";

    slMessage += "David Read, www.monead.com\n\n";

    slMessage += "Copyright (c) 2004-2014\n\n"
        + "This program is free software; you can redistribute it and/or modify "
        +
        "it under the terms of the GNU General Public License as published by "
        +
        "the Free Software Foundation; either version 2 of the License, or "
        + "(at your option) any later version.\n\n"
        + "This program is distributed in the hope that it will be useful, "
        + "but WITHOUT ANY WARRANTY; without even the implied warranty of "
        + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the "
        + "GNU General Public License for more details.\n\n"
        + "You should have received a copy of the GNU General Public License "
        + "along with this program; if not, write to the:\n"
        + "     Free Software Foundation Inc.\n"
        + "     59 Temple Place, Suite 330\n"
        + "     Boston, MA  02111-1307  USA";

    userMessage("BasicQuery SQL Execution Tool\n\n"
        + "Version: " + VERSION + "\n\n"
        + Utility.characterInsert(slMessage, "\n", 70, 90, " ."),
        "About BasicQuery", JOptionPane.INFORMATION_MESSAGE, false);
  }

  /**
   * Supply a brief usage summary for the parameterized SQL syntax.
   */
  private void helpParameterizedSQL() {
    String messageText;
    String[] dataTypes;
    int loop;

    dataTypes = SQLTypes.getKnownTypeNames();

    messageText = Resources.getString("dlgUsingParamsTextOverview") + " ";

    for (loop = 0; loop < dataTypes.length; ++loop) {
      messageText += dataTypes[loop];
      if (loop < dataTypes.length - 1) {
        messageText += ", ";
      }
      if ((loop + 1) % 5 == 0) {
        messageText += "\n                           ";
      }
    }

    messageText += Resources.getString("dlgUsingParamsTextExamples") + " ";

    JOptionPane.showMessageDialog(this, messageText,
        Resources.getString("dlgUsingParamsTitle"),
        JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Advance the list of queries to the next query if the list is not
   * already at the end.
   */
  private void nextQuerySelection() {
    if (querySelection.getSelectedIndex() + 1 < querySelection.getItemCount()) {
      querySelection.setSelectedIndex(querySelection.getSelectedIndex() + 1);
    }
  }

  /**
   * Select all the data in the result table
   */
  private void selectAllData() {
    table.selectAll();
  }

  /**
   * Enable or disable the export menu items based on the latest query
   * 
   * @param available
   *          Whether the export options should be enabled
   */
  private void setExportAvailable(boolean available) {
    fileSaveAsCSV.setEnabled(available);
    fileSaveAsTriples.setEnabled(available);
  }

  /**
   * Copy the selected data cells to the clipboard
   */
  private void copySelectionToClipboard() {
    Action objlCopy;

    try {
      objlCopy = TransferHandler.getCopyAction();
      objlCopy.actionPerformed(new ActionEvent(table,
          ActionEvent.ACTION_PERFORMED,
          (String) objlCopy.getValue(
              Action.
              NAME), EventQueue.getMostRecentEventTime(), 0));
    } catch (Throwable any) {
      LOGGER.error("Failed to copy data to clipboard", any);
      JOptionPane.showMessageDialog(this,
          Resources.getString("errClipboardCopyDataText",
              any.getClass().getName(), any.getMessage() != null
                  ? any.getMessage() : ""),
          Resources.getString("errClipboardCopyDataTitle"),
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Configure the data display based on the user-chosen row formatting.
   */
  private void setupResultsTableColoring() {
    cellRenderer.clearColors();
    if (configTableColoringGreenBar.isSelected()) {
      cellRenderer.addAlternatingRowColor(Color.black, Color.white);
      cellRenderer
          .addAlternatingRowColor(Color.black, new Color(COLOR_GREEN_R_VALUE,
              COLOR_GREEN_G_VALUE, COLOR_GREEN_B_VALUE));
    } else if (configTableColoringYellowBar.isSelected()) {
      cellRenderer.addAlternatingRowColor(Color.black, Color.white);
      cellRenderer
          .addAlternatingRowColor(Color.black, new Color(COLOR_YELLOW_R_VALUE,
              COLOR_YELLOW_G_VALUE, COLOR_YELLOW_B_VALUE));
    } else if (configTableColoringUserDefined.isSelected()) {
      setupUserDefinedColoring();
    }

    if (table != null) {
      table.repaint();
    }
  }

  /**
   * Allow the user to select a font (including size/bold/italic). This setting
   * is used for the message area and the data table display.
   */
  private void chooseFont() {
    FontChooser fontChooser;

    fontChooser = new FontChooser(this);
    fontChooser.setVisible(true);

    // Font used in both table display and message display
    if (fontChooser.getNewFont() != null) {
      setupFont(fontChooser.getNewFont());
    }
  }

  /**
   * Set the font for the message area and table to match the frame's font.
   * The frame's font will either be the default font, if the user has not
   * configured a specific font, or it will be the user-chosen font.
   * 
   * @param font
   *          Font The font to use for the message and table displays.
   */
  private void setupFont(Font font) {
    if (font != null) {
      this.setFont(font);
      message.setFont(font);
      cellRenderer.setText(
          "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
      cellRenderer.setFont(font);

      // For some reason the cellRenderer preferred size equals the font height
      // if no cells are rendered at the moment. Tried creating a one-row
      // model and displaying while changing font size but it didn't help --
      // probably because it wasn't rendered until after the setupFont method
      // completed.
      if (table.getRowCount() == 0) {
        table.setRowHeight(cellRenderer.getPreferredSize().height + 5);
      } else {
        table.setRowHeight(cellRenderer.getPreferredSize().height);
      }
      // table.setRowHeight(this.getGraphics().getFontMetrics(font).getHeight());
      table.getTableHeader().setFont(font);
    }
  }

  /**
   * Check for a new version. The checking class will notify this class if a
   * newer version is available.
   */
  private void checkForNewVersion() {
    final CheckLatestVersion versionCheck = new CheckLatestVersion(VERSION);
    versionCheck.addObserver(this);
    new Thread(versionCheck).start();
  }

  /**
   * Notify the user that a newer version of the program has been released.
   * 
   * @param newVersionInformation
   *          Information about the new version of the program
   */
  private void notifyNewerVersion(NewVersionInformation newVersionInformation) {
    if (newVersionInformation.getUrlToDownloadPage() != null) {
      int choice;

      choice = JOptionPane.showConfirmDialog(
          this,
          "There is a newer version of BasicQuery Available\n"
              + "You are running version " + VERSION
              + " and the latest version is "
              + newVersionInformation.getLatestVersion()
              + "\n\n"
              + newVersionInformation.getDownloadInformation()
              + "\n"
              + "New features include:\n"
              + newVersionInformation.getNewFeaturesDescription() + "\n\n"
              + "Would you like to download the new version now?\n\n",
          "Newer Version Available (" + VERSION + "->"
              + newVersionInformation.getLatestVersion() + ")",
          JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

      if (choice == JOptionPane.YES_OPTION) {
        try {
          Desktop.getDesktop().browse(
              newVersionInformation.getUrlToDownloadPage().toURI());
        } catch (Throwable throwable) {
          LOGGER.error("Cannot launch browser to access download page",
              throwable);
          JOptionPane.showMessageDialog(this,
              "Unable to launch a browser to access the download page\n"
                  + "at "
                  + newVersionInformation.getUrlToDownloadPage().toString()
                  + "\n\n" + throwable.getMessage(),
              "Unable to Access Download Page", JOptionPane.ERROR_MESSAGE);
        }
      }
    } else {
      JOptionPane.showMessageDialog(
          this,
          "There is a newer version of BasicQuery Available\n"
              + "You are running version " + VERSION
              + " and the latest version is "
              + newVersionInformation.getLatestVersion()
              + "\n\n"
              + "New features include:\n"
              + newVersionInformation.getNewFeaturesDescription(),
          "Newer Version Available (" + VERSION + "->"
              + newVersionInformation.getLatestVersion() + ")",
          JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * Cleanup environment. Normally called before closing application.
   */
  private void cleanUp() {
    if (conn != null) {
      try {
        conn.close();
      } catch (Throwable any) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Unable to close connection during shutdown", any);
        }
      }
      conn = null;
    }
  }

  /**
   * Exit the program. Writes out the current configuration before exiting.
   */
  private void exitProgram() {
    saveConfig();
    cleanUp();
    try {
      dispose();
    } catch (Throwable any) {
      // This fails periodically with a NP exception
      // from Container.removeNotify
      LOGGER.warn("Error during application shutdown", any);
    }

    System.exit(0);
  }

  // Begin ActionListener Interface

  /**
   * Depending on which option the user has invoked the
   * appropriate action is performed
   * 
   * @param evt
   *          Specifies the action event that has taken place
   */
  public void actionPerformed(ActionEvent evt) {
    Object source;

    source = evt.getSource();

    if (source == execute) {
      runIt(RUNTYPE_SINGLE);
    } else if (source == remove) {
      removeSelectedQuery();
    } else if (source == commentToggle) {
      commentToggle();
    } else if (source == nextInList) {
      nextQuerySelection();
    } else if (source == querySelection) {
      setupSelectedQuery();
    } else if (source == fileSaveBLOBs) {
      saveBLOBs();
    } else if (source == fileExit) {
      exitProgram();
    } else if (source == querySelectStar) {
      queryStar(Query.MODE_QUERY);
    } else if (source == queryDescribeStar) {
      queryStar(Query.MODE_DESCRIBE);
    } else if (source == queryMakeInsert) {
      makeInsert();
    } else if (source == queryMakeUpdate) {
      makeUpdate();
    } else if (source == querySetOrder) {
      reorderQueries();
    } else if (source == fileOpenSQL) {
      openSQLFile();
    } else if (source == fileSaveAsCSV) {
      chooseFileForSave(table.getModel(), FILE_EXPORT_CSV);
    } else if (source == queryMakeVerboseSelect) {
      makeVerboseSelect();
    } else if (source == queryRunAll) {
      runIt(RUNTYPE_ALL);
    } else if (source == helpAbout) {
      about();
    } else if (source == helpParameterizedSQL) {
      helpParameterizedSQL();
    } else if (source == editCopy) {
      copySelectionToClipboard();
    } else if (source == editSelectAll) {
      selectAllData();
    } else if (source == editSort) {
      sortSelectedColumns();
    } else if (source == previousQuery) {
      histSelectPrev();
    } else if (source == nextQuery) {
      histSelectNext();
    } else if (source == configTableColoringNone
        || source == configTableColoringGreenBar
        || source == configTableColoringYellowBar
        || source == configTableColoringUserDefined) {
      setupResultsTableColoring();
    } else if (source == configLanguageDefault
        || source == configLanguageEnglish
        || source == configLanguageFrench
        || source == configLanguageGerman
        || source == configLanguageItalian
        || source == configLanguagePortuguese
        || source == configLanguageSpanish) {
      changeLanguage();
    } else if (source == configFont) {
      chooseFont();
    } else if (source == asDescribe || source == asQuery || source == asUpdate) {
      setExportAvailable(false);
    }
  }

  // End ActionListener Interface

  // Begin WindowListener Interface

  /**
   * Invoked when the Window is set to be the active Window.
   * 
   * @param evt
   *          The event that specifies that the window is activated
   */
  public void windowActivated(WindowEvent evt) {
  }

  /**
   * Invoked when a window has been closed as the result of calling
   * dispose on the window.
   * 
   * @param evt
   *          The Window event
   */
  public void windowClosed(WindowEvent evt) {
  }

  /**
   * Invoked when the user attempts to close the window from the
   * window's system menu.Exits the application once the window
   * is closed
   * 
   * @param evt
   *          The event that specifies the closing of the window
   */
  public void windowClosing(WindowEvent evt) {
    exitProgram();
  }

  /**
   * Invoked when a Window is no longer the active Window
   * 
   * @param evt
   *          The event that specifies that the window is deactivated
   */
  public void windowDeactivated(WindowEvent evt) {
  }

  /**
   * Invoked when a window is changed from a minimized to a normal state.
   * 
   * @param evt
   *          The event that specifies that the window is deiconified
   */
  public void windowDeiconified(WindowEvent evt) {
  }

  /**
   * Invoked when a window is changed from a normal to a minimized state
   * 
   * @param evt
   *          The event that specifies that the window is iconified
   */
  public void windowIconified(WindowEvent evt) {
  }

  /**
   * Creates a new thread that attaches focus to the window that has been
   * opened and also places the cursor on the textfield for the userid
   * 
   * @param evt
   *          The event that specifies the opening of the window
   */
  public void windowOpened(WindowEvent evt) {
    new Thread(new FocusRequestor(userId)).start();
    if (!loadedDBDriver) {
      noDBDriverWarning();
    }
  }

  // End WindowListener Interface

  @Override
  public void update(Observable o, Object arg) {
    LOGGER.debug("Update received from " + o.getClass().getName());

    if (o instanceof CheckLatestVersion) {
      notifyNewerVersion((NewVersionInformation) arg);
    }
  }

  /**
   * Handle menu selection for exporting the results to triples
   */
  private class ExportResultsAsTriplesListener implements ActionListener {
    /**
     * No operation
     */
    public ExportResultsAsTriplesListener() {

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      chooseFileForSave(table.getModel(), FILE_EXPORT_TRIPLES);
    }
  }

  @Override
  public void keyPressed(KeyEvent arg0) {

  }

  @Override
  public void keyReleased(KeyEvent arg0) {
    setExportAvailable(false);
  }

  @Override
  public void keyTyped(KeyEvent arg0) {

  }

  /**
   * Main startup method for the BasicQuery application.
   * Creates an object of the class BasicQuery.
   * 
   * @param args
   *          Input arguments -- not currently used
   */
  public static final void main(String[] args) {
    try {
      new BasicQuery(true);
    } catch (Throwable any) {
      LOGGER.fatal("Application failure", any);

      // In case logger isn't setup - send this fatal issue to standard out
      System.out.println("BasicQuery:main:Exception:" + any);
      any.printStackTrace();
    }
  }
}
