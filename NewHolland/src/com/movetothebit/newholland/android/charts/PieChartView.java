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
 * PieChartDemo01View.java
 * -----------------
 * (C) Copyright 2010, 2011, by ICOMSYSTECH Co.,Ltd.
 *
 * Original Author:  Niwano Masayoshi (for ICOMSYSTECH Co.,Ltd);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 19-Nov-2010 : Version 0.0.1 (NM);
 */

package com.movetothebit.newholland.android.charts;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.plot.PiePlot;
import org.afree.chart.title.TextTitle;
import org.afree.data.general.DefaultPieDataset;
import org.afree.data.general.PieDataset;
import org.afree.graphics.SolidColor;
import org.afree.graphics.geom.Font;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * PieChartDemo01View
 */
public class PieChartView extends DemoView {

    /**
     * constructor
     * @param context
     */
    public PieChartView(Context context) {
        super(context);

        PieDataset dataset = createDataset();
        AFreeChart chart = createChart(dataset);

        setChart(chart);
    }
    /**
     * constructor
     * @param context
     */
    public PieChartView(Context context, AttributeSet att) {
        super(context, att);

        PieDataset dataset = createDataset();
        AFreeChart chart = createChart(dataset);
        
        setChart(chart);
     
    }
    /**
     * Creates a sample dataset.
     * @return a sample dataset.
     */
    private static PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("", new Double(10.0));
       
        return dataset;
    }
    public void paintChart(PieDataset dataset) {
    	AFreeChart chart = createChart(dataset);
        setChart(chart);
    	
    }
    /**
     * Creates a chart.
     * @param dataset the dataset.
     * @return a chart.
     */
    private static AFreeChart createChart(PieDataset dataset) {

        AFreeChart chart = ChartFactory.createPieChart(
        		"Penetracion de marcas", // chart title
                dataset, // data
                true, // include legend
                true,
                false);
        
        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));
        
        TextTitle title = chart.getTitle();
        title.setToolTipText("A title tooltip!");

        PiePlot plot = (PiePlot) chart.getPlot();
        
        plot.setLabelFont(new Font("SansSerif", Typeface.NORMAL, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;

    }
}
