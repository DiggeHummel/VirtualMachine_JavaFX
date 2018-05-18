package gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser {
	
	private JFileChooser chooser;
	
	/* local fields */
	private File lastDirectory = null;
	private String dataName = "";
	private final String description = ".vm";
	private final String dataType = "vm";
	
	private final String DEFAULT = "ChooseFile";
	
	/* Constructor */
	public FileChooser() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e) {
			e.printStackTrace();
	    }
	}
	
	private void setLastPath() {
		if(lastDirectory != null) chooser.setCurrentDirectory(lastDirectory);
	}
	
	public File chooseFile() {
		this.chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter(this.description, this.dataType));
		chooser.setMultiSelectionEnabled(false);
		chooser.setDialogTitle(this.DEFAULT);
		setLastPath();
	    int returnVal = this.chooser.showOpenDialog(null);	
	    switch(returnVal) {
			case JFileChooser.APPROVE_OPTION:
				File selectedFile = chooser.getSelectedFile();
				setFields(selectedFile);
				return selectedFile;
			case JFileChooser.CANCEL_OPTION:
				return null;
			default:
				return null;
	    }
	   		
	}
	
	private void setFields(File file) {
		String tmp = file.getName();
		this.dataName = tmp.substring(0, tmp.indexOf('.'));
		this.lastDirectory = file.getParentFile();
	}
	
	public String getDataName() {
		return this.dataName;
	}
	
	public File getLastDirectory() {
		return this.lastDirectory;
	}
}
