package us.daveread.basicquery.gui.test;

import javax.swing.JLabel;
import java.awt.Color;

import junit.framework.*;

import us.daveread.basicquery.gui.FlashForeground;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class FlashForegroundTest extends TestCase {

  private FlashForeground ff;

  public FlashForegroundTest() {
  }

  public void setUp() {
    ff = new FlashForeground(new JLabel(), Color.gray, Color.lightGray, 10);
  }

  public void testConstructor() {
    assertNotNull(ff);
  }

  public void testComputeChange() {
    // Compute change is private - test it with various color ranges
    new FlashForeground(new JLabel(), new Color(50, 50, 0),
        new Color(100, 100, 100), 10);

    new FlashForeground(new JLabel(), new Color(50, 0, 50),
        new Color(100, 100, 100), 10);

    new FlashForeground(new JLabel(), new Color(50, 0, 50),
        new Color(50, 0, 50), 10);
  }

  public void testRun() {
    Thread runIt;

    runIt = new Thread(ff);
    runIt.start();
    assertTrue(runIt.isAlive());

    try {
      Thread.sleep(3000);
    }
    catch (Throwable any) {

    }

    runIt.interrupt();

    assertTrue(runIt.isInterrupted() || !runIt.isAlive());
  }
}
