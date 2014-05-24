package us.daveread.basicquery.util;

import java.util.*;
import java.io.*;
import org.apache.log4j.Logger;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author David Read
 * @version $Id: Configuration.java,v 1.2 2006/05/18 22:14:39 daveread Exp $
 */
public class Configuration extends Properties {
  private static Configuration config;
  private static final Logger logger = Logger.getLogger(Configuration.class);

  private final static String PROPERTIES_DIRECTORY = "BasicQuery";
  private final static String FILENAME_PROPERTIES = "BasicQuery.Properties";
  private final static String PROP_SYSTEM_USERHOMEDIR = "user.home";

  private static String userHomeDirectory;

  private Configuration() {
    userHomeDirectory = System.getProperty(PROP_SYSTEM_USERHOMEDIR);

    if (userHomeDirectory == null) {
      userHomeDirectory = PROPERTIES_DIRECTORY;
    } else {
      userHomeDirectory += "/" + PROPERTIES_DIRECTORY;
      new File(userHomeDirectory).mkdirs();
    }
  }

  public static synchronized Configuration instance() {
    if (config == null) {
      config = new Configuration();
    }

    config.setupProperties();

    return config;
  }

  public void store() {
    File configFile;

    configFile = getFile(FILENAME_PROPERTIES);

    try {
      store(new FileOutputStream(configFile, false), "BasicQuery Configuration");
    }
    catch (Throwable any) {
      logger.error("Unable to store properties file (" +
          configFile.getAbsolutePath() + ")", any);
    }
  }

  private void setupProperties() {
    File propFile;

    propFile = getFile(FILENAME_PROPERTIES);

    try {
      config.load(new FileInputStream(propFile));
    }
    catch (Throwable any) {
      logger.error("Unable to load properties file (" +
          propFile.getAbsolutePath() + ")", any);
    }
  }

  public File getFile(String fileName) {
    File configFile;

    configFile = new File(getFilePath(fileName));
    if (!configFile.exists()) {
      userDefaultFile(fileName);
    }

    return configFile;
  }

  private String getFilePath(String fileName) {
    return userHomeDirectory + "/" + fileName;
  }

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
    }
    catch (Throwable any) {
      logger.warn("Unable to copy default file (" + source.getAbsolutePath() +
          ") to configuration file (" + dest.getAbsolutePath() + ")", any);
    }
    finally {
      if (in != null) {
        try {
          in.close();
        }
        catch (Throwable any) {

        }
      }
      if (out != null) {
        try {
          out.close();
        }
        catch (Throwable any) {

        }
      }
    }
  }
}
