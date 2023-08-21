package os;

import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
			OperatingSystem operatingSystem = new OperatingSystem();
			operatingSystem.associate();
			operatingSystem.run();
	}

}
