package us.daveread.basicquery.util.test;

import junit.framework.*;

import us.daveread.basicquery.util.Resources;

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
 * @version $Id: ResourcesTest.java,v 1.2 2006/05/04 03:34:07 daveread Exp $
 */
public class ResourcesTest extends TestCase {
  private final static String testKeyString = "mnuFileLabel";
  private final static String testKeyChar = "mnuFileAccel";
  private final static String testOneParam = "proQueryStatsExportHeaderParam";
  private final static String testTwoParam = "msgQueryTypeSuspicious";
  private final static String testThreeParam = "errDBServerMetaError";
  private final static String testFourParam = "dlgRunAllQueriesProgressNote";
  private final static String testFiveParam =
      "dlgRunAllQueriesProgressNoteWithRemainTime";
  private final static String testSixParam =
      "dlgRunAllQueriesProgressNoteWithRemainTime";
  private final static String testSevenParam =
      "dlgRunAllQueriesProgressNoteWithRemainTime";

  public ResourcesTest() {
  }

  public void testGetString() {
    String[] args;

    assertNotNull(Resources.getString(testKeyString));
    assertEquals('F', Resources.getChar(testKeyChar));
    assertTrue(Resources.getString(testOneParam, "One").indexOf("One") > -1);
    assertTrue(Resources.getString(testTwoParam, "One",
        "Two").indexOf("Two") > -1);
    assertTrue(Resources.getString(testThreeParam, "One", "Two",
        "Three").indexOf("Three") > -1);
    assertTrue(Resources.getString(testFourParam, "One", "Two", "Three",
        "Four").indexOf("Four") > -1);
    assertTrue(Resources.getString(testFiveParam, "One", "Two", "Three", "Four",
        "Five").indexOf("Five") > -1);
    assertTrue(Resources.getString(testSixParam, "One", "Two", "Three", "Four",
        "Five", "Six").indexOf("Six") > -1);
    assertTrue(Resources.getString(testSevenParam, "One", "Two", "Three",
        "Four", "Five", "Six", "Seven").indexOf("Seven") > -1);

    args = new String[3];
    args[0] = "One";
    args[1] = "Two";
    args[2] = "Three";

    assertTrue(Resources.getString(testThreeParam, args).indexOf("Three") > -1);
  }
}
