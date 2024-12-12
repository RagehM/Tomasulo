package units;

import static gui.simulatingStage.Simulate.cycle;
import static units.FloatRegister.floatRegisterTable;
import static units.IntegerRegister.integerRegisterTable;
import static units.stage.Stage.adderTable;
import static units.stage.Stage.branchTable;
import static units.stage.Stage.integerTable;
import static units.stage.Stage.loadTable;
import static units.stage.Stage.multiplyTable;
import static units.stage.Stage.reservedAdder;
import static units.stage.Stage.reservedInteger;
import static units.stage.Stage.reservedLoad;
import static units.stage.Stage.reservedMultiply;
import static units.stage.Stage.reservedStore;
import static units.stage.Stage.storeTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import gui.setupStage.AddressSetup;
import gui.setupStage.AluSetup;
import gui.setupStage.InstructionSetup;
import instructions.BranchInstruction;
import instructions.FloatingInstruction;
import instructions.Instruction;
import instructions.IntegerInstruction;
import units.stage.Stage;
import units.stage.addressStage.LoadStage;
import units.stage.addressStage.StoreStage;
import units.stage.aluStage.BranchStage;
import units.stage.aluStage.FloatingAdderStage;
import units.stage.aluStage.FloatingMultiplyStage;
import units.stage.aluStage.IntegerStage;

public class instructionUnit {
	public static int lastInstructionIndex = 0;

	public static Queue<Instruction> instructionQueue = new LinkedList<>();
	public static ArrayList<Instruction> instructionTable = new ArrayList<Instruction>();

	private static PriorityQueue<Stage> writebackQueue = new PriorityQueue<Stage>();

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

	public static boolean isStalling  = false;

	public static String printQueue() {
		String result = "";
		for (Instruction instruction : instructionQueue) {
			result += instruction.toString() + "\n";
		}
		return result;
	}

	private String getInstructionType(String operation) {
		if (operation.equals("ADD.D") || operation.equals("SUB.D") || operation.equals("MUL.D") || operation.equals("DIV.D")
				|| operation.equals("L.S") || operation.equals("S.S") || operation.equals("S.D") || operation.equals("L.D")
				|| operation.equals("ADD.S") || operation.equals("SUB.S") || operation.equals("MUL.S")
				|| operation.equals("DIV.S")) {
			return "floating";
		} else if (operation.equals("BNE") || operation.equals("BEQ")) {
			return "branch";
		} else {
			return "integer";
		}
	}

	public static String getInstructionOperation(Instruction instruction) {
		if (instruction instanceof IntegerInstruction) {
			String operation = ((IntegerInstruction) instruction).getOperation();
			if (operation.equals("LW") || (operation.equals("LD"))) {
				return operation;
			} else if (operation.equals("SW") || operation.equals("SD") || operation.equals("S.S")
					|| operation.equals("S.D")) {
				return "store";
			} else if (operation.equals("DADDI") || operation.equals("DSUBI")) {
				return operation;
			}
		} else if (instruction instanceof FloatingInstruction) {
			String operation = ((FloatingInstruction) instruction).getOperation();
			if (operation.equals("L.S") || (operation.equals("L.D"))) {
				return operation;
			} else if (operation.equals("SW") || operation.equals("SD") || operation.equals("S.S")
					|| operation.equals("S.D")) {
				return "store";
			}
			if (operation.equals("ADD.D") || operation.equals("ADD.S")) {
				return "ADD";
			}
			if (operation.equals("SUB.D") || operation.equals("SUB.S")) {
				return "SUB";
			}
			if (operation.equals("MUL.D") || operation.equals("MUL.S")) {
				return "MUL";
			}
			if (operation.equals("DIV.D") || operation.equals("DIV.S")) {
				return "DIV";
			}
		} else if (instruction instanceof BranchInstruction) {
			return ((BranchInstruction) instruction).getOperation();
		}
		return "not found";
	}

	public void parse() {
		HashMap<String, Integer> labels = new HashMap<String, Integer>();
		String filePath = "./src/main/java/instructions.txt";
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] instruction = line.split(" ");
				String type = getInstructionType(instruction[0]);
				Instruction instruction1 = null;
				if (instruction.length == 3) {
					if (type.equals("floating")) {
						instruction1 = new FloatingInstruction(instruction[0], instruction[1], instruction[2], "",
								InstructionSetup.getMemoryLatency());
					} else if (type.equals("integer")) {
						instruction1 = new IntegerInstruction(instruction[0], instruction[1], instruction[2], "",
								InstructionSetup.getMemoryLatency());
					}
				} else {
					if (instruction[0].contains(":")) {
						type = getInstructionType(instruction[1]);
						labels.put(instruction[0].substring(0, instruction[0].length() - 1), instructionTable.size());
						System.out.println(labels.toString());
						if (instruction.length == 4) {
							if (type.equals("floating")) {
								instruction1 = new FloatingInstruction(instruction[1], instruction[2], instruction[3], "",
										InstructionSetup.getMemoryLatency());
							} else if (type.equals("integer")) {
								instruction1 = new IntegerInstruction(instruction[1], instruction[2], instruction[3], "",
										InstructionSetup.getMemoryLatency());
							}
						} else {
							if (type.equals("floating")) {
								instruction1 = new FloatingInstruction(instruction[1], instruction[2], instruction[3], instruction[4],
										InstructionSetup.getFloatingLatency());
							} else if (type.equals("integer")) {
								instruction1 = new IntegerInstruction(instruction[1], instruction[2], instruction[3], instruction[4],
										InstructionSetup.getIntegerLatency());
							}
						}
					} else {
						if (type.equals("floating")) {
							instruction1 = new FloatingInstruction(instruction[0], instruction[1], instruction[2], instruction[3],
									InstructionSetup.getFloatingLatency());
						} else if (type.equals("integer")) {
							instruction1 = new IntegerInstruction(instruction[0], instruction[1], instruction[2], instruction[3],
									InstructionSetup.getIntegerLatency());
						} else if (type.equals("branch")) {
							instruction1 = new BranchInstruction(instruction[0], instruction[1], instruction[2],
									labels.get(instruction[3]), InstructionSetup.getIntegerLatency());
						}
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

		if (Stage.getFirstEmptySlot(branchTable) == -1 ) {
			return;
		}

		Instruction instruction = (Instruction) instructionTable.get(lastInstructionIndex);
		String operation = getInstructionOperation(instruction);

		if ((operation.equals("LW") || (operation.equals("LD")) || operation.equals("L.S") || (operation.equals("L.D")))
				&& reservedLoad < AddressSetup.getLoadSize()) {
			LoadStage.dispatchLoad(instruction);
		} 
		else if (operation.equals("store") && reservedStore < AddressSetup.getStoreSize()) {
			StoreStage.dispatchStore(instruction);
		} 
		else if ((operation.equals("ADD") || operation.equals("SUB")) && reservedAdder < AluSetup.getFloatingAdder()) {
			FloatingAdderStage.dispatchAdder(instruction, operation);
		} 
		else if ((operation.equals("MUL") || operation.equals("DIV")) && reservedMultiply < AluSetup.getFloatingMul()) {
			FloatingMultiplyStage.dispatchMultiply(instruction, operation);
		} 
		else if ((operation.equals("BEQ") || operation.equals("BNE"))) {
			BranchStage.dispatchBranch(instruction, operation);
		} 
		else if ((operation.equals("DADDI") || operation.equals("DSUBI"))
				&& reservedInteger < AluSetup.getIntegerAdder()) {
			IntegerStage.dispatchInteger(instruction, operation);
		} 
		else {
			return;
		}
		lastInstructionIndex++;
	}

	public static void execute() {
		// Executing loops over every cell of every stage
		// Execution as a whole doesn't stall for any reason, but execution for one
		// station can store
		// Latencies for each instruction type is taken as an input at the start of the
		// program

		// Load Loop
		for (int i = 0; i < Stage.loadTable.size(); i++) {
			Stage tmp = Stage.loadTable.get(i);
			if (tmp.getBusy()) {
				// If it is busy, increment its execution counter
				tmp.setExecutionCycle(tmp.getExecutionCycle() + 1);
				if (!(tmp.getExecutionCycle() > InstructionSetup.getMemoryLatency())) {
					Instruction instruction = instructionTable.get(tmp.getInstructionIndex());
					instruction.setExecutionComplete(cycle + 1);
					instructionTable.set(tmp.getInstructionIndex(), instruction);
				}
			}
		}

		// Store Loop
		for (int i = 0; i < Stage.storeTable.size(); i++) {
			StoreStage tmp = Stage.storeTable.get(i);
			if (tmp.getBusy() && tmp.getQ() == null) {
				// If it is busy, increment its execution counter
				tmp.setExecutionCycle(tmp.getExecutionCycle() + 1);
				if (!(tmp.getExecutionCycle() > InstructionSetup.getMemoryLatency())) {
					Instruction instruction = instructionTable.get(tmp.getInstructionIndex());
					instruction.setExecutionComplete(cycle + 1);
					instructionTable.set(tmp.getInstructionIndex(), instruction);
				}
			}
		}

		// Adder Loop
		for (int i = 0; i < Stage.adderTable.size(); i++) {
			FloatingAdderStage tmp = Stage.adderTable.get(i);
			if (tmp.getBusy() && tmp.getQj() == null && tmp.getQk() == null) {
				// If it is busy, increment its execution counter
				tmp.setExecutionCycle(tmp.getExecutionCycle() + 1);
				if (!(tmp.getExecutionCycle() > InstructionSetup.getFloatingLatency())) {
					Instruction instruction = instructionTable.get(tmp.getInstructionIndex());
					instruction.setExecutionComplete(cycle + 1);
					instructionTable.set(tmp.getInstructionIndex(), instruction);
				}
			}
		}

		// Multiplier Loop
		for (int i = 0; i < Stage.multiplyTable.size(); i++) {
			FloatingMultiplyStage tmp = Stage.multiplyTable.get(i);
			if (tmp.getBusy() && tmp.getQj() == null && tmp.getQk() == null) {
				// If it is busy, increment its execution counter
				tmp.setExecutionCycle(tmp.getExecutionCycle() + 1);
				if (!(tmp.getExecutionCycle() > InstructionSetup.getFloatingLatency())) {
					Instruction instruction = instructionTable.get(tmp.getInstructionIndex());
					instruction.setExecutionComplete(cycle + 1);
					instructionTable.set(tmp.getInstructionIndex(), instruction);
				}
			}
		}
		// Branch Loop
		for (int i = 0; i < branchTable.size(); i++) {
			BranchStage tmp = Stage.branchTable.get(i);
			if (tmp.getBusy() && tmp.getQj() == null && tmp.getQk() == null) {
				// If it is busy, increment its execution counter
				tmp.setExecutionCycle(tmp.getExecutionCycle() + 1);
				if (!(tmp.getExecutionCycle() > InstructionSetup.getIntegerLatency())) {
					Instruction instruction = instructionTable.get(tmp.getInstructionIndex());
					instruction.setExecutionComplete(cycle + 1);
					instructionTable.set(tmp.getInstructionIndex(), instruction);
				}
				else{
					if(tmp.produce()){
						tmp.setExecutionCycle(0);
						lastInstructionIndex = tmp.getAddress();
					}
					tmp.setBusy(false);
				}
			}
		}

		// Integer Loop
		for(int i = 0; i < integerTable.size(); i++) {
			IntegerStage tmp = integerTable.get(i);
			if(tmp.getBusy() && tmp.getQj() == null) {
				tmp.setExecutionCycle(tmp.getExecutionCycle() + 1);
				if(!(tmp.getExecutionCycle() > InstructionSetup.getIntegerLatency())) {
					Instruction instruction = instructionTable.get(tmp.getInstructionIndex());
					instruction.setExecutionComplete(cycle + 1);
					instructionTable.set(tmp.getInstructionIndex(), instruction);
				}
			}
		}
	}

	public static void writeBack() {
		for (int i = 0; i < loadTable.size(); i++) {
			Stage tmp = Stage.loadTable.get(i);
			if (tmp.getExecutionCycle() == InstructionSetup.getMemoryLatency() + 1) {
				// Do writeback method that takes in the value and the RS name & code, what this
				// does is add it to writeback queue
				writebackQueue.add(tmp);
			}
		}

		for (int i = 0; i < storeTable.size(); i++) {
			StoreStage tmp = Stage.storeTable.get(i);
			if (tmp.getExecutionCycle() == InstructionSetup.getMemoryLatency() + 1) {
				// Do writeback method that takes in the value and the RS name & code, what this
				// does is add it to writeback queue

				// Stores have their own buffer
			}
		}

		for (int i = 0; i < adderTable.size(); i++) {
			FloatingAdderStage tmp = Stage.adderTable.get(i);
			if (tmp.getExecutionCycle() == InstructionSetup.getFloatingLatency() + 1) {
				// Do writeback method that takes in the value and the RS name & code, what this
				// does is add it to writeback queue
				writebackQueue.add(tmp);
			}
		}

		for (int i = 0; i < multiplyTable.size(); i++) {
			FloatingMultiplyStage tmp = Stage.multiplyTable.get(i);
			if (tmp.getExecutionCycle() == InstructionSetup.getFloatingLatency() + 1) {
				// Do writeback method that takes in the value and the RS name & code, what this
				// does is add it to writeback queue
				writebackQueue.add(tmp);
			}
		}

		for(int i = 0; i < integerTable.size(); i++) {
			IntegerStage tmp = integerTable.get(i);
			if(tmp.getExecutionCycle() == InstructionSetup.getIntegerLatency() + 1) {
				writebackQueue.add(tmp);
			}
		}

		// Update RF and RS with the first stage from the queue
		if (writebackQueue.size() != 0) {
			Stage busWriter = writebackQueue.remove();
			busWriter.setBusy(false);
			busWriter.setExecutionCycle(busWriter.getExecutionCycle() + 10);
			// Update instruction table
			Instruction instruction = instructionTable.get(busWriter.getInstructionIndex());
			instruction.setWriteResult(cycle + 1);
			instructionTable.set(busWriter.getInstructionIndex(), instruction);

			double floatValue = Float.MAX_VALUE;
			long integerValue = Long.MAX_VALUE;
			if (busWriter instanceof LoadStage) {
				floatValue = ((LoadStage) busWriter).produce();
				reservedLoad--;
			} 
			else if (busWriter instanceof FloatingAdderStage) {
				floatValue = ((FloatingAdderStage) busWriter).produce();
				reservedAdder--;
			} 
			else if(busWriter instanceof FloatingMultiplyStage) {
				floatValue = ((FloatingMultiplyStage) busWriter).produce();
				reservedMultiply--;
			}
			else {
				integerValue = ((IntegerStage) busWriter).produce();
				reservedInteger--;
			}

			// // set the output of integer stage to be written on the bus

			// if((busWriter instanceof IntegerStage)) {

			// }

			for(int i = 0; i < integerRegisterTable.size(); i++) {
				System.out.println(integerRegisterTable);
				IntegerRegister register = integerRegisterTable.get(i);
				// Check the name of the consumed stage
				if(busWriter.equals(register.getQi())) {
					register.setQi(null);
					register.setContent(integerValue);
				}
			}

			for (int i = 0; i < floatRegisterTable.size(); i++) {
				FloatRegister register = floatRegisterTable.get(i);
				// Check the name of the consumed stage
				if (busWriter.equals(register.getQi())) {
					register.setQi(null);
					register.setContent(floatValue);
				}
			}

			for (int i = 0; i < storeTable.size(); i++) {
				StoreStage stage = storeTable.get(i);
				if (busWriter.equals(stage.getQ())) {
					stage.setQ(null);
					stage.setV((int) floatValue);
				}
			}

			for (int i = 0; i < adderTable.size(); i++) {
				FloatingAdderStage stage = adderTable.get(i);
				if (busWriter.equals(stage.getQj())) {
					stage.setQj(null);
					stage.setVj(floatValue);
				}
				if (busWriter.equals(stage.getQk())) {
					stage.setQk(null);
					stage.setVk(floatValue);
				}
			}

			for (int i = 0; i < multiplyTable.size(); i++) {
				FloatingMultiplyStage stage = multiplyTable.get(i);
				if (busWriter.equals(stage.getQj())) {
					stage.setQj(null);
					stage.setVj(floatValue);
				}
				if (busWriter.equals(stage.getQk())) {
					stage.setQk(null);
					stage.setVk(floatValue);
				}
			}

			for(int i = 0; i < branchTable.size(); i++) {
				BranchStage stage = branchTable.get(i);
				if(busWriter.equals(stage.getQj())) {
					stage.setQj(null);
					stage.setVj(integerValue);
				}
				if(busWriter.equals(stage.getQk())) {
					stage.setQk(null);
					stage.setVk(integerValue);
				}
			}

			for(int i = 0; i < integerTable.size(); i++) {
				IntegerStage stage = integerTable.get(i);
				if(busWriter.equals(stage.getQj())) {
					stage.setQj(null);
					stage.setVj(integerValue);
				}
			}

		}
	}
}