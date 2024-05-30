package main.java.frontend;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import main.java.backend.BackendInterface;
import main.java.backend.PreprocessedImage;

public class FrontendSceneBuilder implements SceneBuilder {
  BackendInterface back = null;
  List<PreprocessedImage> results = List.of(); // Empty at first
  int thumbnailSize = 32;
  String[] allowedExtensions = new String[] {"*.png", "*.jpg", "*.jpeg"};
  private Callback<ListView<PreprocessedImage>, ListCell<PreprocessedImage>> listCellFactoryBasic;
  private Callback<ListView<PreprocessedImage>, ListCell<PreprocessedImage>> listCellFactoryFile;

  public FrontendSceneBuilder(BackendInterface back) {
    // Initialize backend
    this.back = back;

    initializeCellFactories();
  }

  private void initializeCellFactories() {
    // Initialize first cell factory - the Callback is what creates the custom ListCells.
    // The same one is used for ComboBoxes and the results list.
    listCellFactoryBasic = listView -> {
      // Callback's purpose is to construct a single pretty ListCell
      ListCell<PreprocessedImage> cell = new ListCell<PreprocessedImage>() {
        // ListCell has a ImageView used for a little thumbnail of the image
        private final ImageView imageView = new ImageView();

        @Override
        public void updateItem(PreprocessedImage image, boolean empty) {
          super.updateItem(image, empty);
          if (empty || image == null) {
            setGraphic(null);
            setText(null);
          } else {
            // Set up the thumbnail image
            imageView.setImage(new Image(image.getImagePath(), (double) thumbnailSize,
                (double) thumbnailSize, false, false));
            // Update the cell
            // TODO does this work to get the name? lol
            setText(image.getImagePath().substring(image.getImagePath().lastIndexOf("/")));
            setGraphic(imageView);
          }
        }
      };
      return cell;
    };

    listCellFactoryFile = listCellFactoryBasic; // TODO
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
    ComboBox<PreprocessedImage> startCB = buildFormattedComboBox();

    // Label for the ending image
    Label endLabel = buildFormattedLabel("Ending Image", 2);

    // Combobox for choosing the ending image
    ComboBox<PreprocessedImage> endCB = buildFormattedComboBox();

    // Button for generating results
    String generateText = "Generate Image Path";
    Button generateButton = new Button("Generate Image Path");

    // Label for the results list
    Label resultsLabel = buildFormattedLabel("Results", 1);

    // ScrollPane or ListView of results
    Node resultsList = buildResultsList();

    // Add everything into a nice VBox.
    VBox base = new VBox();
    base.getChildren().addAll(title, startLabel, startCB, endLabel, endCB, resultsLabel,
        resultsList);
    // base.getChildren().addAll(title, startLabel, startCB, endLabel, endCB, resultsLabel);
    return base;
  }

  @Override
  public Pane buildFileManagementControls() {
    // Functionality title at the top
    Label title = buildFormattedLabel("Manage Loaded Images", 0);

    // Initialize management buttons
    Button addButton = new Button("Add Images");
    Button folderButton = new Button("Add Folder");
    Button removeButton = new Button("Remove");
    Button toggleButton = new Button("Toggle Disabled/Enabled");
    Button enableAllButton = new Button("Enable All Images");
    Button disableAllButton = new Button("Disable All Images");

    // TODO button action handlers
    // Base image adding button
    addButton.setOnAction((actionEvent) -> {
      FileChooser chooser = new FileChooser();
      // Set extension filter
      chooser.setSelectedExtensionFilter(
          new ExtensionFilter("Image files", List.of(allowedExtensions)));
      // Try to add all the files we got
      for(File f : chooser.showOpenMultipleDialog(null)) {
        try {
          back.receiveFile(f);
        } catch (IOException e) {
          System.out.println("Exception on filechooser: " + e);
          // TODO display error
          e.printStackTrace();
        }
      }
    });

    // Arrange buttons
    GridPane gp = new GridPane(2, 3);
    gp.add(addButton, 0, 0);
    gp.add(folderButton, 1, 0);
    gp.add(removeButton, 0, 1);
    gp.add(enableAllButton, 1, 1);
    gp.add(toggleButton, 0, 2);
    gp.add(disableAllButton, 1, 2);

    // Arrange all elements into pane
    VBox base = new VBox();
    base.getChildren().addAll(title, gp, buildLoadedImagesList());
    
    return base; // TODO
  }

  /**
   * Creates the controls that display the items / images found in a gradient / shortest path.
   */
  public Node buildResultsList() {
    ObservableList<PreprocessedImage> items = FXCollections.observableList(results);
    ListView<PreprocessedImage> listView = new ListView<PreprocessedImage>(items);
    listView.setCellFactory(listCellFactoryBasic);
    listView.setOrientation(Orientation.VERTICAL);

    return listView; // TODO editing and disabling stuff
  }

  /**
   * Creates the controls that display the items / images that are loaded in the images list.
   */
  public Node buildLoadedImagesList() {
    ObservableList<PreprocessedImage> items = FXCollections.observableList(back.getAllImages());
    ListView<PreprocessedImage> listView = new ListView<PreprocessedImage>(items);
    listView.setCellFactory(listCellFactoryFile);
    listView.setOrientation(Orientation.VERTICAL);

    return listView; // TODO testing
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

  /**
   * Creates and returns a ComboBox object with custom ListCells that each have a thumbnail and the
   * name of their image.
   * 
   * @return a ComboBox of PreprocessedImages
   */
  private ComboBox<PreprocessedImage> buildFormattedComboBox() {
    // Initialize the combobox
    ComboBox<PreprocessedImage> comboBox = new ComboBox<PreprocessedImage>();
    // Populate with all images
    comboBox.getItems().addAll(back.getAllImages());

    // Use cell factory to get a custom cell display for each cell in the list
    comboBox.setCellFactory(listCellFactoryBasic);

    // Use the same custom display for the ComboBox button
    // It's ok to call with null currently because listView is never used, but just in case: FIXME
    comboBox.setButtonCell(listCellFactoryBasic.call(null));

    return comboBox;
  }
}
