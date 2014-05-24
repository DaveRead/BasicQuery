package us.daveread.basicquery;

/**
 * Title:        Query
 * <p>Description:  Represents an individual SQL statement.  For BasicQuery this
 *     is both the SQL statement and the mode (select or update) that is used
 *     when executing the statement.</p>
 * <p>Copyright: Copyright (c) 2004, David Read</p>
 * <p>This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.</p>
 *  <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.</p>
 *  <p>You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA</p>
 * <p></p>
 * @author David Read
 * @version $Id: Query.java,v 1.4 2006/05/30 18:36:43 daveread Exp $
 */

public class Query {
  public final static String ID =
      "$Id: Query.java,v 1.4 2006/05/30 18:36:43 daveread Exp $";

  public final static int MODE_QUERY = 0;
  public final static int MODE_UPDATE = 1;
  public final static int MODE_DESCRIBE = 2;

  private String SQL;
  private int Mode;
  private String rawSQL;

  /**
   * Constructs a Query
   *
   * Assumes the mode is a select statement (returns result set)
   *
   * @param aSQL The String to be queried
   */
  public Query(String aRawSQL) {
    setSQL(aRawSQL);
//    SQL = aSQL;
    Mode = MODE_QUERY;
  }

  /**
   * Constructs a Query to perform the operation specified by aMode on the
   * string aSQL
   *
   * @param aSQL  The String to be queried
   * @param aMode The mode value
   */
  public Query(String aRawSQL, int aMode) {
    setSQL(aRawSQL);
//    SQL = aSQL;
    Mode = aMode;
  }

  /**
   * Set the mode value to aMode depending on whether the user
   * wants to update,describe or query
   *
   * @param aMode The mode value that is set
   */
  public void setMode(int aMode) {
    Mode = aMode;
  }

  private void setSQL(String aRawSQL) {
    rawSQL = aRawSQL.trim();
    SQL = rawSQL.replace('\n', ' ');
  }

  /**
   * Returns the SQL string
   *
   * @return String
   */
  public String getSQL() {
    return SQL;
  }

  public boolean isCommented() {
    return SQL.startsWith("//");
  }

  public String getRawSQL() {
    return rawSQL;
  }

  public boolean equals(Object object) {
    return equals((Query)object);
  }

  public boolean equals(Query query) {
    String thisQuery, otherQuery;

    if (query == null) {
      return false;
    }

    thisQuery = getRawSQL();
    if (isCommented()) {
      thisQuery = thisQuery.substring(2);
    }

    otherQuery = query.getRawSQL();
    if (query.isCommented()) {
      otherQuery = otherQuery.substring(2);
    }

    return thisQuery.equals(otherQuery);
  }

  /**
   * Returns the Query Mode
   *
   * @return int
   */
  public int getMode() {
    return Mode;
  }

  /**
   * Returns the SQL statment as a string
   *
   * @return String
   */
  public String toString() {
    return getSQL();
  }
}
