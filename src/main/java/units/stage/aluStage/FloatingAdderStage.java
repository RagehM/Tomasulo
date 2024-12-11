package units.stage.aluStage;

import instructions.Instruction;
import units.RegisterFile;
import units.stage.Stage;

import static gui.simulatingStage.Simulate.cycle;
import static units.RegisterFile.getRegister;
import static units.RegisterFile.updateRegister;
import static units.instructionUnit.lastInstructionIndex;
public class FloatingAdderStage extends AluStage {
	private String stage;
	private static int number = 1;

	public String getStage() {
		return stage;
	}

	public FloatingAdderStage(Boolean busy, String op, float Vj, float Vk, Stage Qj, Stage Qk) {
		super(busy, op, Vj, Vk, Qj, Qk);
		this.stage = "A" + number;
		number++;
		Stage.adderTable.add(this);
	}

	public String toString() {
		return this.stage;
	}

	public float produce() {
		return this.getOp().contains("SUB") ? (this.getVj() - this.getVk()) : (this.getVj() + this.getVk());
	}

	public static void dispatchAdder(Instruction instruction, String operation) {
		int firstUnusedIndex = Stage.getFirstEmptySlot(adderTable);
		FloatingAdderStage adderStage = adderTable.get(firstUnusedIndex);

		adderStage.setBusy(true);
		adderStage.setOp(operation);

		RegisterFile operandRegister1 = getRegister(instruction.getOperand1());
		float operandValue1 = operandRegister1.getContent();

		// check if that register does not depend on any other stage
		if (operandRegister1.getQi() == null) {
			// if yes then set Vj to be the content of that register
			adderStage.setVj(operandValue1);
		}
		else {
			// else make the first operand depends on that stage
			adderStage.setQj(operandRegister1.getQi());
		}

		RegisterFile operandRegister2 = getRegister(instruction.getOperand2());
		float operandValue2 = operandRegister2.getContent();
		// check if that register does not depend on any other stage
		if (operandRegister2.getQi() == null) {
			// if yes then set Vk to be the content of that register
			adderStage.setVk(operandValue2);
		}
		else {
			// else make the second operand depends on that stage
			adderStage.setQk(operandRegister2.getQi());
		}

		adderStage.setIssueCycle(cycle + 1);
		adderStage.setInstructionIndex(lastInstructionIndex);
		updateRegister(instruction.getDestination(), adderStage);
		instruction.setIssue(cycle + 1);
		adderTable.set(firstUnusedIndex, adderStage);
		reservedAdder++;
	}

}
