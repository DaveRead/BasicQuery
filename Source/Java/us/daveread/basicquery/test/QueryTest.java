package us.daveread.basicquery.test;

import junit.framework.*;

import us.daveread.basicquery.Query;

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
 * @version $Id: QueryTest.java,v 1.1 2006/05/01 20:19:31 daveread Exp $
 */
public class QueryTest extends TestCase {
  private Query queryDefMode;
  private Query querySelMode;
  private Query queryUpdMode;
  private Query queryDesMode;

  private static final String select = "select this from that where what";
  private static final String update = "insert this into that";

  public QueryTest() {
  }

  public void setUp() {
    queryDefMode = new Query(select);
    querySelMode = new Query(select + "2", Query.MODE_QUERY);
    queryUpdMode = new Query(update, Query.MODE_UPDATE);
    queryDesMode = new Query(select + "3", Query.MODE_DESCRIBE);
  }

  public void testGetMode() {
    assertEquals(Query.MODE_QUERY, queryDefMode.getMode());
    assertEquals(Query.MODE_QUERY, querySelMode.getMode());
    assertEquals(Query.MODE_DESCRIBE, queryDesMode.getMode());
    assertEquals(Query.MODE_UPDATE, queryUpdMode.getMode());
  }

  public void testGetSQL() {
    assertEquals(select, queryDefMode.getSQL());
    assertEquals(select + "2", querySelMode.getSQL());
    assertEquals(update, queryUpdMode.getSQL());
    assertEquals(select + "3", queryDesMode.getSQL());
  }

  public void testSetMode() {
    queryDefMode.setMode(Query.MODE_DESCRIBE);
    querySelMode.setMode(Query.MODE_UPDATE);
    queryUpdMode.setMode(Query.MODE_QUERY);
    queryDesMode.setMode(Query.MODE_QUERY);

    assertEquals(Query.MODE_DESCRIBE, queryDefMode.getMode());
    assertEquals(Query.MODE_UPDATE, querySelMode.getMode());
    assertEquals(Query.MODE_QUERY, queryUpdMode.getMode());
    assertEquals(Query.MODE_QUERY, queryDesMode.getMode());
  }

  public void testToString() {
    assertEquals(select, queryDefMode.toString());
    assertEquals(select + "2", querySelMode.toString());
    assertEquals(update, queryUpdMode.toString());
    assertEquals(select + "3", queryDesMode.toString());
  }
}
