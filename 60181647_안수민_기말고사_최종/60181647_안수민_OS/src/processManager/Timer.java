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
		long start = System.nanoTime();
		long end = System.nanoTime();
		while ((timeslice>(end-start)) && (this.getProcess().getPcb().geteState()==Process.EState.running)) {	
			end = System.nanoTime();
		}
		if((this.getProcess().getPcb().geteState()==Process.EState.running)) {
		this.seteState(EState.eTerminated);
		}
	}
}
