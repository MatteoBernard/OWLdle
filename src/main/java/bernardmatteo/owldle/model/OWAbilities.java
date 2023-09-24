package bernardmatteo.owldle.model;

import java.util.ArrayList;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import bernardmatteo.owldle.db.DBServices;

public class OWAbilities {
	
	private MongoCollection<Document> spells;
	private MongoCollection<Document> heros;
	private ArrayList<String> nomsHeros;
	private Map spellRandom;
	private int nbTentatives;

	public MongoCollection<Document> getSpells() {
		return spells;
	}

	public MongoCollection<Document> getHeros() {
		return heros;
	}
	
	public ArrayList<String> getNomsHeros() {
		return nomsHeros;
	}

	public Map getSpellRandom() {
		return spellRandom;
	}

	public int getNbTentatives() {
		return nbTentatives;
	}
	
	public OWAbilities() {
		init();
	}
	
	public void init() {
		DBServices.connexionDB();
		spells = DBServices.recupererCollection("spells");
		heros = DBServices.recupererCollection("heros");
		spellRandom = DBServices.recupererSpellAleatoire(spells);
		DBServices.fermerDB();
		nbTentatives = 0;
	}
	
	public void propositionHeros() {
		nbTentatives++;
	}
	
	public boolean bonHeros(Map heros) {
		return spellRandom.get("hero").equals(heros.get("nom"));
	}
	
	public void recupererHeros() {
		nomsHeros = new ArrayList<String>();
		Map j;
		for (Document joueur : heros.find()) {
			j = joueur;
			nomsHeros.add((String) j.get("nom"));
		}
	}
	
}
