package units.stage;

import java.util.ArrayList;

import units.stage.addressStage.LoadStage;
import units.stage.addressStage.StoreStage;
import units.stage.aluStage.*;

public class Stage implements Comparable {
	public static ArrayList<LoadStage> loadTable = new ArrayList<LoadStage>();
	public static ArrayList<StoreStage> storeTable = new ArrayList<StoreStage>();
	public static ArrayList<FloatingAdderStage> adderTable = new ArrayList<FloatingAdderStage>();
	public static ArrayList<FloatingMultiplyStage> multiplyTable = new ArrayList<FloatingMultiplyStage>();
	public static ArrayList<IntegerStage> integerTable = new ArrayList<IntegerStage>();
	public static ArrayList<BranchStage> branchTable = new ArrayList<BranchStage>();


	public static int reservedLoad = 0;
	public static int reservedStore = 0;
	public static int reservedAdder = 0;
	public static int reservedMultiply = 0;
	public static int reservedInteger = 0;
	protected Boolean busy;
	protected int cycleOfExecution = 0;
	private int issueCycle = 0;
	private int instructionIndex = 0;

	public Stage() {
	}

	public Boolean getBusy() {
		return busy;
	}

	public void setBusy(Boolean busy) {
		this.busy = busy;
	}

	public int getExecutionCycle() {
		return cycleOfExecution;
	}

	public void setExecutionCycle(int cycle) {
		this.cycleOfExecution = cycle;
	}

	public int getIssueCycle() {
		return issueCycle;
	}

	public void setIssueCycle(int issueCycle) {
		this.issueCycle = issueCycle;
	}

	public static String toString(ArrayList<? extends Stage> table) {
		String result = "";
		for (int i = 0; i < table.size(); i++) {
			System.out.println(table.get(i).toString());
		}
		return result;
	}

	public static int getFirstEmptySlot(ArrayList<? extends Stage> table) {// Takes the actual table reference

		for (int i = 0; i < table.size(); i++) {
			if (!table.get(i).busy)
				return i;
		}

		return -1;
	}

	public int getInstructionIndex() {
		return instructionIndex;
	}

	public void setInstructionIndex(int instructionIndex) {
		this.instructionIndex = instructionIndex;
	}

	@Override
	public int compareTo(Object o) {
		Stage cast = (Stage) o;
		return Integer.compare(this.issueCycle, cast.issueCycle);
	}
}
