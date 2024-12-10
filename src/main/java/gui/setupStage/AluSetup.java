package gui.setupStage;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class AluSetup {
  private static TextField floatingAdder = new TextField();
  private static TextField floatingMul = new TextField();
  private static TextField integerAdder = new TextField();

  public static int getFloatingAdder() {
    return Integer.parseInt(floatingAdder.getText());
  }

  public static int getFloatingMul() {
    return Integer.parseInt(floatingMul.getText());
  }

  public static int getIntegerAdder() {
    return Integer.parseInt(integerAdder.getText());
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


    Label floatingAdderLabel = new Label("Floating Adder");
    floatingAdder.setPromptText("Enter size");

    Label floatingMulLabel = new Label("Floating multiplier");
    floatingMul.setPromptText("Enter size");

    Label integerAdderLabel = new Label("Integer Adder");
    integerAdder.setPromptText("Enter size");


    gridPane.add(floatingAdderLabel, 0, 0);
    gridPane.add(floatingAdder, 1, 0);

    gridPane.add(floatingMulLabel, 0, 1);
    gridPane.add(floatingMul, 1, 1);

    gridPane.add(integerAdderLabel, 0, 2);
    gridPane.add(integerAdder, 1, 2);

    hbox.getChildren().add(gridPane);

    return hbox;
  }
}
