package gui.setupStage;

import gui.simulatingStage.SimulatingStage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import units.FloatRegister;
import units.IntegerRegister;
import units.MemoryBlocks;
import units.cache.Cache;
import units.instructionUnit;
import units.stage.addressStage.LoadStage;
import units.stage.addressStage.StoreStage;
import units.stage.aluStage.BranchStage;
import units.stage.aluStage.FloatingAdderStage;
import units.stage.aluStage.FloatingMultiplyStage;
import units.stage.aluStage.IntegerStage;

public class SetupStage {

	public static MemoryBlocks memory = new MemoryBlocks(1024);

	public static Stage setupMainStage() {
		Stage stage = new Stage();
		BorderPane borderPane = new BorderPane();
		Scene scene = new Scene(borderPane, 1500, 600);

		HBox loadInstructions = FileSetup.setupFile(stage);
		HBox cacheSetup = CacheSetup.setup();
		HBox instructionSetup = InstructionSetup.setup();
		HBox reservationSetup = AluSetup.setup();
		HBox bufferSetup = AddressSetup.setup();

		Button startButton = new Button("Start!");

		startButton.getStyleClass().add("button");
		startButton.setOnAction(event -> {
			instructionUnit instUnit = new instructionUnit();
			instUnit.parse();
			try {
				Cache cache = new Cache(CacheSetup.getLatency(), CacheSetup.getMissPenalty(), CacheSetup.getCacheSize(),
						CacheSetup.getBlockSize());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			MemoryBlocks.init(CacheSetup.getBlockSize());

			FloatRegister.initRegisterFile();

			IntegerRegister.initRegisterFile();

			for (int i = 0; i < AddressSetup.getLoadSize(); i++) {
				new LoadStage(false, "");
			}

			for (int i = 0; i < AddressSetup.getStoreSize(); i++) {
				new StoreStage(false, "", 0, null);
			}

			for (int i = 0; i < AluSetup.getFloatingAdder(); i++) {
				new FloatingAdderStage(false, "", 0, 0, null, null);
			}

			for (int i = 0; i < AluSetup.getFloatingMul(); i++) {
				new FloatingMultiplyStage(false, "", 0, 0, null, null);
			}

			for (int i = 0; i < AluSetup.getIntegerAdder(); i++) {
				new IntegerStage(false, "", 0, null, 0);
			}

			new BranchStage(false, "", 0);

			Stage simStage = SimulatingStage.setupSimulatingStage(instructionUnit.instructionTable,
					units.stage.Stage.loadTable, units.stage.Stage.storeTable, units.stage.Stage.adderTable,
					units.stage.Stage.multiplyTable, units.stage.Stage.integerTable, units.stage.Stage.branchTable);
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
