/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.guing.plugins.panels.loadgenerator;

import static java.util.Objects.requireNonNull;
import static org.opentcs.guing.plugins.panels.loadgenerator.I18nPlantOverviewPanelLoadGenerator.BUNDLE_PATH;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import java.util.ResourceBundle;
import org.opentcs.access.Kernel;
import org.opentcs.access.SharedKernelServicePortalProvider;
import org.opentcs.components.plantoverview.PluggablePanel;
import org.opentcs.components.plantoverview.PluggablePanelFactory;

/**
 * Creates load generator panels.
 */
public class ContinuousLoadPanelFactory
    implements
      PluggablePanelFactory {

  /**
   * This classe's bundle.
   */
  private final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PATH);
  /**
   * A reference to the shared portal provider.
   */
  private final SharedKernelServicePortalProvider portalProvider;
  /**
   * A provider for the actual panels.
   */
  private final Provider<ContinuousLoadPanel> panelProvider;

  /**
   * Creates a new instance.
   *
   * @param portalProvider The application's portal provider.
   * @param panelProvider A provider for the actual panels.
   */
  @Inject
  public ContinuousLoadPanelFactory(
      SharedKernelServicePortalProvider portalProvider,
      Provider<ContinuousLoadPanel> panelProvider
  ) {
    this.portalProvider = requireNonNull(portalProvider, "portalProvider");
    this.panelProvider = requireNonNull(panelProvider, "panelProvider");
  }

  @Override
  public String getPanelDescription() {
    return bundle.getString("continuousLoadPanelFactory.panelDescription");
  }

  @Override
  public PluggablePanel createPanel(Kernel.State state) {
    if (!providesPanel(state)) {
      return null;
    }

    return panelProvider.get();
  }

  @Override
  public boolean providesPanel(Kernel.State state) {
    return portalProvider != null && Kernel.State.OPERATING.equals(state);
  }
}
