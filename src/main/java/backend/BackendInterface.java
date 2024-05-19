package main.java.backend;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public interface BackendInterface {
  public void receiveFile(File f) throws IOException;
  public boolean removeImage(PreprocessedImage image);
  public boolean addImage(PreprocessedImage image);
  public List<PreprocessedImage> getImagePath(PreprocessedImage image1, PreprocessedImage image2);
  public List<PreprocessedImage> getAllImages();
  public boolean hasImage(PreprocessedImage img);
  public PreprocessedImage getImage(Color c) throws NoSuchElementException;
}
