package units;
import gui.setupStage.InstructionSetup;
import instructions.FloatingInstruction;
import instructions.Instruction;
import instructions.IntegerInstruction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;
public class instructionUnit {

  private Queue<Instruction> instructionQueue = new LinkedList<>();
  public ArrayList instructionTable = new ArrayList<Instruction>();

  public void addInstruction(Instruction instruction) {
    instructionTable.add(instruction);
  }

  public ArrayList getInstructionTable() {
    return instructionTable;
  }

  public void pushInstruction(Instruction instruction) {
    instructionQueue.add(instruction);
  }

  public Instruction popInstruction() {
    return instructionQueue.poll();
  }

  public int getSize() {
    return instructionQueue.size();
  }

  public String printQueue() {
    String result = "";
    for (Instruction instruction : instructionQueue) {
      result += instruction.toString() + "\n";
    }
    return result;
  }

  private String getInstructionType(String operation) {
    if (operation.equals("ADD.D") || operation.equals("SUB.D") || operation.equals("MUL.D") || operation.equals("DIV.D") || operation.equals("L.S") || operation.equals("S.S") || operation.equals("S.D") || operation.equals("L.D") || operation.equals("ADD.S") || operation.equals("SUB.S") || operation.equals("MUL.S") || operation.equals("DIV.S")) {
      return "floating";
    } else {
      return "integer";
    }
  }

  public void parse() {
    String filePath = "./src/main/java/instructions.txt";
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] instruction = line.split(" ");
        String type = getInstructionType(instruction[0]);
        Instruction instruction1 = null;
        if (instruction.length == 3) {
          if (type.equals("floating")) {
            instruction1 = new FloatingInstruction(instruction[0], instruction[1], instruction[2], "", InstructionSetup.getMemoryLatency());
          } else if (type.equals("integer")) {
            instruction1 = new IntegerInstruction(instruction[0], instruction[1], instruction[2], "", InstructionSetup.getMemoryLatency());
          }
        } else {
          if (type.equals("floating")) {
            instruction1 = new FloatingInstruction(instruction[0], instruction[1], instruction[2], instruction[3], InstructionSetup.getFloatingLatency());
          } else if (type.equals("integer")) {
            instruction1 = new IntegerInstruction(instruction[0], instruction[1], instruction[2], instruction[3], InstructionSetup.getIntegerLatency());
          }
        }
        this.pushInstruction(instruction1);
        this.addInstruction(instruction1);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}