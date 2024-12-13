package units.stage.aluStage;

import java.util.ArrayList;

public class CacheStage {
    private static int indexCount=1;
    private String blockName;
    private int address;
    private String value;

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public static ArrayList<CacheStage> cacheTable = new ArrayList<CacheStage>();

    public CacheStage(String blockName, String value, int address) {
        this.blockName=blockName;
        this.address=address;
        this.value = value;
        cacheTable.add(this);
        indexCount++;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static ArrayList<CacheStage> getCacheTable() {
        return cacheTable;
    }

    public static void setCacheTable(ArrayList<CacheStage> cacheTable) {
        CacheStage.cacheTable = cacheTable;
    }
    public String toString(){
        return blockName + " " + address + " " + value;
    }
}
