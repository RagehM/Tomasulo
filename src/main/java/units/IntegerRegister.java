package units;

import units.stage.Stage;

import java.util.ArrayList;

import units.stage.aluStage.IntegerStage;

public class IntegerRegister {
	private String register;
	private static int number = 0;
  private IntegerStage Qi;
	private int content;

	public String getRegister() {
		return register;
	}

	public void setRegister(String register) {
		this.register = register;
	}

	public IntegerStage getQi() {
		return Qi;
	}

	public void setQi(IntegerStage qi) {
		Qi = qi;
	}

	public int getContent() {
		return content;
	}

	public void setContent(int content) {
		this.content = content;
	}

	public static ArrayList<IntegerRegister> integerRegisterTable = new ArrayList<IntegerRegister>();

	public IntegerRegister(IntegerStage qi, int content) {
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
