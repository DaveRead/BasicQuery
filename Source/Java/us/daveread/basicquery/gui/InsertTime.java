package us.daveread.basicquery.gui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: Insert time
 * </p>
 * <p>
 * Description: Places the count of elapsed seconds into a JLabel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004, David Read
 * </p>
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * </p>
 * <p>
 * </p>
 * 
 * @author David Read
 */

public class InsertTime implements Runnable {
  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(InsertTime.class);
  
  /**
   * Number of miiliseconds per second
   */
  private static final int MILLISECONDS_PER_SECOND = 1000;

  /**
   * Number of seconds per minute
   */
  private static final int SECONDS_PER_MINUTE = 60;

  /**
   * Number of minutes per hour
   */
  private static final int MINUTES_PER_HOUR = 60;

  /**
   * The component backing this instance
   */
  private JLabel label;

  /**
   * The starting time offset (e.g. epoch for this instance)
   */
  private long epoch;

  /**
   * Time to delay between updates of the label
   */
  private int msDelay;

  /**
   * Format for the label to display its value
   */
  private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("00");

  /**
   * Create a new InsertTime instance associated with a JLabel.
   * 
   * @param aComp
   *          The JLabel that this instance will update
   * @param aMSZeroTime
   *          The time considered as the start time.
   * @param aMSDelay
   *          The number of milliseconds between updates of the time value.
   */
  public InsertTime(JLabel aComp, long aMSZeroTime, int aMSDelay) {
    label = aComp;
    epoch = aMSZeroTime;
    msDelay = aMSDelay;
  }

  /**
   * Calculate the number of hours represented by the number of milliseconds
   * provided.
   * 
   * @param ms
   *          Milliseconds
   * 
   * @return The number of hours represented by the milliseconds parameter
   */
  private int hours(long ms) {
    return (int) (ms / (MILLISECONDS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR));
  }

  /**
   * Calculate the number of minutes represented by the number of milliseconds
   * provided.
   * 
   * @param ms
   *          Milliseconds
   * 
   * @return The number of minutes represented by the milliseconds parameter
   */
  private int minutes(long ms) {
    return (int) (ms / (MILLISECONDS_PER_SECOND * SECONDS_PER_MINUTE))
        % SECONDS_PER_MINUTE;
  }

  /**
   * Calculate the number of seconds represented by the number of milliseconds
   * provided.
   * 
   * @param ms
   *          Milliseconds
   * 
   * @return The number of seconds represented by the milliseconds parameter
   */
  private int seconds(long ms) {
    return (int) (ms / MILLISECONDS_PER_SECOND) % SECONDS_PER_MINUTE;
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
        final long ms = new Date().getTime() - epoch;
        label.setText(NUMBER_FORMAT.format(hours(ms)) + ":"
            + NUMBER_FORMAT.format(minutes(ms)) + ":"
            + NUMBER_FORMAT.format(seconds(ms)));
        // component.setText("" + (new Date().getTime() - msZeroTime) / 1000);
        try {
          Thread.sleep(msDelay);
        } catch (InterruptedException ie) {
          keepGoing = false;
        }
      }
    } catch (Throwable any) {
        LOGGER.warn("Error maintaining the timer", any);
    } finally {
      label.setText("");
    }
  }
}
