package processManager;

public class Timer extends IODevice {
	public boolean stop;
	
	public void setControl(boolean stop) {
		this.stop = stop;
	}
	public Timer() {
		this.seteState(EState.eIdle);
	}

	public void run() {
		this.seteState(EState.eRunning);
		long timeslice =  300000;
		
		while ((timeslice>0) && (!stop)) {
			long start = System.nanoTime();
			timeslice = timeslice - (System.nanoTime()-start);
		}
		if(!stop) {
		this.seteState(EState.eTerminated);
		}
	}
}
