package dz.djalel.LO.utils;

import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlContainer;
import com.sun.star.beans.Property;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XMultiPropertySet;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import java.util.logging.Level;
import java.util.logging.Logger;
import dz.djalel.LO.dialog.AddonDialog;

/**
 * AddonDialogTools provides functionality for AddOnDialogs.
 */
public class AddonDialogTools {

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private AddonDialogTools() { /* EMPTY */
  }

  /**
   * Convert boolean to short.
   *
   * @param b the boolean
   * @return the short
   */
  public static short boolean2Short(final boolean b) {
    return (short) (b ? 1 : 0);
  }

  /**
   * Convert short to boolean.
   *
   * @param s the short
   * @return the boolean
   */
  public static boolean short2Boolean(final short s) {
    return s != 0;
  }

  /**
   * Enables a control.
   *
   * @param controlContainer the control container
   * @param id               the id
   * @param toggle           the toggle
   */
  public static void enableControl(
      final XControlContainer controlContainer, final String id, final boolean toggle) {
    final XControl dialogControl = getControl(controlContainer, XControl.class, id);
    final XPropertySet ps = getPropertSet(dialogControl.getModel());
    try {
      ps.setPropertyValue(AddonDialog.PROP_ENABLED, toggle);
    } catch (final IllegalArgumentException | UnknownPropertyException |
        WrappedTargetException | PropertyVetoException ex) {
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
  }

  /**
   * Gets a control.
   *
   * @param <T>              The Type of control.
   * @param controlContainer the control container
   * @param type             the Class of the type
   * @param name             the name
   * @return returns a control of class type.
   */
  public static <T> T getControl(
      final XControlContainer controlContainer, final Class<T> type, final String name) {
    return UnoRuntime.queryInterface(type, controlContainer.getControl(name));
  }

  /**
   * Get the XPropertySet interface of the given object. "
   *
   * @param object The object to get the property set from.
   * @return The XPropertySet interface of the object.
   */
  public static XPropertySet getPropertSet(final Object object) {
    return UnoRuntime.queryInterface(XPropertySet.class, object);
  }

  /**
   * Gets a XMultiProperty Set.
   *
   * @param object the object
   * @return the multi propert set
   */
  public static XMultiPropertySet getMultiPropertSet(final Object object) {
    return UnoRuntime.queryInterface(XMultiPropertySet.class, object);
  }

  /**
   * Create instance object.
   *
   * @param multiServiceFactory the multi service factory
   * @param s                   the s
   * @return the object
   * @throws Exception the exception
   */
  public static Object createInstance(
      final XMultiServiceFactory multiServiceFactory, final String s) throws Exception {
    return multiServiceFactory.createInstance(s);
  }

  /**
   * Show the Properties of a control..
   *
   * @param controlContainer the control container
   * @param name             the name of the control.
   */
  public static void showProperties(
      final XControlContainer controlContainer, final String name) {
    if (LOGGER.isLoggable(Level.FINEST)) {
      LOGGER.log(Level.FINEST, "AddonDialog.showProperties");
      final XPropertySet propertySet = getPropertSet(
          getControl(controlContainer, XControl.class, name).getModel());
      final Property[] properties = propertySet.getPropertySetInfo().getProperties();

      for (final Property property : properties) {
        try {
          LOGGER.log(Level.FINEST, "{0}.{1} = {2}", new Object[]{
              name,
              property.Name,
              propertySet.getPropertyValue(property.Name)
          });
        } catch (final UnknownPropertyException | WrappedTargetException ex) {
          LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
      }
    }
  }
}
