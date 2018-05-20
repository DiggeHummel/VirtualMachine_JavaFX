package util;

public class Command {

	/* fields */
	private String[] arithmeticCommands = { "add", "sub", "neg", "and", "or", "not" };
	private String[] arithmeticJMPCommands = { "eq", "lt", "gt" };

	/* variables */
	private CommandType type;
	private String Translator_FLAG;

	// structure | {this.src = command | segment | index}
	private final String[] src;

	/* constructor */
	public Command(String[] command) {
		if (command.length <= 3)
			this.src = command;
		else
			throw new IllegalArgumentException();
		analyzeCommand();
	}

	/* public methods */
	public String getFlag() {
		return this.Translator_FLAG;
	}

	public CommandType getType() {
		return this.type;
	}

	public String getCommand() {
		return this.src[0];
	}

	public String getSegment() {
		if (this.src.length >= 2)
			return this.src[1];
		else
			return "";
	}

	public int getIndex() {
		if (this.src.length >= 2)
			return Integer.parseInt(this.src[2]);
		else
			return 0;
	}

	/* private methods */
	private final void analyzeCommand() {
		if (arrayContains(this.arithmeticJMPCommands, this.src[0])) {
			this.type = CommandType.C_ARITHMETIC;
			this.Translator_FLAG = Translator.JMP_FLAG;
		} else if (arrayContains(this.arithmeticCommands, this.src[0])) {
			this.type = CommandType.C_ARITHMETIC;
			this.Translator_FLAG = Translator.ARITHMETIC_FLAG;
		} else {
			switch (this.src[0]) {
			case "pop":
				this.type = CommandType.C_POP;
				if (this.src[1].equals("pointer"))
					this.Translator_FLAG = Translator.POINTER_FLAG;
				else
					this.Translator_FLAG = Translator.POP_FLAG;
				break;
			case "push":
				this.type = CommandType.C_PUSH;
				if (this.src[1].equals("constant"))
					this.Translator_FLAG = Translator.CONSTANT_FLAG;
				else if (this.src[1].equals("pointer"))
					this.Translator_FLAG = Translator.POINTER_FLAG;
				else
					this.Translator_FLAG = Translator.PUSH_FLAG;
				break;
			case "if-goto":
				this.type = CommandType.C_IF;
				this.Translator_FLAG = Translator.IF_FLAG;
				break;
			case "goto":
				this.type = CommandType.C_GOTO;
				this.Translator_FLAG = Translator.GOTO_FLAG;
				break;
			case "call":
				this.type = CommandType.C_CALL;
				break;
			case "label":
				this.type = CommandType.C_LABEL;
				this.Translator_FLAG = Translator.LABEL_FLAG;
				break;
			case "return":
				this.type = CommandType.C_RETURN;
				break;
			case "function":
				this.type = CommandType.C_FUNCTION;
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	private boolean arrayContains(String[] arr, String suspect) {
		for (int i = 0; i < arr.length; i++) {
			if (suspect.equals(arr[i]))
				return true;
		}
		return false;
	}
}
