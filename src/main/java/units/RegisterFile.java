package units;

import units.stage.Stage;

import java.util.ArrayList;

public class RegisterFile {
  private String register;
  private static int number = 0;
  private Stage Qi;
  private String content;
  public static ArrayList<RegisterFile> registerTable = new ArrayList<RegisterFile>();

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

  public RegisterFile(Stage qi, String content) {
    this.Qi = qi;
    this.content = content;
    this.register = "F" + number;
    number++;
    registerTable.add(this);
  }

  public static void initRegisterFile() {
    for (int i = 0; i < 32; i++) {
      new RegisterFile(null, "");
    }
  }
}
