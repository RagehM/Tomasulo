package gui.setupStage;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class BufferSetup {
  private static TextField loadBuffer = new TextField();
  private static TextField storeBuffer = new TextField();

  public static int getLoadBuffer() {
    return Integer.parseInt(loadBuffer.getText());
  }

  public static int getStoreBuffer() {
    return Integer.parseInt(storeBuffer.getText());
  }

  public static HBox setupBuffer(){
    HBox hbox = new HBox();
    hbox.setSpacing(10);
    hbox.setAlignment(javafx.geometry.Pos.CENTER);
    hbox.getStyleClass().add("hbox-container");

    GridPane gridPane = new GridPane();
    gridPane.setHgap(5);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(20));
    gridPane.getStyleClass().add("vbox-container");


    Label loadBufferLabel = new Label("load Buffer Size");
    loadBuffer.setPromptText("Enter size");

    Label storeBufferLabel = new Label("Store Buffer Size");
    storeBuffer.setPromptText("Enter size");


    gridPane.add(loadBufferLabel, 0, 0);
    gridPane.add(loadBuffer, 1, 0);

    gridPane.add(storeBufferLabel, 0, 1);
    gridPane.add(storeBuffer, 1, 1);

    hbox.getChildren().add(gridPane);

    return hbox;
  }

}
