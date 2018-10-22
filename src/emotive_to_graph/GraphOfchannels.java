package emotive_to_graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import gui.EmotivDetails;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GraphOfchannels extends JFrame{
    private String winTitle;
    ValueMarker marker;
    public GraphOfchannels(final String title, String path, String channel, String wave) {
        super(title);
        winTitle = title;
        final XYDataset dataset = createDataset(path, channel, wave);
        final JFreeChart chart = createChart(dataset);
     //   XYPlot plot = (XYPlot) chart.getPlot();
    //    plot.addDomainMarker(marker);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        chartPanel.setMouseZoomable(true, false);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }
        });

        /*JToolBar toolBar = new JToolBar();
        JButton b = new JButton("Reset");
        b.setActionCommand(ZOOM_RESET_BOTH_COMMAND);
        b.addActionListener(chartPanel);
        toolBar.add(b);
        setContentPane(toolBar);*/
        setContentPane(chartPanel);

    }

    /**
     * Creates a sample dataset.
     *
     * @return A sample dataset.
     */
    private XYDataset createDataset(String path, String channel, String wave) {
        if (Arrays.asList(EmotivDetails.waves).contains(wave)) {
            TimeSeries series = new TimeSeries(wave);
            // final TimeSeries series1 = new TimeSeries("betha");
            MultyGraph multyGraph = new MultyGraph(path);
            List<Double> signals = new ArrayList<>();
            signals = multyGraph.getSignalsOfWaveInChannel(channel, wave);
            List<Double> signals1 = new ArrayList<>();
            // signals1 = multyGraph.getSignalsOfWaveInChannel("IED_AF3", "High_beta");
            //Day current = new Day(1, 1, 1990);
            Second current = new Second();

            for (int i = 0; i < signals.size(); i++) {
               /* if (i == 450) {
                     marker = new ValueMarker( current.getEnd().getTime());  // position is the value on the axis
                    marker.setPaint(Color.YELLOW);
//marker.setLabel("here"); // see JavaDoc for labels, colors, strokes


                }*/
                series.add(current, signals.get(i));
                //      series1.add(current, signals1.get(i));
                current = (Second) current.next();
            }

        /*for (Double signal : signals) {
            try {
                series.add(current, signal);
                current = (Second) current.next();
            }
            catch (SeriesException e) {
                System.err.println("Error adding to series");
            }
        }*/

            TimeSeriesCollection tsc = new TimeSeriesCollection();
            tsc.addSeries(series);
            //     tsc.addSeries(series1);
            return tsc;
        }
        else{
            List<TimeSeries> timeSeriesList = new ArrayList<>();
            for (String s : EmotivDetails.waves) {
                timeSeriesList.add(new TimeSeries(s));
            }
            MultyGraph multyGraph = new MultyGraph(path);
            //List<List<Double>> signalsList = new ArrayList<>();
            List<Double> [] signalsList = new ArrayList[5];
            for (int i = 0; i < EmotivDetails.waves.length; i++) {
                signalsList[i] = new ArrayList<>();
                signalsList[i] = multyGraph.getSignalsOfWaveInChannel(channel, EmotivDetails.waves[i]);
            }
      //      List<Double> signals1 = new ArrayList<>();
      //      signals1 = multyGraph.getSignalsOfWaveInChannel(channel, wave);
           // List<Double> signals1 = new ArrayList<>();
            // signals1 = multyGraph.getSignalsOfWaveInChannel("IED_AF3", "High_beta");
            //Day current = new Day(1, 1, 1990);
            Second current = new Second();
            for (int i = 0; i < signalsList[0].size(); i++) {
                for (int j = 0; j < EmotivDetails.waves.length; j++) {
                    List<Double> list = signalsList[j];
                    double d = list.get(i);
                    timeSeriesList.get(j).add(current, d);
                }
                current = (Second) current.next();
            }

            TimeSeriesCollection tsc = new TimeSeriesCollection();
            for (TimeSeries timeSeries : timeSeriesList) {
                tsc.addSeries(timeSeries);
            }
            //     tsc.addSeries(series1);
            return tsc;
        }
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return A sample chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        return ChartFactory.createTimeSeriesChart(
                winTitle,
                "Time",
                "Signal",
                dataset
        );
    }

    /**
     * Starting point for the application.
     *
     * @param args  ignored.
     */
   /* public static void main(final String[] args) {
        if (args.length == 4) {
            final String title = args[0];
            final GraphOfchannels graph = new GraphOfchannels(args[0], args[1], args[2], args[3]);
            graph.pack();
            //RefineryUtilities.positionFrameRandomly(graph);
            graph.setVisible(true);
        }
    }*/
}
