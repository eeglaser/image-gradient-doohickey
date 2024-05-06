package main.java.backend.loaders;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

/**
 * Loads an image from a file into an Image object.
 */
public interface ImageLoader {
  Image loadImage(File imageFile) throws IOException;
}
