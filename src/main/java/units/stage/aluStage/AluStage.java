package units.stage.aluStage;

import units.stage.Stage;

public class AluStage extends Stage {
	private String op;
	private double Vj;
	private double Vk;
	private Stage Qj;
	private Stage Qk;

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public double getVj() {
		return Vj;
	}

	public void setVj(double vj) {
		Vj = vj;
	}

	public double getVk() {
		return Vk;
	}

	public void setVk(double vk) {
		Vk = vk;
	}

	public Stage getQj() {
		return Qj;
	}

	public void setQj(Stage qj) {
		Qj = qj;
	}

	public Stage getQk() {
		return Qk;
	}

	public void setQk(Stage qk) {
		Qk = qk;
	}

	public AluStage(Boolean busy, String op, double Vj, double Vk, Stage Qj, Stage Qk) {
		this.busy = busy;
		this.op = op;
		this.Vj = Vj;
		this.Vk = Vk;
		this.Qj = Qj;
		this.Qk = Qk;
	}

	public String toString() {
		return this.busy + " " + this.op + " " + this.Vj + " " + this.Vk + " " + this.Qj + " " + this.Qk;
	}
}
