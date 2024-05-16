package main.java.frontend;

import javafx.scene.layout.Pane;
import main.java.backend.BackendInterface;

public class FrontendSceneBuilder implements SceneBuilder {
  BackendInterface back = null;
  
  public FrontendSceneBuilder(BackendInterface back) {
    this.back = back;
  }
  
  @Override
  public Pane buildSceneGraph() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Pane buildImagePathControls() {
    return null; // TODO
  }

  @Override
  public Pane buildFileManagementControls() {
    return null; // TODO
  }

  /**
   * Creates the controls that display the items / images found in a gradient / shortest path.
   */
  public Pane buildResultsList() {
    return null;
  }

  /**
   * Creates the controls that display the items / images that are loaded in the images list.
   */
  public Pane buildLoadedImagesList() {
    return null;
  }
}
