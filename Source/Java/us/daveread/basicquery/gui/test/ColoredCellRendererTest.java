package us.daveread.basicquery.gui.test;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;

import junit.framework.TestCase;

import us.daveread.basicquery.gui.ColoredCellRenderer;

/**
 * <p>
 * Title: Test the ColoredCellRenderer
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
public class ColoredCellRendererTest extends TestCase {

  /**
   * The renderer instance
   */
  private ColoredCellRenderer renderer;

  /**
   * Construct the test instance
   */
  public ColoredCellRendererTest() {
  }

  /**
   * Setup the renderer
   */
  public void setUp() {
    renderer = new ColoredCellRenderer();
  }

  /**
   * Test the constructor
   */
  public void testConstructor() {
    assertNotNull(renderer);

    renderer = new ColoredCellRenderer(ColoredCellRenderer.CENTER);
    assertNotNull(renderer);

    renderer = new ColoredCellRenderer(ColoredCellRenderer.CENTER,
        ColoredCellRenderer.CENTER);
    assertNotNull(renderer);
  }

  /**
   * Test the default colors
   */
  public void testSetDefaultColors() {
    renderer.setDefaultColors(Color.black, Color.blue);
    assertNotNull(renderer);
  }

  /**
   * Test adding a pattern
   */
  public void testAddPattern() {
    renderer.addPattern("Failure", Color.white, Color.red);
    assertNotNull(renderer);
  }

  /**
   * Test getting the component
   */
  public void testGetTableCellRendererComponent() {
    Component cellRenderer;

    final JTable table = new JTable();
    table.setDefaultRenderer(new Object().getClass(), renderer);
    cellRenderer = renderer.getTableCellRendererComponent(table, "Failure",
        true, true,
        1, 1);
    assertNotNull(cellRenderer);
  }

  /**
   * Test clearing the colors
   */
  public void testClearColors() {
    final JTable table = new JTable();
    final JTable table2 = new JTable();

    renderer.addAlternatingRowColor(Color.black, Color.white);
    renderer.clearColors();

    assertEquals(table.getForeground(),
        renderer.getTableCellRendererComponent(table2, "", false, false, 0,
            0).getForeground());
    assertEquals(table.getBackground(),
        renderer.getTableCellRendererComponent(table2, "", false, false, 0,
            0).getBackground());
  }

  /**
   * Test adding multiple row colors
   */
  public void testAddAlternatingRowColor() {
    final JTable table = new JTable();

    renderer.clearColors();
    renderer.addAlternatingRowColor(Color.black, Color.green);
    renderer.addAlternatingRowColor(Color.white, Color.red);

    assertEquals(Color.black,
        renderer.getTableCellRendererComponent(table, "", false, false,
            0, 0).getForeground());
    assertEquals(Color.black,
        renderer.getTableCellRendererComponent(table, null, false, false,
            0, 0).getForeground());
    assertEquals(Color.green,
        renderer.getTableCellRendererComponent(table, "", false, false,
            0, 0).getBackground());

    assertEquals(Color.white,
        renderer.getTableCellRendererComponent(table, "", false, false,
            1, 0).getForeground());
    assertEquals(Color.white,
        renderer.getTableCellRendererComponent(table, null, false, false,
            1, 0).getForeground());
    assertEquals(Color.red,
        renderer.getTableCellRendererComponent(table, "", false, false,
            1, 0).getBackground());

    assertEquals(table.getForeground(),
        renderer.getTableCellRendererComponent(table, "", true, true, 0,
            0).getForeground());
    assertEquals(table.getBackground(),
        renderer.getTableCellRendererComponent(table, "", true, true, 0,
            0).getBackground());

    assertEquals(table.getSelectionForeground(),
        renderer.getTableCellRendererComponent(table, "", true, false, 0,
            0).getForeground());
    assertEquals(table.getSelectionBackground(),
        renderer.getTableCellRendererComponent(table, "", true, false, 0,
            0).getBackground());

  }
}
