package com.movetothebit.newholland.android;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;

public class BaseFragment extends SherlockFragment {
	private static final String KEY_CONTENT = "TestFragment:Content";
    private String mContent = "???";
    
    
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
	            mContent = savedInstanceState.getString(KEY_CONTENT);
	        }
	    }
	 
	 @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        outState.putString(KEY_CONTENT, mContent);
	    }
	
}
