package com.movetothebit.newholland.android.ui;

import java.sql.SQLException;
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
import com.movetothebit.newholland.android.helpers.DataHelper;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.utils.ServerException;


public class ListInscriptionsFilledActivity extends BaseActivity{

	private ListView list;
	private DataAdapter adapter;
	private List<InscriptionData> listData = new ArrayList<InscriptionData>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		getSupportActionBar().show();
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.list_data_layout);
		list =(ListView)findViewById(R.id.list);
		
		
		
		new LoadDataTask().execute();
	}
	
	
	class LoadDataTask extends AsyncTask<Void, Void, String>{
		ProgressDialog pd;
		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ListInscriptionsFilledActivity.this);
			pd.setMessage("Cargando datos de tu zona");
			pd.show();
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {
			
			try {
				listData = mDBHelper.getInscriptionsFilled();
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
				adapter = new DataAdapter(ListInscriptionsFilledActivity.this, listData);
				list.setAdapter(adapter);
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
