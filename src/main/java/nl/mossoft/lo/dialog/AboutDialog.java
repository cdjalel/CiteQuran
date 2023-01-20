package nl.mossoft.lo.dialog;

import com.sun.star.awt.FontWeight;
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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mossoft.lo.utils.AddonDialogTools;
import nl.mossoft.lo.utils.FileTools;

/**
 * The AboutDialog.
 */
public class AboutDialog extends AddonDialog {

  private static final String ABOUT_DLG = "AboutDlg";
  private static final String VERSION_LBL = "VersionLbl";
  private static final String BUILD_LBL = "BuildLbl";
  private static final String ABOUT_LBL = "AboutLbl";
  private static final String COPYRIGHT_LBL = "CopyrightLbl";
  private static final String CLOSE_BTTN = "CloseBttn";
  private static final String LOGO_IMG = "LogoImg";

  private static final String ON_CLOSE_BTTN_PRESSED = "onCloseBttnPressed";

  private static final Map<String, String> SUPPORTED_DIALOG_EVENTS = Map.of(
      ON_CLOSE_BTTN_PRESSED, CLOSE_BTTN + "." + EVENT_ACTION_PERFORMED);

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
    insertLbl(VERSION_LBL, 50, 15, 150, 15, getVersion(), ALIGNMENT_LEFT, 13, FontWeight.BOLD,
        true);
    insertLbl(BUILD_LBL, 50, 30, 150, 10, getBuild(), ALIGNMENT_LEFT, false, true);
    insertLbl(ABOUT_LBL, 50, 45, 150, 25, resourceBundle.getString(ABOUT_LBL), ALIGNMENT_LEFT,
        true, true);
    insertLbl(COPYRIGHT_LBL, 50, 85, 150, 10, getCopyright(), ALIGNMENT_LEFT, false, true);
    insertBttn(CLOSE_BTTN, 190, 100, 40, 15, resourceBundle.getString(CLOSE_BTTN), this,
        true);
    insertImgCntl(LOGO_IMG, 5, 10, 32, 32, getLogo(), true);
   }

  @Override
  protected void setAddonDialogProperties(final Object dialogModel) {
    try {
      final XPropertySet ps = AddonDialogTools.getPropertSet(dialogModel);
      ps.setPropertyValue(PROP_POSITION_X, 100);
      ps.setPropertyValue(PROP_POSITION_Y, 100);
      ps.setPropertyValue(PROP_WIDTH, 230);
      ps.setPropertyValue(PROP_HEIGHT, 115);
      ps.setPropertyValue(PROP_TITLE, resourceBundle.getString(ABOUT_DLG));

    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  private String getVersion() {
    return resourceBundle.getString(VERSION_LBL) + " " + AboutDialog.class.getPackage()
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

  @SuppressWarnings("unused")
  protected void handleCloseBttnPressed() {
    LOGGER.log(Level.FINER, "AboutDialog.handleCloseBttnPressed()");
    dialog.endExecute();
  }

  @Override
  protected void initializeDialog() {
    LOGGER.log(Level.FINER, "AboutDialog.initializeDialog()");

    initializeSupportedEvents(AboutDialog.class, SUPPORTED_DIALOG_EVENTS);
  }
}
