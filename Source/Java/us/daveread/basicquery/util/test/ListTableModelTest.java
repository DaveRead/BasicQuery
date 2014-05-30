package us.daveread.basicquery.util.test;

import java.util.List;
import java.util.ArrayList;
import javax.swing.event.TableModelListener;

import us.daveread.basicquery.util.ListTableModel;
import javax.swing.event.TableModelEvent;

import junit.framework.TestCase;

/**
 * <p>
 * Title: Test the list table model class
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
public class ListTableModelTest extends TestCase implements TableModelListener {
  /**
   * The list table model instance being tested
   */
  private ListTableModel<String> model;

  /**
   * Track whether the model has been changed
   */
  private boolean modelChanged;

  /**
   * Setup the test case instance
   */
  public ListTableModelTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    List<String> data;

    model = new ListTableModel<String>();

    model.addColumn("MyCol1");
    model.addColumn("MyCol2");

    data = new ArrayList<String>();
    data.add("Data1-1");
    data.add("Data1-2");
    model.addRowFast(data);

    data = new ArrayList<String>();
    data.add("Data2-1");
    data.add("Data2-2");
    model.addRow(data);

    data = new ArrayList<String>();
    data.add("Data3-1");
    data.add("Data3-2");
    model.addRow(data);

    model.addTableModelListener(this);
  }

  /**
   * Test adding a row to the model
   */
  public void testAddRow() {
    final List<String> data = new ArrayList<String>();

    data.add("Data4-1");
    data.add("Data4-2");

    model.addRow(data);

    assertEquals(4, model.getRowCount());
    assertTrue(modelChanged);
  }

  /**
   * Test the add row fast method
   */
  public void testAddRowFast() {
    final List<String> data = new ArrayList<String>();

    data.add("Data4-1");
    data.add("Data4-2");

    model.addRowFast(data);

    assertEquals(4, model.getRowCount());
    assertFalse(modelChanged);
  }

  /**
   * Test getting the column count
   */
  public void testGetColumnCount() {
    assertEquals(2, model.getColumnCount());
  }

  /**
   * Test getting a column name
   */
  public void testGetColumnName() {
    assertEquals("MyCol2", model.getColumnName(1));
  }

  /**
   * Test getting the row count
   */
  public void testGetRowCount() {
    assertEquals(3, model.getRowCount());
  }

  /**
   * Test getting a value out of the model
   */
  public void testGetValueAt() {
    assertEquals("Data1-1", model.getValueAt(0, 0));
    assertEquals("Data3-2", model.getValueAt(2, 1));
  }

  /**
   * Test adding a column
   */
  public void testAddColumn() {
    model.addColumn("MyCol3");
    model.addColumn("MyCol4");

    assertEquals(4, model.getColumnCount());
    assertEquals("MyCol3", model.getColumnName(2));
    assertEquals("MyCol4", model.getColumnName(3));
  }

  /**
   * Test changing the column names
   */
  public void testSetColumnIdentifiers() {
    final List<String> cols = new ArrayList<String>();

    cols.add("Column Name 1");
    cols.add("Column Name 2");
    cols.add("Column Name 3");

    model.setColumnIdentifiers(cols);

    assertEquals(3, model.getColumnCount());
    assertEquals("Column Name 1", model.getColumnName(0));
    assertEquals("Column Name 2", model.getColumnName(1));
    assertEquals("Column Name 3", model.getColumnName(2));
  }

  /**
   * Test the update completed method
   */
  public void testUpdateCompleted() {
    assertFalse(modelChanged);
    model.updateCompleted();
    assertTrue(modelChanged);
  }

  @Override
  public void tableChanged(TableModelEvent e) {
    modelChanged = true;
  }
}
