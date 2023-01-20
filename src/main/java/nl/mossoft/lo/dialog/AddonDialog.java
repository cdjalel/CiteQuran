package nl.mossoft.lo.dialog;

import static nl.mossoft.lo.utils.DialogType.ABOUTDIALOG;
import static nl.mossoft.lo.utils.DialogType.INSERTQURANTEXTDIALOG;

import com.sun.star.awt.ActionEvent;
import com.sun.star.awt.ItemEvent;
import com.sun.star.awt.TextEvent;
import com.sun.star.awt.XActionListener;
import com.sun.star.awt.XButton;
import com.sun.star.awt.XCheckBox;
import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlContainer;
import com.sun.star.awt.XControlModel;
import com.sun.star.awt.XDialog;
import com.sun.star.awt.XItemListener;
import com.sun.star.awt.XListBox;
import com.sun.star.awt.XTextComponent;
import com.sun.star.awt.XTextListener;
import com.sun.star.awt.XToolkit;
import com.sun.star.awt.XTopWindow;
import com.sun.star.awt.XWindow;
import com.sun.star.awt.XWindowPeer;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XMultiPropertySet;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameContainer;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.VerticalAlignment;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.mossoft.lo.utils.AddonDialogTools;
import nl.mossoft.lo.utils.DialogType;

/**
 * The AddonDialog.
 */
public abstract class AddonDialog implements XActionListener, XItemListener, XTextListener {

  // Property Constants
  public static final String PROP_ALIGN = "Align";
  public static final String PROP_CHAR_HEIGHT_COMPLEX = "CharHeightComplex";
  public static final String PROP_CHAR_FONT_NAME = "CharFontName";
  public static final String PROP_CHAR_FONT_NAME_COMPLEX = "CharFontNameComplex";
  public static final String PROP_CHAR_HEIGHT = "CharHeight";
  public static final String PROP_DECIMAL_ACCURACY = "DecimalAccuracy";
  public static final String PROP_DROPDOWN = "Dropdown";
  public static final String PROP_ENABLED = "Enabled";
  public static final String PROP_FONT_HEIGHT = "FontHeight";
  public static final String PROP_FONT_WEIGHT = "FontWeight";
  public static final String PROP_HEIGHT = "Height";
  public static final String PROP_IMAGEURL = "ImageURL";
  public static final String PROP_LABEL = "Label";
  public static final String PROP_MULTILINE = "MultiLine";
  public static final String PROP_NAME = "Name";
  public static final String PROP_POSITION_X = "PositionX";
  public static final String PROP_POSITION_Y = "PositionY";
  public static final String PROP_PUSHBUTTON_TYPE = "PushButtonType";
  public static final String PROP_SPIN = "Spin";
  public static final String PROP_STATE = "State";
  public static final String PROP_TITLE = "Title";
  public static final String PROP_TRI_STATE = "TriState";
  public static final String PROP_VERTICAL_ALIGN = "VerticalAlign";
  public static final String PROP_WIDTH = "Width";

  // Alignment Constants
  protected static final short ALIGNMENT_LEFT = (short) 0;
  protected static final short ALIGNMENT_CENTER = (short) 1;
  protected static final short ALIGNMENT_RIGHT = (short) 2;

  protected static final String EVENT_ACTION_PERFORMED = "actionPerformed";
  protected static final String EVENT_ITEM_STATE_CHANGED = "ItemStateChanged";
  protected static final String EVENT_TEXT_CHANGED = "TextChanged";

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private static final String RESOURCE_BUNDLE = "nl.mossoft.lo.messages.DialogLabels";
  protected final ResourceBundle resourceBundle;

  private final Map<String, Method> actionsMap = new LinkedHashMap<>();

  protected XComponent component = null;
  protected XComponentContext componentContext;
  protected XMultiComponentFactory multiComponentFactory;
  protected XMultiServiceFactory multiServiceFactory;
  protected XNameContainer nameContainer;
  protected XControl control;
  protected XControlContainer controlContainer;
  protected XDialog dialog;
  protected XTopWindow topWindow = null;
  protected XWindowPeer windowPeer = null;

  /**
   * Instantiates a new AddonDialog. Is the parent of all dialogs in the Addon.
   *
   * @param componentContext the component context
   * @param locale           the locale
   */
  protected AddonDialog(final XComponentContext componentContext, final Locale locale) {
    this.componentContext = componentContext;
    resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE, locale);
    LOGGER.log(Level.FINER, "AddonDialog.Constructor");

    createDialog();
    addDialogControls();
  }


  private void createDialog() {
    LOGGER.log(Level.FINER, "AddonDialog.createDialog()");
    try {
      final Object dialogModel = createInstanceWithContext(
          "com.sun.star.awt.UnoControlDialogModel", componentContext);
      setAddonDialogProperties(dialogModel);

      // The XMultiServiceFactory of the dialogModel is needed to instantiate the
      // controls...
      multiServiceFactory = UnoRuntime.queryInterface(XMultiServiceFactory.class, dialogModel);

      // The named container is used to insert the created controls into...
      nameContainer = UnoRuntime.queryInterface(XNameContainer.class, dialogModel);

      // create the dialog...
      final Object unoControlDialog = createInstanceWithContext(
          "com.sun.star.awt.UnoControlDialog", componentContext);
      control = UnoRuntime.queryInterface(XControl.class, unoControlDialog);

      // The scope of the control container is public...
      controlContainer = UnoRuntime.queryInterface(XControlContainer.class, unoControlDialog);
      topWindow = UnoRuntime.queryInterface(XTopWindow.class, controlContainer);

      // link the dialog and its model...
      final XControlModel controlModel = UnoRuntime.queryInterface(XControlModel.class,
          dialogModel);
      control.setModel(controlModel);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  /**
   * Add dialog controls.
   */
  protected abstract void addDialogControls();

  /**
   * Create instance with context object.
   *
   * @param instance         the instance
   * @param componentContext the component context
   * @return the object
   * @throws Exception the exception
   */
  public static Object createInstanceWithContext(
      final String instance, final XComponentContext componentContext) throws Exception {

    return componentContext.getServiceManager()
        .createInstanceWithContext(instance, componentContext);
  }

  /**
   * Sets dialog properties.
   *
   * @param dialogModel the dialog model
   */
  protected abstract void setAddonDialogProperties(Object dialogModel);

  /**
   * Load the AddonDialog. Factory method to create the individual dialogs..
   *
   * @param dialogType       the dialog type
   * @param componentContext the component context
   * @param locale           the locale
   * @return the addon dialog
   */
  public static AddonDialog loadAddonDialog(
      final DialogType dialogType, final XComponentContext componentContext,
      final Locale locale) {

    if (dialogType == ABOUTDIALOG) {
      LOGGER.log(Level.FINER, "AddonDialog.loadAddonDialog({0})", new Object[]{ABOUTDIALOG});
      return new AboutDialog(componentContext, locale);
    } else if (dialogType == INSERTQURANTEXTDIALOG) {
      LOGGER.log(Level.FINER, "AddonDialog.loadAddonDialog({0})",
          new Object[]{INSERTQURANTEXTDIALOG});
      return new InsertQuranTextDialog(componentContext, locale);
    }
    return null;
  }

  /**
   * Show.
   */
  public void show() {

    if (windowPeer == null) {
      createWindowPeer();
    }
    dialog = UnoRuntime.queryInterface(XDialog.class, control);
    initializeDialog();
    dialog.execute();
  }

  private void createWindowPeer() {
    try {
      final Object extToolkit = createInstanceWithContext("com.sun.star.awt.ExtToolkit",
          componentContext);
      final XToolkit toolkit = UnoRuntime.queryInterface(XToolkit.class, extToolkit);

      final XWindow window = UnoRuntime.queryInterface(XWindow.class, control);
      window.setVisible(false);
      control.createPeer(toolkit, null);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  protected void initializeDialog() {
    LOGGER.log(Level.FINER, "AddonDialog.initializeDialog()");
  }

  protected void insertChkbx(
      final String name, final int x, final int y, final int width, final int height,
      final XItemListener itemListener, final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertChkbx()");
    try {
      final Object checkBoxModel = AddonDialogTools.createInstance(multiServiceFactory,
          "com.sun.star.awt.UnoControlCheckBoxModel");
      final XMultiPropertySet mps = AddonDialogTools.getMultiPropertSet(checkBoxModel);
      mps.setPropertyValues(new String[]{
          PROP_ENABLED,
          PROP_HEIGHT,
          PROP_NAME,
          PROP_POSITION_X,
          PROP_POSITION_Y,
          PROP_VERTICAL_ALIGN,
          PROP_WIDTH
      }, new Object[]{
          enable,
          height,
          name,
          x,
          y,
          VerticalAlignment.MIDDLE,
          width
      });
      nameContainer.insertByName(name, checkBoxModel);
      final XCheckBox checkBox = AddonDialogTools.getControl(
          controlContainer, XCheckBox.class, name);
      checkBox.addItemListener(itemListener);
      AddonDialogTools.showProperties(controlContainer, name);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  /**
   * Insert button.
   *
   * @param name           the name
   * @param x              the x
   * @param y              the y
   * @param width          the width
   * @param height         the height
   * @param label          the label
   * @param actionListener the action listener
   * @param enable         the enable
   */
  protected void insertBttn(
      String name, final int x, final int y, final int width, final int height,
      final String label, final XActionListener actionListener, final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertBttn()");
    try {
      final Object buttonModel = AddonDialogTools.createInstance(multiServiceFactory,
          "com.sun.star.awt.UnoControlButtonModel");
      final XMultiPropertySet mps = AddonDialogTools.getMultiPropertSet(buttonModel);
      mps.setPropertyValues(new String[]{
          PROP_ENABLED,
          PROP_HEIGHT,
          PROP_LABEL,
          PROP_NAME,
          PROP_POSITION_X,
          PROP_POSITION_Y,
          PROP_WIDTH
      }, new Object[]{
          enable,
          height,
          label,
          name,
          x,
          y,
          width
      });

      nameContainer.insertByName(name, buttonModel);
      final XButton button = AddonDialogTools.getControl(
          controlContainer, XButton.class, name);
      button.addActionListener(actionListener);
      AddonDialogTools.showProperties(controlContainer, name);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  /**
   * Insert a Label.
   *
   * @param name       the name
   * @param x          the x
   * @param y          the y
   * @param width      the width
   * @param height     the height
   * @param label      the label
   * @param alignment  the alignment
   * @param fontHeight the font height
   * @param fontWeight the font weight
   * @param enable     enable
   */
  protected void insertLbl(
      final String name, final int x, final int y, final int width, final int height,
      final String label, final short alignment, final float fontHeight,
      final float fontWeight, final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertLbl()");
    try {
      final Object fixedTextModel = AddonDialogTools.createInstance(
          multiServiceFactory, "com.sun.star.awt.UnoControlFixedTextModel");
      final XMultiPropertySet mps = AddonDialogTools.getMultiPropertSet(fixedTextModel);
      mps.setPropertyValues(new String[]{
          PROP_ALIGN,
          PROP_ENABLED,
          PROP_FONT_HEIGHT,
          PROP_FONT_WEIGHT,
          PROP_HEIGHT,
          PROP_LABEL,
          PROP_NAME,
          PROP_POSITION_X,
          PROP_POSITION_Y,
          PROP_VERTICAL_ALIGN,
          PROP_WIDTH
      }, new Object[]{
          alignment,
          enable,
          fontHeight,
          fontWeight,
          height,
          label,
          name,
          x,
          y,
          VerticalAlignment.MIDDLE,
          width
      });
      nameContainer.insertByName(name, fixedTextModel);
      AddonDialogTools.showProperties(controlContainer, name);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  /**
   * Insert fixed text.
   *
   * @param name      the name
   * @param x         the x
   * @param y         the y
   * @param width     the width
   * @param height    the height
   * @param label     the label
   * @param alignment the alignment
   * @param multiline a multiline label
   * @param enable    enable
   */
  protected void insertLbl(
      final String name, final int x, final int y, final int width, final int height,
      final String label, final short alignment, final boolean multiline,
      final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertLbl()");
    try {
      final Object fixedTextModel = AddonDialogTools.createInstance(
          multiServiceFactory, "com.sun.star.awt.UnoControlFixedTextModel");
      final XMultiPropertySet mps = AddonDialogTools.getMultiPropertSet(fixedTextModel);
      mps.setPropertyValues(new String[]{
          PROP_ALIGN,
          PROP_ENABLED,
          PROP_HEIGHT,
          PROP_LABEL,
          PROP_MULTILINE,
          PROP_NAME,
          PROP_POSITION_X,
          PROP_POSITION_Y,
          PROP_VERTICAL_ALIGN,
          PROP_WIDTH
      }, new Object[]{
          alignment,
          enable,
          height,
          label,
          multiline,
          name,
          x,
          y,
          VerticalAlignment.MIDDLE,
          width
      });
      nameContainer.insertByName(name, fixedTextModel);
      AddonDialogTools.showProperties(controlContainer, name);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  /**
   * Insert GroupBox.
   *
   * @param name   the name
   * @param x      the x
   * @param y      the y
   * @param width  the width
   * @param height the height
   * @param label  the label
   * @param enable the enable
   */
  protected void insertGrpbox(
      final String name, final int x, final int y, final int width, final int height,
      final String label, final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertGrpbox()");
    try {
      final Object groupBoxModel = AddonDialogTools.createInstance(multiServiceFactory,
          "com.sun.star.awt.UnoControlGroupBoxModel");
      final XMultiPropertySet mps = AddonDialogTools.getMultiPropertSet(groupBoxModel);
      mps.setPropertyValues(new String[]{
          PROP_ENABLED,
          PROP_HEIGHT,
          PROP_LABEL,
          PROP_NAME,
          PROP_POSITION_X,
          PROP_POSITION_Y,
          PROP_WIDTH
      }, new Object[]{
          enable,
          height,
          label,
          name,
          x,
          y,
          width
      });
      nameContainer.insertByName(name, groupBoxModel);
      AddonDialogTools.showProperties(controlContainer, name);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  /**
   * Insert image control.
   *
   * @param name   the name
   * @param x      the x
   * @param y      the y
   * @param width  the width
   * @param height the height
   * @param url    the url
   * @param enable the enable
   */
  protected void insertImgCntl(
      final String name, final int x, final int y, final int width, final int height,
      final String url, final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertImgCntl()");
    try {
      final Object imageControlModel = AddonDialogTools.createInstance(
          multiServiceFactory, "com.sun.star.awt.UnoControlImageControlModel");
      final XMultiPropertySet mps = AddonDialogTools.getMultiPropertSet(imageControlModel);
      mps.setPropertyValues(new String[]{
          PROP_ENABLED,
          PROP_HEIGHT,
          PROP_IMAGEURL,
          PROP_NAME,
          PROP_POSITION_X,
          PROP_POSITION_Y,
          PROP_WIDTH
      }, new Object[]{
          enable,
          height,
          url,
          name,
          x,
          y,
          width
      });
      nameContainer.insertByName(name, imageControlModel);
      AddonDialogTools.showProperties(controlContainer, name);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  protected void insertNumFld(
      final String name, final int x, final int y, final int width, final int height,
      final XTextListener textListener, final boolean enable) {
    LOGGER.log(Level.FINER, "AddonDialog.insertNumFld()");
    try {
      final Object numericFieldModel = AddonDialogTools.createInstance(multiServiceFactory,
          "com.sun.star.awt.UnoControlNumericFieldModel");
      final XMultiPropertySet mps = AddonDialogTools.getMultiPropertSet(numericFieldModel);
      mps.setPropertyValues(new String[]{
          PROP_ALIGN,
          PROP_DECIMAL_ACCURACY,
          PROP_ENABLED,
          PROP_HEIGHT,
          PROP_NAME,
          PROP_POSITION_X,
          PROP_POSITION_Y,
          PROP_SPIN,
          PROP_VERTICAL_ALIGN,
          PROP_WIDTH
      }, new Object[]{
          ALIGNMENT_CENTER,
          (short) 0,
          enable,
          height,
          name,
          x,
          y,
          true,
          VerticalAlignment.MIDDLE,
          width
      });
      nameContainer.insertByName(name, numericFieldModel);
      final XTextComponent textComponent = AddonDialogTools.getControl(controlContainer,
          XTextComponent.class, name);
      textComponent.addTextListener(textListener);
      AddonDialogTools.showProperties(controlContainer, name);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  protected void insertLstbx(
      final String name, final int x, final int y, final int width, final int height,
      final XItemListener itemListener, final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertLstbx()");
    try {
      final Object listBoxModel = AddonDialogTools.createInstance(multiServiceFactory,
          "com.sun.star.awt.UnoControlListBoxModel");
      final XMultiPropertySet mps = AddonDialogTools.getMultiPropertSet(listBoxModel);
      mps.setPropertyValues(new String[]{
          PROP_DROPDOWN,
          PROP_ENABLED,
          PROP_HEIGHT,
          PROP_NAME,
          PROP_POSITION_X,
          PROP_POSITION_Y,
          PROP_WIDTH
      }, new Object[]{
          true,
          enable,
          height,
          name,
          x,
          y,
          width
      });
      nameContainer.insertByName(name, listBoxModel);
      final XListBox listBox = AddonDialogTools.getControl(
          controlContainer, XListBox.class, name);
      listBox.addItemListener(itemListener);
      AddonDialogTools.showProperties(controlContainer, name);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  @Override
  public void textChanged(final TextEvent textEvent) {
    LOGGER.log(Level.FINER, "AddonDialog.textChanged()");
    handleDialogEvent(textEvent, EVENT_TEXT_CHANGED);
  }


  protected void handleDialogEvent(final EventObject eventObject, String eventName) {
    try {
      final XControl eventSource = UnoRuntime.queryInterface(
          XControl.class, eventObject.Source);
      final XControlModel controlModel = eventSource.getModel();
      final XPropertySet propertySet = AddonDialogTools.getPropertSet(controlModel);

      final String dialogName = (String) propertySet.getPropertyValue(AddonDialog.PROP_NAME);
      if (actionsMap.containsKey(dialogName + "." + eventName)) {
        LOGGER.log(Level.FINER, "AddonDialog.handleDialogEvent({0}) invoked handler: {1}",
            new Object[]{
                eventName,
                actionsMap.get(dialogName + "." + eventName)
            });
        actionsMap.get(dialogName + "." + eventName).invoke(this);
      }
    } catch (final UnknownPropertyException | WrappedTargetException |
        IllegalArgumentException | InvocationTargetException | IllegalAccessException ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  @Override
  public void actionPerformed(final ActionEvent actionEvent) {
    LOGGER.log(Level.FINER, "AddonDialog.actionPerformed()");
    handleDialogEvent(actionEvent, EVENT_ACTION_PERFORMED);
  }

  @Override
  public void itemStateChanged(final ItemEvent itemEvent) {
    LOGGER.log(Level.FINER, "AddonDialog.itemStateChanged");
    handleDialogEvent(itemEvent, EVENT_ITEM_STATE_CHANGED);
  }

  @Override
  public void disposing(final EventObject eventObject) {/* Not yet implemented */
  }

  protected void initializeSupportedEvents(
      final Class<?> dialogClass, final Map<String, String> supportedEvents) {
    LOGGER.log(Level.FINER, "AddonDialog.initializeSupportedEvents()");
    try {
      for (final var entry : supportedEvents.entrySet()) {
        final String methodName = "handle".concat(entry.getKey().substring(2));
        LOGGER.log(Level.FINEST, "AddonDialog.initializeSupportedEvents[{0},{1}]",
            new Object[]{
                entry.getValue(),
                dialogClass.getDeclaredMethod(methodName)
            });
        actionsMap.put(entry.getValue(), dialogClass.getDeclaredMethod(methodName));
      }
      LOGGER.log(Level.FINEST, "AddonDialog.initializeSupportedEvents Registration completed");
    } catch (final NoSuchMethodException | SecurityException ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }
}
