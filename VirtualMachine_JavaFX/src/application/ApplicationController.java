package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import main.VirtualMachine;

public class ApplicationController {

    @FXML // fx:id="afterLabel"
    private Label afterLabel; // Value injected by FXMLLoader

    @FXML // fx:id="beforeArea"
    private TextArea beforeArea; // Value injected by FXMLLoader

    @FXML // fx:id="compileButton"
    private Button compileButton; // Value injected by FXMLLoader
    
    @FXML // fx:id="mergeButton"
    private Button mergeButton; // Value injected by FXMLLoader
    
    @FXML // fx:id="fielNameField"
    private TextField fileNameField; // Value injected by FXMLLoader
    
    @FXML // fx:id="errLabel"
    private Label errLabel; // Value injected by FXMLLoader
    
    @FXML // fx:id="beforeLabel"
    private Label beforeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="title"
    private Label title; // Value injected by FXMLLoader

    @FXML // fx:id="afterArea"
    private TextArea afterArea; // Value injected by FXMLLoader

    @FXML // fx:id="gridpane"
    private GridPane gridpane; // Value injected by FXMLLoader
    
    private VirtualMachine vm  = new VirtualMachine();

    @FXML
    void compileOnAction(ActionEvent event) {
    	clearAreas();
    	this.vm.vmToAsm(this);
    }
   
    @FXML
    void mergeOnAction(ActionEvent event) {
    	if(!this.fileNameField.getText().equals("")) {
    		this.vm.mergeASMFiles(this, fileNameField.getText());
    	}else {
    		this.errLabel.setVisible(true);
    	}
    }
    
    public void setBeforeLabel(String value) {
    	this.beforeLabel.setText(value);
    }
    
    public void setAfterLabel(String value) {
    	this.afterLabel.setText(value);
    }
    
    public void addBeforeArea(String value) {
    	this.beforeArea.setText(beforeArea.getText() + value +  "\n");
    }
    
    public void addAfterArea(String value) {
    	this.afterArea.setText(afterArea.getText() + value +  "\n");
    }
    
    private void clearAreas() {
    	this.beforeArea.clear();
    	this.afterArea.clear();
    }

}