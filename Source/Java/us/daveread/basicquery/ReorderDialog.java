package us.daveread.basicquery;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;

import org.apache.log4j.Logger;

import us.daveread.basicquery.gui.GUIUtility;
import us.daveread.basicquery.util.Resources;

/**
 * <p>Title: ReorderDialog</p>
 * <p>Description: Display list of objects and allow them to be reordered.</p>
 * <p>Copyright: Copyright (c) 2004, David Read</p>
 * <p>This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.</p>
 *  <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.</p>
 *  <p>You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA</p>
 * <p></p>
 * @author David Read
 * @version $Id: ReorderDialog.java,v 1.3 2006/05/04 03:43:20 daveread Exp $
 */

public class ReorderDialog extends JDialog implements ActionListener,
    WindowListener,
    ListSelectionListener {
  private static final Logger logger = Logger.getLogger(ReorderDialog.class);

  private List ordered;
  private boolean didReorder;
  private JButton up, down, close, cancel;
  private JList reordered;
  private ReorderListModel reorderedModel;

  /**
   * Constructs the JDialog as a modal dialog and sets-up the components.  Finally
   * it makes itself visible.
   * @param parent JFrame The parent JFrame that owns this dialog.
   * @param objects List The list of data to be presented in the list.
   * @throws HeadlessException
   */
  public ReorderDialog(JFrame parent, List objects) throws HeadlessException {
    super(parent, true);
    ordered = objects;

    addWindowListener(this);
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    setTitle(Resources.getString("dlgReorderDlgTitle"));
    setup();
    pack();

    if (getWidth() > parent.getWidth()) {
      setSize(parent.getWidth(), getHeight());
    }

    GUIUtility.Center(this, parent);

    setVisible(true);
  }

  /**
   * Setup the GUI components.
   */
  private void setup() {
    JPanel buttons;
    getContentPane().setLayout(new BorderLayout());

    // Center is list of objects;
    getContentPane().add(new JScrollPane(reordered = new JList(reorderedModel =
        new ReorderListModel(ordered))), BorderLayout.CENTER);
    reordered.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    reordered.addListSelectionListener(this);
    reordered.setVisibleRowCount(ordered.size() < 20 ? ordered.size() : 20);

    // Buttons
    buttons = new JPanel();
    buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
    buttons.add(up = new JButton(Resources.getString("ctlReorderSQLMoveUp")));
    up.addActionListener(this);
    up.setEnabled(false);
    buttons.add(down = new JButton(Resources.getString("ctlReorderSQLMoveDown")));
    down.addActionListener(this);
    down.setEnabled(false);
    buttons.add(close = new JButton(Resources.getString("ctlReorderSQLOkay")));
    close.addActionListener(this);
    buttons.add(cancel = new JButton(Resources.getString("ctlReorderSQLCancel")));
    cancel.addActionListener(this);
    getContentPane().add(buttons, BorderLayout.SOUTH);
  }

  /**
   * User cancelled changes.  This method clears the reorder boolean and
   * disposes of the dialog.
   */
  private void cancelDialog() {
    didReorder = false;
    dispose();
  }

  /**
   * User requested that the dialog be closed.  The current list, which may
   * or may not be reordered from its original state, is obtrained from the model
   * and the dialog is closed.
   */
  private void closeDialog() {
    ordered = reorderedModel.getList();
    dispose();
  }

  /**
   * Move the selected entry up one position (move to lower index value) which
   * in the GUI will result in the selected line moving up one line vertically.
   */
  private void moveUp() {
    reorderedModel.moveUp(reordered.getSelectedIndex());
    reordered.setSelectedIndex(reordered.getSelectedIndex() - 1);
    reordered.ensureIndexIsVisible(reordered.getSelectedIndex());
    didReorder = true;
  }

  /**
   * Move the selected entry down one position (move to higher index value) which
   * in the GUI will result in the selected line moving down one line vertically.
   */
  private void moveDown() {
    reorderedModel.moveDown(reordered.getSelectedIndex());
    reordered.setSelectedIndex(reordered.getSelectedIndex() + 1);
    reordered.ensureIndexIsVisible(reordered.getSelectedIndex());
    didReorder = true;
  }

  /**
   * Get the list that backs the model.
   * @return List The list which may have been reordered.
   */
  public List getAsOrdered() {
    return ordered;
  }

  /**
   * Return whether or not the list was reordered from its original state.
   * @return boolean Whether the list was reordered.
   */
  public boolean isReordered() {
    return didReorder;
  }

  // Begin ActionListener Interface

  /**
   * Depending on which option the user has invoked the
   * appropriate action is performed
   *
   * @param evt Specifies the action event that has taken place
   */
  public void actionPerformed(ActionEvent evt) {
    if (evt.getSource() == cancel) {
      cancelDialog();
    } else if (evt.getSource() == close) {
      closeDialog();
    } else if (evt.getSource() == up) {
      moveUp();
    } else if (evt.getSource() == down) {
      moveDown();
    }
  }

  // End ActionListener Interface

  // Begin ListSelectionListener Interface

  public void valueChanged(ListSelectionEvent evt) {
    if (reordered.getSelectedIndex() != -1) {
      up.setEnabled(reordered.getSelectedIndex() != 0);
      down.setEnabled(reordered.getSelectedIndex() <
          reordered.getModel().getSize() - 1);
    }
  }

  // End ListSelectionListener Interface

  // Begin WindowListener Interface

  /**
   * Invoked when the Window is set to be the active Window.
   *
   * @param evt  The event that specifies that the window is activated
   */
  public void windowActivated(WindowEvent evt) {
  }

  /**
   * Invoked when a window has been closed as the result of calling
   * dispose on the window.
   *
   * @param evt  The Window event
   */
  public void windowClosed(WindowEvent evt) {
  }

  /**
   * Invoked when the user attempts to close the window from the
   * window's system menu.Exits the application once the window
   * is closed
   *
   * @param evt The event that specifies the closing of the window
   */
  public void windowClosing(WindowEvent evt) {
    try {
      dispose();
    }
    catch (Throwable any) {
      // This fails periodically with a NP exception
      // from Container.removeNotify
      logger.error("Failure during dialog clean-up", any);
    }
  }

  /**
   * Invoked when a Window is no longer the active Window
   *
   * @param evt  The event that specifies that the window is deactivated
   */
  public void windowDeactivated(WindowEvent evt) {
  }

  /**
   * Invoked when a window is changed from a minimized to a normal state.
   *
   * @param evt  The event that specifies that the window is deiconified
   */
  public void windowDeiconified(WindowEvent evt) {
  }

  /**
   * Invoked when a window is changed from a normal to a minimized state
   *
   * @param evt  The event that specifies that the window is iconified
   */
  public void windowIconified(WindowEvent evt) {
  }

  /**
   * Creates a new thread that attaches focus to the window that has been
   * opened and also places the cursor on the textfield for the userid
   *
   * @param evt The event that specifies the opening of the window
   */
  public void windowOpened(WindowEvent evt) {
  }

  // End WindowListener Interface
}
