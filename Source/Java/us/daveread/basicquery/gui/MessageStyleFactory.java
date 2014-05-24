package us.daveread.basicquery.gui;

import javax.swing.text.AttributeSet;
import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MessageStyleFactory {

  private static MessageStyleFactory factory;

  private final static int MESSAGEATTRIBUTE_COUNT = 7;

//  public final static int NORMAL = 0;
//  public final static int BOLD = 1;
//  public final static int GREEN = 2;
//  public final static int YELLOW = 3;
//  public final static int RED = 4;
//  public final static int SUBTLE = 5;
//  public final static int SUBTLEUL = 6;

  public final static int BOLD = 1;
  public final static int ITALIC = 2;
  public final static int UNDERLINE = 4;

//  public final static int DEFAULT_FONT_SIZE = 12;

//  private int fontSize = DEFAULT_FONT_SIZE;

//  private AttributeSet messageStyles[];

  private MessageStyleFactory() {
//    setupStyles();
  }

  public static synchronized MessageStyleFactory instance() {
    if (factory == null) {
      factory = new MessageStyleFactory();
    }

    return factory;
  }

/*  public void setFontSize(int pFontSize) {
    if (pFontSize > 0) {
      fontSize = pFontSize;
    }
  }

  public int getFontSize() {
    return fontSize;
  }
*/

  public AttributeSet createStyle(Color textColor) {
    return createStyle(textColor, 0);
  }

  public AttributeSet createStyle(int styleId) {
//    return messageStyles[styleId];
    return createStyle(Color.black, styleId);
  }

  public AttributeSet createStyle(Color textColor, int style) {
    return createStyle(textColor, Color.white, style);
  }

  public AttributeSet createStyle(Color textColor, Color backgroundColor,
      int style) {
    return createStyle(textColor, backgroundColor, isTrue(style, BOLD),
        isTrue(style, ITALIC), isTrue(style, UNDERLINE));
  }

  private AttributeSet createStyle(Color textColor, Color backgroundColor,
      boolean bold, boolean italic,
      boolean underline) {
    AttributeSet attributeSet;

    attributeSet = new SimpleAttributeSet();
    StyleConstants.setForeground((SimpleAttributeSet)attributeSet, textColor);
    StyleConstants.setBackground((SimpleAttributeSet)attributeSet,
        backgroundColor);

    if (bold) {
      StyleConstants.setBold((SimpleAttributeSet)attributeSet, true);
    }

    if (italic) {
      StyleConstants.setItalic((SimpleAttributeSet)attributeSet, true);
    }

    if (underline) {
      StyleConstants.setUnderline((SimpleAttributeSet)attributeSet, true);
    }

//    StyleConstants.setFontSize((SimpleAttributeSet)attributeSet, fontSize);

    return attributeSet;
  }

  /**
   * Sets the style attribute set for the messages
   * Sets the Foreground and background color
   * Sets the style constants
   */
  /*  private void setupStyles() {
      messageStyles = new AttributeSet[MESSAGEATTRIBUTE_COUNT];

      // Normal
      messageStyles[NORMAL] = new SimpleAttributeSet();

      // Bold
      messageStyles[BOLD] = new SimpleAttributeSet();
      StyleConstants.setBold((SimpleAttributeSet) messageStyles[
                             BOLD], true);

      // Green
      messageStyles[GREEN] = new SimpleAttributeSet();
      StyleConstants.setForeground((SimpleAttributeSet) messageStyles[
                                   GREEN], Color.green.darker());
      StyleConstants.setBold((SimpleAttributeSet) messageStyles[
                             GREEN], true);

      // Yellow
      messageStyles[YELLOW] = new SimpleAttributeSet();
      StyleConstants.setForeground((SimpleAttributeSet) messageStyles[
                                   YELLOW], Color.black);
      StyleConstants.setBackground((SimpleAttributeSet) messageStyles[
                                   YELLOW], Color.yellow);
      StyleConstants.setBold((SimpleAttributeSet) messageStyles[
                             YELLOW], true);

      // Red
      messageStyles[RED] = new SimpleAttributeSet();
      StyleConstants.setForeground((SimpleAttributeSet) messageStyles[
                                   RED], Color.red);
      StyleConstants.setBold((SimpleAttributeSet) messageStyles[
                             RED], true);

      // Subtle
      messageStyles[SUBTLE] = new SimpleAttributeSet();
      StyleConstants.setForeground((SimpleAttributeSet) messageStyles[
                                   SUBTLE], Color.blue.brighter());
      StyleConstants.setItalic((SimpleAttributeSet) messageStyles[
                               SUBTLE], true);

      // Subtle Underlined
      messageStyles[SUBTLEUL] = new SimpleAttributeSet();
      StyleConstants.setForeground((SimpleAttributeSet) messageStyles[
                                   SUBTLEUL],
                                   Color.blue.brighter());
      StyleConstants.setItalic((SimpleAttributeSet) messageStyles[
                               SUBTLEUL], true);
      StyleConstants.setUnderline((SimpleAttributeSet) messageStyles[
                                  SUBTLEUL], true);

    }
   */
  private boolean isTrue(int style, int attribute) {
    return (style & attribute) > 0;
  }
}
