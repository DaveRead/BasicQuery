package us.daveread.basicquery.test;

import javax.swing.table.DefaultTableModel;

import junit.framework.*;

import us.daveread.basicquery.QueryHistory;

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
 * @version $Id: QueryHistoryTest.java,v 1.2 2006/05/04 03:38:48 daveread Exp $
 */
public class QueryHistoryTest extends TestCase {
  private QueryHistory hist;

  private static String[][] tableData = { {"Row 0, Col 0", "Row 0, Col 1"},
      {"Row 1, Col 0", "Row 1, Col 1"}, {"Row 2, Col 0", "Row 2, Col 1"},
  };

  private static String[][] tableData2 = { {"R0, C0", "R0, C1"}, {"R1, C0",
      "R1, C1"}, {"R2, C0", "R2, C1"},
  };

  private static String[] columnNames = {
      "Column 0", "Column 1"
  };

  public QueryHistoryTest() {
  }

  public void setUp() {
    hist = new QueryHistory(1, 5, new DefaultTableModel(tableData, columnNames));
  }

  public void testGetURLIndex() {
    assertEquals(5, hist.getURLIndex());
  }

  public void testGetSQLIndex() {
    assertEquals(1, hist.getSQLIndex());
  }

  public void testSetURLIndex() {
    hist.setURLIndex(22);
    assertEquals(22, hist.getURLIndex());
  }

  public void testSetSQLIndex() {
    hist.setSQLIndex(14);
    assertEquals(14, hist.getSQLIndex());
  }

  public void testGetResults() {
    assertEquals(tableData[1][1], (String)hist.getResults().getValueAt(1, 1));
    assertEquals(columnNames[0], hist.getResults().getColumnName(0));
  }

  public void testSetResults() {
    hist.setResults(new DefaultTableModel(tableData2, columnNames));
    assertEquals(tableData2[1][1], (String)hist.getResults().getValueAt(1, 1));
  }
}
