package com.movetothebit.newholland.android.ui;

import java.sql.SQLException;

import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.charts.BarChartView;
import com.movetothebit.newholland.android.charts.OverlayChartView;
import com.movetothebit.newholland.android.charts.PenetrationChartView;
import com.movetothebit.newholland.android.helpers.AnswersHelper;
import com.movetothebit.newholland.android.helpers.ChartHelper;
import com.movetothebit.newholland.android.helpers.FilterHelper;
import com.movetothebit.newholland.android.helpers.InscriptionHelper;
import com.movetothebit.newholland.android.utils.ServerException;
import com.movetothebit.newholland.android.widgets.MultiSelectSpinner;

public class DataActivity extends ChartBaseActivity implements OnTouchListener{
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.data_layout);	
        
       
        infoLayout = (LinearLayout)findViewById(R.id.infoLayout);
        chartButton = (Button) findViewById(R.id.chartButton);
        lostChartView  = (BarChartView)findViewById(R.id.lostChartView);
        dealerSpinner = (MultiSelectSpinner)findViewById(R.id.dealerSpinner);	   
        typeSpinner = (MultiSelectSpinner)findViewById(R.id.typeSpinner);	     
        salesmanSpinner = (MultiSelectSpinner)findViewById(R.id.salesmanSpinner);	       
        modelSpinner = (MultiSelectSpinner)findViewById(R.id.modelSpinner);	       
        modelCompSpinner = (MultiSelectSpinner)findViewById(R.id.modelCompSpinner);
        periodSpinner = (MultiSelectSpinner)findViewById(R.id.periodSpinner);	       
        brandSpinner = (MultiSelectSpinner)findViewById(R.id.brandSpinner);	      
        populationSpinner = (MultiSelectSpinner)findViewById(R.id.populationSpinner);	       
        areaSpinner = (MultiSelectSpinner)findViewById(R.id.areaSpinner);	        
        presenceChartView  = (OverlayChartView)findViewById(R.id.presenceChartView);     
        penetrationChartView  = (PenetrationChartView)findViewById(R.id.penetrationChartView);
        marketChartView  = (OverlayChartView)findViewById(R.id.marketChartView);
        effectivenessChartView  = (OverlayChartView)findViewById(R.id.effectivenessChartView);
        presenceChartView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        penetrationChartView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        effectivenessChartView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        marketChartView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        presenceChartView.setOnTouchListener(this);
        lostChartView.setOnTouchListener(this);
        penetrationChartView.setOnTouchListener(this);
        effectivenessChartView.setOnTouchListener(this);
        marketChartView.setOnTouchListener(this);
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
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
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
							populationSpinner.getSelectedStringsArray(),
							areaSpinner.getSelectedStringsArray(),
							typeSpinner.getSelectedStringsArray());
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
					monthDataSet = ChartHelper.getMonthDataSet(listData, FilterHelper.getBrandValues(getApplicationContext(), getHelper()));
					filterDataSet = ChartHelper.getFilterDataSet(listData,
							AnswersHelper.getAnswersArray(getHelper()).length,
							brandSpinner.getSelectedStringsArray(),
							periodSpinner.getSelectedStringsArray());
					
					refreshData();
				}else{
					Toast.makeText(getApplicationContext(), "No hay Datos", Toast.LENGTH_SHORT).show();
				}
				
			}
			
			super.onPostExecute(result);
		}

		
	}	
	public void refreshData(){
		 try {
			objetives = getHelper().getObjetivesDao().queryForAll();
			fillTables();
			fillCharts();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void fillTables(){		
		fillPresenceTable(filterDataSet);
		fillTotalTable(filterDataSet);
		fillLostTable(filterDataSet);

		
	}
	public CategoryDataset getLostDataset(float[] values) {
		String series1 = "Motivo";
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    String[] label =AnswersHelper.getAnswersArray(getHelper());
	   
	   
	    for(int i = 0;i<label.length;i++){
	    	
	    	 dataset.addValue(values[i], series1, label[i]);
	    }
        return dataset;

    }

	public void fillCharts(){
		
		lostChartView.paintChart(getLostDataset(filterDataSet.lostData),"Operaciones Perdidas");
		presenceChartView.paintChart(monthDataSet,PRESENCE,Float.valueOf(objetives.get(1).getValue())/100);
		penetrationChartView.paintChart(monthDataSet, ChartHelper.getBrandData(getApplicationContext(), getHelper(), listData));
		effectivenessChartView.paintChart(monthDataSet,EFECTIVITY,Float.valueOf(objetives.get(2).getValue())/100);
		marketChartView.paintChart(monthDataSet,MARKET,Float.valueOf(objetives.get(3).getValue())/100);

	
}

	
}
