package us.daveread.basicquery.test;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

import junit.framework.*;

import us.daveread.basicquery.DynamicClassLoader;

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
 * @version $Id: DynamicClassLoaderTest.java,v 1.2 2006/05/04 03:38:48 daveread Exp $
 */
public class DynamicClassLoaderTest extends TestCase {
  private DynamicClassLoader loader;

  public DynamicClassLoaderTest() {
  }

  public void setUp() {
    List libs = new ArrayList();

    // Should be a valid library
    libs.add(new File(System.getProperty("java.home") + "/lib/rt.jar"));

    // Should be a badly formed URL
    libs.add(new File("getfile:// ^%$.nofile"));

    loader = new DynamicClassLoader(libs);
  }

  public void testLoadClass_1() {
    try {
      assertNotNull(loader.loadClass("java.lang.Package"));
    }
    catch (ClassNotFoundException cnf) {
      assertTrue("Should have found class", false);
    }
  }

  public void testLoadClass_2() {
    try {
      assertNotNull(loader.loadClass("java.lang.Package", true));
    }
    catch (ClassNotFoundException cnf) {
      assertTrue("Should have found class", false);
    }
  }

  public void testLoadClass_3() {
    try {
      assertNotNull(loader.loadClass("java.lang.NoSuchPackage", true));
      assertTrue("Should not have found class", false);
    }
    catch (ClassNotFoundException cnf) {
      assertTrue("Should not have found class", true);
    }
  }
}
