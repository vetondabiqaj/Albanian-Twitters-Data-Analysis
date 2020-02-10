package CSparql;

import java.text.ParseException;

import eu.larkc.csparql.cep.api.RdfStream;
import eu.larkc.csparql.core.engine.ConsoleFormatter;
import eu.larkc.csparql.core.engine.CsparqlEngine;
import eu.larkc.csparql.core.engine.CsparqlEngineImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;
import twitterreasoming.GenerateRDFStream;



import java.text.ParseException;

import Charts.CSparqelFormatter;

public class CSEngine {

	public static void Streaming(int seconds, int QueryID, String iri) {
		String query = null;
		RdfStream tg = null;
		// Initialize C-SPARQL Engine
		CsparqlEngine engine = new CsparqlEngineImpl();

		engine.initialize(true);

		// profileLocation
		query = Queries.Query(QueryID, iri);

		tg = new GenerateRDFStream(iri);
		// tg = new BasicIntegerRDFStreamTestGenerator("http://myexample.org/stream");
		System.out.print(query);

		// Register an RDF Stream

		engine.registerStream(tg);

		// Start Streaming (this is only needed for the example, normally streams are
		// external
		// C-SPARQL Engine users are supposed to write their own adapters to create RDF
		// streams

		final Thread t = new Thread((Runnable) tg);
		t.start();
		// Register a C-SPARQL query

		CsparqlQueryResultProxy c1 = null;

		try {
			c1 = engine.registerQuery(query, false);
		} catch (final ParseException ex) {
			// logger.error(ex.getMessage(), ex);
		}

		// Attach a Result Formatter to the query result proxy

		if (c1 != null) {
			//c1.addObserver(new CSparqelFormatter());
			//c1.addObserver(new ConsoleFormatter());
			//c1.addObserver(new Charts.CSPie());
			c1.addObserver(new Charts.CSPieHashTag());
			//c1.addObserver(new Charts.CPBarChartHashTag());
			//c1.addObserver(new Charts.CPBarChartWords());

			// System.out.println(c1);
		}

		// leave the system running for a while
		// normally the C-SPARQL Engine should be left running
		// the following code shows how to stop the C-SPARQL Engine gracefully
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {

		}

		// clean up (i.e., unregister query and stream)
		if (c1 != null) {
			engine.unregisterQuery(c1.getId());
		}

		((GenerateRDFStream) tg).pleaseStop();

		engine.unregisterStream(tg.getIRI());

		// System.exit(0);

	}
}