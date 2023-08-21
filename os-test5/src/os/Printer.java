package os;

import processManager.IODevice;
import processManager.Process.ERegister;

public class Printer extends IODevice {
	public Printer() {
		this.seteState(EState.eIdle);
	}
	public void run() {
		this.seteState(EState.eRunning);
		System.out.println(this.getProcess().getPcb().getRegisters().get(ERegister.eAC.ordinal()).get());
		this.seteState(EState.eTerminated);
	}
}
