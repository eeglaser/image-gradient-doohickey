package backend;

import java.io.File;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

/**
 * Tests the Backend with JUnit tests for each method.
 */
public class BackendUnitTests {
  Backend back; // The current backend instance being used for testing.

  // The base local directory for files used in our tests
  private static final String BASE_DIRECTORY = "test-resources";

  @BeforeEach
  public void initialize() {
    back = new Backend();
    // TODO load tester file
  }

  // ----------------------------------- File Loading -----------------------------------

  /**
   * Tests Backend's receiveFile method when passed a single image file.
   */
  public void testLoadFile() {
    // We don't want the preloaded tester files from initialize() for this test.
    back = new Backend();


  }

  /**
   * Tests Backend's receiveFile method when passed a file directory full of multiple images.
   */
  public void testLoadDirectory() {

  }

  /**
   * Tests Backend's receiveFile method when passed a single file that is not a supported image.
   */
  public void testLoadBadFile() {

  }

  /**
   * Tests Backend's receiveFile method when passed a file directory that has non-images in it.
   */
  public void testLoadBadDirectory() {

  }

  /**
   * This private helper method provides the tests a way to get valid File objects without the
   * JavaFX FileChooser that is planned.
   * 
   * @param path the path to a file or directory, relative to BASE_DIRECTORY. Use '/' as a file
   *             separator, and the method will convert it to the system default.
   * @return A File object from the specified path
   */
  private File getFile(String relativePath) {
    String absolutePath =
        BASE_DIRECTORY + File.separator + relativePath.replace("/", File.separator);
    File file = new File(absolutePath);
    if (file.exists()) {
      return file;
    } else {
      Assertions.fail("File built from " + relativePath + " does not exist: " + absolutePath);
    }
    return null; // Default return statement to satisfy compiler
  }
}
