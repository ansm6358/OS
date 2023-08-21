package os;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import compiler.Compile;
import processManager.Process;

public class FileManager {
	private int processNum=0;
	
	private int parseStack(Scanner scanner) {
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.isEmpty()) {

			} else if (line.substring(0, 2).contentEquals("//")) {

			} else if (line.substring(0, 4).contentEquals("size")) {
				int size = Integer.parseInt(line.substring(7));
				return size;
			}
		}
		return 0;
	}

	private int[] parseCode(Scanner scanner) {
		ArrayList<Integer> codeset = new ArrayList<Integer>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			codeset.add(Integer.parseInt(line));			
		}
		int[] codes = new int[codeset.size()];
		for (int i = 0; i < codeset.size(); i++) {
			codes[i] = codeset.get(i);
		}
		
		return codes;
	}

	public Process getFile(String fileName) {
		try {
			Compile compiler = new Compile();
			fileName = compiler.compiled(fileName);

			int stackSegmentSize = 0;
			int[] codes = null;

			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.isEmpty()) {

				} else if (line.substring(0, 2).contentEquals("//")) {
					
				} else if (line.substring(0, 5).contentEquals(".code")) {
					codes = this.parseCode(scanner);
				} else if (line.substring(0, 6).contentEquals(".stack")) {
					stackSegmentSize = this.parseStack(scanner);
				}
			}

			Process process = new Process(stackSegmentSize, codes, this.processNum);
			this.processNum++;
			scanner.close();
			return process;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
