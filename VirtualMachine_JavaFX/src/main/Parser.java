package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import util.StringModifier;

public class Parser {
	
	/* variables */
	private BufferedReader br;
	private String line;
	private String[] command;

	/* constructor */
	public Parser(File inputFile) {
		try {
			this.br = new BufferedReader(new FileReader(inputFile));
			this.line = null;
	    }catch(IOException e){
        	e.printStackTrace();
        }
	}
	
	/* public methods */
	public boolean hasNextCommand() {
		do {
			try {
				this.line = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(this.line == null) {
				this.close();
				return false;
			}
			this.line = StringModifier.removeByMarker(this.line, "//");
		}
		while(this.line.equals(""));
		System.out.println(line);
		return true;
	}
	
	public void advance() {		
		this.command = this.line.split(" ");
		if(this.command.length > 3) throw new IllegalArgumentException();
	}	
	
	public String[] getCommand() {
		return this.command;
	}
	
	public String CommandToGUI() {
		return this.line;
	}
	
	
	/* private methods */
	private void close() {
		try {
			this.br.close();
		}catch(IOException e){
        	e.printStackTrace();
        }
	}
}