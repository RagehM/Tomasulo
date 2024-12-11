package gui.simulatingStage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import units.Cache;
import units.FloatRegister;
import units.MemoryCell;

public class MemoryTable {
    public VBox createTable(){
        TableView<MemoryCell> tableView = new TableView<>();

        TableColumn<MemoryCell,String> address=new TableColumn<>("Address");
        address.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<MemoryCell,String> value=new TableColumn<>("Value");
        value.setCellValueFactory(new PropertyValueFactory<>("value"));

        tableView.getColumns().addAll(address,value);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ObservableList<MemoryCell> data=FXCollections.observableArrayList();
        for(int i=0;i<MemoryCell.MemoryCellTable.size();i++){
            data.add((MemoryCell) MemoryCell.MemoryCellTable.get(i));
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
