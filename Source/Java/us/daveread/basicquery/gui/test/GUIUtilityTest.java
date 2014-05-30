package us.daveread.basicquery.gui.test;

import java.awt.Window;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Dimension;

import junit.framework.TestCase;

import us.daveread.basicquery.gui.GUIUtility;

/**
 * <p>
 * Title: Test the GUI utility class
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2004-2014
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author David Read
 */
public class GUIUtilityTest extends TestCase {
  /**
   * Parent window width
   */
  private static final int WINDOW_WIDTH = 100;

  /**
   * Parent window height
   */
  private static final int WINDOW_HEIGHT = 50;

  /**
   * Child dialog width
   */
  private static final int INNER_DIALOG_WIDTH = 50;

  /**
   * Child dialog height
   */
  private static final int INNER_DIALOG_HEIGHT = 30;

  /**
   * Window height and width for centering on the display
   */
  private static final int WINDOW_TEST_WIDTH_HEIGHT_FOR_CENTERING_ON_SMALLER_PARENT = 200;
  /**
   * Window height and width for centering on the display
   */
  private static final int WINDOW_TEST_WIDTH_FOR_SCREEN_CENTERING = 300;
  /**
   * Window height and width for centering on the display
   */
  private static final int WINDOW_TEST_HEIGHT_FOR_SCREEN_CENTERING = 100;

  /**
   * Resulting child dialog upper left X position for centering
   */
  private static final int CENTER_X = (WINDOW_WIDTH - INNER_DIALOG_WIDTH) / 2;

  /**
   * Resulting child dialog upper left Y position for centering
   */
  private static final int CENTER_Y = (WINDOW_HEIGHT - INNER_DIALOG_HEIGHT) / 2;

  /**
   * Setup the test case instance
   */
  public GUIUtilityTest() {
  }

  /**
   * Test the center method
   */
  public void testCenter() {
    // Test centering on a parent window
    Window outer;
    Window inner;
    Dimension screenDimension;

    outer = new Window(new Frame());
    outer.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    outer.setLocation(0, 0);
    inner = new Window(new Frame());
    inner.setSize(INNER_DIALOG_WIDTH, INNER_DIALOG_HEIGHT);

    GUIUtility.center(inner, outer);

    assertEquals(CENTER_X, inner.getLocation().x);
    assertEquals(CENTER_Y, inner.getLocation().y);

    // Test centering on the screen
    screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

    inner.setSize(WINDOW_TEST_WIDTH_HEIGHT_FOR_CENTERING_ON_SMALLER_PARENT,
        WINDOW_TEST_WIDTH_HEIGHT_FOR_CENTERING_ON_SMALLER_PARENT);

    GUIUtility.center(inner, outer);

    assertEquals(
        (screenDimension.width - WINDOW_TEST_WIDTH_HEIGHT_FOR_CENTERING_ON_SMALLER_PARENT) / 2,
        inner.getLocation().x);
    assertEquals(
        (screenDimension.height - WINDOW_TEST_WIDTH_HEIGHT_FOR_CENTERING_ON_SMALLER_PARENT) / 2,
        inner.getLocation().y);

    inner.setSize(screenDimension.width + 1, screenDimension.height + 1);

    GUIUtility.center(inner, outer);

    assertEquals(0, inner.getLocation().x);
    assertEquals(0, inner.getLocation().y);

    inner.setSize(WINDOW_TEST_WIDTH_FOR_SCREEN_CENTERING,
        WINDOW_TEST_HEIGHT_FOR_SCREEN_CENTERING);
    GUIUtility.center(inner, null);

    assertEquals(
        (screenDimension.width - WINDOW_TEST_WIDTH_FOR_SCREEN_CENTERING) / 2,
        inner.getLocation().x);
    assertEquals(
        (screenDimension.height - WINDOW_TEST_HEIGHT_FOR_SCREEN_CENTERING) / 2,
        inner.getLocation().y);
  }
}
