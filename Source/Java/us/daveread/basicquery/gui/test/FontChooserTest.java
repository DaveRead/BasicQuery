package us.daveread.basicquery.gui.test;

import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import us.daveread.basicquery.gui.FontChooser;

/**
 * <p>
 * Title: Test the font chooser
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
public class FontChooserTest extends TestCase {
  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(FontChooserTest.class);

  /**
   * Default font size to choose
   */
  private static final int DEFAULT_FONT_SIZE = 12;

  /**
   * The font chooser instance
   */
  private FontChooser chooser;

  /**
   * Ok button - close dialog and keep changes
   */
  private JButton ok;

  /**
   * Cancel button - close dialog and discard changes
   */
  private JButton cancel;

  /**
   * Font name choices
   */
  private JComboBox<String> fontName;

  /**
   * Font size entry
   */
  private JTextField fontSize;

  /**
   * Attribute: bold
   */
  private JCheckBox bold;

  /**
   * Attribute: italic
   */
  private JCheckBox italic;

  /**
   * Setup the test class
   */
  public FontChooserTest() {
  }

  /**
   * Setup the test
   */
  public void setUp() {
    Frame dummyFrame;

    dummyFrame = new Frame();
    dummyFrame.setFont(new Font("Arial", 0, DEFAULT_FONT_SIZE));

    chooser = new FontChooser(dummyFrame);

    findControls();
  }

  /**
   * Test getting the new font
   */
  public void testGetNewFont() {
    assertNotNull(chooser.getNewFont());
    closeDialog();
    assertNotNull(chooser.getNewFont());
  }

  /**
   * Test canceling the dialog
   */
  public void testCancel() {
    assertNotNull(chooser.getNewFont());
    cancelDialog();
    assertNull(chooser.getNewFont());
  }

  /**
   * Test closing the dialog directly
   */
  public void testWindowClose() {
    assertNotNull(chooser.getNewFont());
    chooser.getWindowListeners()[0].windowClosing(new WindowEvent(chooser,
        WindowEvent.WINDOW_CLOSING));
    assertNull(chooser.getNewFont());
  }

  /**
   * Test choosing a font size
   */
  public void testFontSizeControl() {
    assertFalse(chooser.getNewFont().getSize() == 50);
    fontSize.setText("50");
    fontSize.getKeyListeners()[0].keyReleased(new KeyEvent(fontSize,
        KeyEvent.KEY_TYPED, new Date().getTime(), 0, 0, '0'));
    assertTrue(chooser.getNewFont().getSize() == 50);
  }

  /**
   * Test selecting the bold checkbox
   */
  public void testBoldControl() {
    bold.setSelected(false);
    bold.getActionListeners()[0].actionPerformed(new ActionEvent(bold,
        ActionEvent.ACTION_PERFORMED, "Unchecked"));
    assertFalse(chooser.getNewFont().isBold());
    bold.setSelected(true);
    bold.getActionListeners()[0].actionPerformed(new ActionEvent(bold,
        ActionEvent.ACTION_PERFORMED, "Checked"));
    assertTrue(chooser.getNewFont().isBold());
  }

  /**
   * Test selecting the italic checkbox
   */
  public void testItalicControl() {
    italic.setSelected(false);
    italic.getActionListeners()[0].actionPerformed(new ActionEvent(bold,
        ActionEvent.ACTION_PERFORMED, "Unchecked"));
    assertFalse(chooser.getNewFont().isItalic());
    italic.setSelected(true);
    italic.getActionListeners()[0].actionPerformed(new ActionEvent(bold,
        ActionEvent.ACTION_PERFORMED, "Checked"));
    assertTrue(chooser.getNewFont().isItalic());
  }

  /**
   * Test selecting a font name
   */
  public void testFontNameControl() {
    String family;

    fontName.setSelectedIndex(0);
    family = chooser.getNewFont().getFamily();

    fontName.setSelectedIndex(fontName.getItemCount() - 1);

    assertTrue(!family.equals(chooser.getNewFont().getFamily()));
  }

  /**
   * Find the controls on the dialog
   */
  private void findControls() {
    Component[] components;
    int findOk;

    components = chooser.getComponents();

    for (findOk = 0; findOk < components.length; ++findOk) {
      LOGGER.debug("Top Level Component Class: "
          + components[findOk].getClass());
      if (components[findOk] instanceof javax.swing.JRootPane) {
        processContainer((JRootPane) components[findOk], 0);
      }
    }
  }

  /**
   * Obtain the components on the pane and call display on them
   * 
   * @see #displayComponents(Component[], int)
   * 
   * @param pane
   *          The pane containing a set of components
   * @param level
   *          The recursive depth level
   */
  private void processContainer(JRootPane pane, int level) {
    final Component[] paneComponents = pane.getComponents();

    displayComponents(paneComponents, level + 1);
  }

  /**
   * Obtain the components on the panel and call display on them
   * 
   * @see #displayComponents(Component[], int)
   * 
   * @param panel
   *          The panel containing a set of components
   * @param level
   *          The recursive depth level
   */
  private void processContainer(JPanel panel, int level) {
    final Component[] panelComponents = panel.getComponents();

    displayComponents(panelComponents, level + 1);
  }

  /**
   * Obtain the components in a layered pane and call display on them
   * 
   * @see #displayComponents(Component[], int)
   * 
   * @param pane
   *          The layered pan containing a set of components
   * @param level
   *          The recursive depth level
   */
  private void processContainer(JLayeredPane pane, int level) {
    final Component[] paneComponents = pane.getComponents();

    displayComponents(paneComponents, level + 1);
  }

  /**
   * Process the event on a button. Determine which button and take the
   * appropriate action
   * 
   * @param button
   *          The button
   * @param level
   *          The recursive depth level
   */
  private void processJButton(JButton button, int level) {
    if (button.getText().equalsIgnoreCase("ok")) {
      ok = button;
    } else if (button.getText().equalsIgnoreCase("cancel")) {
      cancel = button;
    }

    LOGGER.debug("Level " + level + " JButton: " + button.getName() + ", "
        + button.getText());
  }

  /**
   * Process the event on a checkbox. Determine which checkbox and take the
   * appropriate action
   * 
   * @param box
   *          The checkbox
   * @param level
   *          The recursive depth level
   */
  private void processJCheckBox(JCheckBox box, int level) {
    if (box.getText().equalsIgnoreCase("bold")) {
      bold = box;
    } else if (box.getText().equalsIgnoreCase("italic")) {
      italic = box;
    }

    LOGGER.debug("Level " + level + " JCheckBox: " + box.getName() + ", " 
        + box.getText());
  }

  /**
   * Use the current value in the combobox as the chosen font name
   * 
   * @param box
   *          The combobox
   * @param level
   *          The recursive depth level
   */
  private void processJComboBox(JComboBox<String> box, int level) {
    fontName = box;

    LOGGER.debug("Level " + level + " JComboBox: " + box.getName());
  }

  /**
   * Use the current value of the text field as the font size
   * 
   * @param field
   *          The test field
   * @param level
   *          The recursive depth level
   */
  private void processJTextField(JTextField field, int level) {
    fontSize = field;

    LOGGER.debug("Level " + level + " JTextField: " + field.getName());
  }

  /**
   * Loop through the collection of components and process the component based
   * on the type of component
   * 
   * @see #processContainer(JLayeredPane, int)
   * @see #processContainer(JPanel, int)
   * @see #processContainer(JRootPane, int)
   * @see #processJButton(JButton, int)
   * @see #processJCheckBox(JCheckBox, int)
   * @see #processJComboBox(JComboBox, int)
   * @see #processJTextField(JTextField, int)
   * 
   * @param components The collection of components
   * @param level The recursive depth level
   */
  @SuppressWarnings("unchecked")
  private void displayComponents(Component[] components, int level) {
    for (int findButton = 0; findButton < components.length; ++findButton) {

      if (components[findButton] instanceof javax.swing.JRootPane) {
        processContainer((JRootPane) components[findButton], level + 1);
      } else if (components[findButton] instanceof javax.swing.JPanel) {
        processContainer((JPanel) components[findButton], level + 1);
      } else if (components[findButton] instanceof javax.swing.JLayeredPane) {
        processContainer((JLayeredPane) components[findButton], level + 1);
      } else if (components[findButton] instanceof javax.swing.JButton) {
        processJButton((JButton) components[findButton], level + 1);
      } else if (components[findButton] instanceof javax.swing.JCheckBox) {
        processJCheckBox((JCheckBox) components[findButton], level + 1);
      } else if (components[findButton] instanceof javax.swing.JComboBox) {
        processJComboBox((JComboBox<String>) components[findButton], level + 1);
      } else if (components[findButton] instanceof javax.swing.JTextField) {
        processJTextField((JTextField) components[findButton], level + 1);
      } else {
        LOGGER.debug("Level " + level + " Child Component: " 
            + components[findButton].toString());
      }
    }
  }

  /**
   * Mimic pressing the Ok button
   */
  private void closeDialog() {
    ActionEvent evt;

    evt = new ActionEvent(ok, ActionEvent.ACTION_PERFORMED, "Clicked");

    ok.getActionListeners()[0].actionPerformed(evt);
  }

  /**
   * Mimic pressing the Cancel button
   */
  private void cancelDialog() {
    ActionEvent evt;

    evt = new ActionEvent(cancel, ActionEvent.ACTION_PERFORMED, "Clicked");

    cancel.getActionListeners()[0].actionPerformed(evt);
  }
}
