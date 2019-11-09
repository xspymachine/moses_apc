package com.xpluscloud.mosesshell_davao.util;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xpluscloud.mosesshell_davao.R;
import com.xpluscloud.mosesshell_davao.dbase.CustomerDbManager;
import com.xpluscloud.mosesshell_davao.dbase.OutboxDbManager;
import com.xpluscloud.mosesshell_davao.dbase.SettingDbManager;
import com.xpluscloud.mosesshell_davao.getset.Customer;
import com.xpluscloud.mosesshell_davao.getset.Outbox;
import com.xpluscloud.mosesshell_davao.getset.Setting;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DbUtil {  

  /**
   * Tag used on log messages. 
   */
public static final String TAG = "Db Util";
public static void updateStatus(Context context, Integer id, Integer status) {
		  	OutboxDbManager db = new OutboxDbManager(context);
			db.open();
			db.updateStatus(id, status);		
			db.close();
	}
  

  public static void saveMsg(final Context context, final String recipient, final String message) {
		
	  String dateTime = String.valueOf(System.currentTimeMillis()/1000);
		
		OutboxDbManager db = new OutboxDbManager(context);
		db.open();
		
		Outbox c = new Outbox();
		
		c.setDateTime(dateTime);
		c.setRecipient(recipient);
		c.setMessage(message);
		c.setStatus(0);
		
		db.AddMessage(c);
		
		db.close();
	} 
  
 
	  
  public static String getGateway(Context context) {
	  	SettingDbManager db = new SettingDbManager(context);
		db.open();
		String smsGateway = db.getSetting(Master.SMS_GATEWAY);
		db.close();
		
		if (smsGateway==null || smsGateway=="") smsGateway = Master.INIT_GATEWAY;
		
		return smsGateway;		
  }
  
  
 public static String phTime(Long dTime) {
	  SimpleDateFormat phd = new SimpleDateFormat("MM/dd/yy hh:mm:ss", Locale.ENGLISH); //For title display
	
	  return phd.format(dTime);	  
  }
  
 public static String sqlTime(Long dTime) {
	  SimpleDateFormat sqlTime = new SimpleDateFormat("yy-MM-dd hh:mm:ss", Locale.ENGLISH); //For title display
	  return  sqlTime.format(dTime);
  }
 
 public static String strDate(Long dateMillis) {
		Date resultdate = new Date(dateMillis);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		return df.format(resultdate); 
		
	}
  

 public static Customer getCustomerInfo(Context context, String cCode){
	 Customer ro;
	 
	 CustomerDbManager db = new CustomerDbManager(context);
	 
	 db.open();
	 ro = db.getCustomerByCode(cCode);
	 db.close();
	 
	 return ro;
	 
 }
 
 public static String getSetting(Context context, String key) {
		
		String s="";
		
		SettingDbManager db = new SettingDbManager(context);
		db.open();
		s = db.getSetting(key);
		db.close();
		
		return s;
	}
 
 public static void saveSetting(Context context, String key, String value) {
		
		Setting s = new Setting();
		s.setKey(key);
		s.setValue(value);
		
		SettingDbManager db = new SettingDbManager(context);
		db.open();
		db.AddSetting(s);
		db.close();
	}
 
 public static long dateTomilli(String strDate){
	 DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	 long futureTime = 0;
	 try {
	     Date date = format.parse(strDate);
	     futureTime = date.getTime();
	 } catch (ParseException e) {
	     Log.e("log", e.getMessage(), e);
	 }
	 return (futureTime/1000);
 }
 
 public static void changeDrawableColor(String color, Button btn, int position){
	 StateListDrawable mStateListDrawable = (StateListDrawable)btn.getBackground();
     DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) mStateListDrawable.getConstantState();
     Drawable[] children = drawableContainerState.getChildren();
     GradientDrawable selectedstateDrawable = (GradientDrawable) children[position];
     selectedstateDrawable.setColorFilter(Color.parseColor(color), Mode.MULTIPLY);
//     selectedstateDrawable.setStroke(5, Color.BLACK);
 }
 
 public static void makeToast(LayoutInflater mInflater, String message, Context context, ViewGroup container, int length){
	 LayoutInflater inflater = mInflater;
     View toastLayout = inflater.inflate(R.layout.custom_toast, container);
     TextView text = (TextView) toastLayout.findViewById(R.id.custom_toast_message);
     toastLayout.setTag(text);
     text.setText(message);
	 //length long=1 short=0
     Toast toast = new Toast(context);
     toast.setDuration(length);
     toast.setView(toastLayout);
     toast.show();
 }

	public static void makeToast(LayoutInflater mInflater, String message, Context context, ViewGroup container, int length,int color){
		LayoutInflater inflater = mInflater;
		View toastLayout = inflater.inflate(R.layout.custom_toast, container);
		TextView text = toastLayout.findViewById(R.id.custom_toast_message);
		toastLayout.setTag(text);
		toastLayout.setBackgroundResource(color);
		text.setText(message);
		//length long=1 short=0
		Toast toast = new Toast(context);
		toast.setDuration(length);
		toast.setView(toastLayout);
		toast.show();
	}

}
       