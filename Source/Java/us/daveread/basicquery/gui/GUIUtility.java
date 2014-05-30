package us.daveread.basicquery.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;


/**
 * <p>Title: GUI utilities</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004-2014</p>
 * <p>Company: </p>
 * 
 * @author David Read
 */

public class GUIUtility {
  /**
   * Don't want instances of this class created.
   */
  private GUIUtility() {
  }

  /**
   * Centers a window on the parent window or the screen.  If the parent
   * is null, or smaller than the child, the child is centered on the
   * screen.
   * @param winaChild The window being centered.
   * @param winaParent The parent window, or null if no parent frame exists.
   */
  public static void center(Window winaChild, Window winaParent) {
    Dimension dimlParentSize, dimlMySize;
    Point ptlUpperLeft;
    int ilX, ilY;
    dimlMySize = winaChild.getSize();
    if (winaParent == null) {
      dimlParentSize = Toolkit.getDefaultToolkit().getScreenSize();
      ptlUpperLeft = new Point(0, 0);
    } else {
      dimlParentSize = winaParent.getSize();
      ptlUpperLeft = winaParent.getLocation();

      if (dimlMySize.width > dimlParentSize.width 
          || dimlMySize.height > dimlParentSize.height) {
        dimlParentSize = Toolkit.getDefaultToolkit().getScreenSize();
        ptlUpperLeft = new Point(0, 0);
      }
    }
    if (dimlParentSize.width >= dimlMySize.width) {
      ilX = (dimlParentSize.width - dimlMySize.width) / 2;
    } else {
      ilX = 0;
    }
    if (dimlParentSize.height >= dimlMySize.height) {
      ilY = (dimlParentSize.height - dimlMySize.height) / 2;
    } else {
      ilY = 0;
    }
    ilX += ptlUpperLeft.x;
    ilY += ptlUpperLeft.y;
    winaChild.setLocation(ilX, ilY);
  }
}
