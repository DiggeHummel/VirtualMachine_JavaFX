package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import application.ApplicationController;
import util.Command;
import util.Translator;

public class CodeWriter {

	/* VAR */
	private ApplicationController controller;
	private BufferedWriter bw;
	private int JMP_Count = 0;
	private final Translator translator;

	public CodeWriter(File outputFile) {
		this.translator = new Translator();
		try {
			this.bw = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeCommand(Command c) {
		try {
			String code = translator.getASMCode(c.getFlag(), c.getCommand(), c.getSegment(), c.getIndex(), JMP_Count);
			if (c.getFlag().equals(Translator.JMP_FLAG))
				JMP_Count++;
			code = code.replaceAll("\n", "\r\n");
			this.controller.addAfterArea(code);
			bw.write(code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeInit() {
		try {
			bw.write("Assembler Code generated by VM\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeCall(String functionName, int numArgs) {
	}

	public void writeReturn() {

	}

	public void writeFunction(String functionName, int numLocals) {

	}

	public void close() {
		try {
			this.bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setApplicationController(ApplicationController controller) {
		this.controller = controller;
	}

}