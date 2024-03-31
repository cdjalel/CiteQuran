package dz.djalel.LO.utils;

import com.sun.star.deployment.PackageInformationProvider;
import com.sun.star.deployment.XPackageInformationProvider;
import com.sun.star.ucb.XFileIdentifierConverter;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XURLTransformer;
import java.io.File;
import java.net.URL;

public class FileTools {

  private static final String QURAN_RESOURCES = "data/quran/";

  private FileTools() {
  }

  /**
   * Returns a file path for a file in the installed extension, or null on failure.
   */
  public static File getFilePath(final String file, final XComponentContext context) {
    final XPackageInformationProvider packageInformationProvider =
        PackageInformationProvider.get(context);
    final String location =
        packageInformationProvider.getPackageLocation("dz.djalel.LO.CiteQuran");

    try {
      final Object oUrlTransformer = context.getServiceManager()
          .createInstanceWithContext("com.sun.star.util.URLTransformer", context);
      final XURLTransformer xUrlTransformer =
          UnoRuntime.queryInterface(XURLTransformer.class, oUrlTransformer);

      final com.sun.star.util.URL[] ourl = new com.sun.star.util.URL[1];
      ourl[0] = new com.sun.star.util.URL();
      ourl[0].Complete = location + "/" + file;
      xUrlTransformer.parseStrict(ourl);
      final URL url = new URL(ourl[0].Complete);

      return new File(url.toURI());
    } catch (final Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static URL getFileURL(final String file, final XComponentContext context) {
    final XPackageInformationProvider packageInformationProvider =
        PackageInformationProvider.get(context);
    final String location =
        packageInformationProvider.getPackageLocation("dz.djalel.LO.CiteQuran");

    try {
      final Object oUrlTransformer = context.getServiceManager()
          .createInstanceWithContext("com.sun.star.util.URLTransformer", context);
      final XURLTransformer xUrlTransformer =
          UnoRuntime.queryInterface(XURLTransformer.class, oUrlTransformer);

      final com.sun.star.util.URL[] ourl = new com.sun.star.util.URL[1];
      ourl[0] = new com.sun.star.util.URL();
      ourl[0].Complete = location + "/" + file;
      xUrlTransformer.parseStrict(ourl);

      return new URL(ourl[0].Complete);
    } catch (final Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Convert fileName to url string.
   *
   * @param context  the component context
   * @param fileName the file name
   * @return the url string
   */
  public static String convertFileNameToUrl(
      final XComponentContext context, final File fileName) {
    try {
      final XFileIdentifierConverter fileConverter = UnoRuntime.queryInterface(
          XFileIdentifierConverter.class, context.getServiceManager()
              .createInstanceWithContext("com.sun.star.ucb" + ".FileContentProvider",
                  context));
      return fileConverter.getFileURLFromSystemPath("", fileName.getAbsolutePath());
    } catch (final com.sun.star.uno.Exception ex) {
      return null;
    }
  }
}
