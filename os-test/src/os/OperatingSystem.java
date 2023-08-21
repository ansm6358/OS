package os;

import processManager.ProcessManager;

public class OperatingSystem {
	private UXManager uxManager;
	
	private ProcessManager processManager;
	private MemoryManager memoryManager;
	private FileManager fileManager;
	private IOManager ioManager;
	
	public OperatingSystem() {
		this.processManager = new ProcessManager();
		this.memoryManager = new MemoryManager();
		this.fileManager = new FileManager();
		this.ioManager = new IOManager();
		this.uxManager = new UXManager();	
	}
	public void initialize() {
		//io device
		this.ioManager.settingIO();
		this.associate();
		this.processManager.initialize();
	}
	public void finalize() {
		
	}	
	public void associate() {
		this.processManager.associate(this.memoryManager, this.fileManager, this.ioManager);
		this.uxManager.associate(this.fileManager, this.processManager);
	}
	
	public void run() {
		this.uxManager.run();
		this.uxManager.run();
		this.processManager.run();
	}

}
