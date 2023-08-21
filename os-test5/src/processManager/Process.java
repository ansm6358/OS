package processManager;

import java.util.Vector;

import os.MemoryManager;
import os.MemoryManager.HeapSegment;
import processManager.ProcessManager.EInterrupt;

public class Process { //PCB CodeSegment StackSegment HeapSegment
	//global sonstants
	public enum EState {nnew, running, wait, ready, terminated};
	public enum ERegister {ePC, eCS, eDS, eSS, eHS, eMAR, eMBR, eIR, eStatus, eAC};
	private enum EInstruction {halt, lda, ldv, sta, add, adda, sub, mul, div, diva, and, not, jmp, jmz, cmp, prt, loof, newh};
	//components
	private PCB pcb;
	private Segment codeSegment;
	private Segment stackSegment;
	private Vector<HeapSegment> heapSegment;
	private MemoryManager memoryManager;
	
	//working variables
	public PCB getPcb() {return pcb;}
	public void setPcb(PCB pcb) {this.pcb = pcb;}

	public void association(MemoryManager memoryManager) {
		this.memoryManager = memoryManager;
	}	
	public Process(int stackSegmentSize, int[] codes, int processNum) {
		
		this.pcb = new PCB(processNum);
		this.codeSegment = new Segment(codes);
		this.stackSegment = new Segment(stackSegmentSize);
		this.heapSegment = new Vector<HeapSegment>();
		this.pcb.seteState(Process.EState.ready);
	}
	
	public void initialize() {
		this.pcb.seteState(Process.EState.running);
	}
	public void setIO(IODevice ioDevice) {
		this.pcb.setIoDevice(ioDevice);
	}
	public EInterrupt execute() {
		int PCdata=this.pcb.getRegisters().get(ERegister.ePC.ordinal()).get();
		this.pcb.getRegisters().get(ERegister.ePC.ordinal()).set(PCdata+1);
		this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).set(PCdata);
		int MARdata = this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get();
		this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(this.codeSegment.fetch(MARdata));
		int MBRdata = this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).get();
		this.pcb.getRegisters().get(ERegister.eIR.ordinal()).set(MBRdata);
		int code = this.pcb.getRegisters().get(ERegister.eIR.ordinal()).get();
		
		excuteCode(code);
		if(this.pcb.geteState() == EState.terminated) {
			return EInterrupt.eProcessTerminated;
		} else if(this.pcb.geteState() == EState.wait) {
			
			return EInterrupt.eIOStart;
		} else {
		return null;	
		}
	}	

	public class Segment {	
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
	

	private void excuteCode(int code) {
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
	private int getHeap(int address) {
		for(HeapSegment heap: this.heapSegment) {
			if(address-heap.getSize()<0) {
				return heap.fetch(address);
			} else {
				address = address -heap.getSize();
			}
		}
		return -1;	
	}
	private void setHeap(int address, int data) {
		for(HeapSegment heap: this.heapSegment) {
			if(address-heap.getSize()<0) {
				 heap.store(address, data);
			} else {
				address = address -heap.getSize();
			}
		}		
	}
	private void halt() {
		this.pcb.getRegisters().get(ERegister.eStatus.ordinal()).set(EInterrupt.eProcessTerminated.ordinal());
		this.pcb.seteState(EState.terminated);
	}
	private void lda(int address) {	//heap
		this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).set(address);
		if(address-1024<0) {
		this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(
		this.stackSegment.fetch(this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get()));
		} else {
			this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(
			getHeap(this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get()-1024));
		}
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(
				this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).get());		
	}
	
	private void ldv(int data) {
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(data);
	}
	private void sta(int address) { //heap
		this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).set(address);
		
		if(address-1024<0) {
		this.stackSegment.store(
			this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get(),
			this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get());
		} else {
			setHeap(this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get()-1024,
					this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get());
		}
	}
	
	private void add(int data) {
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get()+data);
	}
	private void adda(int address) { //heap
		this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).set(address);
		if(address-1024<0) {
			this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(
			this.stackSegment.fetch(this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get()));
			} else {
				this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(
				getHeap(this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get()-1024));
			}
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
		if(address-1024<0) {
			this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(
			this.stackSegment.fetch(this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get()));
			} else {
				this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(
				getHeap(this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get()-1024));
			}
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(
				this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get()
				/this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).get());
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
		this.pcb.getRegisters().get(ERegister.ePC.ordinal()).set(data);
		
	}
	private void jmz(int data) { 
		if((this.pcb.getRegisters().get(ERegister.eStatus.ordinal()).get() & 2) == 2) {
			this.pcb.getRegisters().get(ERegister.ePC.ordinal()).set(data);
		}
	}
	private void cmp(int data) { 
		int result = this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get() - data;
		int statusValue = this.pcb.getRegisters().get(ERegister.eStatus.ordinal()).get();
		if(result<0) {
					this.pcb.getRegisters().get(ERegister.eStatus.ordinal()).set(statusValue&00|2);
		} else if(result==0){
				this.pcb.getRegisters().get(ERegister.eStatus.ordinal()).set(statusValue&00|1);
		} else {
			this.pcb.getRegisters().get(ERegister.eStatus.ordinal()).set(statusValue&00);
		}
	}
	private void prt(int address) { //heap
		this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).set(address);
		if(address-1024<0) {
			this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(
			this.stackSegment.fetch(this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get()));
			} else {
				this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).set(
				getHeap(this.pcb.getRegisters().get(ERegister.eMAR.ordinal()).get()-1024));
			}
		this.pcb.getRegisters().get(ERegister.eAC.ordinal()).set(this.pcb.getRegisters().get(ERegister.eMBR.ordinal()).get());
		this.pcb.getRegisters().get(ERegister.eStatus.ordinal()).set(1<<4);
		this.pcb.seteState(EState.wait);
		
		System.out.println(this.pcb.getRegisters().get(ERegister.eAC.ordinal()).get());
	}
	private void loof() {
		
	}
	private void newh(int size) { //heap
		this.heapSegment.add(this.memoryManager.makeHeap(size));
	}
	private Thread thread;
	public void runIO() {
		this.pcb.getIODevice().setControl(false);
		this.pcb.getIODevice().setProcess(this);
		thread = new Thread(this.pcb.getIODevice());
		thread.start();
	}
	public void stopIO() {
		thread.interrupt();
		 //this.pcb.getIODevice().setControl(true);
	}

}
