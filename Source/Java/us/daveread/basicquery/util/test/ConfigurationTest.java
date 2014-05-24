package us.daveread.basicquery.util.test;

import java.io.*;
import junit.framework.*;

import us.daveread.basicquery.util.Configuration;

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
 * @version $Id: ConfigurationTest.java,v 1.1 2006/05/18 22:15:05 daveread Exp $
 */
public class ConfigurationTest extends TestCase {
  private static final String UNITTESTFILENAME = "BasicQuery.UnitTestCOnfigurationFile";

  private Configuration config;

  public ConfigurationTest() {
  }

  public void setUp() {
    config = Configuration.instance();
  }

  private void createTestFile() {
    File defaultFile;
    PrintWriter out;

    defaultFile = new File(UNITTESTFILENAME + ".txt");

    out = null;

    try {
      out = new PrintWriter(new FileWriter(defaultFile, false));
      out.println("This file is created and used during unit testing.");
      out.println("You may delete it.");
    }
    catch (Throwable any) {

    }
    finally {
      if (out != null) {
        try {
          out.close();
        }
        catch (Throwable any) {

        }
      }
    }
  }

  public void testStore() {
    File prop;
    long oldTimeStamp;

    prop = config.getFile("BasicQuery.Properties");
    oldTimeStamp = prop.lastModified();

    config.store();

    assertTrue(prop.lastModified() > oldTimeStamp);
  }

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

  public void testGetFileFailure() {
    assertFalse(config.getFile("NO.SUCH.FILE").exists());
  }
}
