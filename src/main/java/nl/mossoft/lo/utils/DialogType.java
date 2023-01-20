package nl.mossoft.lo.utils;

public enum DialogType {
  ABOUTDIALOG("AboutDialog"),
  INSERTQURANTEXTDIALOG("InsertQuranTextDialog");

  private final String desc;

  DialogType(final String desc) {
    this.desc = desc;
  }

  @Override
  public String toString() {
    return this.desc;
  }
}
