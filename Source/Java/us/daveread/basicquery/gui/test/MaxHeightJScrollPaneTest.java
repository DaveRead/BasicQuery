package us.daveread.basicquery.gui.test;

import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

import junit.framework.*;

import us.daveread.basicquery.gui.MaxHeightJScrollPane;

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
 * @version $Id: MaxHeightJScrollPaneTest.java,v 1.2 2006/05/04 03:41:09 daveread Exp $
 */
public class MaxHeightJScrollPaneTest extends TestCase {
  private MaxHeightJScrollPane scrollPaneVoid;
  private MaxHeightJScrollPane scrollPanePolicy;
  private MaxHeightJScrollPane scrollPaneCompOnly;
  private MaxHeightJScrollPane scrollPaneCompAndPolicy;

  private JPanel jp1, jp2;

  public MaxHeightJScrollPaneTest() {
  }

  public void setUp() {
    scrollPaneVoid = new MaxHeightJScrollPane();
    scrollPanePolicy = new MaxHeightJScrollPane(JScrollPane.
        VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    jp1 = new JPanel();
    jp1.setPreferredSize(new Dimension(200, 1000));
    scrollPaneCompOnly = new MaxHeightJScrollPane(jp1);

    jp2 = new JPanel();
    jp2.setPreferredSize(new Dimension(100, 1500));
    scrollPaneCompAndPolicy = new MaxHeightJScrollPane(jp2,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
  }

  public void testLockHeight() {
    scrollPaneCompOnly.lockHeight();

    assertTrue(jp1.getPreferredSize().height <=
        scrollPaneCompOnly.getPreferredSize().height);
  }

  public void testGetPreferredSize() {
    scrollPaneCompAndPolicy.lockHeight();

    assertTrue(jp2.getPreferredSize().height <=
        scrollPaneCompAndPolicy.getPreferredSize().height);
  }

  public void testGetMaximumSize() {
    scrollPaneCompAndPolicy.lockHeight();

    assertTrue(jp2.getPreferredSize().height <=
        scrollPaneCompAndPolicy.getMaximumSize().height);
  }

  public void testGetMinimumSize() {
    scrollPaneCompAndPolicy.lockHeight();

    assertTrue(jp1.getPreferredSize().height <=
        scrollPaneCompOnly.getMinimumSize().height);
  }

  public void testSetHeight() {
    scrollPaneCompOnly.setHeight(500);

    assertEquals(500, scrollPaneCompOnly.getPreferredSize().height);
    assertEquals(500, scrollPaneCompOnly.getMaximumSize().height);
    assertEquals(500, scrollPaneCompOnly.getMinimumSize().height);
  }
}
