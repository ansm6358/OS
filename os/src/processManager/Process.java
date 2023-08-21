package processManager;

public class Process { //PCB CodeSegment StackSegment HeapSegment
	//global sonstants
	public enum EState {nnew, running, wait, ready, terminated};
	public enum ERegister {ePC, eCS, eDS, eSS, eHS, eMAR, eMBR, eIR, eStatus, eAC}
	public enum EInterrup {eIO, eTerminate};
	
	//components
	private PCB pcb;
	private Segment codeSegment;
	private Segment stackSegment;
	
	//working variables
	private int timeSlice;
	
	public PCB getPcb() {return pcb;}
	public void setPcb(PCB pcb) {this.pcb = pcb;}
//	public Segment getCodeSegment() {return codeSegment;}
//	public void setCodeSegment(Segment codeSegment) {this.codeSegment = codeSegment;}
//	public Segment getStackSegment() {return stackSegment;}
//	public void setStackSegment(Segment stackSegment) {this.stackSegment = stackSegment;}
	
	public Process(int stackSegmentSize, int[] codes) {

		this.pcb = new PCB();
		this.codeSegment = new Segment(codes);
		this.stackSegment = new Segment(stackSegmentSize);
	}
	
	public void initialize(int timeSlice) {
		this.pcb.seteState(Process.EState.running);
		this.timeSlice = timeSlice;
	}

	public void execute() {
		this.codeSegment.fetch(this.getPcb().getRegisters().get(ERegister.ePC.ordinal()).get());
		///////////
		
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
	




}
