package main.java.backend;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import main.java.structures.graph.ChoosyDijkstraGraph;

/**
 * TODO Javadoc header
 */
public class Backend {
  /**
   * List of all images loaded on the backend.
   */
  protected List<PreprocessedImage> allImages = null;
  /**
   * The graph that the backend uses to find the shortest path.
   */
  protected ChoosyDijkstraGraph<PreprocessedImage, Double> graph = null;

  // TODO verify more supported image types.
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
   * Takes a File and loads it into the graph. this method supports directories containing multiple
   * files.
   * 
   * @throws IOException If there was unforeseen trouble loading the file
   * @see main.java.backend.Backend#SUPPORTED_FILE_EXTENSIONS
   */
  public void receiveFile(File file) throws IOException {
    if (file.isDirectory()) {
      int count = 0;
      for (File f : file.listFiles((pathname) -> {
        // Lambda creates a FileFilter that only accepts supported image files.
        return hasSupportedExtension(pathname);
      })) {
        // Now, the loop will only loop through supported files in this directory.
        addSingleFile(f);
        count++;
      }
      // If there were unsupported files, we want to tell the user.
      int totalLength = file.listFiles().length;
      if (count < totalLength) {
        throw new IOException(
            "Supported files were added where possible, but " + (totalLength - count)
                + " file(s) in directory " + file.getName() + " were unsupported.");
      }
    } else {
      // This branch concerns when the file param is just one file and not a directory.
      addSingleFile(file);
    }
  }

  private void addSingleFile(File file) throws IOException {
    if (!hasSupportedExtension(file)) {
      throw new IOException("File " + file.getName() + " does not have a supported file extension");
    }
    // This ugly command chain loads and processes the average color of a BufferedImage that
    // gets created from our file.
    PreprocessedImage image = new PreprocessedImage(file.getPath(), ImageServiceFactory
        .getImageProcessor().processImage(ImageServiceFactory.getImageLoader().loadImage(file)));
    addImage(image);
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
  public boolean removeImage(PreprocessedImage image) {
    boolean retval = graph.removeNode(image); // throws NPE if image is null
    if (retval) {
      allImages.remove(image);
    }
    return retval;
  }

  /**
   * Adds an image to the Backend. All image nodes in the graph are fully connected.
   * 
   * @param image the PreprocessedImage to add to the graph
   * @return true if the image was successfully added, false otherwise
   * @throws NullPointerException if image is null
   */
  public boolean addImage(PreprocessedImage image) {
    boolean retval = true;
    // Nodes must be in the graph before edges can be added
    boolean nodeWorked = graph.insertNode(image); // throws NPE if image is null
    if (nodeWorked) {
      allImages.add(image);
    }
    retval &= nodeWorked;
    // This if statement lets us skip the loop for edges if the node could not be added.
    if (retval) {
      // Now all new edges can be added
      // I love my N^2 runtime
      for (PreprocessedImage j : allImages) {
        if (!image.equals(j)) {
          double dist = computeImageDistance(image, j);
          retval &= graph.insertEdge(image, j, dist);
          graph.insertEdge(j, image, dist);
        }
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
  public List<PreprocessedImage> getImagePath(PreprocessedImage image1, PreprocessedImage image2)
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
   * Computes the square of the Euclidean distance between two images using the values associated
   * with their average Colors in sRGB.<br>
   * We use the square of the distance to make it seem like distant colors are farther than they
   * are, so that the shortest path algorithm favors hopping to close colors instead of straight to
   * the result.
   * 
   * @param image1 The first image to use
   * @param image2 The second image to use
   * @return the distance between the two images
   */
  public double computeImageDistance(PreprocessedImage image1, PreprocessedImage image2) {
    float[] components1RGB = new float[] {image1.getColor().getRed(), image1.getColor().getGreen(),
        image1.getColor().getBlue(), image1.getColor().getAlpha()};
    float[] components2RGB = new float[] {image2.getColor().getRed(), image2.getColor().getGreen(),
        image2.getColor().getBlue(), image2.getColor().getAlpha()};

    float sumOfSquares = 0;
    for (int i = 0; i < components1RGB.length; i++) {
      sumOfSquares += Math.pow((components1RGB[i] - components2RGB[i]), 2);
    }

    return sumOfSquares;
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

  /**
   * Reports whether the specified image is in the Backend's graph.
   * 
   * @param img The image to search for
   * @return true if the Backend's graph contains this image, false otherwise
   */
  public boolean hasImage(PreprocessedImage img) {
    return graph.containsNode(img);
  }
  
  /**
   * Finds the first instance of an image with the specified average Color.
   * 
   * @param c The average color to match an image to
   * @throws NoSuchElementException If no image could be found with the specified average color
   */
  public PreprocessedImage getImage(Color c) throws NoSuchElementException {
    for(PreprocessedImage i : allImages) {
      if(i.getColor().equals(c)) {
        return i;
      }
    }
    throw new NoSuchElementException("An image of color " + c.toString() + " was not found");
  }
}
