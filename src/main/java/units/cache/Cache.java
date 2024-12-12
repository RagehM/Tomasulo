package units.cache;

import static gui.setupStage.SetupStage.memory;
import static gui.simulatingStage.Simulate.cycle;
import static units.instructionUnit.getInstructionOperation;
import static units.instructionUnit.instructionTable;

import java.nio.ByteBuffer;
import java.util.PriorityQueue;

import gui.setupStage.InstructionSetup;
import instructions.Instruction;
import units.stage.addressStage.LoadStage;
import units.stage.addressStage.StoreStage;

public class Cache {
	public static Cache cache = null;
	private static int hitLatency;
	private static int missPenalty;
	private static int cacheSize;
	private static int blockSize;
	private static int blockCount;
	private static PriorityQueue<Block> blocks;

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
			blocks.add(new Block(-1));
		}

		cache = this;
	}

	public static boolean[] checkAddressAvailability(int address, int numberOfBytes) {
		// returns an array of 2 elements,
		// denoting if the needed word (1st
		// and 2nd if that is the case) is
		// available or not
		// Check each cache block to determine if the target address is within it
		int blockBaseAddress = ((int) (address / blockSize)) * blockSize;
		int lastNeededAddress = address + numberOfBytes;
		int secondBlockBaseAddress = (int) (lastNeededAddress / blockSize) * blockSize;
		boolean[] out = new boolean[2];

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
		int lastNeededAddress = address + numberOfBytes;
		int secondBlockBaseAddress = ((int) (lastNeededAddress / blockSize)) * blockSize;
		// Check address availability again to see which blocks I should request
		boolean[] requestedBlocks = checkAddressAvailability(address, numberOfBytes);

		// Takes the block from the memory and replaces a block in our cache, 5aleeha
		// least recently used block replacemant
		if (!requestedBlocks[0]) {
			// Send the address, get the targetBlock and replace
			Block replacementBlock1 = memory.readFromMem(blockBaseAddress);
			replacementBlock1.setLastUsedCycle(cycle + 1 + InstructionSetup.getMemoryLatency());

			Block leastRecentlyUsedBlock1 = blocks.remove();
			writeBackFromCacheToMemory(leastRecentlyUsedBlock1);

			blocks.add(replacementBlock1);
		}

		if (!requestedBlocks[1]) {
			// Send the address + blockSize - 1 , get the targetBlock and replace
			Block replacementBlock2 = memory.readFromMem(secondBlockBaseAddress);
			replacementBlock2.setLastUsedCycle(cycle + 1 + InstructionSetup.getMemoryLatency());

			Block leastRecentlyUsedBlock2 = blocks.remove();
			writeBackFromCacheToMemory(leastRecentlyUsedBlock2);

			blocks.add(replacementBlock2);
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
		// TODO: Handle this case later

		// I have to know if I am loading a single or a double
		Instruction instruction = instructionTable.get(loadStage.getInstructionIndex());
		String operation = getInstructionOperation(instruction);
		int address = Integer.parseInt(loadStage.getAddress());

		if (operation.equals("LD") || operation.equals("L.D")) {
			// Loading 8 bytes the reconstructing them
			byte[] byteList = new byte[8];
			for (int i = 0; i < 8; i++) {
				int blockBaseAddress = ((int) ((address + 1) / blockSize)) * blockSize;
				// Get the correct block
				byteList[i] = getBlockContainingAddress(address + i).getByte(address - blockBaseAddress);
			}

			return reconstructFromBytes(byteList, operation);
		} else if (operation.equals("LW") || operation.equals("L.S")) {
			// Loading 4 bytes then reconstructing them
			byte[] byteList = new byte[4];
			for (int i = 0; i < 4; i++) {
				int blockBaseAddress = ((int) ((address + i) / blockSize)) * blockSize;
				// Get the correct block
				byteList[i] = getBlockContainingAddress(address + i).getByte(address - blockBaseAddress);
			}
			return reconstructFromBytes(byteList, operation);
		}

		throw new Exception("Whoops, something went wrong -> This is cache");
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
				return (double) ByteBuffer.wrap(bytes).getInt();
			} else if (loadOperation.equals("L.S")) {
				return (double) ByteBuffer.wrap(bytes).getFloat();
			}
		} else if (bytes.length == 8) {
			if (loadOperation.equals("LD")) {
				return (double) ByteBuffer.wrap(bytes).getLong();
			} else if (loadOperation.equals("L.D")) {
				return ByteBuffer.wrap(bytes).getDouble();
			}
		}

		throw new Exception("Whoops, something went wrong -> This is reconstructor");
	}

	public static void storeToCache(StoreStage storeStage) {
		// TODO: Write this method
	}

	public String toString() {
		return hitLatency + " " + missPenalty + " " + cacheSize + " " + blockSize;
	}

}
