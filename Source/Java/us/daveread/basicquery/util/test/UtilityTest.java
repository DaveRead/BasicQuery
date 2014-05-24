package us.daveread.basicquery.util.test;

import java.util.Date;
import junit.framework.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

import us.daveread.basicquery.util.Utility;

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
 * @version $Id: UtilityTest.java,v 1.3 2006/05/30 17:38:01 daveread Exp $
 */
public class UtilityTest extends TestCase {
  private static final Logger logger = Logger.getLogger(UtilityTest.class);

  private Object[][] data = { {"R1/C1", "R1/C2", "R1/C3"}, {"R2/C1", "R2/C2",
      "R2/C3"}, {null, null, null}, {"R3/C1", "R3/C2", "R3/C3"}, {"R0/C1",
      "R0/C2", "R0/C3"}, {"R4/C1", "R4/C2", "R4/C3"}, {"R4/C1", "R4/C2",
      "R4/C3"}, {"abcdefghijklmnop", "qrstuvwxyz", "0123456789"}, {null, null, null}
  };

  private Object[][] wideData = { {
      "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ",
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"},
      {"ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ",
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"}
  };

  private String[] colNames = {
      "Col-1", "Col-2", "Col-3"
  };

  public UtilityTest() {
  }

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
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }

    try {
      value = Utility.replace("ABCDEF", null, "xy", 3, false);
      assertTrue(false);
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }

    try {
      value = Utility.replace("ABCDEF", "ABC", null, 3, false);
      assertTrue(false);
    }
    catch (IllegalArgumentException iae) {
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
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }

    try {
      value = Utility.replace("ABCDEF", "ABC", "xy", 10, false);
      assertTrue(false);
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }

  }

  public void testInitColumnSizes() {
    JTable table;
    DefaultTableModel model;
    int width;

    model = new DefaultTableModel(wideData, colNames);
    table = new JTable(model);
    width = table.getColumnModel().getColumn(1).getPreferredWidth();
    Utility.initColumnSizes(table, model);

    logger.info("Initial width[" + width + "]  Updated width[" +
        table.getColumnModel().getColumn(1).getPreferredWidth() + "]");

    assertTrue(table.getColumnModel().getColumn(1).getPreferredWidth() > width);
  }

  public void testGetLongestValues() {
    DefaultTableModel model;
    Object[] longest;

    model = new DefaultTableModel(data, colNames);

    longest = Utility.getLongestValues(model);

    assertEquals("abcdefghijklmnop", (String)longest[0]);
    assertEquals("qrstuvwxyz", (String)longest[1]);
    assertEquals("0123456789", (String)longest[2]);
  }

  public void testFormattedDate() {
    String date;

    date = Utility.formattedDate(new Date());
    assertTrue(date.charAt(2) == '/');

    date = Utility.formattedDate(new Date(), "yyyy-MM-dd");
    assertTrue(date.charAt(4) == '-');
  }

  public void testFormattedNumber() {
    String number;

    number = Utility.formattedNumber(123, "#0000");
    assertEquals("0123", number);
  }

  public void testCharacterInsert() {
    String value;

    value = Utility.characterInsert("ABC DEFGHI JKLM N", "-", 2, 3, " ");
    assertEquals("ABC- DE-FGH-I -JKL-M -N", value);

    value = Utility.characterInsert("AB-CD", "-", 2, 3, " ");
    assertEquals("AB-CD", value);
  }

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

    parsed = Utility.splitWithQuotes("Split;'this;stays';\"third;'still third;'more third;\"last third;fourth", ";");
    assertEquals(4, parsed.length);
    assertEquals("Split", parsed[0]);
    assertEquals("'this;stays'", parsed[1]);
    assertEquals("\"third;'still third;'more third;\"last third", parsed[2]);
    assertEquals("fourth", parsed[3]);

  }
}
