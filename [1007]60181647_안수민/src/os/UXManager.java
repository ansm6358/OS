package os;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class UXManager {
	
	private ProcessManager processManager;
	private Loader loader;
	
	public UXManager() {	
	}
	
	public void associate(Loader loader, ProcessManager processManager) {
		this.loader = loader;
		this.processManager = processManager;
	}
	
	public void run() throws FileNotFoundException {
		Scanner scanner = new Scanner("exe.txt");
		String fileName = scanner.nextLine();
		
		Process process = this.loader.load(fileName);
		this.processManager.execute(process);		
	}

}
