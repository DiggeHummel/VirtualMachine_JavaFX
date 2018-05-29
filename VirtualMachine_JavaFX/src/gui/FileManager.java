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

	public List<File> chooseFiles() {
		// init Chooser
		FileChooser chooser = new FileChooser();
		chooser.setTitle(FileManager.DEFAULT);
		chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(".asm", ".asm"));

		// last directory
		if (lastDirectory != null)
			chooser.setInitialDirectory(lastDirectory);

		List<File> selFiles = chooser.showOpenMultipleDialog(new Stage());

		// new last directory
		if (selFiles != null)
			lastDirectory = selFiles.get(0).getParentFile();

		// new last fileName
		fileName = StringModifier.substring(selFiles.get(0).getName(), 0, ".");

		return selFiles;
	}

	public String getFileName() {
		return this.fileName;
	}

	public File getLastDirectory() {
		return this.lastDirectory;
	}
}
