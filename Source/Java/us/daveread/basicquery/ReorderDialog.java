package us.daveread.basicquery;

import java.util.List;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import us.daveread.basicquery.gui.GUIUtility;
import us.daveread.basicquery.util.Resources;

/**
 * <p>
 * Title: ReorderDialog
 * </p>
 * <p>
 * Description: Display list of objects and allow them to be reordered.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004-2014, David Read
 * </p>
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * </p>
 * <p>
 * </p>
 * 
 * @author David Read
 */

public class ReorderDialog extends JDialog implements ActionListener,
    WindowListener,
    ListSelectionListener {
  /**
   * Serial UID for the class
   */
  private static final long serialVersionUID = 1013157031020796234L;

  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(ReorderDialog.class);

  /**
   * Maximum row height of the list display
   */
  private static final int MAXIMUM_ROWS_TO_DISPLAY = 20;

  /**
   * List of objects the user can reorder
   */
  private List<Object> ordered;

  /**
   * Detect if the order of the object was changed by the user
   */
  private boolean didReorder;

  /**
   * Move the selected item up
   */
  private JButton up;

  /**
   * Move the selected item down
   */
  private JButton down;

  /**
   * Close the dialog (accepting any changes made)
   */
  private JButton close;

  /**
   * Cancel the dialog (ignoring any changes made)
   */
  private JButton cancel;

  /**
   * The reordered list of objects
   */
  private JList reordered;

  /**
   * The model backing the reorderable list display
   */
  private ReorderListModel<Object> reorderedModel;

  /**
   * Constructs the JDialog as a modal dialog and sets-up the components.
   * Finally
   * it makes itself visible.
   * 
   * @param parent
   *          JFrame The parent JFrame that owns this dialog.
   * @param objects
   *          List The list of data to be presented in the list.
   */
  public ReorderDialog(JFrame parent, List<Object> objects) {
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

    GUIUtility.center(this, parent);

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
        new ReorderListModel<Object>(ordered))), BorderLayout.CENTER);
    reordered.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    reordered.addListSelectionListener(this);
    reordered
        .setVisibleRowCount(ordered.size() < MAXIMUM_ROWS_TO_DISPLAY ? ordered
            .size() : MAXIMUM_ROWS_TO_DISPLAY);

    // Buttons
    buttons = new JPanel();
    buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
    buttons.add(up = new JButton(Resources.getString("ctlReorderSQLMoveUp")));
    up.addActionListener(this);
    up.setEnabled(false);
    buttons
        .add(down = new JButton(Resources.getString("ctlReorderSQLMoveDown")));
    down.addActionListener(this);
    down.setEnabled(false);
    buttons.add(close = new JButton(Resources.getString("ctlReorderSQLOkay")));
    close.addActionListener(this);
    buttons
        .add(cancel = new JButton(Resources.getString("ctlReorderSQLCancel")));
    cancel.addActionListener(this);
    getContentPane().add(buttons, BorderLayout.SOUTH);
  }

  /**
   * User canceled changes. This method clears the reorder boolean and
   * disposes of the dialog.
   */
  private void cancelDialog() {
    didReorder = false;
    dispose();
  }

  /**
   * User requested that the dialog be closed. The current list, which may
   * or may not be reordered from its original state, is obtrained from the
   * model
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
   * Move the selected entry down one position (move to higher index value)
   * which
   * in the GUI will result in the selected line moving down one line
   * vertically.
   */
  private void moveDown() {
    reorderedModel.moveDown(reordered.getSelectedIndex());
    reordered.setSelectedIndex(reordered.getSelectedIndex() + 1);
    reordered.ensureIndexIsVisible(reordered.getSelectedIndex());
    didReorder = true;
  }

  /**
   * Get the list that backs the model.
   * 
   * @return List The list which may have been reordered.
   */
  public List<Object> getAsOrdered() {
    return ordered;
  }

  /**
   * Return whether or not the list was reordered from its original state.
   * 
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
   * @param evt
   *          Specifies the action event that has taken place
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

  @Override
  public void valueChanged(ListSelectionEvent evt) {
    if (reordered.getSelectedIndex() != -1) {
      up.setEnabled(reordered.getSelectedIndex() != 0);
      down.setEnabled(reordered.getSelectedIndex() 
          < reordered.getModel().getSize() - 1);
    }
  }

  // End ListSelectionListener Interface

  // Begin WindowListener Interface

  @Override
  public void windowActivated(WindowEvent evt) {
  }

  @Override
  public void windowClosed(WindowEvent evt) {
  }

  @Override
  public void windowClosing(WindowEvent evt) {
    try {
      dispose();
    } catch (Throwable any) {
      // This fails periodically with a NP exception
      // from Container.removeNotify
      LOGGER.error("Failure during dialog clean-up", any);
    }
  }

  @Override
  public void windowDeactivated(WindowEvent evt) {
  }

  @Override
  public void windowDeiconified(WindowEvent evt) {
  }

  @Override
  public void windowIconified(WindowEvent evt) {
  }

  @Override
  public void windowOpened(WindowEvent evt) {
  }

  // End WindowListener Interface
}
