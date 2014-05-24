package us.daveread.basicquery.gui.test;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;

import junit.framework.*;
import us.daveread.basicquery.gui.ColoredCellRenderer;

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
public class ColoredCellRendererTest extends TestCase {

  private ColoredCellRenderer renderer;

  public ColoredCellRendererTest() {
  }

  public void setUp() {
    renderer = new ColoredCellRenderer();
  }

  public void testConstructor() {
    assertNotNull(renderer);

    renderer = new ColoredCellRenderer(ColoredCellRenderer.CENTER);
    assertNotNull(renderer);

    renderer = new ColoredCellRenderer(ColoredCellRenderer.CENTER,
        ColoredCellRenderer.CENTER);
    assertNotNull(renderer);
  }

  public void testSetDefaultColors() {
    renderer.setDefaultColors(Color.black, Color.blue);
    assertNotNull(renderer);
  }

  public void testAddPattern() {
    renderer.addPattern("Failure", Color.white, Color.red);
    assertNotNull(renderer);
  }

  public void testGetTableCellRendererComponent() {
    Component cellRenderer;

    JTable table = new JTable();
    table.setDefaultRenderer(new Object().getClass(), renderer);
    cellRenderer = renderer.getTableCellRendererComponent(table, "Failure", true, true,
        1, 1);
    assertNotNull(cellRenderer);
  }

  public void testClearColors() {
    JTable table = new JTable();
    JTable table2 = new JTable();

    renderer.addAlternatingRowColor(Color.black, Color.white);
    renderer.clearColors();

    assertEquals(table.getForeground(),
        renderer.getTableCellRendererComponent(table2, "", false, false, 0,
        0).getForeground());
    assertEquals(table.getBackground(),
        renderer.getTableCellRendererComponent(table2, "", false, false, 0,
        0).getBackground());
  }

  public void testAddAlternatingRowColor() {
    JTable table = new JTable();

    renderer.clearColors();
    renderer.addAlternatingRowColor(Color.black, Color.green);
    renderer.addAlternatingRowColor(Color.white, Color.red);

    assertEquals(Color.black, renderer.getTableCellRendererComponent(table, "", false, false,
        0, 0).getForeground());
    assertEquals(Color.black, renderer.getTableCellRendererComponent(table, null, false, false,
        0, 0).getForeground());
    assertEquals(Color.green, renderer.getTableCellRendererComponent(table, "", false, false,
        0, 0).getBackground());

    assertEquals(Color.white, renderer.getTableCellRendererComponent(table, "", false, false,
        1, 0).getForeground());
    assertEquals(Color.white, renderer.getTableCellRendererComponent(table, null, false, false,
        1, 0).getForeground());
    assertEquals(Color.red, renderer.getTableCellRendererComponent(table, "", false, false,
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
