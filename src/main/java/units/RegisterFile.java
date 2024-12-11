package units;

import java.util.ArrayList;

import units.stage.Stage;

public class RegisterFile {
	private String register;
	private static int number = 0;
	private Stage Qi;
	private float content;
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

	public float getContent() {
		return content;
	}

	public void setContent(float content) {
		this.content = content;
	}

	public RegisterFile(Stage qi, float content) {
		this.Qi = qi;
		this.content = content;
		this.register = "F" + number;
		number++;
		registerTable.add(this);
	}

	public String toString() {
		return this.register;
	}

	public static void initRegisterFile() {
		for (int i = 0; i < 32; i++) {
			new RegisterFile(null, i);
		}
	}

	public static RegisterFile getRegister(String registerName) {
		for (int i = 0; i < registerTable.size(); i++) {
			if (registerName.equals(registerTable.get(i).getRegister())) {
				return registerTable.get(i);
			}
		}
		return null;
	}

	public static int getRegisterIndex(String registerName) {
		for (int i = 0; i < registerTable.size(); i++) {
			if (registerName.equals(registerTable.get(i).getRegister())) {
				return i;
			}
		}
		return -1;
	}

	public static void updateRegister(String registerName, Stage stage) {
		RegisterFile register = getRegister(registerName);
		int registerIndex = getRegisterIndex(registerName);
		if (register != null && registerIndex != -1) {
			register.setQi(stage);
			registerTable.remove(registerIndex);
			System.out.println(register.toString());
			registerTable.add(registerIndex, register);
		}
	}
}
