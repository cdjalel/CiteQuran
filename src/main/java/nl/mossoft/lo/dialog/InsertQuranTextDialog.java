package nl.mossoft.lo.dialog;

import static nl.mossoft.lo.utils.AddonDialogTools.boolean2Short;
import static nl.mossoft.lo.utils.AddonDialogTools.enableControl;
import static nl.mossoft.lo.utils.AddonDialogTools.getControl;
import static nl.mossoft.lo.utils.AddonDialogTools.short2Boolean;

import com.sun.star.awt.XCheckBox;
import com.sun.star.awt.XListBox;
import com.sun.star.awt.XNumericField;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.frame.XController;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XParagraphCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mossoft.lo.utils.AddonDialogTools;
import nl.mossoft.lo.utils.DocumentHandler;
import nl.mossoft.lo.utils.QuranReader;

/**
 * The InsertQuranTextDialog.
 */
public class InsertQuranTextDialog extends AddonDialog {

  private static final String INSERT_QURAN_TEXT_DLG = "InsertQuranTextDlg";
  private static final String SURAH_GRPBX = "SurahGrpbx";
  private static final String SURAH_LBL = "SurahLbl";
  private static final String SURAH_LSTBX = "SurahLstbx";
  private static final String AYAT_GRPBX = "AyatGrpbx";
  private static final String AYAT_LBL = "AyatLbl";
  private static final String AYAT_ALL_CHKBX = "AyatAllChkbx";
  private static final String AYAT_FROM_LBL = "AyatFromLbl";
  private static final String AYAT_FROM_NUM_FLD = "AyatFromNumFld";
  private static final String AYAT_TO_LBL = "AyatToLbl";
  private static final String AYAT_TO_NUM_FLD = "AyatToNumFld";
  private static final String LANGUAGE_GRPBX = "LanguageGrpbx";
  private static final String ARABIC_LANG_LBL = "ArabicLangLbl";
  private static final String ARABIC_LANG_CHKBX = "ArabicLangChkbx";
  private static final String ARABIC_LANG_VERS_LSTBX = "ArabicLangVersLstbx";

  private static final String ON_ARABIC_LANG_CHKBX_SELECTED = "onArabicLangChkbkSelected";
  private static final String ON_ARABIC_LSTBX_ITEM_SELECTED = "onArabicLstbxItemSelected";
  private static final String ON_ALL_AYAT_CHKBX_SELECTED = "onAllAyatChkbkSelected";
  private static final String ON_AYAT_FROM_NUM_FLD_CHANGED = "onAyatFromNumFldChanged";
  private static final String ON_AYAT_TO_NUM_FLD_CHANGED = "onAyatToNumFldChanged";
  private static final String ON_SURAH_LSTBX_ITEM_SELECTED = "onSurahLstbxItemSelected";

  private static final Map<String, String> SUPPORTED_DIALOG_EVENTS = Map.of(
      ON_ARABIC_LANG_CHKBX_SELECTED, ARABIC_LANG_CHKBX + "." + EVENT_ITEM_STATE_CHANGED,
      ON_ARABIC_LSTBX_ITEM_SELECTED, ARABIC_LANG_VERS_LSTBX + "." + EVENT_ITEM_STATE_CHANGED,
      ON_ALL_AYAT_CHKBX_SELECTED, AYAT_ALL_CHKBX + "." + EVENT_ITEM_STATE_CHANGED,
      ON_AYAT_FROM_NUM_FLD_CHANGED, AYAT_FROM_NUM_FLD + "." + EVENT_TEXT_CHANGED,
      ON_AYAT_TO_NUM_FLD_CHANGED, AYAT_TO_NUM_FLD + "." + EVENT_TEXT_CHANGED,
      ON_SURAH_LSTBX_ITEM_SELECTED, SURAH_LSTBX + "." + EVENT_ITEM_STATE_CHANGED);

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private final boolean selectedTransliterationInd = false;
  private final String selectedTransliterationLanguage = "";
  private final String selectedTransliterationLanguageVersion = "";
  private final boolean selectedLineNumberInd = true;
  private final boolean selectedLineByLineInd = true;
  private final String selectedLatinFontName = "";
  private final String selectedArabicFontName = "";
  private final String selectedTranslationLanguage = "";
  private final String selectedTranslationLanguageVersion = "";
  private final String selectedArabicLanguage = "";
  private final String selectedArabicLanguageVersion = "";
  private final boolean selectedTranslationInd = false;
  private boolean selectedArabicInd = false;
  private long selectedAyatFrom = 1;
  private long selectedAyatTo = 7;
  private boolean selectedAyatAllInd = true;
  private double selectedArabicFontSize;
  private double selectedLatinFontSize;
  private int selectedSurahNo = 1;

  private String defaultArabicFontName;
  private double defaultArabicFontSize;
  private String defaultLatinFontName;
  private double defaultLatinFontSize;

  /**
   * Instantiates a new Addon dialog.
   *
   * @param componentContext the component context
   * @param locale           the locale
   */
  protected InsertQuranTextDialog(
      final XComponentContext componentContext, final Locale locale) {
    super(componentContext, locale);
  }

  @Override
  protected void addDialogControls() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.addDialogControls");
    insertGrpbox(SURAH_GRPBX, 4, 5, 142, 27, resourceBundle.getString(SURAH_GRPBX), true);
    insertLbl(SURAH_LBL, 4, 15, 50, 10, resourceBundle.getString(SURAH_LBL), ALIGNMENT_RIGHT,
        false, true);
    insertLstbx(SURAH_LSTBX, 71, 15, 73, 10, this, true);

    insertGrpbox(AYAT_GRPBX, 4, 32, 142, 59, resourceBundle.getString(AYAT_GRPBX), true);
    insertLbl(AYAT_LBL, 4, 42, 50, 10, resourceBundle.getString(AYAT_LBL), ALIGNMENT_RIGHT,
        false, true);
    insertChkbx(AYAT_ALL_CHKBX, 59, 42, 10, 10, this, true);
    insertLbl(AYAT_FROM_LBL, 4, 58, 50, 10, resourceBundle.getString(AYAT_FROM_LBL),
        ALIGNMENT_RIGHT, false, false);
    insertNumFld(AYAT_FROM_NUM_FLD, 71, 58, 50, 10, this, false);
    insertLbl(AYAT_TO_LBL, 4, 74, 50, 10, resourceBundle.getString(AYAT_TO_LBL),
        ALIGNMENT_RIGHT, false, false);
    insertNumFld(AYAT_TO_NUM_FLD, 71, 74, 50, 10, this, false);

    insertGrpbox(LANGUAGE_GRPBX, 4, 91, 142, 59, resourceBundle.getString(LANGUAGE_GRPBX),
        true);
    insertLbl(ARABIC_LANG_LBL, 4, 101, 50, 10, resourceBundle.getString(ARABIC_LANG_LBL),
        ALIGNMENT_RIGHT, false, true);
    insertLstbx(ARABIC_LANG_VERS_LSTBX, 71, 101, 73, 10, this, true);
    insertChkbx(ARABIC_LANG_CHKBX, 59, 101, 10, 10, this, true);


  }

  @Override
  protected void setAddonDialogProperties(final Object dialogModel) {
    try {
      final XPropertySet ps = AddonDialogTools.getPropertSet(dialogModel);
      ps.setPropertyValue(PROP_POSITION_X, 100);
      ps.setPropertyValue(PROP_POSITION_Y, 100);
      ps.setPropertyValue(PROP_WIDTH, 300);
      ps.setPropertyValue(PROP_HEIGHT, 170);
      ps.setPropertyValue(PROP_TITLE, resourceBundle.getString(INSERT_QURAN_TEXT_DLG));
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  @Override
  protected void initializeDialog() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeDialog()");
    getLoDocumentDefaults();

    initializeSurahLstbx();
    initializeAyatAllChkBx();
    initializeAyatFrom();
    initializeAyatTo();
    initializeArabicLangChkbx();


    initializeSupportedEvents(InsertQuranTextDialog.class, SUPPORTED_DIALOG_EVENTS);
  }

  /**
   * Get default settings from the document.
   */
  private void getLoDocumentDefaults() {
    final XTextDocument textDoc = DocumentHandler.getCurrentDocument(componentContext);
    final XController controller = textDoc.getCurrentController();
    final XTextViewCursorSupplier textViewCursorSupplier = DocumentHandler.getCursorSupplier(
        controller);
    final XTextViewCursor textViewCursor = textViewCursorSupplier.getViewCursor();
    final XText text = textViewCursor.getText();
    final XTextCursor textCursor = text.createTextCursorByRange(textViewCursor.getStart());
    final XParagraphCursor paragraphCursor = UnoRuntime.queryInterface(XParagraphCursor.class,
        textCursor);
    final XPropertySet paragraphCursorPropertySet = DocumentHandler.getPropertySet(
        paragraphCursor);

    try {
      defaultArabicFontSize = (float) paragraphCursorPropertySet.getPropertyValue(
          PROP_CHAR_HEIGHT_COMPLEX);
    } catch (UnknownPropertyException | WrappedTargetException e) {
      defaultArabicFontSize = 10;
    }
    try {
      defaultArabicFontName = (String) paragraphCursorPropertySet.getPropertyValue(
          PROP_CHAR_FONT_NAME_COMPLEX);
    } catch (UnknownPropertyException | WrappedTargetException e) {
      defaultArabicFontName = "No Default set";
    }
    try {
      defaultLatinFontName = (String) paragraphCursorPropertySet.getPropertyValue(
          PROP_CHAR_FONT_NAME);
    } catch (UnknownPropertyException | WrappedTargetException e) {
      defaultLatinFontName = "No Default set";
    }
    try {
      defaultLatinFontSize = (float) paragraphCursorPropertySet.getPropertyValue(
          PROP_CHAR_HEIGHT);
    } catch (UnknownPropertyException | WrappedTargetException e) {
      defaultLatinFontSize = 10;
    }
  }

  /**
   * Initializes the listbox with all the surah names of the Qur'an.
   */
  private void initializeSurahLstbx() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeSurahLstbx");
    final XListBox listBox = getControl(controlContainer, XListBox.class, SURAH_LSTBX);

    for (int i = 0; i < 114; i++) {
      listBox.addItem(QuranReader.getSurahName(i + 1) + " (" + (i + 1) + ")", (short) i);
    }
    listBox.selectItemPos((short) 0, true);
    selectedSurahNo = listBox.getSelectedItemPos() + 1;
  }

  /**
   * Iniializes the all ayat checkbox.
   */
  private void initializeAyatAllChkBx() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeAyatAllChkBx");
    final XCheckBox checkBox = getControl(controlContainer, XCheckBox.class, AYAT_ALL_CHKBX);

    checkBox.setState(boolean2Short(true));
    selectedAyatAllInd = short2Boolean(checkBox.getState());
  }

  /**
   * Initialize the Ayat From NumericField.
   */
  private void initializeAyatFrom() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeAyatFrom");
    final XNumericField numericFieldFrom = getControl(
        controlContainer, XNumericField.class, AYAT_FROM_NUM_FLD);

    numericFieldFrom.setValue(1);
    selectedAyatFrom = Math.round(numericFieldFrom.getValue());
  }

  /**
   * Initialize the Ayat To NumericField.
   */
  private void initializeAyatTo() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeAyatTo");
    final XNumericField numericFieldTo = getControl(
        controlContainer, XNumericField.class, AYAT_TO_NUM_FLD);

    numericFieldTo.setValue(QuranReader.getSurahSize(selectedSurahNo));
    selectedAyatTo = Math.round(numericFieldTo.getValue());
  }

  /**
   * Iniializes the Arabic Language checkbox.
   */
  private void initializeArabicLangChkbx() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeArabicLangChkbx");
    final XCheckBox checkBox = getControl(
        controlContainer, XCheckBox.class, ARABIC_LANG_CHKBX);

    checkBox.setState(boolean2Short(true));
    selectedArabicInd = short2Boolean(checkBox.getState());

    // TODO
    //    new Thread(() -> {
    //      initializeArabicLanguageVersionListBox();
    //      initializeArabicFontListBox();
    //      initializeArabicFontSize();
    //    }).start();
  }

  @SuppressWarnings("unused")
  protected void handleAyatToNumFldChanged() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleAyatToNumFldChanged");
    final XNumericField toField = getControl(
        controlContainer, XNumericField.class, AYAT_TO_NUM_FLD);
    if (Math.round(toField.getValue()) <= selectedAyatFrom) {
      toField.setValue(selectedAyatFrom);
    } else if (Math.round(toField.getValue()) >= QuranReader.getSurahSize(selectedSurahNo)) {
      toField.setValue(QuranReader.getSurahSize(selectedSurahNo));
    }
    selectedAyatTo = Math.round(toField.getValue());
  }

  @SuppressWarnings("unused")
  protected void handleAyatFromNumFldChanged() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleAyatFromNumFldChanged");
    final XNumericField fromField = getControl(
        controlContainer, XNumericField.class, AYAT_FROM_NUM_FLD);
    if (Math.round(fromField.getValue()) >= selectedAyatTo) {
      fromField.setValue(selectedAyatTo);
    } else if (Math.round(fromField.getValue()) <= 1) {
      fromField.setValue(1);
    }
    selectedAyatFrom = Math.round(fromField.getValue());
  }

  @SuppressWarnings("unused")
  protected void handleSurahLstbxItemSelected() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleSurahLstbxItemSelected");
    LOGGER.log(Level.FINEST,
        "InsertQuranTextDialog.handleSurahLstbxItemSelected Surah " + "Before: {0}",
        new Object[]{
            selectedSurahNo
        });

    final XListBox listBox = getControl(controlContainer, XListBox.class, SURAH_LSTBX);
    selectedSurahNo = listBox.getSelectedItemPos() + 1;

    final XNumericField numericFieldFrom = getControl(controlContainer, XNumericField.class,
        AYAT_FROM_NUM_FLD);
    numericFieldFrom.setValue(1);
    selectedAyatFrom = Math.round(numericFieldFrom.getValue());

    final XNumericField numericFieldTo = getControl(controlContainer, XNumericField.class,
        AYAT_TO_NUM_FLD);
    numericFieldTo.setValue(QuranReader.getSurahSize(selectedSurahNo));
    selectedAyatTo = Math.round(numericFieldTo.getValue());

    LOGGER.log(Level.FINEST,
        "InsertQuranTextDialog.handleSurahLstbxItemSelected Surah " + "After:  {0}",
        new Object[]{
            selectedSurahNo
        });
  }

  @SuppressWarnings("unused")
  protected void handleAllAyatChkbkSelected() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleAllAyatChkbkSelected");
    LOGGER.log(Level.FINEST,
        "InsertQuranTextDialog.handleAllAyatChkbkSelected State " + "Before: {0}",
        new Object[]{
            selectedAyatAllInd
        });
    final XCheckBox checkBox = getControl(controlContainer, XCheckBox.class, AYAT_ALL_CHKBX);

    checkBox.setState(boolean2Short(!selectedAyatAllInd));
    selectedAyatAllInd = short2Boolean(checkBox.getState());

    enableControl(controlContainer, AYAT_FROM_LBL, !selectedAyatAllInd);
    enableControl(controlContainer, AYAT_FROM_NUM_FLD, !selectedAyatAllInd);
    enableControl(controlContainer, AYAT_TO_LBL, !selectedAyatAllInd);
    enableControl(controlContainer, AYAT_TO_NUM_FLD, !selectedAyatAllInd);

    if (selectedAyatAllInd) {
      initializeAyatFrom();
      initializeAyatTo();
    }
    LOGGER.log(Level.FINEST,
        "InsertQuranTextDialog.handleAllAyatChkbkSelected State " + "After:  {0}",
        new Object[]{
            selectedAyatAllInd
        });
  }

  @SuppressWarnings("unused")
  protected void handleArabicLstbxItemSelected() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleArabicLstbxItemSelected");

  }

  @SuppressWarnings("unused")
  protected void handleArabicLangChkbkSelected() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleArabicLangChkbkSelected");
    final XCheckBox checkBox = getControl(
        controlContainer, XCheckBox.class, ARABIC_LANG_CHKBX);
    checkBox.setState(boolean2Short(!selectedArabicInd));
    selectedArabicInd = short2Boolean(checkBox.getState());

    enableControl(controlContainer, ARABIC_LANG_LBL, selectedArabicInd);
    enableControl(controlContainer, ARABIC_LANG_VERS_LSTBX, selectedArabicInd);

    // TODO
    //       enableControl(controlContainer, D021_ARABIC_FONT_GROUPBOX, selectedArabicInd);
    //    enableControl(controlContainer, D022_ARABIC_FONT_LABEL, selectedArabicInd);
    //    enableControl(controlContainer, D023_ARABIC_FONT_LISTBOX, selectedArabicInd);
    //    enableControl(controlContainer, D024_ARABIC_FONTSIZE_LABEL, selectedArabicInd);
    //    enableControl(controlContainer, D025_ARABIC_FONTSIZE_NUMFLD, selectedArabicInd);
    //    enableControl(controlContainer, D034_OK_BUTTON,
    //        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
    //    enableControl(controlContainer, D031_MISCELLANEOUS_GROUPBOX,
    //        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
    //    enableControl(controlContainer, D032_LINE_BY_LINE_LABEL,
    //        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
    //    enableControl(controlContainer, D033_LINE_BY_LINE_CHECKBOX,
    //        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
  }


}
