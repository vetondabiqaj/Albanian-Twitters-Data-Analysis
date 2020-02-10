package Charts;



import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import eu.larkc.csparql.common.RDFTable;
import eu.larkc.csparql.common.RDFTuple;
import eu.larkc.csparql.core.ResultFormatter;

public class CPBarChartHashTag extends ResultFormatter {
	public static Map<String, Integer> createDataSet(Observable o, Object arg) {
		RDFTable q = (RDFTable) arg;
		Map<String, Integer> hashtagMap = new HashMap<String, Integer>();

		for (final RDFTuple t : q) {
			String firtKey = t.get(0);
			String[] hashAsArray = firtKey.split(",");
			for (int i = 0; i < hashAsArray.length; i++) {
				if (hashtagMap.get(hashAsArray[i]) != null) {
					hashtagMap.put(hashAsArray[i], Integer.parseInt(hashtagMap.get(hashAsArray[i]).toString()) + 1);
				} else {
					hashtagMap.put(hashAsArray[i], 1);
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
		JFreeChart chart = ChartFactory.createBarChart("Hash Tags",         
		         "Hashtags",            
		         "Counts",
		         dataset,		         
		         PlotOrientation.HORIZONTAL, 
		         false, true, true);

		// Format Label

		try {
			ChartUtils.saveChartAsPNG(new File("charts/bar_hashtags.png"), chart, 600, 600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private CategoryDataset createDataset(Map<String, Integer> hashtagMap) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset( ); 
		hashtagMap = sortByValue(hashtagMap);
		hashtagMap.forEach((k, v) -> {
			dataset.setValue(Double.parseDouble(v.toString()), "", k);
		});
		return dataset;
	}
	
	public static Map<String, Integer> sortByValue(final Map<String, Integer> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
