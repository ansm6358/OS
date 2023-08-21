package os;

import cpu.CentralProcessingUnit;

public class Main {
	
	static public class BIOS{
		public void run() {
			
		}
	}
	public static void main(String[] args) {
			
			BIOS bios = new BIOS();
			bios.run();
			
			OperatingSystem operatingSystem = new OperatingSystem();
			operatingSystem.initialize();
			operatingSystem.run();
			operatingSystem.finalize();
	}

}
