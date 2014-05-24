package us.daveread.basicquery.util.test;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import junit.framework.*;

import us.daveread.basicquery.util.TableMap;
import javax.swing.event.TableModelEvent;

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
 * @version $Id: TableMapTest.java,v 1.1 2006/05/03 03:55:07 daveread Exp $
 */
public class TableMapTest extends TestCase implements TableModelListener {
  private TableMap map;
  private TableModel model1;
  private TableModel model2;
  private boolean changed;

  private String[][] data = { {"R1/C1", "R1/C2", "R1/C3"}, {"R2/C1", "R2/C2",
      "R2/C3"}, {"R3/C1", "R3/C2", "R3/C3"}, {"R4/C1", "R4/C2", "R4/C3"}
  };

  private String[] colNames = {
      "Col-1", "Col-2", "Col-3"
  };

  public TableMapTest() {
  }

  public void setUp() {
    model1 = new DefaultTableModel(data, colNames);
    model2 = new DefaultTableModel(10, 3);

    map = new TableMap();
    map.setModel(model1);

    map.addTableModelListener(this);
  }

  public void testGetModel() {
    assertEquals(model1, map.getModel());
  }

  public void testSetModel() {
    map.setModel(model2);
    assertEquals(model2, map.getModel());
  }

  public void testRemoveModel() {
    map.removeModel(model1);
    assertNull(map.getModel());
  }

  public void testGetValueAt() {
    assertEquals(data[1][2], map.getValueAt(1, 2));
    assertEquals(data[3][0], map.getValueAt(3, 0));
  }

  public void testSetValueAt() {
    map.setValueAt("NewValue", 2, 1);
    assertEquals("NewValue", map.getValueAt(2, 1));
  }

  public void testGetRowCount() {
    assertEquals(data.length, map.getRowCount());
  }

  public void testGetColumnCount() {
    assertEquals(data[0].length, map.getColumnCount());
  }

  public void testGetColumnName() {
    assertEquals(colNames[0], map.getColumnName(0));
    assertEquals(colNames[1], map.getColumnName(1));
    assertEquals(colNames[2], map.getColumnName(2));
  }

  public void testGetColumnClass() {
    assertEquals(Object.class, map.getColumnClass(0));
  }

  public void testIsCellEditable() {
    assertTrue(map.isCellEditable(0, 0));
  }

  public void testTableChanged() {
    map.tableChanged(new TableModelEvent(model1, 0));
    assertTrue(changed);
  }

  public void tableChanged(TableModelEvent e) {
    changed = true;
  }
}
