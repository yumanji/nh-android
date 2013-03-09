package com.movetothebit.newholland.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.adapters.DataAdapter;
import com.movetothebit.newholland.android.helpers.AppHelper;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.utils.RemoteFacade;


public class ListDataActivity extends BaseActivity{

	private ListView list;
	private DataAdapter adapter;
	private List<InscriptionData> listData = new ArrayList<InscriptionData>();
	
	private EditText filterText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		getSupportActionBar().show();
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.list_data_layout);
		filterText = (EditText) findViewById(R.id.filterText);
		list =(ListView)findViewById(R.id.list);
		
		
		
		new LoadDataTask().execute();
	}
	
	
	class LoadDataTask extends AsyncTask<Void, Void, Void>{
		ProgressDialog pd;
		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ListDataActivity.this);
			pd.setMessage("Cargando datos de tu zona");
			pd.show();
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			
			if(AppHelper.getMyLoadData().size()==0){
				AppHelper.setMyLoadData(RemoteFacade.getListInscriptionData(getApplicationContext()));
			}			
			listData = AppHelper.getMyLoadData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(pd.isShowing()){
				pd.dismiss();
			}
			adapter = new DataAdapter(ListDataActivity.this, listData);
			list.setAdapter(adapter);
			super.onPostExecute(result);
		}

		
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.list_data_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        
	        case R.id.search:
		        if(filterText.getVisibility() == View.GONE){
		        	filterText.setVisibility(View.VISIBLE);
		        }else{
		        	filterText.setVisibility(View.GONE);
		        }
		        return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
