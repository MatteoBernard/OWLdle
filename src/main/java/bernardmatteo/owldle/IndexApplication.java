package bernardmatteo.owldle;

import java.io.IOException;
import java.io.InputStream;

import bernardmatteo.owldle.controller.IndexController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class IndexApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader();
    	InputStream fxmlStream = getClass().getResourceAsStream("/bernardmatteo/owldle/view/index.fxml");
    	Parent root = fxmlLoader.load(fxmlStream);
        
        Image icon = new Image(getClass().getResourceAsStream("/bernardmatteo/owldle/images/overwatch-emblem.png"));
        stage.getIcons().add(icon);
        
		Scene scene = new Scene(root, 800, 600);
		scene.getStylesheets().add(getClass().getResource("/bernardmatteo/owldle/styles/owl-players-style.css").toExternalForm());
        stage.setScene(scene);

        stage.setTitle("OWLdle");
        stage.setResizable(false);
        
        IndexController monController = fxmlLoader.getController();
        monController.setStage(stage);	
        monController.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}