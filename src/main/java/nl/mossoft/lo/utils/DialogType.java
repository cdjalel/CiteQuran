package nl.mossoft.lo.utils;

public enum DialogType {
  ABOUTDIALOG("AboutDialog"), ERRORDIALOG("ErrorDialog"), INSERTQURANTEXTDIALOG(
      "InsertQuranTextDialog");

  private final String desc;

  DialogType(final String desc) {
    this.desc = desc;
  }

  @Override
  public String toString() {
    return "nl.mossoft.lo.dialog." + desc;
  }
}
