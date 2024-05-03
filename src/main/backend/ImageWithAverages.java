package main.backend;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * This class is a shiny wrapper for a JavaFX Image, with its relevant averages easily accessible.
 */
public class ImageWithAverages {
  private Image image; // treat this image as a thumbnail.
  private double r;
  private double g;
  private double b;
  private double a;
  private double h;
  private double s;
  private double v;
  private Color color;

  /**
   * Creates an ImageWithAverages using the specified image. Immediately computes the relevant
   * averages.
   * 
   * @param image
   */
  public ImageWithAverages(File file) {
    String uri = file.toURI().toString();
    this.image = new Image(uri);
    calculateAverages();
    color = new Color(r, g, b, a);
    // Now we scale the image down to a 32x32 thumbnail to take up less memory
    this.image = new Image(uri, 32, 32, false, false);
  }

  /**
   * Initializes fields rgbahsv to the averages of their respective properties over the image.
   */
  private void calculateAverages() {
    // TODO test
    // TODO handle when image fails to load or has weird 0 properties
    PixelReader pr = image.getPixelReader();
    Color color;
    double numPixels = image.getWidth() * image.getHeight();

    // Loop through all pixels and store the values.
    // I wish I knew if there was a faster way to do this... There can be a lotta pixels
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        color = pr.getColor(x, y);
        r += color.getRed();
        g += color.getGreen();
        b += color.getBlue();
        a += color.getOpacity();
        h += color.getHue();
        s += color.getSaturation();
        v += color.getBrightness();
      }
    }

    // Compute final averages
    r = r / numPixels;
    g = g / numPixels;
    b = b / numPixels;
    a = a / numPixels;
    h = h / numPixels;
    s = s / numPixels;
    v = v / numPixels;
  }

  public Image getImage() {
    return image;
  }

  public double getAverageR() {
    return r;
  }

  public double getAverageG() {
    return g;
  }

  public double getAverageB() {
    return b;
  }

  public double getAverageA() {
    return a;
  }

  public double getAverageH() {
    return h;
  }

  public double getAverageS() {
    return s;
  }

  public double getAverageV() {
    return v;
  }

  public Color getAverageColor() {
    return color;
  }
}
