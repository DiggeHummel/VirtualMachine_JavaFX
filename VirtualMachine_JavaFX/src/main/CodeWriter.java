package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import application.ApplicationController;
import util.Command;
import util.Translator;

public class CodeWriter {

	/* variables */
	private ApplicationController controller;
	private BufferedWriter bw;
	private int JMP_Count = 0;
	private int Instruction_Count = -1;
	private final Translator translator;

	/* constructor */
	public CodeWriter(File outputFile, ApplicationController controller) {
		try {
			this.bw = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.translator = new Translator();
		this.controller = controller;
	}

	/* public methods */
	public void writeCommand(Command c) {
		try {
			this.Instruction_Count++;
			String code = translator.getASMCode(c, JMP_Count, Instruction_Count);
			if (c.getFlag().equals(Translator.JMP_FLAG))
				JMP_Count++;
			code = code.replaceAll("\n", "\r\n");
			this.controller.addAfterArea(code);
			bw.write(code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}