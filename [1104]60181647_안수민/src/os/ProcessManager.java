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


	public static int TIMESLICE = 10;
	public void execute(Process process) {

		Process.EState eProcessState = Process.EState.running;
		process.initialize(TIMESLICE);
		while (eProcessState == Process.EState.running) { // 이 루핑에서 빠지는 방법은 1. 타입 슬라이스가 끝 2. IO 인터럽트 발생 3. 프로그램 종료			
			eProcessState = process.executeALine();	
			
			if(eProcessState == Process.EState.terminated) {
				
			} else if (eProcessState == Process.EState.wait){
				
			} else if (eProcessState == Process.EState.ready){
			
				}
			}
		}
	}

