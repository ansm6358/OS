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

	public enum EProcessState {nnew, running, wait, ready, terminated};
	public static long TIMESLICE = 300000;
	public void execute(Process process) {

		EProcessState eProcessState = EProcessState.running;
		process.setState(eProcessState);
		resetTimer(TIMESLICE);
		while (eProcessState == EProcessState.running) { // �� ���ο��� ������ ����� 1. Ÿ�� �����̽��� �� 2. IO ���ͷ�Ʈ �߻� 3. ���α׷� ����
			long start = System.nanoTime();
			if (process.executeALine()) {
				System.out.println("exe�� �������ϴ�.");
				eProcessState = EProcessState.terminated;
				process.setState(eProcessState);
			} else if (process.checkInterruptStatus()) {
				eProcessState = EProcessState.wait;
				process.setState(eProcessState);
			} else {
				if (checkTimeExpired(start)) { 
					//eProcessState = EProcessState.ready;
					process.setState(eProcessState);
					System.out.println("Ÿ�� �����̽��� �������ϴ�.");
				}
			}
			
			System.out.println("���� Timer: "+TIMESLICE);
		}
	}

	private boolean checkTimeExpired(long start) {
		long end = System.nanoTime();
		TIMESLICE = TIMESLICE -(end-start);
		if(TIMESLICE<=0) {
			return true;
		} else {
		return false;
		}
	}

	private void resetTimer(long tIMESLICE) {
		tIMESLICE = 300000;
	}
}
