package main;

import java.io.File;

import application.ApplicationController;
import gui.FileManager;
import util.CommandType;

public class VirtualMachine {
	
	private final ApplicationController controller;
	
	public VirtualMachine(ApplicationController controller) {
		this.controller = controller;
	}
	
	public void vmToAsm() {
		FileManager fm = new FileManager();
		File selectedFile = fm.chooseFile();
		if(selectedFile != null) {
			this.controller.setBeforeLabel("Before: .vm");
			this.controller.setAfterLabel("After: .asm");
			Parser parser = new Parser(selectedFile);
			CodeWriter cw = new CodeWriter(new File(fm.getLastDirectory() + "/" + fm.getFileName() + ".asm"));
			cw.setApplicationController(this.controller);
			while(parser.hasNextCommand()) {
				parser.advance();			
				controller.addBeforeArea(parser.CommandToGUI());
				CommandType ct = parser.getCommandType();
				if(ct != null) {
					switch(ct) {
					case C_ARITHMETIC:
						cw.writeArithmetic(parser.getCommand());					
						break;
					case C_POP:
						cw.writePushPop(parser.getCommand(), parser.arg1(), parser.arg2());
						break;
					case C_PUSH:
						cw.writePushPop(parser.getCommand(), parser.arg1(), parser.arg2());
						break;
					default:
						throw new IllegalArgumentException();
					}
				}
			}
			cw.close();
		}
	}
}