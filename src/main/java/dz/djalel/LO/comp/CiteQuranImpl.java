package dz.djalel.LO.comp;

import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.uno.XComponentContext;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import dz.djalel.LO.dialog.AddonDialog;
import dz.djalel.LO.dialog.InsertQuranTextDialog;
import dz.djalel.LO.utils.AddonLogger;
import dz.djalel.LO.utils.DialogType;

public class CiteQuranImpl extends WeakBase
    implements com.sun.star.lang.XServiceInfo, com.sun.star.task.XJobExecutor {

  private static final String ADDON_NAME = "dz.djalel.LO.CiteQuran";
  private static final String IMPLEMENTATION_NAME = CiteQuranImpl.class.getName();
  private static final String[] SERVICE_NAMES = {CiteQuranImpl.ADDON_NAME};

  private static final String COMMAND_SHOW_IQT_DIALOG = "ShowIQTDialog";
  private static final String COMMAND_SHOW_ABOUT_DIALOG = "ShowAboutDialog";

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private final XComponentContext componentContext;
  private final Locale locale;

  public CiteQuranImpl(final XComponentContext componentContext) {
    this.componentContext = componentContext;
    locale = new Locale.Builder().setLanguage("en").setRegion("US").build();

    AddonLogger.setup();

  }

  /**
   * Get a Component Factory for the service to be implemented.
   *
   * @param implementationName name of the service to be implemented
   * @return Component Factory for the service
   */
  public static XSingleComponentFactory __getComponentFactory(
      final String implementationName) {
    XSingleComponentFactory singleComponentFactory = null;

    if (implementationName.equals(CiteQuranImpl.IMPLEMENTATION_NAME)) {
      singleComponentFactory = Factory.createComponentFactory(CiteQuranImpl.class,
          CiteQuranImpl.SERVICE_NAMES);
    }
    return singleComponentFactory;
  }

  /**
   * Registers the service to be implemented.
   *
   * @param registryKey the registration key
   * @return true if successful
   */
  public static boolean __writeRegistryServiceInfo(final XRegistryKey registryKey) {
    return Factory.writeRegistryServiceInfo(CiteQuranImpl.IMPLEMENTATION_NAME,
        CiteQuranImpl.SERVICE_NAMES, registryKey);
  }

  @Override
  public String getImplementationName() {
    return CiteQuranImpl.IMPLEMENTATION_NAME;
  }

  /**
   * returns true if a service is supported.
   */
  @Override
  public boolean supportsService(final String service) {
    for (final String serviceName : CiteQuranImpl.SERVICE_NAMES) {
      if (service.equals(serviceName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the supported services.
   */
  @Override
  public String[] getSupportedServiceNames() {
    return CiteQuranImpl.SERVICE_NAMES;
  }

  @Override
  public void trigger(final String command) {
    switch (command) {
      case COMMAND_SHOW_IQT_DIALOG -> {
        LOGGER.log(Level.FINER, "CiteQuranImpl.trigger.COMMAND_SHOW_IQT_DIALOG");
        AddonDialog.loadAddonDialog(DialogType.INSERTQURANTEXTDIALOG, componentContext, locale)
            .show();
      }
      case COMMAND_SHOW_ABOUT_DIALOG -> {
        LOGGER.log(Level.FINER, "CiteQuranImpl.trigger.COMMAND_SHOW_ABOUT_DIALOG");
        AddonDialog.loadAddonDialog(DialogType.ABOUTDIALOG, componentContext, locale).show();
      }
      default -> LOGGER.log(Level.SEVERE, "Unknown command {0}", new Object[]{command});
    }
  }
}
