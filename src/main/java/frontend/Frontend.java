package main.java.frontend;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.backend.Backend;
import main.java.backend.BackendInterface;
import test.BackendUnitTests;

public class Frontend extends Application {
  public static final String APP_NAME = "image-gradient-doohickey"; // TODO rename later lol
  public static final String BASE_DIRECTORY = "image-gradient-doohickey";
  int width = 1000; // TODO change with some sort of config?
  int height = 800;

  @Override
  public void start(Stage stage) throws Exception {
    BackendInterface back = new Backend();
    initializeFiles(back); // TODO remove call when done with file management controls
    SceneBuilder builder = new FrontendSceneBuilder(back);

    Scene scene = new Scene(builder.buildSceneGraph(), width, height);
    stage.setScene(scene);
    stage.setTitle(APP_NAME);
    stage.show();
  }

  private void initializeFiles(BackendInterface back) throws Exception {
    // Get a File representing a default directory of images
    String relativePath = BASE_DIRECTORY + "/src/main/assets/DefaultImages";
    File file;
    URI uri = null;
    try {
      uri = BackendUnitTests.class.getClassLoader().getResource(relativePath).toURI();
    } catch (URISyntaxException e) {
      return;
    }
    file = new File(uri);
    if (file.exists()) {
      // Send to the Backend
      back.receiveFile(file);
    }
  }
}
