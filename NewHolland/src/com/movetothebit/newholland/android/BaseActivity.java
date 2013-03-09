package com.movetothebit.newholland.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.movetothebit.newholland.android.utils.lConstants;


public class BaseActivity extends SherlockActivity implements lConstants{
	
	
	public SharedPreferences settings;
	public SharedPreferences.Editor settingsEditor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);		
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		settings = getSharedPreferences(getString(R.string.app_preferences), 0);	        
		settingsEditor = settings.edit();	 
	     
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.base_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	finish();
	        	return true;
	        case R.id.sync:
		        Toast.makeText(getApplicationContext(), "Realizando sincronización", Toast.LENGTH_SHORT).show();
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
	
	  /**
     * Converts an intent into a {@link Bundle} suitable for use as fragment arguments.
     */
    public static Bundle intentToFragmentArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable("_uri", data);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }

        return arguments;
    }

    /**
     * Converts a fragment arguments bundle into an intent.
     */
    public static Intent fragmentArgumentsToIntent(Bundle arguments) {
        Intent intent = new Intent();
        if (arguments == null) {
            return intent;
        }

        final Uri data = arguments.getParcelable("_uri");
        if (data != null) {
            intent.setData(data);
        }

        intent.putExtras(arguments);
        intent.removeExtra("_uri");
        return intent;
    }
  
}
