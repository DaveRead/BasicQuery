package us.daveread.basicquery;

import java.util.List;
import java.util.ArrayList;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

/**
 * <p>Title: ReorderListModel</p>
 * <p>Description: ListModel with ability to reorder the list</p>
 * <p>Copyright: Copyright (c) 2004-2014, David Read</p>
 * <p>This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.</p>
 *  <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.</p>
 *  <p>You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA</p>
 * <p></p>
 * @author David Read
 * 
 * @param <T> Type of data being reordered
 */

public class ReorderListModel<T> implements ListModel {
  /**
   * List of objects that can be reordered
   */
  private List<T> data;
  
  /**
   * Listeners to notify if the list is reordered
   */
  private List<ListDataListener> listeners;

  /**
   * Constructor takes a List prepopulated with information to be presented in the GUI list.
   *
   * @param theData List of objects to be represented in the GUI list.
   */
  public ReorderListModel(List<T> theData) {
    listeners = new ArrayList<ListDataListener>();
    data = theData;
  }

  /**
   * Get the number of items in the list.
   * @return int The number of items in the list.
   */
  public int getSize() {
    return data.size();
  }

  /**
   * Get the object at the given position.
   * @param index int The index in the list containing the object to be returned.
   * @return Object
   */
  public T getElementAt(int index) {
    return data.get(index);
  }

  /**
   * Add a ListDataListener
   * @param l ListDataListener listening for events on this instance
   */
  public void addListDataListener(ListDataListener l) {
    listeners.add(l);
  }

  /**
   * Remove a ListDataListener
   * @param l ListDataListener The listener to be removed from the list of listeners
   * being notified when the list changes.
   */
  public void removeListDataListener(ListDataListener l) {
    listeners.remove(l);
  }

  /**
   * Move the object at the given position to the next lower position
   * (previous index value) in the list.  If the item was at index 2 it should
   * be moved to index 1 and the item at index 1 would now be at index 2.
   * @param index int The index of the object to be moved up the list.
   */
  public void moveUp(int index) {
    if (index > 0) {
      data.add(index - 1, data.remove(index));
      notifyListeners();
    }
  }

  /**
   * Move the object at the given position to the next higher position
   * (next index value) in the list.  If the item was at index 2 it should
   * be moved to index 3 and the item at index 3 would now be at index 2.
   * @param index int The index of the object to be moved down the list.
   */
  public void moveDown(int index) {
    if (index < data.size() - 1) {
      data.add(index + 1, data.remove(index));
      notifyListeners();
    }
  }

  /**
   * Get the list which backs this model.
   * @return List The list backing the model.
   */
  public List<T> getList() {
    return data;
  }

  /**
   * Notify all registered listeners that the list has changed.
   */
  private void notifyListeners() {
    ListDataEvent event;

    event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 0);

    for (int loop = 0; loop < listeners.size(); ++loop) {
      listeners.get(loop).contentsChanged(event);
    }
  }
}
