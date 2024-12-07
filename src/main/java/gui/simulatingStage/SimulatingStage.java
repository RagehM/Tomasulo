package gui.simulatingStage;

import gui.setupStage.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import units.addressUnit.LoadUnit;
import units.addressUnit.StoreUnit;
import units.instructionUnit;

public class SimulatingStage {
  public static Stage setupSimulatingStage(instructionUnit instUnit, LoadUnit loadUnit, StoreUnit storeUnit) {
    Stage stage = new Stage();
    BorderPane borderPane = new BorderPane();
    Scene scene = new Scene(borderPane, 1500, 600);


    VBox instructionBox = InstructionTable.createTable(instUnit);
    instructionBox.setAlignment(Pos.TOP_LEFT);
    instructionBox.setPrefWidth(600);
    instructionBox.setPadding(new Insets(10, 10, 10, 10));

    VBox loadBox = AddressTable.createTable(loadUnit, BufferSetup.getLoadBuffer());
    loadBox.setAlignment(Pos.BOTTOM_LEFT);
    loadBox.setPrefWidth(400);
    loadBox.setPadding(new Insets(10, 10, 10, 10));

    VBox storeBox = AddressTable.createTable(storeUnit, BufferSetup.getStoreBuffer());
    storeBox.setAlignment(Pos.BOTTOM_LEFT);
    storeBox.setPrefWidth(400);
    storeBox.setPadding(new Insets(10, 10, 10, 10));

    VBox leftPane = new VBox(10, instructionBox, loadBox, storeBox);
    leftPane.setPadding(new Insets(10));

    borderPane.setLeft(leftPane);

    String cssFile = SetupStage.class.getResource("/style.css").toExternalForm();
    scene.getStylesheets().add(cssFile);

    stage.setScene(scene);
    return stage;
  }
}
