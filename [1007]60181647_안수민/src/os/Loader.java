package os;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Loader { 
	
	private Process process;
	public Process load(String fileName) throws FileNotFoundException {
		int[] codes= new int[10];
		File file = new File(fileName);
		Scanner scanner = new Scanner(file);
		for(int i= 0; scanner.hasNext();i++) {
			if(i<codes.length) {
				int[] tempCode= new int[i+10];
				for(int i2=0; i2<codes.length;i2++) {
					tempCode[i2] = codes[i2];
				}	
				codes=tempCode;
			}
			codes[i]= scanner.nextInt();
		}
		this.process = new Process(codes, 0);
		return this.process;
		
		
	}

}


