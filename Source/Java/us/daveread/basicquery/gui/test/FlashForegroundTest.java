package us.daveread.basicquery.gui.test;

import javax.swing.JLabel;

import org.apache.log4j.Logger;

import java.awt.Color;

import junit.framework.TestCase;

import us.daveread.basicquery.gui.FlashForeground;

/**
 * <p>Title: Test FlashForeground class</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004-2014</p>
 *
 * <p>Company: </p>
 *
 * @author David Read
 */
public class FlashForegroundTest extends TestCase {
  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(FlashForegroundTest.class);
  
  /**
   * Time in milliseconds to allow flasher to run when testing its operation
   */
  private static final int TEST_DELAY_TO_RUN_TO_FLASHER = 3000;

  /**
   * The flash foreground instance 
   */
  private FlashForeground ff;

  /**
   * Create the test case instance
   */
  public FlashForegroundTest() {
  }

  /**
   * Set the test
   */
  public void setUp() {
    ff = new FlashForeground(new JLabel(), Color.gray, Color.lightGray, 10);
  }

  /**
   * Test the constructor
   */
  public void testConstructor() {
    assertNotNull(ff);
  }

  /**
   * Test the change computation
   */
  public void testComputeChange() {
    // Compute change is private - test it with various color ranges
    new FlashForeground(new JLabel(), new Color(50, 50, 0),
        new Color(100, 100, 100), 10);

    new FlashForeground(new JLabel(), new Color(50, 0, 50),
        new Color(100, 100, 100), 10);

    new FlashForeground(new JLabel(), new Color(50, 0, 50),
        new Color(50, 0, 50), 10);
  }

  /**
   * Test the running operation
   */
  public void testRun() {
    Thread runIt;

    runIt = new Thread(ff);
    runIt.start();
    assertTrue(runIt.isAlive());

    try {
      Thread.sleep(TEST_DELAY_TO_RUN_TO_FLASHER);
    }
    catch (Throwable any) {
      LOGGER.warn("Thread sleep error during test execution", any);
    }

    runIt.interrupt();

    assertTrue(runIt.isInterrupted() || !runIt.isAlive());
  }
}
