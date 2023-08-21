package os;

import processManager.ProcessManager;

public class OperatingSystem {
	private UXManager uxManager;
	
	private ProcessManager processManager;
	private MemoryManager memoryManager;
	private FileManager fileManager;
	
	public OperatingSystem() {
		this.processManager = new ProcessManager();
		this.memoryManager = new MemoryManager();
		this.fileManager = new FileManager();
		
		this.uxManager = new UXManager();	
	}
	public void initialize() {
		//io device
		this.associate();
	}
	public void finalize() {
		
	}	
	public void associate() {
		this.processManager.associate(this.memoryManager, this.fileManager);
		this.uxManager.associate(this.fileManager, this.processManager);
	}
	
	public void run() {
		this.uxManager.run();
		this.processManager.run();
	}

}
