package us.daveread.basicquery.util;

import java.util.*;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseListener;

import org.apache.log4j.Logger;

/**
 * A sorter for TableModels. The sorter has a model (conforming to TableModel)
 * and itself implements TableModel. TableSorter does not store or copy
 * the data in the TableModel, instead it maintains an array of
 * integers which it keeps the same size as the number of rows in its
 * model. When the model changes it notifies the sorter that something
 * has changed eg. "rowsAdded" so that its internal array of integers
 * can be reallocated. As requests are made of the sorter (like
 * getValueAt(row, col) it redirects them to its model via the mapping
 * array. That way the TableSorter appears to hold another copy of the table
 * with the rows in a different order. The sorting algorithm used is stable
 * which means that it does not move around rows when its comparison
 * function returns 0 to denote that they are equivalent.
 *
 * <p>Original version 1.5 12/17/97</p>
 *
 * @version $Id: TableSorter.java,v 1.4 2006/05/03 03:54:36 daveread Exp $
 * @author Philip Milne
 */

public class TableSorter extends TableMap {
  public final static String ID =
      "$Id: TableSorter.java,v 1.4 2006/05/03 03:54:36 daveread Exp $";

  private final static Logger logger = Logger.getLogger(TableSorter.class);

  private int indexes[];
  private Vector sortingColumns = new Vector();
  private boolean ascending = true;
  private int compares;
  private List myMouseListeners = new ArrayList(); // DSR - So I can remove them

  /**
   * Constructs a Table Sorter
   * Creates an integer array for consistency
   */
  public TableSorter() {
    indexes = new int[0];
  }

  /**
   * Constructs a  Table Sorter
   *
   * @param model The Table Model Object
   */
  public TableSorter(TableModel model) {
    setModel(model);
  }

  /**
   * Sets the data model for this table to model and
   * registers with it for listener notifications from
   * the new data model.
   *
   * @param model The TableModel Object
   */
  public void setModel(TableModel model) {
    super.setModel(model);
    reallocateIndexes();
  }

  /**
   * Remove the Model model
   *
   * @param model The Table Model object to be removed
   */
  public void removeModel(TableModel model) {
    super.removeModel(model);
  }

  /**
   * Compares the values of two rows in a column
   *
   * @param row1    row index
   * @param row2    row index
   * @param column  column index
   *
   * @return int    Returns values -1,0,1 depending on the sort results
   */

  /*    public int compareRowsByMultipleColumns(int row1, int row2, int columns[]) {
          int retVal;

          retVal = 1;

   for (int column = 0;retVal == 0 && column < columns.length;++column) {
              retVal = compareRowsByColumn(row1, row2, columns[column]);
          }

          return retVal;
      } */

  private int compareRowsByColumn(int row1, int row2, int column) {
    Class type = model.getColumnClass(column);
    TableModel data = model;

    // Check for nulls.

    Object o1 = data.getValueAt(row1, column);
    Object o2 = data.getValueAt(row2, column);

    // If both values are null, return 0.
    if (o1 == null && o2 == null) {
      return 0;
    } else if (o1 == null) { // Define null less than everything.
      return -1;
    } else if (o2 == null) {
      return 1;
    }

    /*
     * We copy all returned values from the getValue call in case
     * an optimised model is reusing one object to return many
     * values.  The Number subclasses in the JDK are immutable and
     * so will not be used in this way but other subclasses of
     * Number might want to do this to save space and avoid
     * unnecessary heap allocation.
     */

//        if (type.getSuperclass() == java.lang.Number.class) {
    if (o1 instanceof java.lang.Number) {
      Number n1 = (Number)data.getValueAt(row1, column);
      double d1 = n1.doubleValue();
      Number n2 = (Number)data.getValueAt(row2, column);
      double d2 = n2.doubleValue();

      if (d1 < d2) {
        return -1;
      } else if (d1 > d2) {
        return 1;
      } else {
        return 0;
      }
//        } else if (type == java.util.Date.class) {
    } else if (o1 instanceof java.util.Date) {
      Date d1 = (Date)data.getValueAt(row1, column);
      long n1 = d1.getTime();
      Date d2 = (Date)data.getValueAt(row2, column);
      long n2 = d2.getTime();

      if (n1 < n2) {
        return -1;
      } else if (n1 > n2) {
        return 1;
      } else {
        return 0;
      }
//        } else if (type == String.class) {
    } else if (o1 instanceof java.lang.String) {
      String s1 = (String)data.getValueAt(row1, column);
      String s2 = (String)data.getValueAt(row2, column);
      int result = s1.compareTo(s2);

      if (result < 0) {
        return -1;
      } else if (result > 0) {
        return 1;
      } else {
        return 0;
      }
//        } else if (type == Boolean.class) {
    } else if (o1 instanceof java.lang.Boolean) {
      Boolean bool1 = (Boolean)data.getValueAt(row1, column);
      boolean b1 = bool1.booleanValue();
      Boolean bool2 = (Boolean)data.getValueAt(row2, column);
      boolean b2 = bool2.booleanValue();

      if (b1 == b2) {
        return 0;
      } else if (b1) { // Define false < true
        return 1;
      } else {
        return -1;
      }
    } else {
      Object v1 = data.getValueAt(row1, column);
      String s1 = v1.toString();
      Object v2 = data.getValueAt(row2, column);
      String s2 = v2.toString();
      int result = s1.compareTo(s2);

      if (result < 0) {
        return -1;
      } else if (result > 0) {
        return 1;
      } else {
        return 0;
      }
    }
  }

  /**
   * Compares values in row1 and row2
   *
   * @param row1 row index
   * @param row2 row index
   *
   * @return int
   */
  private int compare(int row1, int row2) {
    compares++;
    for (int level = 0; level < sortingColumns.size(); level++) {
      Integer column = (Integer)sortingColumns.elementAt(level);
      int result = compareRowsByColumn(row1, row2, column.intValue());
      if (result != 0) {
        return ascending ? result : -result;
      }
    }
    return 0;
  }

  /**
   * Set up a new mapping array of indexes with the right number of
   * elements for the new data model
   */
  private void reallocateIndexes() {
    int rowCount = model.getRowCount();

    indexes = new int[rowCount];

    // Initialise with the identity mapping.
    for (int row = 0; row < rowCount; row++) {
      indexes[row] = row;
    }
  }

  /**
   * The tableChanged Method is called when any change is made to the table
   *
   * @param e The Table ModelEvent
   */
  public void tableChanged(TableModelEvent e) {
    logger.debug("tableChanged");

    reallocateIndexes();

    super.tableChanged(e);
  }

  /**
   * Checks if there has been any uninformed change in the model
   */
  private void checkModel() {
    if (indexes.length != model.getRowCount()) {
      logger.warn("Sorter not informed of a change in model.");
    }
  }

  /**
   * Performs sort operation on the sender
   *
   * @param sender Object that is to sorted
   */
  private void sort(Object sender) {
    checkModel();

    compares = 0;
    // n2sort();
    // qsort(0, indexes.length-1);
    shuttlesort((int[])indexes.clone(), indexes, 0, indexes.length);
    logger.debug("Compares: " + compares);
  }

  /**
   * This is a home-grown implementation which we have not had time
   * to research - it may perform poorly in some circumstances. It
   * requires twice the space of an in-place algorithm and makes
   * NlogN assigments shuttling the values between the two
   * arrays. The number of compares appears to vary between N-1 and
   * NlogN depending on the initial order but the main reason for
   * using it here is that, unlike qsort, it is stable.
   *
   * @param from[]   array that has to be sorted
   * @param to[]     array that has the sorted result
   * @param low      Starting row index 1
   * @param high     Maximum number of rows
   *
   */

  private void shuttlesort(int from[], int to[], int low, int high) {
    if (high - low < 2) {
      return;
    }
    int middle = (low + high) / 2;
    shuttlesort(to, from, low, middle);
    shuttlesort(to, from, middle, high);

    int p = low;
    int q = middle;

    /* This is an optional short-cut; at each recursive call,
             check to see if the elements in this subset are already
             ordered.  If so, no further comparisons are needed; the
             sub-array can just be copied.  The array must be copied rather
             than assigned otherwise sister calls in the recursion might
             get out of sinc.  When the number of elements is three they
             are partitioned so that the first set, [low, mid), has one
             element and and the second, [mid, high), has two. We skip the
             optimisation when the number of elements is three or less as
             the first compare in the normal merge will produce the same
             sequence of steps. This optimisation seems to be worthwhile
             for partially ordered lists but some analysis is needed to
             find out how the performance drops to Nlog(N) as the initial
             order diminishes - it may drop very quickly.  */

    if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0) {
      for (int i = low; i < high; i++) {
        to[i] = from[i];
      }
      return;
    }

    // A normal merge.

    for (int i = low; i < high; i++) {
      if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
        to[i] = from[p++];
      } else {
        to[i] = from[q++];
      }
    }
  }

  // The mapping only affects the contents of the data rows.
  // Pass all requests to these rows through the mapping array: "indexes".

  /**
   * Gets the value of the model at indexes[aRow] and aColumn
   *
   * @param aRow         row index
   * @param aColumn      column index
   *
   * @return model       The tableModel with its value at indexes[aRow] and
   *                     aColumn
   */
  public Object getValueAt(int aRow, int aColumn) {
    checkModel();

    logger.debug("aRow[" + aRow + "] aColumn[" + aColumn + "] indexes[aRow][" +
        indexes[aRow] + "]");

    return model.getValueAt(indexes[aRow], aColumn);
  }

  /**
   *
   * Sets the value of the model at aRow and aColumn to aValue
   *
   * @param aValue
   * @param aRow      row index
   * @param aColumn   column index
   */
  public void setValueAt(Object aValue, int aRow, int aColumn) {
    checkModel();
    model.setValueAt(aValue, indexes[aRow], aColumn);
  }

  /**
   * Sorts the column in an ascending order
   *
   * @param column      column index
   * @param ascending   the column is sorted in an ascending order
   */
  private void sortByColumn(int column, boolean ascending) {
    int columns[];

    columns = new int[1];
    columns[0] = column;
    sortByColumns(columns, ascending);

    /*        this.ascending = ascending;
            sortingColumns.removeAllElements();
            sortingColumns.addElement(new Integer(column));
            sort(this);
            super.tableChanged(new TableModelEvent(this)); */
  }

  /**
   * Calls the method sortByColumns(int,boolean) after setting the sorting
   * option as ascending
   * @param columns   The column values
   */
  public void sortByColumns(int columns[]) {
    sortByColumns(columns, true);
  }

  /**
   *
   * @param columns    The column to be sorted
   * @param ascending  A boolean value that determines whether the column
   *                   is sorted in an ascending or descending order
   */
  public void sortByColumns(int columns[], boolean ascending) {
    this.ascending = ascending;
    sortingColumns.removeAllElements();

    for (int column = 0; column < columns.length; ++column) {
      sortingColumns.addElement(new Integer(columns[column]));
    }
    sort(this);
    super.tableChanged(new TableModelEvent(this));
  }

  /**
   * Adds a mouse listener to the Table to trigger a table sort
   * when a column heading is clicked in the JTable.
   *
   * @param table The JTabel object
   */

  public void addMouseListenerToHeaderInTable(JTable table) {
    final TableSorter sorter = this;
    final JTable tableView = table;
    tableView.setColumnSelectionAllowed(false);
    MouseAdapter listMouseListener = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        TableColumnModel columnModel = tableView.getColumnModel();
        int viewColumn = columnModel.getColumnIndexAtX(e.getX());
        int column = tableView.convertColumnIndexToModel(viewColumn);
        if (e.getClickCount() == 1 && column != -1) {
          logger.info("Sorting ...");
          int shiftPressed = e.getModifiers() & InputEvent.SHIFT_MASK;
          boolean ascending = (shiftPressed == 0);
          sorter.sortByColumn(column, ascending);
        }
      }
    };
    JTableHeader th = tableView.getTableHeader();
    th.addMouseListener(listMouseListener);
    myMouseListeners.add(listMouseListener);
  }

  /**
   * Need ability to remove mouselistener
   * Otherwise creation of new sorter leaks memory
   *
   * @param table The JTable object
   */
  public void removeMouseListenerFromHeaderInTable(JTable table) {
    MouseListener mouseListeners[];

    mouseListeners = table.getTableHeader().getMouseListeners();

    for (int tableLsnr = 0; tableLsnr < mouseListeners.length; ++tableLsnr) {
      logger.debug("Checking Registered Listener:" + mouseListeners[tableLsnr]);
      for (int myLsnr = 0; myLsnr < myMouseListeners.size(); ++myLsnr) {
        logger.debug("Matching to Local Listener:" +
            myMouseListeners.get(myLsnr));
        if (mouseListeners[tableLsnr] == myMouseListeners.get(myLsnr)) {
          table.getTableHeader().removeMouseListener(mouseListeners[tableLsnr]);
          myMouseListeners.remove(myLsnr);
          logger.debug("Removed Mouse Listener:" + mouseListeners[tableLsnr]);
          break;
        }
      }
    }
  }
}
