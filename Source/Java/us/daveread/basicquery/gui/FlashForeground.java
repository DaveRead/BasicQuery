package us.daveread.basicquery.gui;

import java.awt.Component;
import java.awt.Color;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: Flash foreground
 * </p>
 * <p>
 * Description: Eye candy modifying the foreground color of a component
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004-2014, David Read
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

public class FlashForeground implements Runnable {
  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(FlashForeground.class);

  /**
   * The component backing this instance
   */
  private Component component;

  /**
   * The color to display when the flash is "on"
   */
  private Color onColor;

  /**
   * The color to display when the flash is "off"
   */
  private Color offColor;

  /**
   * Delay time when cycling the steps between "on" and "off" colors
   */
  private int msDelay;

  /**
   * Red color value for RGB coloring when the flash is "on"
   */
  private int redOn;

  /**
   * Red color value for RGB coloring when the flash is "off"
   */
  private int redOff;

  /**
   * Green color value for RGB coloring when the flash is "on"
   */
  private int greenOn;

  /**
   * Green color value for RGB coloring when the flash is "off"
   */
  private int greenOff;

  /**
   * Blue color value for RGB coloring when the flash is "on"
   */
  private int blueOn;

  /**
   * Blue color value for RGB coloring when the flash is "off"
   */
  private int blueOff;

  /**
   * Amount of change for the red component of the RGB color at each step in the
   * coloring cycle
   */
  private int redChange;

  /**
   * Amount of change for the green component of the RGB color at each step in
   * the coloring cycle
   */
  private int greenChange;

  /**
   * Amount of change for the blue component of the RGB color at each step in
   * the coloring cycle
   */
  private int blueChange;

  /**
   * Current red value in the RGB color
   */
  private int redValue;

  /**
   * Current green value in the RGB color
   */
  private int greenValue;

  /**
   * Current blue value in the RGB color
   */
  private int blueValue;

  /**
   * The modulus value of the iteration counter for changing the red component
   * of the RGB color
   */
  private int redModulus;

  /**
   * The modulus value of the iteration counter for changing the green component
   * of the RGB color
   */
  private int greenModulus;

  /**
   * The modulus value of the iteration counter for changing the blue component
   * of the RGB color
   */
  private int blueModulus;

  /**
   * The maximum range of the iteration counter to cycle through the component
   * color ranges from "on" to "off"
   */
  private int maxChange;

  /**
   * The current value of the iteration counter cycling through the component
   * color ranges from "on" to "off"
   */
  private int currentValue;

  /**
   * The current iteration direction (1=up or -1=down). Up moves from the "off"
   * color to the "on" color.
   */
  private int direction;

  /**
   * Create a FlashForeground instance, associated with a specific component.
   * Then the instance is notified that it should "flash" it will begin changing
   * the foreground color between the "On" and "Off" colors. When the instance
   * is notified that it should stop, the foreground color is changed to
   * the "Off" color
   * 
   * @param aComp
   *          The component whose foreground color is controlled by this
   *          instance
   * @param aOnColor
   *          The color used to indicate some activity is running.
   * @param aOffColor
   *          The default foreground color of the component.
   * @param aMSDelay
   *          The number of milliseconds between color changes. Note that
   *          the colors change by gradation, so this number should be small.
   */
  public FlashForeground(Component aComp, Color aOnColor, Color aOffColor,
      int aMSDelay) {
    component = aComp;
    onColor = aOnColor;
    offColor = aOffColor;
    msDelay = aMSDelay;
    computeChange();
    direction = -1;
    currentValue = 0;
  }

  /**
   * Calculate the next color to be used for the foreground. The algorithm
   * is pretty simple, just change the RGB values between the "On" and "Off"
   * colors, incrementing based on the widest color descrepency.
   * 
   * @return The Color to be used for the foreground
   */
  private Color nextColor() {
    if (currentValue <= 0 || currentValue >= maxChange) {
      direction *= -1;
    }

    currentValue += direction;

    if (currentValue == maxChange) {
      redValue = redOn;
      greenValue = greenOn;
      blueValue = blueOn;
    } else if (currentValue == 0) {
      redValue = redOff;
      greenValue = greenOff;
      blueValue = blueOff;
    } else {
      if (currentValue % redModulus == 0) {
        redValue += (redChange * direction) < 0 ? 1 : -1;
      }

      if (currentValue % greenModulus == 0) {
        greenValue += (greenChange * direction) < 0 ? 1 : -1;
      }

      if (currentValue % blueModulus == 0) {
        blueValue += (blueChange * direction) < 0 ? 1 : -1;
      }
    }

    if (currentValue == 0 || currentValue == maxChange) {
      LOGGER.debug("currentValue=" + currentValue);
    }

    return new Color(redValue, greenValue, blueValue);
  }

  /**
   * Compute the RGB values for the "On" and "Off" colors. Then determine
   * the distance (numerically) between them.
   */
  private void computeChange() {
    redOn = onColor.getRed();
    redOff = offColor.getRed();
    greenOn = onColor.getGreen();
    greenOff = offColor.getGreen();
    blueOn = onColor.getBlue();
    blueOff = offColor.getBlue();
    redChange = redOff - redOn;
    greenChange = greenOff - greenOn;
    blueChange = blueOff - blueOn;
    maxChange = Math.abs(redChange);
    if (Math.abs(greenChange) > maxChange) {
      maxChange = Math.abs(greenChange);
    }
    if (Math.abs(blueChange) > maxChange) {
      maxChange = Math.abs(blueChange);
    }

    if (redChange != 0) {
      redModulus = maxChange / Math.abs(redChange);
      // if (redModulus < 1) {
      // redModulus = 1;
      // }
    } else {
      redModulus = maxChange + 1;
    }

    if (greenChange != 0) {
      greenModulus = maxChange / Math.abs(greenChange);
      // if (greenModulus < 1) {
      // greenModulus = 1;
      // }
    } else {
      greenModulus = maxChange + 1;
    }

    if (blueChange != 0) {
      blueModulus = maxChange / Math.abs(blueChange);
      // if (blueModulus < 1) {
      // blueModulus = 1;
      // }
    } else {
      blueModulus = maxChange + 1;
    }

    redValue = redOff;
    greenValue = greenOff;
    blueValue = blueOff;
  }

  /**
   * Oscillate the component colors until an interrupt is
   * received.
   */
  public void run() {
    boolean isOn;
    boolean keepGoing;

    isOn = false;
    keepGoing = true;

    try {
      while (keepGoing && !Thread.currentThread().isInterrupted()) {
        isOn = !isOn;
        // component.setForeground(isOn ? onColor : offColor);
        component.setForeground(nextColor());
        try {
          Thread.sleep(msDelay);
        } catch (InterruptedException ie) {
          keepGoing = false;
        }
      }
    } catch (Throwable any) {
      LOGGER.warn("Error when iterating through colors in a compoenent", any);
    } finally {
      component.setForeground(offColor);
    }
  }
}
