package gui.simulatingStage;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import units.stage.addressStage.AddressStage;
import units.addressUnit.AddressUnit;

public class AddressTable {
    public static VBox createTable(AddressUnit unit, int size) {
        TableView<AddressStage> tableView = new TableView<>();

        TableColumn<AddressStage, String> stageCol = new TableColumn<>("Stage");
        stageCol.setCellValueFactory(new PropertyValueFactory<>("stage"));

        TableColumn<AddressStage, String> busycol = new TableColumn<>("Busy");
        busycol.setCellValueFactory(new PropertyValueFactory<>("busy"));

        TableColumn<AddressStage, String> addresscol = new TableColumn<>("Address");
        addresscol.setCellValueFactory(new PropertyValueFactory<>("address"));

        tableView.getColumns().addAll(stageCol, busycol, addresscol);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ObservableList<AddressStage> data = FXCollections.observableArrayList();
        for (int i = 0; i < unit.table.size(); i++) {
           data.add((AddressStage) unit.table.get(i));
        }

        tableView.setItems(data);

        tableView.setFixedCellSize(25);
        tableView.setPrefHeight(25 * size + 30);

        tableView.setPrefWidth(Double.MAX_VALUE);

        VBox vbox = new VBox(tableView);
        vbox.setFillWidth(true);

        return vbox;
    }
}
