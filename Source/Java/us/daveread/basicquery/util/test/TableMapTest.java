package us.daveread.basicquery.util.test;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;

import us.daveread.basicquery.util.TableMap;
import javax.swing.event.TableModelEvent;

import junit.framework.TestCase;

/**
 * <p>
 * Title: Test TableMap class
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
public class TableMapTest extends TestCase implements TableModelListener {
  /**
   * The table map instance being tested
   */
  private TableMap map;

  /**
   * Test model 1
   */
  private TableModel model1;

  /**
   * Test model 2
   */
  private TableModel model2;

  /**
   * Track whether the model has changed
   */
  private boolean changed;

  /**
   * Test data
   */
  private String[][] data = {
      {
          "R1/C1", "R1/C2", "R1/C3"
      },
      {
          "R2/C1", "R2/C2", "R2/C3"
      },
      {
          "R3/C1", "R3/C2", "R3/C3"
      },
      {
          "R4/C1", "R4/C2", "R4/C3"
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
  public TableMapTest() {
  }

  /**
   * Set the test
   */
  public void setUp() {
    model1 = new DefaultTableModel(data, colNames);
    model2 = new DefaultTableModel(10, 3);

    map = new TableMap();
    map.setModel(model1);

    map.addTableModelListener(this);
  }

  /**
   * Test getting the model
   */
  public void testGetModel() {
    assertEquals(model1, map.getModel());
  }

  /**
   * Test setting the model
   */
  public void testSetModel() {
    map.setModel(model2);
    assertEquals(model2, map.getModel());
  }

  /**
   * Test removing the model
   */
  public void testRemoveModel() {
    map.removeModel(model1);
    assertNull(map.getModel());
  }

  /**
   * Test getting a value from the map
   */
  public void testGetValueAt() {
    assertEquals(data[1][2], map.getValueAt(1, 2));
    assertEquals(data[3][0], map.getValueAt(3, 0));
  }

  /**
   * Test setting a value in the map
   */
  public void testSetValueAt() {
    map.setValueAt("NewValue", 2, 1);
    assertEquals("NewValue", map.getValueAt(2, 1));
  }

  /**
   * Test getting the row count
   */
  public void testGetRowCount() {
    assertEquals(data.length, map.getRowCount());
  }

  /**
   * Test getting the column count
   */
  public void testGetColumnCount() {
    assertEquals(data[0].length, map.getColumnCount());
  }

  /**
   * Test getting a column name
   */
  public void testGetColumnName() {
    assertEquals(colNames[0], map.getColumnName(0));
    assertEquals(colNames[1], map.getColumnName(1));
    assertEquals(colNames[2], map.getColumnName(2));
  }

  /**
   * Test getting a column class
   */
  public void testGetColumnClass() {
    assertEquals(Object.class, map.getColumnClass(0));
  }

  /**
   * Test determine if the cell is editable
   */
  public void testIsCellEditable() {
    assertTrue(map.isCellEditable(0, 0));
  }

  /**
   * Test detecting that the table has changed
   */
  public void testTableChanged() {
    map.tableChanged(new TableModelEvent(model1, 0));
    assertTrue(changed);
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    changed = true;
  }
}
