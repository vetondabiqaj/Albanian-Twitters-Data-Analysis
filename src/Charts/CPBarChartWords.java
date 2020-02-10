package Charts;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import eu.larkc.csparql.common.RDFTable;
import eu.larkc.csparql.common.RDFTuple;
import eu.larkc.csparql.core.ResultFormatter;

public class CPBarChartWords extends ResultFormatter {
	public static Map<String, Integer> createDataSet(Observable o, Object arg) {
		RDFTable q = (RDFTable) arg;
		Map<String, Integer> hashtagMap = new HashMap<String, Integer>();

		String regex = "^[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(regex);
		for (final RDFTuple t : q) {
			String firtKey = t.get(0);
			String[] hashAsArray = firtKey.split(" ");
			for (int i = 0; i < hashAsArray.length; i++) {
				String clearedString = hashAsArray[i].replaceAll("[^a-zA-Z0-9]", "");
				Matcher matcher = pattern.matcher(clearedString);
				if (matcher.matches() && clearedString.length()>=2) {
					if (hashtagMap.get(clearedString) != null) {
						hashtagMap.put(clearedString, Integer.parseInt(hashtagMap.get(clearedString).toString()) + 1);
					} else {
						hashtagMap.put(clearedString, 1);
					}
				}
			}
		}
		return hashtagMap;

	}

	@Override
	public void update(Observable o, Object arg) {
		Map<String, Integer> list = createDataSet(o, arg);

		// Create dataset
		CategoryDataset dataset = createDataset(list);

		// Create chart
		JFreeChart chart = ChartFactory.createBarChart("Hash Tags", "Hashtags", "Counts", dataset,
				PlotOrientation.HORIZONTAL, false, true, true);

		// Format Label

		try {
			ChartUtils.saveChartAsPNG(new File("charts/bar_words.png"), chart, 600, 600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private CategoryDataset createDataset(Map<String, Integer> hashtagMap) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		hashtagMap = sortByValue(hashtagMap);
		hashtagMap.forEach((k, v) -> {
			if (dataset.getColumnCount() < 10) {
				dataset.setValue(Double.parseDouble(v.toString()), "", k);
			}
			System.out.println("getRowCount: " + dataset.getColumnCount());
		});
		return dataset;
	}

	public static Map<String, Integer> sortByValue(final Map<String, Integer> wordCounts) {
		return wordCounts.entrySet().stream().sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
}
