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
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.db.DBHelper;
import com.movetothebit.newholland.android.model.AnswerItem;
import com.movetothebit.newholland.android.model.AnswerWinItem;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.model.ModelItem;
import com.movetothebit.newholland.android.utils.RemoteFacade;
import com.movetothebit.newholland.android.utils.ServerException;
import com.movetothebit.newholland.android.utils.SystemException;
import com.movetothebit.newholland.android.utils.lConstants;

public class DataHelper implements lConstants{
	
	public static final String TAG = "Datahelper";
	public static final String URL = "http://nh.movetothebit.com/index.php/comms/";
	public static final String GET_ANSWERS = "getanswers";
	public static final String GET_ANSWERS_WIN = "getanswerswin";
	public static final String GET_MODELS = "getmodels";
	public static final String GET_SURVEY= "getsurvey";
	
	 
   
	
	
	 public static List<AnswerWinItem>  getAnswersWinFromServer(Context ctx) throws ServerException  {
		  	
		  	List<AnswerWinItem> list = null;
			String jsonUrl = "http://nh.movetothebit.com/index.php/comms/getanswerswin/1";
			String json = null;
			Gson gson = null;
			
			try {
							
				json = RemoteFacade.stringFromServer(jsonUrl);
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
			String jsonUrl = "http://nh.movetothebit.com/index.php/comms/getanswers/1";
			String json = null;
			Gson gson = null;
			
			try {
							
				json = RemoteFacade.stringFromServer(jsonUrl);
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

	  public static  List<ModelItem>  getModelsFromServer(Context ctx) throws ServerException {
		  	
		  List<ModelItem> list = null;
			String jsonUrl = "http://nh.movetothebit.com/index.php/comms/getmodels/1";
			String json = null;
			Gson gson = null;
			
			try {
							
				json = RemoteFacade.stringFromServer(jsonUrl);
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
	  
	  public static  boolean  doLogin(Context ctx, String user, String pass) throws ServerException {
		  	
		  
			String jsonUrl = "http://nh.movetothebit.com/index.php/comms/login/";
			String json = null;			
			SharedPreferences 	settings = null;
			try {
							
				settings =ctx.getSharedPreferences(ctx.getString(R.string.app_preferences), 0);
				json = RemoteFacade.stringFromServer(jsonUrl+user+"/"+pass);
				JSONObject jsonObject = new JSONObject(json);
				JSONObject statusObject = jsonObject.getJSONObject("error");
				
				if(statusObject.getString("code").equals("0")){
					
					
					settings.edit().putBoolean(isLogged,true).commit();					
					
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					JSONObject param =(JSONObject) jsonArray.get(0);
					
					settings.edit().putString(userReadOnly,param.get("value").toString()).commit();
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
			return true;
	     
	 }
		  public static   List<InscriptionData> getIncriptionsFromServer(Context ctx) throws ServerException{
				
			  List<InscriptionData> list = null;
				String jsonUrl = "http://nh.movetothebit.com/index.php/comms/getsurvey";
				String json = null;
				Gson gson = null;
				
				try {
								
					json = RemoteFacade.stringFromServer(jsonUrl+"/1");
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
		  

		  
	
}
