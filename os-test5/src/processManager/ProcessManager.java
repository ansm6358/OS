package processManager;

import java.util.Vector;

import os.FileManager;
import os.IOManager;
import os.MemoryManager;
import processManager.Process.EState;

public class ProcessManager {

	// components
	private Loader loader;
	private InterruptHandler interruptHandler;

	private Process currentProcess;
	private Queue<Process> readyQueue;
	private Vector<Process> ioQueue;
	private Queue<Interrupt> interruptQueue;
	private Vector<IODevice> ioDevice;
	// association
	private FileManager fileManager;
	private MemoryManager memoryManager;
	private IOManager ioManager;

	public ProcessManager() { // 프로세스 생성과 소멸을 관리
		//this.devices = new Vector<IODevice>();
		this.loader = new Loader(); // 파일에 있는 것을 메모리에 올려줌, 프로세스의 일부라 봐도 된다
		this.interruptHandler = new InterruptHandler();
		this.readyQueue = new Queue<Process>();
		this.ioQueue = new Vector<Process>();
		this.interruptQueue = new Queue<Interrupt>();
	}

	public void initialize() {
		this.ioDevice = this.ioManager.getIoDevice();
	}
	public void associate(MemoryManager memoryManager, FileManager fileManager, IOManager ioManager) {
		this.fileManager = fileManager;
		this.memoryManager = memoryManager;
		this.ioManager = ioManager;
		this.loader.associate(this.memoryManager);
	}

	// mili seconds
	public static int TIMESLICE = 10;

	public void execute(Process process) {
		this.loader.load(process); // pcb의 주소를 받음
		this.readyQueue.enqueue(process);
	}
	
	//private int timer;
	public void run() {
		//타이머를 뜨레드로 작동
		this.currentProcess = this.readyQueue.dequeue();
		this.currentProcess.setIO(this.ioDevice.get(0));
		this.currentProcess.runIO();
	//this.timer = 0;//this.interruptHandler.startTimer();this.interruptHandler.timerStop();
	while (this.currentProcess != null) {
		EInterrupt interrupt = this.currentProcess.execute();
		if(interrupt != null) {
		this.interruptQueue.enqueue(new Interrupt(interrupt, this.currentProcess));
		}
		//check devices
		//timer
//		timer++;
//		if(timer>5) {
//			this.interruptQueue.enqueue(new Interrupt(EInterrupt.eTimerFinished, this.currentProcess));
//		}
		if(this.currentProcess.getPcb().getIODevice().geteState() == IODevice.EState.eTerminated) {
			this.interruptQueue.enqueue(new Interrupt(EInterrupt.eTimerFinished, this.currentProcess));
		}
		// chech device - ioQueue
		for(Process process: ioQueue) {
			if(process.getPcb().getIODevice().geteState() == IODevice.EState.eTerminated) {
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
				currentProcess.stopIO();
				Interrupt interrupt = interruptQueue.dequeue();
				switch (interrupt.getEtype()) {
				case eProcessStart: // 프로세스
					currentProcess = readyQueue.dequeue(); //
					// timer reset
					break;
				case eProcessTerminated: // 프로세스
					currentProcess = readyQueue.dequeue(); //
					break;
				case eTimerStart: // 타이머
					break;
				case eTimerFinished: // 타이머
					readyQueue.enqueue(currentProcess);
					currentProcess = readyQueue.dequeue();  //
					break;
				case eIOStart: // 현재 진행중인 프로세스
					ioQueue.add(currentProcess);
					currentProcess = readyQueue.dequeue(); //
					break;
				case eIOFinished: // IO 디바이스 
					int processId = interrupt.getProcess().getPcb().getId();
					Process finishProcess =null;
					for(int i =0; (i < ioQueue.size()) && finishProcess ==null; i++) {
						if(ioQueue.get(i).getPcb().getId()==processId) {
							finishProcess = ioQueue.get(i);
							ioQueue.remove(i);	
							readyQueue.enqueue(finishProcess);
						}
					}			
					break;
				default:
					break;
				}
				currentProcess.setIO(ioDevice.get(0));
				currentProcess.runIO();
			}
		}

	}

}
