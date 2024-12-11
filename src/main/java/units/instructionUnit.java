package units;

import gui.setupStage.AddressSetup;
import gui.setupStage.InstructionSetup;
import instructions.FloatingInstruction;
import instructions.Instruction;
import instructions.IntegerInstruction;
import units.stage.Stage;
import units.stage.addressStage.LoadStage;
import units.stage.addressStage.StoreStage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;

import static gui.simulatingStage.Simulate.cycle;
import static units.RegisterFile.updateRegister;
import static units.stage.Stage.*;

public class instructionUnit {
  public static int lastInstructionIndex = 0;

  public Queue<Instruction> instructionQueue = new LinkedList<>();
  public static ArrayList<Instruction> instructionTable = new ArrayList<Instruction>();

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

  private static String getInstructionOperation(Instruction instruction) {
    if(instruction instanceof IntegerInstruction) {
      if(((IntegerInstruction) instruction).getOperation().equals("LW") ||((IntegerInstruction) instruction).getOperation().equals("LD") ||((IntegerInstruction) instruction).getOperation().equals("L.S") || ((IntegerInstruction) instruction).getOperation().equals("L.D")) {
        return "load";
      }
      if(((IntegerInstruction) instruction).getOperation().equals("SW") ||((IntegerInstruction) instruction).getOperation().equals("SD") ||((IntegerInstruction) instruction).getOperation().equals("S.S") || ((IntegerInstruction) instruction).getOperation().equals("S.D")) {
        return "store";
      }
    }
    return "not found";
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

  public static void dispatch() {
    Instruction instruction = (Instruction) instructionTable.get(lastInstructionIndex);
    if(getInstructionOperation(instruction).equals("load") && reservedLoad < AddressSetup.getLoadSize()) {
      int firstUnusedIndex = Stage.getFirstEmptySlot(loadTable);
      LoadStage loadstage = loadTable.get(firstUnusedIndex);
      loadstage.setBusy(true);
      loadstage.setAddress(instruction.getOperand1());
      loadTable.remove(firstUnusedIndex);
      loadTable.add(firstUnusedIndex, loadstage);
      updateRegister(instruction.getDestination(), loadstage);
      instructionTable.remove(lastInstructionIndex);
      instruction.setIssue(cycle + 1);
      instructionTable.add(lastInstructionIndex, instruction);
      reservedLoad++;
    }
    else if(getInstructionOperation(instruction).equals("store") && reservedStore < AddressSetup.getStoreSize()) {
      int firstUnusedIndex = Stage.getFirstEmptySlot(storeTable);
      StoreStage storeStage = storeTable.get(firstUnusedIndex);
      storeStage.setBusy(true);
      storeStage.setAddress(instruction.getOperand1());
      storeTable.remove(firstUnusedIndex);
      storeTable.add(firstUnusedIndex, storeStage);
      reservedStore++;
    }
    lastInstructionIndex++;
  }
}