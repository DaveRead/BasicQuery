package us.daveread.basicquery.gui.test;

import junit.framework.*;

import us.daveread.basicquery.gui.FocusRequestor;

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
 * @version $Id: FocusRequestorTest.java,v 1.2 2006/05/04 03:41:09 daveread Exp $
 */
public class FocusRequestorTest extends TestCase {
  FocusRequestorTestComponent comp;

  public FocusRequestorTest() {
  }

  public void setUp() {
    comp = new FocusRequestorTestComponent();
  }

  public void testFocusRequestor() {
    new FocusRequestor(comp);

    try {
      Thread.sleep(1000);
    }
    catch (InterruptedException intExc) {
    }

    assertTrue(comp.isFocusRequested());
  }
}
