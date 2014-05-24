package us.daveread.basicquery.test;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.event.ListDataListener;

import junit.framework.*;

import us.daveread.basicquery.ReorderListModel;
import javax.swing.event.ListDataEvent;

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
 * @version $Id: ReorderListModelTest.java,v 1.1 2006/05/01 20:19:31 daveread Exp $
 */
public class ReorderListModelTest extends TestCase implements ListDataListener {
  private ReorderListModel model;
  private int changeCount;

  private String[] initialList = {
      "First", "Second", "Third", "Fourth"
  };

  public ReorderListModelTest() {
  }

  public void setUp() {
    model = new ReorderListModel(new ArrayList(Arrays.asList(initialList)));
    changeCount = 0;
  }

  public void testGetSize() {
    assertEquals(initialList.length, model.getSize());
  }

  public void testGetElementAt() {
    assertEquals(initialList[2], (String)model.getElementAt(2));
  }

  public void testAddListDataListener() {
    model.addListDataListener(this);
    model.moveDown(0);
    assertEquals(1, changeCount);
  }

  public void testRemoveListDataListener() {
    model.addListDataListener(this);
    model.moveDown(0);
    model.removeListDataListener(this);
    model.moveDown(1);
    assertEquals(1, changeCount);
  }

  public void testMoveUp() {
    model.moveUp(1);
    model.moveUp(3);

    assertEquals(initialList[0], (String)model.getElementAt(1));
    assertEquals(initialList[1], (String)model.getElementAt(0));
    assertEquals(initialList[2], (String)model.getElementAt(3));
    assertEquals(initialList[3], (String)model.getElementAt(2));
  }

  public void testMoveDown() {
    model.moveDown(0);
    model.moveDown(1);

    assertEquals(initialList[0], (String)model.getElementAt(2));
    assertEquals(initialList[1], (String)model.getElementAt(0));
    assertEquals(initialList[2], (String)model.getElementAt(1));
    assertEquals(initialList[3], (String)model.getElementAt(3));
  }

  public void testGetList() {
    List list = model.getList();

    assertEquals(initialList[0], (String)list.get(0));
    assertEquals(initialList[1], (String)list.get(1));
    assertEquals(initialList[2], (String)list.get(2));
    assertEquals(initialList[3], (String)list.get(3));
  }

  public void contentsChanged(ListDataEvent e) {
    changeCount++;
  }

  public void intervalAdded(ListDataEvent e) {
  }

  public void intervalRemoved(ListDataEvent e) {
  }

}
