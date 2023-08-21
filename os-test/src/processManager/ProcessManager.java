package processManager;

import java.util.Vector;

import os.FileManager;
import os.IOManager;
import os.MemoryManager;
import processManager.Process.ERegister;
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

	public ProcessManager() { // ���μ��� ������ �Ҹ��� ����
		this.loader = new Loader(); // ���Ͽ� �ִ� ���� �޸𸮿� �÷���, ���μ����� �Ϻζ� ���� �ȴ�
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
		this.loader.load(process); // pcb�� �ּҸ� ����
		this.readyQueue.enqueue(process);
	}
	
	//private int timer;
	public void run() {
		//Ÿ�̸Ӹ� �߷���� �۵�
		this.currentProcess = this.readyQueue.dequeue();
		this.currentProcess.setIO(this.ioDevice.get(0));
		currentProcess.setState(EState.running);
		this.currentProcess.runIO();
	while (true) {
		if(this.currentProcess != null) {
		EInterrupt interrupt = this.currentProcess.execute();
		if(interrupt != null) {
		this.interruptQueue.enqueue(new Interrupt(interrupt, this.currentProcess));
		}
		if(this.currentProcess.getPcb().getIODevice().geteState() == IODevice.EState.eTerminated) {
			this.interruptQueue.enqueue(new Interrupt(EInterrupt.eTimerFinished, this.currentProcess));
			this.currentProcess.getPcb().getIODevice().seteState(IODevice.EState.eIdle);
		}
		}
		for(Process process: ioQueue) {
			if(process.getPcb().getIODevice().geteState() == IODevice.EState.eTerminated) {
				this.interruptQueue.enqueue(new Interrupt(EInterrupt.eIOFinished, process));
				process.getPcb().getIODevice().seteState(IODevice.EState.eIdle);
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
		
		public void process() {
			if (interruptQueue.size() > 0) {
				// IHR
				
				Interrupt interrupt = interruptQueue.dequeue();System.out.println(interrupt.getEtype());
				switch (interrupt.getEtype()) {
				case eProcessStart: // ���μ���
					currentProcess = readyQueue.dequeue(); //
					// timer reset
					break;
				case eProcessTerminated: // ���μ���
					currentProcess = readyQueue.dequeue(); //
					break;
				case eTimerStart: // Ÿ�̸�
					break;
				case eTimerFinished: // Ÿ�̸�	
					if(currentProcess !=null) {
					currentProcess.setState(EState.ready);
					readyQueue.enqueue(currentProcess);
					currentProcess = readyQueue.dequeue();
					}
					break;
				case eIOStart: // ���� �������� ���μ���
					int IONum = currentProcess.getPcb().getRegisters().get(ERegister.eStatus.ordinal()).get()>>>4;
					currentProcess.setIO(ioDevice.get(IONum));
					ioQueue.add(currentProcess);
					currentProcess.runIO();
					currentProcess = readyQueue.dequeue(); //
					break;
				case eIOFinished: // IO ����̽� 
					int processId = interrupt.getProcess().getPcb().getId();
					Process finishProcess =null;
					for(int i =0; (i < ioQueue.size()) && finishProcess ==null; i++) {
						if(ioQueue.get(i).getPcb().getId()==processId) {
							finishProcess = ioQueue.get(i);
							finishProcess.setState(EState.ready);
							ioQueue.remove(i);	
							readyQueue.enqueue(finishProcess);
						}
					}
					if(currentProcess==null) {
						currentProcess = readyQueue.dequeue();
					}
					break;
				default:
					break;
				}
				if(currentProcess !=null) {
				currentProcess.setIO(ioDevice.get(0));
				currentProcess.setState(EState.running);
				currentProcess.runIO();
				}
			}
		}

	}

}
