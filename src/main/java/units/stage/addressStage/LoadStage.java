package units.stage.addressStage;

import static gui.simulatingStage.Simulate.cycle;
import instructions.Instruction;
import static units.FloatRegister.updateFloatRegister;
import static units.instructionUnit.instructionTable;
import static units.instructionUnit.lastInstructionIndex;
import units.stage.Stage;
public class LoadStage extends AddressStage {
	private String stage;
	private static int number = 1;

	public String getStage() {
		return stage;
	}

	public LoadStage(Boolean busy, String address) {
		super(busy, address);
		this.stage = "L" + number;
		number++;
		loadTable.add(this);
	}

	public String toString() {
		return this.stage;
	}

	private static void addToLoadTable(LoadStage loadStage) {

	}

	public static boolean checkAddressClash(Instruction instruction) {

		for (int i = 0; i < storeTable.size(); i++) {
			if (storeTable.get(i).getBusy() && storeTable.get(i).getAddress() == instruction.getOperand1()) {
				return true;
			}
		}

		return false;
	}

	public static void dispatchLoad(Instruction instruction) {
			// Check if the load must wait for a store
		if (LoadStage.checkAddressClash(instruction)) {
			// Stall the issuing if a clash is detected
			return;
		}

		int firstUnusedIndex = Stage.getFirstEmptySlot(loadTable);
		LoadStage loadstage = loadTable.get(firstUnusedIndex);

		// Replace in load Reservation Station
		loadstage.setBusy(true);
		loadstage.setAddress(instruction.getOperand1());
		loadstage.setIssueCycle(cycle + 1);
		loadstage.setInstructionIndex(lastInstructionIndex);

		loadTable.set(firstUnusedIndex, loadstage);

		// Update Register File Dependency
		updateFloatRegister(instruction.getDestination(), loadstage); // to be checked again

		// Update Instruction Table Entry
		instruction.setIssue(cycle + 1);
		instructionTable.set(lastInstructionIndex, instruction);
		reservedLoad++;
	}

	public float produce() {
		System.out.println("METHOD NOT IMPLEMENTED YET: LOAD -> PRODUCE");
		return -1;
	}
}
