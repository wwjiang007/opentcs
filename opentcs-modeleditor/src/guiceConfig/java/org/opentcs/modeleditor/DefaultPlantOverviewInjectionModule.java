// SPDX-FileCopyrightText: The openTCS Authors
// SPDX-License-Identifier: MIT
package org.opentcs.modeleditor;

import com.google.inject.TypeLiteral;
import jakarta.inject.Singleton;
import java.io.File;
import java.util.List;
import java.util.Locale;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.opentcs.access.KernelServicePortal;
import org.opentcs.access.SslParameterSet;
import org.opentcs.access.rmi.KernelServicePortalBuilder;
import org.opentcs.access.rmi.factories.NullSocketFactoryProvider;
import org.opentcs.access.rmi.factories.SecureSocketFactoryProvider;
import org.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.opentcs.common.GuestUserCredentials;
import org.opentcs.components.plantoverview.LocationTheme;
import org.opentcs.customizations.ApplicationHome;
import org.opentcs.customizations.plantoverview.PlantOverviewInjectionModule;
import org.opentcs.drivers.LowLevelCommunicationEvent;
import org.opentcs.guing.common.exchange.ApplicationPortalProviderConfiguration;
import org.opentcs.guing.common.exchange.SslConfiguration;
import org.opentcs.modeleditor.application.ApplicationInjectionModule;
import org.opentcs.modeleditor.components.ComponentsInjectionModule;
import org.opentcs.modeleditor.exchange.ExchangeInjectionModule;
import org.opentcs.modeleditor.math.path.PathLengthFunctionInjectionModule;
import org.opentcs.modeleditor.model.ModelInjectionModule;
import org.opentcs.modeleditor.persistence.DefaultPersistenceInjectionModule;
import org.opentcs.modeleditor.transport.TransportInjectionModule;
import org.opentcs.modeleditor.util.ElementNamingSchemeConfiguration;
import org.opentcs.modeleditor.util.ModelEditorConfiguration;
import org.opentcs.modeleditor.util.UtilInjectionModule;
import org.opentcs.util.ClassMatcher;
import org.opentcs.util.gui.dialog.ConnectionParamSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Guice module for the openTCS plant overview application.
 */
public class DefaultPlantOverviewInjectionModule
    extends
      PlantOverviewInjectionModule {

  /**
   * This class's logger.
   */
  private static final Logger LOG
      = LoggerFactory.getLogger(DefaultPlantOverviewInjectionModule.class);

  /**
   * Creates a new instance.
   */
  public DefaultPlantOverviewInjectionModule() {
  }

  @Override
  protected void configure() {
    File applicationHome = new File(System.getProperty("opentcs.home", "."));
    bind(File.class)
        .annotatedWith(ApplicationHome.class)
        .toInstance(applicationHome);

    configurePlantOverviewDependencies();
    install(new ApplicationInjectionModule());
    install(new ComponentsInjectionModule());
    install(new ExchangeInjectionModule());
    install(new PathLengthFunctionInjectionModule());
    install(new ModelInjectionModule());
    install(new DefaultPersistenceInjectionModule());
    install(new TransportInjectionModule());
    install(new UtilInjectionModule());

    // Ensure there is at least an empty binder for pluggable panels.
    pluggablePanelFactoryBinder();
    // Ensure there is at least an empty binder for history entry formatters.
    objectHistoryEntryFormatterBinder();
  }

  private void configurePlantOverviewDependencies() {
    ModelEditorConfiguration configuration
        = getConfigBindingProvider().get(
            ModelEditorConfiguration.PREFIX,
            ModelEditorConfiguration.class
        );
    bind(ApplicationPortalProviderConfiguration.class)
        .toInstance(configuration);
    bind(ModelEditorConfiguration.class)
        .toInstance(configuration);
    configurePlantOverview(configuration);
    configureThemes(configuration);
    configureSocketConnections();
    configureNamingConfiguration();

    bind(new TypeLiteral<List<ConnectionParamSet>>() {
    })
        .toInstance(configuration.connectionBookmarks());
  }

  private void configureNamingConfiguration() {
    ElementNamingSchemeConfiguration configuration
        = getConfigBindingProvider().get(
            ElementNamingSchemeConfiguration.PREFIX,
            ElementNamingSchemeConfiguration.class
        );
    bind(ElementNamingSchemeConfiguration.class)
        .toInstance(configuration);
  }

  private void configureSocketConnections() {
    SslConfiguration sslConfiguration = getConfigBindingProvider().get(
        SslConfiguration.PREFIX,
        SslConfiguration.class
    );

    //Create the data object for the ssl configuration
    SslParameterSet sslParamSet = new SslParameterSet(
        SslParameterSet.DEFAULT_KEYSTORE_TYPE,
        null,
        null,
        new File(sslConfiguration.truststoreFile()),
        sslConfiguration.truststorePassword()
    );
    bind(SslParameterSet.class).toInstance(sslParamSet);

    SocketFactoryProvider socketFactoryProvider;
    if (sslConfiguration.enable()) {
      socketFactoryProvider = new SecureSocketFactoryProvider(sslParamSet);
    }
    else {
      LOG.warn("SSL encryption disabled, connections will not be secured!");
      socketFactoryProvider = new NullSocketFactoryProvider();
    }

    //Bind socket provider to the kernel portal
    bind(KernelServicePortal.class)
        .toInstance(
            new KernelServicePortalBuilder(
                GuestUserCredentials.USER,
                GuestUserCredentials.PASSWORD
            )
                .setSocketFactoryProvider(socketFactoryProvider)
                .setEventFilter(new ClassMatcher(LowLevelCommunicationEvent.class).negate())
                .build()
        );
  }

  private void configureThemes(ModelEditorConfiguration configuration) {
    bind(LocationTheme.class)
        .to(configuration.locationThemeClass())
        .in(Singleton.class);
  }

  private void configurePlantOverview(ModelEditorConfiguration configuration) {
    Locale.setDefault(Locale.forLanguageTag(configuration.locale()));

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException
           | UnsupportedLookAndFeelException ex) {
      LOG.warn("Could not set look-and-feel", ex);
    }
    // Show tooltips for 30 seconds (Default: 4 sec)
    ToolTipManager.sharedInstance().setDismissDelay(30 * 1000);
  }
}
