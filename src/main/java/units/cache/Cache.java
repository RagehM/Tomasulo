package units.cache;

import static gui.setupStage.SetupStage.memory;
import static gui.simulatingStage.Simulate.cycle;
import static units.instructionUnit.getInstructionOperation;
import static units.instructionUnit.instructionTable;
import static units.stage.aluStage.CacheStage.cacheTable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.PriorityQueue;

import gui.setupStage.CacheSetup;
import gui.setupStage.InstructionSetup;
import instructions.Instruction;
import units.stage.addressStage.LoadStage;
import units.stage.addressStage.StoreStage;

public class Cache {
	public static Cache cache = null;
	private static int hitLatency;
	private static int missPenalty;
	private static int cacheSize;
	public static int blockSize;
	public static int blockCount;
	public static PriorityQueue<Block> blocks;

	public void setHitLatency(int hitLatency) {
		Cache.hitLatency = hitLatency;
	}

	public static int getHitLatency() {
		return hitLatency;
	}

	public static int getMissPenalty() {
		return missPenalty;
	}

	public static int getCacheSize() {
		return cacheSize;
	}

	public static int getBlockSize() {
		return blockSize;
	}

	public Cache(int hitLatency, int missPenalty, int cacheSize, int blockSize) throws Exception {
		if (cache != null) {
			return;
		}

		System.out.println("Initializing Cache");

		Cache.hitLatency = hitLatency;
		Cache.missPenalty = missPenalty;
		Cache.cacheSize = cacheSize;
		Cache.blockSize = blockSize;
		if (cacheSize % blockSize != 0) {
			throw new Exception("Cache Cannot be divided into block size of " + blockSize);
		}
		Cache.blockCount = cacheSize / blockSize;

		blocks = new PriorityQueue<Block>();

		for (int i = 0; i < blockCount; i++) {
			Block tmp = new Block(-1);
			tmp.setBlockName("Block " + i);
			blocks.add(tmp);
		}

		cache = this;
	}

	public static Block getBlockWithName(String blockName) {
		ArrayList<Block> cacheBlocks = new ArrayList<Block>(Cache.blocks);

		for (int i = 0; i < cacheBlocks.size(); i++) {
			Block current = cacheBlocks.get(i);
			if (current.getBlockName().equals(blockName)) {
				return current;
			}
		}

		return null;
	}

	public static boolean[] checkAddressAvailability(int address, int numberOfBytes) {
		// returns an array of 2 elements,
		// denoting if the needed word (1st
		// and 2nd if that is the case) is
		// available or not
		// Check each cache block to determine if the target address is within it
		PriorityQueue<Block> tmp = blocks;

		int blockBaseAddress = ((int) (address / blockSize)) * blockSize;
		int lastNeededAddress = address + numberOfBytes - 1;
		int secondBlockBaseAddress = (int) (lastNeededAddress / blockSize) * blockSize;
		boolean[] out = new boolean[2];
		for (int i = 0; i < out.length; i++) {
			out[i] = true;
		}

		// Start by checking the existence of the first block
		if (!blocks.stream().anyMatch(block -> block.getTag() == blockBaseAddress)) {
			out[0] = false;
		}

		if (lastNeededAddress > (blockBaseAddress + blockSize - 1)) {
			if (!blocks.stream().anyMatch(block -> block.getTag() == secondBlockBaseAddress)) {
				out[1] = false;
			}
		}

		return out;
	}

	public static void loadFromMemoryToCache(int address, int numberOfBytes) {
		int blockBaseAddress = ((int) (address / blockSize)) * blockSize;
		int lastNeededAddress = address + numberOfBytes - 1;
		int secondBlockBaseAddress = ((int) (lastNeededAddress / blockSize)) * blockSize;
		// Check address availability again to see which blocks I should request
		boolean[] requestedBlocks = checkAddressAvailability(address, numberOfBytes);

		// Takes the block from the memory and replaces a block in our cache, 5aleeha
		// least recently used block replacemant
		if (!requestedBlocks[0]) {
			// Send the address, get the targetBlock and replace
			Block replacementBlock1 = memory.readFromMem(blockBaseAddress);
			replacementBlock1.setLastUsedCycle(cycle + 1 + CacheSetup.getLatency());
			replacementBlock1.setDirtyBit(false);

			Block leastRecentlyUsedBlock1 = blocks.remove();

			if (leastRecentlyUsedBlock1.isDirtyBit() && leastRecentlyUsedBlock1.getTag() == replacementBlock1.getTag()) {
				leastRecentlyUsedBlock1.setLastUsedCycle(cycle + 1 + CacheSetup.getLatency());
				blocks.add(leastRecentlyUsedBlock1);
			} else {
				writeBackFromCacheToMemory(leastRecentlyUsedBlock1);
				replacementBlock1.setBlockName(leastRecentlyUsedBlock1.getBlockName());
				blocks.add(replacementBlock1);
			}
		}

		if (!requestedBlocks[1]) {
			// Send the address + blockSize - 1 , get the targetBlock and replace
			Block replacementBlock2 = memory.readFromMem(secondBlockBaseAddress);
			replacementBlock2.setLastUsedCycle(cycle + 1 + CacheSetup.getLatency());
			replacementBlock2.setDirtyBit(false);

			Block leastRecentlyUsedBlock2 = blocks.remove();
			writeBackFromCacheToMemory(leastRecentlyUsedBlock2);

			if (leastRecentlyUsedBlock2.isDirtyBit() && leastRecentlyUsedBlock2.getTag() == replacementBlock2.getTag()) {
				leastRecentlyUsedBlock2.setLastUsedCycle(cycle + 1 + CacheSetup.getLatency());
				blocks.add(leastRecentlyUsedBlock2);
			} else {

				writeBackFromCacheToMemory(leastRecentlyUsedBlock2);
				writeBackFromCacheToMemory(leastRecentlyUsedBlock2);
				replacementBlock2.setBlockName(leastRecentlyUsedBlock2.getBlockName());
				blocks.add(replacementBlock2);
			}
		}

	}

	public static void writeBackFromCacheToMemory(Block writeBackBlock) {
		if (writeBackBlock.getTag() == -1) {
			return;
		}
		memory.writeOnMem(writeBackBlock);
		// Pass the block as is to the memory for it to update; the lastUsedCycle won't
		// be used by the memory side
	}

	public static double loadFromCache(LoadStage loadStage) throws Exception {
		// Check if this address is contained within the cache
		// if it is not, this load is a miss, increase execution cycles by penalty
		// (again, since it got replaced somehow)
		System.out.println("LOADING");
		// I have to know if I am loading a single or a double
		Instruction instruction = instructionTable.get(loadStage.getInstructionIndex());
		String operation = getInstructionOperation(instruction);
		int address = Integer.parseInt(loadStage.getAddress());

		PriorityQueue<Block> tmp = blocks;

		double returnValue = -1;

		if (operation.equals("LD") || operation.equals("L.D")) {
			// Loading 8 bytes the reconstructing them
			byte[] byteList = new byte[8];
			for (int i = 0; i < 8; i++) {
				int blockBaseAddress = ((int) ((address + i) / blockSize)) * blockSize;
				// Get the correct block
				byteList[i] = getBlockContainingAddress(address + i).getByte(address + i - blockBaseAddress);
			}

			System.out.print("Binary Value: ");
			for (int i = 0; i < byteList.length; i++) {
				System.out.print(String.format("%8s", Integer.toBinaryString(byteList[i])).replace(' ', '0'));
			}

			returnValue = reconstructFromBytes(byteList, operation);
		} else if (operation.equals("LW") || operation.equals("L.S")) {
			// Loading 4 bytes then reconstructing them
			byte[] byteList = new byte[4];
			for (int i = 0; i < 4; i++) {
				int blockBaseAddress = ((int) ((address + i) / blockSize)) * blockSize;
				// Get the correct block
				byteList[i] = getBlockContainingAddress(address + i).getByte(address - blockBaseAddress + i);
			}

			System.out.print("Binary Value: ");
			for (int i = 0; i < byteList.length; i++) {
				System.out.print(String.format("%8s", Integer.toBinaryString(byteList[i])).replace(' ', '0'));
			}

			returnValue = reconstructFromBytes(byteList, operation);
		}

		System.out.println();
		System.out.println("Value Returned to CPU: " + returnValue);

		return returnValue;

		// throw new Exception("Whoops, something went wrong -> This is cache");
	}

	private static Block getBlockContainingAddress(int address) {
		int blockBaseAddress = ((int) (address / blockSize)) * blockSize;
		for (Block block : blocks) {
			if (block.getTag() == blockBaseAddress) {
				return block;
			}
		}
		return null;
	}

	private static double reconstructFromBytes(byte[] bytes, String loadOperation) throws Exception {
		if (bytes.length == 4) { // int or float
			if (loadOperation.equals("LW")) {
				System.out.println("Reading an int from cache");
				return (double) ByteBuffer.wrap(bytes).getInt();
			} else if (loadOperation.equals("L.S")) {
				System.out.println("Reading a float from cache");
				return (double) ByteBuffer.wrap(bytes).getFloat();
			}
		} else if (bytes.length == 8) {
			if (loadOperation.equals("LD")) {
				System.out.println("Reading a double from cache");
				return (double) ByteBuffer.wrap(bytes).getLong();
			} else if (loadOperation.equals("L.D")) {
				System.out.println("Reading a double from cache");
				return ByteBuffer.wrap(bytes).getDouble();
			}
		}

		throw new Exception("Whoops, something went wrong -> This is reconstructor");
	}

	public static void storeToCache(StoreStage storeStage) throws Exception {
		Instruction instruction = instructionTable.get(storeStage.getInstructionIndex());
		String operation = getInstructionOperation(instruction);
		int address = Integer.parseInt(storeStage.getAddress());

		byte[] byteList;
		System.out.println("Value in Register: " + storeStage.getV());
		switch (operation) {
		case "SW":
			byteList = ByteBuffer.allocate(4).putInt((int) storeStage.getV()).array();
			System.out.println("Writing an int Back to cache");
			break;
		case "SD":
			byteList = ByteBuffer.allocate(8).putLong((long) storeStage.getV()).array();
			System.out.println("Writing a long Back to cache");
			break;
		case "S.S":
			byteList = ByteBuffer.allocate(4).putFloat((float) storeStage.getV()).array();
			System.out.println("Writing an float Back to cache");
			break;
		case "S.D":
			byteList = ByteBuffer.allocate(8).putDouble(storeStage.getV()).array();
			System.out.println("Writing an double Back to cache");
			break;
		default:
			throw new Exception("EDA EDA EDA -> This is storeToCache");
		}
		System.out.print("Binary Value: ");
		for (int i = 0; i < byteList.length; i++) {
			System.out.print(String.format("%8s", Integer.toBinaryString(byteList[i])).replace(' ', '0'));
		}
		System.out.println();

		PriorityQueue<Block> tmp = blocks;

		for (int i = 0; i < byteList.length; i++) {
			int blockBaseAddress = ((int) ((address + i) / blockSize)) * blockSize;
			// Get the correct block
			Block writeBackBlock = getBlockContainingAddress(address + i);
			writeBackBlock.setByte(address + i - blockBaseAddress, byteList[i]);
			writeBackBlock.setDirtyBit(true);

		}
		for (int i = 0; i < Cache.blockCount; i++) {
			String blockName = "Block " + i;
			Block current = Cache.getBlockWithName(blockName);
			for (int j = 0; j < Cache.blockSize; j++) {
				String binaryString = String.format("%8s", Integer.toBinaryString(current.getByte(j) & 0xFF)).replace(' ', '0');
				if(current.getTag()!=-1) {
					cacheTable.get(i * Cache.blockSize + j).setAddress(current.getTag() + j);
				}
				else{
					cacheTable.get(i * Cache.blockSize + j).setAddress(-1);
				}
				System.out.println("cuurent byte:"+current.getByte(j));
				System.out.println("block name:"+blockName);
				System.out.println("byte num"+j);
				System.out.println("tag:"+ current.getTag());
				System.out.println("binary value:"+ binaryString);

				cacheTable.get(i*Cache.blockSize+j).setValue(binaryString);
			}
		}
	}

	public String toString() {
		return hitLatency + " " + missPenalty + " " + cacheSize + " " + blockSize;
	}
}
