package Charts;

import java.util.List;
import java.util.Observable;

import eu.larkc.csparql.common.RDFTable;
import eu.larkc.csparql.common.RDFTuple;

public class DataSet {

	
	@SuppressWarnings("rawtypes")
	public static String[][] createDataSet(Observable o, Object arg) {
		RDFTable q = (RDFTable) arg;
		List names = (List) q.getNames();
		int nameSize = names.size();
		int rows = q.size();
		String[][] list = new String[rows + 1][nameSize];

		int nameKey = 0;
		for (final String t : q.getNames()) {
			list[0][nameKey] = t;
			nameKey++;
		}

		// System.out.println("\n-------" + q.size() + " results at SystemTime=[" +
		// System.currentTimeMillis() + "]--------");
		int rowLoop = 1;


		for (final RDFTuple t : q) {
			for (int i = 0; i < nameSize; i++) {
				list[rowLoop][i] = ClearString(t.get(i));
			}
			// System.out.println(t.toString());
			rowLoop++;
		}
		return list;
		
	}

	private static String ClearString(String str) {
		String[] clearString = null;

		clearString = str.split("#");
		if (clearString.length > 1) {
			clearString = clearString[0].split(String.valueOf('"'));
			return clearString[1];
		}
		return clearString[0];
	}
}
