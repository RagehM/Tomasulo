package units.stage.addressStage;

import instructions.Instruction;
import units.stage.Stage;

public class StoreStage extends AddressStage {
	private String stage;
	private static int number = 1;
	private int V;
	private Stage Q;

	public String getStage() {
		return stage;
	}

	public float getV() {
		return V;
	}

	public void setV(int v) {
		V = v;
	}

	public Stage getQ() {
		return Q;
	}

	public void setQ(Stage q) {
		Q = q;
	}

	public StoreStage(Boolean busy, String address, int V, Stage Q) {
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
