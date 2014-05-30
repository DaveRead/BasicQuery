package us.daveread.basicquery.test;

import java.sql.Driver;

import us.daveread.basicquery.DynamicDriver;
import java.util.Properties;
import java.sql.SQLException;
import java.sql.DriverPropertyInfo;
import java.sql.Connection;

import junit.framework.TestCase;

/**
 * <p>
 * Title: Test the dynamic driver class. Includes a mock driver to support the
 * testing
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
public class DynamicDriverTest extends TestCase implements Driver {
  /**
   * The dynamic driver instance to test
   */
  private DynamicDriver driver;

  /**
   * Setup the test case instance
   */
  public DynamicDriverTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    driver = new DynamicDriver(this);
  }

  /**
   * Test the accepts URLs method
   */
  public void testAcceptsURL() {
    try {
      assertTrue(driver.acceptsURL("anyvalue"));
    } catch (SQLException sql) {
      assertTrue("Should not have an error", false);
    }
  }

  /**
   * Test the connect method
   */
  public void testConnect() {
    try {
      assertNull(driver.connect("anyvalue", null));
    } catch (SQLException sql) {
      assertTrue("Should not have an error", false);
    }
  }

  /**
   * Test the get property info method
   */
  public void testGetPropertyInfo() {
    DriverPropertyInfo[] props;

    try {
      props = driver.getPropertyInfo("testval", null);
      assertTrue(props.length == 1);
      assertTrue(props[0].value.equals("testval"));
    } catch (SQLException sql) {
      assertTrue("Should not have an error", false);
    }
  }

  /**
   * Test the get major version method
   */
  public void testGetMajorVersion() {
    assertEquals(1, driver.getMajorVersion());
  }

  /**
   * Test the get minor version method
   */
  public void testGetMinorVersion() {
    assertEquals(0, driver.getMinorVersion());
  }

  /**
   * Test the JDBC compliant method
   */
  public void testJDBCCompliant() {
    assertTrue(driver.jdbcCompliant());
  }

  /**
   * Create a driver property array with one property having the given URL
   * 
   * @param url
   *          The URL
   * @param props
   *          The properties for the driver
   * 
   * @return The created driver property info instance
   */
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties props) {
    DriverPropertyInfo[] info;

    info = new DriverPropertyInfo[1];
    info[0] = new DriverPropertyInfo("request", url);

    return info;
  }

  /**
   * Return null for the connection
   * 
   * @param url
   *          The url to connect to
   * @param prop
   *          The properties for the connection
   * 
   * @return Null is always returned
   * 
   * @throws SQLException
   *           If an error occurs
   */
  public Connection connect(String url, Properties prop) throws SQLException {
    return null;
  }

  /**
   * Return true for accepts connections
   * 
   * @param url
   *          The URL to connect to
   * 
   * @return True is always returned
   * 
   * @throws SQLException
   *           If an error occurs
   */
  public boolean acceptsURL(String url) throws SQLException {
    return true;
  }

  /**
   * Return true for JDBC compliant
   * 
   * @return True is always returned
   */
  public boolean jdbcCompliant() {
    return true;
  }

  /**
   * Return 1 for the major version
   * 
   * @return The value 1 is always returned
   */
  public int getMajorVersion() {
    return 1;
  }

  /**
   * Return 0 for the minor version
   * 
   * @return The value 0 is always returned
   */
  public int getMinorVersion() {
    return 0;
  }
}
