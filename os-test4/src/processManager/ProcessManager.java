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

	public ProcessManager() { // 프로세스 생성과 소멸을 관리
		//this.devices = new Vector<IODevice>();
		this.loader = new Loader(); // 파일에 있는 것을 메모리에 올려줌, 프로세스의 일부라 봐도 된다
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
		this.loader.load(process); // pcb의 주소를 받음
		this.readyQueue.enqueue(process);
	}
	
	private int timer;
	public void run() {
		//타이머를 뜨레드로 작동
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
				case eProcessStart: // 프로세스
					currentProcess = readyQueue.dequeue();
					// timer reset
					break;
				case eProcessTerminated: // 프로세스
					currentProcess = readyQueue.dequeue();
					break;
				case eTimerStart: // 타이머

					break;
				case eTimerFinished: // 타이머
					readyQueue.enqueue(currentProcess);
					currentProcess = readyQueue.dequeue();
					break;
				case eIOStart: // 현재 진행중인 프로세스
					ioQueue.enqueue(currentProcess);
					currentProcess = readyQueue.dequeue();
					break;
				case eIOFinished: // IO 디바이스
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
