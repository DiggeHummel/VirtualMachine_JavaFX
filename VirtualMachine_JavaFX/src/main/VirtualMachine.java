package main;

import java.io.File;

import application.ApplicationController;
import gui.FileManager;
import util.CommandType;

public class VirtualMachine {
	
	/* GUI controller */
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
				this.controller.addBeforeArea(parser.CommandToGUI());
				CommandType ct = parser.getCommandType();
				dereferenceCommand(ct, cw, parser);
			}
			cw.close();
		}
	}
	
	private void dereferenceCommand(CommandType ct, CodeWriter cw, Parser parser) {
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
			case C_LABEL:
				cw.writeLabel(parser.arg1());
				break;
			case C_GOTO:
				cw.writeGoto(parser.arg1());
				break;
			case C_IF:
				cw.writeIf(parser.arg1());
				break;
			case C_CALL:
				cw.writeCall(parser.arg1(), parser.arg2());
				break;
			case C_RETURN:
				cw.writeReturn();
				break;
			case C_FUNCTION:
				cw.writeFunction(parser.arg1(), parser.arg2());
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}
}