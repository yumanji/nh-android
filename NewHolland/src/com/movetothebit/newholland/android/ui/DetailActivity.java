package com.movetothebit.newholland.android.ui;

import android.os.Bundle;

import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;

public class DetailActivity extends BaseActivity{

	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        getSupportActionBar().show();	        
	        setContentView(R.layout.detail_layout);	   
	       
	     
	       
	    }
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	
	}
	
}
