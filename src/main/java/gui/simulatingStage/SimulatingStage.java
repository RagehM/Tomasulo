package gui.simulatingStage;

import gui.setupStage.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import units.instructionUnit;
import units.stage.addressStage.LoadStage;
import units.stage.addressStage.StoreStage;
import units.stage.aluStage.FloatingAdderStage;
import units.stage.aluStage.FloatingMultiplyStage;

import java.util.ArrayList;

public class SimulatingStage {
  public static Stage setupSimulatingStage(instructionUnit instUnit, ArrayList<LoadStage> loadTable, ArrayList<StoreStage> storeTable, ArrayList<FloatingAdderStage> floatingAdderTable, ArrayList<FloatingMultiplyStage> floatingMultiplyTable) {
    Stage stage = new Stage();
    BorderPane borderPane = new BorderPane();
    Scene scene = new Scene(borderPane, 1500, 600);


    VBox instructionBox = InstructionTable.createTable(instUnit);
    instructionBox.setAlignment(Pos.TOP_LEFT);
    instructionBox.setPrefWidth(600);
    instructionBox.setPadding(new Insets(10, 10, 10, 10));

    VBox loadBox = AddressTable.createTable(loadTable, AddressSetup.getLoadSize());
    loadBox.setAlignment(Pos.CENTER_LEFT);
    loadBox.setPrefWidth(400);
    loadBox.setPadding(new Insets(10, 10, 10, 10));

    VBox storeBox = AddressTable.createTable(storeTable, AddressSetup.getStoreSize());
    storeBox.setAlignment(Pos.BOTTOM_LEFT);
    storeBox.setPrefWidth(400);
    storeBox.setPadding(new Insets(10, 10, 10, 10));

    VBox registerBox = RegisterTable.createTable();
    registerBox.setAlignment(Pos.TOP_RIGHT);
    registerBox.setPrefWidth(400);
    registerBox.setPadding(new Insets(10, 10, 10, 10));

    VBox FloatingAdderBox = AluTable.createTable(floatingAdderTable, AluSetup.getFloatingAdder());
    FloatingAdderBox.setAlignment(Pos.CENTER_RIGHT);
    FloatingAdderBox.setPrefWidth(400);
    FloatingAdderBox.setPadding(new Insets(10, 10, 10, 10));

    VBox FloatingMultiplyBox = AluTable.createTable(floatingMultiplyTable, AluSetup.getFloatingMul());
    FloatingMultiplyBox.setAlignment(Pos.BOTTOM_RIGHT);
    FloatingMultiplyBox.setPrefWidth(400);
    FloatingMultiplyBox.setPadding(new Insets(10, 10, 10, 10));


    VBox leftPane = new VBox(10, instructionBox, loadBox, storeBox);
    leftPane.setPadding(new Insets(10));

    VBox rightPane = new VBox(10, registerBox, FloatingAdderBox, FloatingMultiplyBox);
    rightPane.setPadding(new Insets(10));

    borderPane.setLeft(leftPane);
    borderPane.setRight(rightPane);

    String cssFile = SetupStage.class.getResource("/style.css").toExternalForm();
    scene.getStylesheets().add(cssFile);

    stage.setScene(scene);
    return stage;
  }
}
