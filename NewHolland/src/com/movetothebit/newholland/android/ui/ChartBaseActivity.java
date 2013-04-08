package com.movetothebit.newholland.android.ui;

import java.util.ArrayList;
import java.util.List;

import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;
import org.afree.data.general.DefaultPieDataset;
import org.afree.data.general.PieDataset;

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
import com.movetothebit.newholland.android.charts.model.ChartDataSet;
import com.movetothebit.newholland.android.helpers.AnswersHelper;
import com.movetothebit.newholland.android.helpers.FilterHelper;
import com.movetothebit.newholland.android.model.Brand;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.model.InscriptionTableData;
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
	public ChartDataSet dataSet;
	public LinearLayout infoLayout;
//	public InscriptionTableData data;
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
		modelSpinner.setData( FilterHelper.getModel3Values(getApplicationContext(),getHelper()), "Comp NH");			
		modelCompSpinner.setData(FilterHelper.getModelsCompValues(getApplicationContext(),getHelper()), "Modelo Comparable");			
		periodSpinner.setData(FilterHelper.getPeriodValues(getApplicationContext(),getHelper()), "Periodo");				
		brandSpinner.setData(FilterHelper.getBrandValues(getApplicationContext(),getHelper()), "Marca");				
		populationSpinner.setData(FilterHelper.getPopulationValues(getApplicationContext(),getHelper()), "Población");		
		areaSpinner.setData(FilterHelper.getAreaValues(getApplicationContext(),getHelper()), "Zona");			

		
	}
	
	
	
	
	
	public void fillTotalTable(ChartDataSet data){
		
		TextView mTotalText = (TextView)findViewById(R.id.mTotalText);
		TextView knownTotalText = (TextView)findViewById(R.id.knownTotalText);
		TextView ofertTotalText = (TextView)findViewById(R.id.ofertTotalText);
		TextView winTotalText = (TextView)findViewById(R.id.winTotalText);
		
		mTotalText.setText(String.valueOf((int)data.total));
		knownTotalText.setText(String.valueOf((int)data.known));
		ofertTotalText.setText(String.valueOf((int)data.offert));
		winTotalText.setText(String.valueOf((int) data.win));
	}
	public void fillPresenceTable(ChartDataSet data){
		
		TextView indexKnownText = (TextView)findViewById(R.id.indexKnownText);
		TextView indexPresenceText = (TextView)findViewById(R.id.indexPresenceText);
		//TextView indexPresenceTotalText = (TextView)findViewById(R.id.indexPresenceTotalText);
		TextView indexEffectText = (TextView)findViewById(R.id.indexEffectText);
		TextView indexMarketText = (TextView)findViewById(R.id.indexMarketText);
		indexKnownText.setText(String.valueOf( (int) Math.round(100 * data.known/data.total) +" %"));
		indexPresenceText.setText(String.valueOf((int) Math.round(100 *  data.offert/ data.known) +" %"));
		//indexPresenceTotalText.setText(String.valueOf( (int) Math.round(100 * data.offert/data.total) +" %"));
		indexEffectText.setText(String.valueOf( (int) Math.round(100 * data.win/data.offert) +" %"));
		indexMarketText.setText(String.valueOf((int) Math.round(100 * data.win/data.total) +" %"));
	}
	public void fillLostTable(ChartDataSet data){
		
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
//	
//	public void fillWinTable(InscriptionTableData data){
//		
//		TableLayout winTable = (TableLayout)findViewById(R.id.winTable);
//		String[] answers = AnswersHelper.getAnswersWinArray(getHelper());
//		int[] values = data.getWinData();
//		
//		if(winTable.getChildCount()<=2){
//			for(int i = 0; i< answers.length; i++){
//				TableRow row = (TableRow) View.inflate(ChartBaseActivity.this, R.layout.lost_row_layout, null);
//				row.setTag(i);
//				winTable.addView(row);
//			
//			}
//		}
//		
//		for(int i = 0; i< answers.length; i++){
//				TableRow row =  (TableRow) winTable.getChildAt(i+2);						
//				TextView name = (TextView)row.findViewById(R.id.name);			
//				TextView count = (TextView)row.findViewById(R.id.count);
//				TextView index = (TextView)row.findViewById(R.id.index);
//				
//				name.setText(String.valueOf(answers[i]));
//				count.setText(String.valueOf(values[i]));				
//				index.setText(String.valueOf((int) Math.round(values[i] * 100.0/data.winTotal)+ " %"));
//				
//				
//			
//		}
//		
//		
//		
//	}
	public void fillBrandTable(InscriptionTableData data){
		
//		TableLayout lostTable = (TableLayout)findViewById(R.id.brandTable);
//		String[] brands = FilterHelper.getBrandValues(getApplicationContext(), getHelper());
//		int[] values = data.getBrandData();
//		
//		for(int i = 0; i< brands.length; i++){
//			
//			TableRow row = (TableRow) View.inflate(ChartBaseActivity.this, R.layout.brand_row_layout, null);
//			row.setTag(i);
//			
//			TextView name = (TextView)row.findViewById(R.id.name);			
//			TextView count = (TextView)row.findViewById(R.id.count);
//			
//			
//			name.setText(String.valueOf(brands[i]));
//			count.setText(String.valueOf(values[i]));			
//			lostTable.addView(row);
//		
//		}
		
	}

//protected CategoryDataset getWinDataset() {
//
//	    // row keys...
//	    String series1 = "Motivo";
//	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//	    String[] label =AnswersHelper.getAnswersWinArray(getHelper());
//	    int[] values = data.getWinData();
//	    // column keys...
//	    for(int i = 0;i<label.length;i++){
//	    	
//	    	 dataset.addValue(values[i], series1, label[i]);
//	    }
//
//	    return dataset;
//
//	}
//protected CategoryDataset getLostDataset() {
//
//    // row keys...
//    String series1 = "Motivo";
//    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//    String[] label =AnswersHelper.getAnswersArray(getHelper());
//    int[] values = data.getLostData();
//    // column keys...
//    for(int i = 0;i<label.length;i++){
//    	
//    	 dataset.addValue(values[i], series1, label[i]);
//    }
//
//    return dataset;
//
//}
//
//
///**
// * Creates a sample dataset.
// * @return a sample dataset.
// */
//protected PieDataset getBrandDataset() {
//	
//	
//	DefaultPieDataset dataset = new DefaultPieDataset();
////	String[] label =FilterHelper.getBrandValues(getApplicationContext(), getHelper());
////	int[] values = data.getBrandData();
//	List<Brand> list = data.getBrandData();
//	
//    int others = 0;
//	for(int i = 0;i<list.size();i++){
//		if(i<10){
//			dataset.setValue(list.get(i).getName(), list.get(i).getCount());
//		}else if(i==(list.size()-1)){
//			dataset.setValue("Otros", others);
//		}else{
//			others+=list.get(i).getCount();
//		}    	
//   	 
//   }
//	
//    return dataset;
//}

}
