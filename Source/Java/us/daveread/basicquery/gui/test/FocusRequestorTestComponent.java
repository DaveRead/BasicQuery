package us.daveread.basicquery.gui.test;

import java.awt.Component;

/**
 * <p>Title: The backing component for testing the FocusRequestor class</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006-2014</p>
 *
 * <p>Company: </p>
 *
 * @author David Read
 */
public class FocusRequestorTestComponent extends Component {
  /**
   * Serial UID
   */
  private static final long serialVersionUID = -5874312276163251058L;
  
  /**
   * Whether or not focus has been requested
   */
  private boolean focusRequested;

  /**
   * Construct the instance, default focus requested to be false
   */
  public FocusRequestorTestComponent() {
    focusRequested = false;
  }

  /**
   * Request focus for this component
   */
  public void requestFocus() {
    focusRequested = true;
  }

  /**
   * Check whether focus was requested
   * 
   * @return True if focus was requested
   */
  public boolean isFocusRequested() {
    return focusRequested;
  }
}
