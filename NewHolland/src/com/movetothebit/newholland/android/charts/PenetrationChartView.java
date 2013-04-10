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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
import org.afree.data.time.Day;
import org.afree.data.time.TimePeriodAnchor;
import org.afree.data.time.TimeSeries;
import org.afree.data.time.TimeSeriesCollection;
import org.afree.data.xy.IntervalXYDataset;
import org.afree.data.xy.XYDataset;
import org.afree.date.MonthConstants;
import org.afree.graphics.GradientColor;
import org.afree.graphics.SolidColor;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.movetothebit.newholland.android.charts.model.MonthDataSet;
import com.movetothebit.newholland.android.helpers.DateHelper;
import com.movetothebit.newholland.android.model.Brand;

/**
 * OverlaidXYPlotDemo02View
 */
public class PenetrationChartView extends DemoView {

    /**
     * constructor
     * @param context
     */
    public PenetrationChartView(Context context) {
        super(context);

        
        final AFreeChart chart = createChart();
        setChart(chart);
    }

    public PenetrationChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final AFreeChart chart = createChart();
		setChart(chart);
	}
//   
    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    public void paintChart(MonthDataSet[] dataset,List<Brand> brands) {
    	AFreeChart chart = createPenetrationChart(dataset, brands);
        setChart(chart);    	
    }
	/**
     * Creates an overlaid chart.
     * @return The chart.
     */
  
    /**
     * Creates an overlaid chart.
     * @return The chart.
     */
    private static AFreeChart createPenetrationChart(MonthDataSet[] dataSet,List<Brand> brands ) {

        DateAxis domainAxis = new DateAxis("");
        domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        domainAxis.setDateFormatOverride(new SimpleDateFormat("MMM yy",new Locale("es", "ES")));
        
        List<Brand> topBrands = brands.subList(0, 10);
        List<Brand> otherBrands = brands.subList(10, brands.size());
        ValueAxis rangeAxis = new NumberAxis("Total de Inscripciones");

        // create plot...
        IntervalXYDataset total = createTotalDataset(dataSet);
        XYItemRenderer renderer1 = new XYBarRenderer(0.20);
        GradientColor gp0 = new GradientColor(Color.BLUE, Color.rgb(0, 0, 64));
     
        renderer1.setSeriesPaintType(0, gp0);
        XYPlot plot = new XYPlot(total, domainAxis, rangeAxis, renderer1);

        NumberAxis rangeAxis2 = new NumberAxis("Penetración");
        rangeAxis2.setNumberFormatOverride(NumberFormat.getPercentInstance());
        
        plot.setRangeAxis(1, rangeAxis2);
        int j= 0;
        
        for(Brand brand: topBrands){
        	XYDataset data2A = createBrandDataset(dataSet,brand);
            plot.setDataset(j+1, data2A);
            XYItemRenderer renderer2A = new StandardXYItemRenderer();
            plot.setRenderer(j+1, renderer2A);
            renderer2A.setSeriesStroke(0, 2.0f);
            plot.mapDatasetToRangeAxis(j+1, 1);
            ++j;        	
        }
        
        XYDataset data2A = createOtherDataset(dataSet,otherBrands);
        plot.setDataset(12, data2A);
        XYItemRenderer renderer2A = new StandardXYItemRenderer();
        
        plot.setRenderer(12, renderer2A);
        renderer2A.setSeriesStroke(0, 2.0f);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.setOrientation(PlotOrientation.VERTICAL);

        AFreeChart chart = new AFreeChart(
                "Penetracion de mercado",
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
    private static AFreeChart createChart() {
    	 
        
    	 DateAxis domainAxis = new DateAxis("");
         domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
         
         ValueAxis rangeAxis = new NumberAxis("");

         // create plot...
         IntervalXYDataset data1 = createDataset1();
         XYItemRenderer renderer1 = new XYBarRenderer(0.20);
         GradientColor gp0 = new GradientColor(Color.TRANSPARENT, Color.rgb(0, 0, 64));
      
         renderer1.setSeriesPaintType(0, gp0);
         XYPlot plot = new XYPlot(data1, domainAxis, rangeAxis, renderer1);

         ValueAxis rangeAxis2 = new NumberAxis("");
         plot.setRangeAxis(1, rangeAxis2);

        
         plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
         plot.setOrientation(PlotOrientation.VERTICAL);

         AFreeChart chart = new AFreeChart(
                 "",
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
    private static IntervalXYDataset createDataset1() {

        TimeSeries series1 = new TimeSeries("Series 1");

        for(int i = 1; i <= 1; i++) {
        	series1.add(new Day(i, MonthConstants.OCTOBER, 2011),
        			0);
        }

        TimeSeriesCollection result = new TimeSeriesCollection(series1);
        return result;
    }

    /**
     * Creates a sample dataset.
     * @return The dataset.
     */
    private static XYDataset createDataset2A() {

        TimeSeries series2 = new TimeSeries("Series 2-A");

        for(int i = 1; i <= 1; i++) {
        	series2.add(new Day(i, MonthConstants.OCTOBER, 2011),
        			0);
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

        TimeSeries series2 = new TimeSeries("Series 2-B");

        for(int i = 1; i <= 1; i++) {
        	series2.add(new Day(i, MonthConstants.OCTOBER, 2011),
        			0);
        }

        TimeSeriesCollection result = new TimeSeriesCollection(series2);
        result.setXPosition(TimePeriodAnchor.MIDDLE);
        return result;
    }
    /**
     * Creates a sample dataset.
     * @return The dataset.
     */
    private static IntervalXYDataset createTotalDataset(MonthDataSet[] dataSet) {

        TimeSeries series1 = new TimeSeries("Total");

        for(int i = 0; i < dataSet.length; i++) {
        	
        	if(dataSet[i].total>0){
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
    private static XYDataset createBrandDataset(MonthDataSet[] dataSet, Brand brand) {

        TimeSeries series1 = new TimeSeries(brand.name);

        
        for(int i = 0; i < dataSet.length; i++) {
        		if(dataSet[i].total>0){
        			for(int j = 0; j<dataSet[i].totalBrand.length;j++){
            			
            			if(dataSet[i].totalBrand[j].name.equals(brand.name)){
            				series1.add(DateHelper.getMonthFromStrings(dataSet[i].month,dataSet[i].year),
            	        			(dataSet[i].totalBrand[j].count/dataSet[i].total));
            			}
            		}
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
    private static XYDataset createOtherDataset(MonthDataSet[] dataSet, List<Brand> brands) {

        TimeSeries series1 = new TimeSeries("Otros");

        for(int i = 0; i < dataSet.length; i++) {
        	if(dataSet[i].total>0){
        		float count = 0;
        		for(int j = 0; j< dataSet[i].totalBrand.length; j++){
        			for(int k = 0; k<brands.size();k++){
        				if(brands.get(k).getName().equals(dataSet[i].totalBrand[j].name)){
        					count = count + dataSet[i].totalBrand[j].count;
        				}
        			}
        			
        			
        		}
        		
        		
	        	series1.add(DateHelper.getMonthFromStrings(dataSet[i].month,dataSet[i].year),
	        			(count/dataSet[i].total));
        	}
        }

        TimeSeriesCollection result = new TimeSeriesCollection(series1);
        result.setXPosition(TimePeriodAnchor.MIDDLE);
        return result;
    }


}
