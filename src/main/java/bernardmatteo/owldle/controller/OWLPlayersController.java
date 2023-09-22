package bernardmatteo.owldle.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import bernardmatteo.owldle.db.DBServices;
import bernardmatteo.owldle.model.OWLPlayers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class OWLPlayersController {
	
	private Stage stage;
	private Scene scene;
	private OWLPlayers maPartie;
	
	@FXML
    private Label indice;
	
	@FXML
    private Button boutonValider;
	
	@FXML
    private TextField champEntree;
	
	private AutoCompletionBinding<String> autoCompletionBinding;
	
	@FXML
    private GridPane gridPanePropositions;
	
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
	
	 @FXML
	 public void initialize() {
		 init();
	 }
    
    @FXML
    public void commencerPartie(ActionEvent event) {
    	init();
    }
    
    public void init() {
    	maPartie = new OWLPlayers();
    	
    	gridPanePropositions.getChildren().clear();
    	
    	boutonValider.setDisable(false);
    	champEntree.setDisable(false);
    	champEntree.setText("");
    	
    	indice.setText("Entrez le pseudo d'un joueur !");
    	
    	autoCompletionBinding = TextFields.bindAutoCompletion(champEntree, maPartie.getPseudos());
    }

    @FXML
    public void validerJoueur(ActionEvent event) {
    	try {
    		Map joueur = DBServices.recupererJoueur(champEntree.getText(), maPartie.getJoueurs());
        	
        	ajouterElements(maPartie.GetNbTentatives(), joueur);
        	afficherCouleurs(maPartie.GetNbTentatives(), joueur);
        	champEntree.setText("");
        	
        	maPartie.propositionJoueur(joueur);
        	testFinDePartie(joueur);
    	}
    	catch (NullPointerException e) {
    		indice.setText("Pseudo de joueur non valide..");
    	}
    }
    
    public void ajouterElements(int ligne, Map joueur) {
    	gridPanePropositions.add(new Label((String) joueur.get("pseudo")), 0, ligne);
    	gridPanePropositions.add(new Label((String) joueur.get("pays")), 1, ligne);
    	gridPanePropositions.add(new Label((String) joueur.get("role")), 2, ligne);
    	gridPanePropositions.add(new Label((String) joueur.get("team")), 3, ligne);
    }
    
    public void afficherCouleurs(int ligne, Map joueur) {
    	Label lab = recupererLabel(ligne, 0);
    	if (maPartie.memePseudo(joueur)) {
    		lab.setStyle("-fx-background-color: #00ff7f;"
    				+ "    -fx-background-radius: 5px;"
    				+ "    -fx-opacity: 0.7;");
    	}
    	else lab.setStyle("-fx-background-color: #e65f42;"
    			+ "    -fx-background-radius: 5px;"
    			+ "    -fx-opacity: 0.7;");
    	
    	lab = recupererLabel(ligne, 1);
    	if (maPartie.memePays(joueur)) {
    		lab.setStyle("-fx-background-color: #00ff7f; "
    				+ "    -fx-background-radius: 5px;"
    				+ "    -fx-opacity: 0.7;");
    	}
    	else lab.setStyle("-fx-background-color: #e65f42;"
    			+ "    -fx-background-radius: 5px;"
    			+ "    -fx-opacity: 0.7;");
    	
    	lab = recupererLabel(ligne, 2);
    	if (maPartie.memeRole(joueur)) {
    		lab.setStyle("-fx-background-color: #00ff7f; "
    				+ "    -fx-background-radius: 5px;"
    				+ "    -fx-opacity: 0.7;");
    	}
    	else lab.setStyle("-fx-background-color: #e65f42;"
    			+ "    -fx-background-radius: 5px;"
    			+ "    -fx-opacity: 0.7;");
    	
    	lab = recupererLabel(ligne, 3);
    	if (maPartie.memeTeam(joueur)) {
    		lab.setStyle("-fx-background-color: #00ff7f; "
    				+ "    -fx-background-radius: 5px;"
    				+ "    -fx-opacity: 0.7;");
    	}
    	else lab.setStyle("-fx-background-color: #e65f42;"
    			+ "    -fx-background-radius: 5px;"
    			+ "    -fx-opacity: 0.7;");
    }
    
    public Label recupererLabel(int ligne, int colonne) {
    	for (Node node : gridPanePropositions.getChildren()) {
            int rowIndex = GridPane.getRowIndex(node);
            int columnIndex = GridPane.getColumnIndex(node);

            if (rowIndex == ligne && columnIndex == colonne) {
                return (Label) node;
            }
        }
        return null;
    }
    
    public void testFinDePartie(Map joueur) {
    	if (maPartie.memeJoueur(joueur)) {
    		boutonValider.setDisable(true);
        	champEntree.setDisable(true);
        	indice.setText("Bien joué ! Le joueur était : " + joueur.get("pseudo"));
    	}
    	else if (maPartie.finPartie()) {
    		boutonValider.setDisable(true);
        	champEntree.setDisable(true);
        	indice.setText("Perdu ! Le joueur était : " + (String) maPartie.getJoueurRandom().get("pseudo"));
    	}
    	else indice.setText("Pas trouvé ! Réessaie..");
    }
	
}
