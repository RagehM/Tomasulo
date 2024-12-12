package units.stage.aluStage;

import static gui.simulatingStage.Simulate.cycle;
import instructions.Instruction;
import units.FloatRegister;
import static units.FloatRegister.getFloatRegister;
import static units.FloatRegister.updateFloatRegister;
import static units.instructionUnit.lastInstructionIndex;
import units.stage.Stage;
public class FloatingAdderStage extends AluStage {
	private String stage;
	private static int number = 1;

	public String getStage() {
		return stage;
	}

	public FloatingAdderStage(Boolean busy, String op, double Vj, double Vk, Stage Qj, Stage Qk) {
		super(busy, op, Vj, Vk, Qj, Qk);
		this.stage = "A" + number;
		number++;
		Stage.adderTable.add(this);
	}

	public String toString() {
		return this.stage;
	}

	public double produce() {
		return this.getOp().contains("SUB") ? (this.getVj() - this.getVk()) : (this.getVj() + this.getVk());
	}

	public static void dispatchAdder(Instruction instruction, String operation) {
		int firstUnusedIndex = Stage.getFirstEmptySlot(adderTable);
		FloatingAdderStage adderStage = adderTable.get(firstUnusedIndex);

		adderStage.setBusy(true);
		adderStage.setOp(operation);

		FloatRegister operandRegister1 = getFloatRegister(instruction.getOperand1());
		double operandValue1 = operandRegister1.getContent();

		// check if that register does not depend on any other stage
		if (operandRegister1.getQi() == null) {
			// if yes then set Vj to be the content of that register
			adderStage.setVj(operandValue1);
		}
		else {
			// else make the first operand depends on that stage
			adderStage.setQj(operandRegister1.getQi());
		}

		FloatRegister operandRegister2 = getFloatRegister(instruction.getOperand2());
		double operandValue2 = operandRegister2.getContent();
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
		updateFloatRegister(instruction.getDestination(), adderStage);
		instruction.setIssue(cycle + 1);
		adderTable.set(firstUnusedIndex, adderStage);
		reservedAdder++;
	}

}
