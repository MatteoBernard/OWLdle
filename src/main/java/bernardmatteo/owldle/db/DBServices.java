package bernardmatteo.owldle.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DBServices {
	
	private static MongoDatabase database;
	private static MongoClient mongoClient;
	
	// getters - setters
	
	public static MongoDatabase getDatabase() {
		return (database != null ? database : null);
	}
	public MongoClient getClient() {
		return (mongoClient != null ? mongoClient : null);
	}
	
	// accès DB
	
	public static void connexionDB() {
		String connectionString = "mongodb+srv://others:others@owldle.oibgmg5.mongodb.net/?retryWrites=true&w=majority";
        ConnectionString connString = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .build();
        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("OWLdle");
	}
	
	public static void fermerDB() {
		mongoClient.close();
	}
	
	// opérations sur les joueurs
	
	public static Document creerJoueur(String pseudo, String pays, String role, String team) {
		return new Document("pseudo", pseudo).append("pays", pays).append("role", role).append("team", team);
	}
	
	public static MongoCollection<Document> recupererCollection(String nomCollec) {
		return (database != null ? database.getCollection(nomCollec) : null);
	}
	
	public static void ajouterJoueur(Document joueur, String nomCollec) {
		connexionDB() ;
		database.getCollection(nomCollec).insertOne(joueur);
		fermerDB();
	}
	
	public static void ajouterJoueurs(ArrayList<Document> joueurs, String nomCollec) {
		connexionDB() ;
		database.getCollection(nomCollec).insertMany(joueurs);
		fermerDB();
	}
	
	public static Map recupererJoueur(String pseudo, MongoCollection<Document> collection) {
		Document query = new Document("pseudo", pseudo);
        Document document = (Document) collection.find(query).first();
        Map map = document;
        return map;
	}
	
	public static Map recupererJoueurAleatoire(MongoCollection<Document> collection) {
		connexionDB() ;
        AggregateIterable<Document> aggregation = collection.aggregate(Arrays.asList(
                new Document("$sample", new Document("size", 1))
        ));
        Document randomDocument = aggregation.first();
        Map map = randomDocument;
        fermerDB();
        return map;
	}
	
	// opérations sur les spells
	
	public static byte[] creerTabBytes(String path) throws IOException {
		return FileUtils.readFileToByteArray(new File(path));
	}
	
	public static Document creerSpell(String nom, String hero, byte[] tabBytes) {
		return new Document("nom", nom).append("hero", hero).append("tabBytes", tabBytes);
	}
	
	public static void ajouterSpell(Document spell, String nomCollec) {
		connexionDB() ;
		database.getCollection(nomCollec).insertOne(spell);
		fermerDB();
	}
	
	public static Map recupererSpell(String nom, MongoCollection<Document> collection) {
		Document query = new Document("nom", nom);
		Document document = (Document) collection.find(query).first();
        Map map = document;
        return map;
	}
	
	public static Map recupererSpellAleatoire(MongoCollection<Document> collection) {
		connexionDB() ;
        AggregateIterable<Document> aggregation = collection.aggregate(Arrays.asList(
                new Document("$sample", new Document("size", 1))
        ));
        Document randomDocument = aggregation.first();
        Map map = randomDocument;
        fermerDB();
        return map;
	}
	
	// opérations sur les joueurs
	
	public static Document creerHeros(String nom, byte[] tabBytes) {
		return new Document("nom", nom).append("tabBytes", tabBytes);
	}
	
	public static Map recupererHeros(String nom, MongoCollection<Document> collection) {
		Document query = new Document("nom", nom);
		Document document = (Document) collection.find(query).first();
        Map map = document;
        return map;
	}
}
