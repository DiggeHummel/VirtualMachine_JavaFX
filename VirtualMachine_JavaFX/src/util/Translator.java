package util;

import java.util.HashMap;

public final class Translator {

	/* public fields */
	public static final String CONSTANT_FLAG = "cons";
	public static final String POINTER_FLAG = "ptr";
	public static final String JMP_FLAG = "jmp";

	/* final variables */
	private final String DISSOLVE_POINTER = "A=M\n";
	private final HashMap<String, String> map;

	/* constructor */
	public Translator() {
		this.map = new HashMap<String, String>();
		map.put("not", "@SP\nA=M-1\nM=!M\n");
		map.put("or", "@SP\nAM=M-1\nD=M\nA=A-1\nM=M|D\n");
		map.put("and", "@SP\nAM=M-1\nD=M\nA=A-1\nM=M&D\n");
		map.put("sub", "@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D\n");
		map.put("add", "@SP\nAM=M-1\nD=M\nA=A-1\nM=M+D\n");
		map.put("neg", "@SP\nA=M-1\nM=-M\n");
		map.put("static", "16");
		map.put("temp", "R5");
		map.put("local", "LCL");
		map.put("argument", "ARG");
		map.put("this", "THIS");
		map.put("that", "THAT");
		map.put("eq", "JEQ");
		map.put("lt", "JLT");
		map.put("gt", "JGT");
	}
	
	
	/* public methods */
	public String getASMCode(Command c, int JMP_Count) {
		switch (c.getType()) {
		case C_ARITHMETIC:
			if(c.getFlag().equals(JMP_FLAG))
				return ARITHMETIC_JMP(map.get(c.getCommand()), JMP_Count);
			else
				return map.get(c.getCommand());
		case C_PUSH:
			if(c.getFlag().equals(CONSTANT_FLAG))
				return CONSTANT(c.getArg2());
			else if (c.getFlag().equals(POINTER_FLAG))
				return POINTER(c.getCommand(), c.getArg1(), c.getArg2());
			else
				return PUSH(map.get(c.getArg1()), c.getArg2(), isPointerSegment(c.getArg1()));
		case C_POP:
			if (c.getFlag().equals(POINTER_FLAG))
				return POINTER(c.getCommand(), c.getArg1(), c.getArg2());
			else
				return POP(map.get(c.getArg1()), c.getArg2(), isPointerSegment(c.getArg1()));
		case C_LABEL:
			return LABEL(c.getArg1());
		case C_GOTO:
			return GOTO(c.getArg1());
		case C_IF:
			return IF(c.getArg1());
		case C_CALL:
			return CALL(c.getArg1(), c.getArg2());
		case C_RETURN:
			return RETURN();
		case C_FUNCTION:
			return FUNCTION(c.getArg1(), c.getArg2());
		default:
			throw new IllegalArgumentException();
		}
	}

	
	/* private methods */
	private String ARITHMETIC_JMP(String JMP_Case, int JMP_Count) {
		return "@SP\n" + "AM=M-1\n" + "D=M\n" + "A=A-1\n" + "D=M-D\n" + "@PUT_TRUE" + JMP_Count + "\n" + "D;" + JMP_Case
				+ "\n" + "@SP\n" + "A=M-1\n" + "M=0\n" + "@SKIP" + JMP_Count + "\n" + "0;JMP\n" + "(PUT_TRUE"
				+ JMP_Count + ")\n" + "@SP\n" + "A=M-1\n" + "M=-1\n" + "(SKIP" + JMP_Count + ")\n";
	}

	private String PUSH(String segment, int index, boolean dissolvePTR) {
		return "@" + segment + "\n" + (dissolvePTR ? this.DISSOLVE_POINTER : "") + "D=A\n" + "@" + index + "\n"
				+ "A=A+D\n" + "D=M\n" + "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
	}

	private String POP(String segment, int index, boolean dissolvePTR) {
		return "@" + segment + "\n" + (dissolvePTR ? this.DISSOLVE_POINTER : "") + "D=A\n" + "@" + index + "\n"
				+ "D=A+D\n" + "@R13\n" + "M=D\n" + "@SP\n" + "A=M-1\n" + "D=M\n" + "@R13\n" + "A=M\n" + "M=D\n"
				+ "@SP\n" + "M=M-1\n";
	}

	private String CONSTANT(int index) {
		return "@" + index + "\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
	}

	private String LABEL(String arg) {
		return "(" + arg + ")\n";
	}

	private String GOTO(String arg) {
		return "@" + arg + "\n" + "0;JMP\n";
	}

	private String IF(String arg) {
		return "@SP\n" + "A=M-1\n" + "D=M\n" + "@" + arg + "\n" + "D;JNE\n";
	}
	
	private String CALL(String functionName, int numArgs) {
		return "";
	}
	
	private String RETURN() {
		return "";
	}
	
	private String FUNCTION(String functionName, int numLocals) {
		return "";
	}

	private String POINTER(String command, String segment, int index) {
		if (index == 0)
			segment = "THIS";
		else if (index == 1)
			segment = "THAT";
		else
			throw new IllegalArgumentException();
		switch (command) {
		case "pop":
			return POP(segment, 0, false);
		case "push":
			return PUSH(segment, 0, false);
		default:
			throw new IllegalArgumentException();
		}
	}

	private boolean isPointerSegment(String segment) {
		if (segment.equals("local") || segment.equals("argument") || segment.equals("this") || segment.equals("that"))
			return true;
		return false;
	}
}
