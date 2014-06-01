package us.daveread.basicquery.queries;

/**
 * Title: Query
 * <p>
 * Description: Represents an individual SQL statement. For BasicQuery this is
 * both the SQL statement and the mode (select or update) that is used when
 * executing the statement.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004-2014, David Read
 * </p>
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * </p>
 * <p>
 * </p>
 * 
 * @author David Read
 */

public class Query {
  /**
   * Represents a select-type statement (e.g. produces a result set)
   */
  public static final int MODE_QUERY = 0;

  /**
   * Represents and update-type statement (e.g. changes the data with no result
   * set)
   */
  public static final int MODE_UPDATE = 1;

  /**
   * Represents a select-type statement (e.g. produces a result set) but reports
   * the metadata from the result set instead of the data
   */
  public static final int MODE_DESCRIBE = 2;

  /**
   * The SQL statement
   */
  private String sqlStatement;

  /**
   * The mode for this statement
   */
  private int mode;

  /**
   * The raw statement (e.g. as entered by the user - potentially with carriage
   * returns embedded)
   */
  private String rawSqlStatement;

  /**
   * Constructs a Query
   * 
   * Assumes the mode is a select statement (returns result set)
   * 
   * @param aRawSql
   *          The String to be queried
   */
  public Query(String aRawSql) {
    setSql(aRawSql);
    mode = MODE_QUERY;
  }

  /**
   * Constructs a Query to perform the operation specified by the mode
   * 
   * @param aRawSql
   *          The SQL statement
   * @param aMode
   *          The mode value
   */
  public Query(String aRawSql, int aMode) {
    setSql(aRawSql);
    mode = aMode;
  }

  /**
   * Set the mode value to aMode depending on whether the user
   * wants to update,describe or query
   * 
   * @param aMode
   *          The mode value that is set
   */
  public void setMode(int aMode) {
    mode = aMode;
  }

  /**
   * Set the SQL statement for this query to wrap
   * 
   * @param aRawSql The SQL statement
   */
  private void setSql(String aRawSql) {
    rawSqlStatement = aRawSql.trim();
    sqlStatement = rawSqlStatement.replace('\n', ' ');
  }

  /**
   * Returns the SQL string
   * 
   * @return String
   */
  public String getSql() {
    return sqlStatement;
  }
  
  /**
   * Is the statement commented out
   * 
   * @return True if the statement is commented out
   */
  public boolean isCommented() {
    return sqlStatement.startsWith("//");
  }

  /**
   * The Raw SQL statement as entered, potentially contains carriage returns
   * 
   * @return The raw SQL statement
   */
  public String getRawSql() {
    return rawSqlStatement;
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof Query) {
      return equals((Query) object);
    } 
    
    return false;
  }

  /**
   * Check whether this Query instance is equavalent to another. Equivalence is
   * true if the SQL statements are the same
   * 
   * @param query A query to compare to this query
   * 
   * @return True if the queries are equivalent
   */
  public boolean equals(Query query) {
    String thisQuery, otherQuery;

    if (query == null) {
      return false;
    }

    thisQuery = getRawSql();
    
    if (isCommented()) {
      thisQuery = thisQuery.substring(2).trim();
    }

    otherQuery = query.getRawSql();
    
    if (query.isCommented()) {
      otherQuery = otherQuery.substring(2).trim();
    }

    return thisQuery.equals(otherQuery);
  }
  
  @Override
  public int hashCode() {
    if (isCommented()) {
      return getRawSql().substring(2).trim().hashCode();
    } else {
      return getRawSql().hashCode();
    }
  }

  /**
   * Returns the Query Mode
   * 
   * @return The query mode
   */
  public int getMode() {
    return mode;
  }

  /**
   * Returns the SQL statement as a string
   * 
   * @return The SQL statement
   */
  public String toString() {
    return getSql();
  }
}
