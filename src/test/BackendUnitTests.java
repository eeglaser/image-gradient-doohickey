package test;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.backend.Backend;
import main.java.backend.PreprocessedImage;

/**
 * Tests the Backend with JUnit tests for each method.
 */
public class BackendUnitTests {
  Backend back; // The current backend instance being used for testing.

  // The base local directory for files used in our tests
  // TODO change once project is named something professional :P
  private static final String BASE_DIRECTORY = "image-gradient-doohickey";

  @BeforeEach
  public void initialize() {
    back = new Backend();
    // TODO load tester files
  }

  // ----------------------------------- File Loading -----------------------------------

  /**
   * Tests Backend's receiveFile method when passed a single image file.
   */
  @Test
  public void testLoadFile() {
    // We don't want the preloaded tester files from initialize() for this test.
    back = new Backend();
    String testFilePath = "test/assets/Test_RGB_158_176_54.png";

    // Load in one file...
    try {
      File testFile = getFile(testFilePath);
      back.receiveFile(testFile);

      // Check that the graph now contains the expected file and has expected properties
      List<PreprocessedImage> allImages = back.getAllImages();
      // Expected number of nodes in graph
      Assertions.assertTrue(allImages.size() == 1,
          "Backend's list of images was the wrong size: " + allImages.size());
      // Image has expected file path
      Assertions.assertTrue(allImages.get(0).getImagePath().equals(testFile.getPath()),
          "Backend's loaded image had the wrong path: " + allImages.get(0).getImagePath());
      // Image has expected Color
      Assertions.assertTrue(isCloseColor(allImages.get(0).getColor(), new Color(158, 176, 54)));
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * Tests Backend's receiveFile method when passed a file directory full of multiple images.
   */
  @Test
  public void testLoadDirectory() {
    // We don't want the preloaded tester files from initialize() for this test.
    back = new Backend();
    // This directory has two valid images and nothing else.
    String testFilePath = "test/assets/Test_Directory_Valid";

    // Load in one file...
    try {
      File testFile = getFile(testFilePath);
      back.receiveFile(testFile);

      // Check that the graph now contains the expected file and has expected properties
      List<PreprocessedImage> allImages = back.getAllImages();
      // Expected number of nodes in graph
      Assertions.assertTrue(allImages.size() == 2,
          "Backend's list of images was the wrong size: " + allImages.size());
      // Images have expected file paths
      /*
       * Assertions.assertTrue(allImages.contains(testFile.getPath()),
       * "Backend's loaded image had the wrong path: " + allImages.get(0).getImagePath());
       */
      // Images have expected Colors
      // TODO
      // Assertions.assertTrue(isCloseColor(allImages.get(0).getColor(), new Color(158, 176, 54)));
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * Tests Backend's receiveFile method when passed a single file that is not a supported image. The
   * Backend should throw an IOException.
   */
  @Test
  public void testLoadBadFile() {
    String testFilePath = "test/assets/Test_InvalidFile.txt";
    File testFile = getFile(testFilePath);

    // Load in one file...
    try {
      back.receiveFile(testFile);
      Assertions.fail();
    } catch (IOException e) {
      // This is good.
    }
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
      } catch (URISyntaxException e) {
        Assertions.fail("URI syntax error: " + e.getStackTrace());
        return null;
      }
      file = new File(uri);
      if (file.exists()) {
        return file;
      } else {
        Assertions.fail("File could not be found by uri: " + uri.toString());
      }
    }
    return null; // Default return statement to satisfy compiler
  }

  /**
   * Compares two colors to see if their RGBA values are close enough with epsilon comparison.
   * 
   * @param c1 first color
   * @param c2 second color
   * @return true if they are close enough, false otherwise
   */
  private boolean isCloseColor(Color c1, Color c2) {
    float EPSILON = 0.001f;
    boolean retval = true;
    // I could not get getColorComponents to work before I went to this approach :P
    float[] comp1 = new float[] {c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()};
    float[] comp2 = new float[] {c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()};
    // See if each pair of components is close enough
    for (int i = 0; i < comp1.length; i++) {
      retval &= Math.abs(comp1[i] - comp2[i]) < EPSILON;
    }
    return retval;
  }
}
