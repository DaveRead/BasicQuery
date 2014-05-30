package us.daveread.basicquery.util.test;

import junit.framework.TestCase;
import us.daveread.basicquery.util.Resources;

/**
 * <p>
 * Title: Test the Resources class
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
public class ResourcesTest extends TestCase {
  /**
   * A key for a label
   */
  private static final String TEST_KEY_STRING = "mnuFileLabel";

  /**
   * A key for an accelerator character
   */
  private static final String TEST_KEY_CHAR = "mnuFileAccel";

  /**
   * A key with a one-parameter value
   */
  private static final String TEST_ONE_PARAM = "proQueryStatsExportHeaderParam";

  /**
   * A key with a two-parameter value
   */
  private static final String TEST_TWO_PARAM = "msgQueryTypeSuspicious";

  /**
   * A key with a three-parameter value
   */
  private static final String TEST_THREE_PARAM = "errDBServerMetaError";

  /**
   * A key with a four-parameter value
   */
  private static final String TEST_FOUR_PARAM = "dlgRunAllQueriesProgressNote";

  /**
   * A key with a five-parameter value
   */
  private static final String TEST_FIVE_PARAM = "dlgRunAllQueriesProgressNoteWithRemainTime";

  /**
   * A key with a six-parameter value
   */

  private static final String TEST_SIX_PARAM = "dlgRunAllQueriesProgressNoteWithRemainTime";

  /**
   * A key with a seven-parameter value
   */
  private static final String TEST_SEVEN_PARAM =
      "dlgRunAllQueriesProgressNoteWithRemainTime";

  /**
   * Test param value 1
   */
  private static final String TEST_VALUE_ONE = "One";

  /**
   * Test param value 2
   */
  private static final String TEST_VALUE_TWO = "Two";

  /**
   * Test param value 3
   */
  private static final String TEST_VALUE_THREE = "Three";

  /**
   * Test param value 4
   */
  private static final String TEST_VALUE_FOUR = "Four";

  /**
   * Test param value 5
   */
  private static final String TEST_VALUE_FIVE = "Five";

  /**
   * Test param value 6
   */
  private static final String TEST_VALUE_SIX = "Six";

  /**
   * Test param value 7
   */
  private static final String TEST_VALUE_SEVEN = "Seven";

  /**
   * Setup the test case instance
   */
  public ResourcesTest() {
  }

  /**
   * Test getting a string value
   */
  public void testGetString() {
    String[] args;

    assertNotNull(Resources.getString(TEST_KEY_STRING));
    assertEquals('F', Resources.getChar(TEST_KEY_CHAR));
    assertTrue(Resources.getString(TEST_ONE_PARAM, TEST_VALUE_ONE).indexOf(
        TEST_VALUE_ONE) > -1);
    assertTrue(Resources.getString(TEST_TWO_PARAM, TEST_VALUE_ONE,
        TEST_VALUE_TWO).indexOf(TEST_VALUE_TWO) > -1);
    assertTrue(Resources.getString(TEST_THREE_PARAM, TEST_VALUE_ONE,
        TEST_VALUE_TWO, TEST_VALUE_THREE).indexOf(TEST_VALUE_THREE) > -1);
    assertTrue(Resources.getString(TEST_FOUR_PARAM, TEST_VALUE_ONE,
        TEST_VALUE_TWO, TEST_VALUE_THREE, TEST_VALUE_FOUR).indexOf(
        TEST_VALUE_FOUR) > -1);
    assertTrue(Resources.getString(TEST_FIVE_PARAM, TEST_VALUE_ONE,
        TEST_VALUE_TWO, TEST_VALUE_THREE, TEST_VALUE_FOUR, TEST_VALUE_FIVE)
        .indexOf(TEST_VALUE_FIVE) > -1);
    assertTrue(Resources.getString(TEST_SIX_PARAM, TEST_VALUE_ONE,
        TEST_VALUE_TWO, TEST_VALUE_THREE, TEST_VALUE_FOUR, TEST_VALUE_FIVE,
        TEST_VALUE_SIX).indexOf(TEST_VALUE_SIX) > -1);
    assertTrue(Resources.getString(TEST_SEVEN_PARAM, TEST_VALUE_ONE,
        TEST_VALUE_TWO, TEST_VALUE_THREE, TEST_VALUE_FOUR, TEST_VALUE_FIVE,
        TEST_VALUE_SIX, TEST_VALUE_SEVEN).indexOf(TEST_VALUE_SEVEN) > -1);

    args = new String[3];
    args[0] = TEST_VALUE_ONE;
    args[1] = TEST_VALUE_TWO;
    args[2] = TEST_VALUE_THREE;

    assertTrue(Resources.getString(TEST_THREE_PARAM, args).indexOf(
        TEST_VALUE_THREE) > -1);
  }
}
