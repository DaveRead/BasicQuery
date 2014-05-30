package us.daveread.basicquery.util;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * Ordered model for a table
 * 
 * @author David Read
 * 
 * @param <T> Data type housed in the model
 * 
 */
public class ListTableModel<T> extends AbstractTableModel {
  /**
   * Serial UID
   */
  private static final long serialVersionUID = 15744098355129546L;

  /**
   * Column names
   */
  private List<String> columnNames;

  /**
   * Row data
   */
  private List<List<T>> rowData;

  /**
   * Setup the instance with empty column and data lists
   */
  public ListTableModel() {
    rowData = new ArrayList<List<T>>();
    columnNames = new ArrayList<String>();
  }

  /**
   * Set the list of column names for the model
   * 
   * @param cols
   *          The list of column names
   */
  public void setColumnIdentifiers(List<String> cols) {
    columnNames = cols;
  }

  /**
   * Add a column name to the model
   * 
   * @param colName
   *          The column name
   */
  public void addColumn(String colName) {
    columnNames.add(colName);
  }

  /**
   * Get the column name for the requested column
   * 
   * @param col
   *          The column number
   * 
   * @return The column name
   */
  public String getColumnName(int col) {
    return columnNames.get(col);
  }

  /**
   * Add a data row to the model and notify the table that its model has changed
   * 
   * @see #addRowFast(List)
   * @param row
   *          The data row (a list of objects, one per columns)
   */
  public void addRow(List<T> row) {
    rowData.add(row);
    fireTableRowsInserted(rowData.size() - 1, rowData.size() - 1);
  }

  /**
   * Add a data row to the model witout notifying the table that the model has
   * changed
   * 
   * @see #updateCompleted()
   * 
   * @param row
   *          The data row (a list of objects, one per columns
   */
  public void addRowFast(List<T> row) {
    rowData.add(row);
  }

  /**
   * Notify the table that the model has changed.
   * 
   * @see #addRowFast(List)
   */
  public void updateCompleted() {
    fireTableRowsInserted(0, rowData.size() - 1);
  }

  /**
   * Get the number of columns in the model (based on number of column names)
   * 
   * @return The number of columns in the model
   */
  public int getColumnCount() {
    return columnNames.size();
  }

  /**
   * Get the number of data rows in the model
   * 
   * @return The number of data rows in the model
   */
  public int getRowCount() {
    return rowData.size();
  }

  /**
   * Get the value for a row and column
   * 
   * @param row
   *          The row number
   * @param col
   *          The column number
   * 
   * @return The object at that row and column location
   */
  public T getValueAt(int row, int col) {
    return rowData.get(row).get(col);
  }
}
