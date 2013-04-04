package com.movetothebit.newholland.android.helpers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

import com.movetothebit.newholland.android.db.DBHelper;
import com.movetothebit.newholland.android.model.Brand;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.model.InscriptionTableData;
import com.movetothebit.newholland.android.utils.lConstants;


public class ChartHelper implements lConstants {

	
	public static InscriptionTableData getInscriptionTableData(Context ctx, DBHelper helper, List<InscriptionData> listData){	
	
		InscriptionTableData total = new InscriptionTableData();
		int knownTotal =0;
		int ofertTotal = 0;
		int winTotal = 0;
		int lostTotal = 0;
		int[] lostData = new int[AnswersHelper.getAnswersArray(helper).length];
		int[] winData = new int[AnswersHelper.getAnswersWinArray(helper).length];
		
		
		
		for(InscriptionData item: listData){
			
			if(item.knownOperation==1) knownTotal++;
			if(item.makeOffer==1) ofertTotal++;
			if(item.winOffer==1){
				if(item.getWhyWin()>=0){
					winTotal++;
					winData[item.getWhyWin()] =++winData[item.getWhyWin()]; 
				}
				
			}else{
				if(item.getWhyLose()>=0){
					lostTotal++;
					lostData[item.getWhyLose()] =++lostData[item.getWhyLose()]; 
				}
				
			}
			
			
		}
		
		total.setMarketTotal(listData.size());		
		total.setKnownTotal(knownTotal);
		total.setOfertTotal(ofertTotal);
		total.setWinTotal(winTotal);
		total.setLostTotal(lostTotal);
		total.setLostData(lostData);
		total.setWinData(winData);
		
		
		total.setBrandData(getBrandData(ctx, helper, listData));
		
		//presencew 
		
//		total.setMarketCuote((int) Math.round(brandData.getNh() * 100/listData.size()));
		total.setMarketCuote(20);
		total.setKnownMarket((int) Math.round(knownTotal * 100.0/listData.size()));
		total.setPresence((int) Math.round(ofertTotal * 100.0/knownTotal));
		total.setPresenceTotal((int) Math.round(ofertTotal * 100.0/listData.size()));
		total.setEfectivity((int) Math.round(winTotal * 100.0/ofertTotal));
		
		return total;
	
	}	
	

	public static List<Brand> getBrandData(Context ctx, DBHelper helper,List<InscriptionData> listData){		
		List<Brand> data = new ArrayList<Brand>();		
		List<InscriptionData> list = null;
		try {
			list = helper.getInscriptionsDao().queryBuilder().distinct().selectColumns(BRAND).query();
			
			for(int i=0;i<list.size();i++){
				Brand br = new Brand();
				br.setCount(helper.getInscriptionsDao().queryForEq(BRAND, list.get(i).getBrand()).size());				
				br.setName(list.get(i).getBrand());
				data.add(br);
			}
			Collections.sort(data, new BrandComparator());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
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
