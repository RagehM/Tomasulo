package units.cache;

import static gui.simulatingStage.Simulate.cycle;

import gui.setupStage.CacheSetup;
import gui.setupStage.InstructionSetup;

public class Block {

	private byte[] bytes = new byte[CacheSetup.getBlockSize()];
	private int tag;
	private int lastUsedCycle;

	public void setByte(int index, byte value) {
		bytes[index] = value;
		// used with stores only
	}

	public byte getByte(int index) {
		return bytes[index];
		// used with load only
	}

	public void replaceBlock(byte[] newBytes, int tag) {
		this.bytes = newBytes;
		this.tag = tag;
		this.lastUsedCycle = cycle + 1 + InstructionSetup.getMemoryLatency();
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getLastUsedCycle() {
		return lastUsedCycle;
	}

	public void setLastUsedCycle(int lastUsedCycle) {
		this.lastUsedCycle = lastUsedCycle;
	}

	public Block(int tag) {
		this.lastUsedCycle = 0;
		this.tag = 0;
	}
}
