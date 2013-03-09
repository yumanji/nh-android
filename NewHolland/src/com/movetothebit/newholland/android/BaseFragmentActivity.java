package com.movetothebit.newholland.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.movetothebit.newholland.android.helpers.AppHelper;
import com.movetothebit.newholland.android.location.GPSCallback;
import com.movetothebit.newholland.android.location.GPSManager;

public class BaseFragmentActivity extends SherlockFragmentActivity implements GPSCallback{
	
	public SharedPreferences settings;
	public SharedPreferences.Editor settingsEditor;
	
	private final int GPS_ENABLED = 1;
	private final int NETWORK_ENABLED = 2;
	private final int POSITION_FAIL = 4;
	private GPSManager gpsManager;
	
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);		
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		
		settings = getSharedPreferences(getString(R.string.app_preferences), 0);	        
		settingsEditor = settings.edit();	 
	     
		gpsManager = new GPSManager();
	    gpsManager.setGPSCallback( this );
	}
	
	@Override
	protected void onStart() {
		
		super.onStart();
		if(AppHelper.isNetworkAvailable(BaseFragmentActivity.this)==false){
			showDialog(NETWORK_ENABLED);
		}
        
		if ( !gpsManager.isGPSenabled( BaseFragmentActivity.this ) ) {
			showDialog(GPS_ENABLED);			
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case android.R.id.home:
		    	finish();
		    	return true;
	    	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	protected void showGpsOptions(){  
	       Intent gpsOptionsIntent = new Intent(  
	               android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
	       startActivity(gpsOptionsIntent);  
	} 
	
	protected void showNetworkOptions(){  
	       Intent networkOptionsIntent = new Intent(  
	               android.provider.Settings.ACTION_WIRELESS_SETTINGS);  
	       startActivity(networkOptionsIntent);  
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case GPS_ENABLED:
			return new AlertDialog.Builder(BaseFragmentActivity.this)  
			   .setMessage(getString(R.string.gps_disable))  
		        .setCancelable(false)  
		        .setPositiveButton(getString(R.string.enableGps),  
		             new DialogInterface.OnClickListener(){  
		             public void onClick(DialogInterface dialog, int id){  
		                  showGpsOptions();  
		             }  
		        })  
		        .setNegativeButton(getString(R.string.doNothing),  
		             new DialogInterface.OnClickListener(){  
		             public void onClick(DialogInterface dialog, int id){  
		                dismissDialog(GPS_ENABLED);
		                System.runFinalizersOnExit(true);
		          		System.exit(0);            
		                 
		             }  
		        })
		        .create();  
		case NETWORK_ENABLED:
			return new AlertDialog.Builder(BaseFragmentActivity.this)  
			   .setMessage(getString(R.string.network_disable))  
		        .setCancelable(false)  
		        .setPositiveButton(getString(R.string.enableNetwork),  
		             new DialogInterface.OnClickListener(){  
		             public void onClick(DialogInterface dialog, int id){  
		                  showNetworkOptions();  
		             }  
		        })  
		        .setNegativeButton(getString(R.string.exit_app),  
		             new DialogInterface.OnClickListener(){  
		             public void onClick(DialogInterface dialog, int id){  
		            	 dismissDialog(NETWORK_ENABLED);
		            	 System.runFinalizersOnExit(true);
		         		 System.exit(0);
		            	 
		             }  
		        })
		        .create(); 
		case POSITION_FAIL:
			return new AlertDialog.Builder(BaseFragmentActivity.this)  
			.setTitle(getString(R.string.location_need_title))
			   .setMessage(getString(R.string.location_need_msg))  
		         
		        .setPositiveButton(getString(R.string.wait_location),  
		             new DialogInterface.OnClickListener(){  
		             public void onClick(DialogInterface dialog, int id){
		            	 
		            	 
		            	 
		                  pd = new ProgressDialog(BaseFragmentActivity.this);
		                  pd.setMessage(getString(R.string.obtain_location));
		                  pd.setButton(getString(R.string.exit_app), new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub								
								pd.dismiss();
								System.runFinalizersOnExit(true);
				         		System.exit(0);
								
							}
		                  });
		                  
		                  pd.show();
		                  
		                  gpsManager.startListening(BaseFragmentActivity.this);
		             }  
		        })  
		        .setNegativeButton(getString(R.string.exit_app),  
		             new DialogInterface.OnClickListener(){  
		             public void onClick(DialogInterface dialog, int id){  
		            	 dismissDialog(POSITION_FAIL);
		            	 System.runFinalizersOnExit(true);
		         		 System.exit(0);
		            	 
		             }  
		        })
		        .create(); 
		
		}  

		return null;
	}
	protected static String makeFragmentName(int viewId, int index) {
	     return "android:switcher:" + viewId + ":" + index;
	}
	@Override
	public void onGPSUpdate(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGPSTimeout() {
		// TODO Auto-generated method stub
		
	}
	
}
