package com.movetothebit.newholland.android;

import java.sql.SQLException;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.movetothebit.newholland.android.db.DBHelper;
import com.movetothebit.newholland.android.helpers.AnswersHelper;
import com.movetothebit.newholland.android.helpers.ModelHelper;
import com.movetothebit.newholland.android.model.AnswerItem;
import com.movetothebit.newholland.android.model.AnswerWinItem;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.model.ModelItem;
import com.movetothebit.newholland.android.ui.dialogs.MyAlertDialogFragment;
import com.movetothebit.newholland.android.ui.dialogs.NetworkDialogFragment;
import com.movetothebit.newholland.android.utils.ServerException;
import com.movetothebit.newholland.android.utils.UIUtils;
import com.movetothebit.newholland.android.utils.lConstants;


public class BaseActivity extends SherlockFragmentActivity implements lConstants{
	
	public static final String TAG = "BaseActivity";
	public SharedPreferences settings;
	public SharedPreferences.Editor settingsEditor;

	public DBHelper mDBHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);		
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		settings = getSharedPreferences(getString(R.string.app_preferences), 0);	        
		settingsEditor = settings.edit();
		mDBHelper = getHelper();
		
	     
	} 
   
 
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
    }
	 
	public void showAlertDialog(String title) {
	        DialogFragment newFragment = MyAlertDialogFragment.newInstance(
	                R.string.refresh_exception);
	        newFragment.show(getSupportFragmentManager(), "alertdialog");
	 }

	public void showNetworkDialog() {
        DialogFragment newFragment = NetworkDialogFragment.newInstance( );
        newFragment.show(getSupportFragmentManager(), "networkdialog");
 }

	 public DBHelper getHelper() {
	        if (mDBHelper == null) {
	            mDBHelper = OpenHelperManager.getHelper(this, DBHelper.class);
	        }
	        return mDBHelper;
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
	


	public void refreshSyncDate(){
		String date = UIUtils.getDateNowFormat();
		
		settingsEditor.putBoolean(isSync, true).commit();		
		settingsEditor.putString(lastSync,date ).commit();
		
		TextView lastSync = (TextView) findViewById(R.id.lastSyncText);
		lastSync.setText(date);
	}
	
	
	protected void showGpsOptions(){  
	       Intent gpsOptionsIntent = new Intent(  
	               android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
	       startActivity(gpsOptionsIntent);  
	} 
	
	public void showNetworkOptions(){  
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
