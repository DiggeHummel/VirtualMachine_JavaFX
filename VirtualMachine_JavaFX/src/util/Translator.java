package util;

import java.util.HashMap;

public final class Translator {

	/* public fields */
	public static final String ARITHMETIC_FLAG = "arithmetic";
	public static final String JMP_FLAG = "jmp";
	public static final String PUSH_FLAG = "push";
	public static final String POP_FLAG = "pop";
	public static final String CONSTANT_FLAG = "cons";
	public static final String POINTER_FLAG = "ptr";

	/* final variables */
	private final String DISSOLVE_POINTER = "A=M\n";
	private final HashMap<String, String> map;

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
	}

	public String getASMCode(String flag, String command, String segment, int index, int JMP_Count) {
		switch (flag) {
		case ARITHMETIC_FLAG:
			return map.get(command);
		case JMP_FLAG:
			return ARITHMETIC_JMP(dissolveJMP(command), JMP_Count);
		case PUSH_FLAG:
			return PUSH(map.get(segment), index, isPointerSegment(segment));
		case POP_FLAG:
			return POP(map.get(segment), index, isPointerSegment(segment));
		case CONSTANT_FLAG:
			return CONSTANT(index);
		case POINTER_FLAG:
			return POINTER(command, segment, index);
		default:
			throw new IllegalArgumentException();
		}
	}

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

	private String dissolveJMP(String command) {
		switch (command) {
		case "eq":
			return "JEQ";
		case "lt":
			return "JLT";
		case "gt":
			return "JGT";
		default:
			throw new IllegalArgumentException();
		}
	}
}