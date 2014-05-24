package us.daveread.basicquery.gui;

import java.awt.Component;
import java.awt.Color;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

/**
 * Title:        Basic Query Utility
 * Description:  Execute arbitrary SQL against database accessible with any
 * JDBC-compliant driver.
 * Copyright:    Copyright (c) 2003
 * @author David Read
 * @version $Id: ColoredCellRenderer.java,v 1.5 2006/05/18 02:33:25 daveread Exp $
 */


public class ColoredCellRenderer extends JLabel implements TableCellRenderer {
  public final static String ID =
      "$Id: ColoredCellRenderer.java,v 1.5 2006/05/18 02:33:25 daveread Exp $";

  private final static Logger logger = Logger.getLogger(ColoredCellRenderer.class);

  private Map mapcFGPatterns;
  private Map mapcBGPatterns;

  private List objcAlternatingBGColors;
  private List objcAlternatingFGColors;

  private JTable objcTable;
  private AbstractTableModel objcModel;

  /**
   * Constructor a ColoredCellRenderer - when this function is called it
   * passes default parameters to the function
   * ColoredCellRenderer(int,int,int,int)
   */
  public ColoredCellRenderer() {
    this(JLabel.LEFT, JLabel.CENTER);
  }

  /**
   * Class Constructor - passes the parameter iaHorizontalAlignment to the
   * function ColoredCellRenderer(int,int,int,int)
   *
   * @param iaHorizontalAlignment  The Horizontal Alignment attribute
   */
  public ColoredCellRenderer(int iaHorizontalAlignment) {
    this(iaHorizontalAlignment, JLabel.CENTER);
  }

  /**
   * Creates and Initializes the class ColoredCellRenderer
   *
   * @param iaHorizAlignment        The Horizontal Alignment attribute
   * @param iaVerticalAlignment     The Vertical Alignment attribute
   */
  public ColoredCellRenderer(int iaHorizAlignment, int iaVerticalAlignment) {
    setOpaque(true);
    setHorizontalAlignment(iaHorizAlignment);
    setVerticalAlignment(iaVerticalAlignment);

    mapcFGPatterns = new HashMap();
    mapcBGPatterns = new HashMap();
  }

  public void addAlternatingRowColor(Color objaTextColor, Color objaBGColor) {
    if (objcAlternatingBGColors == null) {
      objcAlternatingBGColors = new ArrayList();
    }

    if (objcAlternatingFGColors == null) {
      objcAlternatingFGColors = new ArrayList();
    }

    objcAlternatingBGColors.add(objaBGColor);
    objcAlternatingFGColors.add(objaTextColor);
  }

  /**
   * Sets the default foreground (text) and background colors for the object
   *
   * @param objaTextColor  The default foreground (text) color is set to this value
   * @param objaBackgroundColor  The default background color is set to this value
   */
  public void setDefaultColors(Color objaTextColor, Color objaBackgroundColor) {
    objcAlternatingBGColors = new ArrayList();
    objcAlternatingBGColors.add(objaBackgroundColor);

    objcAlternatingFGColors = new ArrayList();
    objcAlternatingFGColors.add(objaTextColor);
  }

  public void clearColors() {
    objcAlternatingBGColors = null;
    objcAlternatingFGColors = null;
  }

  /**
   * Associates the specified foreground (text) and background colors with the
   * specified string pattern
   *
   * @param saPattern                  The key in this map is the string
   * @param objaTextColor The value of the foreground (text) Color object associated with the string
   * @param objaBackgroundColor        The value of the background Color object which
   *                                   is associated with the string
   */
  public void addPattern(String saPattern, Color objaTextColor,
      Color objaBackgroundColor) {
    mapcFGPatterns.put(saPattern, objaTextColor);
    mapcBGPatterns.put(saPattern, objaBackgroundColor);
  }

  /**
   * Finds the background color object that is associated with the String pattern
   *
   * @param saText        The string pattern which is the key for the
   *                      color object
   *
   * @return Color        The background Color object
   */
  private Color findBGColor(String saText, int row) {
    Color objlMatchedColor;

    objlMatchedColor = (Color)mapcBGPatterns.get(saText);

    if (objlMatchedColor == null && objcAlternatingBGColors != null) {
      objlMatchedColor = (Color)objcAlternatingBGColors.get(row %
          objcAlternatingBGColors.size());
    }

    return objlMatchedColor;
  }

  /**
   * Finds the foreground color object that is associated with the String pattern
   *
   * @param saText        The string pattern which is the key for the
   *                      color object
   *
   * @return Color        The foreground Color object
   */
  private Color findFGColor(String saText, int row) {
    Color objlMatchedColor;

    objlMatchedColor = (Color)mapcFGPatterns.get(saText);

    if (objlMatchedColor == null && objcAlternatingFGColors != null) {
      objlMatchedColor = (Color)objcAlternatingFGColors.get(row %
          objcAlternatingFGColors.size());
    }

    return objlMatchedColor;
  }

  /**
   * Returns the component used for drawing the cell.Used to configure the
   * renderer appropriately before drawing
   *
   * @param table        The JTable that is asking the renderer to draw
   * @param value        The value of the cell to be rendered
   * @param isSelected   A Boolean Value - true if the cell is to be rendered
   *                     with the selection highlighted
   * @param hasFocus     A Boolean Value - if true render cell appropriately
   * @param row          The row index of the cell to be rendered
   * @param column       The column index of the cell to be rendered
   *
   * @return Component   Returns the component used for drawing the cell
   */
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected,
      boolean hasFocus, int row,
      int column) {

    if (objcTable == null) {
      objcTable = table;
      objcModel = (AbstractTableModel)table.getModel();
    }

    if (value != null) {
      setText(value.toString());
    } else {
      setText("");
    }

    setColor(table, isSelected, hasFocus, row, column);

    return this;
  }

  /**
   * Sets the color for the object objlBackgroundColor
   *
   * @param table           The JTable
   * @param isSelected      Boolean value
   * @param hasFocus        Whether the component has focus
   * @param row             The row index
   * @param col             The column index
   */
  private void setColor(JTable table, boolean isSelected, boolean hasFocus,
      int row, int col) {

    Color objlBackgroundColor;
    Color objlForegroundColor;

    objlBackgroundColor = findBGColor(getText(), row);
    objlForegroundColor = findFGColor(getText(), row);

    if (objlForegroundColor != null) {
      setForeground(objlForegroundColor);
    } else {
      setForeground(table.getForeground());
    }

    if (objlBackgroundColor != null) {
      setBorder(BorderFactory.createLineBorder(objlBackgroundColor, 2));
      setBackground(objlBackgroundColor);
    } else {
      setBorder(BorderFactory.createLineBorder(table.getBackground(), 2));
      setBackground(table.getBackground());
    }

    if (isSelected && !hasFocus) {
      setBorder(BorderFactory.createLineBorder(table.getSelectionBackground(),
          2));
      setBackground(table.getSelectionBackground());
      setForeground(table.getSelectionForeground());
    } else if (isSelected) {
      setBorder(BorderFactory.createLineBorder(table.getSelectionBackground(),
          2));
      setBackground(table.getBackground());
      setForeground(table.getForeground());
    }
  }
}
