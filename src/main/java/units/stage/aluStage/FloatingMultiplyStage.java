package units.stage.aluStage;

import units.RegisterFile;
import units.stage.Stage;

public class FloatingMultiplyStage extends AluStage {
  private String stage;
  private static int number = 1;

  public String getStage() {
    return stage;
  }

  public FloatingMultiplyStage(Boolean busy, String op, RegisterFile Vj, RegisterFile Vk, Stage Qj, Stage Qk) {
    super(busy, op, Vj, Vk, Qj, Qk);
    this.stage = "M" + number;
    number++;
    Stage.multiplyTable.add(this);
  }

  public String toString() {
    return this.stage;
  }
}
