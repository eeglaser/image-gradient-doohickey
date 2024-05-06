package test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.backend.Backend;

/**
 * Tests the Backend with JUnit tests for each method.
 */
public class BackendUnitTests {
  Backend back; // The current backend instance being used for testing.

  // The base local directory for files used in our tests
  // TODO change one project is named something professional :P
  private static final String BASE_DIRECTORY = "image-gradient-doohickey";

  @BeforeEach
  public void initialize() {
    back = new Backend();
    // TODO load tester file
  }

  // ----------------------------------- File Loading -----------------------------------

  /**
   * Tests Backend's receiveFile method when passed a single image file.
   */
  @Test
  public void testLoadFile() {
    // We don't want the preloaded tester files from initialize() for this test.
    back = new Backend();
    
    // Load in one file...
    try {
      back.receiveFile(getFile("test/assets/Test_HSV_All69.png"));
    } catch (IOException e) {
      Assertions.fail(e.getStackTrace().toString());
    }

    // Check that the graph now contains the expected file


  }

  /**
   * Tests Backend's receiveFile method when passed a file directory full of multiple images.
   */
  @Test
  public void testLoadDirectory() {
    // TODO
  }

  /**
   * Tests Backend's receiveFile method when passed a single file that is not a supported image.
   */
  public void testLoadBadFile() {

  }

  /**
   * Tests Backend's receiveFile method when passed a file directory that has non-supported files in
   * it.
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
      // We were unable to get the file normally.
      // Now, try to find it through the classpath. This way usually plays nicer with Eclipse.
      URI uri;
      try {
        uri = BackendUnitTests.class.getClassLoader().getResource(relativePath).toURI();
      }catch(URISyntaxException e) {
        Assertions.fail("URI syntax error: " + e.getStackTrace());
        return null;
      }
      file = new File(uri);
      if(file.exists()) {
        return file;
      }else {
        Assertions.fail("File could not be found by uri: " + uri.toString());
      }
    }
    return null; // Default return statement to satisfy compiler
  }
}
