package units.cache;

import gui.setupStage.CacheSetup;

public class Block implements Comparable {

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

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Block block = (Block) o;
		return Integer.compare(this.lastUsedCycle, block.lastUsedCycle);
	}
}
