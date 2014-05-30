package us.daveread.basicquery.util.test;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import junit.framework.TestCase;
import us.daveread.basicquery.util.Configuration;

/**
 * <p>
 * Title: Test the Configuration class
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
public class ConfigurationTest extends TestCase {
  /**
   * Logger
   */
  private static final Logger LOGGER = Logger
      .getLogger(ConfigurationTest.class);

  /**
   * Configuration for the unit test to use
   */
  private static final String UNITTESTFILENAME = "BasicQuery.UnitTestConfigurationFile";

  /**
   * The configuration instance to test
   */
  private Configuration config;

  /**
   * Setup the test case instance
   */
  public ConfigurationTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    config = Configuration.instance();
  }

  /**
   * Create a test configuration file
   */
  private void createTestFile() {
    File defaultFile;
    PrintWriter out;

    defaultFile = new File(UNITTESTFILENAME + ".txt");

    out = null;

    try {
      out = new PrintWriter(new FileWriter(defaultFile, false));
      out.println("This file is created and used during unit testing.");
      out.println("You may delete it.");
    } catch (Throwable any) {
      LOGGER.error("Error setting up the test configuration file: "
          + defaultFile, any);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (Throwable any) {
          LOGGER.warn("Error closing the test configuration file: "
              + defaultFile, any);

        }
      }
    }
  }

  /**
   * Test storing the configuration to a file
   */
  public void testStore() {
    File prop;
    long oldTimeStamp;

    prop = config.getFile("BasicQuery.Properties");
    oldTimeStamp = prop.lastModified();

    config.store();

    assertTrue(prop.lastModified() > oldTimeStamp);
  }

  /**
   * Test loading the configuration file
   */
  public void testGetFileSuccess() {
    File unitTestFile;

    unitTestFile = config.getFile(UNITTESTFILENAME);

    // Setup - remove existing non-default copy if necessary
    if (unitTestFile != null) {
      unitTestFile.delete();
    }

    // Create the default version of the file
    createTestFile();

    // Get the file - testing the copy of the default copy
    unitTestFile = config.getFile(UNITTESTFILENAME);

    assertNotNull(unitTestFile);

    // Clean up

    // Remove the copied file
    unitTestFile.delete();

    unitTestFile = new File(UNITTESTFILENAME + ".txt");

    // Remove the default file
    if (unitTestFile.exists()) {
      unitTestFile.delete();
    }
  }

  /**
   * Test attmpting to load a non-existant configuration file
   */
  public void testGetFileFailure() {
    assertFalse(config.getFile("NO.SUCH.FILE").exists());
  }
}
