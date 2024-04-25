package backend;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.scene.paint.Color;
import structures.ChoosyDijkstraGraph;

public class Backend implements BackendInterface {
  /**
   * List of all images loaded on the backend.
   */
  protected List<ImageWithAverages> allImages = null;
  /**
   * The graph that the backend uses to find the shortest path.
   */
  protected ChoosyDijkstraGraph<ImageWithAverages, Double> graph = null;

  /**
   * Creates a new Backend
   */
  public Backend() {
    graph = new ChoosyDijkstraGraph<ImageWithAverages, Double>();
    allImages = new ArrayList<ImageWithAverages>();
  }

  /**
   * Takes a File and loads it into the graph.
   */
  @Override
  public void receiveFile(File file) {
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        ImageWithAverages ia = new ImageWithAverages(f);
        addImageToGraph(ia);
      }
    } else {
      ImageWithAverages ia = new ImageWithAverages(file);
      addImageToGraph(ia);
    }
  }

  /**
   * Removes an image and all adjacent edges from the graph.
   * 
   * @param the ImageWithAverages to remove
   * @return true if it was successfully removed, false otherwise
   */
  @Override
  public boolean removeImageFromGraph(ImageWithAverages image) {
    return graph.removeNode(image); // throws NPE if image is null
  }

  /**
   * Adds an image to the graph.
   * 
   * @param image
   * @return
   */
  public boolean addImageToGraph(ImageWithAverages image) {
    boolean retval = true;
    // Nodes must be in the graph before edges can be added
    boolean nodeWorked = graph.insertNode(image); // throws npe if an element is null
    if (nodeWorked) {
      allImages.add(image);
    }
    retval &= nodeWorked;
    // Now all new edges can be added
    // I love my N^2 runtime
    for (ImageWithAverages j : allImages) {
      if (!image.equals(j)) {
        double dist = computeDistanceBetweenImages(image, j);
        retval &= graph.insertEdge(image, j, dist);
        graph.insertEdge(j, image, dist);
      }
    }
    return retval;
  }

  /**
   * Calculates and returns the shortest path between two images.
   * 
   * @param image1 The starting image
   * @param image2 The ending image
   * @return A List of ImageWithAverages representing the path, sorted in order of when each image
   *         is visited
   * @throws NullPointerException     if a parameter is null
   * @throws IllegalArgumentException if a parameter is not in the graph
   * @throws NoSuchElementException   if no path between the given images exists
   */
  @Override
  public List<ImageWithAverages> getPathBetweenImages(ImageWithAverages image1,
      ImageWithAverages image2)
      throws NullPointerException, IllegalArgumentException, NoSuchElementException {
    if (image1 == null || image2 == null) {
      throw new NullPointerException("Both images must be set and not null");
    }
    if (!graph.containsNode(image1) || !graph.containsNode(image2)) {
      throw new IllegalArgumentException("Both images must be in the graph");
    }

    return graph.shortestPathData(image1, image2);
  }

  /**
   * Computes the Euclidean distance between two images using each of their numeric properties.
   * 
   * @param image1 The first image to use
   * @param image2 The second image to use
   * @return the distance between the two images
   */
  @Override
  public double computeDistanceBetweenImages(ImageWithAverages image1, ImageWithAverages image2) {
    return Math.sqrt(Math.pow(image1.getAverageA() + image2.getAverageA(), 2)
        + Math.pow(image1.getAverageR() + image2.getAverageR(), 2)
        + Math.pow(image1.getAverageG() + image2.getAverageG(), 2)
        + Math.pow(image1.getAverageB() + image2.getAverageB(), 2)
        + Math.pow(image1.getAverageH() + image2.getAverageH(), 2)
        + Math.pow(image1.getAverageS() + image2.getAverageS(), 2)
        + Math.pow(image1.getAverageV() + image2.getAverageV(), 2));
  }

  @Override
  public ImageWithAverages findImageClosestToColor(Color color) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ImageWithAverages createDummyImageFromColor(Color color) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Returns a list of all images currently loaded onto the backend.
   * 
   * @return a List of all ImageWithAverages in use by the backend
   */
  @Override
  public List<ImageWithAverages> getAllImages() {
    // Want to return a deep copy.
    List<ImageWithAverages> copy = new ArrayList<ImageWithAverages>();
    for (ImageWithAverages i : allImages) {
      copy.add(i);
    }
    return copy;
  }

}
