package us.daveread.basicquery.gui.test;

import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

import junit.framework.TestCase;

import us.daveread.basicquery.gui.MaxHeightJScrollPane;

/**
 * <p>Title: Test the max height scroll pane class</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006-2014</p>
 *
 * <p>Company: </p>
 *
 * @author David Read
 */
public class MaxHeightJScrollPaneTest extends TestCase {
  /**
   * Maximum height for the scroll pane
   */
  private static final int MAX_HEIGHT = 500;
  
  /**
   * Dimensions for component 1
   */
  private static final Dimension JP1_DIM = new Dimension(200, 1000);
  
  /**
   * Dimensions for component 2
   */
  private static final Dimension JP2_DIM = new Dimension(100, 1500);
  
  /**
   * A scroll pane with a policy but no component
   */
  private MaxHeightJScrollPane scrollPaneCompOnly;
  
  /**
   * A scroll pane with a policy and component
   */
  private MaxHeightJScrollPane scrollPaneCompAndPolicy;

  /**
   * Panel 1 for testing
   */
  private JPanel jp1;

  /**
   * Panel 2 for testing
   */
  private JPanel jp2;

  /**
   * Setup to test case instance
   */
  public MaxHeightJScrollPaneTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    jp1 = new JPanel();
    jp1.setPreferredSize(JP1_DIM);
    scrollPaneCompOnly = new MaxHeightJScrollPane(jp1);

    jp2 = new JPanel();
    jp2.setPreferredSize(JP2_DIM);
    scrollPaneCompAndPolicy = new MaxHeightJScrollPane(jp2,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
  }

  /**
   * Test getting the height
   */
  public void testLockHeight() {
    scrollPaneCompOnly.lockHeight();

    assertTrue(jp1.getPreferredSize().height 
        <= scrollPaneCompOnly.getPreferredSize().height);
  }

  /**
   * Test getting the preferred size
   */
  public void testGetPreferredSize() {
    scrollPaneCompAndPolicy.lockHeight();

    assertTrue(jp2.getPreferredSize().height 
        <= scrollPaneCompAndPolicy.getPreferredSize().height);
  }

  /**
   * Test getting the maximum size
   */
  public void testGetMaximumSize() {
    scrollPaneCompAndPolicy.lockHeight();

    assertTrue(jp2.getPreferredSize().height 
        <= scrollPaneCompAndPolicy.getMaximumSize().height);
  }

  /**
   * Test getting the minimum size
   */
  public void testGetMinimumSize() {
    scrollPaneCompAndPolicy.lockHeight();

    assertTrue(jp1.getPreferredSize().height 
        <= scrollPaneCompOnly.getMinimumSize().height);
  }

  /**
   * Test setting the height
   */
  public void testSetHeight() {
    scrollPaneCompOnly.setHeight(MAX_HEIGHT);

    assertEquals(MAX_HEIGHT, scrollPaneCompOnly.getPreferredSize().height);
    assertEquals(MAX_HEIGHT, scrollPaneCompOnly.getMaximumSize().height);
    assertEquals(MAX_HEIGHT, scrollPaneCompOnly.getMinimumSize().height);
  }
}
