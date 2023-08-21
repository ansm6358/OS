package processManager;

import java.util.Vector;

import os.FileManager;
import os.MemoryManager;
import processManager.InterruptHandler.EInterrupt;
import processManager.Process.EState;

public class ProcessManager {

	// components
	private Loader loader;
	private InterruptHandler interruptHandler;

	private Process currentProcess;
	private Queue<Process> readyQueue;
	private Vector<Process> ioQueue;
	private Queue<EInterrupt> interruptQueue;

	// association
	private FileManager fileManager;
	private MemoryManager memoryManager;

	public ProcessManager() { // ���μ��� ������ �Ҹ��� ����
		//this.devices = new Vector<IODevice>();
		this.loader = new Loader(); // ���Ͽ� �ִ� ���� �޸𸮿� �÷���, ���μ����� �Ϻζ� ���� �ȴ�
		this.interruptHandler = new InterruptHandler();
		this.readyQueue = new Queue<Process>();
		this.ioQueue = new Vector<Process>();
		this.interruptQueue = new Queue<EInterrupt>();
	}

	public void associate(MemoryManager memoryManager, FileManager fileManager) {
		this.fileManager = fileManager;
		this.memoryManager = memoryManager;
	}

	// mili seconds
	public static int TIMESLICE = 10;

	public void execute(Process process) {
		this.loader.load(process); // pcb�� �ּҸ� ����
		this.readyQueue.enqueue(process);
	}
	
	private int timer;
	public void run() {
		//Ÿ�̸Ӹ� �߷���� �۵�
	this.timer = 0;//this.interruptHandler.startTimer();this.interruptHandler.timerStop();
	while (true) {
		Interrupt interrupt = this.currentProcess.execute();
		if(interrupt != null) {
		this.interruptQueue.enqueue(new Interrupt());
		}
		//check devices
		//timer
		
		timer++;
		if(timer>5) {
			this.interruptQueue.enqueue(new Interrupt(EInterrupt.eTimerFinished, this.currentProcess), this);
		}
		
		// chech device - ioQueue
		for(Process process: ioQueue) {
			if(process.getPcb().getIODevice().getState() == IODevice.EIODeviceState.eTerminated) {
				this.interruptQueue.enqueue(new Interrupt(EInterrupt.eIOFinished, process));
			}
		}
		//printer
		//file
		
		this.interruptHandler.process();
	}
		
	}
	public enum EInterrupt {
		eNone, eProcessStart, eProcessTerminated, eTimerStart, eTimerFinished, eIOStart, eIOFinished

	}

	public class InterruptHandler {

		public InterruptHandler() {
			
		}

		
		public void process() {
			if (interruptQueue.size() > 0) {
				// IHR
				
				switch (interruptQueue.dequeue()) {
				case eProcessStart: // ���μ���
					currentProcess = readyQueue.dequeue();
					// timer reset
					break;
				case eProcessTerminated: // ���μ���
					currentProcess = readyQueue.dequeue();
					break;
				case eTimerStart: // Ÿ�̸�

					break;
				case eTimerFinished: // Ÿ�̸�
					readyQueue.enqueue(currentProcess);
					currentProcess = readyQueue.dequeue();
					break;
				case eIOStart: // ���� �������� ���μ���
					ioQueue.enqueue(currentProcess);
					currentProcess = readyQueue.dequeue();
					break;
				case eIOFinished: // IO ����̽�
					int processId = (Integer) this.interrupt[front].getParamerter();
					Process peocess = ioQueue.find(processId);
					readyQueue.enqueue(peocess);
					break;
				default:
					break;
				}

			}
		}

	}
}
