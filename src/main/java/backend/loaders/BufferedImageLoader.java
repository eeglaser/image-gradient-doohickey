package main.java.backend.loaders;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class creates BufferedImages from image files.
 */
public class BufferedImageLoader implements ImageLoader {
  /**
   * Loads an image file into a BufferedImage.
   * 
   * @param imageFile The file to read from
   * @return an Image object created from the File
   * @throws IOException If there was trouble reading the image file. More specifically, if
   *                     ImageIO.read(imageFile) fails to read the file.
   */
  @Override
  public Image loadImage(File imageFile) throws IOException {
    return ImageIO.read(imageFile);
  }

}
