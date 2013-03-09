package com.movetothebit.newholland.android.ui;

import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
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
import com.movetothebit.newholland.android.helpers.AppHelper;
import com.movetothebit.newholland.android.model.InscriptionData;

public class FormActivity extends BaseActivity {

	private InscriptionData item;

	private Switch knownOperationSwitch;
	private Switch makeOfferSwitch;
	private Switch winOfferSwitch;
	private Button saveButton;
	private Button backButton;
	private Button forwardButton;

	private Spinner modelSpinner;
	private Spinner missingSpinner;

	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.form_layout);

		index = getIntent().getExtras().getInt("index");

		modelSpinner = (Spinner) findViewById(R.id.modelSpinner);
		ArrayAdapter<CharSequence> modelAdapter = ArrayAdapter.createFromResource(this,R.array.models,android.R.layout.simple_spinner_item);
		modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww

		modelSpinner.setAdapter(modelAdapter);
		missingSpinner = (Spinner) findViewById(R.id.missingSpinner);
		ArrayAdapter<CharSequence> missingAdapter = ArrayAdapter.createFromResource(this,R.array.missing_operation,android.R.layout.simple_spinner_item);
		missingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		missingSpinner.setAdapter(missingAdapter);
		

		knownOperationSwitch = (Switch) findViewById(R.id.knownOperationSwitch);
		makeOfferSwitch = (Switch) findViewById(R.id.makeOfferSwitch);
		winOfferSwitch = (Switch) findViewById(R.id.winOfferSwitch);

		forwardButton = (Button) findViewById(R.id.forwardButton);
		forwardButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(index<AppHelper.getMyLoadData().size()-1){
					loadInscriptionData(++index);	
				}else{
					index = 0;
					loadInscriptionData(index);
				}
				

			}
		});
		backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(index >0){
					loadInscriptionData(--index);
				}else{
					index = AppHelper.getMyLoadData().size()-1;
					loadInscriptionData(index);
				}
				

			}
		});

		knownOperationSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
					
						if (isChecked) {
							
							makeOfferSwitch.setVisibility(View.VISIBLE);
							if(makeOfferSwitch.isChecked()){
								findViewById(R.id.firstLayout).setVisibility(View.VISIBLE);
							}
						} else {
							findViewById(R.id.firstLayout).setVisibility(View.GONE);
							makeOfferSwitch.setVisibility(View.INVISIBLE);
							
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

							findViewById(R.id.secondLayout).setVisibility(View.GONE);
						} else {
							findViewById(R.id.secondLayout).setVisibility(View.VISIBLE);
						}

					}
				});
		saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new SaveDataTask().execute();

			}
		});

		loadInscriptionData(index);
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
	        
	        case R.id.forward:
	        	if(index<AppHelper.getMyLoadData().size()-1){
					loadInscriptionData(++index);	
				}else{
					index = 0;
					loadInscriptionData(index);
				}
		        return true;
	        case R.id.save:
	        	new SaveDataTask().execute();
		        return true;
	        case R.id.back:
	        	if(index >0){
					loadInscriptionData(--index);
				}else{
					index = AppHelper.getMyLoadData().size()-1;
					loadInscriptionData(index);
				}
		        return true;
		        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public String getModel(int index) {
		String[] array = getResources().getStringArray(R.array.models);
		return array[index].toString();
	}

	public String getMissingOperation(int index) {
		String[] array = getResources().getStringArray(
				R.array.missing_operation);
		return array[index].toString();
	}

	class SaveDataTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(FormActivity.this);
			pd.setMessage("Guardando datos");
			pd.show();

			if (knownOperationSwitch.isChecked()) {

				item.setKnownOperation(YES);

				if (makeOfferSwitch.isChecked()) {
					item.setMakeOffer(YES);
					if (winOfferSwitch.isChecked()) {
						item.setWinOffer(YES);
					}
					item.setFillData(YES);
					item.setModelOffer(getModel(modelSpinner
							.getSelectedItemPosition()));
					item.setWhyLose(getModel(missingSpinner
							.getSelectedItemPosition()));

					EditText nameClientText = (EditText) findViewById(R.id.nameClientEditText);
					EditText priceText = (EditText) findViewById(R.id.priceEditText);
					item.setPrice(Float.valueOf(priceText.getText().toString()));
					item.setNameClient(nameClientText.getText().toString());

				}

			} else {
				item.setKnownOperation(NO);
				EditText observationsText = (EditText) findViewById(R.id.observationsEditText);
				item.setObservations(observationsText.getText().toString());
			}

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			List<InscriptionData> list = AppHelper.getMyLoadData();
			
			list.remove(index);
			list.add(index,item);
			
			AppHelper.setMyLoadData(list);
			
		
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (pd.isShowing()) {
				pd.dismiss();
			}
			Toast.makeText(FormActivity.this, "Inscripción guardada",
					Toast.LENGTH_LONG).show();
			finish();
			super.onPostExecute(result);
		}

	}

	public void loadInscriptionData(int index) {
		List<InscriptionData> list = AppHelper.getMyLoadData();
		item = list.get(index);
		TextView id = (TextView) findViewById(R.id.idText);
		TextView date = (TextView) findViewById(R.id.dateText);
		TextView place = (TextView) findViewById(R.id.placeText);
		TextView machineType = (TextView) findViewById(R.id.machineTypeText);
		TextView brand = (TextView) findViewById(R.id.brandText);
		TextView commercialMode = (TextView) findViewById(R.id.modelText);
		TextView salesmanName = (TextView) findViewById(R.id.salesnameText);
		TextView dealerName = (TextView) findViewById(R.id.dealerText);
		
		id.setText("Inscripción:  "+item.getInscription().toString());
		machineType.setText(item.getMachineType());
		brand.setText(item.getBrand());
		place.setText(item.getProvince() + " (" + item.getPopulation() + ")");
		commercialMode.setText(item.getCommercialModel() + " (" + item.getHp()
				+ " HP)");
		dealerName.setText(item.dealerName);
		date.setText(item.getMonth() + " - " + item.getYear());
		salesmanName.setText(item.getSalesmanName());

		//tengo que hacer que cargue lso datos si los hay
		
		if(item.getKnownOperation()==1){
			knownOperationSwitch.setChecked(true);
		}else{
			knownOperationSwitch.setChecked(false);
		}
		if(item.getMakeOffer()==1){
			makeOfferSwitch.setChecked(true);
		}else{
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
		
		EditText priceText = (EditText) findViewById(R.id.priceEditText);
		priceText.setText(item.getPrice().toString());
		
		//datos del spinner
		
		if(item.getModelOffer()!=null){
			modelSpinner.setSelection(AppHelper.getIndexOfModels(getApplicationContext(), item.getModelOffer()));
		}
		if(item.getWhyLose()!=null){
			missingSpinner.setSelection(AppHelper.getIndexOfMissingOperation(getApplicationContext(), item.getWhyLose()));
		}
		
		
	}
	
}
