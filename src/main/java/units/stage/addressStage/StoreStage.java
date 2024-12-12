package units.stage.addressStage;

import static gui.simulatingStage.Simulate.cycle;
import static units.FloatRegister.getFloatRegister;
import static units.instructionUnit.instructionTable;
import static units.instructionUnit.lastInstructionIndex;

import instructions.Instruction;
import units.FloatRegister;
import units.stage.Stage;

public class StoreStage extends AddressStage {
	private String stage;
	private static int number = 1;
	private double V;
	private Stage Q;

	public String getStage() {
		return stage;
	}

	public double getV() {
		return V;
	}

	public void setV(double v) {
		V = v;
	}

	public Stage getQ() {
		return Q;
	}

	public void setQ(Stage q) {
		Q = q;
	}

	public StoreStage(Boolean busy, String address, double V, Stage Q) {
		super(busy, address);
		this.stage = "S" + number;
		number++;
		this.V = V;
		this.Q = Q;
		storeTable.add(this);
	}

	public String toString() {
		return this.stage;
	}

	public static void dispatchStore(Instruction instruction) {

		int firstUnusedIndex = Stage.getFirstEmptySlot(storeTable);
		StoreStage storeStage = storeTable.get(firstUnusedIndex);

		// Replace in store Reservation Station
		storeStage.setBusy(true);
		storeStage.setAddress(instruction.getOperand1());

		FloatRegister destinationRegister = getFloatRegister(instruction.getDestination());
		double destinationValue = destinationRegister.getContent();

		// check if the destination Register does not depend on any stage
		if (destinationRegister.getQi() == null) {
			// if yes then set the value to be the content of the register
			storeStage.setV((int) destinationValue);
		} else {
			// else make the store depends on that stage
			storeStage.setQ(destinationRegister.getQi());
		}

		storeStage.setIssueCycle(cycle + 1);
		storeStage.setInstructionIndex(lastInstructionIndex);
		storeTable.set(firstUnusedIndex, storeStage);

		// Update Instruction Table Entry
		instruction.setIssue(cycle + 1);
		instructionTable.set(lastInstructionIndex, instruction);
		reservedStore++;
	}

	public static boolean checkAddressClash(Instruction instruction) {

		for (int i = 0; i < storeTable.size(); i++) {
			if (storeTable.get(i).getBusy() && storeTable.get(i).getAddress() == instruction.getOperand1()) {
				return true;
			}
		}

		for (int i = 0; i < loadTable.size(); i++) {
			if (loadTable.get(i).getBusy() && loadTable.get(i).getAddress() == instruction.getOperand1()) {
				return true;
			}
		}

		return false;
	}
}
