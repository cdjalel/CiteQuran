/*
 * This file is part of QuranLO
 *
 * Copyright (C) 2020-2022 <mossie@mossoft.nl>
 *
 * QuranLO is free software: you can redistribute it and/or modify it under the terms of the
 *  GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program
 * . If
 * not, see <https://www.gnu.org/licenses/>.
 */

package dz.djalel.LO.utils;

import com.sun.star.beans.XPropertySet;
import com.sun.star.frame.XController;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import java.util.Objects;

/**
 * Helps getting desktop, components, frames, cursors and other interfaces.
 */
public class DocumentHandler {

  private DocumentHandler() { /* Empty */
  }

  /**
   * Returns the current text document (if any).
   */
  public static XTextDocument getCurrentDocument(final XComponentContext context) {
    return UnoRuntime.queryInterface(XTextDocument.class, getCurrentComponent(context));
  }

  /**
   * Returns the current XComponent.
   */
  private static XComponent getCurrentComponent(final XComponentContext context) {
    return Objects.requireNonNull(getCurrentDesktop(context)).getCurrentComponent();
  }

  /**
   * Returns the current XDesktop.
   */
  private static XDesktop getCurrentDesktop(final XComponentContext context) {
    final XMultiComponentFactory factory = UnoRuntime.queryInterface(
        XMultiComponentFactory.class, context.getServiceManager());
    Object desktop = null;
    try {
      desktop = factory.createInstanceWithContext("com.sun.star.frame.Desktop", context);
    } catch (final Exception e) {
      return null;
    }
    return UnoRuntime.queryInterface(com.sun.star.frame.XDesktop.class, desktop);
  }

  public static XTextViewCursorSupplier getCursorSupplier(final XController controller) {
    return UnoRuntime.queryInterface(XTextViewCursorSupplier.class, controller);
  }

  public static XPropertySet getPropertySet(final Object graphic) {
    return UnoRuntime.queryInterface(com.sun.star.beans.XPropertySet.class, graphic);
  }

}
