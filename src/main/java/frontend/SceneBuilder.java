package main.java.frontend;

import javafx.scene.layout.Pane;

public interface SceneBuilder {
  public Pane buildSceneGraph();
  
  public Pane buildImagePathControls();
  
  public Pane buildFileManagementControls();
}
