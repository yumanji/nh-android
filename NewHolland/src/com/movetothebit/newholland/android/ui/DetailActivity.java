package com.movetothebit.newholland.android.ui;

import java.sql.SQLException;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.adapters.AnswerAdapter;
import com.movetothebit.newholland.android.adapters.AnswerWinAdapter;
import com.movetothebit.newholland.android.helpers.AnswersHelper;
import com.movetothebit.newholland.android.helpers.InscriptionHelper;
import com.movetothebit.newholland.android.helpers.ModelHelper;
import com.movetothebit.newholland.android.model.AnswerItem;
import com.movetothebit.newholland.android.model.AnswerWinItem;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.utils.ServerException;

public class DetailActivity extends BaseActivity {

	private InscriptionData item;

	private Switch knownOperationSwitch;
	private Switch makeOfferSwitch;
	private Switch winOfferSwitch;
	private AutoCompleteTextView modelSpinner;
	private Spinner missingSpinner;
	private Spinner winnerSpinner;
	public List<AnswerItem> lostList;
	public List<AnswerWinItem> winList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.filled_layout);	
		
		try {
			
			item = InscriptionHelper.getInscription(getHelper(), getIntent().getExtras().getString("id"));
			lostList= AnswersHelper.getAnswers(getHelper());
			winList= AnswersHelper.getAnswersWin(getHelper());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(item.getNameClient().length()>0||item.getLastnameClient().length()>0||item.getMailClient().length()>0||item.getPhoneClient().length()>0){
			findViewById(R.id.clientLayout).setVisibility(View.VISIBLE);
		}
		modelSpinner = (AutoCompleteTextView) findViewById(R.id.modelSpinner);		
		ArrayAdapter<String> modelAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ModelHelper.getModelArray(getHelper()));	
		modelSpinner.setEnabled(false);
		modelSpinner.setAdapter(modelAdapter);
		
		missingSpinner = (Spinner) findViewById(R.id.missingSpinner);
		//	ArrayAdapter<String> missingAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,AnswersHelper.getAnswersArray(getHelper()));
			AnswerAdapter missingAdapter = new AnswerAdapter(this,lostList);
			//missingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			missingSpinner.setAdapter(missingAdapter);
			
			winnerSpinner = (Spinner) findViewById(R.id.winSpinner);
			AnswerWinAdapter winingAdapter = new AnswerWinAdapter(this,winList);
//			ArrayAdapter<String> winAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,AnswersHelper.getAnswersWinArray(getHelper()));
//			winAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			winnerSpinner.setAdapter(winingAdapter);


		knownOperationSwitch = (Switch) findViewById(R.id.knownOperationSwitch);
		makeOfferSwitch = (Switch) findViewById(R.id.makeOfferSwitch);
		winOfferSwitch = (Switch) findViewById(R.id.winOfferSwitch);
	
		
		knownOperationSwitch.setEnabled(false);
		makeOfferSwitch.setEnabled(false);
		winOfferSwitch.setEnabled(false);
		

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


	
	public void loadInscriptionData() {
			
			
			
			TextView id = (TextView) findViewById(R.id.idText);
			TextView date = (TextView) findViewById(R.id.dateText);
			TextView place = (TextView) findViewById(R.id.placeText);
			TextView machineType = (TextView) findViewById(R.id.machineTypeText);
			TextView brand = (TextView) findViewById(R.id.brandText);
			TextView commercialMode = (TextView) findViewById(R.id.modelText);
			TextView commercialModeComp = (TextView) findViewById(R.id.modelCompText);
			TextView salesmanName = (TextView) findViewById(R.id.salesnameText);
			TextView dealerName = (TextView) findViewById(R.id.dealerText);
			EditText observationsText = (EditText) findViewById(R.id.observationsEditText);
			EditText nameClientText = (EditText) findViewById(R.id.nameClientEditText);
			EditText surnameClientText = (EditText) findViewById(R.id.surnameClientEditText);
			EditText emailClientText = (EditText) findViewById(R.id.emailEditText);
			EditText phoneClientText = (EditText) findViewById(R.id.indexPresenceText);
			EditText priceText = (EditText) findViewById(R.id.priceEditText);
			
			
			observationsText.setText(item.getObservations());
			observationsText.setEnabled(false);	
			
			nameClientText.setText(item.getNameClient());
			nameClientText.setEnabled(false);	
			
			surnameClientText.setText(item.getLastnameClient());
			surnameClientText.setEnabled(false);			
			emailClientText.setText(item.getMailClient());
			emailClientText.setEnabled(false);
			
			phoneClientText.setText(item.getPhoneClient());
			phoneClientText.setEnabled(false);
		
			priceText.setText(item.getPrice().toString());
			priceText.setEnabled(false);			
			
			id.setText("Inscripción:  "+item.getInscription().toString());
			machineType.setText(item.getMachineType());
			brand.setText(item.getBrand());
			place.setText(item.getPopulation() + " (" + item.getProvince() + ")");
			commercialMode.setText(item.getCommercialModel() + " (" + item.getHp()+ " HP)");
			commercialModeComp.setText(item.getModeloComparable() );
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
			
			modelSpinner.setText(item.getModelOffer());			
			for(int i=0;i<lostList.size();i++){
				if(lostList.get(i).getId()==item.getWhyLose()){
					missingSpinner.setSelection(i);
				}
				 
			 }
			 for(int i=0;i<winList.size();i++){
					if(winList.get(i).getId()==item.getWhyWin()){
						winnerSpinner.setSelection(i);
					}
			}
			
		
		
	}
	
}
