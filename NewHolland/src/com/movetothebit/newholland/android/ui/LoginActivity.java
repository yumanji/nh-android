package com.movetothebit.newholland.android.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.helpers.DataHelper;
import com.movetothebit.newholland.android.utils.ServerException;

public class LoginActivity extends BaseActivity {
	
	public Button accessButton;

	public EditText userText;
	public EditText passText;
	
	
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState); 
	        setContentView(R.layout.login_layout);	   
	        getSupportActionBar().hide();
	        
	        accessButton = (Button) findViewById(R.id.accessButton);
	        userText = (EditText)findViewById(R.id.userText);
	        passText = (EditText)findViewById(R.id.passText);
	       
			accessButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if(userText.getText().length()>0 && passText.getText().length()>0){
							new LoginUserTask().execute();	
						}else{
							Toast.makeText(getApplicationContext(), "Tiene que rellenar los dos campos para regsitrarse", Toast.LENGTH_SHORT).show();
						}
						
						
					}
				});
			 
	        
	       
	    }
	
		
class LoginUserTask extends AsyncTask<Void, Void, String>{
		
		ProgressDialog pd;
		

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(LoginActivity.this);
			pd.setCancelable(false);
			pd.setMessage("Realizando login..");
			pd.show();
			
		
			
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {
			
			 try {
				DataHelper.doLogin(getApplicationContext(), userText.getText().toString(), passText.getText().toString());
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
				Intent i = new Intent(getApplicationContext(), HomeActivity.class);
				startActivity(i);
			}
				
			super.onPostExecute(result);
		}

		
		
	}
	
}
