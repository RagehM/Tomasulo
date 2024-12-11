package gui.simulatingStage;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import units.stage.aluStage.IntegerStage;

public class AluIntegerTable {
  public static TableView<IntegerStage> integerAdderTableView=new TableView<>();
  public static VBox createIntegerTable(ArrayList<IntegerStage> table, int size){

    TableColumn<IntegerStage, String> stageCol = new TableColumn<>("Stage");
    stageCol.setCellValueFactory(new PropertyValueFactory<>("stage"));

    TableColumn<IntegerStage, String> busyCol = new TableColumn<>("Busy");
    busyCol.setCellValueFactory(new PropertyValueFactory<>("busy"));

    TableColumn<IntegerStage, String> opCol = new TableColumn<>("Op");
    opCol.setCellValueFactory(new PropertyValueFactory<>("op"));

    TableColumn<IntegerStage, String> VjCol = new TableColumn<>("Vj");
    VjCol.setCellValueFactory(new PropertyValueFactory<>("Vj"));

    TableColumn<IntegerStage, String> QjCol = new TableColumn<>("Qj");
    QjCol.setCellValueFactory(new PropertyValueFactory<>("Qj"));

    TableColumn<IntegerStage, String> immediate = new TableColumn<>("Immediate");
    immediate.setCellValueFactory(new PropertyValueFactory<>("immediate"));


    integerAdderTableView.getColumns().addAll(stageCol, busyCol, opCol, VjCol, QjCol, immediate);

    integerAdderTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    ObservableList<IntegerStage> data = FXCollections.observableArrayList();
    System.out.println(table.toString());
    for (int i = 0; i < table.size(); i++) {
      data.add( (IntegerStage) table.get(i));
    }

    integerAdderTableView.setItems(data);

    integerAdderTableView.setFixedCellSize(25);
    integerAdderTableView.setPrefHeight(25 * 5 + 30);

    integerAdderTableView.setPrefWidth(Double.MAX_VALUE);

    VBox vbox = new VBox(integerAdderTableView);
    vbox.setFillWidth(true);

    return vbox;
}
}