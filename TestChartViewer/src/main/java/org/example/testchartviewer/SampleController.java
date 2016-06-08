package org.example.testchartviewer;

import java.util.Calendar;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.fx.overlay.CrosshairOverlayFX;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import com.sun.javafx.geom.Ellipse2D;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Skin;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class SampleController
{
	private static ChartTheme currentTheme = new StandardChartTheme("JFree", true);

	@FXML
	private BorderPane borderPane;
	@FXML
	private Button generateButton;
	@FXML
	private TextArea logTextArea;
	@FXML
	private TextField seriesNumberTextField;
	@FXML
	private TextField itemsPerSeriesTextField;
	@FXML
	private CheckBox showShapesCheckbox;

	@FXML
	protected void doGenerate(ActionEvent event)
	{
		generateChart(Integer.valueOf(seriesNumberTextField.getText()),
				Integer.valueOf(itemsPerSeriesTextField.getText()));
	}

	private XYDataset createDataset(int seriesNumber, int itemsNumber)
	{
		final DefaultXYDataset result = new DefaultXYDataset();
		final Random random = new Random();

		for (int i = 0; i < seriesNumber; i++)
		{
			int last = 100;
			final double[][] data = new double[2][itemsNumber];

			for (int j = 0; j < itemsNumber; j++)
			{
				last = last + (random.nextBoolean() ? 1 : -1) * random.nextInt(13);
				data[0][j] = j;
				data[1][j] = last;
			}

			result.addSeries("series #" + i, data);
		}

		return result;
	}

	private JFreeChart createChart(XYDataset dataset)
	{
		return createXYLineChart("title", "domain", "values", dataset);
	}

	public JFreeChart createXYLineChart(String title,
			String xAxisLabel, String yAxisLabel, XYDataset dataset) {

		NumberAxis xAxis = new NumberAxis(xAxisLabel);
		xAxis.setAutoRangeIncludesZero(false);
		NumberAxis yAxis = new NumberAxis(yAxisLabel);
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, showShapesCheckbox.isSelected());
		// float r = 3;
		// renderer.setSeriesShape(0, new Ellipse2D(-r, -r, 2 * r, 2 * r));
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		renderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());

		JFreeChart chart = new JFreeChart(title, plot);
		currentTheme.apply(chart);
		return chart;

	}

	private void generateChart(int seriesNumber, int itemsNumber)
	{
		logTextArea.clear();

		final long startTime = Calendar.getInstance().getTimeInMillis();

		final XYDataset dataset = createDataset(seriesNumber, itemsNumber);

		final long dataCreatedTime = Calendar.getInstance().getTimeInMillis();

		logTextArea.appendText("Data created in " + (dataCreatedTime - startTime) + "ms\n");

		final JFreeChart chart = createChart(dataset);
		// chart.setTitle(new TextTitle("test", Font.font("Cambria",
		// FontWeight.BOLD, FontPosture.ITALIC, 40.0)));

		final ChartViewer viewer = new ChartViewer(chart);
		borderPane.setCenter(viewer);

		final long chartCreatedTime = Calendar.getInstance().getTimeInMillis();

		logTextArea.appendText("Chart created in " + (chartCreatedTime - dataCreatedTime) + "ms\n");

		final long chartDisplayedTime = Calendar.getInstance().getTimeInMillis();
		logTextArea.appendText("Chart displayed in " + (chartDisplayedTime - chartCreatedTime) + "ms\n");
	}

	public static Rectangle2D toRectangle(Bounds bounds) {
		return new Rectangle2D(bounds.getMinX(), bounds.getMinY(),
				bounds.getWidth(), bounds.getHeight());
	}
}
