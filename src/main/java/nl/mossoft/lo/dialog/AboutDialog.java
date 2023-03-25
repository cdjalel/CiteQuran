package nl.mossoft.lo.dialog;

import com.sun.star.awt.FontWeight;
import com.sun.star.awt.XButton;
import com.sun.star.beans.XPropertySet;
import com.sun.star.uno.Exception;
import com.sun.star.uno.XComponentContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mossoft.lo.utils.AddonDialogTools;
import nl.mossoft.lo.utils.FileTools;

/**
 * The AboutDialog.
 */
public class AboutDialog extends AddonDialog {

  private static final String ABOUT_DLG = "AboutDlg";
  private static final String VERSION_LABEL = "VersionLabel";
  private static final String BUILD_LABEL = "BuildLabel";
  private static final String ABOUT_LABEL = "AboutLabel";
  private static final String COPYRIGHT_LABEL = "CopyrightLabel";
  private static final String CLOSE_BUTTON = "CloseButton";
  private static final String LOGO_IMG = "LogoImage";

  private static final String ON_CLOSE_BUTTON_PRESSED = "onCloseButtonPressed";

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private static final String JAR_FILE = "QuranLOAddon.jar";
  private static final String LOGO_FILE = "images/q64.png";

  /**
   * Instantiates a new AboutDialog.
   *
   * @param componentContext the component context
   * @param locale           the locale
   */
  protected AboutDialog(final XComponentContext componentContext, final Locale locale) {
    super(componentContext, locale);
  }

  @Override
  protected void addDialogControls() {
    LOGGER.log(Level.FINER, "AboutDialog.addDialogControls()");
    
    insertLabel(VERSION_LABEL, 50, 15, 150, 15, getVersion(), ALIGNMENT_LEFT, 13,
        FontWeight.BOLD, true);
    insertLabel(BUILD_LABEL, 50, 30, 150, 10, getBuild(), ALIGNMENT_LEFT, false, true);
    insertLabel(
        ABOUT_LABEL, 50, 45, 150, 25, rb.getString(ABOUT_LABEL), ALIGNMENT_LEFT, true, true);
    insertLabel(COPYRIGHT_LABEL, 50, 85, 150, 10, getCopyright(), ALIGNMENT_LEFT, false, true);
    insertButton(CLOSE_BUTTON, 190, 100, 40, 15, rb.getString(CLOSE_BUTTON), true);
    insertImageControl(LOGO_IMG, 5, 10, 32, 32, getLogo(), true);

    LOGGER.log(Level.FINER, "AboutDialog.addDialogControls completed");
  }

  private String getVersion() {
    return rb.getString(VERSION_LABEL) + " " + AboutDialog.class.getPackage()
        .getImplementationVersion();
  }

  private String getBuild() {
    try {
      final File jf = FileTools.getFilePath(JAR_FILE, componentContext);
      assert jf != null;
      final BasicFileAttributes attr = Files.readAttributes(jf.toPath(),
          BasicFileAttributes.class);

      return "Build #" + getVersion().substring(8, 15) + "-" + formatDateTime(
          attr.lastModifiedTime());
    } catch (final IOException ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
    return "...";
  }

  private String getCopyright() {
    return "Copyright © 2020-" + getVersion().substring(8, 12) + " <mossie@mossoft.nl>";
  }

  private String getLogo() {
    return FileTools.convertFileNameToUrl(
        componentContext, FileTools.getFilePath(LOGO_FILE, componentContext));
  }

  /**
   * Format date time string.
   *
   * @param fileTime the file time
   * @return the string
   */
  private static String formatDateTime(final FileTime fileTime) {
    final LocalDateTime localDateTime = fileTime.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
    return localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm"));
  }

  @Override
  protected void setAddonDialogProperties(final Object dialogModel) {
    try {
      final XPropertySet ps = AddonDialogTools.getPropertSet(dialogModel);
      ps.setPropertyValue(PROP_POSITION_X, 100);
      ps.setPropertyValue(PROP_POSITION_Y, 100);
      ps.setPropertyValue(PROP_WIDTH, 230);
      ps.setPropertyValue(PROP_HEIGHT, 115);
      ps.setPropertyValue(PROP_TITLE, rb.getString(ABOUT_DLG));

    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  @Override
  protected void initializeDialog() {
    LOGGER.log(Level.FINER, "AboutDialog.initializeDialog()");

    initializeCloseButton();
  }

  private void initializeCloseButton() {
    registerDialogEvent(
        CLOSE_BUTTON, controlContainer, XButton.class, ON_CLOSE_BUTTON_PRESSED, this);
  }

  @SuppressWarnings("unused")
  protected void handleCloseButtonPressed() {
    LOGGER.log(Level.FINER, "AboutDialog.handleCloseButtonPressed()");
    dialog.endExecute();
  }
}
