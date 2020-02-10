package twitterreasoming;

import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

import com.mongodb.client.MongoCursor;
import org.bson.Document;

import eu.larkc.csparql.cep.api.RdfQuadruple;
import eu.larkc.csparql.cep.api.RdfStream;

public class GenerateRDFStream extends RdfStream implements Runnable {

	public static String[] twProperties = { 
			"lang",
			"hashtag",
			"username", 
			"profileLocation", 
			"profileLang", 
			"profileDescription", 
			"latitude",
			"longitude",
			"screenName",
			"text",
			"id",
			"createdAt",
			"currentUserRetweetId",
			"inReplyToScreenName",
			"isRetweet",
			"isRetweeted",
			"isPossiblySensitive",
			"placecountry",
			"placeStreetAddress",
			"placeName",
			"placeId",
			"placeFullName",
			"placeUrl",
			"placeCountryCode"
			};
	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(GenerateRDFStream.class);
	private boolean keepRunning = false;
	private int c = 1;
	private int ct = 0;

	public GenerateRDFStream(final String iri) {
		super(iri);
	}

	public void pleaseStop() {
		keepRunning = false;
	}

	@Override
	public void run() {
		keepRunning = true;

		while (keepRunning) {
			Document current;
			
			MongoCursor<Document> document = DBStructure.getDataWithOffset("all_twitts", ct, 100);
			while (document.hasNext()) {
				System.out.println(ct);
				current = document.next();
				Long milliseconds = System.currentTimeMillis();
				
				RdfQuadruple q1 = null;
				for (String property : twProperties) {
					if (current.get(property) != null) {
						q1 = new RdfQuadruple(super.getIRI() + "/id/" + current.get("id").toString(),
								super.getIRI() + "/" + property, current.get(property).toString(), milliseconds);
						this.put(q1);
					}
				}

				//Different between tweets 
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ct++;
			}

			if (c % 10 == 0)
				logger.info(ct + " triples streamed so far");

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.c++;
		}
	}

	public static String dumpRelatedStaticKnowledge( String iri) {
		

		Model m = ModelFactory.createDefaultModel();
		
		MongoCursor<Document> document = DBStructure.getDataWithOffset("all_twitts", 0, 2);
		Document current;
		while (document.hasNext()) {
			current = document.next();
			for (String property : twProperties) {
				if (current.get(property) != null) {
					m.add(new ResourceImpl(iri+"/id/" + current.get("id").toString()),
							new PropertyImpl(iri+"/" + property), current.get(property).toString());
				}
			}
		}

		StringWriter sw = new StringWriter();
		m.write(sw, "RDF/XML");
		return sw.toString();
	}

	public static void main(String[] args) {
		System.out.println(dumpRelatedStaticKnowledge("http://twitter.org"));
	}
}
