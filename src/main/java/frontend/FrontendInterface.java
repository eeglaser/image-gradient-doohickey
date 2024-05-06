package main.java.frontend;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This interface is for planning the functionality of the Frontend of the project.
 */
public interface FrontendInterface {
  // locally store Stage maybe. idk i forgot what it needed the first time

  // From extending Application
  public void start(Stage stage);

  /**
   * Creates the initial layout managers for the base structure of the application, and calls on
   * other methods to create the controls for each part.
   * 
   * @param parent the Pane that these controls will belong to
   */
  public void createBaseControls(Pane parent);

  /**
   * Creates the controls for finding the gradient between two images. This functionality should be
   * the primary focus to be implemented first.
   * 
   * @param parent the Pane that these controls will belong to
   */
  public void createFindGradientControls(Pane parent);

  /**
   * Creates the controls for the image loading window. This should not be the primary focus.
   * 
   * @param parent the Pane that these controls will belong to
   */
  public void createLoadImagesControls(Pane parent);

  /**
   * Creates the controls that display the items / images in a gradient / shortest path.
   * 
   * @param parent the Pane that these controls will belong to
   */
  public void createGradientResultsList(Pane parent);

  /**
   * Creates the controls that display the items / images that are loaded in the images list.
   * 
   * @param parent the Pane that these controls will belong to
   */
  public void createLoadedImagesList(Pane parent);
  
}
