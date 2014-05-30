package us.daveread.basicquery.gui;

import java.awt.Component;

import javax.swing.SwingUtilities;

/**
 * Title:        Focus Requestor
 * Description:  A class to place focus requests on the event thread
 * Copyright:    Copyright (c) 2003-2014
 * Company:
 * 
 * @author David Read
 */
public class FocusRequestor implements Runnable {

  /**
   * The component backing this instance
   */
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
