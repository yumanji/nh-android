package com.movetothebit.newholland.android.helpers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.Log;

import com.movetothebit.newholland.android.charts.model.FilterDataSet;
import com.movetothebit.newholland.android.charts.model.MonthDataSet;
import com.movetothebit.newholland.android.db.DBHelper;
import com.movetothebit.newholland.android.model.Brand;
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
	
	public static List<InscriptionData>  getFilterList(List<InscriptionData> data, String[] brands, String[] dates){
		
		List<InscriptionData> dataFilter = new ArrayList<InscriptionData>();
		if(dates.length>0||brands.length>0){
			for(InscriptionData item: data){
				boolean result = true;
				for (String date: dates){
					if((item.getMonth()+" "+item.getYear()).equals(date)){
						result=false;
						break;						
					}
				}
			
				for(String brand:brands){
					if(item.getBrand().equals(brand)){
						result = false;
						break;
					}
				}
				
				if(result)
					dataFilter.add(item);			
			
			}
		}else{
			return data;
		}
		
		return dataFilter;
		
		
		
		
	}
	public static MonthDataSet[] getMonthDataSet(List<InscriptionData> data,String[] brands){
		
			
		String[] lastMonths = DateHelper.getLastMonths(12);		
		MonthDataSet[] monthDataSetList = new MonthDataSet[lastMonths.length];
				
		for(int i=0; i<lastMonths.length; i++){
			
			monthDataSetList[i] = new MonthDataSet();			
			monthDataSetList[i].totalBrand = new Brand[brands.length];
			
			for(int j=0; j<brands.length; j++){
				
				monthDataSetList[i].totalBrand[j] = new Brand();
				monthDataSetList[i].totalBrand[j].name = brands[j];
					
			}
		}
		
		
		Log.d(TAG,"start for;    " +new Date().toGMTString());
		
 		for(InscriptionData item: data){
			
 			//Ahora para cada elemento comprobamos para cada mes de los ultimos los valores para ir rellenando el data set de los graficos
			for(int i=0; i<lastMonths.length; i++){
				
				if( monthDataSetList[i].month== null){					
					monthDataSetList[i].month = lastMonths[i].substring(0,3);
					monthDataSetList[i].year = lastMonths[i].substring(5,8);
				}
				
				if((item.getMonth().toUpperCase(new Locale("es", "ES"))+" "+ item.getYear()).equals(lastMonths[i])){					
					
					++monthDataSetList[i].total;
					
					for(int j=0; j<brands.length; j++){						
						if(item.getBrand().equals(brands[j]))
							++monthDataSetList[i].totalBrand[j].count;
					}
					
					if(item.knownOperation==1)
						++monthDataSetList[i].known;
					
					if(item.makeOffer==1)
						++monthDataSetList[i].offert;
					
					if(item.winOffer==1){						
						if(item.getWhyWin()>=0)
							++monthDataSetList[i].win;							
					}						
				}					
			}			
		}
		
		
		
		return monthDataSetList;
	}
public static FilterDataSet getFilterDataSet(List<InscriptionData> data, int lostCount, String[] brands, String[] dates){
		
		List<InscriptionData> dataFilter = getFilterList(data, brands, dates);
		FilterDataSet dataSet = new FilterDataSet();		
		
		dataSet.lostData = new float[lostCount];
		
		
		
 		for(InscriptionData item: dataFilter){
			
 			++dataSet.total;
			if(item.knownOperation==1)
				++dataSet.known;
			
			if(item.makeOffer==1)
				++dataSet.offert;
					
			if(item.winOffer==1){
				if(item.getWhyWin()>=0)
					++dataSet.win;				
			}else{
				if(item.getWhyLose()>=0&&item.getWhyLose()<=lostCount){
					++dataSet.lost;
					++dataSet.lostData[item.getWhyLose()]; 
				}
			}	
		}
		
		
		
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
