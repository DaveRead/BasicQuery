package us.daveread.basicquery.gui.test;

import javax.swing.JLabel;

import org.apache.log4j.Logger;

import java.util.Date;

import junit.framework.TestCase;

import us.daveread.basicquery.gui.InsertTime;

/**
 * <p>
 * Title: Test the insert time class
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
public class InsertTimeTest extends TestCase {
  /**
   * Logger
   */
  private static final Logger LOGGER = Logger
      .getLogger(InsertTimeTest.class);

  /**
   * Delay time between refreshes of the label
   */
  private static final int DELAY_TIME_BETWEEN_REFRESHES_MS = 500;

  /**
   * The insert time instance being tested
   */
  private InsertTime timeDisp;

  /**
   * The label being updated
   */
  private JLabel jLabel;

  /**
   * Setup the test case instance
   */
  public InsertTimeTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    jLabel = new JLabel();
    timeDisp = new InsertTime(jLabel, new Date().getTime(),
        DELAY_TIME_BETWEEN_REFRESHES_MS);
  }

  /**
   * Test the run method
   */
  public void testRun() {
    Thread run;

    run = new Thread(timeDisp);
    run.start();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException intExc) {
      LOGGER.error("Error while delaying during test run", intExc);
    }

    assertTrue(jLabel.getText().trim().length() > 0);

    run.interrupt();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException intExc) {
      LOGGER.error("Error while delaying during test run", intExc);
    }

    assertTrue(jLabel.getText().trim().length() == 0);
  }
}
