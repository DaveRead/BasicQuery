package us.daveread.basicquery.test;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.event.ListDataListener;

import us.daveread.basicquery.ReorderListModel;
import javax.swing.event.ListDataEvent;

import junit.framework.TestCase;

/**
 * <p>
 * Title: Test the reorder list model class
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
public class ReorderListModelTest extends TestCase implements ListDataListener {
  /**
   * The initial list of objects
   */
  private static final String[] INITIAL_LIST = {
      "First", "Second", "Third", "Fourth"
  };

  /**
   * The reorder list model instance the test
   */
  private ReorderListModel<String> model;

  /**
   * The number of changes made
   */
  private int changeCount;

  /**
   * Setup the test case instance
   */
  public ReorderListModelTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    model = new ReorderListModel<String>(new ArrayList<String>(
        Arrays.asList(INITIAL_LIST)));
    changeCount = 0;
  }

  /**
   * Test getting the size of the model
   */
  public void testGetSize() {
    assertEquals(INITIAL_LIST.length, model.getSize());
  }

  /**
   * Test getting a specific object in the list
   */
  public void testGetElementAt() {
    assertEquals(INITIAL_LIST[2], model.getElementAt(2));
  }

  /**
   * Test adding a listener to the model
   */
  public void testAddListDataListener() {
    model.addListDataListener(this);
    model.moveDown(0);
    assertEquals(1, changeCount);
  }

  /**
   * Test removing a listener from the model
   */
  public void testRemoveListDataListener() {
    model.addListDataListener(this);
    model.moveDown(0);
    model.removeListDataListener(this);
    model.moveDown(1);
    assertEquals(1, changeCount);
  }

  /**
   * Test moving an item up in the list
   */
  public void testMoveUp() {
    model.moveUp(1);
    model.moveUp(3);

    assertEquals(INITIAL_LIST[0], model.getElementAt(1));
    assertEquals(INITIAL_LIST[1], model.getElementAt(0));
    assertEquals(INITIAL_LIST[2], model.getElementAt(3));
    assertEquals(INITIAL_LIST[3], model.getElementAt(2));
  }

  /**
   * Test moving an item down in the list
   */
  public void testMoveDown() {
    model.moveDown(0);
    model.moveDown(1);

    assertEquals(INITIAL_LIST[0], model.getElementAt(2));
    assertEquals(INITIAL_LIST[1], model.getElementAt(0));
    assertEquals(INITIAL_LIST[2], model.getElementAt(1));
    assertEquals(INITIAL_LIST[3], model.getElementAt(3));
  }

  /**
   * Get getting the complete list
   */
  public void testGetList() {
    final List<String> list = model.getList();

    assertEquals(INITIAL_LIST[0], list.get(0));
    assertEquals(INITIAL_LIST[1], list.get(1));
    assertEquals(INITIAL_LIST[2], list.get(2));
    assertEquals(INITIAL_LIST[3], list.get(3));
  }

  @Override
  public void contentsChanged(ListDataEvent e) {
    changeCount++;
  }

  @Override
  public void intervalAdded(ListDataEvent e) {
  }

  @Override
  public void intervalRemoved(ListDataEvent e) {
  }
}
