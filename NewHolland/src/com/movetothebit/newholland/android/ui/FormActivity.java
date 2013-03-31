package com.movetothebit.newholland.android.ui;

import java.sql.SQLException;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.helpers.AnswersHelper;
import com.movetothebit.newholland.android.helpers.AppHelper;
import com.movetothebit.newholland.android.helpers.InscriptionHelper;
import com.movetothebit.newholland.android.helpers.ModelHelper;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.utils.ServerException;

public class FormActivity extends BaseActivity {

	private InscriptionData item;

	private Switch knownOperationSwitch;
	private Switch makeOfferSwitch;
	private Switch winOfferSwitch;
	


	private AutoCompleteTextView modelSpinner;
	private Spinner missingSpinner;
	private Spinner winnerSpinner;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.form_layout);

		
		try {
			item = InscriptionHelper.getInscription(getHelper(), getIntent().getExtras().getString("id"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		modelSpinner = (AutoCompleteTextView) findViewById(R.id.modelSpinner);		
		ArrayAdapter<String> modelAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ModelHelper.getModelArray(getHelper()));		
		//modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		modelSpinner.setAdapter(modelAdapter);
		
		missingSpinner = (Spinner) findViewById(R.id.missingSpinner);
		ArrayAdapter<String> missingAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,AnswersHelper.getAnswersArray(getHelper()));
		missingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		missingSpinner.setAdapter(missingAdapter);
		
		winnerSpinner = (Spinner) findViewById(R.id.winSpinner);
		ArrayAdapter<String> winAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,AnswersHelper.getAnswersWinArray(getHelper()));
		winAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		winnerSpinner.setAdapter(winAdapter);

		knownOperationSwitch = (Switch) findViewById(R.id.knownOperationSwitch);
		makeOfferSwitch = (Switch) findViewById(R.id.makeOfferSwitch);
		winOfferSwitch = (Switch) findViewById(R.id.winOfferSwitch);

		

		knownOperationSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
					
						if (isChecked) {
							
							makeOfferSwitch.setVisibility(View.VISIBLE);
							findViewById(R.id.clientLayout).setVisibility(View.VISIBLE);
							if(makeOfferSwitch.isChecked()){
								findViewById(R.id.firstLayout).setVisibility(View.VISIBLE);
							
							}
						} else {
							findViewById(R.id.firstLayout).setVisibility(View.GONE);
							
							makeOfferSwitch.setVisibility(View.INVISIBLE);
							findViewById(R.id.clientLayout).setVisibility(View.GONE);
							
						}

					}
				});
		makeOfferSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {					

						if (isChecked) {

							findViewById(R.id.firstLayout).setVisibility(View.VISIBLE);
							if(winOfferSwitch.isChecked()){
								findViewById(R.id.secondLayout).setVisibility(View.GONE);
							}else{
								findViewById(R.id.secondLayout).setVisibility(View.VISIBLE);
							}
						} else {
							findViewById(R.id.firstLayout).setVisibility(View.GONE);
						}

					}
				});
		winOfferSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							findViewById(R.id.thirdLayout).setVisibility(View.VISIBLE);
							findViewById(R.id.secondLayout).setVisibility(View.GONE);
							findViewById(R.id.priceLayout).setVisibility(View.GONE);
						} else {
							findViewById(R.id.thirdLayout).setVisibility(View.GONE);
							findViewById(R.id.secondLayout).setVisibility(View.VISIBLE);
							findViewById(R.id.priceLayout).setVisibility(View.VISIBLE);
						}

					}
				});
		
		if(item!=null){
			loadInscriptionData();
		}
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.form_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        
//	        case R.id.forward:
//	        	
//	        	if(index<(getListInscriptionsEmpty().size()-1)){
//					loadInscriptionData(++index);	
//				}else{
//					index = 0;
//					loadInscriptionData(index);
//				}
//		        return true;
	        case R.id.save:
	        	new SaveDataTask().execute();
		        return true;
//	        case R.id.back:
//	        	if(index >0){
//					loadInscriptionData(--index);
//				}else{
//					index = getListInscriptionsEmpty().size()-1;
//					loadInscriptionData( index);
//				}
//		        return true;
		        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	


	class SaveDataTask extends AsyncTask<Void, Void, String> {
		ProgressDialog pd;

		EditText nameClientText = null;
		EditText surnameClientText =  null;
		EditText emailClientText =  null;
		EditText phoneClientText = null;
		EditText priceText =  null;
		EditText observationsText =  null;
		
		@Override
		protected void onPreExecute() {
			
			pd = new ProgressDialog(FormActivity.this);
			pd.setMessage("Guardando datos");
			pd.show();

			nameClientText = (EditText) findViewById(R.id.nameClientEditText);
			surnameClientText = (EditText) findViewById(R.id.surnameClientEditText);
			emailClientText = (EditText) findViewById(R.id.emailEditText);
			phoneClientText = (EditText) findViewById(R.id.indexPresenceText);
			priceText = (EditText) findViewById(R.id.priceEditText);
			observationsText = (EditText) findViewById(R.id.observationsEditText);
			item.setObservations(observationsText.getText().toString());
			item.setFillData(YES);
			
			if (knownOperationSwitch.isChecked()) {

				item.setKnownOperation(YES);				
				
				if (makeOfferSwitch.isChecked()) {
					item.setMakeOffer(YES);
					if (winOfferSwitch.isChecked()) {
						item.setWinOffer(YES);
						item.setWhyWin(winnerSpinner.getSelectedItemPosition());
					}else{
						item.setWhyLose(missingSpinner.getSelectedItemPosition());
						item.setPrice(Float.valueOf(priceText.getText().toString()));
					}
					
					item.setModelOffer(modelSpinner.getText().toString());
					item.setNameClient(nameClientText.getText().toString());
					item.setLastnameClient(surnameClientText.getText().toString());
					item.setMailClient(emailClientText.getText().toString());
					item.setPhoneClient(phoneClientText.getText().toString());					
					

				}

			} else {
				item.setKnownOperation(NO);			
			
			}

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			
			try {
				InscriptionHelper.createOrUpdateInscription(getHelper(),item);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			}
			
		
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			
			if (pd.isShowing()) {
				pd.dismiss();
			}
			
			if(result != null){
				showAlertDialog(result);
			}else{
				Toast.makeText(FormActivity.this, "Inscripción guardada",
						Toast.LENGTH_LONG).show();
				finish();
			}
			
			super.onPostExecute(result);
		}

	}

	public void loadInscriptionData() {
		
		try {
			
			TextView id = (TextView) findViewById(R.id.idText);
			TextView date = (TextView) findViewById(R.id.dateText);
			TextView place = (TextView) findViewById(R.id.placeText);
			TextView machineType = (TextView) findViewById(R.id.machineTypeText);
			TextView brand = (TextView) findViewById(R.id.brandText);
			TextView commercialMode = (TextView) findViewById(R.id.modelText);
			TextView commercialModeComp = (TextView) findViewById(R.id.modelCompText);
			TextView salesmanName = (TextView) findViewById(R.id.salesnameText);
			TextView dealerName = (TextView) findViewById(R.id.dealerText);
			
			id.setText("Inscripción:  "+item.getInscription().toString());
			machineType.setText(item.getMachineType());
			brand.setText(item.getBrand());
			place.setText(item.getPopulation() + " (" + item.getProvince() + ")");
			commercialMode.setText(item.getCommercialModel());
			commercialModeComp.setText(item.getModeloComparable());
			dealerName.setText(item.dealerName);
			date.setText(item.getMonth() + " - " + item.getYear());
			salesmanName.setText(item.getSalesmanName());

			//tengo que hacer que cargue lso datos si los hay
			
			if(item.getKnownOperation()==1){
				knownOperationSwitch.setChecked(true);
				if(item.getMakeOffer()==1){
					makeOfferSwitch.setChecked(true);
				}else{
					makeOfferSwitch.setChecked(false);
				}
			}else{
				knownOperationSwitch.setChecked(false);
				makeOfferSwitch.setChecked(false);
			}
			
			if(item.getWinOffer()==1){
				winOfferSwitch.setChecked(true);
			}else{
				winOfferSwitch.setChecked(false);
			}
			
			EditText observationsText = (EditText) findViewById(R.id.observationsEditText);
			observationsText.setText(item.getObservations());
			
			EditText nameClientText = (EditText) findViewById(R.id.nameClientEditText);
			nameClientText.setText(item.getNameClient());
			
			EditText surnameClientText = (EditText) findViewById(R.id.surnameClientEditText);
			surnameClientText.setText(item.getLastnameClient());
			
			EditText emailClientText = (EditText) findViewById(R.id.emailEditText);
			emailClientText.setText(item.getMailClient());
			
			EditText phoneClientText = (EditText) findViewById(R.id.indexPresenceText);
			phoneClientText.setText(item.getPhoneClient());
			
			EditText priceText = (EditText) findViewById(R.id.priceEditText);
			priceText.setText(item.getPrice().toString());
			
			if(item.getModelOffer()!=null){
				modelSpinner.setText(item.getModelOffer());
			}
			if(item.getWhyLose()>0){
				 
				missingSpinner.setSelection(AnswersHelper.getAnswers(getHelper()).indexOf(item.getWhyLose()));
			}
			if(item.getWhyWin()>0){
				
				winnerSpinner.setSelection(AnswersHelper.getAnswersWin(getHelper()).indexOf(item.getWhyWin()));
				
			}
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
