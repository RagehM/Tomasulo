package gui.setupStage;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class InstructionSetup {

  private static TextField integerLatency = new TextField();
  private static TextField floatingLatency = new TextField();
  private static TextField memoryLatency = new TextField();

  public static int getIntegerLatency() {
    return Integer.parseInt(integerLatency.getText());
  }

  public static int getFloatingLatency() {
    return Integer.parseInt(floatingLatency.getText());
  }

  public static int getMemoryLatency() {
    return Integer.parseInt(memoryLatency.getText());
  }

  public static HBox setup() {
    HBox hbox = new HBox();
    hbox.setSpacing(10);
    hbox.setAlignment(javafx.geometry.Pos.CENTER);
    hbox.getStyleClass().add("hbox-container");

    GridPane gridPane = new GridPane();
    gridPane.setHgap(5);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(20));
    gridPane.getStyleClass().add("vbox-container");


    Label integerLabel = new Label("Integer Latency:");
    integerLatency.setPromptText("Enter latency (cycles)");

    Label floatingLabel = new Label("Floating-Point Latency:");
    floatingLatency.setPromptText("Enter latency (cycles)");

    Label memoryLabel = new Label("Memory Latency:");
    memoryLatency.setPromptText("Enter latency (cycles)");


    gridPane.add(integerLabel, 0, 0);
    gridPane.add(integerLatency, 1, 0);

    gridPane.add(floatingLabel, 0, 1);
    gridPane.add(floatingLatency, 1, 1);

    gridPane.add(memoryLabel, 0, 2);
    gridPane.add(memoryLatency, 1, 2);

    hbox.getChildren().add(gridPane);

    return hbox;
  }
}
