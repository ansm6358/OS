package os;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Loader {

	public Process load(String fileName) {
		try {
			int stackSegmentSize = 0;
			int[] codes = null;
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println(line);
				if (line.isEmpty()) {

				} else if (line.substring(0, 2).contentEquals("//")) {

				} else if (line.substring(0, 5).contentEquals(".code")) {
					codes = this.parseCode(scanner);
				} else if (line.substring(0, 6).contentEquals(".stack")) {
					stackSegmentSize = this.parseStack(scanner);
				}
			}

			Process process = new Process(stackSegmentSize, codes);
			scanner.close();
			return process;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private int parseStack(Scanner scanner) {
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			System.out.println(line);
			if (line.isEmpty()) {

			} else if (line.substring(0, 2).contentEquals("//")) {

			} else if (line.substring(0, 4).contentEquals("size")) {
				int size = Integer.parseInt(line.substring(7));
				return size;
			}
		}
		return -1;
	}

	private int[] parseCode(Scanner scanner) {
		ArrayList<Integer> codeset = new ArrayList<>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			System.out.println(line);
			if (line.isEmpty()) {

			} else if (line.substring(0, 2).contentEquals("//")) {

			} else {
				codeset.add(compile(line));
			}
		}
		int[] codes = new int[codeset.size()];
		for (int i = 0; i < codeset.size(); i++) {
			codes[i] = codeset.get(i);
		}

		return codes;
	}

	private int compile(String line) { // ldi sta addi cmp igz halt
		int value = 0;
		int codeId = 0;
		int code = 0;
		Scanner scanner = new Scanner(line);
		String instruction = scanner.next();

		if (instruction.equals("halt")) { // 0
			codeId = 0;
			value = 0;
		} else if (instruction.equals("sta")) { // 1
			codeId = 1;
			value = scanner.nextInt();

		} else if (instruction.equals("ldi")) { // 2
			codeId = 2;
			value = scanner.nextInt();

		} else if (instruction.equals("cmp")) { // 3
			codeId = 3;
			value = scanner.nextInt();
		} else if (instruction.equals("igz")) { // 4
			codeId = 4;
			value = scanner.nextInt();

		} else if (instruction.equals("addi")) { // 5
			codeId = 5;
			value = scanner.nextInt();
		}
		
		code = (codeId << 16) + value;
		scanner.close();
		return code;
	}

}
