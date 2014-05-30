package us.daveread.basicquery.test;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

import junit.framework.TestCase;

import us.daveread.basicquery.DynamicClassLoader;

/**
 * <p>Title: Test the dynamic class loader class</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006-2014</p>
 *
 * <p>Company: </p>
 *
 * @author David Read
 */
public class DynamicClassLoaderTest extends TestCase {
  /**
   * The classloader instance to test
   */
  private DynamicClassLoader loader;

  /**
   * Setup the test case instance
   */
  public DynamicClassLoaderTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    final List<File> libs = new ArrayList<File>();

    // Should be a valid library
    libs.add(new File(System.getProperty("java.home") + "/lib/rt.jar"));

    // Should be a badly formed URL
    libs.add(new File("getfile:// ^%$.nofile"));

    loader = new DynamicClassLoader(libs);
  }

  /**
   * Test loading an existing class without resolving it
   */
  public void testLoadClassNoResolve() {
    try {
      assertNotNull(loader.loadClass("java.lang.Package"));
    }
    catch (ClassNotFoundException cnf) {
      assertTrue("Should have found class", false);
    }
  }

  /**
   * Test loading an existing class and resolving it
   */
  public void testLoadClassWithResolve() {
    try {
      assertNotNull(loader.loadClass("java.lang.Package", true));
    }
    catch (ClassNotFoundException cnf) {
      assertTrue("Should have found class", false);
    }
  }

  /**
   * Test attempting to load a nonexistent class
   */
  public void testLoadNonexistantClass() {
    try {
      assertNotNull(loader.loadClass("java.lang.NoSuchPackage", true));
      assertTrue("Should not have found class", false);
    }
    catch (ClassNotFoundException cnf) {
      assertTrue("Should not have found class", true);
    }
  }
}
