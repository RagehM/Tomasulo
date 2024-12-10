package units.stage;

import units.stage.addressStage.LoadStage;
import units.stage.addressStage.StoreStage;
import units.stage.aluStage.FloatingAdderStage;
import units.stage.aluStage.FloatingMultiplyStage;

import java.util.ArrayList;

public class Stage {
  public static ArrayList<LoadStage> loadTable = new ArrayList();
  public static ArrayList<StoreStage> storeTable = new ArrayList();
  public static ArrayList<FloatingAdderStage> adderTable = new ArrayList();
  public static ArrayList<FloatingMultiplyStage> multiplyTable = new ArrayList();
  public Stage() {
  }

  public static String printTable(ArrayList<? extends Stage> table) {
    String result = "";
    for(int i = 0; i < table.size(); i++) {
      System.out.println(table.get(i).toString());
    }
    return result;
  }
}
