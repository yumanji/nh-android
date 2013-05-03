package com.movetothebit.newholland.android.helpers;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.movetothebit.newholland.android.db.DBHelper;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.utils.lConstants;

public class FilterHelper implements lConstants{
	
	public static final String TAG = "Filterhelper";
	
	public static List<InscriptionData> getComunValuesFromInscription(Context ctx, DBHelper helper,String columnName){
		
		List<InscriptionData> results = null;
		
		try {
			results = helper.getInscriptionsDao().queryBuilder()
		    .distinct().selectColumns(columnName).orderBy(columnName, true).query();			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
//	public static String[] getBrandValues(Context ctx, DBHelper helper){
//	
//		String[] result = null;
//		List<InscriptionData> results;
//		try {
//			results = helper.getInscriptionsDao().queryBuilder().groupBy(BRAND).selectColumns(MONTH,YEAR).query();
//			if(results.size()!=0){
//				
//				result = new String[results.size()];
//				
//				for (int i = 0;i<results.size();i++) {
//					result[i] = results.get(i).getBrand();
//				    
//				}
//			}else{
//				result = new String[1];
//				result[0] = "No hay datos";
//			}
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}			
//		
//		
//		return result;
//	}
	public static String[] getBrandValues(Context ctx, DBHelper helper){
		
		String[] result = null;
		List<InscriptionData> results = getComunValuesFromInscription(ctx,helper, BRAND);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getBrand();
			    
			}
		}else{
			result = new String[1];
			result[0] = "No hay datos";
		}
		return result;
	}
//	public static BrandData[] getBrandData(Context ctx, DBHelper helper){
//		String[] brands = getBrandValues(ctx,helper);
//		
//		for(int i = 0;i<brands.length;i++) {
//			helper.getInscriptionsDao().queryBuilder().groupBy(BRAND).s;
//		}
//		String[] result = null;
//		helper.getInscriptionsDao().queryBuilder();	
//		
//		
//		return result;
//	}
	public static String[] getPeriodValues(Context ctx){
			
		return DateHelper.getLastMonths(12);
	}
	public static String[] getPeriodValues(Context ctx,DBHelper helper){
		String[] result = null;
		List<InscriptionData> results;
		try {
			results = helper.getInscriptionsDao().queryBuilder().distinct().orderBy(ID, true).selectColumns(MONTH,YEAR).query();
			if(results.size()!=0){
				
				result = new String[results.size()];
				
				for (int i = 0;i<results.size();i++) {
					result[i] = results.get(i).getMonth()+ " "+ results.get(i).getYear() ;
				    
				}
			}else{
				result = new String[1];
				result[0] = "No hay datos";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
		return result;
	}
	public static String[] getPopulationValues(Context ctx, DBHelper helper){
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx, helper,POPULATION);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getPopulation();
			    
			}
		}else{
			result = new String[1];
			result[0] = "No hay datos";
		}
		return result;
	}
	public static String[] getModelsCompValues(Context ctx, DBHelper helper){
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx,helper, MODEL_EQUAL);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getModeloComparable();
			    
			}
		}else{
			result = new String[1];
			result[0] = "No hay datos";
		}
		return result;
	}
	public static String[] getModelsValues(Context ctx,DBHelper helper){
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx, helper,COMMERCIAL_MODEL);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getCommercialModel();
			    
			}
		}else{
			result = new String[1];
			result[0] = "No hay datos";
		}
		return result;
	}
	public static String[] getModel3Values(Context ctx,DBHelper helper){
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx, helper,MODEL3);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getModelo3();
			    
			}
		}else{
			result = new String[1];
			result[0] = "No hay datos";
		}
		return result;
	}
	public static String[] getSalesmanValues(Context ctx, DBHelper helper){
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx,helper, SALESMAN_NAME);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getSalesmanName();
			    
			}
		}else{
			result = new String[1];
			result[0] = "No hay datos";
		}
		return result;
	}
	public static String[] getDealerValues(Context ctx, DBHelper helper){
		String[] result = null;
		List<InscriptionData> results =getComunValuesFromInscription(ctx, helper,DEALER_NAME);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getDealerName();
			    
			}
		}else{
			result = new String[1];
			result[0] = "No hay datos";
		}
		return result;
	}
	public static String[] getAreaValues(Context ctx, DBHelper helper){		
		String[] result = null;
		List<InscriptionData> results = getComunValuesFromInscription(ctx,helper, AREA);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getArea();
			    
			}
		}else{
			result = new String[1];
			result[0] = "No hay datos";
		}
		return result;
	}
	public static String[] getTypeValues(Context ctx, DBHelper helper){		
		String[] result = null;
		List<InscriptionData> results = getComunValuesFromInscription(ctx,helper, MACHINE_TYPE);		
		
		if(results.size()!=0){
			
			result = new String[results.size()];
			
			for (int i = 0;i<results.size();i++) {
				result[i] = results.get(i).getMachineType();
			    
			}
		}else{
			result = new String[1];
			result[0] = "No hay datos";
		}
		return result;
	}
}
