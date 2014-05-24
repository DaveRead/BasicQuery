package us.daveread.basicquery.gui;

import javax.swing.*;

/**
 * <p>Title: FontChooser</p>
 *
 * <p>Description:
 * A font chooser that allows users to pick a font by name, size, style, and
 * color.  The color selection is provided by a JColorChooser pane.  This
 * dialog builds an AttributeSet suitable for use with JTextPane.
 * </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author David Read
 * @version $Id: FontChooser.java,v 1.2 2006/05/18 22:16:57 daveread Exp $
 */

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import us.daveread.basicquery.util.Resources;

public class FontChooser extends JDialog implements ActionListener, KeyListener {

//  private JColorChooser colorChooser;
  private JComboBox fontName;
  private JCheckBox fontBold, fontItalic;
  private JTextField fontSize;
  private JLabel previewLabel;
  private SimpleAttributeSet attributes;
  private Font newFont;
//  private Color newColor;

  public FontChooser(Frame parent) {
    super(parent, Resources.getString("dlgFontChooserTitle"), true);
    Font currentFont;

    currentFont = parent.getFont();

//    setSize(450, 450);
    attributes = new SimpleAttributeSet();
    StyleConstants.setBold(attributes, currentFont.isBold());
    StyleConstants.setItalic(attributes, currentFont.isItalic());
    StyleConstants.setFontFamily(attributes, currentFont.getFamily());
    StyleConstants.setFontSize(attributes, currentFont.getSize());

    // Make sure that any way the user cancels the window does the right thing
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        closeAndCancel();
      }
    });

    // Start the long process of setting up our interface
    Container c = getContentPane();

    String[] fontFamilies;
    fontFamilies = getFontFamilies();
    JPanel fontPanel = new JPanel();
    //    fontName = new JComboBox(new String[] {"TimesRoman",
//                                           "Helvetica", "Courier"});
    fontName = new JComboBox(fontFamilies);
    fontName.setSelectedItem(currentFont.getFamily());
    fontName.addActionListener(this);
    fontSize = new JTextField(currentFont.getSize() + "", 4);
    fontSize.setHorizontalAlignment(SwingConstants.RIGHT);
//    fontSize.addActionListener(this);
    fontSize.addKeyListener(this);
    fontBold = new JCheckBox(Resources.getString("dlgFontChooserBold"));
    fontBold.setSelected(StyleConstants.isBold(attributes));
    fontBold.addActionListener(this);
    fontItalic = new JCheckBox(Resources.getString("dlgFontChooserItalic"));
    fontItalic.setSelected(StyleConstants.isItalic(attributes));
    fontItalic.addActionListener(this);

    fontPanel.add(fontName);
    fontPanel.add(new JLabel(Resources.getString("dlgFontChooserFontSize")));
    fontPanel.add(fontSize);
    fontPanel.add(fontBold);
    fontPanel.add(fontItalic);

    c.add(fontPanel, BorderLayout.NORTH);

    // Set up the color chooser panel and attach a change listener so that color
    // updates get reflected in our preview label.
//    colorChooser = new JColorChooser(Color.black);
//    colorChooser.getSelectionModel()
//                .addChangeListener(new ChangeListener() {
//      public void stateChanged(ChangeEvent e) {
//        updatePreviewColor();
//      }
//    });
//    c.add(colorChooser, BorderLayout.CENTER);

    JPanel previewPanel = new JPanel(new BorderLayout());
    previewLabel = new JLabel(Resources.getString("dlgFontChooserSampleText"));
//    previewLabel.setForeground(colorChooser.getColor());
    previewPanel.add(previewLabel, BorderLayout.CENTER);

    // Add in the Ok and Cancel buttons for our dialog box
    JButton okButton = new JButton(Resources.getString("dlgFontChooserOkay"));
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeAndSave();
      }
    });
    JButton cancelButton = new JButton(Resources.getString(
        "dlgFontChooserCancel"));
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeAndCancel();
      }
    });

    JPanel controlPanel = new JPanel();
    controlPanel.add(okButton);
    controlPanel.add(cancelButton);
    previewPanel.add(controlPanel, BorderLayout.SOUTH);

    // Give the preview label room to grow.
    previewPanel.setMinimumSize(new Dimension(100, 100));
    previewPanel.setPreferredSize(new Dimension(100, 100));

    c.add(previewPanel, BorderLayout.SOUTH);

    pack();
    updatePreviewFont();
    GUIUtility.Center(this, parent);
  }

  private String[] getFontFamilies() {
    String[] fontFamilies;
    Font[] fonts;
    Map mapFamilies;
    java.util.List lstFamilies;
    Iterator itrFamilies;

    fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

    mapFamilies = new HashMap();

    // Get the unique list of font families
    for (int index = 0; index < fonts.length; ++index) {
      mapFamilies.put(fonts[index].getFamily(), null);
    }

    lstFamilies = new ArrayList();
    itrFamilies = mapFamilies.keySet().iterator();

    while (itrFamilies.hasNext()) {
      lstFamilies.add(itrFamilies.next());
    }

    Collections.sort(lstFamilies);
    fontFamilies = (String[])lstFamilies.toArray(new String[lstFamilies.size()]);

    return fontFamilies;
  }

  // Ok, something in the font changed, so figure that out and make a
  // new font for the preview label
  public void actionPerformed(ActionEvent ae) {
    // Check the name of the font
    if (!StyleConstants.getFontFamily(attributes)
        .equals(fontName.getSelectedItem())) {
      StyleConstants.setFontFamily(attributes,
          (String)fontName.getSelectedItem());
    }
    // Check the font size (no error checking yet)
//    if (StyleConstants.getFontSize(attributes) !=
//                                   Integer.parseInt(fontSize.getText())) {
//      StyleConstants.setFontSize(attributes,
//                                 Integer.parseInt(fontSize.getText()));
//    }
    // Check to see if the font should be bold
    if (StyleConstants.isBold(attributes) != fontBold.isSelected()) {
      StyleConstants.setBold(attributes, fontBold.isSelected());
    }
    // Check to see if the font should be italic
    if (StyleConstants.isItalic(attributes) != fontItalic.isSelected()) {
      StyleConstants.setItalic(attributes, fontItalic.isSelected());
    }
    // and update our preview label
    updatePreviewFont();
  }

  // Get the appropriate font from our attributes object and update
  // the preview label
  protected void updatePreviewFont() {
    String name = StyleConstants.getFontFamily(attributes);
    boolean bold = StyleConstants.isBold(attributes);
    boolean ital = StyleConstants.isItalic(attributes);
    int size = StyleConstants.getFontSize(attributes);

    //Bold and italic don’t work properly in beta 4.
    newFont = new Font(name, (bold ? Font.BOLD : 0) +
        (ital ? Font.ITALIC : 0), size);
    previewLabel.setFont(newFont);
  }

  // Get the appropriate color from our chooser and update previewLabel
//  protected void updatePreviewColor() {
//    previewLabel.setForeground(colorChooser.getColor());
  // Manually force the label to repaint
//    previewLabel.repaint();
//  }
  public Font getNewFont() {
    return newFont;
  }

//  public Color getNewColor() { return newColor; }
//  public AttributeSet getAttributes() {
//    return attributes;
//  }

  private void closeAndSave() {
    // Save font & color information
//    newFont = previewLabel.getFont();
//    newColor = previewLabel.getForeground();

    // Close the window
    setVisible(false);
    dispose();
  }

  private void closeAndCancel() {
    // Erase any font information and then close the window
    newFont = null;
//    newColor = null;
    setVisible(false);
    dispose();
  }

  public void keyPressed(KeyEvent e) {
  }

  public void keyReleased(KeyEvent e) {
    try {
      if (StyleConstants.getFontSize(attributes) !=
          Integer.parseInt(fontSize.getText())) {
        StyleConstants.setFontSize(attributes,
            Integer.parseInt(fontSize.getText()));
        updatePreviewFont();
      }
    }
    catch (Throwable any) {
      // Not a legal font size value - maybe empty
      // Don't bother user with this since they may be in the middle of typing
      // a font size
    }
  }

  public void keyTyped(KeyEvent e) {
  }
}
