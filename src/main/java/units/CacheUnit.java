package units;

public class CacheUnit {
  private int hitLatency = 0;
  private int missPenalty = 0;
  private int cacheSize;
  private int blockSize;

  public void setHitLatency(int hitLatency) {
    this.hitLatency = hitLatency;
  }

  public void setMissPenalty(int missPenalty) {
    this.missPenalty = missPenalty;
  }

  public int getHitLatency() {
    return hitLatency;
  }

  public int getMissPenalty() {
    return missPenalty;
  }

  public int getCacheSize() {
    return cacheSize;
  }

  public void setCacheSize(int size) {
    this.cacheSize = size;
  }

  public int getBlockSize() {
    return blockSize;
  }

  public void setBlockSize(int blockSize) {
    this.blockSize = blockSize;
  }

  public CacheUnit (int hitLatency, int missPenalty, int cacheSize, int blockSize) {
    this.hitLatency = hitLatency;
    this.missPenalty = missPenalty;
    this.cacheSize = cacheSize;
    this.blockSize = blockSize;
  }

  public String toString() {
    return hitLatency + " " + missPenalty + " " + cacheSize + " " + blockSize;
  }

}
