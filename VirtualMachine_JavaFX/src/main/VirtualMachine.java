package main;

import java.io.File;

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
		if(selFile == null) return;
		
		// compile
		Parser parser = new Parser(selFile);
		CodeWriter cw = new CodeWriter(new File(fm.getLastDirectory() + "/" + fm.getFileName() + ".asm"), controller);
		cw.writeInit();
		while(parser.hasNextCommand()) {
			parser.advance();			
			controller.addBeforeArea(parser.CommandToGUI());
			Command command = new Command(parser.getCommand());
			cw.writeCommand(command);
		}
		cw.close();		
	}
}