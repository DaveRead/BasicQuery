package us.daveread.basicquery.util.test;

import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import us.daveread.basicquery.util.Utility;

/**
 * <p>
 * Title: Test the utility class
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006-2014
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author David Read
 */
public class UtilityTest extends TestCase {
  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(UtilityTest.class);

  /**
   * Test data
   */
  private Object[][] data = {
      {
          "R1/C1", "R1/C2", "R1/C3"
      },
      {
          "R2/C1", "R2/C2", "R2/C3"
      },
      {
          null, null, null
      },
      {
          "R3/C1", "R3/C2", "R3/C3"
      },
      {
          "R0/C1", "R0/C2", "R0/C3"
      },
      {
          "R4/C1", "R4/C2", "R4/C3"
      },
      {
          "R4/C1", "R4/C2", "R4/C3"
      },
      {
          "abcdefghijklmnop", "qrstuvwxyz", "0123456789"
      },
      {
          null, null, null
      }
  };

  /**
   * Test data - wide
   */
  private Object[][] wideData = {
      {
          "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ",
          "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
      },
      {
          "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ",
          "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
      }
  };

  /**
   * Test column names
   */
  private String[] colNames = {
      "Col-1", "Col-2", "Col-3"
  };

  /**
   * Setup the test case instance
   */
  public UtilityTest() {
  }

  /**
   * Test the replace method
   */
  public void testReplace() {
    String value = "ABCDEFGHABCDEFABCDABC";
    value = Utility.replace(value, "ABC", "xy");
    assertEquals("xyDEFGHxyDEFxyDxy", value);

    value = "ABCDEFGHABCDEFABCDABC";
    value = Utility.replace(value, "ABC", "xy", 3, true);
    assertEquals("ABCDEFGHxyDEFxyDxy", value);

    value = "ABCDEFGHABCDEFABCDABC";
    value = Utility.replace(value, "ABC", "xy", 3, false);
    assertEquals("ABCDEFGHxyDEFABCDABC", value);

    try {
      value = Utility.replace(null, "ABC", "xy", 3, false);
      assertTrue(false);
    } catch (IllegalArgumentException iae) {
      assertTrue(true);
    }

    try {
      value = Utility.replace("ABCDEF", null, "xy", 3, false);
      assertTrue(false);
    } catch (IllegalArgumentException iae) {
      assertTrue(true);
    }

    try {
      value = Utility.replace("ABCDEF", "ABC", null, 3, false);
      assertTrue(false);
    } catch (IllegalArgumentException iae) {
      assertTrue(true);
    }

    value = "";
    value = Utility.replace(value, "ABC", "xy", 0, true);
    assertEquals("", value);

    value = "ABCDEF";
    value = Utility.replace(value, "", "xy", 0, true);
    assertEquals("ABCDEF", value);

    try {
      value = Utility.replace("ABCDEF", "ABC", "xy", -1, false);
      assertTrue(false);
    } catch (IllegalArgumentException iae) {
      assertTrue(true);
    }

    try {
      value = Utility.replace("ABCDEF", "ABC", "xy", 10, false);
      assertTrue(false);
    } catch (IllegalArgumentException iae) {
      assertTrue(true);
    }

  }

  /**
   * Test the initial column sizes method
   */
  public void testInitColumnSizes() {
    JTable table;
    DefaultTableModel model;
    int width;

    model = new DefaultTableModel(wideData, colNames);
    table = new JTable(model);
    width = table.getColumnModel().getColumn(1).getPreferredWidth();
    Utility.initColumnSizes(table, model);

    LOGGER.info("Initial width[" + width + "]  Updated width["
        + table.getColumnModel().getColumn(1).getPreferredWidth() + "]");

    assertTrue(table.getColumnModel().getColumn(1).getPreferredWidth() > width);
  }

  /**
   * Test the get longest value method
   */
  public void testGetLongestValues() {
    DefaultTableModel model;
    Object[] longest;

    model = new DefaultTableModel(data, colNames);

    longest = Utility.getLongestValues(model);

    assertEquals("abcdefghijklmnop", (String) longest[0]);
    assertEquals("qrstuvwxyz", (String) longest[1]);
    assertEquals("0123456789", (String) longest[2]);
  }

  /**
   * Test the formatted date method
   */
  public void testFormattedDate() {
    String date;

    date = Utility.formattedDate(new Date());
    assertTrue(date.charAt(2) == '/');

    date = Utility.formattedDate(new Date(), "yyyy-MM-dd");
    assertTrue(date.charAt(4) == '-');
  }

  /**
   * Test the formatted number method
   */
  public void testFormattedNumber() {
    String number;

    number = Utility.formattedNumber(150, "#0000");
    assertEquals("0150", number);
  }

  /**
   * Test the insert character method
   */
  public void testCharacterInsert() {
    String value;

    value = Utility.characterInsert("ABC DEFGHI JKLM N", "-", 2, 3, " ");
    assertEquals("ABC- DE-FGH-I -JKL-M -N", value);

    value = Utility.characterInsert("AB-CD", "-", 2, 3, " ");
    assertEquals("AB-CD", value);
  }

  /**
   * Test the split with quotes method
   */
  public void testSplitWithQuotes() {
    String[] parsed;

    parsed = Utility.splitWithQuotes("Split;this", ";");
    assertEquals(2, parsed.length);
    assertEquals("Split", parsed[0]);
    assertEquals("this", parsed[1]);

    parsed = Utility.splitWithQuotes("Split;\"this;stays\";third", ";");
    assertEquals(3, parsed.length);
    assertEquals("Split", parsed[0]);
    assertEquals("\"this;stays\"", parsed[1]);
    assertEquals("third", parsed[2]);

    parsed = Utility
        .splitWithQuotes(
            "Split;'this;stays';\"third;'still third;'more third;\"last third;fourth",
            ";");
    assertEquals(4, parsed.length);
    assertEquals("Split", parsed[0]);
    assertEquals("'this;stays'", parsed[1]);
    assertEquals("\"third;'still third;'more third;\"last third", parsed[2]);
    assertEquals("fourth", parsed[3]);

  }
}
