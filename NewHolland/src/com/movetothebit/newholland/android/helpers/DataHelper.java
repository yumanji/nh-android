package com.movetothebit.newholland.android.helpers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.movetothebit.newholland.android.R;
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
	public static final String GET_ANSWERS = "getanswers/";
	public static final String GET_ANSWERS_WIN = "getanswerswin/";
	public static final String GET_MODELS = "getmodels/";
	public static final String GET_SURVEY= "getsurvey/";
	public static final String SET_SURVEY= "setsurvey/";
	public static final String LOGIN= "login/";
	 
	public static boolean  sendInscriptions(Context ctx, List<InscriptionData> list ) throws ServerException  {
	  	
	  	
		String jsonUrl = null;
		String response = null;
		SharedPreferences prefs = null;		
		
		try {
			
			prefs = ctx.getSharedPreferences(ctx.getString(R.string.app_preferences), 0);	
			jsonUrl = URL+SET_SURVEY+prefs.getString(userData, "0");
			Gson gson = new Gson();
			JSONObject jsonObject = new JSONObject();	
			
			JSONArray data = new JSONArray();
			 for(InscriptionData item :list){
				 JSONObject obj = new JSONObject(gson.toJson(item));
				 data.put(obj);
			 }
//	        for (int i=0;i<data.length();i++){ 
//				JSONObject object = data.getJSONObject(i);
//				object.remove(MACHINE_TYPE);
//				object.remove(BRAND);
//				object.remove(COMMERCIAL_MODEL);
//				object.remove(MONTH);
//				object.remove(YEAR);
//				object.remove(HP);
//				object.remove(SEGMENT_MODEL);
//				object.remove(PROVINCE);
//				object.remove(POPULATION);
//				object.remove(DEALER_NAME);
//				object.remove(ID_SALESMAN);
//				object.remove(SALESMAN_NAME);
//				object.remove(MODEL_EQUAL);
//				object.remove(MODEL3);
//				object.remove(SEGMENT_HP);
//				object.remove(AREA);
//				object.remove(SEGMENT_HP);
//			}
			jsonObject.put("data", data);
			
//			response = RemoteFacade.sendJsonToUrl(jsonUrl, jsonObject.toString());
			response = RemoteFacade.postStringToUrl(jsonUrl, jsonObject.toString());
			JSONObject jsonResponse = new JSONObject(response);
			JSONObject errorJson = jsonResponse.getJSONObject("error");
			
			if(errorJson.getString("code").equals("0")){
				return true;
			}else{
				throw new ServerException(new Exception(), errorJson.getString("message"));
			}
			 
		} catch (NotFoundException e) {				
			throw new ServerException(e, ctx.getString(R.string.refresh_exception));
		}  catch (JsonSyntaxException e) {
			 throw new ServerException(e, ctx.getString(R.string.refresh_exception));
		} catch (JSONException e) {
			throw new ServerException(e, ctx.getString(R.string.refresh_exception));
		} 
    
 }
	
	
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
	  
	  public static  boolean  doLogin(Context ctx, String user, String pass) throws ServerException {
		  	
		  
			
			String json = null;			
			SharedPreferences settings = null;		
			
			try {
				
				settings = ctx.getSharedPreferences(ctx.getString(R.string.app_preferences), 0);				
				json = RemoteFacade.stringFromServer(URL+LOGIN+user+"/"+pass);
				JSONObject jsonObject = new JSONObject(json);
				JSONObject statusObject = jsonObject.getJSONObject("error");
				
				if(statusObject.getString("code").equals("0")){					
					
					settings.edit().putBoolean(isLogged,true).commit();					
					settings.edit().putString(userData, user).commit();
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
		  

		  
	
}
