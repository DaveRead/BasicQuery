package us.daveread.basicquery.gui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.JLabel;

/**
 * <p>Title: Insert time</p>
 * <p>Description: Places the count of elapsed seconds into a JLabel</p>
 * <p>Copyright: Copyright (c) 2004, David Read</p>
 * <p>This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.</p>
 * <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.</p>
 * <p>You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA</p>
 * <p></p>
 * @author David Read
 * @version $Id: InsertTime.java,v 1.3 2006/05/03 03:58:25 daveread Exp $
 */

public class InsertTime implements Runnable {
  private JLabel component;
  private long msZeroTime;
  private int msDelay;

  static NumberFormat numberFormat = new DecimalFormat("00");

  /**
   * Create a new InsertTime instance associated with a JLabel.
   *
   * @param aComp The JLabel that this instance will update
   * @param aMSZeroTime The time considered as the start time.
   * @param aMSDelay The number of milliseconds between updates of the time value.
   */
  public InsertTime(JLabel aComp, long aMSZeroTime, int aMSDelay) {
    component = aComp;
    msZeroTime = aMSZeroTime;
    msDelay = aMSDelay;
  }

  private int hours(long ms) {
	  return (int)(ms / (1000 * 60 * 60));
  }
  
  private int minutes(long ms) {
	  return (int)(ms / (1000 * 60)) % 60;
  }
  
  private int seconds(long ms) {
	  return (int)(ms / 1000) % 60;
  }
  /**
   * Track the elapsed seconds, updating the JLabel until an interrupt
   * is received.
   */
  public void run() {
    boolean keepGoing;

    keepGoing = true;

    try {
      while (keepGoing && !Thread.currentThread().isInterrupted()) {
    	long ms =  new Date().getTime() - msZeroTime;
    	component.setText(numberFormat.format(hours(ms)) + ":" 
    			+ numberFormat.format(minutes(ms)) + ":" 
    			+ numberFormat.format(seconds(ms)));
        //component.setText("" + (new Date().getTime() - msZeroTime) / 1000);
        try {
          Thread.sleep(msDelay);
        }
        catch (InterruptedException ie) {
          keepGoing = false;
        }
      }
    }
    catch (Throwable any) {

    }
    finally {
      component.setText("");
    }
  }
}
