package gui.simulatingStage;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import units.stage.addressStage.AddressStage;
import java.util.ArrayList;

public class AddressTable {
    public static VBox createTable(ArrayList<? extends AddressStage> table , int size) {
        TableView<AddressStage> tableView = new TableView<>();

        TableColumn<AddressStage, String> stageCol = new TableColumn<>("Stage");
        stageCol.setCellValueFactory(new PropertyValueFactory<>("stage"));

        TableColumn<AddressStage, String> busyCol = new TableColumn<>("Busy");
        busyCol.setCellValueFactory(new PropertyValueFactory<>("busy"));

        TableColumn<AddressStage, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        tableView.getColumns().addAll(stageCol, busyCol, addressCol);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ObservableList<AddressStage> data = FXCollections.observableArrayList();
        for (int i = 0; i < table.size(); i++) {
           data.add( (AddressStage) table.get(i));
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
