package units.stage.addressStage;

import units.stage.Stage;

public class AddressStage extends Stage {

	private String address;
	private boolean isMiss;

	public boolean isMiss() {
		return isMiss;
	}

	public void setMiss(boolean isMiss) {
		this.isMiss = isMiss;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public AddressStage(boolean busy, String address) {
		this.busy = busy;
		this.address = address;

	}

	public String toString() {
		return this.busy + " " + this.address;
	}
}
