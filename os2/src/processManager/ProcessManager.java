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

	public ProcessManager() { // ���μ��� ������ �Ҹ��� ����
		this.devices = new Vector<IODevice>();
		

		this.loader = new Loader(); // ���Ͽ� �ִ� ���� �޸𸮿� �÷���, ���μ����� �Ϻζ� ���� �ȴ�
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
		process = this.loader.load(process); // pcb�� �ּҸ� ����
		this.readyQueue.enqueue(process);
	}

	public void run() {
		//Ÿ�̸Ӹ� �߷���� �۵�
		Interrupt interrupt = new Interrupt();
		Timer timer = new Timer();
		while (true) {
			this.currentProcess.execute(interrupt);
			
			// IHR
			switch (this.eInterrupt) {
			case eTimeOut: //Ÿ�̸�
				break;
			case eIOStarted: //���� �������� ���μ���
				break;
			case eIOFinisied: //IO ����̽�
				break;
			case eTerminate: //���μ���
				break;
			default:
				break;
			}
		}
	}
}
