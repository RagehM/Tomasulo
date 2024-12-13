package gui.simulatingStage;

import java.util.ArrayList;

import instructions.Instruction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class InstructionTable {
  public static TableView<Instruction> instructionTableView = new TableView<>();
  public static VBox createTable(ArrayList<Instruction> unit ) {

    TableColumn<Instruction, String> operationCol = new TableColumn<>("operation");
    operationCol.setCellValueFactory(new PropertyValueFactory<>("operation"));

    TableColumn<Instruction, String> destinationCol = new TableColumn<>("destination");
    destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));

    TableColumn<Instruction, Integer> operand1Col = new TableColumn<>("operand1");
    operand1Col.setCellValueFactory(new PropertyValueFactory<>("operand1"));

    TableColumn<Instruction, Integer> operand2Col = new TableColumn<>("operand2");
    operand2Col.setCellValueFactory(new PropertyValueFactory<>("operand2"));

    TableColumn<Instruction, Integer> issueCol = new TableColumn<>("issue");
    issueCol.setCellValueFactory(new PropertyValueFactory<>("issue"));

    TableColumn<Instruction, Integer> executionCompleteCol = new TableColumn<>("executionComplete");
    executionCompleteCol.setCellValueFactory(new PropertyValueFactory<>("executionComplete"));

    TableColumn<Instruction, Integer> writeResultCol = new TableColumn<>("writeResult");
    writeResultCol.setCellValueFactory(new PropertyValueFactory<>("writeResult"));

    instructionTableView.getColumns().addAll(operationCol, destinationCol, operand1Col, operand2Col, issueCol, executionCompleteCol, writeResultCol);

    instructionTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    ObservableList<Instruction> data = FXCollections.observableArrayList();
    for (int i = 0; i < unit.size(); i++) {
      data.add((Instruction) unit.get(i));
    }

    instructionTableView.setItems(data);

    instructionTableView.setFixedCellSize(25);
    instructionTableView.setPrefHeight(25 * 7 + 30);

    instructionTableView.setPrefWidth(Double.MAX_VALUE);

    VBox vbox = new VBox(instructionTableView);
    vbox.setFillWidth(true);

    return vbox;
  }
}
