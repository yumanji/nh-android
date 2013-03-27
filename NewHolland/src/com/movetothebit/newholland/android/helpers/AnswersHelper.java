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
import com.movetothebit.newholland.android.model.AnswerItem;
import com.movetothebit.newholland.android.model.AnswerWinItem;
import com.movetothebit.newholland.android.utils.RemoteFacade;
import com.movetothebit.newholland.android.utils.ServerException;
import com.movetothebit.newholland.android.utils.SystemException;
import com.movetothebit.newholland.android.utils.lConstants;

public class AnswersHelper implements lConstants{
	
	public static final String TAG = "Answershelper";
	
	 public static List<AnswerWinItem>  getAnswersWinFromServer(Context ctx) throws ServerException  {
		  	
		  	List<AnswerWinItem> list = null;			
			String json = null;
			Gson gson = null;
			
			
			try {
				
				json = RemoteFacade.stringFromServer(URL+GET_ANSWERS_WIN);
				JSONObject jsonObject = new JSONObject(json);
				JSONObject statusObject = jsonObject.getJSONObject("error");
				
				if(statusObject.getString("code").equals("0")){
					
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					
					list = new ArrayList<AnswerWinItem>();
					       
					 if (jsonArray.length() > 0) { 
						 gson = new Gson();
						 
					        for (int i=0;i<jsonArray.length();i++){ 
					        	
					        	AnswerWinItem message = gson.fromJson(jsonArray.getString(i), AnswerWinItem.class);						        	 
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
	  public static List<AnswerItem>  getAnswersFromServer(Context ctx) throws ServerException  {
		  	
		  	List<AnswerItem> list = null;
			
			String json = null;
			Gson gson = null;
			
			try {
							
				json = RemoteFacade.stringFromServer(URL+GET_ANSWERS);
				JSONObject jsonObject = new JSONObject(json);
				JSONObject statusObject = jsonObject.getJSONObject("error");
				
				if(statusObject.getString("code").equals("0")){
					
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					
					list = new ArrayList<AnswerItem>();
					       
					 if (jsonArray.length() > 0) { 
						 gson = new Gson();
						 
					        for (int i=0;i<jsonArray.length();i++){ 
					        	
					        	AnswerItem message = gson.fromJson(jsonArray.getString(i), AnswerItem.class);						        	 
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
	  
	  public static void downloadAnswers(Context ctx, DBHelper helper) throws SQLException,ServerException{
		
		List<AnswerItem> list = null;
		
		Dao<AnswerItem, Integer> dao = null;
		
		try {
			
			dao = helper.getAnswersDao();
		
			list = getAnswersFromServer(ctx);
			
			
			insertAnswers(ctx,helper, list);
			
			
		} catch (SQLException e) {
			
			throw e;
		} catch (ServerException e) {
			
			throw e;
		}
		
	}
	  public static void downloadAnswersWin(Context ctx, DBHelper helper) throws SQLException,ServerException{
			
			List<AnswerWinItem> list = null;
			
			Dao<AnswerWinItem, Integer> dao = null;
			
			try {
				
				dao = helper.getAnswersWinDao();
			
				list = getAnswersWinFromServer(ctx);
				
				
				insertAnswersWin(ctx,helper, list);
				
				
			} catch (SQLException e) {				
				throw e;
			} catch (ServerException e) {				
				throw e;
			}
			
		}
	  public static void syncAnswers(Context ctx, DBHelper helper) throws SQLException,ServerException{
			
			List<AnswerItem> list = null;
			List<AnswerItem> tempList = null;
			Dao<AnswerItem, Integer> dao = null;
			
			try {
				
				dao = helper.getAnswersDao();
				tempList = getAnswers(helper);
				list = getAnswersFromServer(ctx);
				
				dao.deleteBuilder().where();
				dao.deleteBuilder().delete();
				
				insertAnswers(ctx,helper, list);
				
				
			} catch (SQLException e) {
				insertAnswers(ctx, helper,tempList);
				throw e;
			} catch (ServerException e) {
				insertAnswers(ctx,helper, tempList);
				throw e;
			}
			
		}
	public static void syncAnswersWin(Context ctx,DBHelper helper) throws SQLException,ServerException{
		
		List<AnswerWinItem> list = null;
		List<AnswerWinItem> tempList = null;
		Dao<AnswerWinItem, Integer> dao = null;
		
		try {
			
			dao = helper.getAnswersWinDao();
			tempList = getAnswersWin(helper);
			list = getAnswersWinFromServer(ctx);
			
			dao.deleteBuilder().where();
			dao.deleteBuilder().delete();
			
			insertAnswersWin(ctx, helper,list);
			
			
			
		} catch (SQLException e) {
			
			insertAnswersWin(ctx,helper, tempList);
			throw e;
			
		} catch (ServerException e) {
			insertAnswersWin(ctx,helper, tempList);
			throw e;
			
		}
		
}
	
	public static String[] getAnswersArray(DBHelper helper){
		String[] array = null;
		List<AnswerItem> list = null;
		
		try {
			list = getAnswers(helper);
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
	public static String[] getAnswersWinArray(DBHelper helper){
		String[] array = null;
		List<AnswerWinItem> list = null;
		
		try {
			list = getAnswersWin(helper);
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
public static List<AnswerWinItem> getAnswersWin(DBHelper helper) throws SQLException, ServerException{
		
		List<AnswerWinItem> result  = null;
	
		
		try {
			result = helper.getAnswersWinDao().queryForAll();
			
			
			 
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
		
	    
	}
public static List<AnswerItem> getAnswers(DBHelper helper) throws SQLException, ServerException{
		
		List<AnswerItem> result  = null;
	
		try {
			result = helper.getAnswersDao().queryForAll();			
			
			
		} catch (SQLException e) {
			throw e;
		
		}
		return result;
	   
		
	    
	}
	
	
	public static  void insertAnswers(Context context,DBHelper helper, List<AnswerItem> list)
			throws SQLException {

		try {
			if(list!=null){
			for (AnswerItem item : list) {
				helper.getAnswersDao().createOrUpdate(item);
			}
			}

		} catch (SQLException e) {
			Log.e(TAG, e.toString());
			throw e;

		}
	}

	public static  void insertAnswersWin(Context context,DBHelper helper,
			List<AnswerWinItem> list) throws SQLException {

		try {
			if(list!=null){
				for (AnswerWinItem item : list) {
					helper.getAnswersWinDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			throw e;

		}

	}


	public static  void updateAnswers(Context context,DBHelper helper, List<AnswerItem> list)
			throws SQLException {

		try {
			if(list!=null){
				for (AnswerItem item : list) {
					helper.getAnswersDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			throw e;

		}
	}

	public static  void updateAnswersWin(Context context,DBHelper helper,
			List<AnswerWinItem> list) throws SQLException {

		try {
			if(list!=null){
				for (AnswerWinItem item : list) {
					helper.getAnswersWinDao().createOrUpdate(item);
				}
			}
			

		} catch (SQLException e) {
			throw e;

		}
	}

}
