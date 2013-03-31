package com.movetothebit.newholland.android.helpers;

import java.util.List;

import android.content.Context;

import com.movetothebit.newholland.android.db.DBHelper;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.model.InscriptionTableData;


public class ChartHelper {

	
	public static InscriptionTableData getInscriptionTableData(Context ctx, DBHelper helper, List<InscriptionData> listData){	
	
		InscriptionTableData total = new InscriptionTableData();
		int knownTotal =0;
		int ofertTotal = 0;
		int winTotal = 0;
		int[] lostData = new int[AnswersHelper.getAnswersArray(helper).length];
		int[] winData = new int[AnswersHelper.getAnswersWinArray(helper).length];
		
		
		
		for(InscriptionData item: listData){
			
			if(item.knownOperation==1) knownTotal++;
			if(item.makeOffer==1) ofertTotal++;
			if(item.winOffer==1){
				winTotal++;
				winData[item.getWhyWin()] =++winData[item.getWhyWin()]; 
			}else{
				if(item.getWhyLose()>=0){
					lostData[item.getWhyLose()] =++lostData[item.getWhyLose()]; 
				}
				
			}
			
			
		}
		
		total.setMarketTotal(listData.size());		
		total.setKnownTotal(knownTotal);
		total.setOfertTotal(ofertTotal);
		total.setWinTotal(winTotal);
		total.setLostData(lostData);
		total.setWinData(winData);
		total.setBrandData(getBrandData(ctx, helper, listData));
		return total;
	
	}	
	
	public static int[] getBrandData(Context ctx, DBHelper helper,List<InscriptionData> listData){		
		
		String[] brands = FilterHelper.getBrandValues(ctx, helper);
		int[] result = new int[brands.length];
		
		for(int i=0;i<brands.length;i++){
			
			result[i] = getCountBrand(listData,brands[i]);
		
		}
		
		return result;
	}
	
	public static int getCountBrand(List<InscriptionData> list,String name){
		
		int result = 0;
		for(InscriptionData item: list){
			if(item.getBrand().equals(name)){
				++result;
			}
		}
		return result;
	}
}
