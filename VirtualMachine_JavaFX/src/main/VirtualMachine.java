package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import application.ApplicationController;
import gui.FileManager;
import util.Command;

public class VirtualMachine {

	public VirtualMachine() {
	}

	public void vmToAsm(ApplicationController controller) {
		// GUI Labels
		controller.setBeforeLabel("Before: .vm");
		controller.setAfterLabel("After: .asm");

		// ChooseFile
		FileManager fm = new FileManager();
		File selFile = fm.chooseFile();
		if (selFile == null)
			return;

		// compile
		Parser parser = new Parser(selFile);
		CodeWriter cw = new CodeWriter(new File(fm.getLastDirectory() + "/" + fm.getFileName() + ".asm"), controller);
		cw.writeInit();
		while (parser.hasNextCommand()) {
			parser.advance();
			controller.addBeforeArea(parser.CommandToGUI());
			Command command = new Command(parser.getCommand());
			cw.writeCommand(command);
		}
		cw.close();
	}

	public void mergeASMFiles(ApplicationController controller, String filename) {
		// GUI Labels
		controller.setBeforeLabel("Before: *.asm");
		controller.setAfterLabel("After:" + filename + ".asm");

		// ChooseFiles
		FileManager fm = new FileManager();
		List<File> selFiles = fm.chooseFiles();
		if (selFiles != null) {
			// read
			String completeInput = "";
			try {
				BufferedReader br;
				for (int i = 0; i < selFiles.size(); i++) {
					String input = "";
					controller.addBeforeArea("File: [" + selFiles.get(i).getName() + "]");
					br = new BufferedReader(new FileReader(selFiles.get(i)));
					String line = null;
					while ((line = br.readLine()) != null)
						input += line + "\n\r";
					br.close();
					controller.addBeforeArea(input.replace("\n\r", ""));
					completeInput += input;
				}

				// write
				File file = new File(selFiles.get(0).getParentFile().toString() + "/" + filename + ".asm");
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(completeInput);
				bw.close();
				controller.addAfterArea(filename + ".asm are created.\n\r" + file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}