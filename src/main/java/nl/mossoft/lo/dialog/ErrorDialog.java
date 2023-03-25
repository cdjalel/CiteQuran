package nl.mossoft.lo.dialog;

import com.sun.star.uno.XComponentContext;
import java.util.Locale;

public class ErrorDialog extends AddonDialog {

  /**
   * Instantiates a new ErrorDialog.
   *
   * @param componentContext the component context
   * @param locale           the locale
   */
  protected ErrorDialog(final XComponentContext componentContext, final Locale locale) {
    super(componentContext, locale);
  }

  @Override
  protected void addDialogControls() {

  }

  @Override
  protected void setAddonDialogProperties(Object dialogModel) {

  }
}
