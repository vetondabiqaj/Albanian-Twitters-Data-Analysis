/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterreasoming;

import com.mongodb.client.MongoCursor;
import org.bson.Document;
import CSparql.*;

/**
 *
 * @author Veton Dabiqaj
 */

public class TwitterReasoming {

	static final int count = 1;

	public static final int MainStructure = 1;
	public static final int AllFields = 2;
	public static final int FindByString = 3;
	public static final int GroupBy = 4;
	public static final int GroupByHash = 5;
	public static final int GroubByLang = 6;
	public static final int GroupByText = 7;
	public static final String iri =  "http://twitter.org";

	public static void main(String[] args) {
		//Clear Table in MongoDB
		//DBStructure.deleteAllDocuments("all_twittsasdf");		
		
		CSEngine.Streaming(500, GroupByHash, iri);
				
		//Get old Tweets
		String minId = DBStructure.getMinTwitterID("all_twitts");
		Long id;
		if (minId != null) {
			id = new Long(minId);
		} else {
			id = new Long(-1);
		}
		//TwStructure.search(1, id);
		
		//Y-m-d
		//MongoCursor<Document> document = DBStructure.getData("all_twitts");
		//Model model = RDF.createRDFfromDB(document);

		//System.out.println(document);
		//RDF.Queries(model);

		//TwStructure.straming();

		// DBStructure.getData("all_twitts");

		System.out.println("\n\n\n\n\n----------End of main----------\n\n\n\n\n");
	}

}
