package units.stage.addressStage;

import instructions.Instruction;

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

	public float produce() {
		System.out.println("METHOD NOT IMPLEMENTED YET: LOAD -> PRODUCE");
		return -1;
	}
}
