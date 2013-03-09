package com.movetothebit.newholland.android.ui;

import java.util.Random;

import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.widgets.BarChartView;
import com.movetothebit.newholland.android.widgets.PresenceChartView;

public class DataActivity extends BaseActivity{

	public BarChartView chartView;
	public PresenceChartView presenceChartView;
	public Button chartButton;
	public RelativeLayout infoLayout;
	
	public Spinner dealerSpinner;
	public Spinner dealerManSpinner;
	public Spinner placeSpinner;
	public Spinner modelSpinner;
	public Spinner brandSpinner;
	public Spinner segmentSpinner;
	public Spinner monthSpinner;
	
	
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        getSupportActionBar().show();	
	        getSupportActionBar().setDisplayShowHomeEnabled(true);
	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	        setContentView(R.layout.data_layout);	
	        
	       infoLayout = (RelativeLayout)findViewById(R.id.infoLayout);
	        chartButton = (Button) findViewById(R.id.chartButton);
	        chartView  = (BarChartView)findViewById(R.id.chartView);
	        
	        dealerSpinner = (Spinner)findViewById(R.id.dealerSpinner);
	        dealerSpinner.setOnItemSelectedListener(spinnerListener);
	        dealerManSpinner = (Spinner)findViewById(R.id.dealerManSpinner);
	        dealerManSpinner.setOnItemSelectedListener(spinnerListener);
	        modelSpinner = (Spinner)findViewById(R.id.modelSpinner);
	        modelSpinner.setOnItemSelectedListener(spinnerListener);
	        monthSpinner = (Spinner)findViewById(R.id.monthSpinner);
	        monthSpinner.setOnItemSelectedListener(spinnerListener);
	        brandSpinner = (Spinner)findViewById(R.id.brandSpinner);
	        brandSpinner.setOnItemSelectedListener(spinnerListener);
	        placeSpinner = (Spinner)findViewById(R.id.placeSpinner);
	        placeSpinner.setOnItemSelectedListener(spinnerListener);
	        segmentSpinner = (Spinner)findViewById(R.id.segmentSpinner);
	        segmentSpinner.setOnItemSelectedListener(spinnerListener);
	        
	        presenceChartView  = (PresenceChartView)findViewById(R.id.presenceChartView);
	        chartView.paintChart(getDataset());
	        presenceChartView.paintChart(getPresenceDataSet());
	        chartButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					refreshCharts();
					
				}
			});
	       
	    }
	
	OnItemSelectedListener spinnerListener= new OnItemSelectedListener(){

		@Override
	    public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
	            refreshCharts();
	    }
	    @Override
	      public void onNothingSelected(AdapterView<?> arg0) {
	    //operation with that item that onItemSelected() did not triggered. I mean, let's say you have 3 items on the spinner: A,B,C. Initially what we see its the A item and on this item this method will apply.
	    }
	};
	public void refreshCharts(){
		chartView.paintChart(getDataset());
		presenceChartView.paintChart(getPresenceDataSet());
	}
	
	
	 private CategoryDataset getPresenceDataSet() {

	        // row keys...
	        String series1 = "Indice %";
	        String series2 = "Objetivo NH";
//	        String series3 = "Third";

	        // column keys...
	        String category1 = "Conocimientos de mercado";
	        String category2 = "Presencia";
	        String category3 = "Efectividad";
	        String category4 = "Presencia total";
	        String category5 = "Cuota de mercado";

	        // create the dataset...
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        
	        Random r = new Random();
	        
	        dataset.addValue(1 + (100 - 1) * r.nextDouble(), series1, category1);
	        dataset.addValue(1 + (100 - 1) * r.nextDouble(), series1, category2);
	        dataset.addValue(1 + (100 - 1) * r.nextDouble(), series1, category3);
	        dataset.addValue(1 + (100 - 1) * r.nextDouble(), series1, category4);
	        dataset.addValue(1 + (100 - 1) * r.nextDouble(), series1, category5);

	        dataset.addValue(1 + (100 - 1) * r.nextDouble(), series2, category1);
	        dataset.addValue(1 + (100 - 1) * r.nextDouble(), series2, category2);
	        dataset.addValue(1 + (100 - 1) * r.nextDouble(), series2, category3);
	        dataset.addValue(1 + (100 - 1) * r.nextDouble(), series2, category4);
	        dataset.addValue(1 + (100 - 1) * r.nextDouble(), series2, category5);


	        return dataset;

	    }
	  private CategoryDataset getDataset() {

	        // row keys...
	        String series1 = "Motivo";
     
	        String[] label = getResources().getStringArray(R.array.missing_operation);
	        // column keys...
	        String category1 = label[0];
	        String category2 = label[1];
	        String category3 = label[2];
	        String category4 = label[3];
	        String category5 = label[4];
	        String category6 = label[5];
	        String category7 = label[6];
	        String category8 = label[7];
	        String category9 = label[8];
	        String category10 = label[9];
	        // create the dataset...
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        
	        Random r = new Random();
	        
	      
	        dataset.addValue(1 + (20 - 1) * r.nextDouble(), series1, category1);
	        dataset.addValue(1 + (20 - 1) * r.nextDouble(), series1, category2);
	        dataset.addValue(1 + (20 - 1) * r.nextDouble(), series1, category3);
	        dataset.addValue(1 + (20 - 1) * r.nextDouble(), series1, category4);
	        dataset.addValue(1 + (20 - 1) * r.nextDouble(), series1, category5);
	        dataset.addValue(1 + (20 - 1) * r.nextDouble(), series1, category6);
	        dataset.addValue(1 + (20 - 1) * r.nextDouble(), series1, category7);
	        dataset.addValue(1 + (20 - 1) * r.nextDouble(), series1, category8);
	        dataset.addValue(1 + (20 - 1) * r.nextDouble(), series1, category9);
	        dataset.addValue(1 + (20 - 1) * r.nextDouble(), series1, category10);



	        return dataset;

	    }
	
}
