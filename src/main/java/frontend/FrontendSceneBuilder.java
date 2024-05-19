package main.java.frontend;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.java.backend.BackendInterface;

public class FrontendSceneBuilder implements SceneBuilder {
  BackendInterface back = null;

  public FrontendSceneBuilder(BackendInterface back) {
    this.back = back;
  }

  @Override
  public Pane buildSceneGraph() {
    // This gridpane will contain most of the application
    GridPane gridpane = new GridPane();
    ColumnConstraints column1 = new ColumnConstraints();
    column1.setPercentWidth(50);
    ColumnConstraints column2 = new ColumnConstraints();
    column2.setPercentWidth(50);
    gridpane.getColumnConstraints().addAll(column1, column2);
    // add the functionality controls to the gridpane
    // remember that gridpanes use column,row coordinates
    gridpane.add(buildImagePathControls(), 0, 0);
    gridpane.add(buildFileManagementControls(), 1, 0);

    BorderPane base = new BorderPane();
    base.setCenter(gridpane);
    // TODO add an about button at the bottom

    return base;
  }

  @Override
  public Pane buildImagePathControls() {
    // Functionality title at the top
    Label title = buildFormattedLabel("Find Images Gradient", 0);
    
    // Label for the starting image
    Label startLabel = buildFormattedLabel("Starting Image", 2);

    // Combobox for choosing the starting image
    ComboBox<HBox> startCB = null;// TODO

    // Label for the ending image
    Label endLabel = buildFormattedLabel("Ending Image", 2);

    // Combobox for choosing the ending image
    ComboBox<HBox> endCB = null;// TODO

    // Label for the results list
    Label resultsLabel = buildFormattedLabel("Results", 1);

    // ScrollPane or ListView of results
    Pane resultsList = buildResultsList();

    // Add everything into a nice VBox.
    VBox base = new VBox();
    base.getChildren().addAll(title, startLabel, startCB, endCB, resultsLabel, resultsList);
    return base;
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

  /**
   * Creates and returns a Label object with the specified text that has been formatted as a certain
   * text type with JavaFX CSS according to the specified code. The following are the possible
   * codes:<br>
   * 0: Title<br>
   * 1: Heading<br>
   * 2: Normal text<br>
   * Any other code will also default to normal text.<br>
   * 
   * @param text The text that will appear in the label
   * @param code The code that determines the label type
   * @return a Label that will be formatted when rendered
   */
  private Label buildFormattedLabel(String text, int code) {
    Label label = new Label(text);
    // TODO assign the label to something for jfxcss
    switch (code) {
      case 0: // Title
        break;
      case 1: // Header
        break;
      case 2: // Normal text
        break;
      default:

    }
    return label;
  }
}
