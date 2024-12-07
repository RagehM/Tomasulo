package units;

import units.stage.Stage;

import java.util.ArrayList;

public class RegisterUnit {
  private String register;
  private static int number = 1;
  private Stage Qi;
  private String content;
  public static ArrayList<RegisterUnit> registerTable = new ArrayList<RegisterUnit>();

  public String getRegister() {
    return register;
  }

  public void setRegister(String register) {
    this.register = register;
  }

  public Stage getQi() {
    return Qi;
  }

  public void setQi(Stage qi) {
    Qi = qi;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public RegisterUnit(Stage qi, String content) {
    this.Qi = qi;
    this.content = content;
    this.register = "F" + number;
    number++;
    registerTable.add(this);
  }

  private void registerUnitInit() {
    for (int i = 0; i < 32; i++) {
      new RegisterUnit(null, "");
    }
  }

}
