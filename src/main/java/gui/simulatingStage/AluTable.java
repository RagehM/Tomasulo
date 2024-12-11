package gui.simulatingStage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import units.stage.aluStage.AluStage;
import units.stage.aluStage.FloatingAdderStage;
import units.stage.aluStage.FloatingMultiplyStage;

import java.util.ArrayList;

public class AluTable {
  public static TableView<FloatingAdderStage> adderTableView = new TableView<>();
  public static TableView<FloatingMultiplyStage> multiplyTableView = new TableView<>();
  public static VBox createTable(ArrayList<? extends AluStage> table , int size, String type) {
    if(type == "adder") {
      TableColumn<FloatingAdderStage, String> stageCol = new TableColumn<>("Stage");
      stageCol.setCellValueFactory(new PropertyValueFactory<>("stage"));

      TableColumn<FloatingAdderStage, String> busyCol = new TableColumn<>("Busy");
      busyCol.setCellValueFactory(new PropertyValueFactory<>("busy"));

      TableColumn<FloatingAdderStage, String> opCol = new TableColumn<>("Op");
      opCol.setCellValueFactory(new PropertyValueFactory<>("op"));

      TableColumn<FloatingAdderStage, String> VjCol = new TableColumn<>("Vj");
      VjCol.setCellValueFactory(new PropertyValueFactory<>("Vj"));

      TableColumn<FloatingAdderStage, String> VkCol = new TableColumn<>("Vk");
      VkCol.setCellValueFactory(new PropertyValueFactory<>("Vk"));

      TableColumn<FloatingAdderStage, String> QjCol = new TableColumn<>("Qj");
      QjCol.setCellValueFactory(new PropertyValueFactory<>("Qj"));

      TableColumn<FloatingAdderStage, String> QkCol = new TableColumn<>("Qk");
      QkCol.setCellValueFactory(new PropertyValueFactory<>("Qk"));

      adderTableView.getColumns().addAll(stageCol, busyCol, opCol, VjCol, VkCol, QjCol, QkCol);

      adderTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

      ObservableList<FloatingAdderStage> data = FXCollections.observableArrayList();
      System.out.println(table.toString());
      for (int i = 0; i < table.size(); i++) {
        data.add( (FloatingAdderStage) table.get(i));
      }

      adderTableView.setItems(data);

      adderTableView.setFixedCellSize(25);
      adderTableView.setPrefHeight(25 * 5 + 30);

      adderTableView.setPrefWidth(Double.MAX_VALUE);

      VBox vbox = new VBox(adderTableView);
      vbox.setFillWidth(true);

      return vbox;
    }
    else {
      TableColumn<FloatingMultiplyStage, String> stageCol = new TableColumn<>("Stage");
      stageCol.setCellValueFactory(new PropertyValueFactory<>("stage"));

      TableColumn<FloatingMultiplyStage, String> busyCol = new TableColumn<>("Busy");
      busyCol.setCellValueFactory(new PropertyValueFactory<>("busy"));

      TableColumn<FloatingMultiplyStage, String> opCol = new TableColumn<>("Op");
      opCol.setCellValueFactory(new PropertyValueFactory<>("op"));

      TableColumn<FloatingMultiplyStage, String> VjCol = new TableColumn<>("Vj");
      VjCol.setCellValueFactory(new PropertyValueFactory<>("Vj"));

      TableColumn<FloatingMultiplyStage, String> VkCol = new TableColumn<>("Vk");
      VkCol.setCellValueFactory(new PropertyValueFactory<>("Vk"));

      TableColumn<FloatingMultiplyStage, String> QjCol = new TableColumn<>("Qj");
      QjCol.setCellValueFactory(new PropertyValueFactory<>("Qj"));

      TableColumn<FloatingMultiplyStage, String> QkCol = new TableColumn<>("Qk");
      QkCol.setCellValueFactory(new PropertyValueFactory<>("Qk"));

      multiplyTableView.getColumns().addAll(stageCol, busyCol, opCol, VjCol, VkCol, QjCol, QkCol);

      multiplyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

      ObservableList<FloatingMultiplyStage> data = FXCollections.observableArrayList();
      for (int i = 0; i < table.size(); i++) {
        data.add( (FloatingMultiplyStage) table.get(i));
      }

      multiplyTableView.setItems(data);

      multiplyTableView.setFixedCellSize(25);
      multiplyTableView.setPrefHeight(25 * 5 + 30);

      multiplyTableView.setPrefWidth(Double.MAX_VALUE);

      VBox vbox = new VBox(multiplyTableView);
      vbox.setFillWidth(true);

      return vbox;
    }
  }
}
