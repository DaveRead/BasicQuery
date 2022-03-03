package us.daveread.basicquery.util.test;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JLabel;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import us.daveread.basicquery.util.TableSorter;

/**
 * <p>
 * Title: Test the TableSorter class
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006-2014
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author David Read
 */
public class TableSorterTest extends TestCase {
  /**
   * The table sorter instance to test
   */
  private TableSorter sorter;

  /**
   * Test table model 1
   */
  private TableModel model1;

  /**
   * Test table model 2
   */
  private TableModel model2;

  /**
   * Test table
   */
  private JTable table;

  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(TableSorterTest.class);

  /**
   * Test string data
   */
  private Object[][] data1 = {
      {
          "R1/C1", "R1/C2", "R1/C3"
      },
      {
          "R2/C1", "R2/C2", "R2/C3"
      },
      {
          null, null, null
      },
      {
          "R3/C1", "R3/C2", "R3/C3"
      },
      {
          "R0/C1", "R0/C2", "R0/C3"
      },
      {
          "R4/C1", "R4/C2", "R4/C3"
      },
      {
          "R4/C1", "R4/C2", "R4/C3"
      },
      {
          null, null, null
      }
  };

  /**
   * Test numeric data
   */
  private Object[][] dataNumeric = {
      // Numbers
      {
          1, 2, 3
      },
      {
          11, 2, -3
      },
      {
          15, 20, 30
      },
      {
          15, 20, 30
      },
      {
          -11, -2, -43
      },
      {
          null, null, null
      }
  };

  /**
   * Test date date
   */
  private Object[][] dataDates = {
      // Dates
      {
          new Date(10), new Date(100), new Date(1000)
      },
      {
          new Date(10000), new Date(100), new Date(20)
      },
      {
          new Date(10000), new Date(100), new Date(20)
      },
      {
          new Date(20000), new Date(200), new Date(40)
      },
      {
          new Date(15000), new Date(150), new Date(30)
      },
      {
          null, null, null
      },
      {
          new Date(10), new Date(100), new Date(80)
      }
  };

  /**
   * Test data Boolean
   */
  private Object[][] dataBooleans = {
      // Boolean
      {
          false, true, false
      },
      {
          null, null, null
      },
      {
          true, true, false
      },
      {
          true, true, true
      },
      {
          false, true, false
      }
  };

  /**
   * Test data object
   */
  private Object[][] dataObjects = {
      {
          new JLabel("B"), new JLabel("D"), new JLabel("C")
      },
      {
          null, null, null
      },
      {
          new JLabel("D"), new JLabel("E"), new JLabel("C")
      },
      {
          new JLabel("A"), new JLabel("B"), new JLabel("C")
      }
  };

  /**
   * Test column names
   */
  private String[] colNames = {
      "Col-1", "Col-2", "Col-3"
  };

  /**
   * Setup the test case instance
   */
  public TableSorterTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    model1 = new DefaultTableModel(data1, colNames);
    model2 = new DefaultTableModel(dataNumeric, colNames);

    sorter = new TableSorter();
    sorter.setModel(model1);

    table = new JTable();
  }

  /**
   * Test sorting the model
   */
  public void testTableSorter() {
    sorter = new TableSorter(model2);
    assertEquals(model2, sorter.getModel());
  }

  /**
   * Test setting the model
   */
  public void testSetModel() {
    sorter.setModel(model2);
    assertEquals(model2, sorter.getModel());
  }

  /**
   * Test removing the model
   */
  public void testRemoveModel() {
    sorter.removeModel(model1);
    assertNull(sorter.getModel());
  }

  /*
   * public void testCompareRowsByColumn() {
   * assertEquals(0, sorter.compareRowsByColumn(5, 5, 0));
   * assertEquals(-1, sorter.compareRowsByColumn(5, 4, 1));
   * assertEquals(1, sorter.compareRowsByColumn(3, 5, 2));
   * assertEquals(-1, sorter.compareRowsByColumn(0, 1, 0));
   * assertEquals(0, sorter.compareRowsByColumn(3, 4, 1));
   * assertEquals(1, sorter.compareRowsByColumn(2, 1, 2));
   * 
   * sorter = new TableSorter(model2);
   * 
   * // Numbers
   * assertEquals(-1, sorter.compareRowsByColumn(0, 1, 0));
   * assertEquals(0, sorter.compareRowsByColumn(0, 1, 1));
   * assertEquals(1, sorter.compareRowsByColumn(0, 1, 2));
   * 
   * // Dates
   * assertEquals(-1, sorter.compareRowsByColumn(2, 3, 0));
   * assertEquals(0, sorter.compareRowsByColumn(2, 3, 1));
   * assertEquals(1, sorter.compareRowsByColumn(2, 3, 2));
   * 
   * // Boolean
   * assertEquals(-1, sorter.compareRowsByColumn(4, 5, 0));
   * assertEquals(0, sorter.compareRowsByColumn(4, 5, 1));
   * assertEquals(0, sorter.compareRowsByColumn(4, 5, 2));
   * assertEquals(1, sorter.compareRowsByColumn(5, 4, 0));
   * 
   * // Objects
   * assertEquals(-1, sorter.compareRowsByColumn(6, 7, 0));
   * assertEquals(0, sorter.compareRowsByColumn(6, 7, 2));
   * assertEquals(1, sorter.compareRowsByColumn(7, 6, 1));
   * }
   */

  /*
   * public void testCompare() {
   * sorter.sortByColumn(0);
   * assertEquals(-1, sorter.compare(0, 1));
   * 
   * sorter.sortByColumn(1, false);
   * assertEquals(1, sorter.compare(0, 1));
   * 
   * }
   */

  /**
   * Test getting a value from a location
   */
  public void testGetValueAt() {
    assertEquals(data1[1][2], sorter.getValueAt(1, 2));
    assertEquals(data1[3][0], sorter.getValueAt(3, 0));
  }

  /**
   * Test setting a value at a location
   */
  public void testSetValueAt() {
    sorter.setValueAt("NewValue", 2, 1);
    assertEquals("NewValue", sorter.getValueAt(2, 1));
  }

  /**
   * Test sorting by columns
   */
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

    assertEquals(3, sorter.getValueAt(0, 2));
    assertEquals(-3, sorter.getValueAt(1, 2));

    columns = new int[2];
    columns[0] = 2;
    columns[1] = 0;
    sorter.sortByColumns(columns);

    assertNull(sorter.getValueAt(0, 2));
    assertEquals(-43, sorter.getValueAt(1, 2));

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

    assertEquals(false, sorter.getValueAt(0, 0));
    assertNull(sorter.getValueAt(1, 0));

    columns = new int[2];
    columns[0] = 2;
    columns[1] = 0;
    sorter.sortByColumns(columns);

    assertNull(sorter.getValueAt(0, 0));
    assertEquals(false, sorter.getValueAt(1, 0));

    // Objects
    model2 = new DefaultTableModel(dataObjects, colNames);
    sorter = new TableSorter();
    sorter.setModel(model2);

    assertEquals("D", ((JLabel) sorter.getValueAt(0, 1)).getText());
    assertNull(sorter.getValueAt(1, 1));

    columns = new int[2];
    columns[0] = 2;
    columns[1] = 0;
    sorter.sortByColumns(columns);

    assertNull(sorter.getValueAt(0, 1));
    assertEquals("B", ((JLabel) sorter.getValueAt(1, 1)).getText());
  }

  /**
   * Test adding a mouse listener to the header
   */
  public void testAddMouseListenerToHeaderInTable() {
    int numListeners;

    numListeners = table.getTableHeader().getMouseListeners().length;
    sorter.addMouseListenerToHeaderInTable(table);
    numListeners++;
    assertEquals(numListeners,
        table.getTableHeader().getMouseListeners().length);
  }

  /**
   * Test removing the mouse listener from the header
   */
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

  /**
   * Test sorting based on a mouse click
   */
  public void testSortFromHeaderClick() {
    MouseEvent event;
    MouseListener[] listeners;

    assertEquals("R1/C1", sorter.getValueAt(0, 0));
    assertEquals("R2/C1", sorter.getValueAt(1, 0));

    table = new JTable(sorter);
    sorter.addMouseListenerToHeaderInTable(table);
    event = new MouseEvent(new JLabel("Fake Header"), 0, 0, 0, 10, 10, 1, false);

    listeners = table.getTableHeader().getMouseListeners();

    LOGGER.info("testSortFromHeaderClick listener count[" + listeners.length
        + "]");
    for (int i = 0; i < listeners.length; ++i) {
      listeners[i].mouseClicked(event);
    }

    assertNull(sorter.getValueAt(0, 0));
    assertEquals("R0/C1", sorter.getValueAt(2, 0));
  }
}
