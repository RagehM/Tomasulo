package units.stage.aluStage;

import static gui.simulatingStage.Simulate.cycle;
import static units.instructionUnit.lastInstructionIndex;

import instructions.BranchInstruction;
import instructions.Instruction;
import units.IntegerRegister;

public class BranchStage extends AluStage {
	private String stage;
	private int address;

	public int getAddress() {
		return address;
	}

	public String getStage() {
		return stage;
	}

	public BranchStage(boolean busy, String op, int address) {
		super(busy, op, 0, 0, null, null);
		this.stage = "B1";
		this.address = address;
		if (branchTable.size() != 0) {
			branchTable.remove(0);
			branchTable.add(this);
		} else
			branchTable.add(this);
	}

	public String toString() {
		return (this.stage + getOp() + getVj() + getVk() + getQj() + getQk() + address);
	}

	public static void dispatchBranch(Instruction instruction, String operation) {
		// Branch stalls the whole pipeline until complete, therefore, no need to check
		// for available slots for dispatch
		// The branch RS always has 1 slot
		BranchInstruction branchInstruction = (BranchInstruction) instruction;
		BranchStage branchStage = new BranchStage(true, operation, branchInstruction.getAddress());

		IntegerRegister operandRegister1 = IntegerRegister.getRegister(instruction.getOperand1());
		long registerContent1 = operandRegister1.getContent();
		IntegerRegister operandRegister2 = IntegerRegister.getRegister(instruction.getOperand2());
		long registerContent2 = operandRegister2.getContent();

		if (operandRegister1.getQi() == null) {
			branchStage.setVj(registerContent1);
		} else {
			branchStage.setQj(operandRegister1.getQi());
		}

		if (operandRegister2.getQi() == null) {
			branchStage.setVk(registerContent2);
		} else {
			branchStage.setQj(operandRegister2.getQi());
		}

		branchStage.setIssueCycle(cycle + 1);
		branchStage.setInstructionIndex(lastInstructionIndex);

		instruction.setIssue(cycle + 1);

		// System.out.println(branchTable.get(0));

		// TODO: If it breaks, this is the issue
	}
}
