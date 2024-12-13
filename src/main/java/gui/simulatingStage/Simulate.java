package gui.simulatingStage;

import static units.instructionUnit.dispatch;
import static units.instructionUnit.execute;
import static units.instructionUnit.instructionTable;
import static units.instructionUnit.lastInstructionIndex;
import static units.instructionUnit.writeBack;

import gui.AlertBox;
import units.stage.Stage;

public class Simulate {
	public static int cycle = 0;

	public static void Simulate() throws Exception {
//		adderTable.get(0).setBusy(true);
//		loadTable.get(0).setAddress("505");
//		System.out.println(instructionTable.get(0).toString());
		// w

		if (Stage.checkIfAnythingIsRunning() || lastInstructionIndex != instructionTable.size()) {

			execute();
			writeBack();
			if (lastInstructionIndex != instructionTable.size()) {
				dispatch();
			}
			cycle++;
		} else {
			// Program execution complete, ollo 7aga
			AlertBox.display("Execution Finished", "The Programs Execution is done");
		}
	}
}
