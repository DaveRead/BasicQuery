package us.daveread.basicquery.queries;

import java.lang.ref.SoftReference;
import javax.swing.table.TableModel;

/**
 * Title: QueryInfo
 * <p>
 * Description: Represents an individual SQL statement index and associated URL.
 * This is used in a collection to retain a history list of executed queries.
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

public class QueryInfo {
  /**
   * The index in the list of SQL statements represented by this history entry
   */
  private int sqlIndex;

  /**
   * The index in the list of connection strings represented by this history
   * entry
   */
  private int urlIndex;

  /**
   * The result model represented by this history entry
   */
  private SoftReference<TableModel> results;

  /**
   * Create a history instance.
   * 
   * @param pSQLIndex
   *          The index in the list of SQL statements
   * @param pURLIndex
   *          The index in the list of connection strings
   * @param pResults
   *          The result model
   */
  public QueryInfo(int pSQLIndex, int pURLIndex, TableModel pResults) {
    setSQLIndex(pSQLIndex);
    setURLIndex(pURLIndex);
    setResults(pResults);
  }

  /**
   * Set the index in the list of connection strings represented by this history
   * instance.
   * 
   * @param pURLIndex
   *          The index value
   */
  public void setURLIndex(int pURLIndex) {
    urlIndex = pURLIndex;
  }

  /**
   * Get the index in the list of connection strings represented by this history
   * instance
   * 
   * @return The index value
   */
  public int getURLIndex() {
    return urlIndex;
  }

  /**
   * Set the index in the list of SQL statements represented by this history
   * instance
   * 
   * @param pSQLIndex
   *          The index value
   */
  public void setSQLIndex(int pSQLIndex) {
    sqlIndex = pSQLIndex;
  }

  /**
   * Get the index in the list of SQL statements represented by this history
   * instance
   * 
   * @return The index value
   */
  public int getSQLIndex() {
    return sqlIndex;
  }

  /**
   * Set the result model represented by this history instance
   * 
   * @param pResults
   *          The result model
   */
  public void setResults(TableModel pResults) {
    results = new SoftReference<TableModel>(pResults);
  }

  /**
   * Set the result model represented by this history instance. This may be null
   * if the reference was released due to a low memory condition.
   * 
   * @return The result model or null if the reference was freed
   */
  public TableModel getResults() {
    return results.get();
  }
}
