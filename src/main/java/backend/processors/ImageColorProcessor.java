package main.java.backend.processors;

import java.awt.Color;
import java.awt.Image;

/**
 * Processes an image and returns a color.
 */
public interface ImageColorProcessor {
  Color processImage(Image image);
}
