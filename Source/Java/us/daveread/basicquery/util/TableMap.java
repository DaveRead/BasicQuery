package us.daveread.basicquery.util;

import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * In a chain of data manipulators some behaviour is common. TableMap
 * provides most of this behavour and can be subclassed by filters
 * that only need to override a handful of specific methods. TableMap
 * implements TableModel by routing all requests to its model, and
 * TableModelListener by routing all events to its listeners. Inserting
 * a TableMap which has not been subclassed into a chain of table filters
 * should have no effect.
 * 
 * <p>
 * Original version 1.4 12/17/97
 * </p>
 * 
 * @author Philip Milne
 */

public class TableMap extends AbstractTableModel implements TableModelListener {
  /**
   * Serial UID
   */
  private static final long serialVersionUID = -5521533255340043234L;
  
  /**
   * The table model backing this table map
   */
  private TableModel model;

  /**
   * No operation
   */
  public TableMap() {
  }

  /**
   * Implements TableModel by forwarding all messages
   * to the model
   * 
   * @return The table model
   */
  public TableModel getModel() {
    return model;
  }

  /**
   * Sets the table model for this model and adds a listener to the model that's
   * notified each time a change occurs
   * 
   * @param pModel
   *          The TableModel
   */
  public void setModel(TableModel pModel) {
    this.model = pModel;
    model.addTableModelListener(this);
  }

  /**
   * Remove the model from this instance, removing this instance as a listener
   * as well
   * 
   * TODO Either don't pass the model or verify that it is the current model
   * backing this instance
   * 
   * @param pModel
   *          The TableModel
   */
  public void removeModel(TableModel pModel) {
    pModel.removeTableModelListener(this);
    this.model = null;
  }

  /**
   * Returns the value of the cell at aRow and aColumn
   * 
   * @param aRow
   *          The row whose value is queried
   * @param aColumn
   *          The column whose value is queried
   * 
   * @return The object at the specified cell
   */
  public Object getValueAt(int aRow, int aColumn) {
    return model.getValueAt(aRow, aColumn);
  }

  /**
   * Sets the value in the cell at aRow and aColumn to aValue
   * 
   * @param aValue
   *          The new value
   * @param aRow
   *          The row whose value is changed
   * @param aColumn
   *          The column whose value is changed
   * 
   */
  public void setValueAt(Object aValue, int aRow, int aColumn) {
    model.setValueAt(aValue, aRow, aColumn);
  }

  /**
   * Returns the number of rows in the model
   * 
   * @return The number of rows
   */
  public int getRowCount() {
    return (model == null) ? 0 : model.getRowCount();
  }

  /**
   * Returns the number of columns in the model
   * 
   * @return The number of columns
   */
  public int getColumnCount() {
    return (model == null) ? 0 : model.getColumnCount();
  }

  /**
   * Returns the column name at the column index
   * 
   * @param aColumn
   *          The index of the column
   * 
   * @return The object with the name of the column
   */
  public String getColumnName(int aColumn) {
    return model.getColumnName(aColumn);
  }

  /**
   * Returns the most specific superclass for all the cell values
   * in the column
   * 
   * @param aColumn
   *          The index of the column
   * 
   * @return The common ancestor class of the object values
   *         in the model
   */
  public Class<?> getColumnClass(int aColumn) {
    return model.getColumnClass(aColumn);
  }

  /**
   * Returns true if the cell at row and column is editable
   * If the cell is not editable then the setValueAt will not
   * change the value
   * 
   * @param row
   *          The row whose value is to be queried
   * @param column
   *          The column whose value is to be queried
   * 
   * @return The object with the true value if the cell is editable
   */
  public boolean isCellEditable(int row, int column) {
    return model.isCellEditable(row, column);
  }

  /**
   * To fulfill the TableModelListener interface
   * By default forward all events to all the listeners
   * The notification that tells the listeners the exact
   * range of cells,rows,or columns that changed
   * 
   * @param e
   *          The table model event object that represents the event-fired.
   */
  public void tableChanged(TableModelEvent e) {
    fireTableChanged(e);
  }
}
