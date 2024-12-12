package instructions;

public class BranchInstruction extends Instruction {
	private int address;
	private String operation;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public BranchInstruction(String operation, String operand1, String operand2, int destination, int latency) {
		super(InstructionType.BRANCH, destination + "", operand1, operand2, latency);
		this.address = destination;
		this.operation = operation;
	}

	public int getAddress() {
		return address;
	}
}
