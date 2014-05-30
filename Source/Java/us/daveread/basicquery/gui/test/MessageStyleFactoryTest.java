package us.daveread.basicquery.gui.test;

import java.awt.Color;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;

import junit.framework.TestCase;

import us.daveread.basicquery.gui.MessageStyleFactory;

/**
 * <p>Title: Test the message style factory class</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006-2014</p>
 *
 * <p>Company: </p>
 *
 * @author David Read
 */
public class MessageStyleFactoryTest extends TestCase {
  /**
   * The factory instance
   */
  private MessageStyleFactory msg;

  /**
   * Setup the test case instance
   */
  public MessageStyleFactoryTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    msg = MessageStyleFactory.instance();
  }

  /**
   * Test creating a style
   */
  public void testCreateStyle() {
    AttributeSet attr = msg.createStyle(Color.blue);
    assertEquals(Color.blue, StyleConstants.getForeground(attr));
    assertEquals(Color.white, StyleConstants.getBackground(attr));
    assertFalse(StyleConstants.isBold(attr));
    assertFalse(StyleConstants.isItalic(attr));
    assertFalse(StyleConstants.isUnderline(attr));

    attr = msg.createStyle(Color.blue, Color.yellow, 0);
    assertEquals(Color.blue, StyleConstants.getForeground(attr));
    assertEquals(Color.yellow, StyleConstants.getBackground(attr));
    assertFalse(StyleConstants.isBold(attr));
    assertFalse(StyleConstants.isItalic(attr));
    assertFalse(StyleConstants.isUnderline(attr));

    attr = msg.createStyle(Color.orange, Color.cyan, MessageStyleFactory.BOLD);
    assertEquals(Color.orange, StyleConstants.getForeground(attr));
    assertEquals(Color.cyan, StyleConstants.getBackground(attr));
    assertTrue(StyleConstants.isBold(attr));
    assertFalse(StyleConstants.isItalic(attr));
    assertFalse(StyleConstants.isUnderline(attr));

    attr = msg.createStyle(Color.orange, Color.cyan, MessageStyleFactory.ITALIC);
    assertEquals(Color.orange, StyleConstants.getForeground(attr));
    assertEquals(Color.cyan, StyleConstants.getBackground(attr));
    assertFalse(StyleConstants.isBold(attr));
    assertTrue(StyleConstants.isItalic(attr));
    assertFalse(StyleConstants.isUnderline(attr));

    attr = msg.createStyle(Color.orange, Color.cyan,
        MessageStyleFactory.UNDERLINE);
    assertEquals(Color.orange, StyleConstants.getForeground(attr));
    assertEquals(Color.cyan, StyleConstants.getBackground(attr));
    assertFalse(StyleConstants.isBold(attr));
    assertFalse(StyleConstants.isItalic(attr));
    assertTrue(StyleConstants.isUnderline(attr));

    attr = msg.createStyle(Color.orange, Color.cyan,
        MessageStyleFactory.BOLD | MessageStyleFactory.ITALIC);
    assertEquals(Color.orange, StyleConstants.getForeground(attr));
    assertEquals(Color.cyan, StyleConstants.getBackground(attr));
    assertTrue(StyleConstants.isBold(attr));
    assertTrue(StyleConstants.isItalic(attr));
    assertFalse(StyleConstants.isUnderline(attr));

    attr = msg.createStyle(Color.orange, Color.cyan,
        MessageStyleFactory.BOLD | MessageStyleFactory.UNDERLINE);
    assertEquals(Color.orange, StyleConstants.getForeground(attr));
    assertEquals(Color.cyan, StyleConstants.getBackground(attr));
    assertTrue(StyleConstants.isBold(attr));
    assertFalse(StyleConstants.isItalic(attr));
    assertTrue(StyleConstants.isUnderline(attr));

    attr = msg.createStyle(Color.orange, Color.cyan,
        MessageStyleFactory.ITALIC | MessageStyleFactory.UNDERLINE);
    assertEquals(Color.orange, StyleConstants.getForeground(attr));
    assertEquals(Color.cyan, StyleConstants.getBackground(attr));
    assertFalse(StyleConstants.isBold(attr));
    assertTrue(StyleConstants.isItalic(attr));
    assertTrue(StyleConstants.isUnderline(attr));

    attr = msg.createStyle(Color.orange, Color.cyan,
        MessageStyleFactory.BOLD | MessageStyleFactory.ITALIC 
          | MessageStyleFactory.UNDERLINE);
    assertEquals(Color.orange, StyleConstants.getForeground(attr));
    assertEquals(Color.cyan, StyleConstants.getBackground(attr));
    assertTrue(StyleConstants.isBold(attr));
    assertTrue(StyleConstants.isItalic(attr));
    assertTrue(StyleConstants.isUnderline(attr));

    attr = msg.createStyle(Color.red, MessageStyleFactory.ITALIC);
    assertEquals(Color.red, StyleConstants.getForeground(attr));
    assertEquals(Color.white, StyleConstants.getBackground(attr));
    assertFalse(StyleConstants.isBold(attr));
    assertTrue(StyleConstants.isItalic(attr));
    assertFalse(StyleConstants.isUnderline(attr));

    attr = msg.createStyle(MessageStyleFactory.BOLD 
        | MessageStyleFactory.ITALIC);
    assertEquals(Color.black, StyleConstants.getForeground(attr));
    assertEquals(Color.white, StyleConstants.getBackground(attr));
    assertTrue(StyleConstants.isBold(attr));
    assertTrue(StyleConstants.isItalic(attr));
    assertFalse(StyleConstants.isUnderline(attr));
  }
}
