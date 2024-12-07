package gui.setupStage;

import gui.simulatingStage.RegisterTable;
import gui.simulatingStage.SimulatingStage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import units.CacheUnit;
import units.RegisterUnit;
import units.stage.addressStage.StoreStage;
import units.addressUnit.LoadUnit;
import units.stage.addressStage.LoadStage;
import units.addressUnit.StoreUnit;
import units.instructionUnit;

import java.util.ArrayList;

public class SetupStage {
  public static Stage setupMainStage() {
    Stage stage = new Stage();
    BorderPane borderPane = new BorderPane();
    Scene scene = new Scene(borderPane, 1500, 600);

    HBox loadInstructions = FileSetup.setupFile(stage);
    HBox cacheSetup = CacheSetup.setupCache();
    HBox instructionSetup = InstructionSetup.setupInstructions();
    HBox reservationSetup = StationsSetup.setupStations();
    HBox bufferSetup = BufferSetup.setupBuffer();

    Button startButton = new Button("Start!");

    startButton.getStyleClass().add("button");
    startButton.setOnAction(event -> {
      instructionUnit instUnit = new instructionUnit();
      instUnit.parse();

      CacheUnit cache = new CacheUnit(CacheSetup.getLatency(), CacheSetup.getMissPenalty(), CacheSetup.getCacheSize(), CacheSetup.getBlockSize());

      ArrayList loadStages = new ArrayList<LoadStage>();
      for(int i = 0; i < BufferSetup.getLoadBuffer(); i++) {
        LoadStage loadStage = new LoadStage(false,"");
        loadStages.add(loadStage);
      }
      LoadUnit loadUnit = new LoadUnit(loadStages);

      ArrayList storeStages = new ArrayList<StoreStage>();
      for(int i = 0; i < BufferSetup.getStoreBuffer(); i++) {
        StoreStage storeStage = new StoreStage(false,"");
        storeStages.add(storeStage);
      }
      StoreUnit storeUnit = new StoreUnit(storeStages);

      Stage simStage = SimulatingStage.setupSimulatingStage(instUnit, loadUnit, storeUnit);
      simStage.show();
      stage.hide();
    });

    HBox box = new HBox(cacheSetup, instructionSetup, reservationSetup, bufferSetup);
    box.setAlignment(Pos.CENTER);

    HBox box2 = new HBox(loadInstructions, startButton);
    box2.setAlignment(Pos.CENTER);

    VBox box3 = new VBox(box, box2);
    box3.setAlignment(Pos.CENTER);

    borderPane.setCenter(box);
    borderPane.setBottom(box3);

    String cssFile = SetupStage.class.getResource("/style.css").toExternalForm();
    scene.getStylesheets().add(cssFile);

    stage.setScene(scene);
    return stage;
  }
}
