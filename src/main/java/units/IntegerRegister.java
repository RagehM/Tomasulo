package units;

import java.util.ArrayList;

import units.stage.Stage;
import units.stage.aluStage.IntegerStage;

public class IntegerRegister {
	private String register;
	private static int number = 0;
	private Stage Qi;
	private long content;

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

	public long getContent() {
		return content;
	}

	public void setContent(long content) {
		this.content = content;
	}

	public static ArrayList<IntegerRegister> integerRegisterTable = new ArrayList<IntegerRegister>();

	public String toString() {
		return this.register + this.Qi + this.content;
	}

	public IntegerRegister(IntegerStage qi, long content) {
		this.Qi = qi;
		this.content = content;
		this.register = "R" + number;
		number++;
		integerRegisterTable.add(this);
	}

	public static IntegerRegister getRegister(String registerName) {
		for (int i = 0; i < integerRegisterTable.size(); i++) {
			if (registerName.equals(integerRegisterTable.get(i).getRegister())) {
				return integerRegisterTable.get(i);
			}
		}
		return null;
	}

	public static void initRegisterFile() {
		for (int i = 0; i < 32; i++) {
			new IntegerRegister(null, (long) i);
		}
	}

	public static IntegerRegister getIntegerRegister(String registerName) {
		for (int i = 0; i < integerRegisterTable.size(); i++) {
			if (registerName.equals(integerRegisterTable.get(i).getRegister())) {
				return integerRegisterTable.get(i);
			}
		}
		return null;
	}

	public static int getIntegerRegisterIndex(String registerName) {
		for (int i = 0; i < integerRegisterTable.size(); i++) {
			if (registerName.equals(integerRegisterTable.get(i).getRegister())) {
				return i;
			}
		}
		return -1;
	}

	public static void updateIntegerRegister(String registerName, Stage stage) {
		IntegerRegister register = getIntegerRegister(registerName);
		int registerIndex = getIntegerRegisterIndex(registerName);
		if (register != null && registerIndex != -1) {
			register.setQi(stage);
			integerRegisterTable.set(registerIndex, register);
		}
	}
}
