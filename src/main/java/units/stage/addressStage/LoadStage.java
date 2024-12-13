package units.stage.addressStage;

import static gui.simulatingStage.Simulate.cycle;
import static units.FloatRegister.updateFloatRegister;
import static units.IntegerRegister.updateIntegerRegister;
import static units.instructionUnit.instructionTable;
import static units.instructionUnit.lastInstructionIndex;

import instructions.Instruction;
import units.instructionUnit;
import units.cache.Cache;
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

	public static boolean checkAddressClash(Instruction instruction) {

		for (int i = 0; i < storeTable.size(); i++) {
			if (storeTable.get(i).getBusy() && storeTable.get(i).getAddress() == instruction.getOperand1()) {
				return true;
			}
		}

		return false;
	}

	public static void dispatchLoad(Instruction instruction) {

		int firstUnusedIndex = Stage.getFirstEmptySlot(loadTable);
		LoadStage loadStage = loadTable.get(firstUnusedIndex);
		String operation = instructionUnit.getInstructionOperation(instruction);

		// Replace in load Reservation Station
		loadStage.setBusy(true);
		loadStage.setAddress(instruction.getOperand1());
		loadStage.setIssueCycle(cycle + 1);
		loadStage.setInstructionIndex(lastInstructionIndex);

		loadTable.set(firstUnusedIndex, loadStage);
		int numberOfBytes = (operation.equals("LW") || operation.equals("L.S")) ? 4 : 8;
		boolean[] addressAvailability = Cache.checkAddressAvailability(Integer.parseInt(loadStage.getAddress()),
				numberOfBytes);

		boolean dataAvailable = AddressStage.allTrue(addressAvailability);

		if (!dataAvailable) {
			loadStage.setMiss(true);
		} else {
			loadStage.setMiss(false);
		}

		// Update Register File Dependency
		if (operation.equals("LW") || operation.equals("LD")) {
			updateIntegerRegister(instruction.getDestination(), loadStage);
		} else {
			updateFloatRegister(instruction.getDestination(), loadStage);
		}
		// Update Instruction Table Entry
		instruction.setIssue(cycle + 1);
		instructionTable.set(lastInstructionIndex, instruction);
		reservedLoad++;
	}

	public double produce() throws Exception {
		// to make the instruction be able to execute again after branch if available
		this.setExecutionCycle(0);

		return Cache.loadFromCache(this);
	}
}
