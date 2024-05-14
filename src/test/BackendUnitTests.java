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
  private Backend back; // The current backend instance being used for testing.
  private int baseSize = 0;

  // The base local directory for files used in our tests
  // TODO change once project is named something professional :P
  private static final String BASE_DIRECTORY = "image-gradient-doohickey";

  @BeforeEach
  public void initialize() {
    back = new Backend();
    // TODO load tester files and update baseSize
  }

  // ------------------------------------------------------------------------------------
  // ----------------------------------- File Loading -----------------------------------
  // ------------------------------------------------------------------------------------

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
  public void testLoadUnsupportedFile() {
    back = new Backend();
    String testFilePath = "test/assets/Test_InvalidFile.txt";
    File testFile = getFile(testFilePath);

    // Load in one file...
    try {
      back.receiveFile(testFile);
      Assertions.fail();
    } catch (IOException e) {
      // This is good.
    }
    Assertions.assertTrue(back.getAllImages().isEmpty());
  }

  /**
   * Tests Backend's receiveFile method when passed a file directory that only has non-supported
   * files in it.
   */
  @Test
  public void testLoadUnsupportedDirectory() {
    back = new Backend();
    String testFilePath = "test/assets/Test_Directory_Invalid";
    File testFile = getFile(testFilePath);

    // Load in bad directory...
    try {
      back.receiveFile(testFile);
      Assertions.fail();
    } catch (IOException e) {
      // This is good.
    }
    Assertions.assertTrue(back.getAllImages().isEmpty());
  }

  /**
   * Tests Backend's receiveFile method when passed a file directory that has a mix of non-supported
   * files and valid files in it.
   */
  @Test
  public void testLoadMixedDirectory() {
    back = new Backend();
    String testFilePath = "test/assets/Test_Directory_SemiValid";
    File testFile = getFile(testFilePath);

    // Load in bad directory...
    try {
      back.receiveFile(testFile);
      Assertions.fail();
    } catch (IOException e) {
      // This is good.
    }
    // We should add the good files, and ignore the bad ones.
    Assertions.assertTrue(back.getAllImages().size() == 1);
  }

  // ------------------------------------------------------------------------------------
  // ----------------------------------- Add & Remove -----------------------------------
  // ------------------------------------------------------------------------------------

  public void testAddImage() {
    Color color = new Color(158, 176, 54);
    PreprocessedImage img =
        new PreprocessedImage(getFile("test/assets/Test_RGB_158_176_54.png").getPath(), color);
    
    back.addImage(img);
    Assertions.assertTrue(back.getAllImages().size() == baseSize + 1);
    Assertions.assertTrue(back.hasImage(img));
  }

  /**
   * Tests the Backend's addImage when passed null. The intended behavior is for the method to throw
   * a NullPointerException.
   */
  @Test
  public void testAddNullImage() {
    try {
      back.addImage(null);
      Assertions.fail("No exception thrown for null image");
    } catch (NullPointerException e) {
      // This is good.
      Assertions.assertTrue(back.getAllImages().size() == baseSize);

    } catch (Exception e) {
      Assertions.fail("Unexpected exception: " + e.getClass() + " " + e.getMessage());
    }
  }

  /**
   * Tests the Backend's addImage when the specified image is already in the graph. The intended
   * behavior is for the method to return false and not modify the graph at all.
   */
  public void testAddDuplicateImage() {

  }

  public void testRemoveImage() {

  }

  /**
   * Tests the Backend's removeImage when passed null. The intended behavior is for the method to
   * throw a NullPointerException.
   */
  public void testRemoveNullImage() {
    try {
      back.removeImage(null);
      Assertions.fail("No exception thrown for null image");
    } catch (NullPointerException e) {
      // This is good.
      Assertions.assertTrue(back.getAllImages().size() == baseSize);

    } catch (Exception e) {
      Assertions.fail("Unexpected exception: " + e.getClass() + " " + e.getMessage());
    }
  }

  // ------------------------------------------------------------------------------------
  // ---------------------------------- Helper Methods ----------------------------------
  // ------------------------------------------------------------------------------------

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
