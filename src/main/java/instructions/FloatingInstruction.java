package instructions;

public class FloatingInstruction extends Instruction {

//  public enum Operation {
//    ADD_D,
//    SUB_D,
//    MUL_D,
//    DIV_D,
//    L_S,
//    S_S,
//    L_D,
//    S_D
//  }
  private String operation;

  public String getOperation() {
    return operation;
  }

  public FloatingInstruction(String operation, String destination, String operand1, String operand2, int latency) {
    super(InstructionType.FLOATING, destination, operand1, operand2, latency);
    this.operation = operation;
  }

  public String toString() {
    return operation + " " + super.toString();
  }
}

