package processManager;

import java.util.Vector;

import os.MemoryManager;
import processManager.InterruptHandler.EInterrupt;

public class Process { //PCB CodeSegment StackSegment HeapSegment
	//global sonstants
	public enum EState {nnew, running, wait, ready, terminated};
	public enum ERegister {ePC, eCS, eDS, eSS, eHS, eMAR, eMBR, eIR, eStatus, eAC};
	private enum EInstruction {halt, lda, ldv, sta, add, adda, sub, mul, div, diva, and, not, jmp, jmz, cmp, prt, loof, newh};
	//components
	private PCB pcb;
	private Segment codeSegment;
	private Segment stackSegment;
	private MemoryManager memoryManager;
	//working variables
	public PCB getPcb() {return pcb;}
	public void setPcb(PCB pcb) {this.pcb = pcb;}
//	public Segment getCodeSegment() {return codeSegment;}
//	public void setCodeSegment(Segment codeSegment) {this.codeSegment = codeSegment;}
//	public Segment getStackSegment() {return stackSegment;}
//	public void setStackSegment(Segment stackSegment) {this.stackSegment = stackSegment;}
	public void association(MemoryManager memoryManager) {
		this.memoryManager = memoryManager;
	}	
	public Process(int stackSegmentSize, int[] codes, int processNum) {
		
		this.pcb = new PCB(processNum);
		this.codeSegment = new Segment(codes);
		this.stackSegment = new Segment(stackSegmentSize);
		this.pcb.seteState(Process.EState.ready);
	}
	
	public void initialize() {
		this.pcb.seteState(Process.EState.running);
	}

	public EState executeALine() {
		int PCdata=this.pcb.getRegisters().get(ERegister.ePC.ordinal()).get();
		this.pcb.getRegisters().get(ERegister.ePC.ordinal()).set(PCdata+1);
		this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).set(PCdata);
		int MARdata = this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get();
		this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(this.codeSegment.fetch(MARdata));
		int MBRdata = this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).get();
		this.pcb.getRegisters().get(ERegister.eIR.ordinal()).set(MBRdata);
		int code = this.pcb.getRegisters().get(ERegister.eIR.ordinal()).get();
		
		
		excute(code);
		///////////////////////////////////////////////
		//CPU excute
		///////////////////////////////////////////////
		
		if(this.pcb.getRegisters().get(ERegister.eStatus.ordinal()).get() == EInterrupt.eIOStart.ordinal()) {
			return Process.EState.wait;
		} else if(this.pcb.getRegisters().get(ERegister.eStatus.ordinal()).get() == EInterrupt.eProcessTerminated.ordinal()) {
			return Process.EState.terminated;
		} 
		return Process.EState.running;
	}
	
	
	/////////////////////////////////////////////////////////////
	public class PCB {
		private int id;
		private EState eState;
		private Vector<Register> registers;
		

		public PCB(int processNum) {
			this.id = processNum;
			this.registers = new Vector<Register>();
			for(ERegister eRegister: ERegister.values()) {
				this.registers.add(new Register());
			}
		}

		public int getId() {return id;}
		public void setId(int id) {this.id = id;}
		public EState geteState() {return eState;}
		public void seteState(EState eState) {this.eState = eState;}
		public Vector<Register> getRegisters() {return registers;}
		public void setRegisters(Vector<Register> registers) {this.registers = registers;}
	}

	/////////////////////////////////////////////////////////////
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
	
	/////////////////////////////////////////////////////////////
	private class Register {
		private int value;
		public Register(){
		}
		public int get() {return value;}
		public void set(int value) {this.value = value;}
		
	}
	///////////////////////////////////////////////////////////////
	private void excute(int code) {
		int instruction = code >>> 16;
		int data = code & 0x0000ffff;
		switch(EInstruction.values()[instruction]) {
		case halt:
			halt();
			break; 
		case lda:
			lda(data);
			break;
		case ldv:
			ldv(data);
			break;
		case sta:
			sta(data);
			break;
		case add:
			add(data);
			break;
		case adda:
			adda(data);
			break;
		case sub:
			sub(data);
			break;
		case mul:
			mul(data);
			break;
		case div:
			div(data);
			break;
		case diva:
			diva(data);
			break;
		case and:
			and(data);
			break;
		case not:
			not(data);
			break;
		case jmp:
			jmp(data);
			break;
		case jmz:
			jmz(data);
			break;
		case cmp:
			cmp(data);
			break;
		case prt:
			prt(data);
			break;
		case loof:
			loof();
			break;
		case newh:
			newh(data);
			break;		
		}
	}
	private void halt() {
		this.pcb.getRegisters().get(ERegister.eStatus.ordinal()).set(EInterrupt.eProcessTerminated.ordinal());
	}
	private void lda(int address) {	//heap
		this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).set(address);
		this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(
		this.stackSegment.fetch(this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get()));
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(
				this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).get());		
	}
	private void ldv(int data) {
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(data);
	}
	private void sta(int address) { //heap
		this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).set(address);
		this.stackSegment.store(
			this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get(),
			this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get());
	}
	private void add(int data) {
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get()+data);
	}
	private void adda(int address) { //heap
		this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).set(address);
		this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(
		this.stackSegment.fetch(this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get()));
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(
				this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get()
				+this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).get());
	}
	private void sub(int data) {
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(
				this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get()-data);
	}
	private void mul(int data) {
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(
				this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get()*data);
	}
	private void div(int data) {
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(
				this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get()/data);
	}
	private void diva(int address) { //heap
		this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).set(address);
		this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(
		this.stackSegment.fetch(this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get()));
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(
				this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get()
				+this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).get());
	}
	private void and(int data) {
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(
				this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get()&data);
	}
	private void not(int data) {
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(
				~this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get());
	}
	private void jmp(int data) {

		
	}
	private void jmz(int data) { 

		
	}
	private void cmp(int data) { 

		
	}
	private void prt(int data) { //heap

		
	}
	private void loof() {
		
	}
	private void newh(int instruction) { //heap
		
	}


}
