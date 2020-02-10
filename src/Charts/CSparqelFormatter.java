package Charts;

import java.util.List;
import java.util.Observable;

import javax.swing.text.Document;

import org.json.simple.JSONObject;
import org.openrdf.rio.helpers.RDFJSONParserSettings;

import com.google.gson.Gson;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.vocabulary.RDF;

import eu.larkc.csparql.common.RDFTable;
import eu.larkc.csparql.common.RDFTuple;
import eu.larkc.csparql.core.ResultFormatter;
import eu.larkc.csparql.core.engine.RDFStreamFormatter;
import twitter4j.JSONException;

public class CSparqelFormatter extends ResultFormatter {
	@Override
	public void update(Observable o, Object arg) {
		String[][] list = DataSet.createDataSet(o, arg);

	}
}
