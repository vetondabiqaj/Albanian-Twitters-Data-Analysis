package CSparql;

public class Queries {
	public static final int MainStructure = 1;
	public static final int AllFields = 2;
	public static final int FindByString = 3;
	public static final int GroupBy = 4;
	public static final int GroupByHash = 5;
	public static final int GroubByLang = 6;
	public static final int GroupByText = 7;

	public static String Query(int key, String iri) {
		String query = null;
		switch (key) {
		case 1:
			query = "REGISTER QUERY MainStructure AS " +
					"PREFIX vcard: <" + iri + "/> " + 
					"SELECT ?s ?p ?o "
					+ "FROM STREAM <" + iri + "> [RANGE 5s STEP 1s] " 
					+ "WHERE { ?s ?p ?o }";
			break;
		case 2:
			query = "REGISTER QUERY AllFields AS " + 
					"PREFIX vcard: <" + iri + "/> " 
					+ "\nSELECT  ?createdAt ?username ?screenName ?latitude ?text " 
					+ "FROM STREAM <"+ iri +"> [RANGE 5s STEP 1s] " 
					+ " WHERE { "
					+ "\n OPTIONAL {?predicate vcard:createdAt  ?createdAt . }"
					+ "\n OPTIONAL {?predicate vcard:username  ?username . }"
					+ "\n OPTIONAL {?predicate vcard:screenName ?screenName . }"
					+ "\n OPTIONAL {?predicate vcard:text ?text . }"
					+ "\n OPTIONAL {?predicate vcard:latitude ?latitude . } " + "\n} "
					
			 //+ "OFFSET 1 LIMIT 2"
			;
			break;
		case 3:
			query =  "REGISTER QUERY findByString AS " + "Prefix vcard: <"+ iri +"/>"
					+ "\nSELECT  ?createdAt ?username ?screenName ?latitude ?text " 
					+ "FROM STREAM <"+ iri +"> [RANGE 5m STEP 3s] " 
					+ " WHERE { "
					+ "\n OPTIONAL {?predicate vcard:username  ?username . }"
					+ "\n OPTIONAL {?predicate vcard:screenName ?screenName . }"
					+ "\n OPTIONAL {?predicate vcard:text ?text . }" 
					//+ "\n FILTER ( regex(?text, \"#Kosovo\" )) . "
					//+ "\n OPTIONAL {?predicate vcard:latitude ?latitude . } " 
					+ "\n} ";
			break;
			
		case 4:
			query = "REGISTER QUERY GroubBy AS " + "Prefix vcard: <"+ iri +"/> "
					+ "\nSELECT ?lang  (count(vcard:lang) as ?count) " 
					+ "FROM STREAM <"+ iri + "> [RANGE 5m STEP 3s] " 
					+ " WHERE { "
					+ "\n OPTIONAL {?predicate vcard:lang  ?lang . }" 
					+ "\n} "
					+ "\nGROUP BY ?lang "
					+ "ORDER BY ?count";
			break;
			
		case 5:
			query = "REGISTER QUERY GroubByHashTags AS " + "Prefix vcard: <"+ iri +"/> "
					+ "\nSELECT ?hashtag " 
					+ "FROM STREAM <"+ iri + "> [RANGE 5m STEP 3s] " 
					+ " WHERE { "
					+ "\n {?predicate vcard:hashtag  ?hashtag . }"
					+ "\n} "
					//+ "\nGROUP BY ?hashtag "
					//+ "ORDER BY ?count"
					;
			break;
			
		case 6:
			query = "REGISTER QUERY GroubByLang AS " + "Prefix vcard: <"+ iri +"/> "
					+ "\nSELECT ?lang " 
					+ "FROM STREAM <"+ iri + "> [RANGE 5m STEP 3s] " 
					+ " WHERE { "
					+ "\n {?predicate vcard:lang  ?lang . }"
					+ "\n} "
					//+ "\nGROUP BY ?hashtag "
					//+ "ORDER BY ?count"
					;
			break;
			

			
		case 7:
			query = "REGISTER QUERY GroubByLang AS " + "Prefix vcard: <"+ iri +"/> "
					+ "\nSELECT ?text " 
					+ "FROM STREAM <"+ iri + "> [RANGE 5m STEP 3s] " 
					+ " WHERE { "
					+ "\n {?predicate vcard:text  ?text . }"
					+ "\n} "
					//+ "\nGROUP BY ?hashtag "
					//+ "ORDER BY ?count"
					;
			break;

		default:
			query = "REGISTER QUERY MainStructure AS " +
					"PREFIX vcard: <" + iri + "/> " + 
					"SELECT ?s ?p ?o "
					+ "FROM STREAM <" + iri + "> [RANGE 5s STEP 1s] " 
					+ "WHERE { ?s ?p ?o }";
			break;
		}
		return query;
	}

}
