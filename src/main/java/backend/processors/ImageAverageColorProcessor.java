package main.java.backend.processors;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageAverageColorProcessor implements ImageColorProcessor {

  /**
   * Finds and returns the average Color of this image by averaging the colors of all its pixels.
   * 
   * @param image The image to process
   * @return The average Color of the image, or null if it could not be processed
   * @throws IllegalArgumentException if the image's dimensions are nonpositive
   */
  @Override
  public Color processImage(Image image) {
    if (image instanceof BufferedImage) { // TODO is this cast technical debt? lol
      BufferedImage bimage = (BufferedImage) image;
      // Nonpositive dimensions would cause weird logic errors or division by zero.
      if (bimage.getHeight() == 0 || bimage.getWidth() == 0) {
        throw new IllegalArgumentException("Image dimensions must be positive");
      }

      // Initialize vars for storing components.
      int r = 0;
      int g = 0;
      int b = 0;
      int a = 0;
      int i = 0;
      int j = 0;
      // Get the total components over every pixel.
      for (i = 0; i < bimage.getWidth(); i++) {
        for (j = 0; j < bimage.getHeight(); j++) {
          Color c = new Color(bimage.getRGB(i, j));
          r += c.getRed();
          g += c.getGreen();
          b += c.getBlue();
          a += c.getAlpha();
        }
      }

      // Convert totals to averages.
      int totalPixels = i * j;
      r = r / totalPixels;
      g = g / totalPixels;
      b = b / totalPixels;
      a = a / totalPixels;

      // Construct the average color!
      return new Color(r, g, b, a);
    } else {
      return null;
    }
  }

  public Color fastProcessImage() {
    int skipFactor = 1;
    // TODO
    return null;
  }

}
