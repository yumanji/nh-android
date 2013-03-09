package com.movetothebit.newholland.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;


public class Base extends SherlockFragmentActivity {
	public SharedPreferences settings;
	public SharedPreferences.Editor settingsEditor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);		
		
		settings = getSharedPreferences(getString(R.string.app_preferences), 0);	        
		settingsEditor = settings.edit();	 
	     
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
