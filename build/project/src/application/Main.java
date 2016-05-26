package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author GladeJoa
 */
public class Main extends Application implements Commons {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// === Load FXML ===
		FXMLLoader loader = new FXMLLoader(getClass().getResource(GUI_PATH));
		
		// === Start the show! ===
		Scene scene = new Scene(loader.load(), APP_WIDTH, APP_HEIGHT);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Solarsystem");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}