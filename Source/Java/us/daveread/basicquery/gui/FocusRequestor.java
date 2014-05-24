package us.daveread.basicquery.gui;

import java.awt.*;
import javax.swing.*;

/**
 * Title:        Focus Requestor
 * Description:  A class to place focus requests on the event thread
 * Copyright:    Copyright (c) 2003
 * Company:
 * @author David Read
 * @version $Id: FocusRequestor.java,v 1.3 2006/05/03 03:59:28 daveread Exp $
 */

public class FocusRequestor implements Runnable {
  public final static String ID =
      "$Id: FocusRequestor.java,v 1.3 2006/05/03 03:59:28 daveread Exp $";

  private Component objcComponent;

  /**
   * Class constructor - takes in component that is to receive the input
   * focus
   *
   * @param objaComponent  The component that will receive the input focus
   */

  public FocusRequestor(Component objaComponent) {
    objcComponent = objaComponent;

    SwingUtilities.invokeLater(this);
  }

  /**
   * Requests that the component (from the constructor) gets the input focus.
   */

  public void run() {
    objcComponent.requestFocus();
  }
}
