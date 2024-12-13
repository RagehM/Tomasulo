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
import gui.setupStage.CacheSetup;
import gui.setupStage.InstructionSetup;
import instructions.BranchInstruction;
import instructions.FloatingInstruction;
import instructions.Instruction;
import instructions.IntegerInstruction;
import units.cache.Cache;
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
	private static PriorityQueue<StoreStage> storeQueue = new PriorityQueue<StoreStage>();

	private static boolean branchStall = false;

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

	public static boolean isStalling = false;

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
			} else if (operation.equals("SW") || operation.equals("SD")) {
				return operation;
			} else if (operation.equals("DADDI") || operation.equals("DSUBI")) {
				return operation;
			}
		} else if (instruction instanceof FloatingInstruction) {
			String operation = ((FloatingInstruction) instruction).getOperation();
			if (operation.equals("L.S") || (operation.equals("L.D"))) {
				return operation;
			} else if (operation.equals("S.S") || operation.equals("S.D")) {
				return operation;
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

	public static String getLoadOrStore(String operation) {
		if (operation.equals("LW") || operation.equals("LD") || operation.equals("L.S") || operation.equals("L.D")) {
			return "load";
		} else if (operation.equals("SW") || operation.equals("SD") || operation.equals("S.S") || operation.equals("S.D")) {
			return "store";
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

					String operation = getLoadOrStore(instruction[0]);
					if (operation.equals("load")) {
						if (type.equals("floating")) {
							instruction1 = new FloatingInstruction(instruction[0], instruction[1], instruction[2], "",
									CacheSetup.getLatency());
						} else if (type.equals("integer")) {
							instruction1 = new IntegerInstruction(instruction[0], instruction[1], instruction[2], "",
									CacheSetup.getLatency());
						}
					} else {
						if (type.equals("floating")) {
							instruction1 = new FloatingInstruction(instruction[0], instruction[2], instruction[1], "",
									CacheSetup.getLatency());
						} else if (type.equals("integer")) {
							instruction1 = new IntegerInstruction(instruction[0], instruction[2], instruction[1], "",
									CacheSetup.getLatency());
						}
					}
				} else {
					if (instruction[0].contains(":")) {
						type = getInstructionType(instruction[1]);
						labels.put(instruction[0].substring(0, instruction[0].length() - 1), instructionTable.size());
						String operation = getLoadOrStore(instruction[1]);
						if (instruction.length == 4) {
							if (operation.equals("load")) {
								if (type.equals("floating")) {
									instruction1 = new FloatingInstruction(instruction[1], instruction[2], instruction[3], "",
											CacheSetup.getLatency());
								} else if (type.equals("integer")) {
									instruction1 = new IntegerInstruction(instruction[1], instruction[2], instruction[3], "",
											CacheSetup.getLatency());
								}
							} else {
								if (type.equals("floating")) {
									instruction1 = new FloatingInstruction(instruction[1], instruction[3], instruction[2], "",
											CacheSetup.getLatency());
								} else if (type.equals("integer")) {
									instruction1 = new IntegerInstruction(instruction[1], instruction[3], instruction[2], "",
											CacheSetup.getLatency());
								}
							}
						} else {
							if (type.equals("floating")) {
								instruction1 = new FloatingInstruction(instruction[1], instruction[2], instruction[3], instruction[4],
										InstructionSetup.getFloatingLatency());
							} else if (type.equals("integer")) {
								instruction1 = new IntegerInstruction(instruction[1], instruction[2], instruction[3], instruction[4],
										InstructionSetup.getIntegerLatency());
							} else if (type.equals("branch")) {
								int address = 0;
								if (!labels.containsKey(instruction[4])) {
									address = Integer.parseInt(instruction[4]);
								} else {
									address = labels.get(instruction[4]);
								}
								instruction1 = new BranchInstruction(instruction[1], instruction[2], instruction[3], address,
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
							int address = 0;
							if (!labels.containsKey(instruction[3])) {
								address = Integer.parseInt(instruction[3]);
							} else {
								address = labels.get(instruction[3]);
							}
							instruction1 = new BranchInstruction(instruction[0], instruction[1], instruction[2], address,
									InstructionSetup.getIntegerLatency());
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

		if (Stage.getFirstEmptySlot(branchTable) == -1 || branchStall) {
			branchStall = false;
			return;
		}

		Instruction instruction = (Instruction) instructionTable.get(lastInstructionIndex);
		String operation = getInstructionOperation(instruction);

		if ((operation.equals("LW") || (operation.equals("LD")) || operation.equals("L.S") || (operation.equals("L.D")))
				&& reservedLoad < AddressSetup.getLoadSize()) {
			// Check if the load must wait for a store
			// System.out.println(operation);

			if (LoadStage.checkAddressClash(instruction)) {
				// Stall the issuing if a clash is detected
				return;
			}
			LoadStage.dispatchLoad(instruction);
		} else if ((operation.equals("SW") || operation.equals("SD") || operation.equals("S.S") || operation.equals("S.D"))
				&& reservedStore < AddressSetup.getStoreSize()) {
			// check for if the store must wait for a load or a store
			if (StoreStage.checkAddressClash(instruction)) {
				// Stall the issuing if a clash is detected
				return;
			}
			StoreStage.dispatchStore(instruction);
		} else if ((operation.equals("ADD") || operation.equals("SUB")) && reservedAdder < AluSetup.getFloatingAdder()) {
			FloatingAdderStage.dispatchAdder(instruction, operation);
		} else if ((operation.equals("MUL") || operation.equals("DIV")) && reservedMultiply < AluSetup.getFloatingMul()) {
			FloatingMultiplyStage.dispatchMultiply(instruction, operation);
		} else if ((operation.equals("BEQ") || operation.equals("BNE"))) {
			BranchStage.dispatchBranch(instruction, operation);
		} else if ((operation.equals("DADDI") || operation.equals("DSUBI"))
				&& reservedInteger < AluSetup.getIntegerAdder()) {
			IntegerStage.dispatchInteger(instruction, operation);
		} else {
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
			LoadStage tmp = Stage.loadTable.get(i);
			if (tmp.getBusy()) {
				// If it is busy, increment its execution counter
				tmp.setExecutionCycle(tmp.getExecutionCycle() + 1);

				if (!(tmp.getExecutionCycle() > CacheSetup.getLatency())
						|| (tmp.isMiss() && !(tmp.getExecutionCycle() > CacheSetup.getLatency() + CacheSetup.getMissPenalty()))) {

					Instruction instruction = instructionTable.get(tmp.getInstructionIndex());
					instruction.setExecutionComplete(cycle + 1);
					instructionTable.set(tmp.getInstructionIndex(), instruction);

					if (tmp.isMiss() && (tmp.getExecutionCycle() == CacheSetup.getMissPenalty()
							|| (CacheSetup.getMissPenalty() == 0 && tmp.getExecutionCycle() == 1))) {
						// Once penalty is done, get from memory
						String operation = instructionUnit.getInstructionOperation(instruction);
						int numberOfBytes = (operation.equals("LW") || operation.equals("L.S")) ? 4 : 8;
						Cache.loadFromMemoryToCache(Integer.parseInt(tmp.getAddress()), numberOfBytes);

					}
				}
			}
		}

		// Store Loop
		for (int i = 0; i < Stage.storeTable.size(); i++) {
			StoreStage tmp = Stage.storeTable.get(i);
			if (tmp.getBusy() && tmp.getQ() == null) {
				// If it is busy, increment its execution counter
				tmp.setExecutionCycle(tmp.getExecutionCycle() + 1);

				if (!(tmp.getExecutionCycle() > CacheSetup.getLatency())
						|| (tmp.isMiss() && !(tmp.getExecutionCycle() > CacheSetup.getLatency() + CacheSetup.getMissPenalty()))) {

					Instruction instruction = instructionTable.get(tmp.getInstructionIndex());
					instruction.setExecutionComplete(cycle + 1);
					instructionTable.set(tmp.getInstructionIndex(), instruction);

					if (tmp.isMiss() && (tmp.getExecutionCycle() == CacheSetup.getMissPenalty()
							|| (CacheSetup.getMissPenalty() == 0 && tmp.getExecutionCycle() == 1))) {
						// Once penalty is done, get from memory
						String operation = instructionUnit.getInstructionOperation(instruction);
						int numberOfBytes = (operation.equals("SW") || operation.equals("S.S")) ? 4 : 8;
						Cache.loadFromMemoryToCache(Integer.parseInt(tmp.getAddress()), numberOfBytes);
					}
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
			}
		}

		// Integer Loop
		for (int i = 0; i < integerTable.size(); i++) {
			IntegerStage tmp = integerTable.get(i);
			if (tmp.getBusy() && tmp.getQj() == null) {
				tmp.setExecutionCycle(tmp.getExecutionCycle() + 1);
				if (!(tmp.getExecutionCycle() > InstructionSetup.getIntegerLatency())) {
					Instruction instruction = instructionTable.get(tmp.getInstructionIndex());
					instruction.setExecutionComplete(cycle + 1);
					instructionTable.set(tmp.getInstructionIndex(), instruction);
				}
			}
		}
	}

	public static void writeBack() throws Exception {
		BranchStage branchStage = Stage.branchTable.get(0);

		if (branchStage.getExecutionCycle() == InstructionSetup.getIntegerLatency() + 1) {
			if (branchStage.produce()) {
				lastInstructionIndex = branchStage.getAddress();
			}

			Instruction instruction = instructionTable.get(branchStage.getInstructionIndex());
			instruction.setWriteResult(cycle + 1);
			instructionTable.set(branchStage.getInstructionIndex(), instruction);

			branchStage.setExecutionCycle(0);
			branchStage.setBusy(false);

			branchStall = true;
		}

		for (int i = 0; i < loadTable.size(); i++) {
			LoadStage tmp = Stage.loadTable.get(i);
			if ((!tmp.isMiss() && tmp.getExecutionCycle() == CacheSetup.getLatency() + 1)
					|| (tmp.isMiss() && tmp.getExecutionCycle() == CacheSetup.getLatency() + CacheSetup.getMissPenalty() + 1)) {
				// Do writeback method that takes in the value and the RS name & code, what this
				// does is add it to writeback queue

				// If this load is a miss, make sure it doesn't attempt to writeback before the
				// penalty is over
				writebackQueue.add(tmp);
			}
		}

		for (int i = 0; i < storeTable.size(); i++) {
			StoreStage tmp = Stage.storeTable.get(i);
			if ((!tmp.isMiss() && tmp.getExecutionCycle() == CacheSetup.getLatency() + 1)
					|| (tmp.isMiss() && tmp.getExecutionCycle() == CacheSetup.getLatency() + CacheSetup.getMissPenalty() + 1)) {
				// Do writeback method that takes in the value and the RS name & code, what this
				// does is add it to writeback queue

				// Stores have their own buffer
				storeQueue.add(tmp);
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

		for (int i = 0; i < integerTable.size(); i++) {
			IntegerStage tmp = integerTable.get(i);
			if (tmp.getExecutionCycle() == InstructionSetup.getIntegerLatency() + 1) {
				writebackQueue.add(tmp);
			}
		}

		// Update RF and RS with the first stage from the queue
		if (writebackQueue.size() != 0) {
			Stage busWriter = writebackQueue.remove();
			busWriter.setBusy(false);
			// Update instruction table
			Instruction instruction = instructionTable.get(busWriter.getInstructionIndex());
			instruction.setWriteResult(cycle + 1);
			instructionTable.set(busWriter.getInstructionIndex(), instruction);
			String operation = getInstructionOperation(instruction);

			double floatValue = Float.MAX_VALUE;
			long integerValue = Long.MAX_VALUE;

			if (busWriter instanceof LoadStage) {
				if (operation.equals("LW") || operation.equals("LD")) {
					integerValue = (long) ((LoadStage) busWriter).produce();
				} else {
					floatValue = ((LoadStage) busWriter).produce();
				}
				reservedLoad--;
			} else if (busWriter instanceof FloatingAdderStage) {
				floatValue = ((FloatingAdderStage) busWriter).produce();
				reservedAdder--;
			} else if (busWriter instanceof FloatingMultiplyStage) {
				floatValue = ((FloatingMultiplyStage) busWriter).produce();
				reservedMultiply--;
			} else {
				integerValue = ((IntegerStage) busWriter).produce();
				reservedInteger--;
			}

			// // set the output of integer stage to be written on the bus

			// if((busWriter instanceof IntegerStage)) {

			// }

			for (int i = 0; i < integerRegisterTable.size(); i++) {
				// System.out.println(integerRegisterTable);
				IntegerRegister register = integerRegisterTable.get(i);
				// Check the name of the consumed stage
				if (busWriter.equals(register.getQi())) {
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
					Instruction storeInstruction = instructionTable.get(stage.getInstructionIndex());
					String storeOperation = getInstructionOperation(storeInstruction);
					if (storeOperation.equals("SW") || storeOperation.equals("SD")) {
						stage.setV((double) integerValue);
					} else {
						stage.setV(floatValue);
					}
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

			for (int i = 0; i < branchTable.size(); i++) {
				BranchStage stage = branchTable.get(i);
				if (busWriter.equals(stage.getQj())) {
					stage.setQj(null);
					stage.setVj(integerValue);
				}
				if (busWriter.equals(stage.getQk())) {
					stage.setQk(null);
					stage.setVk(integerValue);
				}
			}

			for (int i = 0; i < integerTable.size(); i++) {
				IntegerStage stage = integerTable.get(i);
				if (busWriter.equals(stage.getQj())) {
					stage.setQj(null);
					stage.setVj(integerValue);
				}
			}

		}

		if (storeQueue.size() != 0) {
			StoreStage writeToMemoryStage = storeQueue.remove();
			writeToMemoryStage.setBusy(false);

			Instruction instruction = instructionTable.get(writeToMemoryStage.getInstructionIndex());
			instruction.setWriteResult(cycle + 1);
			instructionTable.set(writeToMemoryStage.getInstructionIndex(), instruction);

			writeToMemoryStage.setExecutionCycle(0);

			Cache.storeToCache(writeToMemoryStage);

			reservedStore--;
		}

	}
}