package units.stage;

import java.util.ArrayList;

import units.stage.addressStage.LoadStage;
import units.stage.addressStage.StoreStage;
import units.stage.aluStage.FloatingAdderStage;
import units.stage.aluStage.FloatingMultiplyStage;

public class Stage {
  public static ArrayList<LoadStage> loadTable = new ArrayList();
  public static ArrayList<StoreStage> storeTable = new ArrayList();
  public static ArrayList<FloatingAdderStage> adderTable = new ArrayList();
  public static ArrayList<FloatingMultiplyStage> multiplyTable = new ArrayList();
  public static int reservedLoad = 0;
  public static int reservedStore = 0;
  public static int reservedAdder = 0;
  public static int reservedMultiply = 0;
  protected Boolean busy;
  protected int cycleOfExecution = 0;
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

  public static String toString(ArrayList<? extends Stage> table) {
    String result = "";
    for(int i = 0; i < table.size(); i++) {
      System.out.println(table.get(i).toString());
    }
    return result;
  }
  
  public static int getFirstEmptySlot(ArrayList<? extends Stage> table) {//Takes the actual table reference
	  
	 for(int i = 0; i < table.size(); i++) {
		 if(!table.get(i).busy)
			 return i;
	 }
	 
	 return -1;
  }
}
