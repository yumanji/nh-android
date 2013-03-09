package com.movetothebit.newholland.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.widgets.CategoryWidget;

public class HomeActivity extends BaseActivity {
	
	public CategoryWidget standby;
	public CategoryWidget global;
	public CategoryWidget statistics;

	
	
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       
	        
	        setContentView(R.layout.home_layout);	   
	       

	        
	        standby = (CategoryWidget)findViewById(R.id.standby);
	        global = (CategoryWidget)findViewById(R.id.global);
	        statistics = (CategoryWidget)findViewById(R.id.statistics);

	        
	        standby.setData(R.drawable.ic_launcher, "Pendientes", R.drawable.offers);
	        global.setData(R.drawable.ic_launcher, "Listado global", R.drawable.documents);
	        statistics.setData(R.drawable.ic_launcher, "Estadisticas", R.drawable.chart);


	       standby.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), ListDataActivity.class);
					startActivity(i);
//					Intent i = new Intent(getApplicationContext(), CatalogActivity.class);
//					startActivity(i);
					
				}
			});
	        global.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), ListDataActivity.class);
					startActivity(i);
//					IntentHelper.openIntentPdfFromSd(getApplicationContext(), "/nh_1.pdf");
					
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
	
		
}
