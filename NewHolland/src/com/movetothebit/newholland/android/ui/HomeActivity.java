package com.movetothebit.newholland.android.ui;

import java.sql.SQLException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.table.TableUtils;
import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.helpers.AppHelper;
import com.movetothebit.newholland.android.helpers.DataHelper;
import com.movetothebit.newholland.android.helpers.InscriptionHelper;
import com.movetothebit.newholland.android.ui.dialogs.DownloadDialogFragment;
import com.movetothebit.newholland.android.ui.dialogs.LogoutDialogFragment;
import com.movetothebit.newholland.android.ui.dialogs.SyncDialogFragment;
import com.movetothebit.newholland.android.utils.ServerException;
import com.movetothebit.newholland.android.widgets.CategoryWidget;

public class HomeActivity extends BaseActivity {
	
	public CategoryWidget standby;
	public CategoryWidget global;
	public CategoryWidget statistics;

	public TextView lastSyncText;
	public TextView mailText;
	
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       
	        
	        setContentView(R.layout.home_layout);	   
	       
	        if(!settings.getBoolean(isSync, false)){
				showDownloadDialog(R.string.first_sync_title_dialog);
			}
	        
	        standby = (CategoryWidget)findViewById(R.id.standby);
	        global = (CategoryWidget)findViewById(R.id.global);
	        statistics = (CategoryWidget)findViewById(R.id.statistics);
	        lastSyncText = (TextView)findViewById(R.id.lastSyncText);
	        mailText = (TextView)findViewById(R.id.mailText);
	        mailText.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(Intent.ACTION_SEND);
					String[] recipients={getString(R.string.movetothebit_mail)};
					intent.putExtra(Intent.EXTRA_EMAIL, recipients);
					intent.setType("text/html");
					startActivity(Intent.createChooser(intent, "Send mail"));
					
				}
			});
	        
	        lastSyncText.setText(settings.getString(lastSync, getString(R.string.no_sync)));
	        standby.setData(R.drawable.ic_launcher, "Pendientes", R.drawable.offers);
	        global.setData(R.drawable.ic_launcher, "Listado global", R.drawable.documents);
	        statistics.setData(R.drawable.ic_launcher, "Estadisticas", R.drawable.chart);


	       standby.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), ListInscriptionsActivity.class);
					startActivity(i);

					
				}
			});
	        global.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), HistoricActivity.class);
					startActivity(i);
		
				}
			});
	        statistics.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent i = new Intent(getApplicationContext(),DataActivity.class);
					startActivity(i);
					
				}
			});

	        
	       
	    }
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			 MenuInflater inflater = getSupportMenuInflater();
			    inflater.inflate(R.menu.home_menu, menu);
			return super.onCreateOptionsMenu(menu);
		}	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	      
	        case R.id.sync:
	        	showSyncDialog(R.string.refresh_title_dialog);
		        return true;
	        case R.id.logout:
	        	showLogoutDialog();
		        return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void showLogoutDialog(){
		DialogFragment newFragment = LogoutDialogFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "logoutdialog");
		
		
	}
	
	public void showSyncDialog(int message) {
        DialogFragment newFragment = SyncDialogFragment.newInstance( message);
        newFragment.show(getSupportFragmentManager(), "syncdialog");
    
	}
	
	public void showDownloadDialog(int message) {
        DialogFragment newFragment = DownloadDialogFragment.newInstance( message);
        newFragment.show(getSupportFragmentManager(), "downloadhdialog");
    
	}
	
	public void doSyncClick() {
		if(AppHelper.isNetworkAvailable(getApplicationContext())){
			 new SyncDataTask().execute();
		 }else{
			showNetworkDialog();
		 }
	     	       
	}
	
	
	public void doLogoutClick() {
		try {
			if(InscriptionHelper.hasInscriptionHang(getHelper())){
				 new SendDataTask().execute();
			 }else{
				logOut();
			 }
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	     	       
	}
	public void logOut(){
		getHelper().dropAllTables();
		settings.edit().clear().commit();	
		Intent i = new Intent(HomeActivity.this, LoginActivity.class);
		startActivity(i);
		finish();
	}
	
	
	
	public void doDownloadClick() {
		if(AppHelper.isNetworkAvailable(getApplicationContext())){
			new DownloadDataTask().execute();	
		 }else{
			 showNetworkDialog();
		 }
	             
	}
	
	
class SendDataTask extends AsyncTask<Void, Void, String>{
		
		ProgressDialog pd;
		

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(HomeActivity.this);
			pd.setCancelable(false);
			pd.setMessage("Enviando tus datos");
			pd.show();
			
		
			
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {
			
			 try {
				
				InscriptionHelper.sendInscriptions(getApplicationContext(), getHelper());
				
				
			} catch (ServerException e) {
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
			if(result!=null){
				showAlertDialog(result);
			}else{
				logOut();
			}
			
			super.onPostExecute(result);
		}

		
		
	}
	class SyncDataTask extends AsyncTask<Void, Void, String>{
		
		ProgressDialog pd;
		

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(HomeActivity.this);
			pd.setCancelable(false);
			pd.setMessage("Sincronizando tus datos");
			pd.show();
			
		
			
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {
			
			 try {
				
				DataHelper.syncAllData(getApplicationContext(), getHelper());
				
				
			} catch (ServerException e) {
				e.printStackTrace();
				return e.getMessage();
			} catch (SQLException e) {
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
			if(result!=null){
				showAlertDialog(result);
			}else{
				refreshSyncDate();
			}
			
			super.onPostExecute(result);
		}

		
		
	}
class DownloadDataTask extends AsyncTask<Void, Void, String>{
		
		ProgressDialog pd;
		

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(HomeActivity.this);
			pd.setCancelable(false);
			pd.setMessage("Descargando tus datos");
			pd.show();
			
		
			
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {
			
			 try {
				
				DataHelper.downloadAllData(getApplicationContext(), getHelper());
				
				
			} catch (ServerException e) {
				e.printStackTrace();
				return e.getMessage();
			} catch (SQLException e) {
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
			if(result!=null){
				showAlertDialog(result);
			}else{
				refreshSyncDate();
			}
			
			super.onPostExecute(result);
		}

		
		
	}
}
