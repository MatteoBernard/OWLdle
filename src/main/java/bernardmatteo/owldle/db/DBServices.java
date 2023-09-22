package bernardmatteo.owldle.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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
	
	// opérations
	
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
        return map;
	}
}
