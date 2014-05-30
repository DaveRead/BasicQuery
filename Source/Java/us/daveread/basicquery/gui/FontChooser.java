package us.daveread.basicquery.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.log4j.Logger;

import us.daveread.basicquery.util.Resources;

/**
 * <p>
 * Title: FontChooser
 * </p>
 * 
 * <p>
 * Description: A font chooser that allows users to pick a font by name, size,
 * style, and color. The color selection is provided by a JColorChooser pane.
 * This dialog builds an AttributeSet suitable for use with JTextPane.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author David Read
 */
public class FontChooser extends JDialog implements ActionListener, KeyListener {
  /**
   * Serial UID
   */
  private static final long serialVersionUID = -6831041847770546773L;

  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(FontChooser.class);

  // private JColorChooser colorChooser;
  /**
   * The font name choice
   */
  private JComboBox fontName;

  /**
   * The font bold selection
   */
  private JCheckBox fontBold;

  /**
   * The font italic selection
   */
  private JCheckBox fontItalic;

  /**
   * The font size setting
   */
  private JTextField fontSize;

  /**
   * The preview area
   */
  private JLabel previewLabel;

  /**
   * The font attributes
   */
  private SimpleAttributeSet attributes;

  /**
   * The chosen font
   */
  private Font newFont;

  // private Color newColor;

  /**
   * Setup a font chooser dialog with the default font information
   * 
   * @param parent
   *          The parent frame for this dialog
   */
  public FontChooser(Frame parent) {
    super(parent, Resources.getString("dlgFontChooserTitle"), true);
    Font currentFont;

    currentFont = parent.getFont();

    // setSize(450, 450);
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
    final Container container = getContentPane();

    String[] fontFamilies;
    fontFamilies = getFontFamilies();
    final JPanel fontPanel = new JPanel();

    // fontName = new JComboBox(new String[] {"TimesRoman",
    // "Helvetica", "Courier"});
    fontName = new JComboBox(fontFamilies);
    fontName.setSelectedItem(currentFont.getFamily());
    fontName.addActionListener(this);

    fontSize = new JTextField(currentFont.getSize() + "", 4);
    fontSize.setHorizontalAlignment(SwingConstants.RIGHT);
    // fontSize.addActionListener(this);
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

    container.add(fontPanel, BorderLayout.NORTH);

    // Set up the color chooser panel and attach a change listener so that color
    // updates get reflected in our preview label.
    // colorChooser = new JColorChooser(Color.black);
    // colorChooser.getSelectionModel()
    // .addChangeListener(new ChangeListener() {
    // public void stateChanged(ChangeEvent e) {
    // updatePreviewColor();
    // }
    // });
    // c.add(colorChooser, BorderLayout.CENTER);

    final JPanel previewPanel = new JPanel(new BorderLayout());
    previewLabel = new JLabel(Resources.getString("dlgFontChooserSampleText"));
    // previewLabel.setForeground(colorChooser.getColor());
    previewPanel.add(previewLabel, BorderLayout.CENTER);

    // Add in the Ok and Cancel buttons for our dialog box
    final JButton okButton = new JButton(
        Resources.getString("dlgFontChooserOkay"));
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeAndSave();
      }
    });
    final JButton cancelButton = new JButton(Resources.getString(
        "dlgFontChooserCancel"));
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeAndCancel();
      }
    });

    final JPanel controlPanel = new JPanel();
    controlPanel.add(okButton);
    controlPanel.add(cancelButton);
    previewPanel.add(controlPanel, BorderLayout.SOUTH);

    // Give the preview label room to grow.
    previewPanel.setMinimumSize(new Dimension(100, 100));
    previewPanel.setPreferredSize(new Dimension(100, 100));

    container.add(previewPanel, BorderLayout.SOUTH);

    pack();
    updatePreviewFont();
    GUIUtility.center(this, parent);
  }

  /**
   * Get the names of available font families
   * 
   * @return Font family names installed locally
   */
  private String[] getFontFamilies() {
    String[] fontFamilies;
    Font[] fonts;
    Map<String, String> mapFamilies;
    List<String> lstFamilies;
    Iterator<String> itrFamilies;

    fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

    mapFamilies = new HashMap<String, String>();

    // Get the unique list of font families
    for (int index = 0; index < fonts.length; ++index) {
      mapFamilies.put(fonts[index].getFamily(), null);
    }

    lstFamilies = new ArrayList<String>();
    itrFamilies = mapFamilies.keySet().iterator();

    while (itrFamilies.hasNext()) {
      lstFamilies.add(itrFamilies.next());
    }

    Collections.sort(lstFamilies);
    fontFamilies = lstFamilies.toArray(new String[lstFamilies.size()]);

    return fontFamilies;
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    // Check the name of the font
    if (!StyleConstants.getFontFamily(attributes)
        .equals(fontName.getSelectedItem())) {
      StyleConstants.setFontFamily(attributes,
          (String) fontName.getSelectedItem());
    }
    // Check the font size (no error checking yet)
    // if (StyleConstants.getFontSize(attributes) !=
    // Integer.parseInt(fontSize.getText())) {
    // StyleConstants.setFontSize(attributes,
    // Integer.parseInt(fontSize.getText()));
    // }
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

  /**
   * Get the appropriate font from our attributes object and update the preview
   * label
   */
  protected void updatePreviewFont() {
    final String name = StyleConstants.getFontFamily(attributes);
    final boolean bold = StyleConstants.isBold(attributes);
    final boolean ital = StyleConstants.isItalic(attributes);
    final int size = StyleConstants.getFontSize(attributes);

    // Bold and italic don’t work properly in beta 4.
    newFont = new Font(name, (bold ? Font.BOLD : 0)
        + (ital ? Font.ITALIC : 0), size);
    previewLabel.setFont(newFont);
  }

  /**
   * Get the chosen font
   * 
   * @return The chosen font
   */
  public Font getNewFont() {
    return newFont;
  }

  /**
   * Close the dialog, saving the choices made by the user
   */
  private void closeAndSave() {
    setVisible(false);
    dispose();
  }

  /**
   * Close the dalog, discarding the choices made by the user
   */
  private void closeAndCancel() {
    newFont = null;

    setVisible(false);
    dispose();
  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
    try {
      if (StyleConstants.getFontSize(attributes) != Integer.parseInt(fontSize
          .getText())) {
        StyleConstants.setFontSize(attributes,
            Integer.parseInt(fontSize.getText()));
        updatePreviewFont();
      }
    } catch (Throwable any) {
      // Not a legal font size value - maybe empty
      // Don't bother user with this since they may be in the middle of typing
      // a font size
      if (LOGGER.isDebugEnabled()) {
        LOGGER
            .debug(
                "Error parsing the font size - may be in the process of being edited",
                any);
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }
}
