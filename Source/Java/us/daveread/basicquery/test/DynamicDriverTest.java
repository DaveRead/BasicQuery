package us.daveread.basicquery.test;

import java.sql.Driver;

import junit.framework.*;

import us.daveread.basicquery.DynamicDriver;
import java.util.Properties;
import java.sql.SQLException;
import java.sql.DriverPropertyInfo;
import java.sql.Connection;

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
 * @version $Id: DynamicDriverTest.java,v 1.1 2006/05/01 20:19:31 daveread Exp $
 */
public class DynamicDriverTest extends TestCase implements Driver {
  private DynamicDriver driver;

  public DynamicDriverTest() {
  }

  public void setUp() {
    driver = new DynamicDriver(this);
  }

  public void testAcceptsURL() {
    try {
      assertTrue(driver.acceptsURL("anyvalue"));
    }
    catch (SQLException sql) {
      assertTrue("Should not have an error", false);
    }
  }

  public void testConnect() {
    try {
      assertNull(driver.connect("anyvalue", null));
    }
    catch (SQLException sql) {
      assertTrue("Should not have an error", false);
    }
  }

  public void testGetPropertyInfo() {
    DriverPropertyInfo[] props;

    try {
      props = driver.getPropertyInfo("testval", null);
      assertTrue(props.length == 1);
      assertTrue(props[0].value.equals("testval"));
    }
    catch (SQLException sql) {
      assertTrue("Should not have an error", false);
    }
  }

  public void testGetMajorVersion() {
    assertEquals(1, driver.getMajorVersion());
  }

  public void testGetMinorVersion() {
    assertEquals(0, driver.getMinorVersion());
  }

  public void testJDBCCompliant() {
    assertTrue(driver.jdbcCompliant());
  }

  public DriverPropertyInfo[] getPropertyInfo(String url, Properties prop) {
    DriverPropertyInfo[] info;

    info = new DriverPropertyInfo[1];
    info[0] = new DriverPropertyInfo("request", url);

    return info;
  }

  public Connection connect(String url, Properties prop) throws SQLException {
    return null;
  }

  public boolean acceptsURL(String url) throws SQLException {
    return true;
  }

  public boolean jdbcCompliant() {
    return true;
  }

  public int getMajorVersion() {
    return 1;
  }

  public int getMinorVersion() {
    return 0;
  }

}
