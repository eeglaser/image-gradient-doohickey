package main.java.backend;

import main.java.backend.loaders.BufferedImageLoader;
import main.java.backend.loaders.ImageLoader;
import main.java.backend.processors.ImageAverageColorProcessor;
import main.java.backend.processors.ImageColorProcessor;

/**
 * Constructs ImageLoaders and ImageProcessors.
 */
public class ImageServiceFactory {
  public static ImageLoader getImageLoader() {
    return new BufferedImageLoader();
  }
  
  public static ImageColorProcessor getImageProcessor() {
    return new ImageAverageColorProcessor();
  }
}
