package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Compile {
	private enum EInstruction {
		halt, lda, ldv, sta, add, adda, sub, mul, div, diva, and, not, jmp, jmz, cmp, prt, loof, newh
	};

	public String compiled(String fileName) {
		String compilefileName = "exe//compile.txt";
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);

			FileWriter fileWriter = new FileWriter(compilefileName, false);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.length() < 5) {
					fileWriter.write(line + "\r\n");
					fileWriter.flush();
				} else if (line.substring(0, 5).contentEquals(".code")) {
					fileWriter.write(line + "\r\n");
					fileWriter.flush();
					compileCode(scanner, fileWriter);
				} else {
					fileWriter.write(line + "\r\n");
					fileWriter.flush();
				}
			}

			scanner.close();
			fileWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return compilefileName;
	}

	private void compileCode(Scanner scanner, FileWriter fileWriter) {
		try {
			int tempLoof = 0;
			int codeAddress = 0;
			while (scanner.hasNextLine()) {
				String code = scanner.nextLine();
				Scanner compileScanner = new Scanner(code);
				String instruct = compileScanner.next();

				for (EInstruction eInstruction : EInstruction.values()) {
					if (eInstruction.toString().equals(instruct)) {
						int data = 0;
						
						if (eInstruction == EInstruction.loof) {
							tempLoof = codeAddress;
							data = 0;
						} else if (eInstruction == EInstruction.jmz) {
							data = tempLoof;
						} else if (eInstruction == EInstruction.halt) {
							data = 0;
						} else {
							data = compileScanner.nextInt();
						}
						int codes = (eInstruction.ordinal() << 16) + data;

						fileWriter.write(codes + "\r\n");
						fileWriter.flush();
					}
				}
				codeAddress++;
				compileScanner.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
