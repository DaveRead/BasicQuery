package us.daveread.basicquery.gui.test;

import java.awt.*;

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
 * @version $Id: FocusRequestorTestComponent.java,v 1.1 2006/05/01 20:18:54 daveread Exp $
 */
public class FocusRequestorTestComponent extends Component {
  private boolean focusRequested;

  public FocusRequestorTestComponent() {
    focusRequested = false;
  }

  public void requestFocus() {
    focusRequested = true;
  }

  public boolean isFocusRequested() {
    return focusRequested;
  }
}
