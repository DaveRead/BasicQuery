package us.daveread.basicquery.util.test;

import junit.framework.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JLabel;
import java.util.Date;
import org.apache.log4j.Logger;

import us.daveread.basicquery.util.TableSorter;

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
 * @version $Id: TableSorterTest.java,v 1.2 2006/05/04 03:34:07 daveread Exp $
 */
public class TableSorterTest extends TestCase {
  private TableSorter sorter;
  private TableModel model1;
  private TableModel model2;
  private JTable table;

  private static final Logger logger = Logger.getLogger(TableSorterTest.class);

  private Object[][] data1 = { {"R1/C1", "R1/C2", "R1/C3"}, {"R2/C1", "R2/C2",
      "R2/C3"}, {null, null, null}, {"R3/C1", "R3/C2", "R3/C3"}, {"R0/C1",
      "R0/C2", "R0/C3"}, {"R4/C1", "R4/C2", "R4/C3"}, {"R4/C1", "R4/C2",
      "R4/C3"}, {null, null, null}
  };

  private Object[][] dataNumeric = {
      // Numbers
      {new Integer(1), new Integer(2), new Integer(3)}, {new Integer(11),
      new Integer(2), new Integer( -3)}, {new Integer(15), new Integer(20),
      new Integer(30)}, {new Integer(15), new Integer(20), new Integer(30)},
      {new Integer( -11), new Integer( -2), new Integer( -43)}, {null, null, null}
  };

  private Object[][] dataDates = {
      // Dates
      {new Date(10), new Date(100), new Date(1000)}, {new Date(10000),
      new Date(100), new Date(20)}, {new Date(10000), new Date(100),
      new Date(20)}, {new Date(20000), new Date(200), new Date(40)},
      {new Date(15000), new Date(150), new Date(30)}, {null, null, null},
      {new Date(10), new Date(100), new Date(80)}
  };

  private Object[][] dataBooleans = {
      // Boolean
      {new Boolean(false), new Boolean(true), new Boolean(false)}, {null, null, null},
      {new Boolean(true), new Boolean(true), new Boolean(false)}, {new Boolean(true),
      new Boolean(true), new Boolean(true)}, {new Boolean(false), new Boolean(true),
      new Boolean(false)}
  };

  private Object[][] dataObjects = { {new JLabel("B"), new JLabel("D"),
      new JLabel("C")}, {null, null, null}, {new JLabel("D"), new JLabel("E"),
      new JLabel("C")}, {new JLabel("A"), new JLabel("B"), new JLabel("C")}
  };

  private String[] colNames = {
      "Col-1", "Col-2", "Col-3"
  };

  public TableSorterTest() {
  }

  public void setUp() {
    model1 = new DefaultTableModel(data1, colNames);
    model2 = new DefaultTableModel(dataNumeric, colNames);

    sorter = new TableSorter();
    sorter.setModel(model1);

    table = new JTable();
  }

  public void testTableSorter() {
    sorter = new TableSorter(model2);
    assertEquals(model2, sorter.getModel());
  }

  public void testSetModel() {
    sorter.setModel(model2);
    assertEquals(model2, sorter.getModel());
  }

  public void testRemoveModel() {
    sorter.removeModel(model1);
    assertNull(sorter.getModel());
  }

  /*  public void testCompareRowsByColumn() {
      assertEquals(0, sorter.compareRowsByColumn(5, 5, 0));
      assertEquals(-1, sorter.compareRowsByColumn(5, 4, 1));
      assertEquals(1, sorter.compareRowsByColumn(3, 5, 2));
      assertEquals(-1, sorter.compareRowsByColumn(0, 1, 0));
      assertEquals(0, sorter.compareRowsByColumn(3, 4, 1));
      assertEquals(1, sorter.compareRowsByColumn(2, 1, 2));

      sorter = new TableSorter(model2);

      // Numbers
      assertEquals(-1, sorter.compareRowsByColumn(0, 1, 0));
      assertEquals(0, sorter.compareRowsByColumn(0, 1, 1));
      assertEquals(1, sorter.compareRowsByColumn(0, 1, 2));

      // Dates
      assertEquals(-1, sorter.compareRowsByColumn(2, 3, 0));
      assertEquals(0, sorter.compareRowsByColumn(2, 3, 1));
      assertEquals(1, sorter.compareRowsByColumn(2, 3, 2));

      // Boolean
      assertEquals(-1, sorter.compareRowsByColumn(4, 5, 0));
      assertEquals(0, sorter.compareRowsByColumn(4, 5, 1));
      assertEquals(0, sorter.compareRowsByColumn(4, 5, 2));
      assertEquals(1, sorter.compareRowsByColumn(5, 4, 0));

      // Objects
      assertEquals(-1, sorter.compareRowsByColumn(6, 7, 0));
      assertEquals(0, sorter.compareRowsByColumn(6, 7, 2));
      assertEquals(1, sorter.compareRowsByColumn(7, 6, 1));
    } */

  /*   public void testCompare() {
      sorter.sortByColumn(0);
      assertEquals(-1, sorter.compare(0, 1));

      sorter.sortByColumn(1, false);
      assertEquals(1, sorter.compare(0, 1));

    } */

  public void testGetValueAt() {
    assertEquals(data1[1][2], sorter.getValueAt(1, 2));
    assertEquals(data1[3][0], sorter.getValueAt(3, 0));
  }

  public void testSetValueAt() {
    sorter.setValueAt("NewValue", 2, 1);
    assertEquals("NewValue", sorter.getValueAt(2, 1));
  }

  public void testSortByColumns() {
    int[] columns;

    assertEquals("R1/C3", sorter.getValueAt(0, 2));
    assertEquals("R2/C3", sorter.getValueAt(1, 2));

    columns = new int[2];
    columns[0] = 2;
    columns[1] = 0;
    sorter.sortByColumns(columns);

    assertNull(sorter.getValueAt(0, 2));
    assertEquals("R0/C3", sorter.getValueAt(2, 2));

    // Numeric
    model2 = new DefaultTableModel(dataNumeric, colNames);
    sorter = new TableSorter();
    sorter.setModel(model2);

    assertEquals(new Integer(3), sorter.getValueAt(0, 2));
    assertEquals(new Integer( -3), sorter.getValueAt(1, 2));

    columns = new int[2];
    columns[0] = 2;
    columns[1] = 0;
    sorter.sortByColumns(columns);

    assertNull(sorter.getValueAt(0, 2));
    assertEquals(new Integer( -43), sorter.getValueAt(1, 2));

    // Dates
    model2 = new DefaultTableModel(dataDates, colNames);
    sorter = new TableSorter();
    sorter.setModel(model2);

    assertEquals(new Date(1000), sorter.getValueAt(0, 2));
    assertEquals(new Date(20), sorter.getValueAt(1, 2));

    columns = new int[2];
    columns[0] = 2;
    columns[1] = 0;
    sorter.sortByColumns(columns);

    assertNull(sorter.getValueAt(0, 2));
    assertEquals(new Date(20), sorter.getValueAt(1, 2));

    // Booleans
    model2 = new DefaultTableModel(dataBooleans, colNames);
    sorter = new TableSorter();
    sorter.setModel(model2);

    assertEquals(new Boolean(false), sorter.getValueAt(0, 0));
    assertNull(sorter.getValueAt(1, 0));

    columns = new int[2];
    columns[0] = 2;
    columns[1] = 0;
    sorter.sortByColumns(columns);

    assertNull(sorter.getValueAt(0, 0));
    assertEquals(new Boolean(false), sorter.getValueAt(1, 0));

    // Objects
    model2 = new DefaultTableModel(dataObjects, colNames);
    sorter = new TableSorter();
    sorter.setModel(model2);

    assertEquals("D", ((JLabel)sorter.getValueAt(0, 1)).getText());
    assertNull(sorter.getValueAt(1, 1));

    columns = new int[2];
    columns[0] = 2;
    columns[1] = 0;
    sorter.sortByColumns(columns);

    assertNull(sorter.getValueAt(0, 1));
    assertEquals("B", ((JLabel)sorter.getValueAt(1, 1)).getText());
  }

  public void testAddMouseListenerToHeaderInTable() {
    int numListeners;

    numListeners = table.getTableHeader().getMouseListeners().length;
    sorter.addMouseListenerToHeaderInTable(table);
    numListeners++;
    assertEquals(numListeners,
        table.getTableHeader().getMouseListeners().length);
  }

  public void testRemoveMouseListenerFromHeaderInTable() {
    int numListeners;

    numListeners = table.getTableHeader().getMouseListeners().length;

    sorter.addMouseListenerToHeaderInTable(table);
    numListeners++;
    assertEquals(numListeners,
        table.getTableHeader().getMouseListeners().length);

    sorter.removeMouseListenerFromHeaderInTable(table);
    numListeners--;
    assertEquals(numListeners,
        table.getTableHeader().getMouseListeners().length);
  }

  public void testSortFromHeaderClick() {
    MouseEvent event;
    MouseListener[] listeners;

    assertEquals("R1/C1", sorter.getValueAt(0, 0));
    assertEquals("R2/C1", sorter.getValueAt(1, 0));

    table = new JTable(sorter);
    sorter.addMouseListenerToHeaderInTable(table);
    event = new MouseEvent(new JLabel("Fake Header"), 0, 0, 0, 10, 10, 1, false);

    listeners = table.getTableHeader().getMouseListeners();

    logger.info("testSortFromHeaderClick listener count[" + listeners.length +
        "]");
    for (int i = 0; i < listeners.length; ++i) {
      listeners[i].mouseClicked(event);
    }

    assertNull(sorter.getValueAt(0, 0));
    assertEquals("R0/C1", sorter.getValueAt(2, 0));
  }
}
