package main.java.backend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import main.java.structures.graph.ChoosyDijkstraGraph;

public class Backend {
  /**
   * List of all images loaded on the backend.
   */
  protected List<PreprocessedImage> allImages = null;
  /**
   * The graph that the backend uses to find the shortest path.
   */
  protected ChoosyDijkstraGraph<PreprocessedImage, Double> graph = null;

  // TODO verify more supported images.
  /**
   * A list of the file extensions that are supported by the Backend. Only files with these
   * extensions can be loaded when calling receiveFile. Others will be ignored.
   */
  public static final String[] SUPPORTED_FILE_EXTENSIONS = new String[] {".png", ".jpg", ".jpeg"};

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
   * @throws IOException If there was trouble loading the file
   * @see main.java.backend.Backend#SUPPORTED_FILE_EXTENSIONS
   */
  public void receiveFile(File file) throws IOException {
    if (file.isDirectory()) {
      for (File f : file.listFiles((pathname) -> {
        // Lambda creates a FileFilter that only accepts supported image files.
        return hasSupportedExtension(pathname);
      })) {
        // Now, the loop will only loop through supported files in this directory.

        // This ugly command chain loads and processes the average color of a BufferedImage that
        // gets created from our file.
        PreprocessedImage image = new PreprocessedImage(f.getPath(), ImageServiceFactory
            .getImageProcessor().processImage(ImageServiceFactory.getImageLoader().loadImage(f)));
        addImageToGraph(image);

      }
    } else {
      // This branch concerns when the file param is just one file and not a directory.
      // Single files have to be supported too!
      if (!hasSupportedExtension(file)) {
        return;
      }
      // This ugly command chain loads and processes the average color of a BufferedImage that
      // gets created from our file.
      PreprocessedImage image = new PreprocessedImage(file.getPath(), ImageServiceFactory
          .getImageProcessor().processImage(ImageServiceFactory.getImageLoader().loadImage(file)));
      addImageToGraph(image);
    }
  }

  private boolean hasSupportedExtension(File pathname) {
    String fileName = pathname.getName();
    String fileExtension = fileName.substring(fileName.indexOf('.'));

    // Checks if the file's extensions is found in the list of supported extensions.
    boolean retval = false;
    for (int i = 0; i < SUPPORTED_FILE_EXTENSIONS.length; i++) {
      retval |= SUPPORTED_FILE_EXTENSIONS[i].equals(fileExtension);
    }
    return retval;
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
   * average Colors in RGB.
   * 
   * @param image1 The first image to use
   * @param image2 The second image to use
   * @return the distance between the two images
   */
  public double computeDistanceBetweenImages(PreprocessedImage image1, PreprocessedImage image2) {
    float[] components1RGB = new float[] {image1.getColor().getRed(), image1.getColor().getGreen(),
        image1.getColor().getBlue(), image1.getColor().getAlpha()};
    float[] components2RGB = new float[] {image2.getColor().getRed(), image2.getColor().getGreen(),
        image2.getColor().getBlue(), image2.getColor().getAlpha()};

    float sumOfSquares = 0;
    for (int i = 0; i < components1RGB.length; i++) {
      sumOfSquares += Math.pow((components1RGB[i] - components2RGB[i]), 2);
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
