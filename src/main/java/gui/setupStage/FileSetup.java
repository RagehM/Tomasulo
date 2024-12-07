package gui.setupStage;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import units.instructionUnit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileSetup {
  public static HBox setupFile(Stage stage) {
    HBox box = new HBox(10);
    box.setSpacing(10);
    box.setAlignment(javafx.geometry.Pos.CENTER);
    box.getStyleClass().add("hbox-container");

    Label statusText = new Label("No file loaded.");
    Button uploadButton = new Button("Load File");

    uploadButton.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Choose a File");
      File selectedFile = fileChooser.showOpenDialog(stage);

      if (selectedFile != null) {
        try {
          File destinationFile = new File("./src/main/java/" + selectedFile.getName());
          Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

          statusText.setText("File Loaded Successfully");
          uploadButton.setDisable(true);

        } catch (IOException e) {
          statusText.setText("Error loading file: " + e.getMessage());
        }
      } else {
        statusText.setText("No file selected.");
      }
    });
    box.getChildren().addAll(uploadButton, statusText);
    return box;
  }
}
