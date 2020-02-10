package twitterreasoming;



import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
/**
 * @author imssbora
 */
public class PieChartExample extends JFrame {
  private static final long serialVersionUID = 6294689542092367723L;

  public PieChartExample(String title) {
    super(title);

    // Create dataset
    PieDataset dataset = createDataset();

    // Create chart
    JFreeChart chart = ChartFactory.createPieChart(
        "Pie Chart Example",
        dataset,
        true, 
        true,
        true);

    //Format Label
    PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
        "{0} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
    ((PiePlot) chart.getPlot()).setLabelGenerator(labelGenerator);
    
    // Create Panel
    ChartPanel panel = new ChartPanel(chart);
    setContentPane(panel);
  }

  private PieDataset createDataset() {

    DefaultPieDataset dataset=new DefaultPieDataset();
    dataset.setValue("Albania 2", 2);
    dataset.setValue("Croatia 1", 1);
    dataset.setValue("40-59", 3);
    return dataset;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      PieChartExample example = new PieChartExample("Pie Chart Example | BORAJI.COM");
      example.setSize(800, 400);
      example.setLocationRelativeTo(null);
      example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      example.setVisible(true);
    });
  }
}