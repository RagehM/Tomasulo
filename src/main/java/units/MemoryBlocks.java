package units;

import java.util.ArrayList;
import units.cache.Block;

public class MemoryBlocks {
    private Block[] memory;
    private int blockSize;

    public MemoryBlocks(int memorySize, int blockSize) {
        this.blockSize = blockSize;
        if(memorySize % blockSize != 0){
            throw new IllegalArgumentException("Memory Size Should be divisible by ");
        }
        int memorySizeBlocks = memorySize / blockSize;
        memory = new Block[memorySizeBlocks];

        for(int i = 0; i < memorySizeBlocks; i++){
            memory[i] = new Block(i*blockSize);
        }
    }

    public Block readFromMem(int tag){
        return memory[tag];
    }
    public void writeOnMem(Block block){
        memory[block.getTag()] = block;
    }
}
