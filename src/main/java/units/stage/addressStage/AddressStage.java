package units.stage.addressStage;
import units.stage.Stage;

public class AddressStage extends Stage {

  private String address;


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
