package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import application.ApplicationController;

public class CodeWriter {
	
	/* fields */
	private final String ARITHMETIC_A = "@SP\n" + "AM=M-1\n" + "D=M\n" + "A=A-1\n"; //!SP is reduced by 1
	private final String DISSOLVE_POINTER = "A=M\n";
	
	/* VAR */
	private ApplicationController controller;
	private BufferedWriter bw;
	private int JMP_Count = 0;
	
	public CodeWriter(File outputFile) {
		try {
			this.bw = new BufferedWriter(new FileWriter(outputFile));
	    }catch(IOException e){
        	e.printStackTrace();
        }
	}
	
	public void setFileName(String fileName) {
		//useless
	}
	
	private String ARITHMETIC_JMP(String JMP_Case) {
		return this.ARITHMETIC_A +
				"D=M-D\n" +
				"@PUT_TRUE" + JMP_Count + "\n" + 
				"D;" + JMP_Case + "\n" + 
				"@SP\n" +
				"A=M-1\n" +
				"M=0\n" +
				"@SKIP" + JMP_Count + "\n" +
				"0;JMP\n" + 
				"(PUT_TRUE" + JMP_Count + ")\n" + 
				"@SP\n" +
				"A=M-1\n" +
				"M=-1\n" +
				"(SKIP" + JMP_Count + ")\n";
	}
	
	public void writeArithmetic(String command) {
		String code = "";
		switch(command) {
		case "add": 
			code = this.ARITHMETIC_A + "M=M+D\n";
			break;
		case "sub": 
			code = this.ARITHMETIC_A + "M=M-D\n";
			break;
		case "neg": 
			code = "@SP\nA=M-1\nM=-M\n";
			break;
		case "eq": 
			code = ARITHMETIC_JMP("JEQ");
			JMP_Count++;
			break;
		case "lt": 
			code = ARITHMETIC_JMP("JLT");
			JMP_Count++;
			break;
		case "gt": 
			code = ARITHMETIC_JMP("JGT");
			JMP_Count++;
			break;
		case "and": 
			code = this.ARITHMETIC_A + "M=M&D\n";
			break;
		case "or": 
			code = this.ARITHMETIC_A + "M=M|D\n";
			break;
		case "not": 
			code = "@SP\nA=M-1\nM=!M\n";
			break;
			default: throw new IllegalArgumentException();
		}
		write(code);
	}
	
	private String PUSH(String segment, int index, String dissolvePTR) {
		return "@" + segment + "\n" + 
				dissolvePTR +
				"D=A\n" +				 
				"@" + index + "\n" + 
				"A=A+D\n" + 
				"D=M\n" +
				"@SP\n" + 
				"A=M\n" + 
				"M=D\n" + 
				"@SP\n" +
				"M=M+1\n";
	}
	
	private String POP(String segment, int index, String dissolvePTR) {
		return "@" + segment + "\n" +
				dissolvePTR + 
				"D=A\n" +
				"@" + index + "\n" + 
				"D=A+D\n" + 
				"@R13\n" + 
				"M=D\n" +
				"@SP\n" + 
				"A=M-1\n" +
				"D=M\n" + 
				"@R13\n" +
				"A=M\n" +
				"M=D\n" + 
				"@SP\n" +
				"M=M-1\n";
	}
	
	public void writePushPop(String command, String segment, int index) {
		if(index < 0) throw new IllegalArgumentException();
		String code = "";
		switch(command) {
		case "push":
			switch(segment) {
			case "constant":
				code = "@" +  index + "\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
				break;
			case "pointer":
				switch(index) {
				case 0:
					code = PUSH("THIS", 0, "");
					break;
				case 1:
					code = PUSH("THAT", 0, "");
					break;
					default: throw new IllegalArgumentException();
				}
				break;
			case "static":
				code = PUSH("16", index, "");
				break;
			case "temp": 
				code = PUSH("R5", index, "");
				break;
			case "local":
				code = PUSH("LCL", index, this.DISSOLVE_POINTER);
				break;
			case "argument":
				code = PUSH("ARG", index, this.DISSOLVE_POINTER);
				break;
			case "this":
				code = PUSH("THIS", index, this.DISSOLVE_POINTER);
				break;
			case "that":
				code = PUSH("THAT", index, this.DISSOLVE_POINTER);
				break;
			}	
			break;
		case "pop":
			switch(segment) {
			case "pointer":
				switch(index) {
				case 0:
					code = POP("THIS", 0, "");
					break;
				case 1:
					code = POP("THAT", 0, "");
					break;
					default: throw new IllegalArgumentException();
				}
				break;
			case "static":
				code = POP("16", index, "");
				break;
			case "temp": 
				code = POP("R5", index, "");
				break;
			case "local":
				code = POP("LCL", index, this.DISSOLVE_POINTER);
				break;
			case "argument":
				code = POP("ARG", index, this.DISSOLVE_POINTER);
				break;
			case "this":
				code = POP("THIS", index, this.DISSOLVE_POINTER);
				break;
			case "that":
				code = POP("THAT", index, this.DISSOLVE_POINTER);
				break;
			}		
			break;
			default: throw new IllegalArgumentException();
		}		
		write(code);
	} 
	
	private void write(String asmCode) {
		try {
			asmCode = asmCode.replaceAll("\n", "\r\n");
			this.controller.addAfterArea(asmCode);
			bw.write(asmCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			this.bw.close();
		}catch(IOException e){
        	e.printStackTrace();
        }
	}

	public void setApplicationController(ApplicationController controller) {
		this.controller = controller;		
	}

}