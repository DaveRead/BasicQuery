package us.daveread.basicquery.gui.test;

import javax.swing.JLabel;
import java.util.Date;

import junit.framework.*;

import us.daveread.basicquery.gui.InsertTime;

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
 * @version $Id: InsertTimeTest.java,v 1.1 2006/05/01 20:18:54 daveread Exp $
 */
public class InsertTimeTest extends TestCase {
  private InsertTime timeDisp;
  private JLabel jlabel;

  public InsertTimeTest() {
  }

  public void setUp() {
    jlabel = new JLabel();
    timeDisp = new InsertTime(jlabel, new Date().getTime(), 500);
  }

  public void testRun() {
    Thread run;

    run = new Thread(timeDisp);
    run.start();

    try {
      Thread.sleep(1000);
    }
    catch (InterruptedException intExc) {
    }

    assertTrue(jlabel.getText().trim().length() > 0);

    run.interrupt();

    try {
      Thread.sleep(1000);
    }
    catch (InterruptedException intExc) {
    }

    assertTrue(jlabel.getText().trim().length() == 0);
  }
}
