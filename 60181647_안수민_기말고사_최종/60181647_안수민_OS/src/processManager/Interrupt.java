package processManager;

import processManager.ProcessManager.EInterrupt;

public class Interrupt {
		private EInterrupt etype;
		private Object paramerter;
		private Process process;

		public Interrupt() {
			this.etype = EInterrupt.eNone;
			this.paramerter = null;
		}
		public Interrupt(EInterrupt eInterrupt, Process process) {
			this.etype = eInterrupt;
			this.process = process;
		}

		public Process getProcess() {return process;}
		public void setProcess(Process process) {this.process = process;}
		
		public EInterrupt getEtype() {return etype;}
		public void setEtype(EInterrupt etype) {this.etype = etype;}
		public Object getParamerter() {return paramerter;}
		public void setParamerter(Object paramerter) {this.paramerter = paramerter;}
	
}
