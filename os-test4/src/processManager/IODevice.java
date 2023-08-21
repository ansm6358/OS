package processManager;

public class IODevice extends Thread {
	public enum EIODeviceState {
		eIdle,
		eRunning,
		eTerminated,
		eError
	}

		private String deviceName;
		private EIODeviceState eIODeviceState;
		
		public IODevice() {
			this.eIODeviceState = EIODeviceState.eIdle;
			
	}
}
