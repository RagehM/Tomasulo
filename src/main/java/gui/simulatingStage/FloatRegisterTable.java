package gui.simulatingStage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import units.FloatRegister;

public class FloatRegisterTable {
  public static VBox createTable() {
    TableView<FloatRegister> tableView = new TableView<>();

    TableColumn<FloatRegister, String> operationCol = new TableColumn<>("register");
    operationCol.setCellValueFactory(new PropertyValueFactory<>("register"));

    TableColumn<FloatRegister, String> destinationCol = new TableColumn<>("Qi");
    destinationCol.setCellValueFactory(new PropertyValueFactory<>("Qi"));

    TableColumn<FloatRegister, String> operand1Col = new TableColumn<>("content");
    operand1Col.setCellValueFactory(new PropertyValueFactory<>("content"));

    tableView.getColumns().addAll(operationCol, destinationCol, operand1Col);

    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    ObservableList<FloatRegister> data = FXCollections.observableArrayList();
    for (int i = 0; i < FloatRegister.floatRegisterTable.size(); i++) {
      data.add((FloatRegister) FloatRegister.floatRegisterTable.get(i));
    }

    tableView.setItems(data);

    tableView.setFixedCellSize(25);
    tableView.setPrefHeight(25 * 5 + 30);

    tableView.setPrefWidth(Double.MAX_VALUE);

    VBox vbox = new VBox(tableView);
    vbox.setFillWidth(true);

    return vbox;
  }
}
