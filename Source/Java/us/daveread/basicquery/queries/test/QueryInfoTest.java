package us.daveread.basicquery.queries.test;

import javax.swing.table.DefaultTableModel;

import junit.framework.TestCase;

import us.daveread.basicquery.queries.QueryInfo;

/**
 * <p>
 * Title: Test the query info class
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
public class QueryInfoTest extends TestCase {
  /**
   * Test index for the URL
   */
  private static final int URL_INDEX = 22;
  
  /**
   * Test index for the SQL statement
   */
  private static final int SQL_STATEMENT_INDEX = 14;
  
  /**
   * Test data for result set 1
   */
  private static final String[][] TEST_TABLE_DATA = {
      {
          "Row 0, Col 0", "Row 0, Col 1"
      },
      {
          "Row 1, Col 0", "Row 1, Col 1"
      },
      {
          "Row 2, Col 0", "Row 2, Col 1"
      }
  };

  /**
   * Test data for result set 2
   */
  private static final String[][] TEST_TABLE_DATA_2 = {
      {
          "R0, C0", "R0, C1"
      },
      {
          "R1, C0", "R1, C1"
      },
      {
          "R2, C0", "R2, C1"
      }
  };

  /**
   * Column names for the test data
   */
  private static final String[] TEST_COLUMN_NAMES = {
      "Column 0", "Column 1"
  };

  /**
   * The query info instance to test
   */
  private QueryInfo hist;
  
  /**
   * Setup the test case instance
   */
  public QueryInfoTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    hist = new QueryInfo(1, 5, new DefaultTableModel(TEST_TABLE_DATA, TEST_COLUMN_NAMES));
  }

  /**
   * Test getting the index for the connection URL
   */
  public void testGetURLIndex() {
    assertEquals(5, hist.getURLIndex());
  }

  /**
   * Test getting the index for the SQL statement
   */
  public void testGetSQLIndex() {
    assertEquals(1, hist.getSQLIndex());
  }

  /**
   * Set the index for the connection URL
   */
  public void testSetURLIndex() {
    hist.setURLIndex(URL_INDEX);
    assertEquals(URL_INDEX, hist.getURLIndex());
  }

  /**
   * Set the index for the SQL statement
   */
  public void testSetSQLIndex() {
    hist.setSQLIndex(SQL_STATEMENT_INDEX);
    assertEquals(SQL_STATEMENT_INDEX, hist.getSQLIndex());
  }

  /**
   * Test retrieving the cached results
   */
  public void testGetResults() {
    assertEquals(TEST_TABLE_DATA[1][1], (String) hist.getResults().getValueAt(1, 1));
    assertEquals(TEST_COLUMN_NAMES[0], hist.getResults().getColumnName(0));
  }

  /**
   * Set the results
   */
  public void testSetResults() {
    hist.setResults(new DefaultTableModel(TEST_TABLE_DATA_2, TEST_COLUMN_NAMES));
    assertEquals(TEST_TABLE_DATA_2[1][1], (String) hist.getResults().getValueAt(1, 1));
  }
}
