/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterreasoming;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.MongoCursor;

/**queries**/

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;

import java.util.LinkedList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import static java.util.Arrays.asList;

import java.util.ArrayList;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author Veton Dabiqaj
 */
public class DBStructure {
	/**
	 * 
	 * @return db Connection
	 */
	private static MongoDatabase defaultConnection() {
		@SuppressWarnings("resource")
		MongoClient client = new MongoClient("localhost");
		MongoDatabase db = client.getDatabase("twitt_testing");
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);
		return db;
	}

	public static void insert(String table, Document docs) {
		if (docs != null) {
		MongoDatabase db = defaultConnection();
		MongoCollection<Document> collection = db.getCollection(table);
			collection.insertOne(docs);
		}
	}

	public static MongoCursor<Document> getData(String table) {
		MongoDatabase db = defaultConnection();
		MongoCollection<Document> collection = db.getCollection(table);

		MongoCursor<Document> dbCursor = collection.find().iterator();
		return dbCursor;
		/**
		 * Document document = new Document(); try { Document current; while
		 * (cursor.hasNext()) { current = cursor.next();
		 * //System.out.println(cursor.next().toJson());
		 * System.out.println(current.get("_id").toString());
		 * document.put(current.get("_id").toString(), current.toJson()); } } finally {
		 * cursor.close(); } return document;
		 **/
	}

	public static MongoCursor<Document> getDataWithOffset(String table, int offset, int limit) {
		MongoDatabase db = defaultConnection();
		MongoCollection<Document> collection = db.getCollection(table);

		MongoCursor<Document> dbCursor = collection.find().sort(Sorts.descending("id")).skip(offset).limit(limit)
				.iterator();
		// return dbCursor;
		/**
		 * Document document = new Document(); try { Document current; while
		 * (dbCursor.hasNext()) { current = dbCursor.next(); //
		 * System.out.println(cursor.next().toJson());
		 * //System.out.println(current.get("id").toString());
		 * 
		 * } } finally { // cursor.close(); }
		 */
		return dbCursor;

	}

	public static String getMaxTwitterID(String table) {
		MongoDatabase db = defaultConnection();
		MongoCollection<Document> collection = db.getCollection(table);
		MongoCursor<Document> dbCursor = collection.find().sort(Sorts.descending("id")).iterator();
		if (dbCursor.hasNext()) {
			return dbCursor.next().get("id").toString();
		} else {
			return null;
		}
	}

	public static String getMinTwitterID(String table) {
		MongoDatabase db = defaultConnection();
		MongoCollection<Document> collection = db.getCollection(table);
		MongoCursor<Document> dbCursor = collection.find().sort(Sorts.ascending("id")).iterator();
		if (dbCursor.hasNext()) {
			return dbCursor.next().get("id").toString();
		} else {
			return null;
		}
	}

	public static void deleteAllDocuments(String table) {
		MongoDatabase db = defaultConnection();
		MongoCollection<Document> collection = db.getCollection(table);
		collection.deleteMany(new Document());
	}

}
