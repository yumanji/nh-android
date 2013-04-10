/* ===========================================================
 * AFreeChart : a free chart library for Android(tm) platform.
 *              (based on JFreeChart and JCommon)
 * ===========================================================
 *
 * (C) Copyright 2010, by ICOMSYSTECH Co.,Ltd.
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:
 *    AFreeChart: http://code.google.com/p/afreechart/
 *    JFreeChart: http://www.jfree.org/jfreechart/index.html
 *    JCommon   : http://www.jfree.org/jcommon/index.html
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * [Android is a trademark of Google Inc.]
 *
 * -----------------
 * OverlaidXYPlotDemo02View.java
 * -----------------
 * (C) Copyright 2011, by ICOMSYSTECH Co.,Ltd.
 *
 * Original Author:  Yamakami Souichirou (for ICOMSYSTECH Co.,Ltd);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 18-Oct-2011 : Added new sample code (SY);
 */

package com.movetothebit.newholland.android.charts;

import java.text.NumberFormat;

import org.afree.chart.AFreeChart;
import org.afree.chart.axis.DateAxis;
import org.afree.chart.axis.DateTickMarkPosition;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.axis.ValueAxis;
import org.afree.chart.plot.DatasetRenderingOrder;
import org.afree.chart.plot.Marker;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.ValueMarker;
import org.afree.chart.plot.XYPlot;
import org.afree.chart.renderer.xy.StandardXYItemRenderer;
import org.afree.chart.renderer.xy.XYBarRenderer;
import org.afree.chart.renderer.xy.XYItemRenderer;
import org.afree.data.Range;
import org.afree.data.time.Month;
import org.afree.data.time.TimePeriodAnchor;
import org.afree.data.time.TimeSeries;
import org.afree.data.time.TimeSeriesCollection;
import org.afree.data.xy.IntervalXYDataset;
import org.afree.data.xy.XYDataset;
import org.afree.graphics.GradientColor;
import org.afree.graphics.SolidColor;
import org.afree.ui.LengthAdjustmentType;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.movetothebit.newholland.android.charts.model.MonthDataSet;
import com.movetothebit.newholland.android.helpers.DateHelper;
import com.movetothebit.newholland.android.utils.lConstants;

/**
 * OverlaidXYPlotDemo02View
 */
public class OverlayChartView extends DemoView implements lConstants{
	private static final String TAG = "OverlayChart";
    /**
     * constructor
     * @param context
     */
    public OverlayChartView(Context context) {
        super(context);

        
        final AFreeChart chart = createChart();
        setChart(chart);
    }

    public OverlayChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final AFreeChart chart = createChart();
		setChart(chart);
	}
    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    public void paintChart() {
    	AFreeChart chart = createChart();
        setChart(chart);
    	
    }
    
    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    public void paintChart(MonthDataSet[] dataset,int i , float objetive) {
    	AFreeChart chart = null;
    	if(i== MARKET){
    		chart = createKnownChart(dataset, objetive);
    	}else if(i == PRESENCE){
    		chart = createPresenceChart(dataset, objetive);
    	}else if(i == EFECTIVITY){
    		chart = createEffectivityChart(dataset, objetive);
    	}
    	
        setChart(chart);
    	
    }
    

	/**
     * Creates an overlaid chart.
     * @return The chart.
     */
    private static AFreeChart createChart() {

        DateAxis domainAxis = new DateAxis("Date");
        domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        
        ValueAxis rangeAxis = new NumberAxis("Cuota de mercado");

        // create plot...
        IntervalXYDataset data1 = createDataset1();
        XYItemRenderer renderer1 = new XYBarRenderer(0.20);
        GradientColor gp0 = new GradientColor(Color.BLUE, Color.rgb(0, 0, 64));
     
        renderer1.setSeriesPaintType(0, gp0);
        XYPlot plot = new XYPlot(data1, domainAxis, rangeAxis, renderer1);

        ValueAxis rangeAxis2 = new NumberAxis("Inscripciones");
        plot.setRangeAxis(1, rangeAxis2);

        // create subplot 2...
        XYDataset data2A = createDataset2A();
        plot.setDataset(1, data2A);
        XYItemRenderer renderer2A = new StandardXYItemRenderer();
        plot.setRenderer(1, renderer2A);
        renderer2A.setSeriesStroke(0, 2.0f);

        XYDataset data2B = createDataset2B();
        plot.setDataset(2, data2B);
        plot.setRenderer(2, new StandardXYItemRenderer());
        plot.getRenderer(2).setSeriesStroke(0, 2.0f);

        plot.mapDatasetToRangeAxis(2, 1);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.setOrientation(PlotOrientation.VERTICAL);

        AFreeChart chart = new AFreeChart(
                "Penetracion de mercado",
                AFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true);
        
        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));

        return chart;
    }
    
    /**
     * Creates an overlaid chart.
     * @return The chart.
     */
    private static AFreeChart createEffectivityChart(MonthDataSet[] dataSet, float objetive ) {

        DateAxis domainAxis = new DateAxis("");
        domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        
        NumberAxis rangeAxis = new NumberAxis("Efectividad");
        rangeAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        rangeAxis.setRange(new Range(0, 1));
        // create plot...
        IntervalXYDataset data1 = createEffectivityDataset(dataSet);
        XYItemRenderer renderer1 = new XYBarRenderer(0.20);
        GradientColor gp0 = new GradientColor(Color.BLUE, Color.rgb(0, 0, 64));
     
        renderer1.setSeriesPaintType(0, gp0);
        XYPlot plot = new XYPlot(data1, domainAxis, rangeAxis, renderer1);

        ValueAxis rangeAxis2 = new NumberAxis("Total inscripciones");
      
        plot.setRangeAxis(1, rangeAxis2);

        // create subplot 2...
        XYDataset data2A = createTotalDataset(dataSet);
        plot.setDataset(1, data2A);
        XYItemRenderer renderer2A = new StandardXYItemRenderer();
        plot.setRenderer(1, renderer2A);
        renderer2A.setSeriesStroke(0, 2.0f);

        plot.mapDatasetToRangeAxis(1, 1);
        
        Marker marker_H = new ValueMarker(objetive);
        marker_H.setLabelOffsetType(LengthAdjustmentType.EXPAND);
        marker_H.setPaintType(new SolidColor(Color.GREEN));
        marker_H.setStroke(2.0f);
        plot.addRangeMarker(marker_H);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.setOrientation(PlotOrientation.VERTICAL);

        AFreeChart chart = new AFreeChart(
                "Efectividad",
                AFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true);
        
        chart.setBackgroundPaintType(new SolidColor(Color.TRANSPARENT));

        return chart;
    }
    /**
     * Creates an overlaid chart.
     * @return The chart.
     */
    private static AFreeChart createPresenceChart(MonthDataSet[] dataSet, float objetive ) {

        DateAxis domainAxis = new DateAxis("");
        domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        
        NumberAxis rangeAxis = new NumberAxis("Presencia %");
        rangeAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        rangeAxis.setRange(new Range(0, 1));
        // create plot...
        IntervalXYDataset data1 = createPresenceDataset(dataSet);
        XYItemRenderer renderer1 = new XYBarRenderer(0.20);
        GradientColor gp0 = new GradientColor(Color.BLUE, Color.rgb(0, 0, 64));
     
        renderer1.setSeriesPaintType(0, gp0);
        XYPlot plot = new XYPlot(data1, domainAxis, rangeAxis, renderer1);

        ValueAxis rangeAxis2 = new NumberAxis("Total Inscripciones");
        
        plot.setRangeAxis(1, rangeAxis2);

        // create subplot 2...
        XYDataset data2A = createTotalDataset(dataSet);
        plot.setDataset(1, data2A);
        XYItemRenderer renderer2A = new StandardXYItemRenderer();
        plot.setRenderer(1, renderer2A);
        renderer2A.setSeriesStroke(0, 2.0f);
        
        Marker marker_H = new ValueMarker(objetive);
        marker_H.setLabelOffsetType(LengthAdjustmentType.EXPAND);
        marker_H.setPaintType(new SolidColor(Color.GREEN));
        marker_H.setStroke(2.0f);
        
        plot.addRangeMarker(marker_H);
        plot.mapDatasetToRangeAxis(1, 1);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.setOrientation(PlotOrientation.VERTICAL);

        AFreeChart chart = new AFreeChart(
                "Presencia",
                AFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true);
        
        chart.setBackgroundPaintType(new SolidColor(Color.TRANSPARENT));

        return chart;
    }	/**
     * Creates an overlaid chart.
     * @return The chart.
     */
   
	/**
     * Creates an overlaid chart.
     * @return The chart.
     */
    private static AFreeChart createKnownChart(MonthDataSet[] dataSet , float objetive) {

        DateAxis domainAxis = new DateAxis("");
        domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        
        NumberAxis rangeAxis = new NumberAxis("Conocimiento de mercado %");
        rangeAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        rangeAxis.setRange(new Range(0, 1));
        // create plot...
        IntervalXYDataset data1 = createMarketDataset(dataSet);
        XYItemRenderer renderer1 = new XYBarRenderer(0.20);
        GradientColor gp0 = new GradientColor(Color.BLUE, Color.rgb(0, 0, 64));
     
        renderer1.setSeriesPaintType(0, gp0);
        XYPlot plot = new XYPlot(data1, domainAxis, rangeAxis, renderer1);

        ValueAxis rangeAxis2 = new NumberAxis("Total Inscripciones");
      
        plot.setRangeAxis(1, rangeAxis2);

        // create subplot 2...
        XYDataset data2A = createTotalDataset(dataSet);
        plot.setDataset(1, data2A);
        XYItemRenderer renderer2A = new StandardXYItemRenderer();
        plot.setRenderer(1, renderer2A);
        renderer2A.setSeriesStroke(0, 2.0f);

        
        
        plot.mapDatasetToRangeAxis(1, 1);
        
        Marker marker_H = new ValueMarker(objetive);
        marker_H.setLabelOffsetType(LengthAdjustmentType.EXPAND);
        marker_H.setPaintType(new SolidColor(Color.GREEN));
        marker_H.setStroke(2.0f);
        
        plot.addRangeMarker(marker_H);
        
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.setOrientation(PlotOrientation.VERTICAL);

        AFreeChart chart = new AFreeChart(
                "Conocimiento de mercado",
                AFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true);
        
        chart.setBackgroundPaintType(new SolidColor(Color.TRANSPARENT));

        return chart;
    }
    /**
     * Creates a sample dataset.
     * @return The dataset.
     */
    private static IntervalXYDataset createMarketDataset(MonthDataSet[] dataSet) {

        TimeSeries series1 = new TimeSeries("Conocimiento de mercado");

        for(int i = 0; i < dataSet.length; i++) {
        	if(dataSet[i]!=null){
        		Log.d(TAG, "Mercado: " + dataSet[i].month + dataSet[i].year +"   "+  100*(dataSet[i].known/dataSet[i].total));
        	series1.add(DateHelper.getMonthFromStrings(dataSet[i].month,dataSet[i].year),
        			(dataSet[i].known/dataSet[i].total));
        	}
        }

        TimeSeriesCollection result = new TimeSeriesCollection(series1);
        result.setXPosition(TimePeriodAnchor.MIDDLE);
        return result;
    }
    /**
     * Creates a sample dataset.
     * @return The dataset.
     */
    private static IntervalXYDataset createPresenceDataset(MonthDataSet[] dataSet) {

        TimeSeries series1 = new TimeSeries("Presencia");

        for(int i = 0; i < dataSet.length; i++) {
        	if(dataSet[i]!=null){
        		Log.d(TAG, "Presencia: " + dataSet[i].month + dataSet[i].year +"   "+  100*(dataSet[i].offert/dataSet[i].known));
	        	series1.add(DateHelper.getMonthFromStrings(dataSet[i].month,dataSet[i].year),
	        			(dataSet[i].offert/dataSet[i].known));
        	}
        }

        TimeSeriesCollection result = new TimeSeriesCollection(series1);
        result.setXPosition(TimePeriodAnchor.MIDDLE);
        return result;
    }
    /**
     * Creates a sample dataset.
     * @return The dataset.
     */
    private static IntervalXYDataset createEffectivityDataset(MonthDataSet[] dataSet) {

        TimeSeries series1 = new TimeSeries("Efectividad");

        for(int i = 0; i < dataSet.length; i++) {
        	if(dataSet[i]!=null){
        		Log.d(TAG, "Efectividad: " + dataSet[i].month + dataSet[i].year +"   "+  100*(dataSet[i].win/dataSet[i].offert));
        	series1.add(DateHelper.getMonthFromStrings(dataSet[i].month,dataSet[i].year),
        			(dataSet[i].win/dataSet[i].offert));
        	}
        }

        TimeSeriesCollection result = new TimeSeriesCollection(series1);
        result.setXPosition(TimePeriodAnchor.MIDDLE);
        return result;
    }
    
    /**
     * Creates a sample dataset.
     * @return The dataset.
     */
    private static XYDataset createTotalDataset(MonthDataSet[] dataSet) {

        TimeSeries series1 = new TimeSeries("Total");
       
        for(int i = 0; i < dataSet.length; i++) {
        	if(dataSet[i]!=null){
        		 Log.d(TAG, "Total: "+dataSet[i].total );
        	series1.add(DateHelper.getMonthFromStrings(dataSet[i].month,dataSet[i].year),
        			dataSet[i].total);
        	}
        }

        TimeSeriesCollection result = new TimeSeriesCollection(series1);
        result.setXPosition(TimePeriodAnchor.MIDDLE);
        return result;
    }
   
    /**
     * Creates a sample dataset.
     * @return The dataset.
     */
    private static IntervalXYDataset createDataset1() {

        TimeSeries series2 = new TimeSeries("isncripciones");

        for(int i = 1; i <= 4; i++) {
        	series2.add(new Month( i, 2013),
        			i*18);
        }

        TimeSeriesCollection result = new TimeSeriesCollection(series2);
        result.setXPosition(TimePeriodAnchor.MIDDLE);
        return result;
    }
    /**
     * Creates a sample dataset.
     * @return The dataset.
     */
    private static XYDataset createDataset2A() {

        TimeSeries series2 = new TimeSeries("LAMBORGHINI");

        for(int i = 1; i <= 4; i++) {
        	series2.add(new Month( i, 2013),
        			i*18);
        }

        TimeSeriesCollection result = new TimeSeriesCollection(series2);
        result.setXPosition(TimePeriodAnchor.MIDDLE);
        return result;
    }

    /**
     * Creates a sample dataset.
     * @return The dataset.
     */
    private static XYDataset createDataset2B() {

        TimeSeries series2 = new TimeSeries("NEW HOLLAND");

        for(int i = 1; i <= 4; i++) {
        	series2.add(new Month( i, 2013),
        			i*24);
        }

        TimeSeriesCollection result = new TimeSeriesCollection(series2);
        result.setXPosition(TimePeriodAnchor.MIDDLE);
        return result;
    }

}
