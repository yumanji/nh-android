package com.movetothebit.newholland.android.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.afree.data.time.Month;

public class DateHelper {

		
	public static String[] getLastMonths(int index){
		String[] lastMonths = new String[index];
		SimpleDateFormat format = new SimpleDateFormat("MMM yyyy",new Locale("es", "ES"));

		Calendar cal = Calendar.getInstance();
		
		for(int i= 0;i<index;i++){
			if(i==0) {
				lastMonths[i] = format.format(cal.getTime()).toUpperCase(new Locale("es", "ES"));
			}else{
				cal.add(Calendar.MONTH, -1);
				lastMonths[i] = format.format(cal.getTime()).toUpperCase(new Locale("es", "ES"));
			}
			
		}		
		
		return lastMonths;
		
	}
	
	public static Month getMonthFromStrings(String month, String year){
		DateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy",new Locale("es", "ES"));
		Month result =null;
		try {
			Date date = sdf.parse("01/"+month+"/"+year);
			result = new Month(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return result;
	}
}
