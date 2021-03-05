package sample;


import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Graph {
    final NumberAxis xAxis;
    final NumberAxis yAxis;
    final NumberAxis xAxis2;
    final NumberAxis yAxis2;
    XYChart.Series series;
    XYChart.Series series1;
    XYChart.Series series2;
    XYChart.Series series3;
    LineChart<Number, Number> lineChart;
    LineChart<Number, Number> lineChartAll;


    public Graph(String x, String y) {
        xAxis = new NumberAxis();
        yAxis = new NumberAxis();
        xAxis.setLabel(x);
        yAxis.setLabel(y);
        xAxis2 = new NumberAxis();
        yAxis2 = new NumberAxis();
        xAxis2.setLabel(x);
        yAxis2.setLabel(y);

        series = new XYChart.Series();
        series1 = new XYChart.Series();
        series2 = new XYChart.Series();
        series3 = new XYChart.Series();

        lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChartAll = new LineChart<Number, Number>(xAxis2, yAxis2);
        lineChart.setScaleX(0.8);
        lineChart.setScaleY(0.8);
        lineChart.getData().add(series);
        lineChartAll.getData().addAll(series1, series2, series3);

    }

    public LineChart drawGraph(double x, double y) {
        series.getData().add(new XYChart.Data(x, y));

        return lineChart;
    }

    public LineChart drawAllGraph(double x, double speed, double size, double sense) {
        series1.getData().add(new XYChart.Data(x, speed));
        series2.getData().add(new XYChart.Data(x, size));
        series3.getData().add(new XYChart.Data(x, sense));
        return lineChartAll;
    }

    public LineChart getGraph() {
        return lineChart;
    }

    public LineChart getAllGraph() {
        return lineChartAll;
    }

    public void resetGraph() {
        series.getData().clear();
        series1.getData().clear();
        series2.getData().clear();
        series3.getData().clear();

    }


}
