package processManager;

public class IODevice extends Thread {
	public enum EState {
		eIdle,
		eRunning,
		eTerminated,
		eError
	}

		private String deviceName;
		private EState eState;
		private Process process;
		
		public IODevice() {
			this.eState = EState.eIdle;	
	}

		public String getDeviceName() {
			return deviceName;
		}

		public void setDeviceName(String deviceName) {
			this.deviceName = deviceName;
		}

		public EState geteState() {
			return eState;
		}

		public void seteState(EState eState) {
			this.eState = eState;
		}

		public void setControl(boolean b) {
			
		}

		public void setProcess(Process process) {
			this.process = process;
		}

		public Process getProcess() {
			return process;
		}
		
}
