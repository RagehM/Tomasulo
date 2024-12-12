package units.stage.aluStage;

import static gui.simulatingStage.Simulate.cycle;
import instructions.Instruction;
import units.IntegerRegister;
import static units.IntegerRegister.getIntegerRegister;
import static units.IntegerRegister.updateIntegerRegister;
import static units.instructionUnit.lastInstructionIndex;
import units.stage.Stage;

public class IntegerStage extends Stage {
	private String stage;
	private static int number = 1;
	private String op;
	private long Vj;
	private IntegerStage Qj;
	private long immediate; // Immediate Value

	public IntegerStage(Boolean busy, String op, long Vj, IntegerStage Qj, long immediate) {
		this.stage = "I" + number;
		number++;
		this.busy = busy;
		this.op = op;
		this.Vj = Vj;
		this.Qj = Qj;
		this.immediate = immediate;
		integerTable.add(this);
	}

	public IntegerStage getQj() {
		return Qj;
	}

	public void setQj(IntegerStage qj) {
		Qj = qj;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public long getVj() {
		return Vj;
	}

	public void setVj(long vj) {
		Vj = vj;
	}

	public long getImmediate() {
		return immediate;
	}

	public void setImmediate(long immediate) {
		this.immediate = immediate;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String toString() {
		return this.stage;
	}

    public long produce() {
        return this.getOp().contains("DSUBI") ? (this.getVj() - this.getImmediate()) : (this.getVj() + this.getImmediate());
    }
    
    public static void dispatchInteger(Instruction instruction, String operation) {
        int firstUnusedIndex = Stage.getFirstEmptySlot(integerTable);
		IntegerStage integerStage = integerTable.get(firstUnusedIndex);

		integerStage.setBusy(true);
		integerStage.setOp(operation);

		IntegerRegister operandRegister1 = getIntegerRegister(instruction.getOperand1());
		long operandValue1 = operandRegister1.getContent();

		if (operandRegister1.getQi() == null) {
			// if yes then set Vj to be the content of that register
			integerStage.setVj(operandValue1);
		} else {
			// else make the first operand depends on that stage
			integerStage.setQj(operandRegister1.getQi());
		}

		integerStage.setIssueCycle(cycle + 1);
		integerStage.setInstructionIndex(lastInstructionIndex);
		updateIntegerRegister(instruction.getDestination(), integerStage);
		instruction.setIssue(cycle + 1);
		integerTable.set(firstUnusedIndex, integerStage);
		System.out.println(integerTable);
		reservedInteger++;
	}
}
