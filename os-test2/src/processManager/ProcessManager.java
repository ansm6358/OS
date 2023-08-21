package processManager;

import os.FileManager;
import os.MemoryManager;
import processManager.InterruptHandler.EInterrupt;
import processManager.Process.EState;

public class ProcessManager {

	// components
	private Loader loader;
	private InterruptHandler interruptHandler;

	private Process currentProcess;
	private ProcessQueue readyQueue;
	private ProcessQueue ioQueue;

	// association
	private FileManager fileManager;
	private MemoryManager memoryManager;

	public ProcessManager() { // 프로세스 생성과 소멸을 관리
		//this.devices = new Vector<IODevice>();
		this.loader = new Loader(); // 파일에 있는 것을 메모리에 올려줌, 프로세스의 일부라 봐도 된다
		this.interruptHandler = new InterruptHandler();
		this.readyQueue = new ProcessQueue();
		this.ioQueue = new ProcessQueue();
	}

	public void associate(MemoryManager memoryManager, FileManager fileManager) {
		this.fileManager = fileManager;
		this.memoryManager = memoryManager;
		this.loader.associate(this.memoryManager);
	}

	// mili seconds
	public static int TIMESLICE = 10;

	public void execute(Process process) {
		this.loader.load(process); // pcb의 주소를 받음
		this.readyQueue.enqueue(process.getPcb().getId());
	}

	public void run() {
		//타이머를 뜨레드로 작동
		int pcbId= this.readyQueue.dequeue();
		this.currentProcess = this.memoryManager.getProcess(pcbId);
		this.currentProcess.initialize();
		
		EState state = this.currentProcess.getPcb().geteState();
		EInterrupt interrupt = EInterrupt.eNone;
		
		this.interruptHandler.startTimer();
		while ((state==EState.running) && (interrupt==EInterrupt.eNone)) {
			state= this.currentProcess.executeALine();
			interrupt = this.interruptHandler.process();
		}
		this.interruptHandler.timerStop();
	}
}
