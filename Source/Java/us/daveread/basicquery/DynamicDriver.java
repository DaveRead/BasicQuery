package us.daveread.basicquery;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * <p>Title: Dynamic driver</p>
 * <p>Description: Support for loading a DB driver at runtime.  Implements the
 *        java.sql.Driver interface.  See the interface documentation for
 *        descriptions of the methods.</p>
 * <p>Copyright: Copyright (c) 2004-2014, David Read</p>
 * <p>This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.</p>
 * <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.</p>
 * <p>You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA</p>
 * <p></p>
 * @author David Read
 */

public class DynamicDriver implements Driver {
  /**
   * The driver for accessing the database
   */
  private Driver driver;

  /**
   * Creates a DynamicDriver instance from a Driver
   * @param d The driver to be created
   */
  public DynamicDriver(Driver d) {
    driver = d;
  }

  @Override
  public boolean acceptsURL(String url) throws SQLException {
    return driver.acceptsURL(url);
  }

  @Override
  public Connection connect(String url, Properties prop) throws SQLException {
    return driver.connect(url, prop);
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties prop) throws
      SQLException {
    return driver.getPropertyInfo(url, prop);
  }

  @Override
  public int getMajorVersion() {
    return driver.getMajorVersion();
  }

  @Override
  public int getMinorVersion() {
    return driver.getMinorVersion();
  }

  @Override
  public boolean jdbcCompliant() {
    return driver.jdbcCompliant();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return driver.getParentLogger();
  }
}
