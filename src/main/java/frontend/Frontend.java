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
  String starterFilesPath = "src/main/assets/DefaultImages";
  int width = 1200; // TODO change with some sort of config?
  int height = 700;

  @Override
  public void start(Stage stage) throws Exception {
    BackendInterface back = new Backend();
    //initializeFiles(back); // TODO
    SceneBuilder builder = new FrontendSceneBuilder(back);

    Scene scene = new Scene(builder.buildSceneGraph(), width, height);
    stage.setScene(scene);
    stage.setTitle(APP_NAME);
    stage.show();
  }

  private void initializeFiles(BackendInterface back) throws Exception {
    // Get a File representing a default directory of images
    // TODO somethins wrong lol
    File file = getFile(starterFilesPath);
    back.receiveFile(file);

  }

  private File getFile(String relativePath) {
    String absolutePath = File.separator + relativePath.replace("/", File.separator);
    File file = new File(absolutePath);
    if (file.exists()) {
      return file;
    } else {
      // We were unable to get the file normally.
      // Now, try to find it through the classpath. This way usually plays nicer with Eclipse.
      URI uri;
      try {
        uri = BackendUnitTests.class.getClassLoader().getResource(relativePath).toURI();
      } catch (URISyntaxException e) {
        System.out.println("uri syntax exception");
        return null;
      }
      file = new File(uri);
      if (file.exists()) {
        return file;
      } else {
        System.out.println("file does not exist");
      }
    }
    return null; // Default return statement to satisfy compiler
  }
}
