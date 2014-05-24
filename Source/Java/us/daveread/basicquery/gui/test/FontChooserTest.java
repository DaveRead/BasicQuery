package us.daveread.basicquery.gui.test;

import junit.framework.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import org.apache.log4j.Logger;

import us.daveread.basicquery.gui.FontChooser;

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
 * @version $Id: FontChooserTest.java,v 1.1 2006/05/18 22:17:23 daveread Exp $
 */
public class FontChooserTest extends TestCase {
  private static final Logger logger = Logger.getLogger(FontChooserTest.class);
  private FontChooser chooser;
  private JButton ok, cancel;
  private JComboBox fontName;
  private JTextField fontSize;
  private JCheckBox bold, italic;

  public FontChooserTest() {
  }

  public void setUp() {
    Frame dummyFrame;

    dummyFrame = new Frame();
    dummyFrame.setFont(new Font("Arial", 0, 12));

    chooser = new FontChooser(dummyFrame);

    findControls();
  }

  public void testGetNewFont() {
    assertNotNull(chooser.getNewFont());
    closeDialog();
    assertNotNull(chooser.getNewFont());
  }

  public void testCancel() {
    assertNotNull(chooser.getNewFont());
    cancelDialog();
    assertNull(chooser.getNewFont());
  }

  public void testWindowClose() {
    assertNotNull(chooser.getNewFont());
    chooser.getWindowListeners()[0].windowClosing(new WindowEvent(chooser,
        WindowEvent.WINDOW_CLOSING));
    assertNull(chooser.getNewFont());
  }

  public void testFontSizeControl() {
    assertFalse(chooser.getNewFont().getSize() == 50);
    fontSize.setText("50");
    fontSize.getKeyListeners()[0].keyReleased(new KeyEvent(fontSize,
        KeyEvent.KEY_TYPED, new Date().getTime(), 0, 0, '0'));
    assertTrue(chooser.getNewFont().getSize() == 50);
  }

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

  public void testFontNameControl() {
    String family;

    fontName.setSelectedIndex(0);
    family = chooser.getNewFont().getFamily();

    fontName.setSelectedIndex(fontName.getItemCount() - 1);

    assertTrue(!family.equals(chooser.getNewFont().getFamily()));
  }

  private void findControls() {
    Component[] components;
    int findOk;

    components = chooser.getComponents();

    for (findOk = 0; findOk < components.length; ++findOk) {
      logger.debug("Top Level Component Class: " + components[findOk].getClass());
      if (components[findOk] instanceof javax.swing.JRootPane) {
        processContainer((JRootPane)components[findOk], 0);
      }
    }
  }

  private void processContainer(JRootPane pane, int level) {
    Component[] paneComponents = pane.getComponents();

    displayComponents(paneComponents, level + 1);
  }

  private void processContainer(JPanel panel, int level) {
    Component[] panelComponents = panel.getComponents();

    displayComponents(panelComponents, level + 1);
  }

  private void processContainer(JLayeredPane pane, int level) {
    Component[] paneComponents = pane.getComponents();

    displayComponents(paneComponents, level + 1);
  }

  private void processJButton(JButton button, int level) {
    if (button.getText().equalsIgnoreCase("ok")) {
      ok = button;
    } else if (button.getText().equalsIgnoreCase("cancel")) {
      cancel = button;
    }

    logger.debug("Level " + level + " JButton: " + button.getName() + ", " +
        button.getText());
  }

  private void processJCheckBox(JCheckBox box, int level) {
    if (box.getText().equalsIgnoreCase("bold")) {
      bold = box;
    } else if (box.getText().equalsIgnoreCase("italic")) {
      italic = box;
    }

    logger.debug("Level " + level + " JCheckBox: " + box.getName() + ", " +
        box.getText());
  }

  private void processJComboBox(JComboBox box, int level) {
    fontName = box;

    logger.debug("Level " + level + " JComboBox: " + box.getName());
  }

  private void processJTextField(JTextField field, int level) {
    fontSize = field;

    logger.debug("Level " + level + " JTextField: " + field.getName());
  }

  private void displayComponents(Component[] components, int level) {
    for (int findButton = 0; findButton < components.length; ++findButton) {

      if (components[findButton] instanceof javax.swing.JRootPane) {
        processContainer((JRootPane)components[findButton], level + 1);
      } else if (components[findButton] instanceof javax.swing.JPanel) {
        processContainer((JPanel)components[findButton], level + 1);
      } else if (components[findButton] instanceof javax.swing.JLayeredPane) {
        processContainer((JLayeredPane)components[findButton], level + 1);
      } else if (components[findButton] instanceof javax.swing.JButton) {
        processJButton((JButton)components[findButton], level + 1);
      } else if (components[findButton] instanceof javax.swing.JCheckBox) {
        processJCheckBox((JCheckBox)components[findButton], level + 1);
      } else if (components[findButton] instanceof javax.swing.JComboBox) {
        processJComboBox((JComboBox)components[findButton], level + 1);
      } else if (components[findButton] instanceof javax.swing.JTextField) {
        processJTextField((JTextField)components[findButton], level + 1);
      } else {
        logger.debug("Level " + level + " Child Component: " +
            components[findButton].toString());
      }
    }
  }

  private void closeDialog() {
    ActionEvent evt;

    evt = new ActionEvent(ok, ActionEvent.ACTION_PERFORMED, "Clicked");

    ok.getActionListeners()[0].actionPerformed(evt);
  }

  private void cancelDialog() {
    ActionEvent evt;

    evt = new ActionEvent(cancel, ActionEvent.ACTION_PERFORMED, "Clicked");

    cancel.getActionListeners()[0].actionPerformed(evt);
  }
}
