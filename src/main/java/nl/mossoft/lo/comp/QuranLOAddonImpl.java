package nl.mossoft.lo.comp;

import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.uno.XComponentContext;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mossoft.lo.dialog.AddonDialog;
import nl.mossoft.lo.dialog.InsertQuranTextDialog;
import nl.mossoft.lo.utils.AddonLogger;
import nl.mossoft.lo.utils.DialogType;

public class QuranLOAddonImpl
    extends WeakBase
    implements com.sun.star.lang.XServiceInfo, com.sun.star.task.XJobExecutor {

  private static final String ADDON_NAME = "nl.mossoft.lo.QuranLOAddon";
  private static final String IMPLEMENTATION_NAME = QuranLOAddonImpl.class.getName();
  private static final String[] SERVICE_NAMES = { QuranLOAddonImpl.ADDON_NAME };

  private static final String COMMAND_SHOW_IQT_DIALOG = "ShowIQTDialog";
  private static final String COMMAND_SHOW_ABOUT_DIALOG = "ShowAboutDialog";

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private final XComponentContext componentContext;
  private final Locale locale;

  public QuranLOAddonImpl(final XComponentContext componentContext) {
    this.componentContext = componentContext;
    this.locale = new Locale.Builder().setLanguage("en").setRegion("US").build();

    AddonLogger.setup();

  }

  /**
   * Get a Component Factory for the service to be implemented.
   *
   * @param implementationName name of the service to be implemented
   * @return Component Factory for the service
   */
  public static XSingleComponentFactory __getComponentFactory(final String implementationName) {
    XSingleComponentFactory singleComponentFactory = null;

    if (implementationName.equals(QuranLOAddonImpl.IMPLEMENTATION_NAME)) {
      singleComponentFactory = Factory.createComponentFactory(QuranLOAddonImpl.class,
          QuranLOAddonImpl.SERVICE_NAMES);
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
    return Factory.writeRegistryServiceInfo(QuranLOAddonImpl.IMPLEMENTATION_NAME, QuranLOAddonImpl.SERVICE_NAMES,
        registryKey);
  }

  @Override
  public String getImplementationName() {
    return QuranLOAddonImpl.IMPLEMENTATION_NAME;
  }

  /**
   * returns true if a service is supported.
   */
  @Override
  public boolean supportsService(final String service) {
    for (final String serviceName : QuranLOAddonImpl.SERVICE_NAMES) {
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
    return QuranLOAddonImpl.SERVICE_NAMES;
  }

  @Override
  public void trigger(final String command) {
    switch (command) {
      case COMMAND_SHOW_IQT_DIALOG -> {
        LOGGER.log(Level.FINER, "QuranLOAddonImpl.trigger.COMMAND_SHOW_IQT_DIALOG");
        InsertQuranTextDialog.loadAddonDialog(DialogType.INSERTQURANTEXTDIALOG,
            this.componentContext,
            this.locale).show();
      }
      case COMMAND_SHOW_ABOUT_DIALOG -> {
        LOGGER.log(Level.FINER, "QuranLOAddonImpl.trigger.COMMAND_SHOW_ABOUT_DIALOG");
        AddonDialog.loadAddonDialog(DialogType.ABOUTDIALOG, this.componentContext, this.locale).show();
      }
      default -> LOGGER.log(Level.SEVERE, "Unknown command {0}", new Object[]{ command });
    }
  }
}
