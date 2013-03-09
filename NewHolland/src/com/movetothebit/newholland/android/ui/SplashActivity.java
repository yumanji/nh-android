package com.movetothebit.newholland.android.ui;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;

public class SplashActivity extends BaseActivity {
    /** Called when the activity is first created. */


	private final int DISPLAY_LENGTH = 1000;	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);  
        getSupportActionBar().hide();
        setContentView(R.layout.splash_layout); 
       
    }

    @Override
	protected void onStart() {
		super.onStart();
		
	
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				if(settings.getBoolean("islogged", false)){
					Intent mainIntent = new Intent(SplashActivity.this,HomeActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}else{
					Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}
				
			}
			
		}, DISPLAY_LENGTH);
//	
    }	
    
}