package com.movetothebit.newholland.android.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.model.Item;

public class AppHelper {
	public class Constants {
		 
		public static final String CONSUMER_KEY = "5LqODwPNSPYirczrUIFL4g";
		public static final String CONSUMER_SECRET= "pBBMBR5wOOggNaFQNsz8wwvcQ5psfcsrhFNEwTOc";
	 
		public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
		public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
		public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";
	 
		final public static String	CALLBACK_SCHEME = "hazte";
		final public static String	CALLBACK_URL = CALLBACK_SCHEME + "://twitter";
	 
	}

	public static Date updateDate;
	public static final int NO_NET=0;
	public static final int GPRS=1;
	public static final int CONNECT=2;
	public static final int FILTER_INTENT=100;
	public static final int TWITTER_INTENT=200;
	public static  int activity_load;
	public static Location lastLocation;
	public static List<Item> myOrder = new ArrayList<Item>();
	

	public static List<InscriptionData> myLoadData = new ArrayList<InscriptionData>();
	
	public static int getIndexOfModels(Context ctx, String input){
		
	
		List<String> list = Arrays.asList(ctx.getResources().getStringArray(R.array.models));
		
		return list.indexOf(input);
		
	}
	public static int getIndexOfMissingOperation(Context ctx, String input){
		
		
		List<String> list = Arrays.asList(ctx.getResources().getStringArray(R.array.missing_operation));
		
		return list.indexOf(input);
		
	}
	public static List<InscriptionData> getMyLoadData() {
		return myLoadData;
	}
	public static void setMyLoadData(List<InscriptionData> myLoadData) {
		AppHelper.myLoadData = myLoadData;
	}
	public static void addOrder(Item item){
		myOrder.add(item);
	}
	public static void cleanOrder(){
		myOrder.clear();
	}
	
	public static List<Item> getMyOrder() {
		return myOrder;
	}

	public static void setMyOrder(List<Item> myOrder) {
		AppHelper.myOrder = myOrder;
	}

	public static Date getUpdateDate() {
		return updateDate;
	}

	public static void setUpdateDate(Date updateDate) {
		AppHelper.updateDate = updateDate;
	}

	public static boolean isNetworkAvailable(Context ctx) {

		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
		
	public static boolean isLocationAvailable(Context ctx) {
			 
		LocationManager location = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		
			   if (location != null) {
				    return true;
			   }else{
				   return false;
			   }
			   
		}
	
	public static boolean isGPSAvailable(Context ctx) {
		 
		LocationManager location = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);			
			   if (location != null) {
				   if ( !location.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
				        return true;
				    }else{
				    	return false;
				    }					   
			   }else{
				   return false;
			   }				   
		}
	
	//getters and setters
	
	

	public static Location getLastLocation() {
		return lastLocation;
	}

	public static void setLastLocation(Location lastLocation) {
		AppHelper.lastLocation = lastLocation;
	}

	public static int getActivity_load() {
		return activity_load;
	}

	public static void setActivity_load(int activity_load) {
		AppHelper.activity_load = activity_load;
	}
}
