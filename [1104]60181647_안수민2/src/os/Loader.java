package os;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Loader {
	private int loopTemp=-1;
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
		ArrayList<Integer> codes = new ArrayList<>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			 System.out.println(line);
			if (line.isEmpty()) {

			} else if (line.substring(0, 2).contentEquals("//")) {

			} else {
				codes.addAll(compile(line, codes.size()));
			}
		}
		return null;
	}

	private ArrayList<Integer> compile(String line, int codeNum) {
		ArrayList<Integer> codes = new ArrayList<>();
		int address = 0;
		int value = 0;
		int code = 0;

		Scanner scanner = new Scanner(line);
		String instruction = scanner.next();

		if (instruction.equals("halt")) { // 0
			codes.add(0);
		} else if (instruction.equals("sti")) { // 1
			String temp = scanner.next();
			address = Integer.parseInt(temp.substring(0, temp.length() - 1));
			String tempValue = scanner.next();

			if (checkNum(tempValue)) {
				value = Integer.parseInt(tempValue);
				codes.add((3 << 16) + value);
				codes.add((2 << 16) + address);
			} else {
				for (int i = 0; i < tempValue.length(); i++) {
					value = tempValue.charAt(i);
					codes.add((3 << 16) + value);
					codes.add((2 << 16) + address);
					address = address + 2;
				}
				codes.add((2 << 16) + 0);
			}
		} else if (instruction.equals("sta")) { // 2
			String temp = scanner.next();
			address = Integer.parseInt(temp.substring(0, temp.length()));
			codes.add((2 << 16) + address);
		} else if (instruction.equals("ldi")) { // 3
			String temp = scanner.next();
			value = Integer.parseInt(temp);
				codes.add((3 << 16) + value);
			
		} else if (instruction.equals("lda")) { // 4
			String temp = scanner.next();
			address = Integer.parseInt(temp);
				codes.add((4 << 16) + address);
		} else if (instruction.equals("cmp")) { // 5
			String temp = scanner.next();
			address = Integer.parseInt(temp);
				codes.add((5 << 16) + address);
		} else if (instruction.equals("igz")) { // 6
			String temp = scanner.next();
				codes.add((6 << 16) + loopTemp);
				
		} else if (instruction.equals("intr")) { // 7
			String temp = scanner.next();
			address = Integer.parseInt(temp.substring(0, temp.length() - 1));
			value = Integer.parseInt(scanner.next());
			codes.add((7 << 16) + (address<<8)+value);
			
		} else if (instruction.equals("loop:")) { // 8
			codes.add((8<<16)+codeNum);
		} else if (instruction.equals("addi")) { // 9
			String temp = scanner.next();
			value = Integer.parseInt(temp);
			codes.add((9 << 16) + value);
		}
		
		return codes;
	}

	private boolean checkNum(String tempValue) {
		try {
			Double.parseDouble(tempValue);

		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
