/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.guing.common.components;

/**
 */
public interface EditableComponent
    extends
      org.jhotdraw.gui.EditableComponent {

  /**
   * Delete the components that are currently selected in the tree and save
   * them to allow restoring by a Paste operation.
   */
  void cutSelectedItems();

  /**
   * Save the components that are currently selected in the tree
   * to allow creating a clone by a Paste operation.
   */
  void copySelectedItems();

  /**
   *
   */
  void pasteBufferedItems();
}
