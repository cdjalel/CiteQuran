/*
 * This file is part of QuranLO
 *
 * Copyright (C) 2020-2022 <mossie@mossoft.nl>
 *
 * QuranLO is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <https://www.gnu.org/licenses/>.
 */

package nl.mossoft.lo.comp;

import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * The type Registration handler.
 */
public class RegistrationHandler {

  /**
   * Get a component factory for a single component.
   *
   * @param implementationName the implementation name of the component
   * @return the single component factory
   */
  public static XSingleComponentFactory __getComponentFactory(final String implementationName) {
    XSingleComponentFactory factory = null;

    final Class[] classes = findServicesImplementationClasses();

    int i = 0;
    while (i < classes.length && factory == null) {
      final Class<?> clazz = classes[i];
      if (implementationName.equals(clazz.getCanonicalName())) {
        try {
          final Class<?>[] getTypes = new Class[]{String.class};
          final Method getFactoryMethod = clazz.getMethod("__getComponentFactory", getTypes);
          final Object o = getFactoryMethod.invoke(null, implementationName);
          factory = (XSingleComponentFactory) o;
        } catch (final Exception e) {
          System.err.println("Error happened");
          e.printStackTrace();
        }
      }
      i++;
    }
    return factory;
  }

  /**
   * It reads the file `RegistrationHandler.classes` and returns the classes to be implemented.
   *
   * @return An array of classes that implement the XRegistryKey interface.
   */
  private static Class<?>[] findServicesImplementationClasses() {

    final ArrayList<Class<?>> classes = new ArrayList<>();

    final InputStream in = RegistrationHandler.class.getResourceAsStream("RegistrationHandler.classes");

    try (in; final LineNumberReader reader = new LineNumberReader(new InputStreamReader(in))) {
      String line = reader.readLine();
      while (line != null) {
        if (!line.equals("")) {
          line = line.trim();
          try {
            final Class<?> clazz = Class.forName(line);

            final Class<?>[] writeTypes = new Class[]{XRegistryKey.class};
            final Class<?>[] getTypes = new Class[]{String.class};

            final Method writeRegMethod = clazz.getMethod("__writeRegistryServiceInfo", writeTypes);
            final Method getFactoryMethod = clazz.getMethod("__getComponentFactory", getTypes);

            if (writeRegMethod != null && getFactoryMethod != null) {
              classes.add(clazz);
            }

          } catch (final Exception e) {
            e.printStackTrace();
          }
        }
        line = reader.readLine();
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return classes.toArray(new Class[classes.size()]);
  }

  /**
   * Registers the service and returns if it's successful.
   *
   * @param registryKey the registration key
   * @return true if successful
   */
  public static boolean __writeRegistryServiceInfo(final XRegistryKey registryKey) {

    final Class<?>[] classes = findServicesImplementationClasses();

    boolean success = true;
    int i = 0;
    while (i < classes.length && success) {
      final Class<?> clazz = classes[i];
      try {
        final Class<?>[] writeTypes = new Class[]{XRegistryKey.class};
        final Method getFactoryMethod = clazz.getMethod("__writeRegistryServiceInfo", writeTypes);
        final Object o = getFactoryMethod.invoke(null, registryKey);
        success = success && ((Boolean) o).booleanValue();
      } catch (final Exception e) {
        success = false;
        e.printStackTrace();
      }
      i++;
    }
    return success;
  }
}
