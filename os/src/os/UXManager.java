package os;

import java.util.Scanner;

import processManager.Process;
import processManager.ProcessManager;



public class UXManager {
	
	private ProcessManager processManager;
	private FileManager fileManager;
	
	public UXManager() {	
	}
	
	public void associate(FileManager fileManager, ProcessManager processManager) {
		this.fileManager = fileManager;
		this.processManager = processManager;
	}
	
	public void run() {
		//login
		//desktop display
		//wait for event
		Scanner scanner = new Scanner(System.in);
		String fileName = scanner.nextLine();
		Process process = this.fileManager.getFile(fileName);
		this.processManager.execute(process);		
	}

}
