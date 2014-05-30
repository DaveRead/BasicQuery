package us.daveread.basicquery.gui;

import javax.swing.text.AttributeSet;
import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class MessageStyleFactory {

  /**
   * The factory instance - Singleton
   */
  private static MessageStyleFactory factory;

  /**
   * Attribute: bold font
   */
  public static final int BOLD = 1;

  /**
   * Attribute: italic font
   */
  public static final int ITALIC = 2;

  /**
   * Attribute: underline font
   */
  public static final int UNDERLINE = 4;

  /**
   * private constructor for Singleton
   */
  private MessageStyleFactory() {

  }

  /**
   * Get the instance of the factory
   * 
   * @return The factory instance
   */
  public static synchronized MessageStyleFactory instance() {
    if (factory == null) {
      factory = new MessageStyleFactory();
    }

    return factory;
  }

  /**
   * Create the attribute set with the supplied color
   * 
   * @param textColor
   *          The font foreground color
   * 
   * @return The created attribute set
   */
  public AttributeSet createStyle(Color textColor) {
    return createStyle(textColor, 0);
  }

  /**
   * Create the attribute set with the supplied style
   * 
   * @param styleId
   *          The style identifier
   * 
   * @return The created attribute set
   */
  public AttributeSet createStyle(int styleId) {
    return createStyle(Color.black, styleId);
  }

  /**
   * Create the attribute set with the supplied color and style
   * 
   * @param textColor
   *          The font foreground color
   * @param style
   *          The style identifier
   * 
   * @return The created attribute set
   */
  public AttributeSet createStyle(Color textColor, int style) {
    return createStyle(textColor, Color.white, style);
  }

  /**
   * Create the attribute set with the supplied forground color, background
   * color and style
   * 
   * @param textColor
   *          The font foreground color
   * @param backgroundColor
   *          The background color
   * @param style
   *          The style identifier
   * 
   * @return The created attribute set
   */
  public AttributeSet createStyle(Color textColor, Color backgroundColor,
      int style) {
    return createStyle(textColor, backgroundColor, isTrue(style, BOLD),
        isTrue(style, ITALIC), isTrue(style, UNDERLINE));
  }

  /**
   * 
   * Create the attribute set with the supplied forground color, background
   * color and style features
   * 
   * @param textColor
   *          The font foreground color
   * @param backgroundColor
   *          The background color
   * @param bold
   *          Bold style on/off
   * @param italic
   *          Italic setting on/off
   * @param underline
   *          Underlined setting on/off
   * 
   * @return The created attribute set
   */
  private AttributeSet createStyle(Color textColor, Color backgroundColor,
      boolean bold, boolean italic,
      boolean underline) {
    AttributeSet attributeSet;

    attributeSet = new SimpleAttributeSet();
    StyleConstants.setForeground((SimpleAttributeSet) attributeSet, textColor);
    StyleConstants.setBackground((SimpleAttributeSet) attributeSet,
        backgroundColor);

    if (bold) {
      StyleConstants.setBold((SimpleAttributeSet) attributeSet, true);
    }

    if (italic) {
      StyleConstants.setItalic((SimpleAttributeSet) attributeSet, true);
    }

    if (underline) {
      StyleConstants.setUnderline((SimpleAttributeSet) attributeSet, true);
    }

    // StyleConstants.setFontSize((SimpleAttributeSet)attributeSet, fontSize);

    return attributeSet;
  }

  /**
   * Check whether a given style is part of the attributes
   * 
   * @param style The style id
   * @param attribute The attribute setting
   * 
   * @return True if the style is included in the attribute
   */
  private boolean isTrue(int style, int attribute) {
    return (style & attribute) > 0;
  }
}
