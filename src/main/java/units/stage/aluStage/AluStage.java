package units.stage.aluStage;

import units.RegisterFile;
import units.stage.Stage;

public class AluStage extends Stage {
  private String op;
  private RegisterFile Vj;
  private RegisterFile Vk;
  private Stage Qj;
  private Stage Qk;

  public String getOp() {
    return op;
  }

  public void setOp(String op) {
    this.op = op;
  }

  public RegisterFile getVj() {
    return Vj;
  }

  public void setVj(RegisterFile vj) {
    Vj = vj;
  }

  public RegisterFile getVk() {
    return Vk;
  }

  public void setVk(RegisterFile vk) {
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

  public AluStage(Boolean busy, String op, RegisterFile Vj, RegisterFile Vk, Stage Qj, Stage Qk) {
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
