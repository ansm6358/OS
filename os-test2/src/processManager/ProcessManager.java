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

	public ProcessManager() { // ���μ��� ������ �Ҹ��� ����
		//this.devices = new Vector<IODevice>();
		this.loader = new Loader(); // ���Ͽ� �ִ� ���� �޸𸮿� �÷���, ���μ����� �Ϻζ� ���� �ȴ�
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
		this.loader.load(process); // pcb�� �ּҸ� ����
		this.readyQueue.enqueue(process.getPcb().getId());
	}

	public void run() {
		//Ÿ�̸Ӹ� �߷���� �۵�
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
