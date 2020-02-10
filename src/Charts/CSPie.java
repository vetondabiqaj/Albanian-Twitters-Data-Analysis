package Charts;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Observable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import eu.larkc.csparql.core.ResultFormatter;

public class CSPie extends ResultFormatter {

	@Override
	public void update(Observable o, Object arg) {
		String[][] list = DataSet.createDataSet(o, arg);
		
		// Create dataset
		PieDataset dataset = createDataset(list);

		// Create chart
		JFreeChart chart = ChartFactory.createPieChart("Pie Chart", dataset, true, true, true);

	    //Format Label
	    PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
	        "{0} ({2})", new DecimalFormat("0.0"), new DecimalFormat("0%"));
	    ((PiePlot) chart.getPlot()).setLabelGenerator(labelGenerator);

		try {
			ChartUtils.saveChartAsPNG(new File("charts/pie_schart.png"), chart, 600, 600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private PieDataset createDataset(String[][] list) {
		DefaultPieDataset dataset=new DefaultPieDataset();
	    for(int i=1;i<list.length;i++) {
			dataset.setValue(list[i][0] + "("+list[i][1]+")", Double.parseDouble(list[i][1]));
		}
	    return dataset;
	}
	
}
