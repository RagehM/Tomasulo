package units.stage.aluStage;

import units.stage.Stage;

public class AluStage extends Stage {
  private Boolean busy;
  private String op;
  private Stage Vj;
  private Stage Vk;
  private Stage Qj;
  private Stage Qk;

  public Boolean getBusy() {
    return busy;
  }

  public void setBusy(Boolean busy) {
    this.busy = busy;
  }

  public String getOp() {
    return op;
  }

  public void setOp(String op) {
    this.op = op;
  }

  public Stage getVj() {
    return Vj;
  }

  public void setVj(Stage vj) {
    Vj = vj;
  }

  public Stage getVk() {
    return Vk;
  }

  public void setVk(Stage vk) {
    Vk = vk;
  }

  public Stage getQj() {
    return Qj;
  }

  public void setQj(Stage qj) {
    Qj = qj;
  }

  public Stage getQk() {
    return Qk;
  }

  public void setQk(Stage qk) {
    Qk = qk;
  }

  public AluStage(Boolean busy, String op, Stage Vj, Stage Vk, Stage Qj, Stage Qk) {
    this.busy = busy;
    this.op = op;
    this.Vj = Vj;
    this.Vk = Vk;
    this.Qj = Qj;
    this.Qk = Qk;
  }

  public String toString() {
    return this.busy + " " + this.op + " " + this.Vj + " " + this.Vk + " " + this.Qj + " " + this.Qk;
  }
}
