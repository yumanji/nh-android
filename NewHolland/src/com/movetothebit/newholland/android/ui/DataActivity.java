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

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.helpers.FilterHelper;
import com.movetothebit.newholland.android.widgets.BarChartView;
import com.movetothebit.newholland.android.widgets.MultiSelectSpinner;
import com.movetothebit.newholland.android.widgets.PresenceChartView;

public class DataActivity extends BaseActivity{

	public BarChartView chartView;
	public PresenceChartView presenceChartView;
	public Button chartButton;
	public RelativeLayout infoLayout;
	
	public MultiSelectSpinner dealerSpinner;
	public MultiSelectSpinner salesmanSpinner;
	public MultiSelectSpinner populationSpinner;
	public MultiSelectSpinner modelSpinner;
	public MultiSelectSpinner modelCompSpinner;
	public MultiSelectSpinner brandSpinner;
	public MultiSelectSpinner areaSpinner;
	public MultiSelectSpinner periodSpinner;
	
	
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       
	        getSupportActionBar().setDisplayShowHomeEnabled(true);
	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	        setContentView(R.layout.data_layout);	
	        
	       infoLayout = (RelativeLayout)findViewById(R.id.infoLayout);
	        chartButton = (Button) findViewById(R.id.chartButton);
	        chartView  = (BarChartView)findViewById(R.id.chartView);
	        
	        dealerSpinner = (MultiSelectSpinner)findViewById(R.id.dealerSpinner);	     
	        salesmanSpinner = (MultiSelectSpinner)findViewById(R.id.salesmanSpinner);	       
	        modelSpinner = (MultiSelectSpinner)findViewById(R.id.modelSpinner);	       
	        modelCompSpinner = (MultiSelectSpinner)findViewById(R.id.modelCompSpinner);
	        periodSpinner = (MultiSelectSpinner)findViewById(R.id.periodSpinner);	       
	        brandSpinner = (MultiSelectSpinner)findViewById(R.id.brandSpinner);	      
	        populationSpinner = (MultiSelectSpinner)findViewById(R.id.populationSpinner);	       
	        areaSpinner = (MultiSelectSpinner)findViewById(R.id.areaSpinner);	        
	        presenceChartView  = (PresenceChartView)findViewById(R.id.presenceChartView);     
	   
	        
	        loadFilterValues(); 
	        
	        chartButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					refreshCharts();
					
				}
			});
	       
	    }
	
	@Override
	protected void onStart() {		
		
        chartView.paintChart(getDataset());
        presenceChartView.paintChart(getPresenceDataSet());
		super.onStart();
	}

	public void loadFilterValues(){
		
		dealerSpinner.setData(FilterHelper.getDealerValues(getApplicationContext(),getHelper()), "Concesionario");			
		salesmanSpinner.setData(FilterHelper.getSalesmanValues(getApplicationContext(),getHelper()), "Vendedor");			
		modelSpinner.setData( FilterHelper.getModelsValues(getApplicationContext(),getHelper()), "Modelo NH");			
		modelCompSpinner.setData(FilterHelper.getModelsCompValues(getApplicationContext(),getHelper()), "Modelo Comparable");			
		periodSpinner.setData(FilterHelper.getPeriodValues(getApplicationContext(),getHelper()), "Periodo");				
		brandSpinner.setData(FilterHelper.getBrandValues(getApplicationContext(),getHelper()), "Marca");				
		populationSpinner.setData(FilterHelper.getPopulationValues(getApplicationContext(),getHelper()), "Población");		
		areaSpinner.setData(FilterHelper.getAreaValues(getApplicationContext(),getHelper()), "Zona");			

	}
	
	public void refreshCharts(){
		chartView.paintChart(getDataset());
		presenceChartView.paintChart(getPresenceDataSet());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	      
	        case R.id.reset:
	        	loadFilterValues();
		        return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.list_data_menu, menu);
		return super.onCreateOptionsMenu(menu);
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
