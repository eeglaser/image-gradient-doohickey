package backend;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

  @Override
  public void receiveFiles(List<File> file) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean removeImagesFromGraph(List<ImageWithAverages> images) {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean addImagesToGraph(List<ImageWithAverages> images) {
    boolean retval = true;
    // Nodes must be in the graph before edges can be added
    for(ImageWithAverages i : images) {
      boolean nodeWorked = graph.insertNode(i); // throws npe if an element is null
      if(nodeWorked) {
        allImages.add(i);
      }
      retval &= nodeWorked;
    }
    // Now all new edges can be added
    // I love my N^2 runtime
    for(ImageWithAverages i : images) {
      for(ImageWithAverages j : allImages) {
        if(!i.equals(j)) {
          double dist = computeDistanceBetweenImages(i, j);
          retval &= graph.insertEdge(i, j, dist);
          graph.insertEdge(j, i, dist);
        }
      }
    }
    return retval;
  }

  @Override
  public List<ImageWithAverages> getPathBetweenImages(ImageWithAverages image1,
      ImageWithAverages image2) {
    // TODO Auto-generated method stub
    return null;
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
