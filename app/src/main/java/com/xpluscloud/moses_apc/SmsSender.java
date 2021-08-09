package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

import static com.xpluscloud.moses_apc.util.DbUtil.updateStatus;


public final class SmsSender {
	static Integer counter=1;
	static Integer messageCount=0;
	
	static void send(final Context context, final Integer id, final String recipient, String smsMsg) {
    	String SENT = "SMS_SENT";
       // String DELIVERED = "SMS_DELIVERED"; 
        final Integer SUCCESS = 1;
        final Integer FAILED = 2;
        
        //ArrayList<PendingIntent> deliveryIntents;
        ArrayList<PendingIntent> sentIntents = null;
    	
    	try {
    		
    		String txtMsg = smsMsg + ";" + id;
    		
    		SmsManager sms = SmsManager.getDefault();
			ArrayList<String> msgStringArray = sms.divideMessage(txtMsg);
						
			messageCount = msgStringArray.size(); 			
			sentIntents = new ArrayList<PendingIntent>(messageCount);
			 
			 
			for (int j = 0; j < messageCount; j++) { 
			    Intent intent = new Intent(SENT+id);
				sentIntents.add(PendingIntent.getBroadcast(context, 0, intent , PendingIntent.FLAG_UPDATE_CURRENT));
			}			
			
			sms.sendMultipartTextMessage(recipient, null, msgStringArray, sentIntents, null);
			
			context.registerReceiver(new BroadcastReceiver(){
			    @Override
			    public void onReceive(Context context, Intent intent) {
			        switch (getResultCode()) { 
			            case Activity.RESULT_OK:
			            	updateStatus(context,id,SUCCESS);			            	
			            	Log.e("SMS Sent ", "Msg ID# (" + id + ") successfully sent to " + recipient + " Index/Size: " + counter +"/" + messageCount );
			                break; 
			            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			                //Toast.makeText(context.getApplicationContext(), "Sending failed! Try again...",  Toast.LENGTH_SHORT).show(); 
			            	Log.e("SMS Error ", "RESULT_ERROR_GENERIC_FAILURE!");
			                updateStatus(context,id,FAILED);
			                break; 
			            case SmsManager.RESULT_ERROR_NO_SERVICE:
			                //Toast.makeText(context.getApplicationContext(), "No signal from service provider!",  Toast.LENGTH_SHORT).show(); 
			            	Log.e("SMS Error ", "RESULT_ERROR_NO_SERVICE");
			                updateStatus(context,id,FAILED);
			                break; 
			            case SmsManager.RESULT_ERROR_NULL_PDU:
			                ///Toast.makeText(context.getApplicationContext(), "Message Error!", Toast.LENGTH_SHORT).show(); 
			            	Log.e("SMS Error ", "RESULT_ERROR_NULL_PDU");
			                updateStatus(context,id,FAILED);
			                break; 
			            case SmsManager.RESULT_ERROR_RADIO_OFF:
			            	Log.e("SMS Error ", "RESULT_ERROR_RADIO_OFF");
			                //Toast.makeText(context.getApplicationContext(), "Device is Offline!",    Toast.LENGTH_SHORT).show(); 
			                updateStatus(context,id,FAILED);
			                break; 
			        } 
			        
			       
			       if (counter>=messageCount) {			       
			    	   context.unregisterReceiver(this);
			    	   counter=0;
			       }
			        counter++;
			    } 
			}, new IntentFilter(SENT+id));
			

    		
    	} catch(Exception e) {
    		Log.e("ERROR: ", "There is a problem Sending SMS to " + recipient);
    	}
    }	
}