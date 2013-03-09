package com.movetothebit.newholland.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;

public class LoginActivity extends BaseActivity {
	
private Button accessButton;

	
	
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState); 
	        setContentView(R.layout.login_layout);	   
	        getSupportActionBar().hide();
	        
	        accessButton = (Button) findViewById(R.id.accessButton);
	        
	       
			accessButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						settingsEditor.putBoolean("islogged", true).commit();
						
						Intent mainIntent = new Intent(LoginActivity.this,HomeActivity.class);
						startActivity(mainIntent);
						finish();
						
					}
				});
			 
	        
	       
	    }
	
		
}
