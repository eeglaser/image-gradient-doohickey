package main.java.frontend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.backend.Backend;
import main.java.backend.BackendInterface;

public class Frontend extends Application {
  public static final String APP_NAME = "image-gradient-doohickey"; // TODO rename later lol
  int width = 1000; // TODO change with some sort of config?
  int height = 800;
  
  @Override
  public void start(Stage stage) throws Exception {
    BackendInterface back = new Backend();
    SceneBuilder builder = new FrontendSceneBuilder(back);
    
    Scene scene = new Scene(builder.buildSceneGraph(), width, height);
    stage.setScene(scene);
    stage.setTitle(APP_NAME);
    stage.show();
  }
}
