package units.stage.addressStage;

public class LoadStage extends AddressStage {
	private String stage;
	private static int number = 1;

	public String getStage() {
		return stage;
	}

	public LoadStage(Boolean busy, String address) {
		super(busy,address);
		this.stage = "L" + number;
		number++;
		loadTable.add(this);
	}

	public String toString() {
		return this.stage + " " + super.toString();
	}
}
