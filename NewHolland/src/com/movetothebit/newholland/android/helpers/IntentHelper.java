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

	
	public static void openIntentPdf(Context context, int source){

		 
        Uri path = Uri.parse(FileHelper.pathFromRaw(context, source)); 
     
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

		 Uri path = Uri.parse("file:///android:asset/nh_1.pdf"); 
     
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
		
		 Uri path = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+name));
		  
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
