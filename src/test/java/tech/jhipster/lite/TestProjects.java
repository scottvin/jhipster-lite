package tech.jhipster.lite;

public final class TestProjects {

  private static String lastProjectFolder;

  private TestProjects() {}

  public static String newTestFolder() {
    lastProjectFolder = TestFileUtils.tmpDirForTest();
    return lastProjectFolder();
  }

  public static String lastProjectFolder() {
    return lastProjectFolder;
  }
}
