package units;

import units.cache.Block;

public class MemoryBlocks {
	private static Block[] memory;
	private static int blockSize;
	private static int memorySize;

	public MemoryBlocks(int memorySize) {
		MemoryBlocks.memorySize = memorySize;
	}

	public static void init(int blockSize) {
		MemoryBlocks.blockSize = blockSize;
		if (memorySize % blockSize != 0) {
			throw new IllegalArgumentException("Memory Size Should be divisible by ");
		}
		int memorySizeBlocks = memorySize / blockSize;
		memory = new Block[memorySizeBlocks];

		for (int i = 0; i < memorySizeBlocks; i++) {
			memory[i] = new Block(i * blockSize);
		}
	}

	public Block readFromMem(int tag) {
		return memory[tag / blockSize];
	}

	public void writeOnMem(Block block) {
		memory[block.getTag() / blockSize] = block;
	}
}
