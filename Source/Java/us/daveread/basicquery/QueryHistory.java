package us.daveread.basicquery;

import java.lang.ref.SoftReference;
import javax.swing.table.TableModel;

/**
 * Title:        QueryHistory
 * <p>Description:  Represents an individual SQL statement index and associated URL.
 *     This is used in a collection to retain a history list of executed queries.
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
 * @version $Id: QueryHistory.java,v 1.2 2006/04/20 03:09:07 daveread Exp $
 */

public class QueryHistory {
  private int SQLIndex, URLIndex;
  private SoftReference results;

  public QueryHistory(int pSQLIndex, int pURLIndex, TableModel results) {
    setSQLIndex(pSQLIndex);
    setURLIndex(pURLIndex);
    setResults(results);
  }

  public void setURLIndex(int pURLIndex) {
    URLIndex = pURLIndex;
  }

  public int getURLIndex() {
    return URLIndex;
  }

  public void setSQLIndex(int pSQLIndex) {
    SQLIndex = pSQLIndex;
  }

  public int getSQLIndex() {
    return SQLIndex;
  }

  public void setResults(TableModel pResults) {
    results = new SoftReference(pResults);
  }

  public TableModel getResults() {
    return (TableModel)results.get();
  }
}
