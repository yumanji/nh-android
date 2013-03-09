package com.movetothebit.newholland.android.utils;

import android.content.Context;

public class FileHelper {
	
	public static String pathFromRaw(Context ctx,int id){
		return "android.resource://" + ctx.getPackageName() + "/" + id;
	}
	public static String pathFromRaw(Context ctx,String name){
		return "android.resource://" + ctx.getPackageName() + "/raw" + name;
	}
}
