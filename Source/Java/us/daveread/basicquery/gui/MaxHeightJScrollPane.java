/**
 * Title:        Basic Query Utility
 * Description:  Execute arbitrary SQL against database accessible with any JDBC-compliant driver.
 * Copyright:    Copyright (c) 2003
 * Company:
 * @author David Read
 * @version $Id: MaxHeightJScrollPane.java,v 1.3 2006/05/14 03:42:04 daveread Exp $
 */

package us.daveread.basicquery.gui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JScrollPane;

/**
 * Is a fixed height version of the JScrollPane where the
 * height can be modified
 */
public class MaxHeightJScrollPane extends JScrollPane {
  public final static String ID =
      "$Id: MaxHeightJScrollPane.java,v 1.3 2006/05/14 03:42:04 daveread Exp $";

  private static final int MIN_HEIGHT = 20;

  private int maxHeight;

  /**
   * Constructs an empty MaxHeightJScrollPane where scrollbars appear
   * when needed
   */

  public MaxHeightJScrollPane() {
  }

  /**
   * Constructs a MaxHeighJScrollPane where the scrollbars will appear
   * only when the dimensions are larger than the view.
   *
   * @param comp The component that will be contained within this
   *             JScrollPane.
   */

  public MaxHeightJScrollPane(Component comp) {
    super(comp);
  }

  /**
   * Constructs a MaxJScrollpane whose view position can be controlled
   * by a pair of  scrollbars.The scrollbar policies define when the
   * scrollbars are needed
   *
   * @param comp The component that appears in the scrollpanes viewport
   * @param vsbpolicy The integer that defines the vertical scrollbar policy
   * @param hsbpolicy The integer that defines the horizontal scrollbar
   *                  policy
   *
   * @see #VerticalScrollBarPolicy(int)
   * @see #HorizontalScrollBarPolicy(int)
   */

  public MaxHeightJScrollPane(Component comp, int vsbPolicy, int hsbPolicy) {
    super(comp, vsbPolicy, hsbPolicy);
  }

  /**
   * Constructs an empty MaxJScrollPane with the specified vertical
   * and horizontal scrollbar policies

   * @param vsbpolicy The integer that defines the vertical scrollbar policy
   * @param hsbpolicy The integer that defines the horizontal scrollbar policy
   */

  public MaxHeightJScrollPane(int vsbPolicy, int hsbPolicy) {
    super(vsbPolicy, hsbPolicy);
  }

  /**
   * Sets the maximum height of this JScrollPane to the current height
   */

  public void lockHeight() {
    maxHeight = super.getPreferredSize().height;
  }

  public void lockHeight(int height) {
    if (height < MIN_HEIGHT) {
      height = MIN_HEIGHT;
    }

    maxHeight = height;
  }

  /**
   * Sets the maximum height of this JScrollpane

   * @param height Value of the maximum height of this JScrollPane
   */

  public void setHeight(int height) {
    maxHeight = height;
  }

  /**
   * Gets the width and height of the dimension object
   *
   * @return dimension A dimension object with the maximum height
   */


  public Dimension getPreferredSize() {
    if (maxHeight != 0.0) {
      return new Dimension(super.getPreferredSize().width, maxHeight);
    } else {
      return super.getPreferredSize();
    }
  }

  /**
   * Returns the maximum size of this component
   *
   * @return Dimension
   */

  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

  /**
   * Returns the minimum size of this component
   *
   * @return Dimension
   */

  public Dimension getMinimumSize() {
    return getPreferredSize();
  }
}
