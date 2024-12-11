package gui.simulatingStage;

import java.util.ArrayList;

import gui.setupStage.AddressSetup;
import gui.setupStage.AluSetup;
import gui.setupStage.SetupStage;
import instructions.Instruction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import units.stage.addressStage.LoadStage;
import units.stage.addressStage.StoreStage;
import units.stage.aluStage.FloatingAdderStage;
import units.stage.aluStage.FloatingMultiplyStage;
import units.stage.aluStage.IntegerStage;

public class SimulatingStage {
  public static Stage setupSimulatingStage(ArrayList<Instruction> instructionTable, ArrayList<LoadStage> loadTable, ArrayList<StoreStage> storeTable, ArrayList<FloatingAdderStage> floatingAdderTable, ArrayList<FloatingMultiplyStage> floatingMultiplyTable, ArrayList<IntegerStage> integerTable) {
    Stage stage = new Stage();
    BorderPane borderPane = new BorderPane();
    Scene scene = new Scene(borderPane, 1500, 600);

    VBox instructionBox = InstructionTable.createTable(instructionTable);
    instructionBox.setAlignment(Pos.TOP_LEFT);
    instructionBox.setPrefWidth(600);
    instructionBox.setPadding(new Insets(10, 10, 10, 10));

    VBox loadBox = AddressTable.createTable(loadTable, AddressSetup.getLoadSize(), "load");
    loadBox.setAlignment(Pos.CENTER_LEFT);
    loadBox.setPrefWidth(400);
    loadBox.setPadding(new Insets(10, 10, 10, 10));

    VBox storeBox = AddressTable.createTable(storeTable, AddressSetup.getStoreSize(), "store");
    storeBox.setAlignment(Pos.BOTTOM_LEFT);
    storeBox.setPrefWidth(400);
    storeBox.setPadding(new Insets(10, 10, 10, 10));

    VBox floatRegisterBox = FloatRegisterTable.createTable();
    floatRegisterBox.setAlignment(Pos.TOP_RIGHT);
    floatRegisterBox.setPrefWidth(400);
    floatRegisterBox.setPadding(new Insets(10, 10, 10, 10));

    VBox integerRegisterBox = IntegerRegisterTable.createTable();
    integerRegisterBox.setAlignment(Pos.TOP_RIGHT);
    integerRegisterBox.setPrefWidth(400);
    integerRegisterBox.setPadding(new Insets(10, 10, 10, 10));

    VBox FloatingAdderBox = AluFloatTable.createTable(floatingAdderTable, AluSetup.getFloatingAdder(), "adder");
    FloatingAdderBox.setAlignment(Pos.CENTER_RIGHT);
    FloatingAdderBox.setPrefWidth(400);
    FloatingAdderBox.setPadding(new Insets(10, 10, 10, 10));

    VBox FloatingMultiplyBox = AluFloatTable.createTable(floatingMultiplyTable, AluSetup.getFloatingMul(), "multiply");
    FloatingMultiplyBox.setAlignment(Pos.BOTTOM_RIGHT);
    FloatingMultiplyBox.setPrefWidth(400);
    FloatingMultiplyBox.setPadding(new Insets(10, 10, 10, 10));

    VBox IntegerAdderBox = AluIntegerTable.createIntegerTable(integerTable, AluSetup.getIntegerAdder());
    IntegerAdderBox.setAlignment(Pos.BOTTOM_RIGHT);
    IntegerAdderBox.setPrefWidth(400);
    IntegerAdderBox.setPadding(new Insets(10, 10, 10, 10));

    Button nextCycle = new Button("next Cycle ->");

    nextCycle.setOnAction(e -> {
      Simulate.Simulate();
      AddressTable.loadTableView.refresh();
      AddressTable.storeTableView.refresh();
      AluFloatTable.adderTableView.refresh();
      AluFloatTable.multiplyTableView.refresh();
      InstructionTable.instructionTableView.refresh();
    });

//    Button prevCycle = new Button("<- Prev Cycle"); // eh2 eh2 eh2

    HBox topRightPane = new HBox(10,floatRegisterBox, integerRegisterBox);
    topRightPane.setAlignment(Pos.TOP_RIGHT);
    topRightPane.setPadding(new Insets(10));

    VBox leftPane = new VBox(10, instructionBox, loadBox, storeBox);
    leftPane.setPadding(new Insets(10));

    VBox rightPane = new VBox(10, topRightPane, FloatingAdderBox, FloatingMultiplyBox,IntegerAdderBox);
    rightPane.setPadding(new Insets(10));

    HBox bottomPane = new HBox(10, nextCycle);
    bottomPane.setAlignment(Pos.BOTTOM_CENTER);
    bottomPane.setPadding(new Insets(10));

    borderPane.setLeft(leftPane);
    borderPane.setRight(rightPane);
    borderPane.setBottom(bottomPane);

    String cssFile = SetupStage.class.getResource("/style.css").toExternalForm();
    scene.getStylesheets().add(cssFile);

    stage.setScene(scene);
    return stage;
  }
}
