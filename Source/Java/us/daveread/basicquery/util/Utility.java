package us.daveread.basicquery.util;

import java.awt.Component;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

/**
 * Title:        Utilities
 * Description:  Utilities for string manipulation and other convenience methods
 * Copyright:    Copyright (c) 2003
 *
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
 *
 * @author David Read
 * @version $Id: Utility.java,v 1.7 2006/05/30 17:37:14 daveread Exp $
 */

public class Utility {
  public final static String ID =
      "$Id: Utility.java,v 1.7 2006/05/30 17:37:14 daveread Exp $";

  private final static Logger logger = Logger.getLogger(Utility.class);

  // Map used for storing date formats for formattedDate() methods
  private static Map formats;

  // Establish static configuration
  static {
    formats = new HashMap();
  }

  private Utility() {
  }

  /**
   * This method picks good column sizes.
   * If all column heads are wider than the column's cells'
   * contents, then you can just use column.sizeWidthToFit().
   *
   * @param table The table contains the columns for comparision
   * @param model This is the Default Table Model of the Jtable
   */


  /**
   * Replaces the specified substring with another in the first (whole) string.
   *
   * @param aString The whole string.
   * @param aOldSubString The substrinng to be replaced.
   * @param aNewSubString The new substring to replace the old one.
   *
   * @return The whole string with the substrings replaced.
   */
  public static String replace(String aString, String aOldSubString,
      String aNewSubString) {
    return replace(aString, aOldSubString, aNewSubString, 0, true);
  }

  /**
   * Replaces the specified substring with another in the first (whole)
   * string starting at iaStartIndex.
   *
   * @param aString The whole string.
   * @param aOldSubString The substring to be replaced.
   * @param aNewSubString The new substring to replace the old one.
   * @param iaStartIndex The index at which to start the process of 'search
   * and replace'.
   *
   * @return The whole string with the substrings replaced.
   */
  public static String replace(String saString, String saOldSubString,
      String saNewSubString, int iaStartIndex,
      boolean replaceAll) {
    StringBuffer sblResult;
    boolean blDone, blFirstReplacement;
    int ilIndex;
    if (saString == null || saOldSubString == null || saNewSubString == null) {
      throw new IllegalArgumentException("arguments may not be null");
    } else if (saString.equals("") || saOldSubString.equals("")) {
      return saString; // return the empty String as such as nothing can be replaced
    } else if (iaStartIndex < 0) {
      throw new IllegalArgumentException(
          "iaStartIndex must be greater than or equal to 0");
    } else if (iaStartIndex >= saString.length()) {
      throw new IllegalArgumentException("iaStartIndex cannot equal or exceed" +
          "length of input String saString");
    }
    sblResult = new StringBuffer();
    blDone = false;
    blFirstReplacement = true;

    while (!blDone) {
      ilIndex = saString.indexOf(saOldSubString, iaStartIndex);
      if ((!blFirstReplacement && !replaceAll) || ilIndex == -1) {
        sblResult.append(saString);
        blDone = true;
      } else {
        sblResult.append(saString.substring(0, ilIndex));
        sblResult.append(saNewSubString);
        if (saString.length() > ilIndex + saOldSubString.length()) {
          saString = saString.substring(ilIndex + saOldSubString.length(),
              saString.length());
        } else {
          blDone = true;
        }
        blFirstReplacement = false;
        iaStartIndex = 0; // We have skipped the beginning, now always asses what's left
      }
    }
    return sblResult.toString();
  }

  /*
   * This method picks good column sizes.
   * If all column heads are wider than the column's cells'
   * contents, then you can just use column.sizeWidthToFit().
   */

  public static void initColumnSizes(JTable table, TableModel model) {
    TableColumn column = null;
    Component comp = null;
    int headerWidth = 0;
    int cellWidth = 0;
    Object[] longValues = getLongestValues(model);

    for (int i = 0; i < longValues.length; i++) {
      column = table.getColumnModel().getColumn(i);

      try {
//                comp = column.getHeaderRenderer().
//                                 getTableCellRendererComponent(
//                                     null, column.getHeaderValue(),
//                                     false, false, 0, 0);
        comp = table.getTableHeader().getDefaultRenderer().
            getTableCellRendererComponent(
            null, column.getHeaderValue() + "W",
            false, false, 0, 0);
        headerWidth = comp.getPreferredSize().width;

        // Periodically the return value from getPreferredSize() is huge
        // No idea what is going on - this recheck seems to always come back
        // with a sane value
        if (headerWidth > 10000) {
          logger.debug("Header unusually wide (" +
              headerWidth + "): calc again");

          headerWidth = comp.getPreferredSize().width;

          logger.debug("Result of header recalc (" +
              headerWidth + ")");
        }
      }
      catch (NullPointerException e) {
        System.err.println("Null pointer exception!");
        System.err.println("  getHeaderRenderer returns null in 1.3.");
        System.err.println("  The replacement is getDefaultRenderer.");
      }

      comp = table.getDefaultRenderer(model.getColumnClass(i)).
          getTableCellRendererComponent(
          table, longValues[i] + "W",
          false, false, 0, i);
      cellWidth = comp.getPreferredSize().width;

      // Periodically the return value from getPreferredSize() is huge
      // No idea what is going on - this recheck seems to always come back
      // with a sane value
      if (cellWidth > 10000) {
        logger.debug("Column unusually wide (" +
            cellWidth + "): calc again");

        cellWidth = comp.getPreferredSize().width;

        logger.debug("Result of recalc (" +
            cellWidth + ")");
      }

      logger.debug("Initializing width of column "
          + i + ". "
          + "headerWidth = " + headerWidth
          + "; cellWidth = " + cellWidth
          + "; longValue = [" + longValues[i] + "]");

      //NOTE: Before Swing 1.1 Beta 2, use setMinWidth instead.
      column.setPreferredWidth(Math.max(headerWidth, cellWidth));
    }
  }

  /**
   * Gets longest values , one for each column
   *
   * @param model The DefaultTable model
   *
   * @return obj1longest Object that has the longest value for each column
   */
  public static Object[] getLongestValues(TableModel model) {
    Object objlLongest[], objlValue;
    int ilLen[], ilThisLen;
    int ilNumRows, ilRow, ilNumCols, ilCol;

    ilNumCols = model.getColumnCount();
    ilNumRows = model.getRowCount();

    objlLongest = new Object[ilNumCols];
    ilLen = new int[ilNumCols];

    for (ilCol = 0; ilCol < ilNumCols; ++ilCol) {
      objlLongest[ilCol] = "";
      ilLen[ilCol] = 0;
    }

    for (ilRow = 0; ilRow < ilNumRows; ++ilRow) {
      for (ilCol = 0; ilCol < ilNumCols; ++ilCol) {
        objlValue = model.getValueAt(ilRow, ilCol);
        if (objlValue != null) {
          if ((ilThisLen = objlValue.toString().length()) > ilLen[ilCol]) {
            objlLongest[ilCol] = objlValue;
            ilLen[ilCol] = ilThisLen;
            logger.debug(
                "Get longest value, Checking(" + ilRow + "," +
                ilCol + ")=" + ilThisLen);
          }
        }
      }
    }

    return objlLongest;
  }

  /**
   * Formats the System date to appear as MM/dd/yyyy HH:mm:ss.SSS
   *
   * @param date     The system date that needs to be formatted
   *
   * @return String  The formatted date
   */
  public static String formattedDate(java.util.Date date) {
    return formattedDate(date, "MM/dd/yyyy HH:mm:ss.SSS");
  }

  /**
   * Formats the System date in the order determined by the formatString
   *
   * @param date           The System Date
   * @param formatString   The desired format for writing out the date
   *
   * @return String        The formatted date
   */
  public static String formattedDate(java.util.Date date, String formatString) {
    SimpleDateFormat format;

    format = (SimpleDateFormat)formats.get(formatString);
    if (format == null) {
      format = new SimpleDateFormat(formatString);
      formats.put(formatString, format);
    }

    return format.format(date);
  }

  public static String formattedNumber(long number, String format) {
    DecimalFormat fmt;

    fmt = new DecimalFormat(format);

    return fmt.format(number);
  }

  /**
   * Ensure that a given character is inserted at intervals along a String.
   * This is typically used to force inclusing of a space periodically.
   * The method will walk through the supplied data string, looking for characters
   * that are in the break characters String.  If found, and the minimum number
   * of characters have been traversed, the insert string is inserted and the
   * character count reset.  If the maximum section length is reached and no
   * break characters have been found, the insert string is inserted anyway.
   *
   * @param saData The string to have characters inserted into
   * @param saInsert The String to insert into the data string
   * @param iaMinSectionLength The mimumum number of characters in the data
   *      string, that must occur between inserted strings
   * @param iaMaxSectionLength The maximum number of characters to allow
   *      in the data string between inserted strings
   * @param saBreakCharacters The characters that signal a possible insertion
   *      point in the data string
   *
   * @return The updated data string.  If the data string is null, a null
   *      String is returned.
   */
  public final static String characterInsert(String saData, String saInsert,
      int iaMinSectionLength,
      int iaMaxSectionLength,
      String saBreakCharacters) {
    StringBuffer sblResult;
    String slResult;
    char clData[];
    int ilLen, ilPosit, ilSectionLength;

    if (saData != null) {
      ilLen = saData.length();
      clData = new char[ilLen];
      saData.getChars(0, ilLen, clData, 0);
      ilPosit = 0;
      ilSectionLength = 0;
      sblResult = new StringBuffer(ilLen * 2);

      for (ilPosit = 0; ilPosit < ilLen; ++ilPosit) {
        sblResult.append(clData[ilPosit]);
        ++ilSectionLength;
        if (saInsert.length() == 1 && clData[ilPosit] == saInsert.charAt(0)) {
          ilSectionLength = 0;
        } else if ((ilSectionLength >= iaMinSectionLength &&
            saBreakCharacters.indexOf(clData[ilPosit]) >= 0) ||
            ilSectionLength >= iaMaxSectionLength) {
          sblResult.append(saInsert);
          ilSectionLength = 0;
        }
      }
      saData = sblResult.toString();
    }

    return saData;
  }

  /**
   * Split a string at a pattern, but be sensitive to quoted sections of the
   * string.  Quoted sections should not be parsed.  Nested quotes and
   * apostrophies are also observed.
   *
   * e.g. Parsing for semicolons (;), the following string:
   *
   * This is;a test;string
   *
   * should become three string:
   * 1) This is
   * 2) a test
   * 3) string
   *
   * But the string:
   *
   * This "is;a" test;string
   *
   * should only become two strings:
   * 1) This "is;a" test
   * 2) string
   *
   * @param data String The string to be split into separate strings
   * @param splitPattern String The pattern used to split the string
   * @return String[] The individually split strings
   */
  public static String[] splitWithQuotes(String data, String splitPattern) {
    String[] maxParts;
    List tempParts;
    String recombined;
    int partIndex;
    List stackOfQuotes;
    char[] partData;
    int charIndex;
    boolean escapeNext;

    stackOfQuotes = new ArrayList();
    maxParts = data.split(splitPattern);
    tempParts = new ArrayList();
    recombined = "";
    escapeNext = false;

    logger.debug("Split [" + data + "] at [" + splitPattern + "]");

    for (partIndex = 0; partIndex < maxParts.length; ++partIndex) {
      partData = maxParts[partIndex].toCharArray();
      for (charIndex = 0; charIndex < partData.length; ++charIndex) {
        if (!escapeNext && (partData[charIndex] == '\'' ||
            partData[charIndex] == '"')) {
          if (stackOfQuotes.size() > 0) {
            if (((Character)stackOfQuotes.get(0)).charValue() ==
                partData[charIndex]) {
              stackOfQuotes.remove(0);
            } else {
              stackOfQuotes.add(0, new Character(partData[charIndex]));
            }
          } else {
            stackOfQuotes.add(0, new Character(partData[charIndex]));
          }
        } else if (!escapeNext && partData[charIndex] == '\\') {
          escapeNext = true;
        } else if (escapeNext) {
          escapeNext = false;
        }

      }

      // At this point we'll either have an empty stackOfQuotes - in which case
      // this data can stand on it's own, appended to anything in recombined if
      // it is not empty.
      // Otherwise we are in a quoted string and this value must be appended
      // to the recombined string.

      // For simplicity we'll append the string to recombined, which may be empty
      // then either add recombined to the tempParts if we are not in a quote
      // or leave it sit there if we are in a quote.

      // We append the splitPattern since that must have followed the value
      // or it wouldn't have been a separate part.
      if (recombined.length() > 0) {
        recombined += splitPattern;
      }

      recombined += maxParts[partIndex];

      // Not in a quoted string
      if (stackOfQuotes.size() == 0) {
        tempParts.add(recombined);
        recombined = "";
      }
    }

    // If recombined still has a string in it, then there were one or more
    // dangling quotes - just put the string in place and allow the process
    // to continue.
    if (recombined.length() > 0) {
      tempParts.add(recombined);
    }

    // Return the array of strings
    return (String[])tempParts.toArray(new String[tempParts.size()]);
  }

  /*    public static Object getValueAt(TableModel model, int iaRow, int iaCol) {
          Object objlValue;

          if (iaRow < model.getRowCount() && iaCol < model.getColumnCount() &&
                  iaRow >= 0 && iaCol >= 0) {
              objlValue = model.getValueAt(iaRow, iaCol);
          } else {
              objlValue = null;
          }

          return objlValue;
      } */
}
