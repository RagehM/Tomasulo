package instructions;

public class IntegerInstruction extends Instruction {

//  public enum Operation {
//    LW,
//    LD,
//    SW,
//    SD,
//    ADDI,
//    SUBI
//  }
  //private final Operation operation;
  private String operation;

  public String getOperation() {
    return operation;
  }

  public IntegerInstruction(String operation, String destination, String operand1, String operand2, int latency) {
    super(InstructionType.INTEGER, destination, operand1, operand2, latency);
    this.operation = operation;
  }

  public String toString() {
    return operation + " " + super.toString();
  }

}

