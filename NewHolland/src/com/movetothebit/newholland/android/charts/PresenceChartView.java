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

import org.afree.chart.AFreeChart;
import org.afree.chart.axis.DateAxis;
import org.afree.chart.axis.DateTickMarkPosition;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.axis.ValueAxis;
import org.afree.chart.plot.DatasetRenderingOrder;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.XYPlot;
import org.afree.chart.renderer.xy.StandardXYItemRenderer;
import org.afree.chart.renderer.xy.XYBarRenderer;
import org.afree.chart.renderer.xy.XYItemRenderer;
import org.afree.data.category.CategoryDataset;
import org.afree.data.time.Day;
import org.afree.data.time.Month;
import org.afree.data.time.TimePeriodAnchor;
import org.afree.data.time.TimeSeries;
import org.afree.data.time.TimeSeriesCollection;
import org.afree.data.xy.IntervalXYDataset;
import org.afree.data.xy.XYDataset;
import org.afree.date.MonthConstants;
import org.afree.graphics.GradientColor;
import org.afree.graphics.SolidColor;

import com.movetothebit.newholland.android.db.DBHelper;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * OverlaidXYPlotDemo02View
 */
public class PresenceChartView extends DemoView {

    /**
     * constructor
     * @param context
     */
    public PresenceChartView(Context context) {
        super(context);

        
        final AFreeChart chart = createChart();
        setChart(chart);
    }

    public PresenceChartView(Context context, AttributeSet attrs) {
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
     * Creates a sample dataset.
     * @return The dataset.
     */
    private static IntervalXYDataset createDataset1() {

        TimeSeries series1 = new TimeSeries("Inscripciones");

        for(int i = 1; i <= 4; i++) {
        	series1.add(new Month( i, 2013),
        			i*20);
//        	series1.add(new Day(i, MonthConstants.OCTOBER, 2011),
//        			Math.random() * 7000 + 11000);
        }

        TimeSeriesCollection result = new TimeSeriesCollection(series1);
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
