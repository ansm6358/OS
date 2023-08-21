package os;

import java.io.FileNotFoundException;

public class OperatingSystem {
	private UXManager uxManager;
	private Loader loader;
	private ProcessManager processManager;
	private MemoryManager memoryManager;
	private FileManager fileManager;
	
	public OperatingSystem() {
		this.uxManager = new UXManager();	
		this.loader = new Loader();
		this.processManager = new ProcessManager();
		this.memoryManager = new MemoryManager();
		this.fileManager = new FileManager();
	}
	
	public void associate() {
		this.uxManager.associate(this.loader, this.processManager);
		this.processManager.associate(this.memoryManager, this.fileManager);
	}
	
	public void run() throws FileNotFoundException {
		this.uxManager.run();
		
	}
}
