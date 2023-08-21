package os;

import java.util.Vector;

import processManager.IODevice;
import processManager.Timer;

public class IOManager {
	private Vector<IODevice> ioDevice;
	private Timer timer;
	private Printer printer;
	
	public Vector<IODevice> getIoDevice() {return ioDevice;}
	public void setIoDevice(Vector<IODevice> ioDevice) {this.ioDevice = ioDevice;}	
	
	public IOManager() {
		this.ioDevice = new Vector<IODevice>();
		this.timer = new Timer();
		this.printer = new Printer();
	}

	public void settingIO() {
		this.ioDevice.add(this.timer);
		this.ioDevice.add(this.printer);
	}


	
	
}
