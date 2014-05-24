package us.daveread.basicquery.util;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ListTableModel extends AbstractTableModel {
  private List columnNames;
  private List rowData;

  public ListTableModel() {
    rowData = new ArrayList();
    columnNames = new ArrayList();
  }

  public void setColumnIdentifiers(List cols) {
    columnNames = cols;
  }

  public void addColumn(String colName) {
    columnNames.add(colName);
  }

  public String getColumnName(int col) {
    return (String)columnNames.get(col);
  }

  public void addRow(List row) {
    rowData.add(row);
    fireTableRowsInserted(rowData.size() - 1, rowData.size() - 1);
  }

  public void addRowFast(List row) {
    rowData.add(row);
  }

  public void updateCompleted() {
    fireTableRowsInserted(0, rowData.size() - 1);
  }

  public int getColumnCount() {
    return columnNames.size();
  }

  public int getRowCount() {
    return rowData.size();
  }

  public Object getValueAt(int row, int col) {
    return ((List)rowData.get(row)).get(col);
  }
}
