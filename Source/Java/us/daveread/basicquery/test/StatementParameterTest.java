package us.daveread.basicquery.test;

import junit.framework.*;

import us.daveread.basicquery.StatementParameter;

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
 * @version $Id: StatementParameterTest.java,v 1.2 2006/05/04 03:38:48 daveread Exp $
 */
public class StatementParameterTest extends TestCase {
  private StatementParameter outParam;
  private StatementParameter inParam;

  private static int outParamType = 1;
  private static int inParamType = 4;
  private static String inParamValue = "invalue";

  public StatementParameterTest() {
  }

  public void setUp() {
    outParam = new StatementParameter(StatementParameter.OUT, outParamType);
    inParam = new StatementParameter(StatementParameter.IN, inParamType,
        inParamValue);
  }

  public void testGetDataString() {
    assertNull(outParam.getDataString());
    assertEquals(inParamValue, inParam.getDataString());
  }

  public void testGetDataType() {
    assertEquals(outParamType, outParam.getDataType());
    assertEquals(inParamType, inParam.getDataType());
  }

  public void testGetType() {
    assertEquals(StatementParameter.OUT, outParam.getType());
    assertEquals(StatementParameter.IN, inParam.getType());
  }
}
