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

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.adapters.FormDataAdapter;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.utils.ServerException;
import com.movetothebit.newholland.android.widgets.MultiSelectSpinner;


public class ListInscriptionsEmptyActivity extends BaseActivity{

	private ListView list;
	private FormDataAdapter adapter;
	private List<InscriptionData> listData = new ArrayList<InscriptionData>();
	private MultiSelectSpinner modelSpinner;
	private Button filterButton;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		getSupportActionBar().show();
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.list_data_layout);
		
		modelSpinner = (MultiSelectSpinner) findViewById(R.id.modelSpinner);
		filterButton = (Button) findViewById(R.id.filterButton);
		filterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new LoadDataTask().execute();
				
			}
		});
		modelSpinner.setItems(getModelArray());
		modelSpinner.setPrompt("Modelos");
		modelSpinner.setSelection(0);
		
		
		list =(ListView)findViewById(R.id.list);
		
		
		
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		new LoadDataTask().execute();
	
	
	}
	class LoadDataTask extends AsyncTask<Void, Void, String>{
		ProgressDialog pd;
		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ListInscriptionsEmptyActivity.this);
			pd.setMessage("Cargando datos de tu zona");
			pd.show();
			
			
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {
			
			try {
				if(modelSpinner.getSelectedStrings().size()>0){
					listData = mDBHelper.getInscriptionsFilter(modelSpinner.getSelectedStringsArray());
				}else{
					listData = mDBHelper.getInscriptionsEmpty();	
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
					
					adapter = new FormDataAdapter(ListInscriptionsEmptyActivity.this, listData);						
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

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.list_data_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	


}
