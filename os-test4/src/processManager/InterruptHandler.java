package processManager;

import java.util.Vector;

import processManager.Process.EState;

public class InterruptHandler {
	public enum EInterrupt {
		eNone, eProcessStart, eProcessTerminated, eTimerStart, eTimerFinished, eIOStart, eIOFinished

	}

	public class Interrupt {
		private EInterrupt etype;
		private Object paramerter;

		public Interrupt() {
			this.etype = EInterrupt.eNone;
			this.paramerter = null;
		}

		public EInterrupt getEtype() {
			return etype;
		}

		public void setEtype(EInterrupt etype) {
			this.etype = etype;
		}

		public Object getParamerter() {
			return paramerter;
		}

		public void setParamerter(Object paramerter) {
			this.paramerter = paramerter;
		}
	}

	private Vector<IODevice> ioDevices;

	private Interrupt[] interrupt;
	private int front;
	private int rear;
	private int length;
	private int maxLength;
	private Timer timer;

	public InterruptHandler() {
		this.maxLength = 10;
		this.front = 0;
		this.rear = 0;
		this.length = 0;
		this.interrupt = new Interrupt[this.maxLength];
		this.ioDevices = new Vector<IODevice>();
		this.timer = new Timer(new Interrupt());

	}

	public void startTimer() {
		this.timer.start();
	}
	public void timerStop() {
		this.timer.end();
	}
	public void addInterrupt(Interrupt interrupt) {
		this.interrupt[this.rear] = interrupt;
		this.rear = (this.rear + 1) % this.maxLength;
	}

	public void asscociate(IODevice ioDevice) {
		this.ioDevices.add(ioDevice);
	}

	public EInterrupt process() {
		EInterrupt interrupt = EInterrupt.eNone;
		if (length > 0) {
			// IHR
			switch (this.interrupt[front].getEtype()) {
			case eProcessStart: // 프로세스
				break;
			case eProcessTerminated: // 프로세스
				break;
			case eTimerStart: // 타이머
				break;
			case eTimerFinished: // 타이머
				break;
			case eIOStart: // 현재 진행중인 프로세스
				break;
			case eIOFinished: // IO 디바이스
				break;
			default:
				break;
			}
			this.front = (this.front + 1) % this.maxLength;
			this.length--;
			return interrupt;
		} else {
			return interrupt;
		}
	}



}
