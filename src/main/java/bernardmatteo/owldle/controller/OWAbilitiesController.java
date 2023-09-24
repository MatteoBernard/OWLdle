package bernardmatteo.owldle.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.bson.types.Binary;

import bernardmatteo.owldle.db.DBServices;
import bernardmatteo.owldle.model.OWAbilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;

public class OWAbilitiesController {
	
	private Stage stage;
	private Scene scene;
	private OWAbilities maPartie;
	
	@FXML
    private Rectangle rectHeros;
	
	@FXML
    private Rectangle rectSpell;
	
	@FXML
    private Label indice;
	
	@FXML
    private Label indTentatives;
	
	@FXML
    private Button boutonValider;
	
	@FXML
    private ImageView imgHero;

    @FXML
    private ImageView imgSpell;
	
	@FXML
    private TextField champEntree;
	
	private AutoCompletionBinding<String> autoCompletionBinding;
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
    public void lancerIndex(ActionEvent event) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
    	InputStream fxmlStream = getClass().getResourceAsStream("/bernardmatteo/owldle/view/index.fxml");
    	Parent root = fxmlLoader.load(fxmlStream);

    	Scene sceneAjout = new Scene(root);
    	sceneAjout.getStylesheets().add(getClass().getResource("/bernardmatteo/owldle/styles/owl-players-style.css").toExternalForm());
    	this.stage.setScene(sceneAjout);

    	IndexController controller = fxmlLoader.getController();
    	controller.setStage(this.stage);
    	controller.setScene(this.scene);
    }
	
	public void init() throws IOException {
    	maPartie = new OWAbilities();
    	
    	imgSpell.setImage(binaryToImage((Binary) maPartie.getSpellRandom().get("tabBytes")));
    	imgHero.setImage(new Image("/bernardmatteo/owldle/images/overwatch-emblem.png"));
    	
    	rectHeros.setFill(Color.RED);
    	
    	boutonValider.setDisable(false);
    	champEntree.setDisable(false);
    	champEntree.setText("");
    	
    	maPartie.recupererHeros();
    	autoCompletionBinding = TextFields.bindAutoCompletion(champEntree, maPartie.getNomsHeros());
    	autoCompletionBinding.setPrefWidth(champEntree.getPrefWidth());
    	autoCompletionBinding.setOnAutoCompleted(event -> {
    	    autoCompletionBinding.getAutoCompletionPopup().hide();
    	});
    	
    	indice.setText("A qui appartient cette capacité ?");
    	indTentatives.setText("");
    	
    }
	
	public Image binaryToImage(Binary binary) throws IOException {
		 if (binary != null) {
		        byte[] data = binary.getData();
		        if (data != null && data.length > 0) {
		            return new Image(new ByteArrayInputStream(data));
		        }
		 }
		 return new Image("src/main/resources/bernardmatteo/owldle/images/overwatch-emblem.png");
	}
	
	@FXML
	 public void initialize() throws IOException {
		 init();
	 }
	
	@FXML
    void commencerPartie(ActionEvent event) throws IOException {
		init();
    }

    @FXML
    void validerHero(ActionEvent event) throws IOException {
    	try {
    		Map heros = DBServices.recupererHeros(champEntree.getText(), maPartie.getHeros());
    		imgHero.setImage(binaryToImage((Binary) heros.get("tabBytes")));

        	champEntree.setText("");
        	
        	maPartie.propositionHeros();
        	if (maPartie.bonHeros(heros)) {
        		champEntree.setDisable(true);
        		boutonValider.setDisable(true);
        		rectHeros.setFill(Color.GREEN);
        		indice.setText("Bien joué ! La capacité était : "
        				+ maPartie.getSpellRandom().get("nom")
        				+ " de "
        				+ maPartie.getSpellRandom().get("hero"));
        		indTentatives.setText("Trouvé en " + maPartie.getNbTentatives()+ " " + (maPartie.getNbTentatives()!=1 ? "essais" : "essai"));
        	}
        	else {
        		indice.setText("Mauvais héros..");
        	}
    	} catch (NullPointerException e) {
    		indice.setText("Nom de héros non valide..");
    	}
    	
    }

}
