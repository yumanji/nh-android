package com.movetothebit.newholland.android.ui;

import org.afree.chart.demo.activity.AnnotationDemo01Activity;
import org.afree.chart.demo.activity.BarChartDemo01Activity;
import org.afree.chart.demo.activity.CandleStickChartDemo03Activity;
import org.afree.chart.demo.activity.DeviationRendererDemo02Activity;
import org.afree.chart.demo.activity.JDBCXYChartDemoActivity;
import org.afree.chart.demo.activity.MarkerDemo01Activity;
import org.afree.chart.demo.activity.OverlaidXYPlotDemo02Activity;
import org.afree.chart.demo.activity.PieChartDemo01Activity;
import org.afree.chart.demo.activity.ScatterPlotDemo03Activity;
import org.afree.chart.demo.activity.SlidingCategoryPlotDemo01Activity;
import org.afree.chart.demo.activity.SlidingCategoryPlotDemo02Activity;
import org.afree.chart.demo.activity.TimeSeriesChartDemo01Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;

public class ChartsActivity extends BaseActivity{

	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        getSupportActionBar().show();	
	        getSupportActionBar().setDisplayShowHomeEnabled(true);
	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	        setContentView(R.layout.charts_activity);	
	        
	        Button chart1  = (Button)findViewById(R.id.button1);
	        chart1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), AnnotationDemo01Activity.class);
					startActivity(i);					
				}
			});
	        
	        Button chart2  = (Button)findViewById(R.id.button2);
	        chart2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), BarChartDemo01Activity.class);
					startActivity(i);					
				}
			});
	        
	        Button chart3  = (Button)findViewById(R.id.button3);
	        chart3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), CandleStickChartDemo03Activity.class);
					startActivity(i);					
				}
			});
	        
	        Button chart4  = (Button)findViewById(R.id.button4);
	        chart4.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), DeviationRendererDemo02Activity.class);
					startActivity(i);					
				}
			});
	        
	        Button chart5  = (Button)findViewById(R.id.button5);
	        chart5.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), JDBCXYChartDemoActivity.class);
					startActivity(i);					
				}
			});
	        
	        Button chart6  = (Button)findViewById(R.id.button6);
	        chart6.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), MarkerDemo01Activity.class);
					startActivity(i);					
				}
			});
	        
	        Button chart7  = (Button)findViewById(R.id.button7);
	        chart7.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), OverlaidXYPlotDemo02Activity.class);
					startActivity(i);					
				}
			});
	        
	        Button chart8  = (Button)findViewById(R.id.button8);
	        chart8.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), PieChartDemo01Activity.class);
					startActivity(i);					
				}
			});
	        
	        Button chart9  = (Button)findViewById(R.id.button9);
	        chart9.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), ScatterPlotDemo03Activity.class);
					startActivity(i);					
				}
			});
	        
	        Button chart10  = (Button)findViewById(R.id.button10);
	        chart10.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), SlidingCategoryPlotDemo01Activity.class);
					startActivity(i);					
				}
			});
	        
	        Button chart11  = (Button)findViewById(R.id.button11);
	        chart11.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), SlidingCategoryPlotDemo02Activity.class);
					startActivity(i);					
				}
			});
	        
	        Button chart12  = (Button)findViewById(R.id.button12);
	        chart12.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), TimeSeriesChartDemo01Activity.class);
					startActivity(i);					
				}
			});
	       
	     
	       
	    }
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	
	}
	
}
