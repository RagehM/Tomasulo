package gui.simulatingStage;

import instructions.Instruction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import units.RegisterUnit;
import units.stage.Stage;

public class RegisterTable {
  public static VBox createTable(RegisterUnit unit ) {
    TableView<RegisterUnit> tableView = new TableView<>();

    TableColumn<RegisterUnit, String> operationCol = new TableColumn<>("register");
    operationCol.setCellValueFactory(new PropertyValueFactory<>("register"));

    TableColumn<RegisterUnit, Stage> destinationCol = new TableColumn<>("Qi");
    destinationCol.setCellValueFactory(new PropertyValueFactory<>("Qi"));

    TableColumn<RegisterUnit, String> operand1Col = new TableColumn<>("content");
    operand1Col.setCellValueFactory(new PropertyValueFactory<>("content"));

    tableView.getColumns().addAll(operationCol, destinationCol, operand1Col);

    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    ObservableList<RegisterUnit> data = FXCollections.observableArrayList();
    for (int i = 0; i < RegisterUnit.registerTable.size(); i++) {
      data.add((RegisterUnit) RegisterUnit.registerTable.get(i));
    }

    tableView.setItems(data);

    tableView.setFixedCellSize(25);
    tableView.setPrefHeight(25 * data.size() + 30);

    tableView.setPrefWidth(Double.MAX_VALUE);

    VBox vbox = new VBox(tableView);
    vbox.setFillWidth(true);

    return vbox;
  }
}
