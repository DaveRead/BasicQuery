package us.daveread.basicquery.gui.test;

import java.awt.Window;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Dimension;

import junit.framework.*;

import us.daveread.basicquery.gui.GUIUtility;

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
public class GUIUtilityTest extends TestCase {
  public GUIUtilityTest() {
  }

  public void testCenter() {
    Window outer;
    Window inner;
    Dimension screenDimension;

    outer = new Window(new Frame());
    outer.setSize(100, 50);
    outer.setLocation(0, 0);

    inner = new Window(new Frame());
    inner.setSize(50, 30);

    GUIUtility.Center(inner, outer);

    assertEquals(25, inner.getLocation().x);
    assertEquals(10, inner.getLocation().y);

    screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

    inner.setSize(200, 200);

    GUIUtility.Center(inner, outer);

    assertEquals((screenDimension.width - 200) / 2, inner.getLocation().x);
    assertEquals((screenDimension.height - 200) / 2, inner.getLocation().y);

    inner.setSize(screenDimension.width + 1, screenDimension.height + 1);

    GUIUtility.Center(inner, outer);

    assertEquals(0, inner.getLocation().x);
    assertEquals(0, inner.getLocation().y);

    inner.setSize(300, 100);
    GUIUtility.Center(inner, null);

    assertEquals((screenDimension.width - 300) / 2, inner.getLocation().x);
    assertEquals((screenDimension.height - 100) / 2, inner.getLocation().y);
  }
}
