package gui.simulatingStage;

import static units.instructionUnit.*;
import static units.stage.Stage.*;

public class Simulate {
	public static int cycle = 0;
	public static void Simulate() {
//		adderTable.get(0).setBusy(true);
//		loadTable.get(0).setAddress("505");
//		System.out.println(instructionTable.get(0).toString());
		// w
		execute();
		if(lastInstructionIndex != instructionTable.size()){
			dispatch();
		}
		cycle++;
	}
}
