package units.stage.aluStage;

import instructions.Instruction;
import units.FloatRegister;
import units.stage.Stage;

import static gui.simulatingStage.Simulate.cycle;
import static units.FloatRegister.getRegister;
import static units.FloatRegister.updateRegister;
import static units.instructionUnit.lastInstructionIndex;
public class FloatingMultiplyStage extends AluStage {
	private String stage;
	private static int number = 1;

	public String getStage() {
		return stage;
	}

	public FloatingMultiplyStage(Boolean busy, String op, float Vj, float Vk, Stage Qj, Stage Qk) {
		super(busy, op, Vj, Vk, Qj, Qk);
		this.stage = "M" + number;
		number++;
		Stage.multiplyTable.add(this);
	}

	public String toString() {
		return this.stage;
	}

	public static void dispatchMultiply(Instruction instruction, String operation) {
		// get the first un busy multiply stage
		int firstUnusedIndex = Stage.getFirstEmptySlot(multiplyTable);
		FloatingMultiplyStage multiplyStage = multiplyTable.get(firstUnusedIndex);

		// update its status to be busy
		multiplyStage.setBusy(true);
		multiplyStage.setOp(operation);

		// get the first operand register
		FloatRegister operandRegister1 = getRegister(instruction.getOperand1());
		float operandValue1 = operandRegister1.getContent();

		// check if that register does not depend on any other stage
		if (operandRegister1.getQi() == null) {
			// if yes then set Vj to be the content of that register
			multiplyStage.setVj(operandValue1);
		}
		else {
			// else make the first operand depends on that stage
			multiplyStage.setQj(operandRegister1.getQi());
		}

		// get the second operand register
		FloatRegister operandRegister2 = getRegister(instruction.getOperand2());
		float operandValue2 = operandRegister2.getContent();

		// check if that register does not depend on any other stage
		if (operandRegister2.getQi() == null) {
			// if yes then set Vk to be the content of that register
			multiplyStage.setVk(operandValue2);
		}
		else {
			// else make the second operand depends on that stage
			multiplyStage.setQk(operandRegister2.getQi());
		}

		multiplyStage.setIssueCycle(cycle + 1);
		multiplyStage.setInstructionIndex(lastInstructionIndex);
		updateRegister(instruction.getDestination(), multiplyStage);
		instruction.setIssue(cycle + 1);
		multiplyTable.set(firstUnusedIndex, multiplyStage);
		reservedMultiply++;
	}


	public float produce() {
		return this.getOp().contains("MUL") ? (this.getVj() * this.getVk()) : (this.getVj() / this.getVk());
	}
}
