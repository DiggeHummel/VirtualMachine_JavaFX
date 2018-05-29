package main;

import java.io.File;
import java.util.List;

import application.ApplicationController;
import gui.FileManager;
import util.Command;

public class VirtualMachine {

	public VirtualMachine() {
	}

	public void vmToAsm(ApplicationController controller, boolean multiple) {
		// GUI Labels
		controller.setBeforeLabel("Before: .vm");
		controller.setAfterLabel("After: .asm");

		// ChooseFiles
		FileManager fm = new FileManager();
		List<File> selFiles = fm.chooseFiles();
		if (selFiles == null)
			return;
		System.out.println(selFiles.toString());

		// OutputFile
		File outFile;
		if (multiple)
			outFile = new File(fm.getLastDirectory() + "/" + controller.getFileName() + ".asm");
		else
			outFile = new File(fm.getLastDirectory() + "/" + fm.getFileName() + ".asm");

		// init CodeWriter
		CodeWriter cw = new CodeWriter(outFile, controller);

		// compile
		for (File in : selFiles) {
			Parser parser = new Parser(in);
			cw.writeFileName(in.getName());
			while (parser.hasNextCommand()) {
				parser.advance();
				controller.addBeforeArea(parser.CommandToGUI());
				Command command = new Command(parser.getCommand());
				cw.writeCommand(command);
			}
		}
		cw.close();
	}
}