package units.stage.addressStage;

import static gui.simulatingStage.Simulate.cycle;
import static units.FloatRegister.updateFloatRegister;
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
		boolean dataAvailable = AddressStage
				.allTrue(Cache.checkAddressAvailability(Integer.parseInt(loadStage.getAddress()), numberOfBytes));

		if (!dataAvailable) {
			loadStage.setMiss(true);
			Cache.loadFromMemoryToCache(Integer.parseInt(loadStage.getAddress()), numberOfBytes);
		}

		// Update Register File Dependency
		updateFloatRegister(instruction.getDestination(), loadStage); // to be checked again

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
