package units.stage.aluStage;

import units.stage.Stage;

public class FloatingAdderStage extends AluStage {
  private String stage;
  private static int number = 1;

  public String getStage() {
    return stage;
  }

  public FloatingAdderStage(Boolean busy, String op, Stage Vj, Stage Vk, Stage Qj, Stage Qk) {
    super(busy, op, Vj, Vk, Qj, Qk);
    this.stage = "A" + number;
    number++;
    Stage.adderTable.add(this);
  }

  public String toString() {
    return this.stage + " " + super.toString();
  }

}
