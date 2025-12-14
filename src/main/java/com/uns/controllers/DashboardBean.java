package com.uns.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
import java.io.Serializable;

@Named("dashboardBean")
@RequestScoped
public class DashboardBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private LineChartModel lineModel;
    private PieChartModel pieModel;

    @PostConstruct
    public void init() {
        createLineModel();
        createPieModel();
    }

    private void createLineModel() {
        lineModel = new LineChartModel();

        LineChartSeries series = new LineChartSeries();
        series.setLabel("Requerimientos");

        series.set("Julio", 15);
        series.set("Agosto", 18);
        series.set("Septiembre", 22);
        series.set("Octubre", 20);
        series.set("Noviembre", 24);
        series.set("Diciembre", 24);

        lineModel.addSeries(series);

        lineModel.setTitle("Requerimientos por Mes");
        lineModel.setLegendPosition("e");
        lineModel.getAxis(AxisType.Y).setLabel("Cantidad");
        lineModel.getAxis(AxisType.Y).setMin(0);
        lineModel.getAxis(AxisType.Y).setMax(30);
    }

    private void createPieModel() {
        pieModel = new PieChartModel();

        pieModel.set("Pendiente", 8);
        pieModel.set("Aprobado", 12);
        pieModel.set("Enviada", 15);
        pieModel.set("Anulada", 3);

        pieModel.setTitle("Estado de Órdenes de Compra");
        pieModel.setLegendPosition("w");
        pieModel.setShowDataLabels(true);
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }
}