package dz.djalel.LO.dialog;

import static dz.djalel.LO.utils.AddonDialogTools.boolean2Short;
import static dz.djalel.LO.utils.AddonDialogTools.enableControl;
import static dz.djalel.LO.utils.AddonDialogTools.getControl;
import static dz.djalel.LO.utils.AddonDialogTools.short2Boolean;

import com.sun.star.awt.XButton;
import com.sun.star.awt.XCheckBox;
import com.sun.star.awt.XListBox;
import com.sun.star.awt.XNumericField;
import com.sun.star.awt.XProgressBar;
import com.sun.star.awt.XTextComponent;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.frame.XController;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.text.XParagraphCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import dz.djalel.LO.utils.AddonDialogTools;
import dz.djalel.LO.utils.DocumentHandler;
import dz.djalel.LO.utils.QuranReader;

/**
 * The InsertQuranTextDialog.
 */
public class InsertQuranTextDialog extends AddonDialog {

  private static final String ARABIC = "Arabic";
  private static final String TRANSLITERATION = "Transliteration";

  private static final String INSERT_QURAN_TEXT_DLG = "InsertQuranTextDlg";

  private static final String INSERT_AYA_BUTTON = "InsertAyaButton";
  private static final String HORIZONTAL_SEPARATOR = "HorizontalSeparator";

  private static final String SURAH_GROUP_BOX = "SurahGroupBox";
  private static final String SURAH_LABEL = "SurahLabel";
  private static final String SURAH_LIST_BOX = "SurahListBox";
  private static final String AYAT_GROUP_BOX = "AyatGroupBox";
  private static final String AYAT_LABEL = "AyatLabel";
  private static final String AYAT_ALL_CHECK_BOX = "AyatAllCheckBox";
  private static final String AYAT_FROM_LABEL = "AyatFromLabel";
  private static final String AYAT_FROM_NUMERIC_FIELD = "AyatFromNumericField";
  private static final String AYAT_TO_LABEL = "AyatToLabel";
  private static final String AYAT_TO_NUMERIC_FIELD = "AyatToNumericField";
  private static final String LANGUAGE_GROUP_BOX = "LanguageGroupBox";
  private static final String ARABIC_FONT_GROUP_BOX = "ArabicFontGroupBox";
  private static final String ARABIC_FONT_LABEL = "ArabicFontLabel";
  private static final String ARABIC_FONT_LIST_BOX = "ArabicFontListBox";
  private static final String ARABIC_FONT_SIZE_NUMERIC_FIELD = "ArabicFontSizeNumericField";
  private static final String ARABIC_LANGUAGE_LABEL = "ArabicLanguageLabel";
  private static final String ARABIC_LANGUAGE_CHECK_BOX = "ArabicLanguageCheckBox";
  private static final String ARABIC_LANGUAGE_VERSION_LIST_BOX =
      "ArabicLanguageVersionListBox";
  private static final String LATIN_FONT_GROUP_BOX = "LatinFontGroupBox";
  private static final String LATIN_FONT_LABEL = "LatinFontLabel";
  private static final String LATIN_FONT_LIST_BOX = "LatinFontListBox";
  private static final String LATIN_FONT_SIZE_NUMERIC_FIELD = "LatinFontSizeNumericField";
  private static final String TRANSLATION_LANGUAGE_VERSION_LABEL =
      "TranslationLanguageVersionLabel";
  private static final String TRANSLATION_LANGUAGE_VERSION_CHECK_BOX =
      "TranslationLanguageVersionCheckBox";
  private static final String TRANSLATION_LANGUAGE_VERSION_LIST_BOX =
      "TranslationLanguageVersionListBox";
  private static final String TRANSLITERATION_LANGUAGE_VERSION_LABEL =
      "TransliterationLanguageVersionLabel";
  private static final String TRANSLITERATION_LANGUAGE_VERSION_CHECK_BOX =
      "TransliterationLanguageVersionCheckBox";
  private static final String TRANSLITERATION_LANGUAGE_VERSION_LIST_BOX =
      "TransliterationLanguageVersionListBox";
  private static final String MISCELLANEOUS_GROUP_BOX = "MiscGroupBox";
  private static final String LINE_BY_LINE_LABEL = "LineByLineLabel";
  private static final String LINE_BY_LINE_CHECK_BOX = "LineByLineCheckBox";
  private static final String INSERT_SURAH_BUTTON = "InsertSurahButton";
  private static final String INSERT_PROGRESS_BAR = "InsertProgressBar";

  // Dialog Events
  private static final String ON_ARABIC_FONT_SELECTED = "onArabicFontSelected";
  private static final String ON_ARABIC_LANGUAGE_CHECK_BOX_SELECTED =
      "onArabicLanguageCheckBoxSelected";
  private static final String ON_ARABIC_LANGUAGE_VERSION_SELECTED =
      "onArabicLanguageVersionSelected";
  private static final String ON_ARABIC_FONT_SIZE_NUMERIC_FIELD_CHANGED =
      "onArabicFontSizeNumericFieldChanged";
  private static final String ON_ALL_AYAT_CHECK_BOX_SELECTED = "onAllAyatCheckBoxSelected";
  private static final String ON_AYAT_FROM_NUMERIC_FIELD_CHANGED =
      "onAyatFromNumericFieldChanged";
  private static final String ON_AYAT_TO_NUMERIC_FIELD_CHANGED = "onAyatToNumericFieldChanged";
  private static final String ON_LATIN_FONT_SELECTED = "onLatinFontSelected";
  private static final String ON_LATIN_FONT_SIZE_NUMERIC_FIELD_CHANGED =
      "onLatinFontSizeNumericFieldChanged";

  private static final String ON_SURAH_SELECTED = "onSurahSelected";
  private static final String ON_TRANSLATION_LANGUAGE_CHECK_BOX_SELECTED =
      "onTranslationLanguageCheckBoxSelected";
  private static final String ON_TRANSLATION_LANGUAGE_VERSION_SELECTED =
      "onTranslationLanguageVersionSelected";
  private static final String ON_TRANSLITERATION_LANGUAGE_CHECK_BOX_SELECTED =
      "onTransliterationLanguageCheckBoxSelected";
  private static final String ON_TRANSLITERATION_LANGUAGE_VERSION_SELECTED =
      "onTransliterationLanguageVersionSelected";

  private static final String ON_LINE_BY_LINE_CHECK_BOX_SELECTED = "onLineByLineCheckBoxSelected";
  private static final String ON_INSERT_SURAH_BUTTON_PRESSED = "onInsertSurahButtonPressed";

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private boolean selectedTransliterationInd = false;
  private String selectedTransliterationLanguage = "";
  private String selectedTransliterationLanguageVersion = "";
  private final boolean selectedLineNumberInd = true;
  private boolean selectedLineByLineInd = true;
  private String selectedLatinFontName = "";
  private String selectedArabicFontName = "";
  private boolean selectedTranslationInd = false;
  private String selectedTranslationLanguage = "";
  private String selectedTranslationLanguageVersion = "";
  private String selectedArabicLanguage = "";
  private String selectedArabicLanguageVersion = "";
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

  private static final String LEFT_PARENTHESIS = new String(Character.toChars(64830));
  private static final String RIGHT_PARENTHESIS = new String(Character.toChars(64831));

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
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.addDialogControls()");

    insertHorizontalFixedLine(HORIZONTAL_SEPARATOR, 2, 2, 296, 2);

    // Surah GroupBox
    insertGroupBox(SURAH_GROUP_BOX, 2, 4, 296, 27, rb.getString(SURAH_GROUP_BOX), true);
    insertLabel(SURAH_LABEL, 6, 15, 20, 10, rb.getString(SURAH_LABEL), ALIGNMENT_LEFT, false, true);
    insertListBox(SURAH_LIST_BOX, 26, 15, 65, 10, true);

    // Ayat GroupBox
    //insertGroupBox(AYAT_GROUP_BOX, 4, 32, 142, 59, rb.getString(AYAT_GROUP_BOX), true);
    insertLabel(AYAT_LABEL, 110, 15, 24, 10, rb.getString(AYAT_LABEL), ALIGNMENT_LEFT, false, true);
    insertCheckBox(AYAT_ALL_CHECK_BOX, 138, 15, 10, 10, true);
    insertLabel(AYAT_FROM_LABEL, 151, 15, 16, 10, rb.getString(AYAT_FROM_LABEL), ALIGNMENT_LEFT,
        false, false);
    insertNumericField(AYAT_FROM_NUMERIC_FIELD, 169, 15, 25, 10, false);
    insertLabel(AYAT_TO_LABEL, 195, 15, 10, 10, rb.getString(AYAT_TO_LABEL), ALIGNMENT_LEFT,
        false, false);
    insertNumericField(AYAT_TO_NUMERIC_FIELD, 205, 15, 25, 10, false);
    insertLabel(LINE_BY_LINE_LABEL, 230, 15, 50, 10, rb.getString(LINE_BY_LINE_LABEL),
        ALIGNMENT_RIGHT, false, true);
    insertCheckBox(LINE_BY_LINE_CHECK_BOX, 282, 15, 10, 10, true);


    // Language GroupBox
    insertGroupBox(LANGUAGE_GROUP_BOX, 2, 32, 296, 58, rb.getString(LANGUAGE_GROUP_BOX), true);
    insertLabel(ARABIC_LANGUAGE_LABEL, 4, 42, 46, 10, rb.getString(ARABIC_LANGUAGE_LABEL),
        ALIGNMENT_RIGHT, false, true);
    insertCheckBox(ARABIC_LANGUAGE_CHECK_BOX, 52, 42, 10, 10, true);
    insertListBox(ARABIC_LANGUAGE_VERSION_LIST_BOX, 64, 42, 73, 10, true);
    insertLabel(TRANSLATION_LANGUAGE_VERSION_LABEL, 4, 57, 46, 10,
        rb.getString(TRANSLATION_LANGUAGE_VERSION_LABEL), ALIGNMENT_RIGHT, false, false);
    insertCheckBox(TRANSLATION_LANGUAGE_VERSION_CHECK_BOX, 52, 57, 10, 10, true);
    insertListBox(TRANSLATION_LANGUAGE_VERSION_LIST_BOX, 64, 57, 73, 10, false);
    insertLabel(TRANSLITERATION_LANGUAGE_VERSION_LABEL, 4, 73, 46, 10,
        rb.getString(TRANSLITERATION_LANGUAGE_VERSION_LABEL), ALIGNMENT_RIGHT, false, false);
    insertCheckBox(TRANSLITERATION_LANGUAGE_VERSION_CHECK_BOX, 52, 73, 10, 10, false);
    insertListBox(TRANSLITERATION_LANGUAGE_VERSION_LIST_BOX, 64, 73, 73, 10, false);

    // Arabic Font GroupBox
    //insertGroupBox(ARABIC_FONT_GROUP_BOX, 150, 5, 142, 59, rb.getString(ARABIC_FONT_GROUP_BOX), true);
    insertLabel(ARABIC_FONT_LABEL, 150, 42, 34, 10, rb.getString(ARABIC_FONT_LABEL),
        ALIGNMENT_RIGHT, false, true);
    insertListBox(ARABIC_FONT_LIST_BOX, 188, 42, 82, 10, true);
    insertNumericField(ARABIC_FONT_SIZE_NUMERIC_FIELD, 272, 42, 22, 10, true);

    // Latin Font GroupBox
    // insertGroupBox(LATIN_FONT_GROUP_BOX, 150, 64, 142, 59, rb.getString(LATIN_FONT_GROUP_BOX), true);
    insertLabel(LATIN_FONT_LABEL, 150, 57, 34, 10, rb.getString(LATIN_FONT_LABEL),
        ALIGNMENT_RIGHT, false, true);
    insertListBox(LATIN_FONT_LIST_BOX, 188, 57, 82, 10, true);
    insertNumericField(LATIN_FONT_SIZE_NUMERIC_FIELD, 272, 57, 22, 10, true);

    // Misc GroupBox
    //insertGroupBox(MISCELLANEOUS_GROUP_BOX, 150, 123, 142, 28, rb.getString(MISCELLANEOUS_GROUP_BOX), true);
    // Insert Group
    insertButton(INSERT_SURAH_BUTTON, 4, 94, 40, 15, rb.getString(INSERT_SURAH_BUTTON), true);
    insertProgressBar(INSERT_PROGRESS_BAR, 46, 94, 252, 18);

    LOGGER.log(Level.FINER, "InsertQuranTextDialog.addDialogControls completed");
  }

  @Override
  protected void setAddonDialogProperties(final Object dialogModel) {
    try {
      final XPropertySet ps = AddonDialogTools.getPropertSet(dialogModel);
      ps.setPropertyValue(PROP_POSITION_X, 200);
      ps.setPropertyValue(PROP_POSITION_Y, 100);
      ps.setPropertyValue(PROP_WIDTH, 300);
      ps.setPropertyValue(PROP_HEIGHT, 170);
      ps.setPropertyValue(PROP_TITLE, rb.getString(INSERT_QURAN_TEXT_DLG));
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  @Override
  protected void initializeDialog() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeDialog()");
    getLoDocumentDefaults();

    initializeSurahListBox();
    initializeAyatAllChkBx();
    initializeAyatFrom();
    initializeAyatTo();
    initializeArabicLanguageCheckBox();
    initializeArabicLanguageVersionListBox();
    initializeTranslationLanguageCheckBox();
    initializeTranslationLanguageVersionListBox();
    initializeTransliterationLanguageCheckBox();
    initializeTransliterationLanguageVersionListBox();
    initializeArabicFontListBox();
    initializeArabicFontSize();
    initializeLatinFontListBox();
    initializeLatinFontSize();
    initializeLineByLineCheckBox();

    initializeInsertButton();
    initializeInsertSurahProgressBar();

    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeDialog() Completed");
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
    } catch (final UnknownPropertyException | WrappedTargetException e) {
      defaultArabicFontSize = 10;
    }

    try {
      defaultArabicFontName = "KFGQPC HAFS Uthmanic Script";
      boolean available = false;

      // Check if the font is available. TODO: pack & install it otherwise.
      final Locale locale = new Locale.Builder().setScript("ARAB").build();
      final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
          .getAvailableFontFamilyNames(locale);

      for (int i = 0; i < fonts.length; i++) {
        if (fonts[i].equals(defaultArabicFontName)) {
          available = true;
          break;
        }
      }

      if (!available) {
        defaultArabicFontName = (String) paragraphCursorPropertySet.getPropertyValue(
          PROP_CHAR_FONT_NAME_COMPLEX);
      }
    } catch (final UnknownPropertyException | WrappedTargetException e) {
      defaultArabicFontName = "No Default set";
    }

    try {
      defaultLatinFontName = (String) paragraphCursorPropertySet.getPropertyValue(
          PROP_CHAR_FONT_NAME);
    } catch (final UnknownPropertyException | WrappedTargetException ex) {
      defaultLatinFontName = "No Default set";
    }
    try {
      defaultLatinFontSize = (float) paragraphCursorPropertySet.getPropertyValue(
          PROP_CHAR_HEIGHT);
    } catch (final UnknownPropertyException | WrappedTargetException e) {
      defaultLatinFontSize = 10;
    }
  }

  /**
   * Initializes the listbox with all the surah names of the Qur'an.
   */
  private void initializeSurahListBox() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeSurahListBox");
    final XListBox listBox = getControl(controlContainer, XListBox.class, SURAH_LIST_BOX);

    for (int i = 0; i < 114; i++) {
      listBox.addItem(QuranReader.getSurahName(i + 1) + " (" + (i + 1) + ")", (short) i);
    }
    listBox.selectItemPos((short) 0, true);
    selectedSurahNo = listBox.getSelectedItemPos() + 1;

    registerDialogEvent(
        SURAH_LIST_BOX, controlContainer, XListBox.class, ON_SURAH_SELECTED, this);
  }

  /**
   * Iniializes the all ayat checkbox.
   */
  private void initializeAyatAllChkBx() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeAyatAllChkBx");
    final XCheckBox checkBox = getControl(
        controlContainer, XCheckBox.class, AYAT_ALL_CHECK_BOX);

    checkBox.setState(boolean2Short(true));
    selectedAyatAllInd = short2Boolean(checkBox.getState());

    registerDialogEvent(AYAT_ALL_CHECK_BOX, controlContainer, XCheckBox.class,
        ON_ALL_AYAT_CHECK_BOX_SELECTED, this);
  }

  /**
   * Initialize the Ayat From NumericField.
   */
  private void initializeAyatFrom() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeAyatFrom");
    final XNumericField numericFieldFrom = getControl(
        controlContainer, XNumericField.class, AYAT_FROM_NUMERIC_FIELD);

    numericFieldFrom.setValue(1);
    selectedAyatFrom = Math.round(numericFieldFrom.getValue());

    registerDialogEvent(AYAT_FROM_NUMERIC_FIELD, controlContainer, XTextComponent.class,
        ON_AYAT_FROM_NUMERIC_FIELD_CHANGED, this);
  }

  /**
   * Initialize the Ayat To NumericField.
   */
  private void initializeAyatTo() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeAyatTo");
    final XNumericField numericFieldTo = getControl(
        controlContainer, XNumericField.class, AYAT_TO_NUMERIC_FIELD);

    numericFieldTo.setValue(QuranReader.getSurahSize(selectedSurahNo));
    selectedAyatTo = Math.round(numericFieldTo.getValue());

    registerDialogEvent(AYAT_TO_NUMERIC_FIELD, controlContainer, XTextComponent.class,
        ON_AYAT_TO_NUMERIC_FIELD_CHANGED, this);
  }

  /**
   * Iniializes the Arabic Language checkbox.
   */
  private void initializeArabicLanguageCheckBox() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeArabicLanguageCheckBox");
    final XCheckBox checkBox = getControl(
        controlContainer, XCheckBox.class, ARABIC_LANGUAGE_CHECK_BOX);

    checkBox.setState(boolean2Short(true));
    selectedArabicInd = short2Boolean(checkBox.getState());
    registerDialogEvent(ARABIC_LANGUAGE_CHECK_BOX, controlContainer, XCheckBox.class,
        ON_ARABIC_LANGUAGE_CHECK_BOX_SELECTED, this);
  }

  /**
   * Iniializes the listbox with all the Arabic versions of the Qur'an.
   */
  private void initializeArabicLanguageVersionListBox() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeArabicLanguageVersionLstbox()");
    new Thread(() -> {
      final XListBox listBox = getControl(
          controlContainer, XListBox.class, ARABIC_LANGUAGE_VERSION_LIST_BOX);
      final List<String> fns = getQuranTxtFiles();
      int k = 0;
      for (final String fn : fns) {
        final String[] parts = fn.split("[.]");
        if (parts[1].equals(ARABIC)) {
          listBox.addItem(parts[1] + " (" + parts[2].replace("_", " ") + ")", (short) k);
          k++;
        }
      }
      listBox.selectItemPos((short) 0, true);

      selectedArabicLanguage = getItemLanguague(listBox.getSelectedItem());
      selectedArabicLanguageVersion = getItemVersion(listBox.getSelectedItem());

      registerDialogEvent(ARABIC_LANGUAGE_VERSION_LIST_BOX, controlContainer, XListBox.class,
          ON_ARABIC_LANGUAGE_VERSION_SELECTED, this);
    }).start();
  }

  private void initializeTranslationLanguageCheckBox() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeTranslationLanguageCheckBox()");
    final XCheckBox checkBox = getControl(controlContainer, XCheckBox.class,
        TRANSLATION_LANGUAGE_VERSION_CHECK_BOX);

    checkBox.setState(boolean2Short(false));
    selectedTranslationInd = short2Boolean(checkBox.getState());

    registerDialogEvent(TRANSLATION_LANGUAGE_VERSION_CHECK_BOX, controlContainer,
        XCheckBox.class, ON_TRANSLATION_LANGUAGE_CHECK_BOX_SELECTED, this);
  }

  /**
   * Iniializes the listbox with all the Translation versions of the Qur'an.
   */
  private void initializeTranslationLanguageVersionListBox() {
    LOGGER.log(
        Level.FINER, "InsertQuranTextDialog.initializeTranslationLanguageVersionListBox()");
    new Thread(() -> {
      final XListBox listBox = getControl(
          controlContainer, XListBox.class, TRANSLATION_LANGUAGE_VERSION_LIST_BOX);

      final List<String> fns = getQuranTxtFiles();
      int k = 0;
      for (final String fn : fns) {
        final String[] parts = fn.split("[.]");
        if (!parts[1].equals(ARABIC) && !parts[1].equals(TRANSLITERATION)) {
          listBox.addItem(parts[1] + " (" + parts[2].replace("_", " ") + ")", (short) k);
          k++;
        }
      }

      listBox.selectItemPos((short) 0, true);

      selectedTranslationLanguage = getItemLanguague(listBox.getSelectedItem());
      selectedTranslationLanguageVersion = getItemVersion(listBox.getSelectedItem());

      registerDialogEvent(TRANSLATION_LANGUAGE_VERSION_LIST_BOX, controlContainer,
          XListBox.class, ON_TRANSLATION_LANGUAGE_VERSION_SELECTED, this);
    }).start();
  }

  private void initializeTransliterationLanguageCheckBox() {
    LOGGER.log(
        Level.FINER, "InsertQuranTextDialog.initializeTransliterationLanguageCheckBox()");
    final XCheckBox checkBox = getControl(controlContainer, XCheckBox.class,
        TRANSLITERATION_LANGUAGE_VERSION_CHECK_BOX);

    checkBox.setState(boolean2Short(false));
    selectedTransliterationInd = short2Boolean(checkBox.getState());

    registerDialogEvent(TRANSLITERATION_LANGUAGE_VERSION_CHECK_BOX, controlContainer,
        XCheckBox.class, ON_TRANSLITERATION_LANGUAGE_CHECK_BOX_SELECTED, this);
  }

  private void initializeTransliterationLanguageVersionListBox() {
    LOGGER.log(Level.FINER,
        "InsertQuranTextDialog.initializeTransliterationLanguageVersionListBox()");
    new Thread(() -> {
      final XListBox listBox = getControl(
          controlContainer, XListBox.class, TRANSLITERATION_LANGUAGE_VERSION_LIST_BOX);

      final List<String> fns = getQuranTxtFiles();
      int k = 0;
      for (final String fn : fns) {
        final String[] parts = fn.split("[.]");
        if (parts[1].equals(TRANSLITERATION)) {
          listBox.addItem(parts[1] + " (" + parts[2].replace("_", " ") + ")", (short) k);
          k++;
        }
      }

      if (0 == k) {
        // no transliteration files. Disable list.
        enableControl(controlContainer, TRANSLITERATION_LANGUAGE_VERSION_LIST_BOX, false);
        return;
      }

      listBox.selectItemPos((short) 0, true);

      selectedTransliterationLanguage = getItemLanguague(listBox.getSelectedItem());
      selectedTransliterationLanguageVersion = getItemVersion(listBox.getSelectedItem());

      registerDialogEvent(TRANSLITERATION_LANGUAGE_VERSION_LIST_BOX, controlContainer,
          XListBox.class, ON_TRANSLITERATION_LANGUAGE_VERSION_SELECTED, this);
    }).start();
  }

  private void initializeArabicFontListBox() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeArabicFontListBox()");
    new Thread(() -> {
      final XListBox listBox = getControl(
          controlContainer, XListBox.class, ARABIC_FONT_LIST_BOX);
      final Locale locale = new Locale.Builder().setScript("ARAB").build();
      final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
          .getAvailableFontFamilyNames(locale);

      for (int i = 0; i < fonts.length; i++) {
        if (new Font(fonts[i], Font.PLAIN, 10).canDisplay(
            0x0627)) { // If Alif -> Arabic support ,
            // DCH TODO: && can handle Quran. Or ship the fonts within the exention and load them
          listBox.addItem(fonts[i], (short) i);
          if (fonts[i].equals(getDefaultArabicFontName())) {
            listBox.selectItemPos((short) i, true);
          }
        }
      }
      listBox.selectItem(getDefaultArabicFontName(), true);
      selectedArabicFontName = listBox.getSelectedItem();

      registerDialogEvent(ARABIC_FONT_LIST_BOX, controlContainer, XListBox.class,
          ON_ARABIC_FONT_SELECTED, this);

    }).start();
  }

  /**
   * Initialize the Arabic Fontsize NumericField.
   */
  private void initializeArabicFontSize() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeArabicFontSize()");
    final XNumericField sizeField = getControl(
        controlContainer, XNumericField.class, ARABIC_FONT_SIZE_NUMERIC_FIELD);

    sizeField.setValue(getDefaultArabicFontSize());
    selectedArabicFontSize = getDefaultArabicFontSize();

    registerDialogEvent(ARABIC_FONT_SIZE_NUMERIC_FIELD, controlContainer, XTextComponent.class,
        ON_ARABIC_FONT_SIZE_NUMERIC_FIELD_CHANGED, this);
  }

  private void initializeLatinFontListBox() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeLatinFontListBox()");
    new Thread(() -> {
      final XListBox listBox = getControl(
          controlContainer, XListBox.class, LATIN_FONT_LIST_BOX);
      final Locale locale = new Locale.Builder().setScript("LATN").build();
      final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
          .getAvailableFontFamilyNames(locale);

      for (int i = 0; i < fonts.length; i++) {
        listBox.addItem(fonts[i], (short) i);
        if (fonts[i].equals(getDefaultLatinFontName())) {
          listBox.selectItemPos((short) i, true);
        }
      }
      listBox.selectItem(getDefaultLatinFontName(), true);
      selectedLatinFontName = listBox.getSelectedItem();

      registerDialogEvent(LATIN_FONT_LIST_BOX, controlContainer, XListBox.class,
          ON_LATIN_FONT_SELECTED, this);

    }).start();
  }

  /**
   * Initialize the Latin Fontsize NumericField.
   */
  private void initializeLatinFontSize() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeLatinFontSize()");
    final XNumericField sizeField = getControl(
        controlContainer, XNumericField.class, LATIN_FONT_SIZE_NUMERIC_FIELD);

    sizeField.setValue(getDefaultLatinFontSize());
    selectedLatinFontSize = getDefaultLatinFontSize();

    registerDialogEvent(LATIN_FONT_SIZE_NUMERIC_FIELD, controlContainer, XTextComponent.class,
        ON_LATIN_FONT_SIZE_NUMERIC_FIELD_CHANGED, this);
  }

  private void initializeLineByLineCheckBox() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeLineByLineCheckBox()");
    final XCheckBox checkBox = getControl(
        controlContainer, XCheckBox.class, LINE_BY_LINE_CHECK_BOX);

    checkBox.setState(boolean2Short(false));
    selectedLineByLineInd = short2Boolean(checkBox.getState());

    registerDialogEvent(LINE_BY_LINE_CHECK_BOX, controlContainer, XCheckBox.class,
        ON_LINE_BY_LINE_CHECK_BOX_SELECTED, this);
  }

  private void initializeInsertButton() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.initializeInsertButton");
    registerDialogEvent(
        INSERT_SURAH_BUTTON, controlContainer, XButton.class, ON_INSERT_SURAH_BUTTON_PRESSED, this);
  }

  private void setProgressBarValue(int v) {
    final XProgressBar progBar = getControl(controlContainer, XProgressBar.class, INSERT_PROGRESS_BAR);
    progBar.setValue(v);
  }

  private void initializeInsertSurahProgressBar() {
    final XProgressBar progBar = getControl(controlContainer, XProgressBar.class, INSERT_PROGRESS_BAR);
    progBar.setRange(0, 100);
    progBar.setValue(0);
  }

  /**
   * Get the available Quran text files.
   *
   * @return list of files
   */
  private List<String> getQuranTxtFiles() {
    final File path = QuranReader.getFilePath("data/quran", componentContext);
    assert path != null;
    final String[] files = path.list((dir, name) -> name.toLowerCase().endsWith(".xml"));
    assert files != null;
    final List<String> fns = Arrays.asList(files);
    Collections.sort(fns);
    return fns;
  }

  /**
   * Transforms the listbox item description of a languguage listbox into a language.
   *
   * @param item the listbox item
   * @return the language
   */
  private static String getItemLanguague(final String item) {
    final String[] itemsSelected = item.split("[(]");
    return itemsSelected[0].trim();
  }

  /**
   * Transforms the listbox item description of a languguage listbox into a text version.
   *
   * @param item the listbox item
   * @return the version
   */
  private static String getItemVersion(final String item) {
    final String[] itemsSelected = item.split("[(]");
    return itemsSelected[1].replace(")", " ").trim().replace(" ", "_");
  }

  /**
   * Get the default font for Arabic.
   *
   * @return the fontsize
   */
  private String getDefaultArabicFontName() {
    return defaultArabicFontName;
  }

  /**
   * Get the default fontsize for Arabic.
   *
   * @return the fontsize
   */
  private double getDefaultArabicFontSize() {
    return defaultArabicFontSize;
  }

  /**
   * Get the default font for non Arabic.
   *
   * @return the fontsize
   */
  private String getDefaultLatinFontName() {
    return defaultLatinFontName;
  }

  /**
   * Get the default latin fontsize.
   *
   * @return the fontsize
   */
  private double getDefaultLatinFontSize() {
    return defaultLatinFontSize;
  }

  @SuppressWarnings("unused")
  protected void handleAyatToNumericFieldChanged() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleAyatToNumericFieldChanged");
    final XNumericField toField = getControl(
        controlContainer, XNumericField.class, AYAT_TO_NUMERIC_FIELD);
    if (Math.round(toField.getValue()) <= selectedAyatFrom) {
      toField.setValue(selectedAyatFrom);
    } else if (Math.round(toField.getValue()) >= QuranReader.getSurahSize(selectedSurahNo)) {
      toField.setValue(QuranReader.getSurahSize(selectedSurahNo));
    }
    selectedAyatTo = Math.round(toField.getValue());
  }

  @SuppressWarnings("unused")
  protected void handleAyatFromNumericFieldChanged() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleAyatFromNumericFieldChanged");
    final XNumericField fromField = getControl(
        controlContainer, XNumericField.class, AYAT_FROM_NUMERIC_FIELD);
    if (Math.round(fromField.getValue()) >= selectedAyatTo) {
      fromField.setValue(selectedAyatTo);
    } else if (Math.round(fromField.getValue()) <= 1) {
      fromField.setValue(1);
    }
    selectedAyatFrom = Math.round(fromField.getValue());
  }

  @SuppressWarnings("unused")
  protected void handleSurahSelected() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleSurahSelected");
    LOGGER.log(Level.FINEST,
        "InsertQuranTextDialog.handleSurahSelected Surah " + "Before: {0}", new Object[]{
            selectedSurahNo
        });

    final XListBox listBox = getControl(controlContainer, XListBox.class, SURAH_LIST_BOX);
    selectedSurahNo = listBox.getSelectedItemPos() + 1;

    final XNumericField numericFieldFrom = getControl(controlContainer, XNumericField.class,
        AYAT_FROM_NUMERIC_FIELD);
    numericFieldFrom.setValue(1);
    selectedAyatFrom = Math.round(numericFieldFrom.getValue());

    final XNumericField numericFieldTo = getControl(controlContainer, XNumericField.class,
        AYAT_TO_NUMERIC_FIELD);
    numericFieldTo.setValue(QuranReader.getSurahSize(selectedSurahNo));
    selectedAyatTo = Math.round(numericFieldTo.getValue());

    LOGGER.log(Level.FINEST,
        "InsertQuranTextDialog.handleSurahSelected Surah " + "After:  {0}", new Object[]{
            selectedSurahNo
        });
  }

  @SuppressWarnings("unused")
  protected void handleTransliterationLanguageVersionSelected() {
    LOGGER.log(
        Level.FINER, "InsertQuranTextDialog.handleTransliterationLanguageVersionSelected()");
  }


  @SuppressWarnings("unused")
  protected void handleAllAyatCheckBoxSelected() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleAllAyatCheckBoxSelected()");
    LOGGER.log(Level.FINEST,
        "InsertQuranTextDialog.handleAllAyatCheckBoxSelected State " + "Before: {0}",
        new Object[]{
            selectedAyatAllInd
        });
    final XCheckBox checkBox = getControl(
        controlContainer, XCheckBox.class, AYAT_ALL_CHECK_BOX);

    checkBox.setState(boolean2Short(!selectedAyatAllInd));
    selectedAyatAllInd = short2Boolean(checkBox.getState());

    enableControl(controlContainer, AYAT_FROM_LABEL, !selectedAyatAllInd);
    enableControl(controlContainer, AYAT_FROM_NUMERIC_FIELD, !selectedAyatAllInd);
    enableControl(controlContainer, AYAT_TO_LABEL, !selectedAyatAllInd);
    enableControl(controlContainer, AYAT_TO_NUMERIC_FIELD, !selectedAyatAllInd);

    if (selectedAyatAllInd) {
      initializeAyatFrom();
      initializeAyatTo();
    }
    LOGGER.log(Level.FINEST,
        "InsertQuranTextDialog.handleAllAyatCheckBoxSelected State " + "After:  {0}",
        new Object[]{
            selectedAyatAllInd
        });
  }

  @SuppressWarnings("unused")
  protected void handleArabicLanguageVersionSelected() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleArabicLanguageVersionSelected()");
    final XListBox listBox = getControl(
        controlContainer, XListBox.class, ARABIC_LANGUAGE_VERSION_LIST_BOX);

    selectedArabicLanguage = getItemLanguague(listBox.getSelectedItem());
    selectedArabicLanguageVersion = getItemVersion(listBox.getSelectedItem());
  }

  @SuppressWarnings("unused")
  protected void handleTranslationLanguageVersionSelected() {
    LOGGER.log(
        Level.FINER, "InsertQuranTextDialog.handleTranslationLanguageVersionSelected()");

    final XListBox listBox = getControl(controlContainer, XListBox.class,
        TRANSLATION_LANGUAGE_VERSION_LIST_BOX);

    selectedTranslationLanguage = getItemLanguague(listBox.getSelectedItem());
    selectedTranslationLanguageVersion = getItemVersion(listBox.getSelectedItem());
  }

  @SuppressWarnings("unused")
  protected void handleTranslationLanguageCheckBoxSelected() {
    LOGGER.log(
        Level.FINER, "InsertQuranTextDialog.handleTranslationLanguageCheckBoxSelected()");
    final XCheckBox checkBox = getControl(
        controlContainer, XCheckBox.class, TRANSLATION_LANGUAGE_VERSION_CHECK_BOX);

    checkBox.setState(boolean2Short(!selectedTranslationInd));
    selectedTranslationInd = short2Boolean(checkBox.getState());

    enableControl(
        controlContainer, TRANSLATION_LANGUAGE_VERSION_LIST_BOX, selectedTranslationInd);
    enableControl(
        controlContainer, TRANSLATION_LANGUAGE_VERSION_LABEL, selectedTranslationInd);

    enableControl(controlContainer, LATIN_FONT_GROUP_BOX,
        selectedTransliterationInd || selectedTranslationInd);
    enableControl(controlContainer, LATIN_FONT_LABEL, selectedTransliterationInd
        || selectedTranslationInd);
    enableControl(controlContainer, LATIN_FONT_LIST_BOX,
        selectedTransliterationInd || selectedTranslationInd);
    enableControl(controlContainer, LATIN_FONT_SIZE_NUMERIC_FIELD,
        selectedTransliterationInd || selectedTranslationInd);

    enableControl(controlContainer, MISCELLANEOUS_GROUP_BOX,
        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);

    enableControl(controlContainer, LINE_BY_LINE_LABEL,
        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
    enableControl(controlContainer, LINE_BY_LINE_CHECK_BOX,
        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);

    enableControl(controlContainer, INSERT_SURAH_BUTTON,
        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
  }

  @SuppressWarnings("unused")
  protected void handleTransliterationLanguageCheckBoxSelected() {
    LOGGER.log(
        Level.FINER, "InsertQuranTextDialog.handleTransliterationLanguageCheckBoxSelected()");
    final XCheckBox checkBox = getControl(
        controlContainer, XCheckBox.class, TRANSLITERATION_LANGUAGE_VERSION_CHECK_BOX);

    checkBox.setState(boolean2Short(!selectedTransliterationInd));
    selectedTransliterationInd = short2Boolean(checkBox.getState());

    enableControl(
        controlContainer, TRANSLITERATION_LANGUAGE_VERSION_LIST_BOX, selectedTransliterationInd);
    enableControl(
        controlContainer, TRANSLITERATION_LANGUAGE_VERSION_LABEL, selectedTransliterationInd);

    enableControl(controlContainer, LATIN_FONT_GROUP_BOX,
        selectedTransliterationInd || selectedTranslationInd);
    enableControl(controlContainer, LATIN_FONT_LABEL, selectedTransliterationInd
        || selectedTranslationInd);
    enableControl(controlContainer, LATIN_FONT_LIST_BOX,
        selectedTransliterationInd || selectedTranslationInd);
    enableControl(controlContainer, LATIN_FONT_SIZE_NUMERIC_FIELD,
        selectedTransliterationInd || selectedTranslationInd);

    enableControl(controlContainer, MISCELLANEOUS_GROUP_BOX,
        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);

    enableControl(controlContainer, LINE_BY_LINE_LABEL,
        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
    enableControl(controlContainer, LINE_BY_LINE_CHECK_BOX,
        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);

    enableControl(controlContainer, INSERT_SURAH_BUTTON,
        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
  }

  @SuppressWarnings("unused")
  protected void handleArabicLanguageCheckBoxSelected() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleArabicLanguageCheckBoxSelected()");
    final XCheckBox checkBox = getControl(
        controlContainer, XCheckBox.class, ARABIC_LANGUAGE_CHECK_BOX);
    checkBox.setState(boolean2Short(!selectedArabicInd));
    selectedArabicInd = short2Boolean(checkBox.getState());

    enableControl(controlContainer, ARABIC_LANGUAGE_LABEL, selectedArabicInd);
    enableControl(controlContainer, ARABIC_LANGUAGE_VERSION_LIST_BOX, selectedArabicInd);
    enableControl(controlContainer, ARABIC_FONT_GROUP_BOX, selectedArabicInd);
    enableControl(controlContainer, ARABIC_FONT_LABEL, selectedArabicInd);
    enableControl(controlContainer, ARABIC_FONT_LIST_BOX, selectedArabicInd);
    enableControl(controlContainer, ARABIC_FONT_SIZE_NUMERIC_FIELD, selectedArabicInd);

    enableControl(controlContainer, MISCELLANEOUS_GROUP_BOX,
        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
    enableControl(controlContainer, LINE_BY_LINE_LABEL,
        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
    enableControl(controlContainer, LINE_BY_LINE_CHECK_BOX,
        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);

    enableControl(controlContainer, INSERT_SURAH_BUTTON,
        selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
  }

  @SuppressWarnings("unused")
  protected void handleArabicFontSelected() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleArabicFontSelected()");
    final XListBox listBox = getControl(
        controlContainer, XListBox.class, ARABIC_FONT_LIST_BOX);

    selectedArabicFontName = listBox.getSelectedItem();
  }

  @SuppressWarnings("unused")
  protected void handleArabicFontSizeNumericFieldChanged() {
    LOGGER.log(Level.FINER, this.getClass().getName() + "." +
        new Object(){}.getClass().getEnclosingMethod().getName());
    final XNumericField sizeField = getControl(
        controlContainer, XNumericField.class, ARABIC_FONT_SIZE_NUMERIC_FIELD);
    final XTextComponent sizeTextComponent = getControl(controlContainer, XTextComponent.class,
        ARABIC_FONT_SIZE_NUMERIC_FIELD);

    selectedArabicFontSize = sizeField.getValue();
  }

  @SuppressWarnings("unused")
  protected void handleLatinFontSelected() {
    LOGGER.log(Level.FINER, this.getClass().getName() + "." +
        new Object(){}.getClass().getEnclosingMethod().getName());
    final XListBox listBox = getControl(
        controlContainer, XListBox.class, LATIN_FONT_LIST_BOX);

    selectedLatinFontName = listBox.getSelectedItem();
  }

  @SuppressWarnings("unused")
  protected void handleLatinFontSizeNumericFieldChanged() {
    LOGGER.log(Level.FINER, this.getClass().getName() + "." +
        new Object(){}.getClass().getEnclosingMethod().getName());
    final XNumericField sizeField = getControl(
        controlContainer, XNumericField.class, LATIN_FONT_SIZE_NUMERIC_FIELD);
    final XTextComponent sizeTextComponent = getControl(controlContainer, XTextComponent.class,
        LATIN_FONT_SIZE_NUMERIC_FIELD);

    selectedLatinFontSize = sizeField.getValue();
  }

  @SuppressWarnings("unused")
  protected void handleLineByLineCheckBoxSelected() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleLineByLineCheckBoxSelecte()");
    final XCheckBox checkBox = getControl(
        controlContainer, XCheckBox.class, LINE_BY_LINE_CHECK_BOX);

    checkBox.setState(boolean2Short(!selectedLineByLineInd));
    selectedLineByLineInd = short2Boolean(checkBox.getState());
  }

  @SuppressWarnings("unused")
  protected void handleInsertSurahButtonPressed() {
    LOGGER.log(Level.FINER, "InsertQuranTextDialog.handleInsertSurahButtonPressed()");
    insertSurah(selectedSurahNo);
    dialog.endExecute();
  }

  public void insertSurah(int surahNumber) {
      final XTextDocument textDoc = DocumentHandler.getCurrentDocument(componentContext);
      XController controller = textDoc.getCurrentController();
      XTextViewCursorSupplier textViewCursorSupplier = DocumentHandler.getCursorSupplier(controller);
      XTextViewCursor textViewCursor = textViewCursorSupplier.getViewCursor();
      XText text = textViewCursor.getText();
      XTextCursor textCursor = text.createTextCursorByRange(textViewCursor.getStart());
      XParagraphCursor paragraphCursor = (XParagraphCursor)UnoRuntime.queryInterface(XParagraphCursor.class, textCursor);
      XPropertySet paragraphCursorPropertySet = DocumentHandler.getPropertySet(paragraphCursor);

      try {
         paragraphCursorPropertySet.setPropertyValue("CharFontName", selectedLatinFontName);
         paragraphCursorPropertySet.setPropertyValue("CharFontNameComplex", selectedArabicFontName);
         paragraphCursorPropertySet.setPropertyValue("CharHeight", selectedLatinFontSize);
         paragraphCursorPropertySet.setPropertyValue("CharHeightComplex", selectedArabicFontSize);
         long from = selectedAyatAllInd ? 1L : selectedAyatFrom;
         long to = selectedAyatAllInd ? QuranReader.getSurahSize(surahNumber) + 1L : selectedAyatTo + 1L;
         if (selectedLineByLineInd) {
            insertSurahLineByLine(surahNumber, text, paragraphCursor, from, to);
         } else {
            insertSurahAsOneBlock(surahNumber, text, paragraphCursor, from, to);
         }
      } catch (UnknownPropertyException | PropertyVetoException | WrappedTargetException | com.sun.star.lang.IllegalArgumentException e) {
         e.printStackTrace();
      }
   }

  private static short getLanguageWritingMode(String language) {
    Map<String, Short> directionmap = new LinkedHashMap();
    directionmap.put("Arabic", Short.valueOf((short)1));
    directionmap.put("English", Short.valueOf((short)0));
    directionmap.put("Dutch", Short.valueOf((short)0));
    directionmap.put("Indonesian", Short.valueOf((short)0));
    directionmap.put("Urdu", Short.valueOf((short)1));
    return directionmap.containsKey(language) ? (Short)directionmap.get(language) : 0;
  }

   private static int getFontNumberBase(String fontname) {
      Map<String, Integer> fontmap = new LinkedHashMap();
      fontmap.put("Al Qalam Quran Majeed", 1776);
      fontmap.put("Al Qalam Quran Majeed 1", 1776);
      fontmap.put("Al Qalam Quran Majeed 2", 1776);
      fontmap.put("Noto Nastaliq Urdu", 1632);
      fontmap.put("KFGQPC HAFS Uthmanic Script", 48);
      fontmap.put("me_quran", 1632);
      fontmap.put("Scheherazade", 1632);
      fontmap.put("Scheherazade quran", 1632);
      return fontmap.containsKey(fontname) ? (Integer)fontmap.get(fontname) : 48;
   }

  public static String numToAyatNumber(long n, String language, String fontname) {
    int base = getFontNumberBase(fontname);

    StringBuilder as;
    for(as = new StringBuilder(); n > 0L; n /= 10L) {
       as.append(Character.toChars(base + (int)(n % 10L)));
    }

    return as.reverse().toString();
  }

  private String getBismillah(String language, String version) {
    QuranReader qr = new QuranReader(language, version, componentContext);
    return qr.getBismillah();
  }

  private String getAyahLine(int surahno, long ayahno, String language, String version, String fontName) {
    QuranReader qr = new QuranReader(language, version, componentContext);
    String line = qr.getAyahNoOfSuraNo(surahno, ayahno);
    if (selectedLineNumberInd) {
      if (getLanguageWritingMode(language) == 1) {
        line = line + " " + RIGHT_PARENTHESIS + numToAyatNumber(ayahno, language, fontName) + LEFT_PARENTHESIS + " ";
      } else {
         line = "(" + numToAyatNumber(ayahno, language, fontName) + ") " + line;
      }
    }

    return line;
  }

  private void insertBismillahSurahLineByLIne(XText text, XParagraphCursor paragraphCursor) {
      try {
         if (selectedArabicInd) {
            insertParagraph(text, paragraphCursor, getBismillah(selectedArabicLanguage, selectedArabicLanguageVersion), selectedArabicLanguage, selectedArabicFontName, selectedArabicFontSize);
         }

         if (selectedTranslationInd) {
            insertParagraph(text, paragraphCursor, getBismillah(selectedTranslationLanguage, selectedTranslationLanguageVersion), selectedTranslationLanguage, selectedLatinFontName, selectedLatinFontSize);
         }

         if (selectedTransliterationInd) {
            insertParagraph(text, paragraphCursor, getBismillah(selectedTransliterationLanguage, selectedTransliterationLanguageVersion), selectedTranslationLanguage, selectedLatinFontName, selectedLatinFontSize);
         }
      } catch (UnknownPropertyException | PropertyVetoException | WrappedTargetException | com.sun.star.lang.IllegalArgumentException e) {
         e.printStackTrace();
      }

   }

   private void insertParagraph(XText text, XParagraphCursor paragraphCursor, String paragraph, String language, String fontName, double fontSize) throws UnknownPropertyException, PropertyVetoException, WrappedTargetException {
      paragraphCursor.gotoEndOfParagraph(false);
      text.insertControlCharacter(paragraphCursor, (short)0, false);
      XPropertySet paragraphCursorPropertySet = DocumentHandler.getPropertySet(paragraphCursor);
      if (getLanguageWritingMode(language) == 0) {
         paragraphCursorPropertySet.setPropertyValue("ParaAdjust", ParagraphAdjust.LEFT);
         paragraphCursorPropertySet.setPropertyValue("WritingMode", Short.valueOf((short)0));
         paragraphCursorPropertySet.setPropertyValue("CharFontName", fontName);
         paragraphCursorPropertySet.setPropertyValue("CharHeight", fontSize);
      } else {
         paragraphCursorPropertySet.setPropertyValue("ParaAdjust", ParagraphAdjust.RIGHT);
         paragraphCursorPropertySet.setPropertyValue("WritingMode", Short.valueOf((short)1));
         paragraphCursorPropertySet.setPropertyValue("CharFontNameComplex", fontName);
         paragraphCursorPropertySet.setPropertyValue("CharHeightComplex", fontSize);
      }

      text.insertString(paragraphCursor, paragraph, false);
   }

   private void insertSurahAsOneBlock(int surahNumber, XText text, XParagraphCursor paragraphCursor, long from, long to) {
      if (selectedArabicInd) {
         insertSurahTextBlock(surahNumber, text, paragraphCursor, from, to, selectedArabicLanguage, selectedArabicLanguageVersion, selectedArabicFontName, selectedArabicFontSize);
      }

      if (selectedTranslationInd) {
         insertSurahTextBlock(surahNumber, text, paragraphCursor, from, to, selectedTranslationLanguage, selectedTranslationLanguageVersion, selectedLatinFontName, selectedLatinFontSize);
      }

      if (selectedTransliterationInd) {
         insertSurahTextBlock(surahNumber, text, paragraphCursor, from, to, selectedTransliterationLanguage, selectedTransliterationLanguageVersion, selectedLatinFontName, selectedArabicFontSize);
      }

   }

   private void insertSurahLineByLine(int surahNumber, XText text, XParagraphCursor paragraphCursor, long from, long to) {
      try {
         if (from == 1L && surahNumber != 1 && surahNumber != 9) {
            insertBismillahSurahLineByLIne(text, paragraphCursor);
         }

         for(long l = from; l < to; ++l) {
            setProgressBarValue((int)(100L * l / (to - from + 1L)));
            if (selectedArabicInd) {
               insertParagraph(text, paragraphCursor, getAyahLine(surahNumber, l, selectedArabicLanguage, selectedArabicLanguageVersion, selectedArabicFontName), selectedArabicLanguage, selectedArabicFontName, selectedArabicFontSize);
            }

            if (selectedTranslationInd) {
               insertParagraph(text, paragraphCursor, getAyahLine(surahNumber, l, selectedTranslationLanguage, selectedTranslationLanguageVersion, selectedLatinFontName), selectedTranslationLanguage, selectedLatinFontName, selectedLatinFontSize);
            }

            if (selectedTransliterationInd) {
               insertParagraph(text, paragraphCursor, getAyahLine(surahNumber, l, selectedTransliterationLanguage, selectedTransliterationLanguageVersion, selectedLatinFontName), selectedTransliterationLanguage, selectedLatinFontName, selectedLatinFontSize);
            }
         }
      } catch (UnknownPropertyException | PropertyVetoException | WrappedTargetException | com.sun.star.lang.IllegalArgumentException e) {
         e.printStackTrace();
      }
   }

   private void insertSurahTextBlock(int surahNumber, XText text, XParagraphCursor paragraphCursor, long from, long to, String language, String languageVersion, String fontName, double fontSize) {
      try {
         StringBuilder lb = new StringBuilder();

         for(long l = from; l < to; ++l) {
            if (l == 1L && surahNumber != 1 && surahNumber != 9) {
               lb.append(getBismillah(language, languageVersion));
               lb.append("\n");
            }

            setProgressBarValue((int)(100L * l / (to - from + 1L)));
            lb.append(getAyahLine(surahNumber, l, language, languageVersion, fontName));
            lb.append(" ");
         }

         insertParagraph(text, paragraphCursor, lb.toString() + "\n", language, fontName, fontSize);
      } catch (UnknownPropertyException | PropertyVetoException | WrappedTargetException | com.sun.star.lang.IllegalArgumentException e) {
         e.printStackTrace();
      }
   }
}
