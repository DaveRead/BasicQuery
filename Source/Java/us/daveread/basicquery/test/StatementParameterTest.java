package us.daveread.basicquery.test;

import junit.framework.TestCase;
import us.daveread.basicquery.StatementParameter;

/**
 * <p>Title: Test the statement parameter class</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006-2014</p>
 *
 * <p>Company: </p>
 *
 * @author David Read
 */
public class StatementParameterTest extends TestCase {
  /**
   * A test out parameter
   */
  private StatementParameter outParam;
  
  /**
   * A test in parameter
   */
  private StatementParameter inParam;

  /**
   * The type for the out parameter
   */
  private static int outParamType = 1;
  
  /**
   * The type for the in parameter
   */
  private static int inParamType = 4;
  
  /**
   * The value for the in parameter
   */
  private static String inParamValue = "invalue";

  /**
   * Setup the test case instance
   */
  public StatementParameterTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    outParam = new StatementParameter(StatementParameter.OUT, outParamType);
    inParam = new StatementParameter(StatementParameter.IN, inParamType,
        inParamValue);
  }

  /**
   * Test getting the parameter values
   */
  public void testGetDataString() {
    assertNull(outParam.getDataString());
    assertEquals(inParamValue, inParam.getDataString());
  }

  /**
   * Test getting parameter data types
   */
  public void testGetDataType() {
    assertEquals(outParamType, outParam.getDataType());
    assertEquals(inParamType, inParam.getDataType());
  }

  /**
   * Test getting the parameter type (in/out)
   */
  public void testGetType() {
    assertEquals(StatementParameter.OUT, outParam.getType());
    assertEquals(StatementParameter.IN, inParam.getType());
  }
}
