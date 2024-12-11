package units.stage.addressStage;

import instructions.Instruction;

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
  
  public static boolean checkAddressClash(Instruction instruction) {
	  
	  for(int i = 0; i < storeTable.size(); i++) {
		  if(storeTable.get(i).getBusy() && storeTable.get(i).getAddress() == instruction.getOperand1()) {
			  return true;
		  }
	  }
	  
	  for(int i = 0; i < loadTable.size(); i++) {
		  if(loadTable.get(i).getBusy() && loadTable.get(i).getAddress() == instruction.getOperand1()) {
			  return true;
		  }
	  }
	  
	  return false;
  }
}
