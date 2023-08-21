package os;

import java.util.Vector;

import os.ProcessManager.EProcessState;

public class Process { //PCB CodeSegment StackSegment HeapSegment
	private PCB pcb;
	private Segment codeSegment;
	private Segment stackSegment;
	private int address;
	
	public Process(int[] codes, int stackSegmentSize) {
		this.pcb = new PCB();
		this.codeSegment = new Segment(codes);
		this.stackSegment = new Segment(stackSegmentSize);
		this.address =0;
	}
	private class PCB {
		private Vector<Register> registers;
		public PCB() {
			this.registers = new Vector<Register>();
			for(ERegister eRegister: ERegister.values()) {
				this.registers.add(new Register());
			}
		}
	}

	private class Segment {	
		private int[] memory;
		public Segment(int size) {
			this.memory = new int[size];
		}
		public Segment(int[] memory) {
			this.memory = memory;
		}
		public void store(int address, int data) {
			this.memory[address] = data;
		}
		public int fetch(int address) {
			return this.memory[address];
		}
	}
	
	public enum ERegister {
		ePC, eCS, eDS, eSS, eHS, eMAR, eMBR, eIR, eStatus, eAC
	}
	private class Register {
	}
	public void setState(EProcessState eProcessState) {
		
	}
	public boolean executeALine() {
		int code = this.codeSegment.fetch(this.address);
		if(code==0) {
			return true;
		}
		System.out.println();
		this.address++;
		return false;
	}
	public boolean checkInterruptStatus() {
		return false;
	}
}
