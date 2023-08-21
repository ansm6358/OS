package os;

public class ProcessManager {
	private Loader loader;

	private ProcessQueue readyQueue;
	private ProcessQueue ioQueue;

	private FileManager fileManager;
	private MemoryManager memoryManager;

	public ProcessManager() { // 프로세스 생성과 소멸을 관리
		this.loader = new Loader(); // 파일에 있는 것을 메모리에 올려줌, 프로세스의 일부라 봐도 된다
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
		while (eProcessState == EProcessState.running) { // 이 루핑에서 빠지는 방법은 1. 타입 슬라이스가 끝 2. IO 인터럽트 발생 3. 프로그램 종료
			long start = System.nanoTime();
			if (process.executeALine()) {
				System.out.println("exe가 끝났습니다.");
				eProcessState = EProcessState.terminated;
				process.setState(eProcessState);
			} else if (process.checkInterruptStatus()) {
				eProcessState = EProcessState.wait;
				process.setState(eProcessState);
			} else {
				if (checkTimeExpired(start)) { 
					//eProcessState = EProcessState.ready;
					process.setState(eProcessState);
					System.out.println("타임 슬라이스가 끝났습니다.");
				}
			}
			
			System.out.println("남은 Timer: "+TIMESLICE);
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
