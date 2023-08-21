package os;

public class ProcessManager {
	private Loader loader;

	private ProcessQueue readyQueue;
	private ProcessQueue ioQueue;

	private FileManager fileManager;
	private MemoryManager memoryManager;

	public ProcessManager() { // ���μ��� ������ �Ҹ��� ����
		this.loader = new Loader(); // ���Ͽ� �ִ� ���� �޸𸮿� �÷���, ���μ����� �Ϻζ� ���� �ȴ�
		this.readyQueue = new ProcessQueue();
		this.ioQueue = new ProcessQueue();
	}

	public void associate(MemoryManager memoryManager, FileManager fileManager) {
		this.fileManager = fileManager;
		this.memoryManager = memoryManager;
	}


	public static int TIMESLICE = 10;
	public void execute(Process process) {

		Process.EState eProcessState = Process.EState.running;
		process.initialize(TIMESLICE);
		while (eProcessState == Process.EState.running) { // �� ���ο��� ������ ����� 1. Ÿ�� �����̽��� �� 2. IO ���ͷ�Ʈ �߻� 3. ���α׷� ����			
			eProcessState = process.executeALine();	
			
			if(eProcessState == Process.EState.terminated) {
				
			} else if (eProcessState == Process.EState.wait){
				
			} else if (eProcessState == Process.EState.ready){
			
				}
			}
		}
	}

