package us.daveread.basicquery;

import java.sql.*;
import java.util.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.table.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Array;

import org.apache.commons.dbcp.*;
import org.apache.commons.pool.*;
import org.apache.commons.pool.impl.*;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;

import us.daveread.basicquery.gui.*;
import us.daveread.basicquery.util.*;
import us.daveread.basicquery.images.ImageUtility;

/**
 * Title:        Basic Query Utility
 * <p>Execute arbitrary SQL against database accessible with any
 * JDBC-compliant driver.</p>
 *
 * <p>Copyright: Copyright (c) 2004, David Read</p>
 *
 * <p>This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.</p>
 *
 * <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.</p>
 *
 * <p>You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA</p>
 *
 * <p></p>
 *
 * @author David Read
 * @version $Id: BasicQuery.java,v 1.11 2006/05/30 18:33:55 daveread Exp $
 */

public class BasicQuery extends JFrame implements Runnable, ActionListener,
    WindowListener, Observer {

  private final static String VERSION = "01.04.00";
  public final static String ID =
      "$Id: BasicQuery.java,v 1.11 2006/05/30 18:33:55 daveread Exp $";

  private final static Logger logger = Logger.getLogger(BasicQuery.class);

//  private final static String FILENAME_PROPERTIES = "BasicQuery.Properties.txt";
  private final static String FILENAME_DRIVERS = "BasicQuery.Drivers";
  private final static String FILENAME_LOGGER = "BasicQuery.Logger";
  private final static String FILENAME_DEFAULTQUERIES = "BasicQuery.SQL";
  private final static String FILENAME_CONNECTSTRINGS = "BasicQuery.Connect";
  private final static String DBPOOL_NAME = "BasicQuery.DBPool";
  private final static String DBSTATS_NAME = "BasicQuery.Stats.csv";
  private final static String DBRESULTS_NAME = "BasicQuery.Results.csv";

  private final static String PROP_USERID = "USERID";
  private final static String PROP_PASSWORD = "PASSWORD";
  private final static String PROP_AUTOCOMMIT = "AUTOCOMMIT";
  private final static String PROP_READONLY = "READONLY";
  private final static String PROP_POOLING = "POOLING";
  private final static String PROP_CONNECTION = "CONNECTION";
  private final static String PROP_SQL = "SQL";
  private final static String PROP_SQLFILENAME = "SQLFILE";
  private final static String PROP_MAXROWS = "MAXROWS";
  private final static String PROP_LOGSTATS = "LOGSTATS";
  private final static String PROP_LOGRESULTS = "LOGRESULTS";
  private final static String PROP_EXPORTRAW = "EXPORTRAW";
  private final static String PROP_EXPORTNOCR = "EXPORTNOCR";
  private final static String PROP_ASSOCSQLURL = "ASSOCSQLURL";
  private final static String PROP_PARSESEMICOLON = "PARSESEMICOLON";
  private final static String PROP_SAVEPASSWORD = "SAVEPASSWORD";
  private final static String PROP_UPPERLEFTX = "UPPERLEFTX";
  private final static String PROP_UPPERLEFTY = "UPPERLEFTY";
  private final static String PROP_WIDTH = "WIDTH";
  private final static String PROP_HEIGHT = "HEIGHT";
  private final static String PROP_MAXIMIZED = "MAXIMIZED";
  private final static String PROP_DBDRIVERDIR = "DBDRIVERDIR";
  private final static String PROP_WARNUPDATEPATTERNS = "WARNUPDATEPATTERNS";
  private final static String PROP_WARNSELECTPATTERNS = "WARNSELECTPATTERNS";
  private final static String PROP_DISPLAYDBSERVERINFO = "DISPLAYDBSERVERINFO";
  private final static String PROP_DISPLAYCLIENTINFO = "DISPLAYCLIENTINFO";
  private final static String PROP_DISPLAYCOLUMNDATATYPE =
      "DISPLAYCOLUMNDATATYPE";
  private final static String PROP_TABLE_COLORING = "TABLECOLORING";
  private final static String PROP_TABLE_COLOR_USERDEF = "TABLECOLORUSERDEF";
  private final static String PROP_LANGUAGE = "LANGUAGE";
  private final static String PROP_FONT_FAMILY = "FONTFAMILY";
  private final static String PROP_FONT_SIZE = "FONTSIZE";
  private final static String PROP_FONT_BOLD = "FONTBOLD";
  private final static String PROP_FONT_ITALIC = "FONTITALIC";

  private final static String DEFAULT_DBDRIVERDIR = "DBDrivers";
  private final static String DEFAULTWARNUPDATEPATTERNS =
      "SELECT,SHOW,DESCRIBE,DESC";
  private final static String DEFAULTWARNSELECTPATTERNS =
      "INSERT,UPDATE,DELETE,ALTER,DROP,CREATE";

  // Text Styles
  private static final String STYLE_NORMAL = "Normal";
  private static final String STYLE_BOLD = "Bold";
  private static final String STYLE_GREEN = "Green";
  private static final String STYLE_YELLOW = "Yellow";
  private static final String STYLE_RED = "Red";
  private static final String STYLE_SUBTLE = "Subtle";
  private static final String STYLE_SUBTLE_UL = "SubtleUL";

  // Table row coloring options
  private final static String TABLECOLORING_GREENBAR = "GREENBAR";
  private final static String TABLECOLORING_YELLOWBAR = "YELLOWBAR";
  private final static String TABLECOLORING_USERDEFINED = "USERDEFINED";
  private final static String TABLECOLORING_NONE = "NONE";

  // DB server info display options
  private static final String DBSERVERINFO_LONG = "LONG";
  private static final String DBSERVERINFO_BRIEF = "BRIEF";

  // Parameter value yes/no constants
  private static final String PARAMVALUE_NO = "NO";
  private static final String PARAMVALUE_YES = "YES";

  // Query run mode
  private final static int RUNTYPE_SINGLE = 0;
  private final static int RUNTYPE_ALL = 1;

  // System property containing default user language and country
  private final static String PROP_SYSTEM_DEFAULTLANGUAGE = "user.language";
  private final static String PROP_SYSTEM_DEFAULTCOUNTRY = "user.country";

  // Languages
  private final static String LANG_DEFAULT = "DEFAULT";
  private final static String LANG_ENGLISH = "ENGLISH";
  private final static String LANG_FRENCH = "FRENCH";
  private final static String LANG_GERMAN = "GERMAN";
  private final static String LANG_ITALIAN = "ITALIAN";
  private final static String LANG_PORTUGUESE = "PORTUGUESE";
  private final static String LANG_SPANISH = "SPANISH";

  /**
   * Prefix to comment-out queries
   */
  private final static String COMMENT_PREFIX = "//";

  private JTable table;
  private ColoredCellRenderer cellRenderer;
  private int modeOfCurrentTable;
  private Map mapOfCurrentTables;
  private String userDefTableColoring;

  private JComboBox querySelection;
  private String queryFilename;
  private JTextArea queryText;
  private JComboBox connectString;
  private JTextField userId;
  private JPasswordField password;
  private JRadioButton asQuery, asUpdate, asDescribe;
  private JCheckBox autoCommit, readOnly, poolConnect;
  private JComboBox maxRows;
  private JButton previousQuery, nextQuery;
  private java.util.List historyQueries;
  private int historyPosition;

  // Allow this many history entries to be held
  private int maxHistoryEntries = 1000;

  private Map messageStyles;

  private JButton execute;
  private JButton remove;
  private JButton commentToggle;
  private JButton nextInList;

  private JTextPane message;
  private StyledDocument messageDocument;

  private JMenuItem fileOpenSQL;
  private JCheckBoxMenuItem fileLogStats;
  private JCheckBoxMenuItem fileLogResults;
  private JMenuItem fileSaveAsCSV;
  private JMenuItem fileSaveBLOBs;
  private JCheckBoxMenuItem fileExportsRaw;
  private JCheckBoxMenuItem fileNoCRAddedToExportRows;
  private JMenuItem editSort;
  private JMenuItem editSelectAll;
  private JMenuItem editCopy;
  private JMenuItem queryMakeVerboseSelect;
  private JMenuItem queryMakeInsert;
  private JMenuItem queryMakeUpdate;
  private JMenuItem querySelectStar;
  private JMenuItem queryDescribeStar;
  private JMenuItem querySetOrder;
  private JMenuItem queryRunAll;
  private JCheckBoxMenuItem configHistoryAssocSQLAndConnect;
  private JCheckBoxMenuItem configParseSemicolons;
  private JCheckBoxMenuItem configSavePassword;
  private JCheckBoxMenuItem configDisplayColumnDataType;
  private JCheckBoxMenuItem configDisplayClientInfo;
  private JMenuItem configFont;
  private JRadioButtonMenuItem configDisplayDBServerInfoShort;
  private JRadioButtonMenuItem configDisplayDBServerInfoLong;
  private JRadioButtonMenuItem configDisplayDBServerInfoNone;
  private JRadioButtonMenuItem configTableColoringNone;
  private JRadioButtonMenuItem configTableColoringGreenBar;
  private JRadioButtonMenuItem configTableColoringYellowBar;
  private JRadioButtonMenuItem configTableColoringUserDefined;
  private JRadioButtonMenuItem configLanguageDefault;
  private JRadioButtonMenuItem configLanguageEnglish;
  private JRadioButtonMenuItem configLanguageItalian;
  private JRadioButtonMenuItem configLanguageSpanish;
  private JRadioButtonMenuItem configLanguageFrench;
  private JRadioButtonMenuItem configLanguageGerman;
  private JRadioButtonMenuItem configLanguagePortuguese;

  private JMenuItem helpAbout;
  private JMenuItem helpParameterizedSQL;

  private Connection conn;
  private String lastConnection;
  private String lastUserId, lastPassword;
  private int runType;
  private Thread runningQuery;
  private JLabel runIndicator, timeIndicator;
  private Thread flashRunIndicator, timeRunIndicator;
  private String dbDriverDir;
  private boolean loadedDBDriver;

  // Query mode warning patterns, if the SQL begins with an "incorrect"
  // term for the query type selected, a warning message will be displayed
  private String scWarnUpdatePatterns;
  private String scWarnSelectPatterns;

  /**
   * Constructs a Basic Query instance
   *
   * @param wantGUI   If true, the GUI is presented.
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
   * Setup the correct locale based on the user language choice.  If the user
   * has chosen default, then the system language defaults are used.
   */
  private void setupLanguage() {
//    Properties props;
    String temp;

//    props = new Properties();

    try {
//      props.load(new FileInputStream(FILENAME_PROPERTIES));
    }
    catch (Throwable any) {
      // Probably no property file
      logger.warn("Unable to load configuration", any);
      messageOut(Resources.getString("errFailLoadUserConfig", any.getMessage()));
    }

//    temp = props.getProperty(PROP_LANGUAGE, LANG_DEFAULT);
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

    if (logger.isDebugEnabled()) {
      Properties props = System.getProperties();
      Set keys = props.keySet();
      Iterator keyItr = keys.iterator();

      while (keyItr.hasNext()) {
        String key = (String)keyItr.next();
        logger.debug("Prop: [" + key + "]=[" + System.getProperty(key) + "]");
      }
    }
  }

  /**
   * Adds packages (JARs) to the classpath.  By default it adds any JARs
   * in the database driver directory (set in the properties file) to the
   * classpath.
   *
   * @return A DynamicClassLoader containing jars and zip-file based classes
   */
  private DynamicClassLoader getDBClassLoader() {
    File dir;
    File[] files;
    java.util.List archives;
    DynamicClassLoader dbClassLoader;
    String archivesAdded;

    dir = new File(dbDriverDir);
    messageOut(Resources.getString("msgDBDriverDir", dir.getAbsolutePath()));
    files = dir.listFiles();
    archives = new ArrayList();
    archivesAdded = "";

    for (int f = 0; files != null && f < files.length; ++f) {
      if (files[f].getName().toLowerCase().endsWith(".jar") ||
          files[f].getName().toLowerCase().endsWith(".zip")) {
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

    getContentPane().setLayout(new BorderLayout());

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
//    table.getTableHeader().setFont(new Font(table.getTableHeader().getFont().
//        getName(), table.getTableHeader().getFont().getStyle(),
//        MessageStyleFactory.instance().getFontSize()));
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

    outerPanel.add(new JScrollPane(queryText = new JTextArea(5, 60)),
        BorderLayout.SOUTH);
    queryText.setLineWrap(true);
    queryText.setWrapStyleWord(true);

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

    // Prev/Next and the checkboxes are all on the flowPanel - Center of outerPanel
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
    boxedPanel.add(readOnly = new JCheckBox(Resources.getString("ctlReadOnly"), false));
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
    boxedPanel.add(asQuery = new JRadioButton(Resources.getString("ctlSelect"), true));
    boxedPanel.add(asUpdate = new JRadioButton(Resources.getString("ctlUpdate")));
    boxedPanel.add(asDescribe = new JRadioButton(Resources.getString(
        "ctlDescribe")));
    bGroup = new ButtonGroup();
    bGroup.add(asQuery);
    bGroup.add(asUpdate);
    bGroup.add(asDescribe);
    flowPanel.add(boxedPanel);

    flowPanel.add(new JLabel("     "));

    boxedPanel = new JPanel();
    boxedPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    boxedPanel.setBorder(getStandardBorder());
    boxedPanel.add(new JLabel(Resources.getString("proMaxRows")));
    boxedPanel.add(maxRows = new JComboBox());
    maxRows.setEditable(false);
    flowPanel.add(boxedPanel);

    flowPanel.add(new JLabel("     "));

    flowPanel.add(execute = new JButton(Resources.getString("ctlExecute")));
    execute.addActionListener(this);
    flowPanel.add(remove = new JButton(Resources.getString("ctlRemove")));
    remove.addActionListener(this);
    flowPanel.add(commentToggle = new JButton(Resources.getString("ctlComment")));
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

    ((DefaultComboBoxModel)maxRows.getModel()).addElement(Resources.getString(
        "proNoLimit"));
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("50");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("100");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("500");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("1000");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("5000");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("10000");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("20000");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("30000");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("50000");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("100000");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("200000");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("500000");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("1000000");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("1500000");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("2000000");
    ((DefaultComboBoxModel)maxRows.getModel()).addElement("3000000");
    maxRows.setSelectedItem("100");

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
    }
    catch (Throwable any) {
      // No Apache Commons DB Pooling Library Found (DBCP)
      logger.error(
          Resources.getString("errNoPoolLib"),
          any);
    }

    setDefaults();

    maxHeightJScrollPane.lockHeight(getHeight() / 6);

//    setVisible(true);

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
   * and background.  Multiple pairs can be defined and the colors will
   * alternate between rows.  The foreground and background colors are
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
    if (configTableColoringUserDefined.isSelected() && userDefTableColoring != null) {
      temp = userDefTableColoring.trim();
      if (temp.length() > 0) {
        groups = temp.split(",");

        logger.debug("User def color group count=" + groups.length);

        for (int group = 0; group < groups.length; ++group) {
          logger.debug("Group[" + group + "] = [" + groups[group] + "]");
          colors = groups[group].split("/");
          logger.debug("User def color count=" + colors.length);
          if (colors.length == 2 && colors[0].length() == 6 &&
              colors[1].length() == 6) {
            addDisplayRowColor(colors[0], colors[1]);
            configTableColoringUserDefined.setEnabled(true);
          } else {
            logger.warn("Unable to parse the user-defined color pattern: " +
                groups[group]);
            messageOut(Resources.getString("errUserDefinedColorErrorParse",
                groups[group]), STYLE_RED);
          }
        }
      }
    }
  }

  /**
   * Does the actual parsing of foreground and background colors.  Colors
   * are supplied as 6 digit (hex) strings (e.g. 000000=black, ffffff=white)
   *
   * @param foregroundColor String The foregound color as RGB hex
   * @param backgroundColor String The background color as RGB hex
   */
  private void addDisplayRowColor(String foregroundColor,
      String backgroundColor) {
    logger.debug("Add user defined display color row FG:" + foregroundColor +
        " BG:" + backgroundColor);

    try {
      cellRenderer.addAlternatingRowColor(new Color(Integer.parseInt(
          foregroundColor.substring(0, 2), 16),
          Integer.parseInt(
          foregroundColor.
          substring(2, 4), 16),
          Integer.parseInt(
          foregroundColor.substring(4, 6), 16)), new Color(Integer.parseInt(
          backgroundColor.substring(0, 2), 16),
          Integer.parseInt(backgroundColor.
          substring(2, 4), 16),
          Integer.parseInt(backgroundColor.substring(4, 6), 16))
          );
    }
    catch (Throwable any) {
      // Probably a bad hex value
      logger.warn("Error setting row coloring for FG(" + foregroundColor +
          ") BG(" + backgroundColor + ")");
      messageOut(Resources.getString("errUserDefinedColorError",
          foregroundColor, backgroundColor, any.getMessage()), STYLE_RED);
    }
  }

  /**
   * Sets-up the collection of text styles that can be used to format the
   * information in the message area.
   */
  private void setupTextStyles() {
    messageStyles = new HashMap();

    messageStyles.put(STYLE_BOLD,
        MessageStyleFactory.instance().createStyle(Color.black,
        MessageStyleFactory.BOLD));
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
        MessageStyleFactory.ITALIC |
        MessageStyleFactory.UNDERLINE));
    messageStyles.put(STYLE_YELLOW,
        MessageStyleFactory.instance().createStyle(Color.black,
        Color.yellow, MessageStyleFactory.BOLD));
  }

  /**
   * Saves the current settings into the properties file.
   */
  private void saveProperties() {
    Configuration props;

//    props = new Properties();
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
    props.setProperty(PROP_EXPORTNOCR,
        fileNoCRAddedToExportRows.isSelected() ? PARAMVALUE_YES : PARAMVALUE_NO);
    props.setProperty(PROP_SAVEPASSWORD,
        configSavePassword.isSelected() ? PARAMVALUE_YES : PARAMVALUE_NO);
    props.setProperty(PROP_ASSOCSQLURL,
        configHistoryAssocSQLAndConnect.isSelected() ? PARAMVALUE_YES :
        PARAMVALUE_NO);
    props.setProperty(PROP_PARSESEMICOLON,
        configParseSemicolons.isSelected() ? PARAMVALUE_YES :
        PARAMVALUE_NO);
    props.setProperty(PROP_DISPLAYCOLUMNDATATYPE,
        configDisplayColumnDataType.isSelected() ? PARAMVALUE_YES :
        PARAMVALUE_NO);
    props.setProperty(PROP_DISPLAYCLIENTINFO,
        configDisplayClientInfo.isSelected() ? PARAMVALUE_YES :
        PARAMVALUE_NO);
    props.setProperty(PROP_DISPLAYDBSERVERINFO,
        configDisplayDBServerInfoLong.isSelected() ? DBSERVERINFO_LONG :
        configDisplayDBServerInfoShort.isSelected() ? DBSERVERINFO_BRIEF :
        PARAMVALUE_NO);
    props.setProperty(PROP_UPPERLEFTX, getLocation().x + "");
    props.setProperty(PROP_UPPERLEFTY, getLocation().y + "");
    props.setProperty(PROP_WIDTH, getSize().width + "");
    props.setProperty(PROP_HEIGHT, getSize().height + "");
    props.setProperty(PROP_MAXIMIZED,
        getExtendedState() == JFrame.MAXIMIZED_BOTH ? PARAMVALUE_YES :
        PARAMVALUE_NO);
    props.setProperty(PROP_DBDRIVERDIR, dbDriverDir);

    // Add the select and update query type warning patterns to the properties.
    // Technically don't need to do this since the user can't change in app,
    // but want to be sure they are in the property file so the user has a
    // clue that they can be modified.
    props.setProperty(PROP_WARNSELECTPATTERNS, scWarnSelectPatterns);
    props.setProperty(PROP_WARNUPDATEPATTERNS, scWarnUpdatePatterns);

    // Data Row Coloring
    props.setProperty(PROP_TABLE_COLORING,
        configTableColoringGreenBar.isSelected() ? TABLECOLORING_GREENBAR :
        configTableColoringYellowBar.isSelected() ? TABLECOLORING_YELLOWBAR :
        configTableColoringUserDefined.isSelected() ? TABLECOLORING_USERDEFINED :
        TABLECOLORING_NONE);

    if (userDefTableColoring != null) {
      props.setProperty(PROP_TABLE_COLOR_USERDEF, userDefTableColoring);
    }

    // Language
    props.setProperty(PROP_LANGUAGE,
        configLanguageDefault.isSelected() ? LANG_DEFAULT :
        configLanguageEnglish.isSelected() ? LANG_ENGLISH :
        configLanguageFrench.isSelected() ? LANG_FRENCH :
        configLanguageGerman.isSelected() ? LANG_GERMAN :
        configLanguageItalian.isSelected() ? LANG_ITALIAN :
        configLanguagePortuguese.isSelected() ? LANG_PORTUGUESE :
        configLanguageSpanish.isSelected() ? LANG_SPANISH :
        LANG_DEFAULT);

    // Font
    props.setProperty(PROP_FONT_FAMILY, getFont().getFamily());
    props.setProperty(PROP_FONT_SIZE, getFont().getSize() + "");
    props.setProperty(PROP_FONT_BOLD, getFont().isBold() ? "YES" : "NO");
    props.setProperty(PROP_FONT_ITALIC, getFont().isItalic() ? "YES" : "NO");

    // Write out the properties to the file

    props.store();
    try {
//      props.store(new FileOutputStream(FILENAME_PROPERTIES, false),
//          "BasicQuery Configuration");
    }
    catch (Throwable any) {
      logger.error("Unable to save configuration", any);
      messageOut(Resources.getString("errFailSaveUserConfig", any.getMessage()));
    }
  }

  /**
   * Sets the configuration based on the values in the configuration file.
   */
  private void loadProperties() {
    Properties props;
    String temp;

//    props = new Properties();
    props = Configuration.instance();

    try {
//      props.load(new FileInputStream(FILENAME_PROPERTIES));
    }
    catch (Throwable any) {
      // Probably no property file
      logger.warn("Unable to load configuration", any);
      messageOut(Resources.getString("errFailLoadUserConfig", any.getMessage()));
    }

    userId.setText(props.getProperty(PROP_USERID, ""));
    password.setText(props.getProperty(PROP_PASSWORD, ""));

    if (props.getProperty(PROP_AUTOCOMMIT, PARAMVALUE_NO).equals(PARAMVALUE_YES)) {
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
        PARAMVALUE_NO).equals(PARAMVALUE_YES) ?
        JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);

    if (!props.getProperty(PROP_MAXIMIZED, PARAMVALUE_NO).equals(PARAMVALUE_YES)) {
      try {
        setLocation(Integer.parseInt(props.getProperty(PROP_UPPERLEFTX,
            "0")),
            Integer.parseInt(props.getProperty(PROP_UPPERLEFTY,
            "0")));
        setSize(Integer.parseInt(props.getProperty(PROP_WIDTH, "800")),
            Integer.parseInt(props.getProperty(PROP_HEIGHT, "600")));
      }
      catch (Throwable any) {
        logger.info("Missing property?", any);
      }
    }

    userDefTableColoring = props.getProperty(PROP_TABLE_COLOR_USERDEF);

    dbDriverDir = props.getProperty(PROP_DBDRIVERDIR, DEFAULT_DBDRIVERDIR);

    // Query type versus SQL warning patterns
    scWarnSelectPatterns = props.getProperty(PROP_WARNSELECTPATTERNS,
        DEFAULTWARNSELECTPATTERNS);
    scWarnUpdatePatterns = props.getProperty(PROP_WARNUPDATEPATTERNS,
        DEFAULTWARNUPDATEPATTERNS);
  }

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
    }
    catch (Throwable any) {
      // Probably font is not configured in property file - just ignore
      // and use system default font
      logger.info("Failed to setup a font from the properties file", any);
    }
  }

  /**
   * Set default values at startup.
   */
  private void setDefaults() {
    Properties props;

//    props = new Properties();
    props = Configuration.instance();

    try {
//      props.load(new FileInputStream(FILENAME_PROPERTIES));
    }
    catch (Throwable any) {
      logger.warn("Unable to load configuration", any);
      messageOut(Resources.getString("errFailLoadUserConfig", any.getMessage()));
    }

    if (props.getProperty(PROP_POOLING, PARAMVALUE_YES).equals(PARAMVALUE_YES)) {
      poolConnect.setSelected(true);
    } else {
      poolConnect.setSelected(false);
    }

    try {
      connectString.setSelectedIndex(Integer.parseInt(props.getProperty(
          PROP_CONNECTION)));
    }
    catch (Throwable any) {
      // Probably the list changed
      logger.info("Value not on list", any);
    }

    try {
      querySelection.setSelectedIndex(Integer.parseInt(props.getProperty(
          PROP_SQL)));
    }
    catch (Throwable any) {
      // Probably the list changed
      logger.info("Value not on list", any);
    }

    try {
      maxRows.setSelectedIndex(Integer.parseInt(props.getProperty(PROP_MAXROWS)));
    }
    catch (Throwable any) {
      // Probably the list changed
      logger.info("Value not on list", any);
    }

    // No queries executed yet, disable prev/next buttons
    previousQuery.setEnabled(false);
    nextQuery.setEnabled(false);
    historyQueries = new ArrayList();
    historyPosition = -1;
  }

  /**
   * Sets the database connection pool.
   *
   * @param connectURI The url specifying the database to which it connects
   * @param userId     The user id field
   * @param password   The password field
   */
  private void setupDBPool(String connectURI, String userId, String password) {
    removeDBPool();

    try {
      GenericObjectPool connectionPool = new GenericObjectPool(null);

      configurePool(connectionPool, connectURI, userId, password);

      PoolingDriver driver = new PoolingDriver();
      driver.registerPool(DBPOOL_NAME, connectionPool);
      logger.info("DB Connection Pool setup [" + DBPOOL_NAME + "]");
    }
    catch (Throwable any) {
      logger.error("Unable to setup database connection pool", any);
      messageOut(Resources.getString("errFailSetupDBPool", any.getMessage()),
          STYLE_RED);
    }
  }

  /**
   * Configures the database connection pool and sets the pool configuration.
   *
   * @param connectionPool   The ObjectPool
   * @param connectURI       The url specifying the database to which it
   *                         connects using JDBC
   * @param userId           The user id attribute
   * @param password         The password attribute
   *
   * @throws java.lang.Exception  Throws an SQL exception that provides
   *                              information on a database access error
   *                              or other errors
   *
   * @todo Make the pool access dynamic (use the dynamic class loader?)
   *     so that the application will compile/run without the Apache
   *     commons library.
   */
  private void configurePool(GenericObjectPool connectionPool,
      String connectURI, String userId, String password) throws
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
    //                 added to put these under user control
    connectionPool.setMaxActive(1);
    connectionPool.setWhenExhaustedAction(GenericObjectPool.
        WHEN_EXHAUSTED_BLOCK);
    connectionPool.setMaxWait(5000);
    connectionPool.setMaxIdle(1);
    connectionPool.setTimeBetweenEvictionRunsMillis(30000);
    connectionPool.setNumTestsPerEvictionRun(5);
    connectionPool.setMinEvictableIdleTimeMillis(60000);

    DriverManagerConnectionFactory connectionFactory = new
        DriverManagerConnectionFactory(connectURI, userId, password);
    PoolableConnectionFactory poolableConnectionFactory = new
        PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);

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
    ObjectPool dbpool = getDBPool();

    if (dbpool != null) {
      try {
        dbpool.close();
      }
      catch (Throwable any) {
        logger.error("Unable to close the database connection pool", any);
        messageOut(Resources.getString("errFailCloseDBPool", any.getMessage()),
            STYLE_RED);
      }
    }
  }

  /**
   * Gets the driver for the database pool
   *
   * @return ObjectPool  Returns the driver object for the specific
   *                     pool name
   */
  private ObjectPool getDBPool() {
    try {
      return new PoolingDriver().getConnectionPool(DBPOOL_NAME);
    }
    catch (SQLException excSQL) {
      logger.error("Unable to load DB Pool", excSQL);
      return null;
    }
  }

  /**
   * Creates the various menu options and attaches the mnemonics and
   * registers listeners.
   */
  private void loadMenu() {
    JMenuBar menubar;
    JMenu menu;
    JMenu subMenu;
    ButtonGroup buttonGroup;

    menubar = new JMenuBar();
    setJMenuBar(menubar);

    // File Menu
    menu = new JMenu(Resources.getString("mnuFileLabel"));
    menu.setMnemonic(Resources.getChar("mnuFileAccel"));
    menu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuFileDesc"));
    menubar.add(menu);

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

    // File | Log Resuilts
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
    fileNoCRAddedToExportRows.setMnemonic(Resources.getChar("mnuFileNoCRAccel"));
    fileNoCRAddedToExportRows.getAccessibleContext().setAccessibleDescription(
        Resources.getString("mnuFileNoCRDesc"));
    fileNoCRAddedToExportRows.setEnabled(true);
    fileNoCRAddedToExportRows.setSelected(false);
    menu.add(fileNoCRAddedToExportRows);

    // Edit Menu
    menu = new JMenu(Resources.getString("mnuEditLabel"));
    menu.setMnemonic(Resources.getChar("mnuEditAccel"));
    menu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuEditDesc"));
    menubar.add(menu);

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

    // Query Menu
    menu = new JMenu(Resources.getString("mnuQueryLabel"));
    menu.setMnemonic(Resources.getChar("mnuQueryAccel"));
    menu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuQueryDesc"));
    menubar.add(menu);

    // Query | Select Statement
    queryMakeVerboseSelect = new JMenuItem(Resources.getString(
        "mnuQuerySelectLabel"));
    queryMakeVerboseSelect.setAccelerator(KeyStroke.getKeyStroke(Resources.
        getChar("mnuQuerySelectAccel"),
        ActionEvent.ALT_MASK));
    queryMakeVerboseSelect.setMnemonic(Resources.getChar("mnuQuerySelectAccel"));
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
    queryDescribeStar.getAccessibleContext().setAccessibleDescription(Resources.
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

    // Configuration Menu
    menu = new JMenu(Resources.getString("mnuSetupLabel"));
    menu.setMnemonic(Resources.getChar("mnuSetupAccel"));
    menu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuSetupDesc"));
    menubar.add(menu);

    // Setup | Language
    subMenu = new JMenu(Resources.getString("mnuSetupLanguageLabel"));
    subMenu.setMnemonic(Resources.getChar("mnuSetupLanguageAccel"));
    subMenu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuSetupLanguageDesc"));
    menu.add(subMenu);

    // Setup | Language | System Default
    if (System.getProperty(PROP_SYSTEM_DEFAULTLANGUAGE) != null) {
      if (System.getProperty(PROP_SYSTEM_DEFAULTCOUNTRY) != null) {
        configLanguageDefault = new JRadioButtonMenuItem(
            Resources.getString("mnuSetupLanguageDefaultLabel") + " (" +
            System.getProperty(PROP_SYSTEM_DEFAULTLANGUAGE) + "_" +
            System.getProperty(PROP_SYSTEM_DEFAULTCOUNTRY) + ")");
      } else {
        configLanguageDefault = new JRadioButtonMenuItem(
            Resources.getString("mnuSetupLanguageDefaultLabel") + " (" +
            System.getProperty(PROP_SYSTEM_DEFAULTLANGUAGE) + ")");
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
    subMenu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuSetupDBServerDesc"));
    menu.add(subMenu);

    // Configuration | Display DB Server Info | None
    configDisplayDBServerInfoNone = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupDBServerNoneLabel"));
    configDisplayDBServerInfoNone.setMnemonic(Resources.getChar(
        "mnuSetupDBServerNoneAccel"));
    configDisplayDBServerInfoNone.getAccessibleContext().
        setAccessibleDescription(Resources.getString("mnuSetupDBServerNoneDesc"));
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
        setAccessibleDescription(Resources.getString("mnuSetupDBServerLongDesc"));
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
    subMenu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuSetupRowColorDesc"));
    menu.add(subMenu);

    // Setup | Table Row Coloring | None
    configTableColoringNone = new JRadioButtonMenuItem(
        Resources.getString("mnuSetupRowColorNoneLabel"));
    configTableColoringNone.setMnemonic(Resources.getChar(
        "mnuSetupRowColorNoneAccel"));
    configTableColoringNone.getAccessibleContext().
        setAccessibleDescription(Resources.getString("mnuSetupRowColorNoneDesc"));
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
    configHistoryAssocSQLAndConnect.getAccessibleContext().
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
    configDisplayColumnDataType.getAccessibleContext().setAccessibleDescription(
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

    // Help Menu
    menu = new JMenu(Resources.getString("mnuHelpLabel"));
    menu.setMnemonic(Resources.getChar("mnuHelpAccel"));
    menu.getAccessibleContext().setAccessibleDescription(Resources.getString(
        "mnuHelpDesc"));
    menubar.add(menu);

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
  }

  /**
   * Loads the drivers to allow connection to the database.  Driver definitions
   * are read from a driver configuration file.
   */
  private void loadDrivers() {
    boolean isFromFile;
    BufferedReader drivers;
    String driverName;
    int sepPosit;
    DynamicClassLoader dbClassLoader = getDBClassLoader();

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
    }
    catch (FileNotFoundException fnf) {
      // Okay, not required, but it keeps output silent
      // when the file doesn't exist, which would be typical
      // for new installation.
    }
    catch (Exception any) {
      logger.error("Problem loading the database driver(s)", any);
      messageOut(Resources.getString("errFailLoadingDBDrivers",
          Configuration.instance().getFile(FILENAME_DRIVERS).getAbsolutePath() +
          ": " + any.getMessage()),
          STYLE_RED);
    }
    finally {
      if (drivers != null) {
        try {
          drivers.close();
        }
        catch (Exception any) {
          logger.error("Unable to close the drivers configuration file", any);
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
   * @param text          The string to be inserted
   * @param attributeId   The message attribute id
   */
  private void messageOut(String text, String style) {
    messageOut(text, style, true);
  }

  /**
   * Places a message in the message display area using the style supplied.
   *
   * @param text String The message to display
   * @param style String The predefined style to use for the text
   * @param newLine boolean Whether to force a newline after the message
   */
  private void messageOut(String text, String style, boolean newLine) {
    try {
      messageDocument.insertString(messageDocument.getLength(), text,
          (AttributeSet)messageStyles.get(style));

      if (newLine) {
        messageDocument.insertString(messageDocument.getLength(), "\n",
            (AttributeSet)messageStyles.get(
            STYLE_NORMAL));
      }
    }
    catch (BadLocationException excLoc) {
      logger.warn("Bad location while inserting message text", excLoc);
    }

    if (messageDocument.getLength() > 0) {
      message.setCaretPosition(messageDocument.getLength() - 1);
    }
  }

  /**
   * writes the string onto the message document, displayed in the lower
   * portion of the GUI.
   *
   * @param text   The string that is to be inserted
   */
  private void messageOut(String text) {
    logger.info(text);
    if (messageDocument != null) {
      messageOut(text, STYLE_NORMAL);
    }
  }

  /**
   * Loads the driver into the JVM making it available for usage by the
   * driver class
   *
   * @param driverClass String specifying the class name for the driver
   * @param productName String specifying the product name - oracle,sybase or
   *                    mySql
   */
  private void loadDriver(String driverClass, String productName,
      DynamicClassLoader dbClassLoader) {
    Constructor constructDriver;

    try {
      constructDriver = Class.forName(driverClass, true, dbClassLoader).
          getConstructor(null);
      DriverManager.registerDriver(new DynamicDriver((Driver)constructDriver.
          newInstance(null)));

      messageOut(Resources.getString("msgDriverLoaded", productName),
          STYLE_GREEN);
    }
    catch (Exception any) {
      logger.error("Unable to load DB driver [" + driverClass + "]", any);
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
      addToCombo(querySelection,
          new Query(
          "$PARAM[OUT,INTEGER]$ = {call function($PARAM[IN,STRING,ARG1]$, '', '', 'ARG4'}",
          Query.MODE_QUERY));
    }
  }

  /**
   * Checks whether an input (query or connect string) is already in its
   * associated combo box.  If not, the new information is added to the combo list
   *
   * @param combo JComboBox which has a list of the query statements or connect strings.
   */
  private void checkForNewString(JComboBox combo) {
//    String newString, foundString;
    Object newValue;
    int checkDups, matchAt;
    boolean match;
//    boolean newCommented, foundCommented;

//    newCommented = foundCommented = false;
    matchAt = -1;

    if (combo == querySelection) {
      newValue = new Query(queryText.getText(), whichModeValue());
//      newString = queryText.getText();
//      newString = newString.replace('\n', ' ');
    } else {
//      newString = (String)combo.getEditor().getItem();
      newValue = combo.getEditor().getItem();
    }

//    if (newString.startsWith(COMMENT_PREFIX)) {
//      newCommented = true;
//      newString = newString.substring(2);
//    }

//    if (newString.trim().length() > 0) {
    if (newValue.toString().length() > 0) {
      match = false;
      for (checkDups = 0; checkDups < combo.getItemCount() && !match;
          ++checkDups) {
//        if (combo == querySelection) {
//          foundString = ((Query)combo.getItemAt(checkDups)).getSQL();
//        } else {
//          foundString = ((String)combo.getItemAt(checkDups));
//        }

//        if (foundString.startsWith(COMMENT_PREFIX)) {
//          foundString = foundString.substring(2);
//          foundCommented = true;
//        } else {
//          foundCommented = false;
//        }

//        if (newString.equals(foundString)) {
        if (newValue.equals(combo.getItemAt(checkDups))) {
          match = true;
          if (combo == querySelection) {
            ((Query)combo.getItemAt(checkDups)).setMode(whichModeValue());
          }
          combo.setSelectedIndex(checkDups);
          matchAt = checkDups;
        }
      }

//      if (newCommented) {
//        newString = COMMENT_PREFIX + newString;
//      }

      if (!match) {
        addToCombo(combo, newValue);
        if (combo == querySelection) {
//          addToCombo(combo, new Query(newString, whichModeValue()));
          combo.setSelectedIndex(combo.getModel().getSize() - 1);
          matchAt = combo.getSelectedIndex();
        } else {
//          addToCombo(combo, newString);
        }
      }
//      if (foundCommented != newCommented) {
//        if (combo == querySelection) {
//          replaceInCombo(combo, matchAt,
//              new Query(newString, whichModeValue()));
//        } else {
//          replaceInCombo(combo, matchAt, newString);
//        }
//      }
      if (combo == querySelection) {
        if (((Query)newValue).isCommented() !=
            ((Query)combo.getSelectedItem()).isCommented()) {
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
   * @param mode  The integer value for the mode that is selected
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
   * the query object.Otherwise it converts the string to  a query object
   * and returns it.
   *
   * @return Query The Query Object
   */
  private Query getQuery() {
    logger.debug("getSelectItem[" + querySelection.getSelectedItem() + "]");

    if (querySelection.getSelectedItem() == null) {
      return null;
    } else if (querySelection.getSelectedItem() instanceof Query) {
      return (Query)querySelection.getSelectedItem();
    } else {
      return new Query((String)querySelection.getSelectedItem(),
          whichModeValue());
    }
  }

  /**
   * Adds a new object in the combo box
   *
   * @param combo    The JComboBox
   * @param newData  The Object that is to be added
   */
  private void addToCombo(JComboBox combo, Object newData) {
    ((DefaultComboBoxModel)combo.getModel()).addElement(newData);
  }

  /**
   * Replaces an existing object with a new one
   *
   * @param combo     The JComboBox
   * @param position  The int value of the positon where the
   *                  repalcement occurs
   * @param newData   The new Object that is to be inserted in the combobox
   */
  private void replaceInCombo(JComboBox combo, int position, Object newData) {
    ((DefaultComboBoxModel)combo.getModel()).removeElementAt(position);
    ((DefaultComboBoxModel)combo.getModel()).insertElementAt(newData,
        position);
    combo.setSelectedIndex(position);
  }

  private void processStatement() {
    processStatement(false);
  }

  private void processStatement(boolean batch) {
    String rawStatement;

    rawStatement = getQuery().getRawSQL();

    if (rawStatement != null) {
      rawStatement = rawStatement.trim();

      if (fileSaveAsCSV.isEnabled() && rawStatement.startsWith(">") && rawStatement.length() > 1) {
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
   * @param batch If true, prevents error pop-up dialog boxes from being created
   */
  private void displayResultsAsTable(boolean batch) {
    TableSorter sorter;
    TableModel model;
    String rawSQL, sqlStatement;
    String[] eachQuery;
    int queryIndex;

    // Clear the Message Area
    message.setText("");

    // Parse the SQL for semicolons (separates multiple statements)
    rawSQL = getQuery().getRawSQL();

    if (configParseSemicolons.isSelected()) {
      eachQuery = Utility.splitWithQuotes(rawSQL, ";");
      logger.info("Queries embedded in this string: " + eachQuery.length);
      if (logger.isDebugEnabled()) {
        for (int i = 0; i < eachQuery.length; ++i) {
          logger.debug("Embedded Query " + i + ": " + eachQuery[i]);
        }
      }
    } else {
      eachQuery = new String[1];
      eachQuery[0] = rawSQL;
    }

    // Execute each query embedded in the input string
    for (queryIndex = 0; queryIndex < eachQuery.length; ++queryIndex) {
      logger.debug("Embedded query index=" + queryIndex);

      sqlStatement = eachQuery[queryIndex];

      logger.info("Query to execute: " + sqlStatement);

      // For multiple statements executed at once, separate messages
      if (queryIndex > 0) {
        messageOut("\n------------------------------------------------");
      }

      // If old sorter exists, remove its mouse listener(s) from table and
      // temporarily point table to empty model while real model is updated
      try {
        sorter = (TableSorter)table.getModel();
        sorter.removeMouseListenerFromHeaderInTable(table);
      }
      catch (Throwable any) {
        // Probably table was empty
      }
      table.setModel(new ListTableModel());

      getQuery().setMode(whichModeValue());

      /* Initializes the table model - either creates it, or, clears-out the existing one. */
      model = new ListTableModel();
      sorter = new TableSorter(model);

      if (!commentedQuery()) { // if query is not commented-out
        execute(sqlStatement, model);
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
   * Displays a popup message dialog.  If the message is likely to be a long
   * string without line breaks, the reformatMessage parameter can be set
   * true and line breaks will be added to allow the message to fit neatly
   * on the screen.
   *
   * @param message The message to be displayed to the user.
   * @param title The title of the message dialog
   * @param messageType The message type as defined in JOptionPane
   * @param reformatMessage Whether to insert line breaks within the message
   */
  private void userMessage(String message, String title, int messageType,
      boolean reformatMessage) {
    if (reformatMessage) {
      JOptionPane.showMessageDialog(this,
          Utility.characterInsert(message, "\n",
          60, 80, " .,"), title, messageType);
    } else {
      JOptionPane.showMessageDialog(this, message, title, messageType);
    }
  }

  /**
   * Displays a popup message dialog.
   *
   * @param message The message to be displayed to the user.
   * @param title The title of the message dialog
   * @param messageType The message type as defined in JOptionPane
   */
  private void userMessage(String message, String title, int messageType) {
    userMessage(message, title, messageType, true);
  }

  /**
   * Looks for queries that have been commented out
   *
   * @return boolean Returns true if there are any commented queries
   */
  private boolean commentedQuery() {
    if (getQuery() != null && getQuery().getSQL() != null) {
      return getQuery().getSQL().startsWith(COMMENT_PREFIX);
    } else {
      return false;
    }
  }

  /**
   * Test that SQL agrees with query type selected, otherwise warn user.
   *
   * @todo This needs a lot of work dealing with the parsing of the SQL since
   *     it could have $PARAM[...]$ type syntax at the beginning and the simple
   *     grabbing of the first set of characters is not useful.
   *
   * @param saQuery The SQL being executed
   * @return True is the query type and the SQL are compatible or if the user
   *     agrees to force the query type.
   */
  private boolean isOkayQueryType(String saQuery) {
    boolean blOkay;
    String slFirstWord;
    String slQueryType;

    blOkay = true;
//    slQueryType = "Undefined";
    slQueryType = Resources.getString("proUnknown");

    if (asQuery.isSelected()) {
//      slQueryType = "Select";
      slQueryType = Resources.getString("ctlSelect");
    } else if (asDescribe.isSelected()) {
//      slQueryType = "Describe Result (Select)";
      slQueryType = Resources.getString("ctlDescribe");
    } else if (asUpdate.isSelected()) {
//      slQueryType = "Update";
      slQueryType = Resources.getString("ctlUpdate");
    }

    if (saQuery != null && saQuery.trim().length() > 0) {
      slFirstWord = saQuery.split(" ", 2)[0].toUpperCase();
    } else {
      slFirstWord = null;
    }

    if (slFirstWord != null) {
      // Test is a little weird, don't want to object if we don't recognize the
      // beginning of the SQL.  For instance, EXEC and CALL are used for stored proc
      // execution, but they may return a resultset or not, can't warn user in
      // that case.  So just test for likely mistakes.
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
          "\n", 60, 80, " "),
          Resources.getString("msgQueryTypeSuspiciousTitle"),
          JOptionPane.YES_NO_OPTION,
          JOptionPane.WARNING_MESSAGE) ==
          JOptionPane.YES_OPTION;
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
   * @param model
   */
  private void execute(String SQL, TableModel model) {
    Statement stmt;
    ResultSet result;
    ResultSetMetaData meta;
    java.util.List rowData;
    int retValue;
    SQLWarning warning;
    int myType[];
    Object value;
    String typeName;
    String colName;
    String metaName;
    boolean hasResults, hasBLOB;
    java.util.Date connAsk, connGot, stmtGot, queryStart;
    java.util.Date queryReady, queryRSFetched, queryRSProcessed;
    int rows;
    int cols;
    boolean hasParams;
    java.util.List allParams, outParams;

    stmt = null;
    meta = null;
    rowData = null;
    result = null;
    retValue = 0;
    hasBLOB = false;
    hasResults = false;
    queryStart = null;
    queryReady = null;
    queryRSFetched = null;
    queryRSProcessed = null;
    connAsk = null;
    connGot = null;
    stmtGot = null;
    rows = 0;
    cols = 0;
    hasParams = false;
    allParams = new ArrayList();
    outParams = null;

    modeOfCurrentTable = whichModeValue();
    mapOfCurrentTables = new HashMap();

    // Try to prevent incorrect selection of query type by checking
    // beginning of SQL statement for obvious stuff
    // First check "Select" and Describe query types
    if (!isOkayQueryType(getQuery().getSQL())) {
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
      }
      catch (Throwable any) {

      }
    }

    conn = null;

    try {
      messageOut(Resources.getString("msgExecuteQuery",
          asQuery.isSelected() ? Resources.getString("msgQuery") :
          asDescribe.isSelected() ?
          Resources.getString("msgDescribe") : Resources.getString("msgUpdate"),
          SQL), STYLE_BOLD);
      if (poolConnect.isSelected()) {
        messageOut(Resources.getString("msgPoolStats") + " ", STYLE_SUBTLE, false);
        if (getDBPool() != null) {
          messageOut(Resources.getString("msgPoolStatsCount",
              getDBPool().getNumActive() + "", getDBPool().getNumIdle() + ""));
          logger.debug("Retrieved existing DB connection pool");
        } else {
          logger.debug("No existing DB pool");
          messageOut(Resources.getString("msgPoolNone"));
        }
      }
      if (getDBPool() == null || /* conn == null */
          !((String)connectString.getEditor().getItem()).equals(
          lastConnection) ||
          !userId.getText().equals(lastUserId) ||
          !new String(password.getPassword()).equals(lastPassword)) {

        removeDBPool();

        lastConnection = (String)connectString.getEditor().getItem();
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
        conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:" +
            DBPOOL_NAME);
        logger.debug("Got pooled connection");
        messageOut(Resources.getString("msgGotPoolConn"), STYLE_GREEN);
      } else {
        conn = DriverManager.getConnection(lastConnection, lastUserId,
            lastPassword);
        logger.debug("Got non-pooled connection");
        messageOut(Resources.getString("msgGotDirectConn"), STYLE_GREEN);
      }

      if (hasParams = SQL.indexOf("$PARAM[") > -1) {
        SQL = makeParams(SQL, allParams);
      }

      connGot = new java.util.Date();

      conn.setAutoCommit(autoCommit.isSelected());
      conn.setReadOnly(readOnly.isSelected());

      if (!hasParams) {
        stmt = conn.createStatement();
      } else {
        stmt = conn.prepareCall(SQL);
        setupCall((CallableStatement)stmt, allParams);
      }

      stmtGot = new java.util.Date();

      try {
        if (!maxRows.getSelectedItem().equals(Resources.getString("proNoLimit"))) {
          stmt.setMaxRows(Integer.parseInt((String)maxRows.getSelectedItem()));
          messageOut("\n" + Resources.getString("msgMaxRows",
              stmt.getMaxRows() + ""), STYLE_SUBTLE);
        }
      }
      catch (Exception any) {
        logger.warn("Unable to set maximum rows", any);
        messageOut(Resources.getString("errFailSetMaxRows",
            (String)maxRows.getSelectedItem(), any.getMessage()), STYLE_YELLOW);
      }

      if (asQuery.isSelected() || asDescribe.isSelected()) {
        queryStart = new java.util.Date();
        if (!hasParams) {
          int updateCount;
          
          // Execute the query synchronously
          stmt.execute(SQL);
          messageOut(Resources.getString("msgQueryExecutedByDB"), STYLE_GREEN);
          
          // Process the query results and/or report status
          if ((updateCount = stmt.getUpdateCount()) > -1) {
            do {
              logger.debug("Looking for results [update=" +
                  updateCount + "]");
              stmt.getMoreResults();
            }
            while ((updateCount = stmt.getUpdateCount()) > -1);
          }
          result = stmt.getResultSet();
        } else {
          result = ((PreparedStatement)stmt).executeQuery();
        }
        queryReady = new java.util.Date();
        meta = result.getMetaData();
        cols = meta.getColumnCount();
      } else {
        queryStart = new java.util.Date();
        if (!hasParams) {
          retValue = stmt.executeUpdate(SQL);
        } else {
          retValue = ((PreparedStatement)stmt).executeUpdate();
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
            metaName = meta.getColumnTypeName(col + 1) + " " +
                meta.getColumnDisplaySize(col + 1) + " (";

            // have had oracle tables report large precision values
            // for BLOB fields that caused exception to be thrown
            // by getPrecision() since the value was beyond int
            try {
              metaName += meta.getPrecision(col + 1);
            }
            catch (Exception any) {
              metaName += "?";
              logger.warn("Unable to get column precision", any);
            }
            metaName += ".";
            metaName += meta.getScale(col + 1);
            metaName += ")";

            colName += " [" + metaName + "]";
          }

          ((ListTableModel)model).addColumn(colName);
          // Keep collection of tables used for Insert and Update Menu Selections
          try {
            mapOfCurrentTables.put(meta.getTableName(col + 1), null);
          }
          catch (Exception any) {
            // Probably unimplemented method - Sybase driver
            logger.warn("Failed to obtain table name from metadata", any);
            messageOut(Resources.getString("errFailReqTableName",
                any.getMessage()), STYLE_SUBTLE);
          }
        }

        rowData = new ArrayList();

        myType = new int[cols];

        for (int col = 0; col < cols; ++col) {
          typeName = meta.getColumnTypeName(col + 1).toUpperCase();
          if (typeName.equals("NUMBER")) {
            if (meta.getScale(col + 1) > 0) {
              myType[col] = 3; // DOUBLE
            } else if (meta.getPrecision(col + 1) < 8) {
              myType[col] = 1; // INTEGER
            } else {
              myType[col] = 2; // LONG
            }
          } else if (typeName.equals("LONG")) {
            myType[col] = 2;
          } else if (typeName.equals("DATETIME")) {
            myType[col] = 5; // Date/Time
          } else if (typeName.equals("DATE")) {
            myType[col] = 5; // Date/Time
          } else if (typeName.equals("BLOB")) {
            myType[col] = 6;
            hasBLOB = true;
          } else {
            myType[col] = 0; // Default - String
          }
        }

        if (fileLogResults.isSelected()) {
        	writeDataAsCSV(SQL, model, DBRESULTS_NAME, result, myType, false);
        } else {
        while (result.next()) {
          ++rows;
          rowData = new ArrayList();

          for (int col = 0; col < cols; ++col) {
        	value = getResultField(result, col + 1, myType[col]);
/*            try {
              switch (myType[col]) {
                case 1: // Integer
                  value = new Integer(result.getInt(col + 1));
                  break;
                case 2: // Long
                  value = new Long(result.getLong(col + 1));
                  break;
                case 3: // Double
                  value = new Double(result.getDouble(col + 1));
                  break;
                case 5: // Date/Time
                  value = result.getTimestamp(col + 1);
                  break;
                case 6: // BLOB
                  try {
                    value = result.getBlob(col + 1);
                  }
                  catch (Throwable any) {
                    logger.error("Error retrieving BLOB field key", any);

                    // Probably unsupported method (first found in a MySQL Driver)
                    value = result.getString(col + 1);

                    // Change type to not be BLOB since
                    // the data in the column will not
                    // function as BLOB -- i.e. export
                    // BLOB will not work.
                    myType[col] = 0;
                  }
                  break
                      ;
                case 0: // Fall through to default
                default:
                  value = result.getString(col + 1);
                  break;
              }
            }
            catch (Exception any) {
              logger.error("Error at row[" + model.getRowCount() + "] Col[" +
                  (col + 1) + " Type[" + myType[col] + "]", any);
              messageOut(Resources.getString("errFailDataRead",
                  model.getRowCount() + "/" + (col + 1), any.getMessage()),
                  STYLE_YELLOW);
              value = result.getString(col + 1);
            } */
            rowData.add(value);
          }

          ((ListTableModel)model).addRowFast(rowData);
          hasResults = true;
        }
        ((ListTableModel)model).updateCompleted();
        }
        
        queryRSProcessed = new java.util.Date();
      } else if (asDescribe.isSelected()) {
        String colLabel;

        meta = result.getMetaData();

        myType = new int[6];

        for (int col = 0; col < 6; ++col) {
          switch (col) {
            case 0: // Col Name
              colLabel = Resources.getString("proColumnName");
              myType[col] = 0;
              break;
            case 1: // Col Type
              colLabel = Resources.getString("proColumnType");
              myType[col] = 0;
              break;
            case 2: // Col Length
              colLabel = Resources.getString("proColumnLength");
              myType[col] = 1;
              break;
            case 3: // Col precision
              colLabel = Resources.getString("proColPrecision");
              myType[col] = 1;
              break;
            case 4: // Col scale
              colLabel = Resources.getString("proColScale");
              myType[col] = 1;
              break;
            case 5: // Nulls Okay?
              colLabel = Resources.getString("proColNullsAllowed");
              myType[col] = 0;
              break;
            default: // oops
              colLabel = Resources.getString("proColUndefined");
              break;
          }

          if (configDisplayColumnDataType.isSelected()) {
            colLabel += " [";
            colLabel += myType[col] == 0 ? Resources.getString("proColCharType") :
                Resources.getString("proColNumeric");
            colLabel += "]";
          }

          ((ListTableModel)model).addColumn(colLabel);
        }

        rowData = new ArrayList();

        for (int col = 0; col < cols; ++col) {
          rowData = new ArrayList();

          for (int row = 0; row < 6; ++row) {
            switch (row) {
              case 0: // Name
                colName = meta.getColumnName(col + 1);
                if (colName == null || colName.trim().length() == 0) {
                  colName = Resources.getString("msgUnnamedColumn",
                      meta.getColumnLabel(col + 1));
                }
                value = colName;
                break;
              case 1: // Type
                value = meta.getColumnTypeName(col + 1);
                break;
              case 2: // Length
                value = new Integer(meta.getColumnDisplaySize(col + 1));
                break;
              case 3: // Precision
                try {
                  value = new Integer(meta.getPrecision(col + 1));
                }
                catch (Exception any) {
                  value = "?";
                  logger.warn("Unable to obtain column precision", any);
                }
                break
                    ;
              case 4: // Scale
                value = new Integer(meta.getScale(col + 1));
                break;
              case 5: // Nulls Okay?
                value = meta.isNullable(col + 1) ==
                    ResultSetMetaData.columnNullable ?
                    Resources.getString("proYes") :
                    meta.isNullable(col + 1) == ResultSetMetaData.columnNoNulls ?
                    Resources.getString("proNo") :
                    Resources.getString("proUnknown");
                break;
              default:
                value = null;
                break;
            }

            rowData.add(value);

            // Keep collection of tables used for Insert and Update Menu Selections
            try {
              mapOfCurrentTables.put(meta.getTableName(col + 1), null);
            }
            catch (Exception any) {
              // Probably unimplemented method - Sybase driver
              logger.warn("Failed to obtain table name from metadata", any);
              messageOut(Resources.getString("errFailReqTableName",
                  any.getMessage()), STYLE_SUBTLE);
            }
          }
          ((ListTableModel)model).addRow(rowData);
        }

        while (result.next()) {
          rows++;
          for (int col = 0; col < cols; ++col) {
            result.getObject(col + 1);
          }
        }

        queryRSFetched = new java.util.Date();

      } else {
        messageOut("\n" + Resources.getString("msgReturnValue") + " " +
            retValue, STYLE_BOLD, false);
        rows = stmt.getUpdateCount();
      }

      messageOut("\n" + Resources.getString("msgRows") + " ", STYLE_BOLD, false);
      if (rows == stmt.getMaxRows() && rows > 0) {
        messageOut("" + rows, STYLE_YELLOW);
      } else {
        messageOut("" + rows, STYLE_BOLD);
      }
    }
    catch (SQLException sql) {
      logger.error("Error executing SQL", sql);
      messageOut(Resources.getString("errFailSQL", sql.getClass().getName(),
          sql.getMessage()), STYLE_RED);
      userMessage(Resources.getString("errFailSQLText", sql.getMessage()),
          Resources.getString("errFailSQLTitle"), JOptionPane.ERROR_MESSAGE);
      while ((sql = sql.getNextException()) != null) {
        logger.error("Next Exception", sql);
      }
      modeOfCurrentTable = -1;
    }
    catch (Throwable any) {
      logger.error("Error executing SQL", any);
      messageOut(Resources.getString("errFailSQL", any.getClass().getName(),
          any.getMessage()), STYLE_RED);
      userMessage(Resources.getString("errFailSQLText", any.getMessage()),
          Resources.getString("errFailSQLTitle"), JOptionPane.ERROR_MESSAGE);
      modeOfCurrentTable = -1;
    }
    finally {
      fileSaveBLOBs.setEnabled(hasBLOB);
      fileSaveAsCSV.setEnabled(hasResults && model.getRowCount() > 0);
      queryMakeInsert.setEnabled(modeOfCurrentTable == Query.MODE_DESCRIBE ||
          modeOfCurrentTable == Query.MODE_QUERY);

      if (hasParams) {
        outParams = getOutParams((CallableStatement)stmt, allParams);
      }

      logger.debug("Check for more results");

      try {
        int resultCount = 0;
        while (stmt.getMoreResults()) {
          int updateCount;
          ++resultCount;
          updateCount = stmt.getUpdateCount();
          logger.debug("More results [" +
              resultCount + "][updateCount=" +
              updateCount + "]");
        }
      }
      catch (SQLException sql) {
        logger.error("Failed checking for more results", sql);
        messageOut(Resources.getString("errFailAddlResults",
            sql.getClass().getName(), sql.getMessage()));
      }

      logger.debug("No more results");

      if (result != null) {
        try {
          result.close();
          logger.info("Resultset closed");
        }
        catch (Throwable any) {
          logger.error("Unable to close resultset", any);
        }
      }

      if (stmt != null) {
        try {
          warning = stmt.getWarnings();
          while (warning != null) {
            logger.warn("Stmt Warning: " + warning.toString());
            messageOut(Resources.getString("errStmtWarning", warning.toString()),
                STYLE_YELLOW);
            warning = warning.getNextWarning();
          }
        }
        catch (Throwable any) {
          logger.warn("Error retrieving statement SQL warnings", any);
        }

        try {
          stmt.close();
          logger.debug("Statement closed");
        }
        catch (Throwable any) {
          logger.error("Unable to close statement", any);
        }
      }

      if (conn != null) {
        try {
          warning = conn.getWarnings();
          while (warning != null) {
            logger.warn("Connt Warning: " + warning.toString());
            messageOut(Resources.getString("errConnWarning", warning.toString()),
                STYLE_YELLOW);
            warning = warning.getNextWarning();
          }
        }
        catch (Throwable any) {
          logger.warn("Error retrieving connection SQL warnings", any);
        }
      }

      // Close the connection if there are no BLOBs.
      // If the user decides to save a BLOB we will need to DB connection
      // to remain open, hence we only close here if there are no BLOBs
      if (!hasBLOB && conn != null) {
        try {
          conn.close();
          conn = null;
          logger.debug("DB Connection closed");
        }
        catch (Throwable any) {
          logger.error("Unable to close DB connection", any);
        }
      }

      reportStats(SQL, connAsk, connGot, stmtGot, queryStart, queryReady,
          queryRSFetched, queryRSProcessed, rows, cols,
          asDescribe.isSelected() ? model : null, outParams);
      //reportResults(SQL, model);
    }
  }

  /**
   * Outputs Connection Meta Data to the message area.  This is controlled by
   * a configuration menu option.
   *
   * @param conn Connection The DB connection
   */
  private void reportConnectionStats(Connection conn) {
    Class connectionClass;
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
        dbMeta = conn.getMetaData();

        msg = new StringBuffer();
        connectionClass = Class.forName("java.sql.DatabaseMetaData");
        allMethods = connectionClass.getMethods();
        for (int i = 0; i < allMethods.length; ++i) {
          // Only interested in the bean accessor methods with no args
          if (allMethods[i].getName().startsWith("get") &&
              allMethods[i].getParameterTypes().length == 0) {
            try {
              // Some methods should not be called - would leak connections
              returnTypeName = allMethods[i].getReturnType().toString();
              if (returnTypeName.indexOf("Connection") > 1 ||
                  returnTypeName.indexOf("Statement") > -1 ||
                  returnTypeName.indexOf("ResultSet") > -1) {
                logger.debug("Skipping method [" + allMethods[i].getName() +
                    "] return type [" + allMethods[i].getReturnType() + "]");
                continue;
              }

              // If brief output, skip the more detailed information
              if (formatBrief) {
                methodName = allMethods[i].getName();
                if (methodName.indexOf("Keyword") > -1 ||
                    methodName.indexOf("Functions") > -1 ||
                    methodName.indexOf("Max") > -1) {
                  continue;
                }

              }
              // Call the method
              returnValue = allMethods[i].invoke(dbMeta, null);

              // Check if the returned value is an array
              if (!returnValue.getClass().isArray()) {
                // For nonarray - check if the returned value is a ResultSet
                if (returnValue instanceof ResultSet) {
                  // Only output DB server row-based data for long DB details
                  if (formatLong) {
                    // Have a result set with the information
                    messageOut("  " + fixAccessorName(allMethods[i].getName()) +
                        ": " + Resources.getString("msgSetOfRows"),
                        STYLE_SUBTLE);
                    resultValues = (ResultSet)returnValue;
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
                  messageOut("  " + fixAccessorName(allMethods[i].getName()) +
                      ": " + returnValue,
                      STYLE_SUBTLE);
                }
              } else if (formatLong) {
                // Have an array - display the values in the array
                // Will output if long format details are requested
                numElements = Array.getLength(returnValue);
                messageOut("  " + fixAccessorName(allMethods[i].getName()) +
                    ": " + Resources.getString("msgSetOfValues",
                    numElements + ""),
                    STYLE_SUBTLE);
                for (element = 0; element < numElements; ++element) {
                  messageOut("    " + Array.get(returnValue, element),
                      STYLE_SUBTLE);

                }
              }
            }
            catch (Throwable any) {
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
      }
      catch (Throwable any) {
        logger.error("Error retrieving DB Server Metadata", any);
        messageOut("  " + Resources.getString("errFailDBServerMeta",
            any.getClass().getName(), any.getMessage()));
      }
    }
  }

  /**
   * Convert an accessor name (e.g. getSomeValue) into a user friendly
   * label, splitting at uppercase letter boundaries (e.g. Some Value)
   *
   * @param accessorName String The name of the acceeor method
   * @return String The reformatted label
   */
  private String fixAccessorName(String accessorName) {
    int posit;
    byte[] characters;
    StringBuffer newName;
    boolean prevWasLC;

    if (accessorName.startsWith("get")) {
      accessorName = accessorName.substring(3);
    }

    newName = new StringBuffer();
    characters = accessorName.getBytes();
    newName.append((char)characters[0]);
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
      newName.append((char)characters[posit]);
    }

    return newName.toString();
  }

  /**
   * Takes a callable statement and retrieves the out parameters, displaying
   * them in the message area of the GUI.
   *
   * @param stmt CallableStatement just executed
   * @param params List of parameters used in the callable statement
   * @return List
   */
  private java.util.List getOutParams(CallableStatement stmt,
      java.util.List params) {
    java.util.List values;
    int paramIndex;
    StatementParameter param;

    paramIndex = 0;

    values = new ArrayList();
    try {
      for (paramIndex = 0; paramIndex < params.size(); ++paramIndex) {
        param = (StatementParameter)params.get(paramIndex);
        if (param.getType() == StatementParameter.OUT) {
          messageOut(Resources.getString("msgOutParam") + " ", STYLE_SUBTLE, false);
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
    }
    catch (Throwable any) {
      logger.error("Failed to read output parameter at index (" + paramIndex +
          ")", any);
      messageOut(Resources.getString("errFailReadingOutParam", paramIndex + "",
          any.getMessage()));
    }

    return values;
  }

  /**
   * Takes a SQL statement and determines whether the syntax for using
   * in/out parameters has been used.  For each in parameter the SQL
   * is updated to replace the parameter with a "?" and stores the
   * parameter value in the parameter list.  Out parameters are simply
   * stored in the parameters list and the specialized parameter syntax
   * removed.  The end result is a parameterized SQL statement and a
   * list of parameters.
   *
   * @param SQL The SQL statement to be processed for parameters
   * @param params The collection of parameters found in the SQL statement
   * @return The updated SQL staement with the specialized parameter syntax removed
   */
  private String makeParams(String SQL, java.util.List params) {
    int beginPosit, endPosit;

    while ((beginPosit = SQL.indexOf("$PARAM[")) > -1) {
      endPosit = SQL.substring(beginPosit).indexOf("]$");
      if (endPosit == -1) {
        endPosit = SQL.length() - 2;
      } else {
        endPosit += beginPosit;
      }
      processParameter(SQL.substring(beginPosit + 7, endPosit).trim(), params);
      SQL = Utility.replace(SQL, SQL.substring(beginPosit, endPosit + 2), "?",
          0, false);
      messageOut(Resources.getString("msgParamSearchLoopBefore", SQL));
    }

    messageOut(Resources.getString("msgParamSearchLoopAfter", SQL));

    return SQL;
  }

  /**
   * Parse a parameter.  Determines the parameter direction (In/Out) and
   * type.
   *
   * @param param The parameter as supplied in the input SQL statement
   * @param params The collection of parameters.  The parameter decoded from the
   *     param input is added to this list.
   */
  private void processParameter(String param, java.util.List params) {
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
   * @param stmt The callable statement
   * @param params The list of parameters
   */
  private void setupCall(CallableStatement stmt, java.util.List params) {
    int paramIndex;
    StatementParameter param;

    paramIndex = 0;

    try {
      for (paramIndex = 0; paramIndex < params.size(); ++paramIndex) {
        param = (StatementParameter)params.get(paramIndex);

        if (param.getType() == StatementParameter.IN) {
          switch (param.getDataType()) {
            case java.sql.Types.VARCHAR:
              stmt.setString(paramIndex + 1, param.getDataString());
              break;
            case java.sql.Types.INTEGER:
              stmt.setInt(paramIndex + 1, Integer.parseInt(param.getDataString()));
              break;
            default:
              stmt.setObject(paramIndex + 1, param.getDataString());
              break;
          }
        } else {
          stmt.registerOutParameter(paramIndex + 1, param.getType());
        }
      }
    }
    catch (Throwable any) {
      logger.error("Failed to register output parameter at index [" +
          paramIndex + "]", any);
      messageOut(Resources.getString("errFailRegisterOutParam", paramIndex + "",
          any.getMessage()));
    }
  }

  /**
   * Log the results of executing the SQL to a flat file
   *
   * @param model The table model containing the results
   */
  private void reportResults(String query, TableModel model) {
    if (fileLogResults.isSelected() && model != null) {
      writeDataAsCSV(query, model, DBRESULTS_NAME, true);
    }
  }

  /**
   * Reports statistics on various data such as the date a query was executed and
   * the date the results were fetched. Other statistics are reported on such as the
   * the date a connection was asked for, and the date it was actually received.
   * The report statistics are written to an external text file
   * represented by DBSTATS_NAME.
   *
   * @param connAsk
   * @param connGot
   * @param stmtGot
   * @param queryStart
   * @param queryReady
   * @param queryRSFetched
   * @param queryRSProcessed
   * @param rows
   * @param cols
   */
  private void reportStats(String sqlStatement, java.util.Date connAsk,
      java.util.Date connGot,
      java.util.Date stmtGot,
      java.util.Date queryStart, java.util.Date queryReady,
      java.util.Date queryRSFetched,
      java.util.Date queryRSProcessed,
      int rows, int cols, TableModel model,
      java.util.List outParams) {
    Runtime runtime;
    String runStats;
    PrintWriter out;
    boolean firstEntry;

    runStats = "";
    out = null;
    firstEntry = false;

    if (fileLogStats.isSelected()) {
      // Identify whether file exists, if not create and add header row
      try {
        new FileReader(DBSTATS_NAME).close();
      }
      catch (Exception any) {
        firstEntry = true;
      }

      try {
        out = new PrintWriter(new FileWriter(DBSTATS_NAME, true));
      }
      catch (Exception any) {
        logger.error("Failed to write the statistics file [" + DBSTATS_NAME +
            "]", any);
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
    out.print("\"" +
//        querySelection.getSelectedItem().toString().replace('"', '\'') +
        sqlStatement.replace('"', '\'') +
        "\",");

    // Output timestamp
    out.print(Utility.formattedDate(new java.util.Date()) + ",");

    // Output time required to get connection to database
    if (connAsk != null && connGot != null) {
      runStats += Resources.getString("proTimeConnOpen",
          (connGot.getTime() - connAsk.getTime()) + "");
      runStats += "  ";
      out.print((connGot.getTime() - connAsk.getTime()) + ",");
    } else {
      out.print("\"" + Resources.getString("proValueNotApplicable") + "\",");
    }

    // Output time required to get statement object
    if (connGot != null && stmtGot != null) {
      runStats += Resources.getString("proTimeStmtAccess",
          (stmtGot.getTime() - connGot.getTime()) + "");
      runStats += "  ";
      out.print((stmtGot.getTime() - connGot.getTime()) + ",");
    } else {
      out.print("\"" + Resources.getString("proValueNotApplicable") + "\",");
    }

    // Time it took to configure statement
    if (queryStart != null && stmtGot != null) {
      runStats += Resources.getString("proTimeStmtSetup",
          (queryStart.getTime() - stmtGot.getTime()) + "");
      runStats += "  ";
      out.print((queryStart.getTime() - stmtGot.getTime()) + ",");
    } else {
      out.print("\"" + Resources.getString("proValueNotApplicable") + "\",");
    }

    runStats += "\n          ";

    // Output time DB took to execute query
    if (queryStart != null && queryReady != null) {
      runStats += Resources.getString("proTimeDBExecute",
          (queryReady.getTime() - queryStart.getTime()) + "");
      runStats += "  ";
      out.print((queryReady.getTime() - queryStart.getTime()) + ",");
    } else {
      out.print("\"" + Resources.getString("proValueNotApplicable") + "\",");
    }

    // Output time it took to fetch all results
    if (queryReady != null && queryRSFetched != null) {
      runStats += Resources.getString("proTimeResultsFetch",
          (queryRSFetched.getTime() - queryReady.getTime()) + "");
      runStats += "  ";
      out.print((queryRSFetched.getTime() - queryReady.getTime()) + ",");
    } else {
      out.print("\"" + Resources.getString("proValueNotApplicable") + "\",");
    }

    // Output time it took to process all results
    if (queryReady != null && queryRSProcessed != null) {
      runStats += Resources.getString("proTimeResultSet",
          (queryRSProcessed.getTime() - queryReady.getTime()) + "");
      runStats += "  ";
      out.print((queryRSProcessed.getTime() - queryReady.getTime()) + ",");
    } else {
      out.print("\"" + Resources.getString("proValueNotApplicable") + "\",");
    }

    if (runStats.length() > 0) {
      messageOut(Resources.getString("proTimeDBStats") + " ", STYLE_SUBTLE, false);
      messageOut(runStats, STYLE_NORMAL, false);
      runStats = "";
    }

    // Output total time it took to obtain connection, execute SQL and obtain results
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
      out.print("\"" + Resources.getString("proValueNotApplicable") + "\",");
    }

    messageOut(runStats, STYLE_BOLD, true);

    // Output number of columns in resultset
    out.print(cols + ",");

    // Output number of rows returned or modified
    out.print(rows + "," + maxRows.getSelectedItem().toString() + ",");

    runtime = Runtime.getRuntime();

    // Output environment information
    out.print(runtime.totalMemory() + "," + runtime.freeMemory() + "," +
        (runtime.totalMemory() - runtime.freeMemory()) + "," +
        runtime.maxMemory() + "," + runtime.availableProcessors());

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

      messageOut(Resources.getString("proClientEnv") + " ", STYLE_SUBTLE, false);
      messageOut(runStats);
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
        out.print(" (" + model.getValueAt(row, 3) + "," +
            model.getValueAt(row, 4) + ")");

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
   * @param combo       The JComboBox - it can be the combobox that contains
   *                    all the queries or the combobox with the URLs
   * @param fileName    The name of the file that contains the queries or the
   *                    connect URLs
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
    }
    catch (Throwable any) {
      logger.error("Failed to load combo box with lookup values", any);
    }
    finally {
      if (in != null) {
        try {
          in.close();
        }
        catch (Throwable any) {
          logger.error("Failed to close the input file", any);
        }
      }
    }
  }

  /**
   * Writes all the queries in the query selection (or connection URLS) combobox
   * to the supplied file in a print formatted representation.
   *
   * @param combo       The JComboBox - it can be the combobox that contains
   *                    all the queries or the combobox with the URLs
   * @param fileName    The fileName to be written to contain the queries or the
   *                    connect URLs
   */
  private void writeOutCombo(JComboBox combo, String fileName) {
    PrintWriter out;

    if (combo != null) {
      out = null;
      try {
        out = new PrintWriter(new FileWriter(fileName));
        for (int i = 0; i < combo.getModel().getSize(); ++i) {
          if (combo == querySelection) {
            out.println(((Query)combo.getModel().getElementAt(i)).getSQL() +
                "[" +
                ((Query)combo.getModel().getElementAt(i)).getMode());
          } else {
            out.println((String)combo.getModel().getElementAt(i));
          }
        }
      }
      catch (Exception any) {
        logger.error("Failed to write out the content of the combobox", any);
      }
      finally {
        if (out != null) {
          try {
            out.close();
          }
          catch (Exception any) {
            logger.error("Failed to close the combobox output file", any);
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
          logger.debug("Blob[" +
              (java.sql.Blob)model.getValueAt(selectedRow, col) + "]");
          filesWritten +=
              writeBlob(model.getColumnName(col),
              (java.sql.Blob)model.getValueAt(selectedRow, col)) +
              "\n";
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
   * @param colName The column that contains the BLOB object
   * @param blob    The BLOB object that is written onto the file
   *
   * @return The name of the file created
   */
  private String writeBlob(String colName, java.sql.Blob blob) {
    FileOutputStream out;
    InputStream in;
    byte block[];
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
      block = new byte[4096];
      out = new FileOutputStream(fileName, false);
      in = blob.getBinaryStream();
      while ((bytesRead = in.read(block)) > 0) {
        out.write(block, 0, bytesRead);
      }
    }
    catch (Exception any) {
      logger.error("Failed to write BLOB field value to file [" + fileName +
          "]", any);
      fileName += " (" + Resources.getString("errFailBLOBWrite",
          any.getClass().getName(), any.getMessage()) + ")";
    }
    finally {
      if (out != null) {
        try {
          out.close();
        }
        catch (Exception any) {
          logger.error("Failed to close the BLOB output file", any);
        }
      }
    }

    return fileName;
  }

  /**
   * Create the query 'select * from' for selected the column in the table
   *
   * @param mode The integer value for the different modes
   */
  private void queryStar(int mode) {
    String value;

    logger.debug("Row:" + table.getSelectedRow() + " Col:" +
        table.getSelectedColumn());
    logger.debug("Row Count:" + table.getSelectedRowCount() +
        " Col Count:" + table.getSelectedColumnCount());
    if (table.getSelectedRowCount() != 1 || table.getSelectedColumnCount() != 1) {
      userMessage(Resources.getString("errChooseOneCellText"),
          Resources.getString("errChooseOneCellTitle"),
          JOptionPane.ERROR_MESSAGE);
    } else {
      value = table.getModel().getValueAt(table.getSelectedRow(),
          table.getSelectedColumn()).toString();
      logger.debug("Value class: " + value.getClass().getName());
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
   * comboBox as the  selected item.
   */
  private void makeInsert() {
    String[][] columns;
    String tables;
    String SQL;

    tables = getTablesInQuery();
    columns = getColumnNamesForTable();

    SQL = "insert into " + tables + "(";

    for (int i = 0; i < columns.length; ++i) {
      if (i > 0) {
        SQL += ",";
      }
      SQL += columns[i][0];
    }

    SQL += ") values (";

    for (int i = 0; i < columns.length; ++i) {
      if (columns[i][1].startsWith("VARCHAR") ||
          columns[i][1].startsWith("CHAR")) {
        SQL += "''";
      }
      if (i < columns.length - 1) {
        SQL += ",";
      }
    }

    SQL += ")";

    messageOut(Resources.getString("msgMadeSQL", SQL));
    queryText.setText(SQL);
    checkForNewString(querySelection);
    querySelection.setSelectedItem(SQL);
  }

  /**
   * Create an update statement depending on the tables and the columns that
   * were selected.The update statement is then listed in the querySelection
   * comboBox as the  selected item.
   */
  private void makeUpdate() {
    String[][] columns;
    String tables;
    String thisPhrase;
    String SQL;
    String where;
    boolean firstColOut;

    tables = getTablesInQuery();
    columns = getColumnNamesForTable();
    firstColOut = true;

    where = "";
    SQL = "update " + tables + " ";

    for (int i = 0; i < columns.length; ++i) {
      thisPhrase = columns[i][0] + "=";
      if (columns[i][1].startsWith("VARCHAR") ||
          columns[i][1].startsWith("CHAR")) {
        thisPhrase += "''";
      }

      if (columns[i][2] != null) {
        if (where.length() > 0) {
          where += " and ";
        }
        where += thisPhrase;
      } else {
        if (firstColOut) {
          SQL += "set ";
          firstColOut = false;
        } else {
          SQL += ", ";
        }

        SQL += thisPhrase;
      }
    }

    if (where.length() > 0) {
      SQL += " where " + where;
    }

    messageOut(Resources.getString("msgMadeSQL", SQL));
    queryText.setText(SQL);
    checkForNewString(querySelection);
    querySelection.setSelectedItem(SQL);
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
    String SQL;
    String where;
    boolean firstColOut;

    tables = getTablesInQuery();
    columns = getColumnNamesForTable();
    firstColOut = true;

    where = "";
    SQL = "";

    for (int i = 0; i < columns.length; ++i) {
      thisPhrase = columns[i][0] + "=";
      if (columns[i][1].startsWith("VARCHAR") ||
          columns[i][1].startsWith("CHAR")) {
        thisPhrase += "''";
      }

      if (columns[i][2] != null) {
        if (where.length() > 0) {
          where += " and ";
        }
        where += thisPhrase;
      } else {
        if (firstColOut) {
          SQL += "select ";
          firstColOut = false;
        } else {
          SQL += ", ";
        }

        SQL += columns[i][0];
      }
    }

    SQL += " from " + tables;

    if (where.length() > 0) {
      SQL += " where " + where;
    }

    messageOut(Resources.getString("msgMadeSQL", SQL));
    queryText.setText(SQL);
    checkForNewString(querySelection);
    querySelection.setSelectedItem(SQL);
  }

  /**
   * Gets the names of the tables that are referenced during the query
   *
   * @return tableNames A String that contains the table names
   */
  private String getTablesInQuery() {
    Iterator tables;
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
   * @return columns A string array with the Column Names and  Values
   */
  private String[][] getColumnNamesForTable() {
    String columns[][];
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
          columns[i][1] = columns[i][0].substring(columns[i][0].indexOf("[") +
              1);
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
   * Saves the Result in a file with the .csv extension
   */
  private void chooseFileForSave(TableModel model) {
    JFileChooser chooser = new JFileChooser();
    int returnVal;

    chooser.setDialogTitle(Resources.getString("dlgSaveDataTitle"));
    chooser.setApproveButtonText(Resources.getString("dlgSaveDataButton"));
    chooser.setApproveButtonMnemonic(Resources.getChar(
        "dlgSaveDataButtonMnemonic"));
    returnVal = chooser.showOpenDialog(this);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      saveResultAsCSV(model, chooser.getSelectedFile().getAbsoluteFile());
    }
  }

  /**
   * Saves the Result in a file with the .csv extension
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
      }
      catch (FileNotFoundException fnf) {
        okToWrite = true;
      }
      catch (Exception any) {
        logger.error("Error preparing to save data", any);
        messageOut(Resources.getString("errFailDataSavePrep", any.getMessage()),
            STYLE_RED);
      }
    }

    if (okToWrite) {
      writeDataAsCSV(getQuery().getSQL(), model, fileName, false);
    }
  }

  /**
   * Writes the data onto a file that is specified by the filePath
   *
   * @param filePath A String that denotes the filepath
   *
   * @todo Make this handle other data types (especially Date/Time) better
   * for cleaner import to spreadsheet.
   */
  private void writeDataAsCSV(String query, TableModel model, String filePath,
      boolean append) {
    PrintWriter out;
    int row, col;
    boolean quote[], quoteThis;
    String temp;
    Object value;

    out = null;

    this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    (flashRunIndicator = new Thread(new FlashForeground(runIndicator,
        Color.red.brighter(), Color.lightGray, 5))).start();
    (timeRunIndicator = new Thread(new InsertTime(timeIndicator,
        new java.util.Date().getTime(),
        250))).start();
    
    
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
//      out.print(getQuery().getSQL());
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
              temp = Utility.formattedDate((java.sql.Timestamp)value);
            } else {
              temp = model.getValueAt(row, col).toString();
              if (!fileExportsRaw.isSelected()) {
                temp = temp.replace('"', '\'');
                temp = temp.replace('\n', '~');
              }
            }
          }
          catch (Exception any) {
            logger.error("Failed to export data", any);

            // Display error and move on
            messageOut(Resources.getString("errFailDuringExport",
                any.getMessage()), STYLE_RED);
            temp = "";
          }

          // Decide if quotes are needed:
          //      -If the row is a known character type
          //      -If this field contains a comma
          quoteThis = !fileExportsRaw.isSelected() &&
              (quote[col] || temp.indexOf(",") > -1);

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
    }
    catch (Exception any) {
      logger.error("Unable to write data to file", any);
      messageOut(Resources.getString("errFailDataSave", any.toString()),
          STYLE_RED);
    }
    finally {
      if (out != null) {
        try {
          out.close();
        }
        catch (Exception any) {
          logger.error("Failed while writing data to CSV file", any);
        }
      }
    }

    flashRunIndicator.interrupt();
    flashRunIndicator = null;
    timeRunIndicator.interrupt();
    timeRunIndicator = null;
    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
}

private Object getResultField(ResultSet result, int columnNumber, int columnType) {
	Object value = null;
	
    try {
        switch (columnType) {
        case 1: // Integer
          value = new Integer(result.getInt(columnNumber));
          break;
        case 2: // Long
          value = new Long(result.getLong(columnNumber));
          break;
        case 3: // Double
          value = new Double(result.getDouble(columnNumber));
          break;
        case 5: // Date/Time
          value = result.getTimestamp(columnNumber);
          break;
        case 6: // BLOB
          try {
            value = result.getBlob(columnNumber);
          }
          catch (Throwable any) {
            logger.error("Error retrieving BLOB field key", any);

            // Probably unsupported method (first found in a MySQL Driver)
            value = result.getString(columnNumber);
          }
          break
              ;
        case 0: // Fall through to default
        default:
          value = result.getString(columnNumber);
          break;
      }
    }
    catch (Exception any) {
      logger.error("Error Column[" +
          columnNumber + " Type[" + columnType + "]", any);
      messageOut(Resources.getString("errFailDataRead",
          "" + columnNumber, any.getMessage()),
          STYLE_YELLOW);
      try {
      value = result.getString(columnNumber);
      }
      catch (Throwable throwable) {
          logger.error("Error Column[" +
                  columnNumber + " Type[" + columnType + "]", any);
              messageOut(Resources.getString("errFailDataRead",
                  "" + columnNumber, any.getMessage()),
                  STYLE_RED);
    	  throw new IllegalStateException("Error Column[" +
    	          columnNumber + " Type[" + columnType + "]");
      }
    }
    
    return value;
}
  /**
   * Writes the data onto a file that is specified by the filePath
   * This version writes the data from the ResultSet instance
   * so that the results do not need to be loaded into memory.
   *
   * @param filePath A String that denotes the filepath
   *
   * @todo Make this handle other data types (especially Date/Time) better
   * for cleaner import to spreadsheet.
   */
  private void writeDataAsCSV(String query, TableModel model, String filePath,
      ResultSet result, int[] myType, boolean append) {
    PrintWriter out;
    int row, col;
    boolean quote[], quoteThis;
    String temp;
    Object value;

    out = null;

    /*
    this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    (flashRunIndicator = new Thread(new FlashForeground(runIndicator,
        Color.red.brighter(), Color.lightGray, 5))).start();
    (timeRunIndicator = new Thread(new InsertTime(timeIndicator,
        new java.util.Date().getTime(),
        250))).start();
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
//      out.print(getQuery().getSQL());
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
    	if (row % 100000 == 0) {
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
              temp = Utility.formattedDate((java.sql.Timestamp)value);
            } else {
              temp = value.toString();
              if (!fileExportsRaw.isSelected()) {
                temp = temp.replace('"', '\'');
                temp = temp.replace('\n', '~');
              }
            }
          }
          catch (Exception any) {
            logger.error("Failed to export data", any);

            // Display error and move on
            messageOut(Resources.getString("errFailDuringExport",
                any.getMessage()), STYLE_RED);
            temp = "";
          }

          // Decide if quotes are needed:
          //      -If the row is a known character type
          //      -If this field contains a comma
          quoteThis = !fileExportsRaw.isSelected() &&
              (quote[col] || temp.indexOf(",") > -1);

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
    }
    catch (Exception any) {
      logger.error("Unable to write data to file", any);
      messageOut(Resources.getString("errFailDataSave", any.toString()),
          STYLE_RED);
    }
    finally {
      if (out != null) {
        try {
          out.close();
        }
        catch (Exception any) {
          logger.error("Failed while writing data to CSV file", any);
        }
      }
    }

    /*
    flashRunIndicator.interrupt();
    flashRunIndicator = null;
    timeRunIndicator.interrupt();
    timeRunIndicator = null;
    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
      queryText.setText(getQuery().getSQL());

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
  }

  /**
   * Executes the query(ies).  It is expected that this will be
   * run on its own thread -- hence it is a run method.
   */
  public void run() {

    this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    (flashRunIndicator = new Thread(new FlashForeground(runIndicator,
        Color.red.brighter(), Color.lightGray, 5))).start();
    (timeRunIndicator = new Thread(new InsertTime(timeIndicator,
        new java.util.Date().getTime(),
        250))).start();
    try {
      if (runType == RUNTYPE_ALL) {
        runAllQueries();
      } else {
        checkForNewString(querySelection);
        checkForNewString(connectString);
        processStatement();
      }
    }
    catch (Throwable any) {
      logger.error("Failure while trying to execute query", any);
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
    }
    catch (Exception any) {
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
        for (execution = 0; !monitor.isCanceled() && execution < numExecutions;
            ++execution) {
          monitor.setProgress(query * numExecutions + execution);
          if (elapsed <= 0) {
            monitor.setNote(Resources.getString("dlgRunAllQueriesProgressNote",
                (execution + 1) + "", numExecutions + "", (query + 1) + "",
                numQueries + ""));
          } else {
            remaining = elapsed * ((numExecutions * (numQueries - (query + 1))) +
                (numExecutions - execution));
            hours = (int)(remaining / 3600);
            remaining -= (hours * 3600);
            minutes = (int)(remaining / 60);
            remaining -= (minutes * 60);
            seconds = (int)remaining;

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
          elapsed = (int)((new java.util.Date().getTime() -
              start.getTime()) / 1000);
        }
      }
    } else {
      elapsed = -1;
      for (execution = 0; !monitor.isCanceled() && execution < numExecutions;
          ++execution) {
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
            hours = (int)(remaining / 3600);
            remaining -= (hours * 3600);
            minutes = (int)(remaining / 60);
            remaining -= (minutes * 60);
            seconds = (int)remaining;

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
        elapsed = (int)((new java.util.Date().getTime() -
            start.getTime()) / 1000);
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
      newSQL = query.getSQL();

      logger.debug("commentToggle::Orig[" + newSQL + "]");

      if (newSQL.startsWith(COMMENT_PREFIX)) {
        newSQL = newSQL.substring(2);
      } else {
        newSQL = COMMENT_PREFIX + newSQL;
      }

      logger.debug("commentToggle:New[" + newSQL + "]");

      replaceInCombo(querySelection, position, new Query(newSQL, query.getMode()));
    }
  }

  /**
   * Builds the GUI window title - including the currently open SQL statement
   * file.
   *
   * @param pathToSQLFile The path to the currently open SQL statement file
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
    File chosenSQLFile;
    int returnVal;

    chosenSQLFile = null;

    // Save current information, including SQL Statements
    saveConfig();

    // Allow user to choose/create new file for SQL Statements
    fileMenu = new JFileChooser(new File(queryFilename));
    fileMenu.setSelectedFile(new File(queryFilename));
    fileMenu.setDialogTitle(Resources.getString("dlgSQLFileTitle"));
    fileMenu.setDialogType(JFileChooser.OPEN_DIALOG);
    fileMenu.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileMenu.setMultiSelectionEnabled(false);

    if (fileMenu.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      chosenSQLFile = fileMenu.getSelectedFile();
//      if (!chosenSQLFile.getName().toLowerCase().endsWith(".txt")) {
//        chosenSQLFile = new File(chosenSQLFile.getAbsolutePath() +
//            ".txt");
//      }

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
        } else if (returnVal == JOptionPane.CANCEL_OPTION) {
          chosenSQLFile = null;
        }
      } else {
        setQueryFilename(chosenSQLFile.getAbsolutePath());
        querySelection.removeAllItems();
        queryText.setText("");
        loadCombo(querySelection, queryFilename);
      }
    }

    if (chosenSQLFile != null) {
      setQueryFilename(chosenSQLFile.getAbsolutePath());
      saveConfig();
    }
  }

  /**
   * Add latest query index to the history of executed queries
   * The query is added after the currently selected query in the history list.
   * The associated connect URL is also saved, though its use is controlled by a menu checkbox setting.
   */
  private void histMaintQueryExecuted(TableModel results) {
    int thisQuery, lastQuery;

    thisQuery = querySelection.getSelectedIndex();

    if (thisQuery >= 0) {

      if (historyPosition >= 0) {
        lastQuery = ((QueryHistory)historyQueries.get(historyPosition)).
            getSQLIndex();
      } else {
        lastQuery = -1;
      }

      if (thisQuery != lastQuery) {
        // If there were different next queries they are removed by the execution of
        // a query other than the one pointed to on the history list.
        histMaintRemoveFollowingQueries();
        historyQueries.add(new QueryHistory(thisQuery,
            connectString.getSelectedIndex(),
            results));
        // Update history position
        historyPosition = historyQueries.size() - 1;
      } else {
        ((QueryHistory)historyQueries.get(historyPosition)).setURLIndex(
            connectString.getSelectedIndex());
        ((QueryHistory)historyQueries.get(historyPosition)).setResults(
            results);
      }

      // Limit to defined number of entries
      if (historyQueries.size() > maxHistoryEntries) {
        historyQueries.remove(0);
        // Update history position
        historyPosition = historyQueries.size() - 1;
      }

      logger.debug("historyPosition=" + historyPosition);

      // Update GUI
      setPrevNextIndication();
    }
  }

  /**
   * Remove queries that follow the currently selected query in the history list.
   */
  private void histMaintRemoveFollowingQueries() {
    int histNewEnd;

    histNewEnd = historyPosition + 1;
    while (historyQueries.size() > histNewEnd) {
      logger.debug("Removed item at=" + histNewEnd);
      historyQueries.remove(histNewEnd);
    }

    logger.debug("historyPosition=" + historyPosition + " historyQueries.size=" +
        historyQueries.size());
  }

  /**
   * Activate/deactivate the previous and next query history indicators based
   * on the number of history entries and the current position within the history
   * list.
   */
  private void setPrevNextIndication() {
    previousQuery.setEnabled(historyPosition > -1);
    nextQuery.setEnabled(historyPosition < historyQueries.size() - 1);
  }

  /**
   * Maintain the query history list when an entry is deleted from the
   * collection stored of SQL queries.  For each entry in the history list the
   * impact of a deletion may be:
   * <ul>
   * <li>None, if the deleted query follows the index of the history entry
   * <li>Require subtracting one from the SQL history index position if it follows the deleted query; or
   * <li>Require removal of the history entry if the SQL index held is the index of the deleted query.
   * </ul>
   *
   * @param queryIndex The index of the deleted query in the stored query list.
   */
  private void histMaintQueryDeleted(int queryIndex) {
    int loop, thisIndex;

    loop = 0;
    while (loop < historyQueries.size()) {
      thisIndex = ((QueryHistory)historyQueries.get(loop)).getSQLIndex();
      if (thisIndex < queryIndex) {
        ++loop;
      } else if (thisIndex == queryIndex) {
        historyQueries.remove(loop);
        if (historyPosition >= loop) {
          historyPosition--;
        }
      } else {
        thisIndex--;
        ((QueryHistory)historyQueries.get(loop)).setSQLIndex(thisIndex);
        ++loop;
      }
    }
    setPrevNextIndication();
  }

  /**
   * Set the currently selected query to the previous SQL index in the history list.
   * If the user has selected to also associate the connect URL, then the last connect
   * URL used for the query will also be selected.
   */
  private void histSelectPrev() {
    // Two possibilities, the user has changed the selected query without executing
    // and wants to return to the last executed; or
    // the latest executed query is currently selected in which case the user
    // expects to return to a query before this one

    int currentSQLIndex, histSQLIndex, histConnectIndex;
    TableModel histModel;
    TableSorter sorter;

    currentSQLIndex = querySelection.getSelectedIndex();

    histSQLIndex = ((QueryHistory)historyQueries.get(historyPosition)).
        getSQLIndex();
    histConnectIndex = ((QueryHistory)historyQueries.get(historyPosition)).
        getURLIndex();
    histModel = ((QueryHistory)historyQueries.get(historyPosition)).
        getResults();

    if (currentSQLIndex == histSQLIndex && historyPosition > -1) {
      --historyPosition;
      if (historyPosition > -1) {
        histSQLIndex = ((QueryHistory)historyQueries.get(historyPosition)).
            getSQLIndex();
        histConnectIndex = ((QueryHistory)historyQueries.get(historyPosition)).
            getURLIndex();
        histModel = ((QueryHistory)historyQueries.get(historyPosition)).
            getResults();
      }
    }

    querySelection.setSelectedIndex(histSQLIndex);

    if (configHistoryAssocSQLAndConnect.isSelected()) {
      connectString.setSelectedIndex(histConnectIndex);
    }

    try {
      sorter = (TableSorter)table.getModel();
      sorter.removeMouseListenerFromHeaderInTable(table);
    }
    catch (Throwable any) {
      // Probably table was empty
    }
    table.setModel(new ListTableModel());
    if (histModel != null) {
      sorter = new TableSorter(histModel);
      table.setModel(sorter);
      sorter.addMouseListenerToHeaderInTable(table);
      Utility.initColumnSizes(table, sorter);
    }

    setPrevNextIndication();
  }

  /**
   * Set the currently selected query to the next SQL index in the history list.
   * If the user has selected to also associate the connect URL, then the last connect
   * URL used for the query will also be selected.
   */
  private void histSelectNext() {
    // Two possibilities, the user has changed the selected query without executing
    // and wants to move to what was the next executed query; or
    // a previous query is currently selected in which case the user
    // expects to return to a query after this one
    int currentSQLIndex, histSQLIndex, histConnectIndex;
    TableModel histModel;
    TableSorter sorter;

    currentSQLIndex = querySelection.getSelectedIndex();
    ++historyPosition;
    histSQLIndex = ((QueryHistory)historyQueries.get(historyPosition)).
        getSQLIndex();
    histConnectIndex = ((QueryHistory)historyQueries.get(historyPosition)).
        getURLIndex();
    histModel = ((QueryHistory)historyQueries.get(historyPosition)).
        getResults();

    if (currentSQLIndex == histSQLIndex &&
        historyPosition < historyQueries.size() - 1) {
      ++historyPosition;
      histSQLIndex = ((QueryHistory)historyQueries.get(historyPosition)).
          getSQLIndex();
      histConnectIndex = ((QueryHistory)historyQueries.get(historyPosition)).
          getURLIndex();
      histModel = ((QueryHistory)historyQueries.get(historyPosition)).
          getResults();
    }

    querySelection.setSelectedIndex(histSQLIndex);

    if (configHistoryAssocSQLAndConnect.isSelected()) {
      connectString.setSelectedIndex(histConnectIndex);
    }

    try {
      sorter = (TableSorter)table.getModel();
      sorter.removeMouseListenerFromHeaderInTable(table);
    }
    catch (Throwable any) {
      // Probably table was empty
    }
    table.setModel(new ListTableModel());
    if (histModel != null) {
      sorter = new TableSorter(histModel);
      table.setModel(sorter);
      sorter.addMouseListenerToHeaderInTable(table);
      Utility.initColumnSizes(table, sorter);
    }

    setPrevNextIndication();
  }

  /**
   * Allow the user to place the queries into a particluar order.  A separate
   * dialog is used that permits rearranging the list of queries.
   */
  private void reorderQueries() {
    java.util.List queries;

    queries = new ArrayList();

    for (int loop = 0; loop < querySelection.getItemCount(); ++loop) {
      queries.add(querySelection.getItemAt(loop));
    }

    ReorderDialog setOrder = new ReorderDialog(this, queries);

    if (setOrder.isReordered()) {
      rebuildSQLListing(setOrder.getAsOrdered());
    }
  }

  /**
   * Reloads the list of queries in the combobox.
   *
   * @param queries List The collection of queries
   */
  private void rebuildSQLListing(java.util.List queries) {
    historyQueries = new ArrayList();
    historyPosition = -1;

    querySelection.removeAllItems();
    for (int loop = 0; loop < queries.size(); ++loop) {
      addToCombo(querySelection, queries.get(loop));
    }
  }

  /**
   * Sort a table model based on the selected columns.
   */
  private void sortSelectedColumns() {
    int selectedViewColumns[], selectedModelColumns[];
    TableSorter sorter;

    selectedViewColumns = table.getSelectedColumns();
    selectedModelColumns = new int[selectedViewColumns.length];

    for (int column = 0; column < selectedViewColumns.length; ++column) {
      selectedModelColumns[column] =
          table.convertColumnIndexToModel(selectedViewColumns[column]);
    }

    try {
      sorter = (TableSorter)table.getModel();
      sorter.sortByColumns(selectedModelColumns);
    }
    catch (Throwable any) {
      // Probably table was empty
    }
  }

  /**
   * Run a SQL statement on its own thread.  If a statement is currently being
   * executed the user may either cancel the new request or the currently
   * running query.
   *
   * @param statementRunType The type of query execution -- single or
   * multiple statement
   */
  private void runIt(int statementRunType) {
    if (runningQuery != null) {
      if (!runningQuery.isAlive()) {
        runningQuery = null;
      } else {
        int decision = JOptionPane.showConfirmDialog(this,
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
   * Notify user if no DB drivers have been loaded.  Probably no DB JARs installed.
   */
  private void noDBDriverWarning() {
    JOptionPane.showMessageDialog(this,
        Resources.getString("dlgNoDBDriversLoadedText",
        new File(dbDriverDir).getAbsolutePath(),
        Configuration.instance().getFile(FILENAME_DRIVERS).getAbsolutePath()),
        Resources.getString("dlgNoDBDriversLoadedTitle"),
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Displays an information message in a dialog box giving details about the
   * BasicQuery that has been executed
   */
  private void about() {
    String slMessage;

    slMessage = "Execute arbitrary SQL statements against" +
        " any database with JDBC library.Records timing" +
        " and memory usage information to assist in" +
        " performance tuning of database interactions.\n\n";

    slMessage += "David Read, DaveRead@aol.com, www.daveread.us\n\n";

    slMessage += "Copyright (c) 2006\n\n" +
        "This program is free software; you can redistribute it and/or modify " +
        "it under the terms of the GNU General Public License as published by " +
        "the Free Software Foundation; either version 2 of the License, or " +
        "(at your option) any later version.\n\n" +
        "This program is distributed in the hope that it will be useful, " +
        "but WITHOUT ANY WARRANTY; without even the implied warranty of " +
        "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the " +
        "GNU General Public License for more details.\n\n" +
        "You should have received a copy of the GNU General Public License " +
        "along with this program; if not, write to the:\n" +
        "     Free Software Foundation Inc.\n" +
        "     59 Temple Place, Suite 330\n" +
        "     Boston, MA  02111-1307  USA";

    userMessage("BasicQuery SQL Execution Tool\n\n" +
        "Version: " + VERSION + "\n\n" +
        Utility.characterInsert(slMessage, "\n", 70, 90, " .") +
        "\n\nBuild Information:\n" +
        "  " + ID + "\n" +
        "  " + Query.ID + "\n" +
        "  " + Utility.ID + "\n" +
        "  " + StatementParameter.ID + "\n" +
        "  " + DynamicClassLoader.ID,
        "About BasicQuery", JOptionPane.INFORMATION_MESSAGE, false);
  }

  /**
   * Supply a brief usage summary for the parameterized SQL syntax.
   */
  private void helpParameterizedSQL() {
    String message;
    String dataTypes[];
    int loop;

    dataTypes = SQLTypes.getKnownTypeNames();

    message = Resources.getString("dlgUsingParamsTextOverview") + " ";

    for (loop = 0; loop < dataTypes.length; ++loop) {
      message += dataTypes[loop];
      if (loop < dataTypes.length - 1) {
        message += ", ";
      }
      if ((loop + 1) % 5 == 0) {
        message += "\n                           ";
      }
    }

    message += Resources.getString("dlgUsingParamsTextExamples") + " ";

    JOptionPane.showMessageDialog(this, message,
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
   * Copy the selected data cells to the clipboard
   */
  private void copySelectionToClipboard() {
    Action objlCopy;

    try {
      objlCopy = TransferHandler.getCopyAction();
      objlCopy.actionPerformed(new ActionEvent(table,
          ActionEvent.ACTION_PERFORMED,
          (String)objlCopy.getValue(
          Action.
          NAME), EventQueue.getMostRecentEventTime(), 0));
    }
    catch (Throwable any) {
      logger.error("Failed to copy data to clipboard", any);
      JOptionPane.showMessageDialog(this,
          Resources.getString("errClipboardCopyDataText",
          any.getClass().getName(), any.getMessage() != null ?
          any.getMessage() : ""),
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
      cellRenderer.addAlternatingRowColor(Color.black, new Color(192, 255, 192));
    } else if (configTableColoringYellowBar.isSelected()) {
      cellRenderer.addAlternatingRowColor(Color.black, Color.white);
      cellRenderer.addAlternatingRowColor(Color.black, new Color(255, 255, 192));
    } else if (configTableColoringUserDefined.isSelected()) {
      setupUserDefinedColoring();
    }

    if (table != null) {
      table.repaint();
    }
  }

  /**
   * Allow the user to select a font (including size/bold/italic).  This setting
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
   * @param font Font The font to use for the message and table displays.
   */
  private void setupFont(Font font) {
    if (font != null) {
      this.setFont(font);
      message.setFont(font);
      cellRenderer.setText(
          "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
      cellRenderer.setFont(font);

      // For some reason the cellRenderer preferred size equals the font height
      // if no cells are rendered at the moment.  Tried creating a one-row
      // model and displaying while changing font size but it didn't help --
      // probably because it wasn't rendered until after the setupFont method
      // completed.
      if (table.getRowCount() == 0) {
        table.setRowHeight(cellRenderer.getPreferredSize().height + 5);
      } else {
        table.setRowHeight(cellRenderer.getPreferredSize().height);
      }
//        table.setRowHeight(this.getGraphics().getFontMetrics(font).getHeight());
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
          logger.error("Cannot launch browser to access download page",
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
   * Cleanup environment.  Normally called before closing application.
   */
  private void cleanUp() {
    if (conn != null) {
      try {
        conn.close();
      }
      catch (Throwable any) {

      }
      conn = null;
    }
  }

// Begin ActionListener Interface

  /**
   * Depending on which option the user has invoked the
   * appropriate action is performed
   *
   * @param evt Specifies the action event that has taken place
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
      chooseFileForSave(table.getModel());
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
    } else if (source == configTableColoringNone ||
        source == configTableColoringGreenBar ||
        source == configTableColoringYellowBar ||
        source == configTableColoringUserDefined) {
      setupResultsTableColoring();
    } else if (source == configLanguageDefault ||
        source == configLanguageEnglish ||
        source == configLanguageFrench ||
        source == configLanguageGerman ||
        source == configLanguageItalian ||
        source == configLanguagePortuguese ||
        source == configLanguageSpanish) {
      changeLanguage();
    } else if (source == configFont) {
      chooseFont();
    }
  }

// End ActionListener Interface

// Begin WindowListener Interface

  /**
   * Invoked when the Window is set to be the active Window.
   *
   * @param evt  The event that specifies that the window is activated
   */
  public void windowActivated(WindowEvent evt) {
  }

  /**
   * Invoked when a window has been closed as the result of calling
   * dispose on the window.
   *
   * @param evt  The Window event
   */
  public void windowClosed(WindowEvent evt) {
  }

  /**
   * Invoked when the user attempts to close the window from the
   * window's system menu.Exits the application once the window
   * is closed
   *
   * @param evt The event that specifies the closing of the window
   */
  public void windowClosing(WindowEvent evt) {
    saveConfig();
    cleanUp();
    try {
      dispose();
    }
    catch (Throwable any) {
      // This fails periodically with a NP exception
      // from Container.removeNotify
      logger.warn("Error during application shutdown", any);
    }

    System.exit(0);
  }

  /**
   * Invoked when a Window is no longer the active Window
   *
   * @param evt  The event that specifies that the window is deactivated
   */
  public void windowDeactivated(WindowEvent evt) {
  }

  /**
   * Invoked when a window is changed from a minimized to a normal state.
   *
   * @param evt  The event that specifies that the window is deiconified
   */
  public void windowDeiconified(WindowEvent evt) {
  }

  /**
   * Invoked when a window is changed from a normal to a minimized state
   *
   * @param evt  The event that specifies that the window is iconified
   */
  public void windowIconified(WindowEvent evt) {
  }

  /**
   * Creates a new thread that attaches focus to the window that has been
   * opened and also places the cursor on the textfield for the userid
   *
   * @param evt The event that specifies the opening of the window
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
    logger.debug("Update received from " + o.getClass().getName());

    if (o instanceof CheckLatestVersion) {
      notifyNewerVersion((NewVersionInformation) arg);
    }
  }
  
  /**
   * Main startup method for the BasicQuery application.
   * Creates an object of the class BasicQuery.
   *
   * @param args Input arguments -- not currently used
   */
  public final static void main(String args[]) {
    try {
      new BasicQuery(true);
    }
    catch (Throwable any) {
      logger.fatal("Application failure", any);

      // In case logger isn't setup - send this fatal issue to standard out
      System.out.println("BasicQuery:main:Exception:" + any);
      any.printStackTrace();
    }
  }
}
