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
			String code = translator.getASMCode(c);
			code = "//" + c.getCommand() + "\r\n" + code.replaceAll("\n", "\r\n");
			this.controller.addAfterArea(code);
			bw.write(code);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeBootstrap(String fileName) {
		if (!fileName.equals("Sys.vm")) {
			try {
				String boot = "//bootstrap \r\n" + translator.bootstrap().replaceAll("\n", "\r\n");
				this.controller.addAfterArea(boot);
				bw.write(boot);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void writeFileName(String fileName) {
		try {
			this.controller.addAfterArea("//new File: " + fileName);
			bw.write("//new File: " + fileName + "\r\n");
			translator.setFileName(fileName);
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