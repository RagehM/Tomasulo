package gui.setupStage;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CacheSetup {
  private static TextField latencyField = new TextField();
  private static TextField missPenaltyField = new TextField();
  private static TextField cacheSizeField = new TextField();
  private static TextField blockSizeField = new TextField();

  public static int getLatency() {
    return Integer.parseInt(latencyField.getText());
  }

  public static int getMissPenalty() {
    return Integer.parseInt(missPenaltyField.getText());
  }

  public static int getCacheSize() {
    return Integer.parseInt(cacheSizeField.getText());
  }

  public static int getBlockSize() {
    return Integer.parseInt(blockSizeField.getText());
  }

public static VBox setup() {
    VBox vbox = new VBox();
    vbox.setSpacing(10);
    vbox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
    vbox.getStyleClass().add("vbox-container");

    GridPane gridPane = new GridPane();
    gridPane.setHgap(5);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(20));
    gridPane.getStyleClass().add("vbox-container");

    Label titleLabel = new Label("Cache Configuration");
    titleLabel.getStyleClass().add("label-title");

    Label latencyLabel = new Label("Latency");
    latencyField.setPromptText("Hit Latency in cycles");

    Label missPenaltyLabel = new Label("Miss Penalty");
    missPenaltyField.setPromptText("Miss Penalty in cycles");

    Label cacheSizeLabel = new Label("Cache Size");
    cacheSizeField.setPromptText("Cache Size");

    Label blockSizeLabel = new Label("Block Size");
    blockSizeField.setPromptText("Block Size");

    gridPane.add(latencyLabel, 0, 0);
    gridPane.add(latencyField, 1, 0);

    gridPane.add(missPenaltyLabel, 0, 1);
    gridPane.add(missPenaltyField, 1, 1);

    gridPane.add(cacheSizeLabel, 0, 2);
    gridPane.add(cacheSizeField, 1, 2);

    gridPane.add(blockSizeLabel, 0, 3);
    gridPane.add(blockSizeField, 1, 3);

    vbox.getChildren().addAll(titleLabel, gridPane);

    return vbox;
}

}
