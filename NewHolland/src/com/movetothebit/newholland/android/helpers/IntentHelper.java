package com.movetothebit.newholland.android.helpers;

import java.io.File;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.movetothebit.newholland.android.utils.FileHelper;

public class IntentHelper {
//	public static void openIntentWebActivity(Context ctx, String url){
//	    	Intent i = new Intent(ctx, WebActivity.class);
//	    	Bundle extras = new Bundle();
//	    	extras.putString("url", url);
//	    	i.putExtras(extras);
//	    	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	    	ctx.startActivity(i);
//	}
	
//	public static void openIntentCalendar(Context context){
//		Intent i = new Intent(context, CalendarActivity.class);
//		context.startActivity(i);
//
//	}
	
	public static void openIntentPdf(Context context, int source){
		 System.out.println("path to pdf:   "+source);
		 
        Uri path = Uri.parse(FileHelper.pathFromRaw(context, source)); 
        System.out.println("uri pdf:  "+path.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
             context.startActivity(intent);
            } 
         catch (ActivityNotFoundException e) {
        	 e.printStackTrace();
             Toast.makeText(context, "No hay ninguna aplicacion para ver pdfs", Toast.LENGTH_LONG).show();
            }
	}
	public static void sendEmail(Context ctx, String email){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT,email);
		
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(Intent.createChooser(intent, "Email:"));	
		
	}
	public static void openIntentPdf(Context context, String name){
		
//		file:///android:asset/nh_1.pdf
//       Uri path = Uri.parse(FileHelper.pathFromRaw(context, name)); 
		 Uri path = Uri.parse("file:///android:asset/nh_1.pdf"); 
       System.out.println("uri pdf:  "+path.toString());
       Intent intent = new Intent(Intent.ACTION_VIEW);
       intent.setDataAndType(path, "application/pdf");
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

       try {
            context.startActivity(intent);
           } 
        catch (ActivityNotFoundException e) {
       	 e.printStackTrace();
            Toast.makeText(context, "No hay ninguna aplicacion para ver pdfs", Toast.LENGTH_LONG).show();
           }
	}
	public static void openIntentPdfFromSd(Context context, String name){
		
		
		 System.out.println("path to pdf:   "+name);
//		 Uri path = Uri.parse("android.resource://com.movetothebit.newholland.android/raw"+name);
		 Uri path = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+name));
		    System.out.println("uri pdf:  "+path.toString());
	     Intent intent = new Intent(Intent.ACTION_VIEW);
	     intent.setDataAndType(path, "application/pdf");
	     intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    

	     try {
	          context.startActivity(intent);
	         } 
	      catch (ActivityNotFoundException e) {
	     	 e.printStackTrace();
	          Toast.makeText(context, "No hay ninguna aplicacion para ver pdfs", Toast.LENGTH_LONG).show();
	         }
	}
	
	
}
