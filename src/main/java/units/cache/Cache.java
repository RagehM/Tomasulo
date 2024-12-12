package units.cache;

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

		Cache.hitLatency = hitLatency;
		Cache.missPenalty = missPenalty;
		Cache.cacheSize = cacheSize;
		Cache.blockSize = blockSize;
		if (cacheSize % blockSize != 0) {
			throw new Exception("Cache Cannot be divided into block size of " + blockSize);
		}
		Cache.blockCount = cacheSize / blockSize;

		for (int i = 0; i < blockCount; i++) {
			blocks.add(new Block(-1));
		}

		cache = this;
	}

	public static boolean[] checkAddressAvailability(int address, int numberOfBytes) { // returns an array of 2 elements,
																																											// each
		// denoting if the needed word (1st
		// and 2nd if that is the case) is
		// available or not
		// Check each cache block to determine if the target address is within it
		int blockBaseAddress = (int) (address / blockSize) * blockSize;
		int lastNeededAddress = address + numberOfBytes;
		int lastNeededBlockBaseAddress = (int) (lastNeededAddress / blockSize) * blockSize;
		boolean[] out = new boolean[2];

		// Start by checking the existence of the first block
		if (!blocks.stream().anyMatch(block -> block.getTag() == blockBaseAddress)) {
			out[0] = false;
		}

		if (lastNeededAddress > (blockBaseAddress + blockSize - 1)) {
			if (!blocks.stream().anyMatch(block -> block.getTag() == lastNeededBlockBaseAddress)) {
				out[1] = false;
			}
		}

		return out;
	}

	public static void loadFromMemoryToCache(int address, int numberOfBytes) {
		// Called after the miss penalty cycles are up
		// TODO: Call method from memory

		// Check address availability again to see which blocks I should request
		boolean[] requestedBlocks = checkAddressAvailability(address, numberOfBytes);

		// Takes the block from the memory and replaces a block in our cache, 5aleeha
		// least recently used block replacemant
		if (!requestedBlocks[0]) {
			// Send the address, get the targetBlock and replace
			Block replacementBlock1 = null;
			replacementBlock1.setLastUsedCycle(cycle + 1 + InstructionSetup.getMemoryLatency());

			Block leastRecentlyUsedBlock1 = blocks.remove();
			writeBackFromCacheToMemory(leastRecentlyUsedBlock1);

			blocks.add(replacementBlock1);
		}

		if (!requestedBlocks[1]) {
			// Send the address + blockSize - 1 , get the targetBlock and replace
			Block replacementBlock2 = null;
			replacementBlock2.setLastUsedCycle(cycle + 1 + InstructionSetup.getMemoryLatency());

			Block leastRecentlyUsedBlock2 = blocks.remove();
			writeBackFromCacheToMemory(leastRecentlyUsedBlock2);

			blocks.add(replacementBlock2);
		}
	}

	public static void writeBackFromCacheToMemory(Block leastRecentlyUsedBlock) {
		// TODO: Call method from memory

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

		if (operation.equals("LD") || operation.equals("L.D")) {
			// Loading 8 bytes the reconstructing them
			byte[] byteList = null;
			return reconstructFromBytes(byteList, operation);
		} else if (operation.equals("LW") || operation.equals("L.S")) {
			// Loading 4 bytes then reconstructing them
			byte[] byteList = null;
			return reconstructFromBytes(byteList, operation);
		}

		throw new Exception("Whoops, something went wrong -> This is cache");
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

	}

	public String toString() {
		return hitLatency + " " + missPenalty + " " + cacheSize + " " + blockSize;
	}

}
