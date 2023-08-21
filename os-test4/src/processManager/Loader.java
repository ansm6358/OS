package processManager;

import os.MemoryManager;

public class Loader {
	private MemoryManager memoryManager;
	
	public Process load(Process process) {
		
		this.memoryManager.allocate(process);
		return process;
	}

	

}
