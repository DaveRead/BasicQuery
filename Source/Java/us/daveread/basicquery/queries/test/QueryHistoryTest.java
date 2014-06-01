package us.daveread.basicquery.queries.test;

import javax.swing.table.DefaultTableModel;

import junit.framework.TestCase;

import us.daveread.basicquery.queries.QueryHistory;
import us.daveread.basicquery.queries.QueryInfo;

/**
 * <p>
 * Title: Test the query history class
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
public class QueryHistoryTest extends TestCase {

  /**
   * Setup the test case instance
   */
  public QueryHistoryTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    QueryHistory.getInstance().clearAllQueries();
  }

  public void testAddQuery() {
    QueryInfo queryInfo = new QueryInfo(0, 0, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);
    assertEquals("Incorrect number of history queries", 1, QueryHistory
        .getInstance().getNumberOfQueries());
  }

  public void testDeleteQueryAtIndex() {
    QueryInfo queryInfo = new QueryInfo(0, 0, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(2, 2, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(1, 1, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    QueryHistory.getInstance().deleteQueryAtIndex(1);

    assertEquals("Incorrect number of remaining history queries", 2,
        QueryHistory.getInstance().getNumberOfQueries());
    assertEquals("Incorrect SQL statement index", 1, QueryHistory.getInstance()
        .getCurrentQueryInfo().getSQLIndex());
  }

  public void testGetNumberOfQueries() {
    QueryInfo queryInfo = new QueryInfo(0, 0, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(2, 2, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    assertEquals("Incorrect number of history queries", 2, QueryHistory
        .getInstance().getNumberOfQueries());
  }

  public void testGetCurrentQueryInfo() {
    QueryInfo queryInfo = new QueryInfo(0, 0, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(2, 2, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(1, 1, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    assertEquals("Incorrect current query info instance", 1, QueryHistory
        .getInstance().getCurrentQueryInfo().getSQLIndex());
  }

  public void testMoveBackward() {
    QueryInfo queryInfo = new QueryInfo(0, 0, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(2, 2, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(1, 1, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    QueryHistory.getInstance().moveBackward();

    assertEquals("Incorrect current query info instance", 2, QueryHistory
        .getInstance().getCurrentQueryInfo().getSQLIndex());

  }

  public void testMoveForward() {
    QueryInfo queryInfo = new QueryInfo(0, 0, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(2, 2, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(1, 1, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    QueryHistory.getInstance().moveBackward();
    QueryHistory.getInstance().moveBackward();
    QueryHistory.getInstance().moveForward();

    assertEquals("Incorrect current query info instance", 2, QueryHistory
        .getInstance().getCurrentQueryInfo().getSQLIndex());
  }

  public void testHasPrevious() {
    QueryInfo queryInfo = new QueryInfo(0, 0, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(2, 2, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(1, 1, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    assertEquals("Incorrect has previous result, should have a previous entry",
        true, QueryHistory.getInstance().hasPrevious());

    QueryHistory.getInstance().moveBackward();
    QueryHistory.getInstance().moveBackward();

    assertEquals(
        "Incorrect has previous result, should not have a previous entry",
        false, QueryHistory.getInstance().hasPrevious());
  }

  public void testHasNext() {
    QueryInfo queryInfo = new QueryInfo(0, 0, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(2, 2, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(1, 1, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    assertEquals("Incorrect has next result, should not have a next entry",
        false, QueryHistory.getInstance().hasNext());

    QueryHistory.getInstance().moveBackward();

    assertEquals("Incorrect has next result, should have a next entry", true,
        QueryHistory.getInstance().hasNext());
  }

  public void testClearAllQueries() {
    QueryInfo queryInfo = new QueryInfo(0, 0, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(2, 2, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(1, 1, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    QueryHistory.getInstance().clearAllQueries();

    assertEquals("Failed to remove all queries properly", 0, QueryHistory
        .getInstance().getNumberOfQueries());
  }

  public void testTruncation() {
    QueryInfo queryInfo = new QueryInfo(0, 0, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(2, 2, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(1, 1, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    QueryHistory.getInstance().moveBackward();

    queryInfo = new QueryInfo(4, 4, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    QueryHistory.getInstance().moveBackward();

    assertEquals("Incorrect previous result", 2, QueryHistory.getInstance()
        .getCurrentQueryInfo().getSQLIndex());

    QueryHistory.getInstance().moveForward();

    assertEquals("Incorrect next result", 4, QueryHistory.getInstance()
        .getCurrentQueryInfo().getSQLIndex());
  }

  public void testInsertSameQuery() {
    QueryInfo queryInfo = new QueryInfo(0, 0, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(2, 2, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    queryInfo = new QueryInfo(2, 3, new DefaultTableModel());
    QueryHistory.getInstance().addQuery(queryInfo);

    assertEquals("Incorrect number of queries", 2, QueryHistory.getInstance()
        .getNumberOfQueries());
    assertEquals("Incorrect connection index", 3, QueryHistory.getInstance()
        .getCurrentQueryInfo().getURLIndex());
  }

  public void testEnforceSize() {
    QueryInfo queryInfo = null;
    
    for (int index = 0; index < 1000 + 2; ++index) {
      queryInfo = new QueryInfo(index, 0, new DefaultTableModel());
      QueryHistory.getInstance().addQuery(queryInfo);
    }

    assertEquals("Incorrect number of queries", 1000, QueryHistory
        .getInstance().getNumberOfQueries());

    while (QueryHistory.getInstance().hasPrevious()) {
      QueryHistory.getInstance().moveBackward();
    }    
  }
  
  public void testExceptions() {
    try {
      QueryHistory.getInstance().getCurrentQueryInfo();
      fail("Should have thrown an IllegalAccessError");
    }
    catch (IllegalAccessError iae) {
      assertTrue("Incorrect message", iae.getMessage().startsWith("No queries"));
    }
    
    try {
      QueryHistory.getInstance().moveBackward();
      fail("Should have thrown an IllegalAccessError");
    }
    catch (IllegalAccessError iae) {
      assertTrue("Incorrect message", iae.getMessage().startsWith("No previous"));
    }
    
    try {
      QueryHistory.getInstance().moveForward();
      fail("Should have thrown an IllegalAccessError");
    }
    catch (IllegalAccessError iae) {
      assertTrue("Incorrect message", iae.getMessage().startsWith("No more"));
    }
  }
}
