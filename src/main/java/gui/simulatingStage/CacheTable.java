package gui.simulatingStage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import units.stage.aluStage.BranchStage;
import units.stage.aluStage.CacheStage;

import java.util.ArrayList;

public class CacheTable {
    public static TableView<CacheStage> cacheTableView=new TableView<>();

    public static VBox createTable(ArrayList<CacheStage> table,int size){

        TableColumn<CacheStage,String> block=new TableColumn<>("Block");
        block.setCellValueFactory(new PropertyValueFactory<>("blockName"));
        
        TableColumn<CacheStage,String> address=new TableColumn<>("Address");
        address.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<CacheStage,String> value=new TableColumn<>("value");
        value.setCellValueFactory((new PropertyValueFactory<>("value")));

        cacheTableView.getColumns().addAll(block,address,value);
        cacheTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        ObservableList<CacheStage> data = FXCollections.observableArrayList();
        for (int i = 0; i < table.size(); i++) {
            data.add((CacheStage) table.get(i));
        }

        cacheTableView.setItems(data);

        cacheTableView.setFixedCellSize(25);
        cacheTableView.setPrefHeight(25 * 5 + 30);

        cacheTableView.setPrefWidth(Double.MAX_VALUE);

        VBox vbox = new VBox(cacheTableView);
        vbox.setFillWidth(true);

        return vbox;

    }
}
