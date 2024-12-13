package gui.setupStage;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class InstructionSetup {

  private static TextField integerLatency = new TextField();
  private static TextField floatingLatency = new TextField();


  public static int getIntegerLatency() {
    return Integer.parseInt(integerLatency.getText());
  }

  public static int getFloatingLatency() {
    return Integer.parseInt(floatingLatency.getText());
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

    Label titleLabel = new Label("Instructions Latency");
    titleLabel.getStyleClass().add("label-title");

    Label integerLabel = new Label("Integer Latency:");
    integerLatency.setPromptText("Enter latency (cycles)");

    Label floatingLabel = new Label("Floating-Point Latency:");
    floatingLatency.setPromptText("Enter latency (cycles)");



    gridPane.add(integerLabel, 0, 0);
    gridPane.add(integerLatency, 1, 0);

    gridPane.add(floatingLabel, 0, 1);
    gridPane.add(floatingLatency, 1, 1);


    vbox.getChildren().addAll(titleLabel, gridPane);

    return vbox;
  }
}
