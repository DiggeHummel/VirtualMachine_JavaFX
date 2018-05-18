package gui;

import java.io.File;

import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileManager {

	/* local variables */
	private File lastDirectory = null;
	private String fileName = "";

	/* static fields */
	private static final String DEFAULT = "ChooseFile";

	/* Constructor */
	public FileManager() {}

	public File chooseFile() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle(FileManager.DEFAULT);
    	chooser.setSelectedExtensionFilter(new ExtensionFilter(".vm", "vm"));
    	if(lastDirectory != null) chooser.setInitialDirectory(lastDirectory);
    	File selFile = chooser.showOpenDialog(new Stage());
    	if(selFile != null) {
    		lastDirectory = selFile.getParentFile();
    		fileName = selFile.getName();
    	}
		return selFile;
	}

	public String getFileName() {
		return this.fileName;
	}

	public File getLastDirectory() {
		return this.lastDirectory;
	}
}
