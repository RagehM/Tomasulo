package units;

import java.util.ArrayList;

public class MemoryCell {
    private int address;
    private float value;
    public static ArrayList<MemoryCell> MemoryCellTable;

    public MemoryCell(int address, float value) {
        this.address = address;
        this.value = value;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public static ArrayList<MemoryCell> getMemoryCellTable() {
        return MemoryCellTable;
    }

    public static void setMemoryCellTable(ArrayList<MemoryCell> memoryCellTable) {
        MemoryCellTable = memoryCellTable;
    }
}
