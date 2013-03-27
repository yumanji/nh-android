package com.movetothebit.newholland.android.helpers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.j256.ormlite.dao.Dao;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.db.DBHelper;
import com.movetothebit.newholland.android.model.ModelItem;
import com.movetothebit.newholland.android.utils.RemoteFacade;
import com.movetothebit.newholland.android.utils.ServerException;
import com.movetothebit.newholland.android.utils.SystemException;
import com.movetothebit.newholland.android.utils.lConstants;

public class ModelHelper implements lConstants{
	public static final String TAG = "Modelhelper";
	
	 public static  List<ModelItem>  getModelsFromServer(Context ctx) throws ServerException {
		  	
		  List<ModelItem> list = null;
			
			String json = null;
			Gson gson = null;
			
			try {
							
				json = RemoteFacade.stringFromServer(URL+GET_MODELS);
				JSONObject jsonObject = new JSONObject(json);
				JSONObject statusObject = jsonObject.getJSONObject("error");
				
				if(statusObject.getString("code").equals("0")){
					
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					
					list = new ArrayList<ModelItem>();
					       
					 if (jsonArray.length() > 0) { 
						 gson = new Gson();
						 
					        for (int i=0;i<jsonArray.length();i++){ 
					        	
					        	ModelItem message = gson.fromJson(jsonArray.getString(i), ModelItem.class);						        	 
							    list.add(message);
					        	   
					        }
					       
					} 
				}else{
					throw new ServerException(new Exception(), statusObject.getString("message"));

				}
				 
			} catch (NotFoundException e) {				
				throw new ServerException(e, ctx.getString(R.string.refresh_exception));
			} catch (JsonSyntaxException e) {
				 throw new ServerException(e, ctx.getString(R.string.refresh_exception));
			} catch (JSONException e) {
				throw new ServerException(e, ctx.getString(R.string.refresh_exception));
			} catch (SystemException e) {
				throw new ServerException(e, ctx.getString(R.string.refresh_exception));
			}
			return list;
	     
	 }
		public static String[] getModelArray(DBHelper helper){
		String[] array = null;
		List<ModelItem> list = null;
		
		try {
			list = getModels(helper);
			array = new String[list.size()];
			
			for (int i = 0; i < list.size(); i++){
				array[i]= list.get(i).getValue();
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
	}
	public static void syncModels(Context ctx, DBHelper helper) throws SQLException,ServerException{
		
		List<ModelItem> list = null;
		List<ModelItem> tempList = null;
		Dao<ModelItem, Integer> dao = null;
		
		try {
			
			dao = helper.getModelsDao();
			tempList = getModels(helper);
			list = getModelsFromServer(ctx);
			
			dao.deleteBuilder().where();
			dao.deleteBuilder().delete();
			
			insertModels(ctx,helper, list);
			
			
			
		} catch (SQLException e) {
			insertModels(ctx,helper, tempList);
			throw e;
			
		} catch (ServerException e) {
			insertModels(ctx,helper, tempList);
			throw e;
			
		}
		
	}
	public static void downloadModels(Context ctx, DBHelper helper) throws SQLException,ServerException{
		
		List<ModelItem> list = null;
		
		Dao<ModelItem, Integer> dao = null;
		
		try {
			
			dao = helper.getModelsDao();			
			list = getModelsFromServer(ctx);			
			insertModels(ctx,helper, list);		
			
		} catch (SQLException e) {
		
			throw e;
			
		} catch (ServerException e) {
		
			throw e;
			
		}
		
	}
	public static List<ModelItem> getModels(DBHelper helper) throws SQLException, ServerException{
		
		List<ModelItem> result  = null;
		
		
		try {
			result = helper.getModelsDao().queryForAll();
			
			
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
		
	    
	}

	public static  void insertModels(Context context,DBHelper helper, List<ModelItem> list)
			throws SQLException {

		try {
			if(list!=null){
				for (ModelItem item : list) {
					helper.getModelsDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			throw e;

		}
	}
	
	public static  void updateModels(Context context, DBHelper helper,List<ModelItem> list)
			throws SQLException {

		try {
			if(list!=null){
				for (ModelItem item : list) {
					helper.getModelsDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			Log.e(TAG, e.toString());
			throw e;

		}
	}
}
