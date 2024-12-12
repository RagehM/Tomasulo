package units.stage.aluStage;

import static gui.simulatingStage.Simulate.cycle;
import instructions.BranchInstruction;
import instructions.Instruction;
import units.IntegerRegister;
import static units.instructionUnit.lastInstructionIndex;

public class BranchStage extends AluStage {
	private String stage;

	public void setAddress(int address) {
		this.address = address;
	}

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
		return ((this.stage + getOp() + getVj() + getVk() + getQj() + getQk() + address)+" " +this.busy+" "+ this.address);
	}

	public boolean produce() {
		if(this.getOp().contains("BNE")){
			if((this.getVj()!=this.getVk())){
				return true;
			}
		}else{
			if((this.getVj()==this.getVk())){
				return true;
			}
		}
		return false;
	}


	public static void dispatchBranch(Instruction instruction, String operation) {
		// Branch stalls the whole pipeline until complete, therefore, no need to check
		// for available slots for dispatch
		// The branch RS always has 1 slot
		BranchInstruction branchInstruction = (BranchInstruction) instruction;
		BranchStage branchStage = branchTable.get(0);

		if(branchStage.getBusy()){
			return;
		}
		branchStage.setBusy(true);
		branchStage.setAddress(branchInstruction.getAddress());

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


		System.out.println(branchTable.toString());
		System.out.println(branchStage.toString());
		branchTable.set(0, branchStage);
		// TODO: If it breaks, this is the issue
	}
}
