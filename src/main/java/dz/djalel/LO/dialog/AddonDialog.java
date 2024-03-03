/*
 * This file is part of CiteQuranOXT
 *
 * Copyright (C) 2020-2022 <mossie@mossoft.nl>
 * Copyright (C) 2024  Djalel Chefrour <cdjalel@gmail.com>
 *
 * CiteQuranOXT is free software based on QuranLO from <mossie@mossoft.nl> and Android
 * QuranKeyboard from <cdjalel@gmail>. You can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation version 3L icense.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <https://www.gnu.org/licenses/>.
 */

package dz.djalel.LO.dialog;

import com.sun.star.awt.ActionEvent;
import com.sun.star.awt.ItemEvent;
import com.sun.star.awt.TextEvent;
import com.sun.star.awt.XActionListener;
import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlContainer;
import com.sun.star.awt.XControlModel;
import com.sun.star.awt.XDialog;
import com.sun.star.awt.XItemListener;
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
import com.sun.star.lang.XEventListener;
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
import dz.djalel.LO.utils.AddonDialogTools;
import dz.djalel.LO.utils.DialogType;

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
  protected static final String EVENT_ITEM_STATE_CHANGED = "itemStateChanged";
  protected static final String EVENT_TEXT_CHANGED = "textChanged";

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private static final String RESOURCE_BUNDLE = "dz.djalel.LO.messages.DialogLabels";
  private static final Map<String, Method> actionsMap = new LinkedHashMap<>();
  protected final ResourceBundle rb;
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
    rb = ResourceBundle.getBundle(RESOURCE_BUNDLE, locale);
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
    try {
      Class[] parameterTypes = new Class[]{
          XComponentContext.class,
          Locale.class
      };
      Class<?> clazz = Class.forName(dialogType.toString());

      return (AddonDialog) clazz.getDeclaredConstructor(parameterTypes)
          .newInstance(componentContext, locale);
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
        IllegalAccessException | InvocationTargetException ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
    return new ErrorDialog(componentContext, locale);
  }

  protected static <T> void registerDialogEvent(
      final String dlgName, final XControlContainer controlContainer, final Class<T> type,
      final String eventName, final XEventListener listener) {
    LOGGER.log(Level.FINER, "AddonDialog.registerDialogEvent({0},{1})", new Object[]{
        dlgName,
        eventName
    });
    try {
      final String event = dlgName + "." + eventName2Event(eventName);

      final Object dlgControl = AddonDialogTools.getControl(controlContainer, type, dlgName);
      final Method[] methods = dlgControl.getClass().getDeclaredMethods();
      for (final Method method : methods) {
        if (method.getName().equals("addItemListener") || method.getName()
            .equals("addActionListener") || method.getName().equals("addTextListener")) {
          method.invoke(dlgControl, listener);
          final Method handler = listener.getClass()
              .getDeclaredMethod("handle".concat(eventName.substring(2)));
          LOGGER.log(Level.FINER,
              "AddonDialog.registerDialogEvent handler {1} registered for event {0}",
              new Object[]{
                  event,
                  handler.getName(),
                  });
          actionsMap.put(event, handler);
          return;
        }
      }
    } catch (final IllegalAccessException | InvocationTargetException |
        NoSuchMethodException ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  protected static String eventName2Event(final String eventName) {
    switch (eventName.substring(eventName.length() - 7)) {
      case "Pressed" -> {
        return EVENT_ACTION_PERFORMED;
      }
      case "elected" -> {
        return EVENT_ITEM_STATE_CHANGED;
      }
      case "Changed" -> {
        return EVENT_TEXT_CHANGED;
      }
      default ->
          LOGGER.log(Level.SEVERE, "No Mapping for event: {0}", new Object[]{eventName});
    }
    return "???";
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

  protected void insertCheckBox(
      final String dlgName, final int x, final int y, final int width, final int height,
      final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertCheckBox()");
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
          dlgName,
          x,
          y,
          VerticalAlignment.MIDDLE,
          width
      });
      nameContainer.insertByName(dlgName, checkBoxModel);

      AddonDialogTools.showProperties(controlContainer, dlgName);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  /**
   * Insert button.
   *
   * @param dlgName the name of the dialog
   * @param x       the x postion of the dialog
   * @param y       the y postion of the dialog
   * @param width   the width of the dialog
   * @param height  the height of the dialog
   * @param label   the label shown on the button
   * @param enable  enable/disable the control on creation
   */
  protected void insertButton(
      final String dlgName, final int x, final int y, final int width, final int height,
      final String label, final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertButton()");
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
          dlgName,
          x,
          y,
          width
      });
      nameContainer.insertByName(dlgName, buttonModel);
      AddonDialogTools.showProperties(controlContainer, dlgName);
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
  protected void insertLabel(
      final String name, final int x, final int y, final int width, final int height,
      final String label, final short alignment, final float fontHeight,
      final float fontWeight, final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertLabel()");
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
  protected void insertLabel(
      final String name, final int x, final int y, final int width, final int height,
      final String label, final short alignment, final boolean multiline,
      final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertLabel()");
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
  protected void insertGroupBox(
      final String name, final int x, final int y, final int width, final int height,
      final String label, final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertGroupBox()");
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
  protected void insertImageControl(
      final String name, final int x, final int y, final int width, final int height,
      final String url, final boolean enable) {

    LOGGER.log(Level.FINER, "AddonDialog.insertImageControl()");
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

  protected void insertNumericField(
      final String dlgName, final int x, final int y, final int width, final int height,
      final boolean enable) {
    LOGGER.log(Level.FINER, "AddonDialog.insertNumericField()");
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
          dlgName,
          x,
          y,
          true,
          VerticalAlignment.MIDDLE,
          width
      });
      nameContainer.insertByName(dlgName, numericFieldModel);
      AddonDialogTools.showProperties(controlContainer, dlgName);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  protected void insertListBox(
      final String dlgName, final int x, final int y, final int width, final int height,
      final boolean enable) {
    insertListBox(dlgName, x, y, width, height, enable, true);
  }

  protected void insertListBox(
      final String dlgName, final int x, final int y, final int width, final int height,
      final boolean enable, final boolean dropdown) {

    LOGGER.log(Level.FINER, "AddonDialog.insertListBox()");
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
          dropdown,
          enable,
          height,
          dlgName,
          x,
          y,
          width
      });
      nameContainer.insertByName(dlgName, listBoxModel);
      AddonDialogTools.showProperties(controlContainer, dlgName);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  protected void insertProgressBar(
      final String dlgName, final int x, final int y, final int width, final int height) {
    LOGGER.log(Level.FINER, "AddonDialog.insertProgressBar()");
    try {
      final Object progressBarModel = multiServiceFactory.createInstance("com.sun.star.awt.UnoControlProgressBarModel");
      final XMultiPropertySet mps = AddonDialogTools.getMultiPropertSet(progressBarModel);
      mps.setPropertyValues(new String[]{
          PROP_HEIGHT,
          PROP_NAME,
          PROP_POSITION_X,
          PROP_POSITION_Y,
          PROP_WIDTH,
      }, new Object[]{
          Integer.valueOf(height),
          dlgName,
          Integer.valueOf(x),
          Integer.valueOf(y),
          Integer.valueOf(width),
      });

      // The controlmodel is not really available until inserted to the Dialog container
      nameContainer.insertByName(dlgName, progressBarModel);
      AddonDialogTools.showProperties(controlContainer, dlgName);
    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, "Unexpected Exception: " + ex.toString(), ex);
    }
  }

  public void insertHorizontalFixedLine(final String dlgName,final int x, final int y, final int width, final int height) {
    try {
      final Object lineModel = multiServiceFactory.createInstance("com.sun.star.awt.UnoControlFixedLineModel");
      final XMultiPropertySet mps = AddonDialogTools.getMultiPropertSet(lineModel);

      mps.setPropertyValues(
          new String[] {
              "Height",
              "Name",
              "Orientation",
              "PositionX",
              "PositionY",
              "Width"
          },
          new Object[] {
              Integer.valueOf(height),
              dlgName,
              Integer.valueOf(0),
              Integer.valueOf(x),
              Integer.valueOf(y),
              Integer.valueOf(width)
          }
      );

      // The controlmodel is not really available until inserted to the Dialog container
      nameContainer.insertByName(dlgName, lineModel);

    } catch (final Exception ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
}

  @Override
  public void textChanged(final TextEvent textEvent) {
    LOGGER.log(Level.FINER, "AddonDialog.textChanged()");
    handleDialogEvent(textEvent, EVENT_TEXT_CHANGED);
  }

  protected void handleDialogEvent(final EventObject eventObject, final String eventName) {
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
                actionsMap.get(dialogName + "." + eventName).getName()
            });
        actionsMap.get(dialogName + "." + eventName).invoke(this);
      } else {
        LOGGER.log(
            Level.FINER,
            "AddonDialog.handleDialogEvent({0}) No event handler registered for dialog {1}",
            new Object[]{
                eventName,
                dialogName
            });
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

}
