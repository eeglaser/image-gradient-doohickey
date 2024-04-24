package backend;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.scene.paint.Color;

// What do we want the backend to be able to actually do?
/**
 * This interface is simply for planning the functionality of the Backend of the project.
 */
public interface BackendInterface {
  // Plan:
  // Use Graph for finding shortest path
  // -- DijkstraGraph from previous cs400 project?
  // -- alter so that nodes may be enabled or disabled without being unloaded?...
  // Use SearchTree for finding closest image from any given color
  // -- self-balancing 2-3-4 tree?
  // Use List for quickly returning list of all loaded images
  // -- keep an ArrayList here, or perhaps the SearchTree can give us a list in order?

  // ---------------------------------------------------------------------------------

  /**
   * Receives a list of Files from the frontend and loads them into the backend.
   * 
   * @param file A List of files to load into the backend.
   */
  public void receiveFiles(List<File> file);

  // private void unpackSingleFile(File file);

  // private void unpackFileDirectory(File file);

  // Our graph will be complete, so only call when absolutely necessary!
  // Or, implement such that it only adds an image at a time and does not always redo everything!
  /**
   * Adds images into the graph, computing their edge weights along the way. This graph is fully
   * connected and complete, so edges will be created between every image.
   * 
   * @param images the List of ImageWithAverages to add to the graph.
   * @return true if all images were successfully added, false if at least one image was already in
   *         the graph
   */
  // private boolean addImagesIntoGraph(List<ImageWithAverages> images);

  /**
   * Removes images from the graph if they can be found.
   * 
   * @param images the List of images to remove
   * @return true if all images were found and removed, false if at least one image was unable to be
   *         removed
   */
  public boolean removeImagesFromGraph(List<ImageWithAverages> images);

  /**
   * Finds the shortest path between two images.
   * 
   * @param image1 The start of the path
   * @param image2 The end of the path
   * @return a List of ImageWithAverages in order of when they are found on the path
   * @throws NoSuchElementException if no path is possible with the specified parameters
   */
  public List<ImageWithAverages> getPathBetweenImages(ImageWithAverages image1,
      ImageWithAverages image2);

  /**
   * Computes the epic 7-dimensional distance between two images, to be used as an edge cost.
   * 
   * @param image1 The first image
   * @param image2 The second image
   * @return a double representing the total distance between the two images. This will always be
   *         positive.
   */
  public double computeDistanceBetweenImages(ImageWithAverages image1, ImageWithAverages image2);

  // Hmm how to do this? Will we need a second data structure sorted by Color? Perhaps a list we can
  // perform binary search on?
  // Definitely make this after the other features, since it's kinda QOL.
  // Perhaps maintain a sorted list of images that can also be used for other things, such as
  // displaying the complete list of images lmao.
  /**
   * Finds an image in the graph whose averages are closest to a specified color.
   * 
   * @param color the color that an image should be close to
   * @return an ImageWithAverages that is similar to the specified color
   */
  public ImageWithAverages findImageClosestToColor(Color color);

  /**
   * Creates a dummy ImageWithAverages to use in place of a loaded image. This dummy will have an
   * Image made up entirely of pixels of the color it represents, and all its averages will of
   * course point to that color. <br>
   * 
   * @param color The color that the object will represent
   */
  public ImageWithAverages createDummyImageFromColor(Color color);

  /**
   * Gets a list of every image currently loaded.
   * 
   * @return a List of every ImageWithAverages that is loaded.
   */
  public List<ImageWithAverages> getAllImages();
}
