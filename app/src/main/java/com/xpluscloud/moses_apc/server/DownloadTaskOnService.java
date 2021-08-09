package com.xpluscloud.moses_apc.server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.xpluscloud.moses_apc.LostPasswordActivity;
import com.xpluscloud.moses_apc.R;
import com.xpluscloud.moses_apc.dbase.CustomerDbManager;
import com.xpluscloud.moses_apc.dbase.WipeDataDbManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;

public class DownloadTaskOnService extends AsyncTask<String, Void, Boolean> {
	
	private Context context;
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private int NOTIFICATION_ID = 1;
	
	String result;
	String ccode="";
	
	public DownloadTaskOnService(Context context) {
		this.context = context;
		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	@Override
	public Boolean doInBackground(String... params) {
		Boolean completed = Boolean.FALSE;
		String url = params[0];
		String Table = params[1];
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("devid", params[2]));
		postParameters.add(new BasicNameValuePair("apik", params[3]));
		postParameters.add(new BasicNameValuePair("ccode", params[4]));
		ccode = params[4];
		
		try {
			result = CustomHttpClient.executeHttpPost(url, postParameters);
			Log.e("result",result);
			
			JsonParser.InsertData(context,Table,result);
			
			completed = Boolean.TRUE;
		} catch(IOException ioe) {
			
		} catch(IllegalStateException ise) {
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	
		return completed;
	}
	
	@Override
	protected void onPostExecute(Boolean result1) {
		// TODO Auto-generated method stub
//		super.onPostExecute(result);
		String newResult = ""+result;
		if(newResult.contains(",")){
			CustomerDbManager db = new CustomerDbManager(context);
			db.open();
			String cusName = db.getCustomerName(ccode);
			db.close();
			createNotification("Customer Approved",cusName+" has been approved");
		}
		if(newResult.trim().equals("1") && !newResult.contains("[]")) {
			Log.e("wipedata", "true");
			WipeDataDbManager db = new WipeDataDbManager(context);
			db.open();
			db.dropTables();
			db.close();
		}
		
		if(newResult.trim().equals("2") && !newResult.contains("[]")) {
			Log.e("wipedata", "true");
			showNotification("Lost Password Creator","MoiSeS 9",1);
		}
		
		if(newResult.trim().contains("oldversion")){
			Log.e("newversionn", "true");
			showNotification("New version is available","Please update your STAT application ASAP get the latest version",2);
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void createNotification(String contentTitle, String contentText) {
		 
        //Build the notification using Notification.Builder
        Notification.Builder builder = new Notification.Builder(context)
        .setSmallIcon(R.drawable.moses396)
        .setAutoCancel(true)
        .setContentTitle(contentTitle)
        .setContentText(contentText);
 
        //Get current notification
        mNotification = builder.getNotification();
 
        //Show the notification
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }
	
	@SuppressWarnings("deprecation")
	private void showNotification(String message, String title, int code) {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = message;

        // Set the icon, scrolling text and timestamp
       Notification notification = new Notification(R.drawable.moses396, "MoiSeSx",
                System.currentTimeMillis());
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        Intent intent = null;
        switch(code){
        	case 1: {
        		intent = new Intent(context, LostPasswordActivity.class);
        		break;
        	}
        	case 2: {
        		Uri uri = Uri.parse(" https://moses.xpluscloud.com/app/delvaz"); // missing 'http://' will cause crashed
        		intent = new Intent(Intent.ACTION_VIEW, uri);
        		break;
        	}
        }        
        
        intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
         //The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(((ContextWrapper) context).getBaseContext(), 0,
              intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Set the info for the views that show in the notification panel.
//        notification.setLatestEventInfo(context, title,
//                      text, contentIntent);
		Notification.Builder builder = new Notification.Builder(context);

		builder.setAutoCancel(false);
		builder.setContentTitle(title);
		builder.setContentText(text);
		builder.setContentIntent(contentIntent);
		builder.setOngoing(true);
		builder.setNumber(100);
		builder.build();
		notification = builder.getNotification();
		// Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNotificationManager.notify(R.string.app_name, notification);
    }
}

