package processManager;

import processManager.InterruptHandler.EInterrupt;
import processManager.InterruptHandler.Interrupt;

public class Timer extends Thread {
	private Interrupt interrupt;
	private boolean stop;

	public Timer(Interrupt interrupt) {
		this.interrupt = interrupt;
		this.stop = true;
	}

	public void end() {
		this.stop = true;
	}

	public void run() {
		this.stop = false;
		long timeslice =  300000;
		
		while ((timeslice>0) && (stop ==false)) {
			long start = System.nanoTime();
			timeslice = 30000 - (System.nanoTime()-start);
		}
		if(!(timeslice>0)) {
		this.interrupt.setEtype(EInterrupt.eTimerFinished);
		}
	}
}
