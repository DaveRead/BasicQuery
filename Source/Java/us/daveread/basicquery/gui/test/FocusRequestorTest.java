package us.daveread.basicquery.gui.test;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import us.daveread.basicquery.gui.FocusRequestor;

/**
 * <p>
 * Title: Test focus requestor
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
public class FocusRequestorTest extends TestCase {
  /**
   * Logger
   */
  private static final Logger LOGGER = Logger
      .getLogger(FocusRequestorTest.class);

  /**
   * Time in milliseconds to allow flasher to run when testing its operation
   */
  private static final int TEST_DELAY = 1000;

  /**
   * The backing component for the test
   */
  private FocusRequestorTestComponent comp;

  /**
   * Setup the test case instance
   */
  public FocusRequestorTest() {
  }

  /**
   * Setup the component for the test
   */
  public void setUp() {
    comp = new FocusRequestorTestComponent();
  }

  /**
   * Test the requestor
   */
  public void testFocusRequestor() {
    new FocusRequestor(comp);

    try {
      Thread.sleep(TEST_DELAY);
    } catch (InterruptedException intExc) {
      LOGGER.error("Error while delaying during test run", intExc);
    }

    assertTrue(comp.isFocusRequested());
  }
}
