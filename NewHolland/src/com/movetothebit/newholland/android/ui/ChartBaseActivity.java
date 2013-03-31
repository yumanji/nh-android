package com.movetothebit.newholland.android.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.helpers.AnswersHelper;
import com.movetothebit.newholland.android.helpers.ChartHelper;
import com.movetothebit.newholland.android.helpers.FilterHelper;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.model.InscriptionTableData;
import com.movetothebit.newholland.android.widgets.BarChartView;
import com.movetothebit.newholland.android.widgets.MultiSelectSpinner;
import com.movetothebit.newholland.android.widgets.PresenceChartView;

public class ChartBaseActivity extends BaseActivity{
	public BarChartView chartView;
	public PresenceChartView presenceChartView;
	public Button chartButton;
	public Button resetButton;
	public RelativeLayout infoLayout;
	public InscriptionTableData data;
	public MultiSelectSpinner dealerSpinner;
	public MultiSelectSpinner salesmanSpinner;
	public MultiSelectSpinner populationSpinner;
	public MultiSelectSpinner modelSpinner;
	public MultiSelectSpinner modelCompSpinner;
	public MultiSelectSpinner brandSpinner;
	public MultiSelectSpinner areaSpinner;
	public MultiSelectSpinner periodSpinner;
	
	public List<InscriptionData> listData = new ArrayList<InscriptionData>();
	

	
	
	
	public boolean isFilterActive(){
		
		if(salesmanSpinner.getSelectedStrings().size()>0||
			dealerSpinner.getSelectedStrings().size()>0||
			modelSpinner.getSelectedStrings().size()>0||
			modelCompSpinner.getSelectedStrings().size()>0||
			periodSpinner.getSelectedStrings().size()>0||
			brandSpinner.getSelectedStrings().size()>0||
			populationSpinner.getSelectedStrings().size()>0||
			areaSpinner.getSelectedStrings().size()>0){
			
			return true;
		}else{
			return false;
		}
		
		
	}
	
	public void resetFilterValues(){
		
		dealerSpinner.setData(FilterHelper.getDealerValues(getApplicationContext(),getHelper()), "Concesionario");			
		salesmanSpinner.setData(FilterHelper.getSalesmanValues(getApplicationContext(),getHelper()), "Vendedor");			
		modelSpinner.setData( FilterHelper.getModelsValues(getApplicationContext(),getHelper()), "Modelo NH");			
		modelCompSpinner.setData(FilterHelper.getModelsCompValues(getApplicationContext(),getHelper()), "Modelo Comparable");			
		periodSpinner.setData(FilterHelper.getPeriodValues(getApplicationContext(),getHelper()), "Periodo");				
		brandSpinner.setData(FilterHelper.getBrandValues(getApplicationContext(),getHelper()), "Marca");				
		populationSpinner.setData(FilterHelper.getPopulationValues(getApplicationContext(),getHelper()), "Población");		
		areaSpinner.setData(FilterHelper.getAreaValues(getApplicationContext(),getHelper()), "Zona");			

		
	}
	
	
	
	
	
	public void fillTotalTable(InscriptionTableData data){
		
		TextView mTotalText = (TextView)findViewById(R.id.mTotalText);
		TextView knownTotalText = (TextView)findViewById(R.id.knownTotalText);
		TextView ofertTotalText = (TextView)findViewById(R.id.ofertTotalText);
		TextView winTotalText = (TextView)findViewById(R.id.winTotalText);
		
		mTotalText.setText(String.valueOf(data.getMarketTotal()));
		knownTotalText.setText(String.valueOf(data.getKnownTotal()));
		ofertTotalText.setText(String.valueOf(data.getOfertTotal()));
		winTotalText.setText(String.valueOf(data.getWinTotal()));
	}
	
	public void fillLostTable(InscriptionTableData data){
		
		TableLayout lostTable = (TableLayout)findViewById(R.id.missingTable);
		String[] answers = AnswersHelper.getAnswersArray(getHelper());
		int[] values = data.getLostData();
		
		for(int i = 0; i< answers.length; i++){
			
			TableRow row = (TableRow) View.inflate(ChartBaseActivity.this, R.layout.lost_row_layout, null);
			row.setTag(i);
			
			TextView name = (TextView)row.findViewById(R.id.name);			
			TextView count = (TextView)row.findViewById(R.id.count);
			TextView index = (TextView)row.findViewById(R.id.index);
			
			name.setText(String.valueOf(answers[i]));
			count.setText(String.valueOf(values[i]));
			
			index.setText(String.valueOf((float) Math.round(values[i] * 100.0/data.marketTotal)));
			
			lostTable.addView(row);
		
		}
		
	}
	
	public void fillWinTable(InscriptionTableData data){
		
		TableLayout lostTable = (TableLayout)findViewById(R.id.winTable);
		String[] answers = AnswersHelper.getAnswersWinArray(getHelper());
		int[] values = data.getWinData();
		
		for(int i = 0; i< answers.length; i++){
			
			TableRow row = (TableRow) View.inflate(ChartBaseActivity.this, R.layout.lost_row_layout, null);
			row.setTag(i);
			
			TextView name = (TextView)row.findViewById(R.id.name);			
			TextView count = (TextView)row.findViewById(R.id.count);
			TextView index = (TextView)row.findViewById(R.id.index);
			
			name.setText(String.valueOf(answers[i]));
			count.setText(String.valueOf(values[i]));			
		
			index.setText(String.valueOf((float) Math.round(values[i] * 100.0/data.marketTotal)));
			
			lostTable.addView(row);
		
		}
		
	}
	public void fillBrandTable(InscriptionTableData data){
		
		TableLayout lostTable = (TableLayout)findViewById(R.id.brandTable);
		String[] brands = FilterHelper.getBrandValues(getApplicationContext(), getHelper());
		int[] values = data.getBrandData();
		
		for(int i = 0; i< brands.length; i++){
			
			TableRow row = (TableRow) View.inflate(ChartBaseActivity.this, R.layout.brand_row_layout, null);
			row.setTag(i);
			
			TextView name = (TextView)row.findViewById(R.id.name);			
			TextView count = (TextView)row.findViewById(R.id.count);
			
			
			name.setText(String.valueOf(brands[i]));
			count.setText(String.valueOf(values[i]));			
			lostTable.addView(row);
		
		}
		
	}
	protected CategoryDataset getPresenceDataSet() {
    // row keys...
    String series1 = "Indice %";
    String series2 = "Objetivo NH";
//    String series3 = "Third";

    // column keys...
    String category1 = "Conocimientos de mercado";
    String category2 = "Presencia";
    String category3 = "Efectividad";
    String category4 = "Presencia total";
    String category5 = "Cuota de mercado";

    // create the dataset...
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
    Random r = new Random();
    
    dataset.addValue(1 + (100 - 1) * r.nextDouble(), series1, category1);
    dataset.addValue(1 + (100 - 1) * r.nextDouble(), series1, category2);
    dataset.addValue(1 + (100 - 1) * r.nextDouble(), series1, category3);
    dataset.addValue(1 + (100 - 1) * r.nextDouble(), series1, category4);
    dataset.addValue(1 + (100 - 1) * r.nextDouble(), series1, category5);

    dataset.addValue(1 + (100 - 1) * r.nextDouble(), series2, category1);
    dataset.addValue(1 + (100 - 1) * r.nextDouble(), series2, category2);
    dataset.addValue(1 + (100 - 1) * r.nextDouble(), series2, category3);
    dataset.addValue(1 + (100 - 1) * r.nextDouble(), series2, category4);
    dataset.addValue(1 + (100 - 1) * r.nextDouble(), series2, category5);


    return dataset;

}
protected CategoryDataset getDataset() {

    // row keys...
    String series1 = "Motivo";
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    String[] label =AnswersHelper.getAnswersArray(getHelper());
    int[] values = data.getLostData();
    // column keys...
    for(int i = 0;i<label.length;i++){
    	
    	 dataset.addValue(values[i], series1, label[i]);
    }

    return dataset;

}
}
