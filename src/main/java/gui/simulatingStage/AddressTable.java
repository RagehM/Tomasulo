package gui.simulatingStage;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import units.stage.addressStage.AddressStage;
import units.stage.addressStage.LoadStage;
import units.stage.addressStage.StoreStage;

import java.util.ArrayList;

public class AddressTable {
    public static TableView<LoadStage> loadTableView = new TableView<>();
    public static TableView<StoreStage> storeTableView = new TableView<>();
    public static VBox createTable(ArrayList<? extends AddressStage> table , int size, String type) {
        if(type == "load") {

            TableColumn<LoadStage, String> stageCol = new TableColumn<>("Stage");
            stageCol.setCellValueFactory(new PropertyValueFactory<>("stage"));

            TableColumn<LoadStage, String> busyCol = new TableColumn<>("Busy");
            busyCol.setCellValueFactory(new PropertyValueFactory<>("busy"));

            TableColumn<LoadStage, String> addressCol = new TableColumn<>("Address");
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

            loadTableView.getColumns().addAll(stageCol, busyCol, addressCol);

            loadTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            ObservableList<LoadStage> data = FXCollections.observableArrayList();
            for (int i = 0; i < table.size(); i++) {
                data.add( (LoadStage) table.get(i));
            }

            loadTableView.setItems(data);

            loadTableView.setFixedCellSize(25);
            loadTableView.setPrefHeight(25 * 5 + 30);

            loadTableView.setPrefWidth(Double.MAX_VALUE);

            VBox vbox = new VBox(loadTableView);
            vbox.setFillWidth(true);

            return vbox;
        }
        else {
            TableColumn<StoreStage, String> stageCol = new TableColumn<>("Stage");
            stageCol.setCellValueFactory(new PropertyValueFactory<>("stage"));

            TableColumn<StoreStage, String> busyCol = new TableColumn<>("Busy");
            busyCol.setCellValueFactory(new PropertyValueFactory<>("busy"));

            TableColumn<StoreStage, String> addressCol = new TableColumn<>("Address");
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

            storeTableView.getColumns().addAll(stageCol, busyCol, addressCol);

            storeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            ObservableList<StoreStage> data = FXCollections.observableArrayList();
            for (int i = 0; i < table.size(); i++) {
                data.add( (StoreStage) table.get(i));
            }

            storeTableView.setItems(data);

            storeTableView.setFixedCellSize(25);
            storeTableView.setPrefHeight(25 * 5 + 30);

            storeTableView.setPrefWidth(Double.MAX_VALUE);

            VBox vbox = new VBox(storeTableView);
            vbox.setFillWidth(true);

            return vbox;
        }
    }
}
