package us.daveread.basicquery.queries;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Maintain the query history and current location in that history.
 * 
 * @author David Read
 */
public class QueryHistory {
  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(QueryHistory.class);

  /**
   * Maximum size of the query history list
   */
  private static final int MAXIMUM_HISTORY_ENTRIES = 1000;

  /**
   * Singleton instance
   */
  private static QueryHistory instance = new QueryHistory();

  /**
   * Collection of historical queries
   */
  private final List<QueryInfo> queryHistoryList = new ArrayList<QueryInfo>();

  /**
   * Current position in the history list
   */
  private int currentPosition = -1;

  /**
   * Private constructor for Singleton
   */
  private QueryHistory() {

  }

  /**
   * Get the instance
   * 
   * @return The instance
   */
  public static final QueryHistory getInstance() {
    return instance;
  }

  /**
   * Places the query at end of list and changes current position to point to
   * that query. If the query is at the same position as the current query in
   * the history, then only the result set and connection URL are replaced.
   * 
   * 
   * @param query
   *          The query that is to be added to the history list
   */
  public void addQuery(QueryInfo query) {
    if (getNumberOfQueries() > 0
        && query.getSQLIndex() != getCurrentQueryInfo().getSQLIndex()) {
      truncateQueryHistory();
    }

    appendQuery(query);
  }

  /**
   * Delete the query at the SQL index position given. This is NOT the position
   * in the history list, it is the SQL index position. Any queries in the
   * history list with a matching SQL index will be removed and SQL indexes
   * beyond the removed index will be decremented by one so that they point to
   * the correct position in the SQL list.
   * 
   * @param index
   *          The SQL index of the query being deleted
   */
  public void deleteQueryAtIndex(int index) {
    int position = 0;
    int histPosition;

    while (position < queryHistoryList.size()) {
      if (queryHistoryList.get(position).getSQLIndex() == index) {
        queryHistoryList.remove(position);
        if (getHistoryPosition() >= position) {
          --currentPosition;
        }
      } else {
        if ((histPosition = queryHistoryList.get(position).getSQLIndex()) > index) {
          queryHistoryList.get(position).setSQLIndex(histPosition - 1);
        }

        ++position;
      }
    }
  }

  /**
   * Remove queries that follow the currently selected query in the history
   * list.
   */
  private void truncateQueryHistory() {
    if (getHistoryPosition() > -1) {
      while (queryHistoryList.size() > getHistoryPosition() + 1) {
        LOGGER.debug("Removed item at=" + (getHistoryPosition() + 1));
        queryHistoryList.remove(getHistoryPosition() + 1);
      }
    }

    LOGGER.debug("historyPosition=" + getHistoryPosition()
        + " historyQueries.size="
        + getNumberOfQueries());
  }

  /**
   * Append a query at the end of the history list. If the SQL index matches the
   * current history position's query's SQL index then only the results and
   * connection URL are updated in the query history.
   * 
   * @param query
   *          The query to be inserted at the end of the query history list
   */
  private void appendQuery(QueryInfo query) {
    if (getNumberOfQueries() == 0
        || query.getSQLIndex() != getCurrentQueryInfo().getSQLIndex()) {
      queryHistoryList.add(query);
      setHistoryPosition(getNumberOfQueries() - 1);
    } else if (getNumberOfQueries() > 0
        && query.getSQLIndex() == getCurrentQueryInfo().getSQLIndex()) {
      getCurrentQueryInfo().setResults(query.getResults());
      getCurrentQueryInfo().setURLIndex(query.getURLIndex());
    }

    enforceSize();
  }

  /**
   * Ensure that the size of the history list does not go beyond the defined
   * maximum size
   */
  private void enforceSize() {
    if (queryHistoryList.size() > MAXIMUM_HISTORY_ENTRIES) {
      while (queryHistoryList.size() > MAXIMUM_HISTORY_ENTRIES) {
        queryHistoryList.remove(0);
        --currentPosition;
      }

      /*
       * This "can't" happen, but is here in case there is a bug that allows the
       * history list to grow beyond the max size
       */
      if (getHistoryPosition() < 0) {
        currentPosition = 0;
      }
    }
  }

  /**
   * Set the current history position
   * 
   * @param position
   *          The current history position
   */
  private void setHistoryPosition(int position) {
    currentPosition = position;
  }

  /**
   * Get the number of queries in the history list
   * 
   * @return The number of queries in the history list
   */
  public int getNumberOfQueries() {
    return queryHistoryList.size();
  }

  /**
   * Get the position of the current query in the history list
   * 
   * @return The position of the current query in the history list
   */
  private int getHistoryPosition() {
    return currentPosition;
  }

  /**
   * Get the query info for the current query position in the history list. If
   * there is no query history and exception is thrown.
   * 
   * @return The current query info from the history list
   */
  public QueryInfo getCurrentQueryInfo() {
    if (getHistoryPosition() > -1) {
      return queryHistoryList.get(getHistoryPosition());
    } else {
      throw new IllegalAccessError("No queries in the history list");
    }
  }

  /**
   * Move back one position in the history list. If the list is empty or already
   * at the beginning an exception is thrown
   * 
   * @see #hasPrevious()
   */
  public void moveBackward() {
    if (getHistoryPosition() > 0) {
      --currentPosition;
    } else {
      throw new IllegalAccessError("No previous queries in the history list");
    }
  }

  /**
   * Move forward on position in the history list. If the list is empty of
   * already at the end an exception is thrown
   * 
   * @see #hasNext()
   */
  public void moveForward() {
    if (getHistoryPosition() + 1 < getNumberOfQueries()) {
      ++currentPosition;
    } else {
      throw new IllegalAccessError("No more queries in the history list");
    }
  }

  /**
   * Check whether there is a query in the history list prior to the current
   * history position.
   * 
   * @see #moveBackward()
   * 
   * @return True if there is a query prior to the current history position
   */
  public boolean hasPrevious() {
    return getHistoryPosition() > 0;
  }

  /**
   * Check whether there is a query in the history list after to the current
   * history position.
   * 
   * @see #moveForward()
   * 
   * @return True if there is a query after the current history position
   */
  public boolean hasNext() {
    return getHistoryPosition() + 1 < getNumberOfQueries()
        && getNumberOfQueries() > 0;
  }

  /**
   * Remove all queries from history
   */
  public void clearAllQueries() {
    queryHistoryList.clear();
    currentPosition = -1;
  }
}
