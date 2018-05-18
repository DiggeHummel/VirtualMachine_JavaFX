package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import main.VirtualMachine;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private static VirtualMachine vm;
	
	public static void compile() {
		vm.vmToAsm();
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Application.fxml"));
			BorderPane root = (BorderPane) loader.load();
			ApplicationController controller = loader.<ApplicationController>getController();		
			vm = new VirtualMachine(controller);			
			Scene scene = new Scene(root);
			primaryStage.setTitle("VirtualMachine");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
