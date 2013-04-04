package com.movetothebit.newholland.android.ui;

import java.sql.SQLException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.charts.BarChartView;
import com.movetothebit.newholland.android.charts.PieChartView;
import com.movetothebit.newholland.android.charts.PresenceChartView;
import com.movetothebit.newholland.android.helpers.ChartHelper;
import com.movetothebit.newholland.android.helpers.InscriptionHelper;
import com.movetothebit.newholland.android.utils.ServerException;
import com.movetothebit.newholland.android.widgets.MultiSelectSpinner;

public class DataActivity extends ChartBaseActivity{
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.data_layout);	
        
        infoLayout = (LinearLayout)findViewById(R.id.infoLayout);
        chartButton = (Button) findViewById(R.id.chartButton);
        lostChartView  = (BarChartView)findViewById(R.id.lostChartView);
        winChartView  = (BarChartView)findViewById(R.id.winChartView);
        brandChartView  = (PieChartView)findViewById(R.id.brandChartView);
        dealerSpinner = (MultiSelectSpinner)findViewById(R.id.dealerSpinner);	     
        salesmanSpinner = (MultiSelectSpinner)findViewById(R.id.salesmanSpinner);	       
        modelSpinner = (MultiSelectSpinner)findViewById(R.id.modelSpinner);	       
        modelCompSpinner = (MultiSelectSpinner)findViewById(R.id.modelCompSpinner);
        periodSpinner = (MultiSelectSpinner)findViewById(R.id.periodSpinner);	       
        brandSpinner = (MultiSelectSpinner)findViewById(R.id.brandSpinner);	      
        populationSpinner = (MultiSelectSpinner)findViewById(R.id.populationSpinner);	       
        areaSpinner = (MultiSelectSpinner)findViewById(R.id.areaSpinner);	        
        presenceChartView  = (PresenceChartView)findViewById(R.id.presenceChartView);     
        
        resetButton = (Button) findViewById(R.id.resetButton);
		resetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetFilterValues();
				
			}
		});
        
        resetFilterValues(); 
        
        chartButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new LoadDataTask().execute();
				
			}
		});
       
    }
	
	@Override
	protected void onStart() {	
		
		new LoadDataTask().execute();
		super.onStart();
	}
	
	
	class LoadDataTask extends AsyncTask<Void, Void, String>{
		ProgressDialog pd;
		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(DataActivity.this);
			pd.setMessage("Cargando datos...");
			pd.setCancelable(false);
			pd.show();
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {			
						
			try {
				
				if(isFilterActive()){
					
					listData = InscriptionHelper.getInscriptionsFilter(getHelper(),
							salesmanSpinner.getSelectedStringsArray(),
							dealerSpinner.getSelectedStringsArray(),
							modelSpinner.getSelectedStringsArray(),
							modelCompSpinner.getSelectedStringsArray(),
							periodSpinner.getSelectedStringsArray(),
							brandSpinner.getSelectedStringsArray(),
							populationSpinner.getSelectedStringsArray(),
							areaSpinner.getSelectedStringsArray());
				}else{
					listData = InscriptionHelper.getInscriptions(getHelper());	
				}
				
				
			
			} catch (SQLException e) {			
				e.printStackTrace();
				return e.getMessage();
			
			} catch (ServerException e) {			
				e.printStackTrace();
				return e.message;
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if(pd.isShowing()){
				pd.dismiss();
			}
			if(result!=null){
				showAlertDialog(result);
			}else{
				if(listData.size()>0){
					data = ChartHelper.getInscriptionTableData(getApplicationContext(), getHelper(), listData);
					refreshData();
				}else{
					Toast.makeText(getApplicationContext(), "No hay Datos", Toast.LENGTH_SHORT).show();
				}
				
			}
			
			super.onPostExecute(result);
		}

		
	}	
	public void refreshData(){
		fillTables();
		fillCharts();
	}
	public void fillTables(){		
		fillPresenceTable(data);
		fillTotalTable(data);
		fillLostTable(data);
		fillWinTable(data);
		//fillBrandTable(data);
		
	}
	public void fillCharts(){
		
		lostChartView.paintChart(getLostDataset(),"Operaciones Perdidas");
		winChartView.paintChart(getWinDataset(),"Operaciones Ganadas");
		brandChartView.paintChart(getBrandDataset());
		presenceChartView.paintChart();

	
}
}
