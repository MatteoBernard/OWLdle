package bernardmatteo.owldle.controller;

import java.io.IOException;
import java.io.InputStream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IndexController {
	
	private Stage stage;
	private Scene scene;
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	
	@FXML
    void lancerJoueursOWL(ActionEvent event) throws IOException {

    	FXMLLoader fxmlLoader = new FXMLLoader();
    	InputStream fxmlStream = getClass().getResourceAsStream("/bernardmatteo/owldle/view/owl-players-view.fxml");
    	Parent root = fxmlLoader.load(fxmlStream);

    	Scene sceneAjout = new Scene(root);
    	sceneAjout.getStylesheets().add(getClass().getResource("/bernardmatteo/owldle/styles/owl-players-style.css").toExternalForm());
    	this.stage.setScene(sceneAjout);

    	OWLPlayersController controller = fxmlLoader.getController();
    	controller.setStage(this.stage);
    	controller.setScene(this.scene);
    }
	
	@FXML
    void lancerCapacitesOW(ActionEvent event) throws IOException {

    	FXMLLoader fxmlLoader = new FXMLLoader();
    	InputStream fxmlStream = getClass().getResourceAsStream("/bernardmatteo/owldle/view/ow-abilities-view.fxml");
    	Parent root = fxmlLoader.load(fxmlStream);

    	Scene sceneAjout = new Scene(root);
    	sceneAjout.getStylesheets().add(getClass().getResource("/bernardmatteo/owldle/styles/owl-players-style.css").toExternalForm());
    	this.stage.setScene(sceneAjout);

    	OWAbilitiesController controller = fxmlLoader.getController();
    	controller.setStage(this.stage);
    	controller.setScene(this.scene);
    }
}
