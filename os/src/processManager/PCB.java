package processManager;

import java.util.Vector;

import processManager.Process.ERegister;
import processManager.Process.EState;

public class PCB {
	
	private int id;
	private EState eState;
	private Vector<Register> registers;
	private Vector<IODevice> ioDevices;
	
	public PCB() {
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
	
	public class Register {
		private int value;
		public Register(){
		}
		public int get() {return value;}
		public void set(int value) {this.value = value;}
		
	}
}