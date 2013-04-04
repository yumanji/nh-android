package com.movetothebit.newholland.android.helpers;

import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;

import com.google.gson.JsonSyntaxException;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.db.DBHelper;
import com.movetothebit.newholland.android.utils.RemoteFacade;
import com.movetothebit.newholland.android.utils.ServerException;
import com.movetothebit.newholland.android.utils.SystemException;
import com.movetothebit.newholland.android.utils.lConstants;

public class DataHelper implements lConstants{
	
	public static final String TAG = "Datahelper";
	
	
	
	public static void syncAllData(Context ctx, DBHelper helper) throws SQLException,ServerException{
		
		if(InscriptionHelper.sendInscriptions(ctx, helper)){
			syncData(ctx, helper);
		}
		
	}

	
	public static void downloadAllData(Context ctx, DBHelper helper) throws SQLException,ServerException{	
		
			InscriptionHelper.downloadInscriptions(ctx,helper);
			AnswersHelper.downloadAnswers(ctx, helper);
			AnswersHelper.downloadAnswersWin(ctx,helper);
			ModelHelper.downloadModels(ctx,helper);
		
	}
	
	public static void syncData(Context ctx, DBHelper helper) throws SQLException,ServerException{	
		
		InscriptionHelper.syncInscriptions(ctx,helper);
		AnswersHelper.syncAnswers(ctx, helper);
		AnswersHelper.syncAnswersWin(ctx,helper);
		ModelHelper.syncModels(ctx,helper);
	
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
					settings.edit().putInt(userReadOnly,Integer.valueOf(param.get("value").toString())).commit();
					
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
	
		  

		  
	
}
