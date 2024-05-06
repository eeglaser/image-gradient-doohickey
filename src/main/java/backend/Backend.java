package main.java.backend;

import java.awt.color.ColorSpace;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import main.java.structures.graph.ChoosyDijkstraGraph;

// TODO engineer the backend better lol
public class Backend {
  /**
   * List of all images loaded on the backend.
   */
  protected List<PreprocessedImage> allImages = null;
  /**
   * The graph that the backend uses to find the shortest path.
   */
  protected ChoosyDijkstraGraph<PreprocessedImage, Double> graph = null;

  /**
   * Creates a new Backend
   */
  public Backend() {
    graph = new ChoosyDijkstraGraph<PreprocessedImage, Double>();
    allImages = new ArrayList<PreprocessedImage>();
  }

  /**
   * Takes a File and loads it into the graph.
   * 
   * @throws IOException If there was trouble loading the image
   */
  public void receiveFile(File file) throws IOException {
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        PreprocessedImage image =
            new PreprocessedImage(f.getPath(), ImageServiceFactory.getImageProcessor()
                .processImage(ImageServiceFactory.getImageLoader().loadImage(file)));
        addImageToGraph(image);
      }
    } else {
      PreprocessedImage image = new PreprocessedImage(file.getPath(), ImageServiceFactory
          .getImageProcessor().processImage(ImageServiceFactory.getImageLoader().loadImage(file)));
      addImageToGraph(image);
    }
  }

  /**
   * Removes an image from the Backend. Its node and all adjacent edges will be removed from the
   * graph. This is not the same as disabling an image!
   * 
   * @param the PreprocessedImage to remove
   * @return true if it was successfully removed, false otherwise
   */
  public boolean removeImageFromGraph(PreprocessedImage image) {
    return graph.removeNode(image); // throws NPE if image is null
  }

  /**
   * Adds an image to the Backend. All image nodes in the graph are fully connected.
   * 
   * @param image
   * @return
   */
  public boolean addImageToGraph(PreprocessedImage image) {
    boolean retval = true;
    // Nodes must be in the graph before edges can be added
    boolean nodeWorked = graph.insertNode(image); // throws npe if an element is null
    if (nodeWorked) {
      allImages.add(image);
    }
    retval &= nodeWorked;
    // Now all new edges can be added
    // I love my N^2 runtime
    for (PreprocessedImage j : allImages) {
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
   * @return A List of PreprocessedImage representing the path, sorted in order of when each image
   *         is visited
   * @throws NullPointerException     if a parameter is null
   * @throws IllegalArgumentException if a parameter is not in the graph
   * @throws NoSuchElementException   if no path between the given images exists
   */
  public List<PreprocessedImage> getPathBetweenImages(PreprocessedImage image1,
      PreprocessedImage image2)
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
   * Computes the Euclidean distance between two images using the values associated with their
   * average Colors in HSV.
   * 
   * @param image1 The first image to use
   * @param image2 The second image to use
   * @return the distance between the two images
   */
  public double computeDistanceBetweenImages(PreprocessedImage image1, PreprocessedImage image2) {
    float[] components1HSV = new float[] {};
    float[] components2HSV = new float[] {};
    image1.getColor().getColorComponents(ColorSpace.getInstance(ColorSpace.TYPE_HSV),
        components1HSV);
    image2.getColor().getColorComponents(ColorSpace.getInstance(ColorSpace.TYPE_HSV),
        components2HSV);
    
    float sumOfSquares = 0;
    for(int i = 0; i < components1HSV.length; i++) {
      sumOfSquares += Math.pow((components1HSV[i] - components2HSV[i]), 2);
    }
    
    return Math.sqrt(sumOfSquares);
  }

  /**
   * Returns a list of all images currently loaded onto the backend.
   * 
   * @return a List of all PreprocessedImage in use by the backend
   */
  public List<PreprocessedImage> getAllImages() {
    // Want to return a deep copy.
    List<PreprocessedImage> copy = new ArrayList<PreprocessedImage>();
    for (PreprocessedImage i : allImages) {
      copy.add(i);
    }
    return copy;
  }

}
