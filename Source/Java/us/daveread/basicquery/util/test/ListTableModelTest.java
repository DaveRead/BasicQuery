package us.daveread.basicquery.util.test;

import java.util.List;
import java.util.ArrayList;
import javax.swing.event.TableModelListener;
import junit.framework.*;

import us.daveread.basicquery.util.ListTableModel;
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
 * @version $Id: ListTableModelTest.java,v 1.2 2006/05/04 03:34:07 daveread Exp $
 */
public class ListTableModelTest extends TestCase implements TableModelListener {
  private ListTableModel model;
  private boolean modelChanged;

  public ListTableModelTest() {
  }

  public void setUp() {
    List data;

    model = new ListTableModel();

    model.addColumn("MyCol1");
    model.addColumn("MyCol2");

    data = new ArrayList();
    data.add("Data1-1");
    data.add("Data1-2");
    model.addRowFast(data);

    data = new ArrayList();
    data.add("Data2-1");
    data.add("Data2-2");
    model.addRow(data);

    data = new ArrayList();
    data.add("Data3-1");
    data.add("Data3-2");
    model.addRow(data);

    model.addTableModelListener(this);
  }

  public void testAddRow() {
    List data = new ArrayList();

    data.add("Data4-1");
    data.add("Data4-2");

    model.addRow(data);

    assertEquals(4, model.getRowCount());
    assertTrue(modelChanged);
  }

  public void testAddRowFast() {
    List data = new ArrayList();

    data.add("Data4-1");
    data.add("Data4-2");

    model.addRowFast(data);

    assertEquals(4, model.getRowCount());
    assertFalse(modelChanged);
  }

  public void testGetColumnCount() {
    assertEquals(2, model.getColumnCount());
  }

  public void testGetColumnName() {
    assertEquals("MyCol2", model.getColumnName(1));
  }

  public void testGetRowCount() {
    assertEquals(3, model.getRowCount());
  }

  public void testGetValueAt() {
    assertEquals("Data1-1", model.getValueAt(0, 0));
    assertEquals("Data3-2", model.getValueAt(2, 1));
  }

  public void testAddColumn() {
    model.addColumn("MyCol3");
    model.addColumn("MyCol4");

    assertEquals(4, model.getColumnCount());
    assertEquals("MyCol3", model.getColumnName(2));
    assertEquals("MyCol4", model.getColumnName(3));
  }

  public void testSetColumnIdentifiers() {
    List cols = new ArrayList();

    cols.add("Column Name 1");
    cols.add("Column Name 2");
    cols.add("Column Name 3");

    model.setColumnIdentifiers(cols);

    assertEquals(3, model.getColumnCount());
    assertEquals("Column Name 1", model.getColumnName(0));
    assertEquals("Column Name 2", model.getColumnName(1));
    assertEquals("Column Name 3", model.getColumnName(2));
  }

  public void testUpdateCompleted() {
    assertFalse(modelChanged);
    model.updateCompleted();
    assertTrue(modelChanged);
  }

  public void tableChanged(TableModelEvent e) {
    modelChanged = true;
  }
}
