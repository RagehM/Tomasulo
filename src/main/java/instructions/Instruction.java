package instructions;

public class Instruction {

	private final InstructionType type;
	private String destination;
	private String operand1;
	private String operand2 = "";
	private final int latency;
	private int issue;
	private int executionComplete;
	private int writeResult;

	public int getIssue() {
		return issue;
	}

	public void setIssue(int issue) {
		this.issue = issue;
	}

	public int getExecutionComplete() {
		return executionComplete;
	}

	public void setExecutionComplete(int executionComplete) {
		this.executionComplete = executionComplete;
	}

	public int getWriteResult() {
		return writeResult;
	}

	public void setWriteResult(int writeResult) {
		this.writeResult = writeResult;
	}

	public enum InstructionType {
		INTEGER, FLOATING, BRANCH
	}

	public String getOperand2() {
		return operand2;
	}

	public void setOperand2(String operand2) {
		this.operand2 = operand2;
	}

	public String getOperand1() {
		return operand1;
	}

	public void setOperand1(String operand1) {
		this.operand1 = operand1;
	}

	public InstructionType getCategory() {
		return type;
	}

	public String getDestination() {
		return destination;
	}

	public int getLatency() {
		return latency;
	}

	public Instruction(InstructionType type, String destination, String operand1, String operand2, int latency) {
		this.type = type;
		this.destination = destination;
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.latency = latency;
		this.issue = 0;
		this.executionComplete = 0;
		this.writeResult = 0;
	}

	public String toString() {
		return destination + " " + operand1 + " " + operand2 + " " + +issue + " " + executionComplete + " " + writeResult
				+ " " + type;
	}
}
