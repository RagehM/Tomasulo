package units.stage.aluStage;

import static gui.simulatingStage.Simulate.cycle;
import static units.instructionUnit.lastInstructionIndex;

import instructions.BranchInstruction;
import instructions.Instruction;
import units.IntegerRegister;

public class BranchStage extends AluStage {

	private int address;

	public BranchStage(boolean busy, String op, int address) {
		super(busy, op, 0, 0, null, null);
		this.address = address;
		if (branchTable.size() != 0)
			branchTable.set(0, this);
		else
			branchTable.add(this);
	}

	public int getAddress() {
		return address;
	}

	public static void displatchBranch(Instruction instruction, String operation) {
		// Branch stalls the whole pipeline until complete, therefore, no need to check
		// for available slots for dispatch
		// The branch RS always has 1 slot
		BranchInstruction branchInstruction = (BranchInstruction) instruction;
		BranchStage branchStage = new BranchStage(true, operation, branchInstruction.getAddress());

		IntegerRegister operandRegister1 = IntegerRegister.getRegister(instruction.getOperand1());
		int registerContent1 = operandRegister1.getContent();
		IntegerRegister operandRegister2 = IntegerRegister.getRegister(instruction.getOperand2());
		int registerContent2 = operandRegister2.getContent();

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

		// TODO: If it breaks, this is the issue
	}
}
