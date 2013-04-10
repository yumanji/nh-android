package com.movetothebit.newholland.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.charts.BarChartView;
import com.movetothebit.newholland.android.charts.OverlayChartView;
import com.movetothebit.newholland.android.charts.PenetrationChartView;
import com.movetothebit.newholland.android.charts.model.FilterDataSet;
import com.movetothebit.newholland.android.charts.model.MonthDataSet;
import com.movetothebit.newholland.android.helpers.AnswersHelper;
import com.movetothebit.newholland.android.helpers.FilterHelper;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.model.ObjetiveItem;
import com.movetothebit.newholland.android.widgets.MultiSelectSpinner;

public class ChartBaseActivity extends BaseActivity{
	//public BarChartView winChartView;
	public BarChartView lostChartView;
	//public PieChartView brandChartView;
	public OverlayChartView presenceChartView;
	public OverlayChartView marketChartView;
	public OverlayChartView effectivenessChartView;
	public PenetrationChartView penetrationChartView;
	public Button chartButton;
	public Button resetButton;
	public MonthDataSet[] monthDataSet;
	public FilterDataSet filterDataSet;
	public LinearLayout infoLayout;
//	public InscriptionTableData data;
	public MultiSelectSpinner typeSpinner;
	public MultiSelectSpinner dealerSpinner;
	public MultiSelectSpinner salesmanSpinner;
	public MultiSelectSpinner populationSpinner;
	public MultiSelectSpinner modelSpinner;
	public MultiSelectSpinner modelCompSpinner;
	public MultiSelectSpinner brandSpinner;
	public MultiSelectSpinner areaSpinner;
	public MultiSelectSpinner periodSpinner;
	
	public List<InscriptionData> listData = new ArrayList<InscriptionData>();
	
	public List<ObjetiveItem >objetives = new ArrayList<ObjetiveItem>();
	
	
	
	public boolean isFilterActive(){
		
		if(salesmanSpinner.getSelectedStrings().size()>0||
			dealerSpinner.getSelectedStrings().size()>0||
			modelSpinner.getSelectedStrings().size()>0||
			modelCompSpinner.getSelectedStrings().size()>0||
			populationSpinner.getSelectedStrings().size()>0||
			areaSpinner.getSelectedStrings().size()>0||
			typeSpinner.getSelectedStrings().size()>0){
			
			return true;
		}else{
			return false;
		}
		
		
	}
	
	public void resetFilterValues(){
		
		dealerSpinner.setData(FilterHelper.getDealerValues(getApplicationContext(),getHelper()), "Concesionario");			
		salesmanSpinner.setData(FilterHelper.getSalesmanValues(getApplicationContext(),getHelper()), "Vendedor");			
		modelSpinner.setData( FilterHelper.getModel3Values(getApplicationContext(),getHelper()), "Comp NH");			
		modelCompSpinner.setData(FilterHelper.getModelsCompValues(getApplicationContext(),getHelper()), "Modelo Comparable");			
		periodSpinner.setData(FilterHelper.getPeriodValues(getApplicationContext(),getHelper()), "Periodo");				
		brandSpinner.setData(FilterHelper.getBrandValues(getApplicationContext(),getHelper()), "Marca");				
		populationSpinner.setData(FilterHelper.getPopulationValues(getApplicationContext(),getHelper()), "Población");		
		areaSpinner.setData(FilterHelper.getAreaValues(getApplicationContext(),getHelper()), "Zona");			
		typeSpinner.setData(FilterHelper.getTypeValues(getApplicationContext(),getHelper()), "Tipo");
		
	}
	
	
	
	
	
	public void fillTotalTable(FilterDataSet data){
		
		TextView mTotalText = (TextView)findViewById(R.id.mTotalText);
		TextView knownTotalText = (TextView)findViewById(R.id.knownTotalText);
		TextView ofertTotalText = (TextView)findViewById(R.id.ofertTotalText);
		TextView winTotalText = (TextView)findViewById(R.id.winTotalText);
		
		mTotalText.setText(String.valueOf((int)data.total));
		knownTotalText.setText(String.valueOf((int)data.known));
		ofertTotalText.setText(String.valueOf((int)data.offert));
		winTotalText.setText(String.valueOf((int) data.win));
	}
	public void fillPresenceTable(FilterDataSet data){
		
		TextView indexKnownText = (TextView)findViewById(R.id.indexKnownText);
		TextView indexPresenceText = (TextView)findViewById(R.id.indexPresenceText);		
		TextView indexEffectText = (TextView)findViewById(R.id.indexEffectText);
		TextView indexMarketText = (TextView)findViewById(R.id.indexMarketText);
		TextView objetiveKnownText = (TextView)findViewById(R.id.objetiveKnownText);
		TextView objetivePresenceText = (TextView)findViewById(R.id.objetivePresenceText);		
		TextView objetiveEffectText = (TextView)findViewById(R.id.objetiveEffectText);
		TextView objetiveMarketText = (TextView)findViewById(R.id.objetiveMarketText);
	
		
		
			indexKnownText.setText(String.valueOf( (int) Math.round(100 * data.known/data.total) +" %"));
			indexPresenceText.setText(String.valueOf((int) Math.round(100 *  data.offert/ data.known) +" %"));		
			indexEffectText.setText(String.valueOf( (int) Math.round(100 * data.win/data.offert) +" %"));
			indexMarketText.setText(String.valueOf((int) Math.round(100 * data.win/data.total) +" %"));
			objetiveKnownText.setText(String.valueOf(objetives.get(0).getValue()));
			objetivePresenceText.setText(String.valueOf(objetives.get(1).getValue()));		
			objetiveEffectText.setText(String.valueOf(objetives.get(2).getValue() ));
			objetiveMarketText.setText(String.valueOf(objetives.get(3).getValue()));
			
		
		
		
	}
	public void fillLostTable(FilterDataSet data){
		
		TableLayout lostTable = (TableLayout)findViewById(R.id.missingTable);
		String[] answers = AnswersHelper.getAnswersArray(getHelper());
		float[] values = data.lostData;
		
		if(lostTable.getChildCount()<=2){
			for(int i = 0; i< answers.length; i++){
				TableRow row = (TableRow) View.inflate(ChartBaseActivity.this, R.layout.lost_row_layout, null);
				row.setTag(i);
				lostTable.addView(row);
			
			}
		}
			for(int i = 0; i< answers.length; i++){
				
				TableRow row =  (TableRow) lostTable.getChildAt(i+2);		
					
				TextView name = (TextView)row.findViewById(R.id.name);			
				TextView count = (TextView)row.findViewById(R.id.count);
				TextView index = (TextView)row.findViewById(R.id.index);
				
				name.setText(String.valueOf(answers[i]));
				count.setText(String.valueOf((int) values[i]));				
				index.setText(String.valueOf((int) Math.round(values[i] * 100.0/(data.lost)))+ " %");
				
				
			
			}
		
	}


}
