package units;

import java.util.ArrayList;

import units.stage.Stage;

public class FloatRegister {
	private String register;
	private static int number = 0;
	private Stage Qi;
	private double content;
	public static ArrayList<FloatRegister> floatRegisterTable = new ArrayList<FloatRegister>();

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

	public double getContent() {
		return content;
	}

	public void setContent(double content) {
		this.content = content;
	}

	public FloatRegister(Stage qi, double content) {
		this.Qi = qi;
		this.content = content;
		this.register = "F" + number;
		number++;
		floatRegisterTable.add(this);
	}

	public String toString() {
		return this.register;
	}

	public static void initRegisterFile() {
		for (int i = 0; i < 32; i++) {
			new FloatRegister(null, i);
		}
	}

	public static FloatRegister getFloatRegister(String registerName) {
		for (int i = 0; i < floatRegisterTable.size(); i++) {
			if (registerName.equals(floatRegisterTable.get(i).getRegister())) {
				return floatRegisterTable.get(i);
			}
		}
		return null;
	}

	public static int getFloatRegisterIndex(String registerName) {
		for (int i = 0; i < floatRegisterTable.size(); i++) {
			if (registerName.equals(floatRegisterTable.get(i).getRegister())) {
				return i;
			}
		}
		return -1;
	}

	public static void updateFloatRegister(String registerName, Stage stage) {
		FloatRegister register = getFloatRegister(registerName);
		int registerIndex = getFloatRegisterIndex(registerName);
		if (register != null && registerIndex != -1) {
			register.setQi(stage);
			floatRegisterTable.remove(registerIndex);
			floatRegisterTable.add(registerIndex, register);
		}
	}
}
