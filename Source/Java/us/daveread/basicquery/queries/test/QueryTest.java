package us.daveread.basicquery.queries.test;

import junit.framework.TestCase;
import us.daveread.basicquery.queries.Query;

/**
 * <p>
 * Title: Test the query class
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
public class QueryTest extends TestCase {
  /**
   * Test select statement
   */
  private static final String SELECT_STATEMENT = "select this from that where what";

  /**
   * Test update statement
   */
  private static final String UPDATE_STATEMENT = "insert this into that";

  /**
   * A query in default mode
   */
  private Query queryDefMode;

  /**
   * A query in select mode
   */
  private Query querySelMode;

  /**
   * A query in update mode
   */
  private Query queryUpdMode;

  /**
   * A query in describe mode
   */
  private Query queryDesMode;

  /**
   * Setup the test case instance
   */
  public QueryTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    queryDefMode = new Query(SELECT_STATEMENT);
    querySelMode = new Query(SELECT_STATEMENT + "2", Query.MODE_QUERY);
    queryUpdMode = new Query(UPDATE_STATEMENT, Query.MODE_UPDATE);
    queryDesMode = new Query(SELECT_STATEMENT + "3", Query.MODE_DESCRIBE);
  }

  /**
   * Test getting the query mode
   */
  public void testGetMode() {
    assertEquals(Query.MODE_QUERY, queryDefMode.getMode());
    assertEquals(Query.MODE_QUERY, querySelMode.getMode());
    assertEquals(Query.MODE_DESCRIBE, queryDesMode.getMode());
    assertEquals(Query.MODE_UPDATE, queryUpdMode.getMode());
  }

  /**
   * Test getting the SQL statement
   */
  public void testGetSQL() {
    assertEquals(SELECT_STATEMENT, queryDefMode.getSql());
    assertEquals(SELECT_STATEMENT + "2", querySelMode.getSql());
    assertEquals(UPDATE_STATEMENT, queryUpdMode.getSql());
    assertEquals(SELECT_STATEMENT + "3", queryDesMode.getSql());
  }

  /**
   * Test setting the query mode
   */
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

  /**
   * Test obtaining the string value of the query
   */
  public void testToString() {
    assertEquals(SELECT_STATEMENT, queryDefMode.toString());
    assertEquals(SELECT_STATEMENT + "2", querySelMode.toString());
    assertEquals(UPDATE_STATEMENT, queryUpdMode.toString());
    assertEquals(SELECT_STATEMENT + "3", queryDesMode.toString());
  }

  /**
   * Test comment identification
   */
  public void testIsCommented() {
    final Query commentedQuery = new Query("// " + querySelMode.getRawSql(),
        querySelMode.getMode());

    assertEquals("Should be commented", true, commentedQuery.isCommented());

    assertEquals("Should not be commented", false, querySelMode.isCommented());
  }

  /**
   * Test equality evaluation
   */
  public void testEquals() {
    final Object notQuery = new Object();
    final Object aQuery = querySelMode;
    final Query sameAsSel = new Query(querySelMode.getRawSql(),
        querySelMode.getMode());
    final Query sameAsSelButCommented = new Query("// "
        + querySelMode.getRawSql(),
        querySelMode.getMode());
    final Query diffFromSel = new Query(queryDesMode.getRawSql(),
        queryDesMode.getMode());

    assertTrue("Should be equal", sameAsSel.equals(querySelMode));
    assertTrue("Should be equal even though commented",
        sameAsSelButCommented.equals(querySelMode));
    assertTrue("Should be equal even though commented",
        querySelMode.equals(sameAsSelButCommented));
    assertTrue("Should be equal",
        aQuery.equals(querySelMode));
    assertFalse("Should not be equal", diffFromSel.equals(querySelMode));
    assertFalse("Should not be equal", querySelMode.equals(notQuery));
    assertFalse("Should not be equal", querySelMode.equals((Query) null));

  }

  /**
   * Test hash code calculation
   */
  public void testHashCode() {
    final Query sameAsSel = new Query(querySelMode.getRawSql(),
        querySelMode.getMode());
    final Query sameAsSelButCommented = new Query("// "
        + querySelMode.getRawSql(), querySelMode.getMode());
    final Query diffFromSel = new Query(queryDesMode.getRawSql(),
        queryDesMode.getMode());

    assertEquals("Should have same hash code", querySelMode.hashCode(),
        sameAsSel.hashCode());
    assertEquals("Should have same hash code even though commented",
        querySelMode.hashCode(), sameAsSelButCommented.hashCode());
    assertTrue("Should have different hash code",
        querySelMode.hashCode() != diffFromSel.hashCode());

  }
}
