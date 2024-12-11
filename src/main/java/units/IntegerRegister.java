package units;

import units.stage.Stage;

import java.util.ArrayList;

public class IntegerRegister {
	private String register;
	private static int number = 0;
  private Stage Qi; //TODO change this to be of type integerStage
	private int content;

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

	public int getContent() {
		return content;
	}

	public void setContent(int content) {
		this.content = content;
	}

	public static ArrayList<IntegerRegister> integerRegisterTable = new ArrayList<IntegerRegister>();

	public IntegerRegister(Stage qi, int content) {
		this.Qi = qi;
		this.content = content;
		this.register = "R" + number;
		number++;
		integerRegisterTable.add(this);
	}

	public static void initRegisterFile() {
		for (int i = 0; i < 32; i++) {
			new IntegerRegister(null, i);
		}
	}
}
