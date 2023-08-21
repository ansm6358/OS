package os;

import java.util.ArrayList;

import processManager.Process;

public class MemoryManager {

	ArrayList<Process> processes = new ArrayList<Process>(); 
	//allocate memory and set PCB info
	public void allocate(Process process) {
		process.association(this);
		this.processes.add(process);
	}
	public Process getProcess(int pcbId) {
		return this.processes.get(pcbId);
	}
	
}
