package units.stage.addressStage;
import units.stage.Stage;

public class AddressStage extends Stage {
  private Boolean busy;
  private String address;

  public Boolean getBusy() {
    return busy;
  }

  public void setBusy(Boolean busy) {
    this.busy = busy;
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
}
