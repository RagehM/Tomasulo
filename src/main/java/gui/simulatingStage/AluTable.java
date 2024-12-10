package gui.simulatingStage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import units.stage.aluStage.AluStage;

import java.util.ArrayList;

public class AluTable {
  public static VBox createTable(ArrayList<? extends AluStage> table , int size) {
    TableView<AluStage> tableView = new TableView<>();

    TableColumn<AluStage, String> stageCol = new TableColumn<>("Stage");
    stageCol.setCellValueFactory(new PropertyValueFactory<>("stage"));

    TableColumn<AluStage, String> busyCol = new TableColumn<>("Busy");
    busyCol.setCellValueFactory(new PropertyValueFactory<>("busy"));

    TableColumn<AluStage, String> opCol = new TableColumn<>("Op");
    opCol.setCellValueFactory(new PropertyValueFactory<>("op"));

    TableColumn<AluStage, String> VjCol = new TableColumn<>("Vj");
    VjCol.setCellValueFactory(new PropertyValueFactory<>("Vj"));

    TableColumn<AluStage, String> VkCol = new TableColumn<>("Vk");
    VkCol.setCellValueFactory(new PropertyValueFactory<>("Vk"));

    TableColumn<AluStage, String> QjCol = new TableColumn<>("Qj");
    QjCol.setCellValueFactory(new PropertyValueFactory<>("Qj"));

    TableColumn<AluStage, String> QkCol = new TableColumn<>("Qk");
    QkCol.setCellValueFactory(new PropertyValueFactory<>("Qk"));

    tableView.getColumns().addAll(stageCol, busyCol, opCol, VjCol, VkCol, QjCol, QkCol);

    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    ObservableList<AluStage> data = FXCollections.observableArrayList();
    for (int i = 0; i < table.size(); i++) {
      data.add( (AluStage) table.get(i));
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
