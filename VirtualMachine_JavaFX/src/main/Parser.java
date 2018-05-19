package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import util.CommandType;
import util.StringModifier;

public class Parser {

	/* fields */
	private String[] arithmeticCommands = {"add", "sub", "neg", "eq", "lt", "gt", "and", "or", "not"};
	
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
	
	/* methods */
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
		return true;
	}
	
	public void advance() {		
		this.command = this.line.split(" ");
		if(this.command.length > 3) throw new IllegalArgumentException();
	}
	
	public CommandType getCommandType() {
		if(this.line != null && !command[0].equals("")) {
			if(containsArithmeticCommand(command[0])) return CommandType.C_ARITHMETIC;
			switch(command[0]) {
			case "pop": 
				return CommandType.C_POP;
			case "push": 
				return CommandType.C_PUSH;
			case "if-goto":
				return CommandType.C_IF;
			case "goto":
				return CommandType.C_GOTO;
			case "call":
				return CommandType.C_CALL;
			case "label":
				return CommandType.C_LABEL;
			case "return":
				return CommandType.C_RETURN;
			case "function":
				return CommandType.C_FUNCTION;
			default:
				throw new IllegalArgumentException();
			}
		}
		return null;
	}
	
	public String getCommand() {
		return this.command[0];
	}
	
	public String arg1() {
		return  command[1];
	}
	
	public int arg2() {
		return Integer.parseInt(command[2]);
	}
	
	public String CommandToGUI() {
		return this.line;
	}
	
	private void close() {
		try {
			this.br.close();
		}catch(IOException e){
        	e.printStackTrace();
        }
	}
	
	private boolean containsArithmeticCommand(String suspect) {
		for(int i = 0; i < this.arithmeticCommands.length; i++) {
			if(suspect.equals(this.arithmeticCommands[i])) return true;
		}
		return false;
	}
}