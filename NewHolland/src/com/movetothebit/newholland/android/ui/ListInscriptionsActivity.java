package com.movetothebit.newholland.android.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.adapters.FormDataAdapter;
import com.movetothebit.newholland.android.helpers.FilterHelper;
import com.movetothebit.newholland.android.helpers.InscriptionHelper;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.utils.ServerException;
import com.movetothebit.newholland.android.widgets.MultiSelectSpinner;


public class ListInscriptionsActivity extends BaseActivity{

	private ListView list;
	private FormDataAdapter adapter;
	private List<InscriptionData> listData = new ArrayList<InscriptionData>();
	private MultiSelectSpinner dealerSpinner;
	private MultiSelectSpinner salesmanSpinner;
	private Button filterButton;
	private Button resetButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.list_data_layout);
		
		list =(ListView)findViewById(R.id.list);
		dealerSpinner = (MultiSelectSpinner) findViewById(R.id.dealerSpinner);
		salesmanSpinner = (MultiSelectSpinner) findViewById(R.id.salesmanSpinner);
		filterButton = (Button) findViewById(R.id.filterButton);
		resetButton = (Button) findViewById(R.id.resetButton);
		resetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetFilters();
				
			}
		});
		filterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new LoadDataTask().execute();
				
			}
		});
				
		resetFilters();
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		new LoadDataTask().execute();
	
	
	}
	

	
	public void resetFilters(){
		dealerSpinner.setData(FilterHelper.getDealerValues(getApplicationContext(),getHelper()), "Concesionario");		
		salesmanSpinner.setData(FilterHelper.getSalesmanValues(getApplicationContext(),getHelper()), "Vendedor");
		
	}
	class LoadDataTask extends AsyncTask<Void, Void, String>{
		ProgressDialog pd;
		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ListInscriptionsActivity.this);
			pd.setMessage("Cargando datos de tu zona");
			pd.setCancelable(false);
			pd.show();
			
			
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {
			
			try {
				if(salesmanSpinner.getSelectedStrings().size()>0||dealerSpinner.getSelectedStrings().size()>0){
					listData = InscriptionHelper.getInscriptionsFilter(getHelper(),salesmanSpinner.getSelectedStringsArray(), dealerSpinner.getSelectedStringsArray());
				}else{
					listData = InscriptionHelper.getInscriptionsEmpty(getHelper());	
				}
				
				
			} catch (SQLException e) {
				
				e.printStackTrace();
				return e.getMessage();
			} catch (ServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if(pd.isShowing()){
				pd.dismiss();
			}
			if(result == null){
				if(listData.size()>0){
					
					adapter = new FormDataAdapter(ListInscriptionsActivity.this, listData);						
					list.setAdapter(adapter);
					list.setVisibility(View.VISIBLE);
					findViewById(R.id.noDataLayout).setVisibility(View.GONE);
					
				}else{
					
					list.setVisibility(View.GONE);
					findViewById(R.id.noDataLayout).setVisibility(View.VISIBLE);
				}
				
			}else{
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
			}
			
			super.onPostExecute(result);
		}

		
		
	}


	


}
