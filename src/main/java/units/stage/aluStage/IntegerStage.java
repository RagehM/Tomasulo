package units.stage.aluStage;

import units.stage.Stage;

public class IntegerStage extends Stage {
    private String stage;
	private static int number = 1;
    private String op;
    private int Vj;
    private IntegerStage Qj;
    private int immediate; // Immediate Value

    public IntegerStage(Boolean busy, String op, int Vj, IntegerStage Qj, int immediate) {
        this.stage = "I" + number;
        number++;
        this.busy = busy;
        this.op = op;
        this.Vj = Vj;
        this.Qj = Qj;
        this.immediate = immediate;
        integerTable.add(this);
    }

    public IntegerStage getQj() {
        return Qj;
    }

    public void setQj(IntegerStage qj) {
        Qj = qj;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public int getVj() {
        return Vj;
    }

    public void setVj(int vj) {
        Vj = vj;
    }

    public int getImmediate() {
        return immediate;
    }

    public void setImmediate(int immediate) {
        this.immediate = immediate;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public float produce() {
        return this.getOp().contains("DSUBI") ? (this.getVj() - this.getImmediate()) : (this.getVj() + this.getImmediate());
    }
}
