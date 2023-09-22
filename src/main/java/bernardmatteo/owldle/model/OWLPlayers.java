package bernardmatteo.owldle.model;

import java.util.ArrayList;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import bernardmatteo.owldle.db.DBServices;

public class OWLPlayers {
	
	private MongoCollection<Document> joueurs;
	private ArrayList<String> pseudos;
	private Map joueurRandom;
	private static final int NB_CHANCES = 10;
	private int nbTentatives;
	
	public OWLPlayers() {
		init();
	}
	
	public MongoCollection<Document> getJoueurs() {
		return joueurs;
	}

	public Map getJoueurRandom() {
		return joueurRandom;
	}
	
	public int GetNbTentatives() {
		return nbTentatives;
	}
	
	public ArrayList<String> getPseudos() {
		return pseudos;
	}
	
	public void init() {
		DBServices.connexionDB();
		joueurs = DBServices.recupererCollection("joueurs");
		joueurRandom = DBServices.recupererJoueurAleatoire(joueurs);
		DBServices.fermerDB();
		nbTentatives = 0;
		recupererPseudos();
	}

	public boolean memePseudo(Map joueur) {
		return joueurRandom.get("pseudo").equals(joueur.get("pseudo"));
	}
	
	public boolean memePays(Map joueur) {
		return joueurRandom.get("pays").equals(joueur.get("pays"));
	}
	
	public boolean memeRole(Map joueur) {
		return joueurRandom.get("role").equals(joueur.get("role"));
	}
	
	public boolean memeTeam(Map joueur) {
		return joueurRandom.get("team").equals(joueur.get("team"));
	}
	
	public boolean memeJoueur(Map joueur) {
		return memePseudo(joueur) &&
				memePays(joueur) &&
				memeRole(joueur) &&
				memeTeam(joueur);
	}
	
	public void propositionJoueur(Map joueur) {
		nbTentatives ++;
		
		if (memeJoueur(joueur)) 
			System.out.println("Bien joué !");
		else 
			System.out.println("Mauvais joueur..");
		
		if (finPartie()) {
			System.out.println("Fin de partie.");
		}
	}
	
	public void recupererPseudos() {
		pseudos = new ArrayList<String>();
		Map j;
		for (Document joueur : joueurs.find()) {
			j = joueur;
			pseudos.add((String) j.get("pseudo"));
		}
	}
	
	public boolean finPartie() {
		return (nbTentatives == NB_CHANCES);
	}
}
