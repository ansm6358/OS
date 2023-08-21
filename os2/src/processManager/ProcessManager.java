package processManager;

import java.util.Vector;

import os.FileManager;
import os.MemoryManager;

public class ProcessManager {
	
	private enum EIODeviceState {
		eIdle,
		eRunning,
		eTerminated,
		eError
	}
	private class IODevice{
		private String deviceName;
		private EIODeviceState eIODeviceState;
		
		public IODevice() {
			this.eIODeviceState = EIODeviceState.eIdle;
			
		}
	}
	private Vector<IODevice> devices;
	//
	// components
	private Loader loader;

	private Process currentProcess;
	private ProcessQueue readyQueue;
	private ProcessQueue ioQueue;

	// association
	private FileManager fileManager;
	private MemoryManager memoryManager;

	public ProcessManager() { // 프로세스 생성과 소멸을 관리
		this.devices = new Vector<IODevice>();
		

		this.loader = new Loader(); // 파일에 있는 것을 메모리에 올려줌, 프로세스의 일부라 봐도 된다
		this.readyQueue = new ProcessQueue();
		this.ioQueue = new ProcessQueue();
	}

	public void associate(MemoryManager memoryManager, FileManager fileManager) {
		this.fileManager = fileManager;
		this.memoryManager = memoryManager;
	}

	// mili seconds
	public static int TIMESLICE = 10;

	public void execute(Process process) {
		process = this.loader.load(process); // pcb의 주소를 받음
		this.readyQueue.enqueue(process);
	}

	public void run() {
		//타이머를 뜨레드로 작동
		Interrupt interrupt = new Interrupt();
		Timer timer = new Timer();
		while (true) {
			this.currentProcess.execute(interrupt);
			
			// IHR
			switch (this.eInterrupt) {
			case eTimeOut: //타이머
				break;
			case eIOStarted: //현재 진행중인 프로세스
				break;
			case eIOFinisied: //IO 디바이스
				break;
			case eTerminate: //프로세스
				break;
			default:
				break;
			}
		}
	}
}
