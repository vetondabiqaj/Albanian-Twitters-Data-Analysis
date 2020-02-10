package twitterreasoming;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LineChartToPNGEx {

    public static void main(String[] args) throws IOException {

    	XYSeries series1 = new XYSeries("2014");
        series1.add(1, 1);
        series1.add(1, 4);
        

    	XYSeries series11 = new XYSeries("2016");
        series11.add(2, 1);
        series11.add(2, 2);
        series11.add(10, 2);


        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series11);

        JFreeChart chart = ChartFactory.createXYStepChart(
                "Average salary per age",
                "Age",
                "Salary (€)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true
        );


        ChartUtils.saveChartAsPNG(new File("line_chart.png"), chart, 450, 400);
    }
}