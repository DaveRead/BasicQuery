package us.daveread.basicquery;

import java.sql.*;
import java.util.Properties;

/**
 * <p>Title: Dynamic driver</p>
 * <p>Description: Support for loading a DB driver at runtime.  Implements the
 *        java.sql.Driver interface.  See the interface documentation for
 *        descriptions of the methods.</p>
 * <p>Copyright: Copyright (c) 2004, David Read</p>
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
 * @version $Id: DynamicDriver.java,v 1.2 2006/04/20 03:07:28 daveread Exp $
 */

public class DynamicDriver implements Driver {
  private Driver driver;

  /**
   * Creates a DynamicDriver instance from a Driver
   * @param d The driver to be created
   */
  public DynamicDriver(Driver d) {
    driver = d;
  }

  public boolean acceptsURL(String url) throws SQLException {
    return driver.acceptsURL(url);
  }

  public Connection connect(String url, Properties prop) throws SQLException {
    return driver.connect(url, prop);
  }

  public DriverPropertyInfo[] getPropertyInfo(String url, Properties prop) throws
      SQLException {
    return driver.getPropertyInfo(url, prop);
  }

  public int getMajorVersion() {
    return driver.getMajorVersion();
  }

  public int getMinorVersion() {
    return driver.getMinorVersion();
  }

  public boolean jdbcCompliant() {
    return driver.jdbcCompliant();
  }
}
