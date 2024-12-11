package units;

import static gui.simulatingStage.Simulate.cycle;
import static units.RegisterFile.getRegister;
import static units.RegisterFile.registerTable;
import static units.RegisterFile.updateRegister;
import static units.stage.Stage.adderTable;
import static units.stage.Stage.loadTable;
import static units.stage.Stage.multiplyTable;
import static units.stage.Stage.reservedAdder;
import static units.stage.Stage.reservedLoad;
import static units.stage.Stage.reservedMultiply;
import static units.stage.Stage.reservedStore;
import static units.stage.Stage.storeTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import gui.setupStage.AddressSetup;
import gui.setupStage.AluSetup;
import gui.setupStage.InstructionSetup;
import instructions.FloatingInstruction;
import instructions.Instruction;
import instructions.IntegerInstruction;
import units.stage.Stage;
import units.stage.addressStage.LoadStage;
import units.stage.addressStage.StoreStage;
import units.stage.aluStage.FloatingAdderStage;
import units.stage.aluStage.FloatingMultiplyStage;

public class instructionUnit {
	public static int lastInstructionIndex = 0;

	public Queue<Instruction> instructionQueue = new LinkedList<>();
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

	public String printQueue() {
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
		} else {
			return "integer";
		}
	}

	private static String getInstructionOperation(Instruction instruction) {
		if (instruction instanceof IntegerInstruction) {
			if (((IntegerInstruction) instruction).getOperation().equals("LW")
					|| ((IntegerInstruction) instruction).getOperation().equals("LD")) {
				return "load";
			} else if (((IntegerInstruction) instruction).getOperation().equals("SW")
					|| ((IntegerInstruction) instruction).getOperation().equals("SD")
					|| ((IntegerInstruction) instruction).getOperation().equals("S.S")
					|| ((IntegerInstruction) instruction).getOperation().equals("S.D")) {
				return "store";
			}
		} else if (instruction instanceof FloatingInstruction) {
			String operation = ((FloatingInstruction) instruction).getOperation();
			if (operation.equals("L.S") || (operation.equals("L.D"))) {
				return "load";
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
						instruction1 = new FloatingInstruction(instruction[0], instruction[1], instruction[2], "",
								InstructionSetup.getMemoryLatency());
					} else if (type.equals("integer")) {
						instruction1 = new IntegerInstruction(instruction[0], instruction[1], instruction[2], "",
								InstructionSetup.getMemoryLatency());
					}
				} else {
					if (type.equals("floating")) {
						instruction1 = new FloatingInstruction(instruction[0], instruction[1], instruction[2], instruction[3],
								InstructionSetup.getFloatingLatency());
					} else if (type.equals("integer")) {
						instruction1 = new IntegerInstruction(instruction[0], instruction[1], instruction[2], instruction[3],
								InstructionSetup.getIntegerLatency());
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
		String operation = getInstructionOperation(instruction);
		if (getInstructionOperation(instruction).equals("load") && reservedLoad < AddressSetup.getLoadSize()) {
			// Check if the load must wait for a store
			if (LoadStage.checkAddressClash(instruction)) {
				// Stall the issuing if a clash is detected
				return;
			}
			int firstUnusedIndex = Stage.getFirstEmptySlot(loadTable);
			LoadStage loadstage = loadTable.get(firstUnusedIndex);
			// Replace in load RS
			loadstage.setBusy(true);
			loadstage.setAddress(instruction.getOperand1());
			loadstage.setIssueCycle(cycle + 1);
			loadstage.setInstructionIndex(lastInstructionIndex);
			System.out.println(lastInstructionIndex);
			loadTable.remove(firstUnusedIndex);
			loadTable.add(firstUnusedIndex, loadstage);
			// Update RF Dependency
			updateRegister(instruction.getDestination(), loadstage);
			// Update Instruction Table Entry
			instructionTable.remove(lastInstructionIndex);
			instruction.setIssue(cycle + 1);
			instructionTable.add(lastInstructionIndex, instruction);
			reservedLoad++;
		} else if (getInstructionOperation(instruction).equals("store") && reservedStore < AddressSetup.getStoreSize()) {
			// check for if the store must wait for a load or a store
			if (StoreStage.checkAddressClash(instruction)) {
				// Stall the issuing if a clash is detected
				return;
			}
			int firstUnusedIndex = Stage.getFirstEmptySlot(storeTable);
			StoreStage storeStage = storeTable.get(firstUnusedIndex);
			// Replace in load RS
			storeStage.setBusy(true);
			storeStage.setAddress(instruction.getOperand1());
			RegisterFile destinationRegister = getRegister(instruction.getOperand2());
			float destinationValue = destinationRegister.getContent();
			if (destinationRegister.getQi() == null) {
				storeStage.setV("" + destinationValue);
			} else {
				storeStage.setQ(destinationRegister.getQi());
			}
			storeStage.setIssueCycle(cycle + 1);
			storeStage.setInstructionIndex(lastInstructionIndex);
			storeTable.remove(firstUnusedIndex);
			storeTable.add(firstUnusedIndex, storeStage);
			// Update Instruction Table Entry
			instructionTable.remove(lastInstructionIndex);
			instruction.setIssue(cycle + 1);
			instructionTable.add(lastInstructionIndex, instruction);
			reservedStore++;
		} else if (operation.equals("ADD") || operation.equals("SUB") && reservedAdder < AluSetup.getFloatingAdder()) {
			FloatingAdderStage adderStage = adderTable.get(reservedAdder);
			adderStage.setBusy(true);
			adderStage.setOp(operation);
			RegisterFile operandRegister1 = getRegister(instruction.getOperand2());
			float operandValue1 = operandRegister1.getContent();
			if (operandRegister1.getQi() == null) {
				adderStage.setVj(operandValue1);
			} else {
				adderStage.setQj(operandRegister1.getQi());
			}
			RegisterFile operandRegister2 = getRegister(instruction.getOperand2());
			float operandValue2 = operandRegister2.getContent();
			if (operandRegister2.getQi() == null) {
				adderStage.setVk(operandValue2);
			} else {
				adderStage.setQk(operandRegister2.getQi());
			}
			adderStage.setIssueCycle(cycle + 1);
			adderStage.setInstructionIndex(lastInstructionIndex);
			RegisterFile destinationRegister = getRegister(instruction.getDestination());
			updateRegister(instruction.getDestination(), adderStage);
			adderTable.remove(reservedAdder);
			instruction.setIssue(cycle + 1);
			adderTable.add(reservedAdder, adderStage);
			reservedAdder++;
		} else if (operation.equals("MUL") || operation.equals("DIV") && reservedMultiply < AluSetup.getFloatingAdder()) {
			FloatingMultiplyStage multiplyStage = multiplyTable.get(reservedMultiply);
			multiplyStage.setBusy(true);
			multiplyStage.setOp(operation);
			RegisterFile operandRegister1 = getRegister(instruction.getOperand2());
			float operandValue1 = operandRegister1.getContent();
			if (operandRegister1.getQi() == null) {
				multiplyStage.setVj(operandValue1);
			} else {
				multiplyStage.setQj(operandRegister1.getQi());
			}
			RegisterFile operandRegister2 = getRegister(instruction.getOperand2());
			float operandValue2 = operandRegister2.getContent();
			if (operandRegister2.getQi() == null) {
				multiplyStage.setVk(operandValue2);
			} else {
				multiplyStage.setQk(operandRegister2.getQi());
			}
			multiplyStage.setIssueCycle(cycle + 1);
			multiplyStage.setInstructionIndex(lastInstructionIndex);
			RegisterFile destinationRegister = getRegister(instruction.getDestination());
			updateRegister(instruction.getDestination(), multiplyStage);
			adderTable.remove(reservedMultiply);
			instruction.setIssue(cycle + 1);
			multiplyTable.add(reservedMultiply, multiplyStage);
			reservedMultiply++;
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

		// Update RF and RS with the first stage from the queue
		if (writebackQueue.size() != 0) {
			Stage busWriter = writebackQueue.remove();
			// Update instruction table
			Instruction instruction = instructionTable.get(busWriter.getInstructionIndex());
			instruction.setWriteResult(cycle + 1);
			instructionTable.set(busWriter.getInstructionIndex(), instruction);

			float value;

			if (busWriter instanceof LoadStage) {
				value = ((LoadStage) busWriter).produce();
			} else if (busWriter instanceof FloatingAdderStage) {
				value = ((FloatingAdderStage) busWriter).produce();
			} else {
				value = ((FloatingMultiplyStage) busWriter).produce();
			}

			for (int i = 0; i < registerTable.size(); i++) {
				RegisterFile register = registerTable.get(i);
				// Check the name of the consumed stage
				if (busWriter.equals(register.getQi())) {
					register.setQi(null);
					register.setContent(value);
				}
			}

			for (int i = 0; i < storeTable.size(); i++) {

			}

			for (int i = 0; i < adderTable.size(); i++) {

			}

			for (int i = 0; i < multiplyTable.size(); i++) {

			}

		}
	}
}