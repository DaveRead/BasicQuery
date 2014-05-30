package us.daveread.basicquery.test;

import java.util.List;
import java.util.Arrays;

import junit.framework.TestCase;

import us.daveread.basicquery.SQLTypes;

/**
 * <p>Title: Test the SQL types class</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006-2014</p>
 *
 * <p>Company: </p>
 *
 * @author David Read
 */
public class SQLTypesTest extends TestCase {
  /**
   * Setup the test case instance
   */
  public SQLTypesTest() {
  }

  /**
   * Test getting the SQL type ids
   */
  public void testGetSQLTypeId() {
    assertEquals(java.sql.Types.BOOLEAN, SQLTypes.getSQLTypeId("Boolean"));
    assertEquals(java.sql.Types.CHAR, SQLTypes.getSQLTypeId("Char"));
    assertEquals(java.sql.Types.DATE, SQLTypes.getSQLTypeId("Date"));
    assertEquals(java.sql.Types.DECIMAL, SQLTypes.getSQLTypeId("Decimal"));
    assertEquals(java.sql.Types.DOUBLE, SQLTypes.getSQLTypeId("Double"));
    assertEquals(java.sql.Types.FLOAT, SQLTypes.getSQLTypeId("Float"));
    assertEquals(java.sql.Types.INTEGER, SQLTypes.getSQLTypeId("Integer"));
    assertEquals(java.sql.Types.NULL, SQLTypes.getSQLTypeId("Null"));
    assertEquals(java.sql.Types.DATE, SQLTypes.getSQLTypeId("Date"));
    assertEquals(java.sql.Types.VARCHAR, SQLTypes.getSQLTypeId("String"));

    assertEquals(java.sql.Types.OTHER, SQLTypes.getSQLTypeId("Blob"));
    assertEquals(java.sql.Types.OTHER, SQLTypes.getSQLTypeId("UnknownType"));

  }

  /**
   * Test getting the SQL types names
   */
  public void testGetKnownTypeNames() {
    final String[] types = SQLTypes.getKnownTypeNames();
    final List<String> typesList = Arrays.asList(types);

    assertTrue(typesList.contains("BOOLEAN"));
    assertTrue(typesList.contains("CHAR"));
    assertTrue(typesList.contains("STRING"));
  }
}
