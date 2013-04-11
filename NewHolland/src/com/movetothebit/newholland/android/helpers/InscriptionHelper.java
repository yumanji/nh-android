package com.movetothebit.newholland.android.helpers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.db.DBHelper;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.utils.RemoteFacade;
import com.movetothebit.newholland.android.utils.ServerException;
import com.movetothebit.newholland.android.utils.SystemException;
import com.movetothebit.newholland.android.utils.lConstants;

public class InscriptionHelper implements lConstants{
	
	public static final String TAG = "Inscriptionhelper";
	
	
	public static boolean hasInscriptionHang(DBHelper helper) throws SQLException{
		
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		
		try {
			
			 queryBuilder = helper.getInscriptionsDao().queryBuilder();			 
			 queryBuilder.where().eq(FILL_DATA, 1).and().eq(HISTORIC, 0);			
			 
			 result = helper.getInscriptionsDao().query(queryBuilder.prepare());
		
			if(result.size()==0){
				return false;
			}else{
				return true;
			}
			
		} catch (SQLException e) {
			throw e;
		
		}
		 
		
	}
	public static InscriptionData getInscription(DBHelper helper, String index) throws SQLException{		
		
		List<InscriptionData> resultList  = null;		
		
		try {
			resultList =  helper.getInscriptionsDao().queryForEq(INSCRIPTION, index);				
			
		} catch (SQLException e) {
			throw e;
		}
		
		return resultList.get(0);
	}
	
	 public static   List<InscriptionData> getIncriptionsFromServer(Context ctx) throws ServerException{
			
		  	List<InscriptionData> list = null;
			String jsonUrl = null;
			String json = null;
			Gson gson = null;				
			SharedPreferences settings = null;		
			
			try {
				
				settings = ctx.getSharedPreferences(ctx.getString(R.string.app_preferences), 0);							
				jsonUrl = URL+GET_SURVEY+settings.getString(userData, "0");
				json = RemoteFacade.stringFromServer(jsonUrl);
				JSONObject jsonObject = new JSONObject(json);
				JSONObject statusObject = jsonObject.getJSONObject("error");
				
				if(statusObject.getString("code").equals("0")){
					
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					
					list = new ArrayList<InscriptionData>();
					       
					 if (jsonArray.length() > 0) { 
						 gson = new Gson();
						 
					        for (int i=0;i<jsonArray.length();i++){ 
					        	
					        	InscriptionData message = gson.fromJson(jsonArray.getString(i), InscriptionData.class);						        	 
							    list.add(message);
					        	   
					        }
					      
					}
					 
				}else{
					throw new ServerException(new Exception(), statusObject.getString("message"));

				}
				 
			} catch (NotFoundException e) {				
				throw new ServerException(e, ctx.getString(R.string.refresh_exception));
			}  catch (JsonSyntaxException e) {
				 throw new ServerException(e, ctx.getString(R.string.refresh_exception));
			} catch (JSONException e) {
				throw new ServerException(e, ctx.getString(R.string.refresh_exception));
			} catch (SystemException e) {
				throw new ServerException(e, ctx.getString(R.string.refresh_exception));
			}
	   
			return list;
	}
	public static boolean  sendInscriptions(Context ctx, DBHelper helper ) throws ServerException  {
	  	
	  	
		String jsonUrl = null;
		String response = null;
		SharedPreferences prefs = null;		
		List<InscriptionData> list = null;
		
		try {
			
			list = getSendInscriptions(helper);
			if(list.size()>0){
				prefs = ctx.getSharedPreferences(ctx.getString(R.string.app_preferences), 0);	
				jsonUrl = URL+SET_SURVEY+prefs.getString(userData, "0");
				Gson gson = new Gson();
				JSONObject jsonObject = new JSONObject();	
				
				JSONArray data = new JSONArray();
				 for(InscriptionData item :list){
					 JSONObject obj = new JSONObject(gson.toJson(item));
					 data.put(obj);
				 }

				jsonObject.put("data", data);
				

				response = RemoteFacade.postStringToUrl(jsonUrl, jsonObject.toString());
				JSONObject jsonResponse = new JSONObject(response);
				JSONObject errorJson = jsonResponse.getJSONObject("error");
				
				if(errorJson.getString("code").equals("0")){
					return true;
				}else{
					throw new ServerException(new Exception(), errorJson.getString("message"));
				}
			}else{
				return true;
			}
			
			 
		} catch (NotFoundException e) {				
			throw new ServerException(e, ctx.getString(R.string.refresh_exception));
		}  catch (JsonSyntaxException e) {
			 throw new ServerException(e, ctx.getString(R.string.refresh_exception));
		} catch (JSONException e) {
			throw new ServerException(e, ctx.getString(R.string.refresh_exception));
		} catch (SQLException e) {			
			e.printStackTrace();
			throw new ServerException(e, ctx.getString(R.string.refresh_exception));
		} 
    
 }
	public static void downloadInscriptions(Context ctx,DBHelper helper) throws SQLException,ServerException{
		
		List<InscriptionData> list = null;			
		
		try {		
			
			list = getIncriptionsFromServer(ctx);
			insertInscriptions(ctx,helper, list);
			
			
		} catch (SQLException e) {
			throw e;
		} catch (ServerException e) {
			
			throw e;
			
		}
		
	}

	public static void syncInscriptions(Context ctx,DBHelper helper) throws SQLException,ServerException{
		
		List<InscriptionData> list = null;
		List<InscriptionData> tempList = null;
		Dao<InscriptionData, Integer> dao = null;
		
		try {
			
			dao = helper.getInscriptionsDao();
			tempList = getInscriptions(helper);
			list = getIncriptionsFromServer(ctx);
			
			dao.deleteBuilder().where();
			dao.deleteBuilder().delete();
			
			insertInscriptions(ctx,helper, list);
			
			
		} catch (SQLException e) {
			
			insertInscriptions(ctx, helper,tempList);
			throw e;
		} catch (ServerException e) {
			insertInscriptions(ctx, helper,tempList);
			throw e;
			
		}
		
	}
	
	public static  void updateInscriptions(Context context,DBHelper helper,
			List<InscriptionData> list) throws SQLException {

		try {
			if(list!=null){
				for (InscriptionData item : list) {
					helper.getInscriptionsDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			throw e;

		}
	}
	
	public static List<InscriptionData> getInscriptions(DBHelper helper) throws SQLException, ServerException{
		
		List<InscriptionData> result  = null;		
		
		try {
			result =  helper.getInscriptionsDao().queryForAll();				
			
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
		
	    
	}
	public static List<InscriptionData> getHistoric(DBHelper helper) throws SQLException, ServerException{
		
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		
		try {
			
			 queryBuilder = helper.getInscriptionsDao().queryBuilder();
			 queryBuilder.setWhere(queryBuilder.where().eq(HISTORIC, 1));
			    
			 result = helper.getInscriptionsDao().query(queryBuilder.prepare());
			
		} catch (SQLException e) {
			throw e;		
		}
		return result;    
	}

	
	public static void createOrUpdateInscription(DBHelper helper,InscriptionData item) throws SQLException{
		helper.getInscriptionsDao().createOrUpdate(item);
	}
	

	
	public static List<InscriptionData> getInscriptionsFilter(DBHelper helper, String[] salesman, String[] dealer,String[] type)throws SQLException, ServerException{
		
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		Where<InscriptionData, Integer> where= null;
		
		try {
			
			queryBuilder = helper.getInscriptionsDao().queryBuilder();
			 // get the WHERE object to build our query
			where = queryBuilder.where();
			// the name field must be equal to "foo"
			if(salesman.length>0){
				where.in(SALESMAN_NAME, salesman);
				where.and();
			}
			
			// the password field must be equal to "_secret"
			if(dealer.length>0){
				where.in(DEALER_NAME, dealer);
				where.and();
			}
			if(type.length>0){
				where.in(MACHINE_TYPE, type);
				where.and();
			}
			where.eq(HISTORIC, 0);
			
			result = helper.getInscriptionsDao().query(queryBuilder.prepare());
		
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
	}
	public static List<InscriptionData> getInscriptionsFilter(DBHelper helper, String[] salesman, String[] dealer, String[] model, String[] modelComp, String[] period, String[] brand, String[] population, String[] area)throws SQLException, ServerException{
		
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		Where<InscriptionData, Integer> where= null;
		
		try {
			
			queryBuilder = helper.getInscriptionsDao().queryBuilder();
			 // get the WHERE object to build our query
			where = queryBuilder.where();
			// the name field must be equal to "foo"
			if(salesman.length>0){
				where.in(SALESMAN_NAME, salesman);
				where.and();	
			}
			
			// the password field must be equal to "_secret"
			if(dealer.length>0){
				where.in(DEALER_NAME, dealer);
				where.and();				
			}
			if(model.length>0){
				where.in(MODEL3, model);
				where.and();				
			}
			if(modelComp.length>0){
				where.in(MODEL_EQUAL, modelComp);
				where.and();				
			}
			if(period.length>0){
				where.in(MONTH, period);
				where.and();				
			}
			if(brand.length>0){
				where.in(BRAND, brand);
				where.and();				
			}
			if(population.length>0){
				where.in(POPULATION, population);
				where.and();				
			}
			if(area.length>0){
				where.in(AREA, area);
				where.and();				
			}			
			
			where.eq(HISTORIC, 0);
			
			result = helper.getInscriptionsDao().query(queryBuilder.prepare());
		
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
	}
	public static List<InscriptionData> getInscriptionsFilter(DBHelper helper, String[] salesman, String[] dealer, String[] model, String[] modelComp, String[] population, String[] area,String[] type)throws SQLException, ServerException{
		
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		Where<InscriptionData, Integer> where= null;
		
		try {
			
			queryBuilder = helper.getInscriptionsDao().queryBuilder();
			 // get the WHERE object to build our query
			where = queryBuilder.where();
			// the name field must be equal to "foo"
			if(salesman.length>0){
				where.in(SALESMAN_NAME, salesman);
				where.and();	
			}
			
			// the password field must be equal to "_secret"
			if(dealer.length>0){
				where.in(DEALER_NAME, dealer);
				where.and();				
			}
			if(model.length>0){
				where.in(MODEL3, model);
				where.and();				
			}
			if(modelComp.length>0){
				where.in(MODEL_EQUAL, modelComp);
				where.and();				
			}			
			if(population.length>0){
				where.in(POPULATION, population);
				where.and();				
			}
			if(area.length>0){
				where.in(AREA, area);
				where.and();				
			}			
			if(type.length>0){
				where.in(MACHINE_TYPE, type);
				where.and();				
			}
			where.eq(HISTORIC, 0);
			
			result = helper.getInscriptionsDao().query(queryBuilder.prepare());
		
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
	}
	public static List<InscriptionData> getInscriptionsEmpty(DBHelper helper) throws SQLException, ServerException{
		
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		
		try {
			
		
			 queryBuilder = helper.getInscriptionsDao().queryBuilder();
			 queryBuilder.setWhere(queryBuilder.where().eq(HISTORIC, 0));
			    
			 result = helper.getInscriptionsDao().query(queryBuilder.prepare());
			
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
		
	    
	}
	
	public static List<InscriptionData> getHistoricFilter(DBHelper helper,String[] salesman, String[] dealer,String[] type)throws SQLException, ServerException{
		
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		Where<InscriptionData, Integer> where= null;
		
		try {
			
			queryBuilder = helper.getInscriptionsDao().queryBuilder();
			 // get the WHERE object to build our query
			where = queryBuilder.where();
			// the name field must be equal to "foo"
			if(salesman.length>0){
				where.in(SALESMAN_NAME, salesman);
				where.and();
			}
			
			// the password field must be equal to "_secret"
			if(dealer.length>0){
				where.in(DEALER_NAME, dealer);
				where.and();
			}
			
			if(type.length>0){
				where.in(MACHINE_TYPE, type);
				where.and();
			}
			where.eq(HISTORIC, 1);
			
			result = helper.getInscriptionsDao().query(queryBuilder.prepare());
		
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
	}
	public static List<InscriptionData> getSendInscriptions(DBHelper helper) throws SQLException, ServerException{
	
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		
		try {
			
			 queryBuilder = helper.getInscriptionsDao().queryBuilder();			 
			 queryBuilder.where().eq(FILL_DATA, 1).and().eq(HISTORIC, 0);			
			 
			 result = helper.getInscriptionsDao().query(queryBuilder.prepare());
		
			
			
		} catch (SQLException e) {
			throw e;
		
		}
		return result;  
		
	}   
	public static List<InscriptionData> getInscriptionsFilled(DBHelper helper) throws SQLException, ServerException{
		
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		
		try {
			
			 queryBuilder = helper.getInscriptionsDao().queryBuilder();			 
			 queryBuilder.setWhere(queryBuilder.where().eq(lConstants.FILL_DATA, "1"));
			    
			 result = helper.getInscriptionsDao().query(queryBuilder.prepare());
		
			
			
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
		
	    
	}
	public static List<InscriptionData> getInscriptionsGlobal(DBHelper helper) throws SQLException, ServerException{
		
		List<InscriptionData> result  = null;
		QueryBuilder<InscriptionData,Integer> queryBuilder = null;
		
		try {
			
			 queryBuilder = helper.getInscriptionsDao().queryBuilder();			 
			 queryBuilder.where().eq(FILL_DATA, 1).or().eq(HISTORIC, 1);			
			 
			 result = helper.getInscriptionsDao().query(queryBuilder.prepare());
		
			
			
		} catch (SQLException e) {
			throw e;
		
		}
		return result;  
		
	    
	}
	public static void createInscriptions(Context context,DBHelper helper,
			List<InscriptionData> list) throws SQLException {

		try {
			if(list!=null){
				for (InscriptionData item : list) {
					helper.getInscriptionsDao().create(item);
				}
			}
			

		} catch (SQLException e) {
			// dao.rollBack(helper.getConnectionSource());
			Log.e(TAG, "Error creando usuario");
			throw e;

		}
	}
	public static void insertInscriptions(Context context,DBHelper helper,
			List<InscriptionData> list) throws SQLException {

		try {
			if(list!=null){
				for (InscriptionData item : list) {
					helper.getInscriptionsDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			// dao.rollBack(helper.getConnectionSource());
			Log.e(TAG, "Error creando usuario");
			throw e;

		}
	}
}
