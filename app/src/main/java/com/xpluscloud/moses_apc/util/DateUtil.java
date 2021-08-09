package com.xpluscloud.moses_apc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	public static String phShortDate(Long dateMillis) {
		Date resultdate = new Date(dateMillis);
		
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		return df.format(resultdate); 
		
	}
	
	public static String phShortDateTime(Long dateMillis) {
		Date resultdate = new Date(dateMillis);
		
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
		return df.format(resultdate); 
		
	}
	
	public static String phLongDate(Long dateMillis) {
		Date resultdate = new Date(dateMillis);
		
		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
		return df.format(resultdate); 
		
	}
	public static String phLongDay(Long dateMillis) {
		Date resultdate = new Date(dateMillis);

		SimpleDateFormat df = new SimpleDateFormat("MMMM dd", Locale.US);
		return df.format(resultdate);

	}
	
	public static String phLongDateTime(Long dateMillis) {
		Date resultdate = new Date(dateMillis);
		
		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.US);
		return df.format(resultdate); 
		
	}
	
	public static String hour12(Calendar cal) {
		
		SimpleDateFormat df = new SimpleDateFormat("hh:mm a", Locale.US);
		return df.format(cal.getTime()); 
		
	}
	
	public static String strDate(Long dateMillis) {
		Date resultdate = new Date(dateMillis);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		return df.format(resultdate); 
		
	}
	
	public static String strDateTime(Long dateMillis) {
		Date resultdate = new Date(dateMillis);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
		return df.format(resultdate); 
		
	}
	
	public static String shortDateToLongDate(String dateTime){
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
		Date newDate = null;
		try {
			newDate = format.parse(dateTime);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		    
//		long contime = newDate.getTime();
		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.US);
		return df.format(newDate); 
	}
	
	public static String updateTime(int hours, int mins) {
		String timeset = "";
		if(hours>12){
			hours -= 12;
			timeset = "PM";
		}else if(hours == 0){
			hours += 12;
			timeset = "AM";
		}else if(hours == 12) timeset = "PM";
		else timeset = "AM";
		
		String minutes = "";
		if(mins<10) minutes = "0" + mins;
		else minutes = ""+mins;
		
		String aTime = hours + ":" + minutes + " " + timeset;
		return aTime;		
	}
	
	public static String strDateMY(Long dateMillis) {
		Date resultdate = new Date(dateMillis);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		return df.format(resultdate); 
		
	}

	public static String hour12mill(Long dateMillis) {
		Date resultdate = new Date(dateMillis);

		SimpleDateFormat df = new SimpleDateFormat("hh:mm a", Locale.US);
		return df.format(resultdate);

	}

	public static String reformatDate(String date){
		SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		long contime = newDate.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		return df.format(newDate);
	}

	public static String reformatDay(String date){
		SimpleDateFormat format = new SimpleDateFormat("MMMMM dd", Locale.US);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		long contime = newDate.getTime();
		SimpleDateFormat df = new SimpleDateFormat("MM-dd", Locale.US);
		return df.format(newDate);
	}
	public static String reformatDay2(String date){
		SimpleDateFormat format = new SimpleDateFormat("MM-dd", Locale.US);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		long contime = newDate.getTime();
		SimpleDateFormat df = new SimpleDateFormat("MMMM dd", Locale.US);
		return df.format(newDate);
	}

	public static String reverseDate(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		long contime = newDate.getTime();
		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
		return df.format(newDate);
	}

	public static long getLongDate(String date){
		SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		long contime = newDate.getTime();
		return newDate.getTime();
	}

	public static String DateToStr(String dateTime){
		SimpleDateFormat format = new SimpleDateFormat("MM-yyyy", Locale.US);
		Date newDate = null;
		try {
			newDate = format.parse(dateTime);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		long contime = newDate.getTime();
		SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy",Locale.US);
		return df.format(newDate);
	}

	public static String birthDateOnly(Long dateMillis) {
		Date resultdate = new Date(dateMillis);

		SimpleDateFormat df = new SimpleDateFormat("-MM-dd",Locale.US);
		return df.format(resultdate);

	}
}
