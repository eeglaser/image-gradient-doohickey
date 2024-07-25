package main.java.backend;

import java.awt.Color;

/**
 * This class provides a simple way to bundle an image file with its average color. These are
 * intended to populate the graph.
 */
public class PreprocessedImage {
  private String imagePath;
  private Color processedColor;

  public PreprocessedImage(String path, Color color) {
    imagePath = path;
    processedColor = color;
  }

  public String getImagePath() {
    return imagePath;
  }

  public Color getColor() {
    return processedColor;
  }
  
  @Override
  public String toString() {
    String str = processedColor.toString();
    return(str.substring(str.indexOf("[")) + " " + imagePath);
  }
}
