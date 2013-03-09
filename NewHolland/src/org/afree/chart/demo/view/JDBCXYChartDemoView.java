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
 * JDBCXYChartDemoView.java
 * -----------------
 * (C) Copyright 2010, by ICOMSYSTECH Co.,Ltd.
 *
 * Original Author:  Niwano Masayoshi (for ICOMSYSTECH Co.,Ltd);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 26-Jan-2011 : Version 1.0.0 (NM);
 */ 

package org.afree.chart.demo.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.afree.chart.ChartFactory;
import org.afree.chart.AFreeChart;
import org.afree.chart.demo.DemoView;
import org.afree.data.jdbc.JDBCXYDataset;
import org.afree.data.xy.XYDataset;

import android.content.Context;

/**
 * JDBCXYChartDemoView
 * 
 * Notice
 * An XY chart that obtains data from a database via JDBC.
 * To run this demo, you need to have a JDBC driver and database that you can access via JDBC.
 * We used sqldroid-0.1 for validation.
 * You can get this driver from http://code.google.com/p/sqldroid/
 */
public class JDBCXYChartDemoView extends DemoView {

    /**
     * constructor
     * @param context
     */
    public JDBCXYChartDemoView(Context context, String dbFilePath) {
        super(context);

        final AFreeChart chart = createChart(dbFilePath);

        setChart(chart);
    }

    /**
     * Initialize table.
     * @param con
     */
    private static void init(Connection con) throws Exception {
        boolean create = con.createStatement().execute(
            "CREATE TABLE XYData1 (X REAL, SERIESE1 REAL, SERIESE2 REAL, SERIESE3 REAL, SERIESE4 REAL)");
        
        if( create ) {
	        con.createStatement().execute(
	            "INSERT INTO XYData1 (X, SERIESE1, SERIESE2, SERIESE3, SERIESE4 ) VALUES (10, 32.1, 53.4, 32.1, 53.4)");
	        con.createStatement().execute(
	            "INSERT INTO XYData1 (X, SERIESE1, SERIESE2, SERIESE3, SERIESE4 ) VALUES (20, 54.3, 75.2, 54.3, 75.2)");
	        con.createStatement().execute(
	            "INSERT INTO XYData1 (X, SERIESE1, SERIESE2, SERIESE3, SERIESE4 ) VALUES (30, 55.9, 37.1, 55.9, 37.1)");
	        con.createStatement().execute(
	            "INSERT INTO XYData1 (X, SERIESE1, SERIESE2, SERIESE3, SERIESE4 ) VALUES (40, 55.2, 27.5, 55.2, 27.5)");
	        con.createStatement().execute(
	            "INSERT INTO XYData1 (X, SERIESE1, SERIESE2, SERIESE3, SERIESE4 ) VALUES (50, 49.8, 22.3, 49.8, 22.3)");
	        con.createStatement().execute(
	            "INSERT INTO XYData1 (X, SERIESE1, SERIESE2, SERIESE3, SERIESE4 ) VALUES (60, 48.4, 17.7, 48.4, 17.7)");
	        con.createStatement().execute(
	            "INSERT INTO XYData1 (X, SERIESE1, SERIESE2, SERIESE3, SERIESE4 ) VALUES (70, 49.7, 15.3, 49.7, 15.3)");
	        con.createStatement().execute(
	            "INSERT INTO XYData1 (X, SERIESE1, SERIESE2, SERIESE3, SERIESE4 ) VALUES (80, 44.4, 12.1, 44.4, 12.1)");
	        con.createStatement().execute(
	            "INSERT INTO XYData1 (X, SERIESE1, SERIESE2, SERIESE3, SERIESE4 ) VALUES (90, 46.3, 11.0, 46.3, 11.0)");
        }
    }

    /**
     * Creates a sample dataset.
     * @return a sample dataset.
     */
    private static XYDataset createDataset(String dbFilePath) {

        JDBCXYDataset data = null;

        String url = "jdbc:sqldroid:" + dbFilePath;
        Connection con;

        try {
            Class.forName("com.lemadi.storage.database.sqldroid.SqldroidDriver");
        } catch (ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }

        try {
            con = DriverManager.getConnection(url);

            // init data
            init(con);

            data = new JDBCXYDataset(con);
            String sql = "SELECT * FROM XYDATA1;";
            data.executeQuery(sql);
            con.close();
        }

        catch (SQLException e) {
            System.err.print("SQLException: ");
            System.err.println(e.getMessage());
        }

        catch (Exception e) {
            System.err.print("Exception: ");
            System.err.println(e.getMessage());
        }

        return data;
    }

    /**
     * Creates a sample chart.
     * @param dataset the dataset for the chart.
     * @return a sample chart.
     */
    private static AFreeChart createChart(String dbFilePath) {


        // read the data from the database...
        XYDataset data = createDataset(dbFilePath);

        // create the chart...
        AFreeChart chart = ChartFactory.createTimeSeriesChart(
            "JDBC XY Chart Demo", // chart title
            "Date",
            "Value",
            data,                 // data
            true,                 // include legend
            true,
            false
        );

        return chart;

    }
}
