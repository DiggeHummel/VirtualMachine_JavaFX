package gui;

import java.io.File;
import java.util.List;

import javafx.stage.Stage;
import util.StringModifier;
import javafx.stage.FileChooser;

public class FileManager {

	/* local variables */
	private File lastDirectory = null;
	private String fileName = "";

	/* static fields */
	private static final String DEFAULT = "ChooseFile";

	/* Constructor */
	public FileManager() {
	}

	public File chooseFile() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle(FileManager.DEFAULT);
		chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(".vm", ".vm"));
		if (lastDirectory != null)
			chooser.setInitialDirectory(lastDirectory);
		File selFile = chooser.showOpenDialog(new Stage());
		if (selFile != null) {
			lastDirectory = selFile.getParentFile();
			fileName = StringModifier.substring(selFile.getName(), 0, ".");
			return selFile;
		} else
			return null;
	}

	public List<File> chooseFiles() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle(FileManager.DEFAULT);
		chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(".asm", ".asm"));
	
		if (lastDirectory != null)
			chooser.setInitialDirectory(lastDirectory);
		List<File> selFiles = chooser.showOpenMultipleDialog(new Stage());
		return selFiles;
	}
	
	public String getFileName() {
		return this.fileName;
	}

	public File getLastDirectory() {
		return this.lastDirectory;
	}
}
