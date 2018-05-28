package util;

import java.util.HashMap;

public final class Translator {

	/* public fields */
	public static final String CONSTANT_FLAG = "cons";
	public static final String POINTER_FLAG = "ptr";
	public static final String JMP_FLAG = "jmp";
	public static final String INIT_FLAG = "init";

	/* final variables */
	private final String DISSOLVE_POINTER = "A=M\n";
	private final HashMap<String, String> map;

	/* variables */
	private boolean isInFunction = false;
	private String functionName = "";

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
	public String getASMCode(Command c, int JMP_Count, int Instruction_Count) {
		switch (c.getType()) {
		case C_ARITHMETIC:
			if (c.getFlag().equals(JMP_FLAG))
				return ARITHMETIC_JMP(map.get(c.getCommand()), JMP_Count);
			else
				return map.get(c.getCommand());
		case C_PUSH:
			if (c.getFlag().equals(CONSTANT_FLAG))
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
			return CALL(c.getArg1(), c.getArg2(), Instruction_Count);
		case C_RETURN:
			return RETURN();
		case C_FUNCTION:
			if (c.getFlag().equals(INIT_FLAG))
				return INIT();
			else
				return FUNCTION(c.getArg1(), c.getArg2());
		default:
			throw new IllegalArgumentException();
		}
	}

	public String INIT() {
		return "@256\n" + "D=A\n" + "@SP\n" + "M=D\n";
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
		return "@" + index + "\n" + "D=A\n" + "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
	}

	private String LABEL(String arg) {
		return ((this.isInFunction) ? "(" + this.functionName + "$" : "(") + arg + ")\n";
	}

	private String GOTO(String arg) {
		return "@" + arg + "\n" + "0;JMP\n";
	}

	private String IF(String arg) {
		return "@SP\n" + "M=M-1\n" + "D=M\n" + "@" + arg + "\n" + "D;JNE\n";
	}

	private String CALL(String functionName, int numArgs, int Instruction_Count) {
		return pushToStack("RETURN_" + Instruction_Count, true) + pushToStack("LCL", false) + pushToStack("ARG", false)
				+ pushToStack("THIS", false) + pushToStack("THAT", false) + "@" + (numArgs + 5) + "\n" + "D=A\n"
				+ "@SP\n" + "A=M\n" + "D=A-D\n" + "@ARG\n" + "M=D\n" + "@SP\n" + "D=M\n" + "@LCL\n" + "M=D\n"
				+ GOTO(functionName) + "(" + "RETURN_" + Instruction_Count + ")\n";
	}

	private String pushToStack(String register, boolean direct) {
		return "@" + register + "\n" + (direct ? "D=A\n" : "D=M\n") + "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
	}

	private String RETURN() {
		return "@LCL\n" + "D=M\n" + "@R13\n" + "M=D\n" + "@5\n" + "A=D-A\n" + "D=M\n" + "@R14\n" + "M=D\n" + "@SP\n"
				+ "AM=M-1\n" + "D=M\n" + "@ARG\n" + "A=M\n" + "M=D\n" + "@ARG\n" + "D=M\n" + "@SP\n" + "M=D+1\n"
				+ frame("THAT") + frame("THIS") + frame("ARG") + frame("LCL") + "@R14\n" + "A=M\n" + "0;JMP\n";
	}

	private String frame(String register) {
		return "@R13\n" + "D=M-1\n" + "AM=D\n" + "D=M\n" + "@" + register + "\n" + "M=D\n";
	}

	private String FUNCTION(String functionName, int numLocals) {
		this.functionName = functionName;
		this.isInFunction = true;
		String out = "(" + functionName + ")\n";
		for (int i = 0; i < numLocals; i++)
			out += CONSTANT(0);
		return out;
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
