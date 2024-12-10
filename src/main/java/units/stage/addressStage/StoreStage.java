package units.stage.addressStage;

public class StoreStage extends AddressStage {
  private String stage;
  private static int number = 1;

  public String getStage() {
    return stage;
  }

  public StoreStage(Boolean busy, String address) {
    super(busy,address);
    this.stage = "S" + number;
    number++;
    storeTable.add(this);
  }
  public String toString() {
    return this.stage + " " + super.toString();
  }
}
