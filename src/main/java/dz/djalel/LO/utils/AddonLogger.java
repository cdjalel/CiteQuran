package dz.djalel.LO.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The Addon logger.
 */
public class AddonLogger {

  private AddonLogger() { /* Utility classes should not have public constructors */
  }

  /**
   * Initializes the logger.
   */
  public static void setup() {
    try {
      System.setProperty(
          "java.util.logging.SimpleFormatter.format",
          "[%1$tF %1$tT] [%4$-7s] %5$s %n");

      final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
      logger.setLevel(Level.FINER);

      final FileHandler fileHandler = new FileHandler("Logging.txt");

      fileHandler.setFormatter(new SimpleFormatter());
      logger.addHandler(fileHandler);
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }

}
