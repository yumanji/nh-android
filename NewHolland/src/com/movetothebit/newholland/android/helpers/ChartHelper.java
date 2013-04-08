package com.movetothebit.newholland.android.helpers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.movetothebit.newholland.android.charts.model.ChartDataSet;
import com.movetothebit.newholland.android.charts.model.MonthDataSet;
import com.movetothebit.newholland.android.db.DBHelper;
import com.movetothebit.newholland.android.model.Brand;
import com.movetothebit.newholland.android.model.BrandData;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.model.InscriptionTableData;
import com.movetothebit.newholland.android.utils.lConstants;


public class ChartHelper implements lConstants {

	public static final String TAG = "ChartHelper";
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
	
	public static ChartDataSet getDataSet(List<InscriptionData> data,String[] brands, int lostCount){
		
		ChartDataSet dataSet = new ChartDataSet();
		
		String[] lastMonths = DateHelper.getLastMonths(20);
		MonthDataSet[] monthDataSetList = new MonthDataSet[lastMonths.length];
		if(dataSet.lostData == null){
			dataSet.lostData = new float[lostCount];
			dataSet.totalBrand = new Brand[brands.length];
		}
		Log.d(TAG,"start for;    " +new Date().toGMTString());
		//Recorremos todos los elementos del filtro
 		for(InscriptionData item: data){
			
 			//Ahora para cada elemento comprobamos para cada mes de los ultimos los valores para ir rellenando el data set de los graficos
			for(int i=0; i<lastMonths.length; i++){
				
				if((item.getMonth()+" "+ item.getYear()).equals(lastMonths[i])){
					
					if( monthDataSetList[i]== null){
						monthDataSetList[i] = new MonthDataSet();
						monthDataSetList[i].lostData = new float[lostCount];
						monthDataSetList[i].totalBrand = new Brand[brands.length];
						monthDataSetList[i].month = lastMonths[i].substring(0,3);
						monthDataSetList[i].year = lastMonths[i].substring(5,8);
					}
					
					monthDataSetList[i].total = ++monthDataSetList[i].total;
					dataSet.total = ++dataSet.total;
					
					if(item.knownOperation==1){
						monthDataSetList[i].known = ++monthDataSetList[i].known;
						dataSet.known = ++dataSet.known;
					}
					if(item.makeOffer==1){
						monthDataSetList[i].offert = ++monthDataSetList[i].offert;
						dataSet.offert = ++dataSet.offert;
					}
					if(item.winOffer==1){
						
						if(item.getWhyWin()>=0){
							monthDataSetList[i].win = ++monthDataSetList[i].win;	
							dataSet.win = ++dataSet.win;
						}
						
					}else{
						if(item.getWhyLose()>=0){
							
							monthDataSetList[i].lost = ++monthDataSetList[i].lost;
							dataSet.lost = ++dataSet.lost;
							monthDataSetList[i].lostData[item.getWhyLose()] =++monthDataSetList[i].lostData[item.getWhyLose()]; 
							dataSet.lostData[item.getWhyLose()] =++dataSet.lostData[item.getWhyLose()]; 
						}
						
					}	
					
					for(int j=0; j<brands.length; j++){
//						Log.d(TAG, item.getBrand()+"  //  " +brands[j]);
						if(monthDataSetList[i].totalBrand[j]==null)
							monthDataSetList[i].totalBrand[j] = new Brand();
						if(dataSet.totalBrand[j]== null)
							dataSet.totalBrand[j] = new Brand();
						
						if(item.getBrand().equals(brands[j])){						
							
							
							monthDataSetList[i].totalBrand[j].name = brands[j];
							monthDataSetList[i].totalBrand[j].count = ++monthDataSetList[i].totalBrand[j].count;
							dataSet.totalBrand[j].name = brands[j];
							dataSet.totalBrand[j].count = ++dataSet.totalBrand[j].count;
						}						
							
					}
				}			
				
			}
			
		}
		
		dataSet.monthDataSet = monthDataSetList;
		
		
		
		Log.d(TAG,"end for;    " + new Date().toGMTString());
		
		return dataSet;
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
	public static List<Brand> getTopBrandData(Context ctx, DBHelper helper,List<InscriptionData> listData){		
		
		return getBrandData(ctx, helper, listData).subList(0, 9);
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
