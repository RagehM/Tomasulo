package gui.simulatingStage;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import units.stage.aluStage.BranchStage;

public class BranchTable {
  // stage op vj qj vk qk address
    public static TableView<BranchStage> branchTableView = new TableView<>();
  public static VBox createBranchTable(ArrayList<BranchStage> table, int size){

    TableColumn<BranchStage, String> stageCol = new TableColumn<>("Stage");
    stageCol.setCellValueFactory(new PropertyValueFactory<>("stage"));

    TableColumn<BranchStage, String> busyCol = new TableColumn<>("Busy");
    busyCol.setCellValueFactory(new PropertyValueFactory<>("busy"));

    TableColumn<BranchStage, String> opCol = new TableColumn<>("Op");
    opCol.setCellValueFactory(new PropertyValueFactory<>("op"));

    TableColumn<BranchStage, String> VjCol = new TableColumn<>("Vj");
    VjCol.setCellValueFactory(new PropertyValueFactory<>("Vj"));

    TableColumn<BranchStage, String> QjCol = new TableColumn<>("Qj");
    QjCol.setCellValueFactory(new PropertyValueFactory<>("Qj"));

    TableColumn<BranchStage, String> VkCol = new TableColumn<>("VK");
    VkCol.setCellValueFactory(new PropertyValueFactory<>("Vk"));

    TableColumn<BranchStage, String> QkCol = new TableColumn<>("Qk");
    QkCol.setCellValueFactory(new PropertyValueFactory<>("Qk"));

    TableColumn<BranchStage, String> address = new TableColumn<>("Address");
    address.setCellValueFactory(new PropertyValueFactory<>("address"));


    branchTableView.getColumns().addAll(stageCol, busyCol, opCol, VjCol, QjCol, VkCol, QkCol, address);

    branchTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    ObservableList<BranchStage> data = FXCollections.observableArrayList();
    for (int i = 0; i < table.size(); i++) {
      data.add((BranchStage) table.get(i));
    }

    branchTableView.setItems(data);

    branchTableView.setFixedCellSize(25);
    branchTableView.setPrefHeight(25 * 5 + 30);

    branchTableView.setPrefWidth(Double.MAX_VALUE);

    VBox vbox = new VBox(branchTableView);
    vbox.setFillWidth(true);

    return vbox;
}
}
