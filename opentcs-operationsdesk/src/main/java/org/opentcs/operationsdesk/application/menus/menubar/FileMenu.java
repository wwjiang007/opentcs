// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.operationsdesk.application.menus.menubar;

import static java.util.Objects.requireNonNull;
import static org.opentcs.operationsdesk.event.KernelStateChangeEvent.State.LOGGED_IN;

import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.opentcs.customizations.ApplicationEventBus;
import org.opentcs.guing.common.application.action.file.ModelPropertiesAction;
import org.opentcs.guing.common.application.action.file.SaveModelAction;
import org.opentcs.guing.common.application.action.file.SaveModelAsAction;
import org.opentcs.operationsdesk.application.action.ViewActionMap;
import org.opentcs.operationsdesk.application.action.actions.ConnectToKernelAction;
import org.opentcs.operationsdesk.application.action.actions.DisconnectFromKernelAction;
import org.opentcs.operationsdesk.event.KernelStateChangeEvent;
import org.opentcs.operationsdesk.util.I18nPlantOverviewOperating;
import org.opentcs.thirdparty.guing.common.jhotdraw.util.ResourceBundleUtil;
import org.opentcs.thirdparty.operationsdesk.jhotdraw.application.action.file.CloseFileAction;
import org.opentcs.util.event.EventHandler;
import org.opentcs.util.event.EventSource;

/**
 * The application's "File" menu.
 */
public class FileMenu
    extends
      JMenu
    implements
      EventHandler {

  /**
   * A menu item for persisting the kernel's current model.
   */
  private final JMenuItem menuItemSaveModel;
  /**
   * A menu item for persisting the kernel's current model with a new name.
   */
  private final JMenuItem menuItemSaveModelAs;
  /**
   * A menu item for connecting to a kernel.
   */
  private final JMenuItem menuItemConnect;
  /**
   * A menu item for disconnecting from the kernel.
   */
  private final JMenuItem menuItemDisconnect;
  /**
   * A menu item for showing the current model's properties.
   */
  private final JMenuItem menuItemModelProperties;
  /**
   * A menu item for closing the application.
   */
  private final JMenuItem menuItemClose;

  /**
   * Creates a new instance.
   *
   * @param actionMap The application's action map.
   * @param eventSource Where this instance registers for application events.
   */
  @Inject
  @SuppressWarnings("this-escape")
  public FileMenu(
      ViewActionMap actionMap,
      @Nonnull
      @ApplicationEventBus
      EventSource eventSource
  ) {
    requireNonNull(actionMap, "actionMap");
    requireNonNull(eventSource, "eventSource");

    final ResourceBundleUtil labels
        = ResourceBundleUtil.getBundle(I18nPlantOverviewOperating.MENU_PATH);

    this.setText(labels.getString("fileMenu.text"));
    this.setToolTipText(labels.getString("fileMenu.tooltipText"));
    this.setMnemonic('F');

    // Menu item File -> Save Model
    menuItemSaveModel = new JMenuItem(actionMap.get(SaveModelAction.ID));
    add(menuItemSaveModel);

    // Menu item File -> Save Model As
    menuItemSaveModelAs = new JMenuItem(actionMap.get(SaveModelAsAction.ID));
    add(menuItemSaveModelAs);

    addSeparator();

    menuItemConnect = new JMenuItem(actionMap.get(ConnectToKernelAction.ID));
    menuItemConnect.setEnabled(true);
    add(menuItemConnect);

    menuItemDisconnect = new JMenuItem(actionMap.get(DisconnectFromKernelAction.ID));
    menuItemDisconnect.setEnabled(false);
    add(menuItemDisconnect);

    addSeparator();

    menuItemModelProperties = new JMenuItem(actionMap.get(ModelPropertiesAction.ID));
    add(menuItemModelProperties);

    addSeparator();

    // Menu item File -> Close
    menuItemClose = new JMenuItem(actionMap.get(CloseFileAction.ID));
    add(menuItemClose); // TODO: Nur bei "Stand-Alone" Frame

    eventSource.subscribe(this);
  }

  @Override
  public void onEvent(Object event) {
    if (event instanceof KernelStateChangeEvent kernelStateChangeEvent) {
      handleKernelStateChangeEvent(kernelStateChangeEvent);
    }
  }

  private void handleKernelStateChangeEvent(KernelStateChangeEvent event) {
    switch (event.getNewState()) {
      case LOGGED_IN:
        menuItemConnect.setEnabled(false);
        menuItemDisconnect.setEnabled(true);
        break;
      case DISCONNECTED:
        menuItemConnect.setEnabled(true);
        menuItemDisconnect.setEnabled(false);
        break;
      default:
        // Do nothing.
    }
  }
}
