package gui.setupStage;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class AddressSetup {
  private static TextField loadSize = new TextField();
  private static TextField storeSize = new TextField();

  public static int getLoadSize() {
    return Integer.parseInt(loadSize.getText());
  }

  public static int getStoreSize() {
    return Integer.parseInt(storeSize.getText());
  }

  public static HBox setup(){
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
    loadSize.setPromptText("Enter size");

    Label storeBufferLabel = new Label("Store Buffer Size");
    storeSize.setPromptText("Enter size");


    gridPane.add(loadBufferLabel, 0, 0);
    gridPane.add(loadSize, 1, 0);

    gridPane.add(storeBufferLabel, 0, 1);
    gridPane.add(storeSize, 1, 1);

    hbox.getChildren().add(gridPane);

    return hbox;
  }

}
