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
import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.ui.dialogs.RefreshDialogFragment;
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
				showRefreshDialog(R.string.first_sync_title_dialog);
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
//					intent.putExtra(Intent.EXTRA_SUBJECT,"abc");
//					intent.putExtra(Intent.EXTRA_TEXT,"def");
//					intent.putExtra(Intent.EXTRA_CC,"ghi");
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
					Intent i = new Intent(getApplicationContext(), ListInscriptionsEmptyActivity.class);
					startActivity(i);

					
				}
			});
	        global.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), ListInscriptionsFilledActivity.class);
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
	        	showRefreshDialog(R.string.refresh_title_dialog);
		        return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	public void showRefreshDialog(int message) {
        DialogFragment newFragment = RefreshDialogFragment.newInstance( message);
        newFragment.show(getSupportFragmentManager(), "refreshdialog");
    
 }
	public void doRefreshClick() {
	      new SyncDataTask().execute();	       
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
				mDBHelper.syncAllData(getApplicationContext());
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
