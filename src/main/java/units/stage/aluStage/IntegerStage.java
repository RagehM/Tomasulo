package units.stage.aluStage;

import units.stage.Stage;

public class IntegerStage extends Stage {
    private String op;
    private int Vj;
    private int immediate; // Immediate Value
    private IntegerStage Qj;
    public IntegerStage(String op, int Vj, int Vk) {
        super();
        this.op = op;
        this.Vj = Vj;
        this.immediate = Vk;
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

    public float produce() {
        return this.getOp().contains("DSUBI") ? (this.getVj() - this.getImmediate()) : (this.getVj() + this.getImmediate());
    }



}
