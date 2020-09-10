package com.xpluscloud.mosesshell_davao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xpluscloud.mosesshell_davao.dbase.CustomerDbManager;
import com.xpluscloud.mosesshell_davao.dbase.OutboxDbManager;
import com.xpluscloud.mosesshell_davao.dbase.SettingDbManager;
import com.xpluscloud.mosesshell_davao.getset.Customer;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.Master;


public class SMSReceiver extends BroadcastReceiver {
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private String allMessages ="";
	
	Context context;
	
	private enum Command {	    
	    CMDGTW,		//Change Gateway
	    CMDSUS,		//Suspend
	    CMDSND,		//ReSend Outbox ID 
	    SNDCUS,		//Send Customer by ccode 
	    UPLCUS,		//Upload Customers(Set customer status to 20)
		DISTIM		//change the dispatch timer
	    ;
	}
	
	// @Override 
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("SMS Receiver Broadcast", "Intent received: " + intent.getAction());
		try{
		    if  (intent.getAction().equals(SMS_RECEIVED))  { 	        
		        Bundle bundle = intent.getExtras();
		        if (bundle != null) { 
		        	Object[] pdus = (Object[])bundle.get("pdus");
		        	final SmsMessage[] messages = new SmsMessage[pdus.length];
		        	for (int i = 0; i < pdus.length; i++) { 
	                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
	                    allMessages += messages[i].getMessageBody();
	                } 
		        	Log.i("Message Received",allMessages);
	                if (allMessages.length() > 6) {                     
	                    processCommand(context,messages[0].getOriginatingAddress(),allMessages);                    
	                } 
		        } 	 
		    } 	 
	    } catch(Exception e){
			Log.e("SMS Receiver", "Exception Error " + e.getMessage());
		};   
	}
	
	private void processCommand(Context context, String Sender, String inMsg) {
		Sender=Sender.substring(Sender.length()-10);	
		
		Log.i("Sender",Sender);
		if (Master.commanders.contains(Sender)) {
			String cmd[] = inMsg.split(" ");
			Log.w("Command",cmd[0]);
			try {
				Command command = Command.valueOf(cmd[0]);	
				Integer id;
				
				CustomerDbManager cdb = new CustomerDbManager(context);
				switch(command) {					
					case CMDGTW:
						String mobileno = cmd[1].substring(cmd[1].length()-10);
						String ccode;
						
						if (mobileno.matches("^[9]{1}[0-9]{9}$")) {
							mobileno = Master.COUNTRY_CODE + mobileno;
							
							SettingDbManager sdb = new SettingDbManager(context);
							sdb.open();									
							sdb.insert(Master.SMS_GATEWAY, mobileno, 0);
							sdb.close();
							
							Log.w("New Gateway",mobileno);
						}
						abortBroadcast();
						break;
					case CMDSUS:
						abortBroadcast();
						break;
					case CMDSND:
						//Update Outbox Status to 0
						Log.w("About to process ","CMDSND command");
						OutboxDbManager sdb = new OutboxDbManager(context);
						sdb.open();
						id = Integer.valueOf(cmd[1]);
						sdb.updateStatus(id,0);
						sdb.resetPriority(id);
						sdb.close();
						abortBroadcast();
						break;	
					case SNDCUS:
						ccode = cmd[1];	
						SendCustomer(context,ccode);						
						abortBroadcast();
						break;
					case UPLCUS:
						Integer status = Integer.valueOf(cmd[1]);
						
						cdb.open();
						cdb.setStatusAll(status);
						cdb.close();
						abortBroadcast();
						break;
					case DISTIM:
						String time = cmd[1];
						DbUtil.saveSetting(context,"distim",time);
						break;
					
				}
			}catch(Exception e) {
                Log.e("Command", "Exception Error");}
			
		}
		else {
			Log.e("Commander", Sender + " Not found");
		}
	}
	
	
	public void SendCustomer(Context context, String ccode) {
		
		CustomerDbManager db = new CustomerDbManager(context);
		db.open();		
		Customer c = db.getCustomerByCode(ccode);
		db.close();
		
		if (c!=null) {
			String sysTime = String.valueOf(System.currentTimeMillis()/1000);
			TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//			String devId = telephonyManager.getDeviceId();
			String devId = Master.getDevId2(context);
			
			String message = Master.CMD_ACUS + " " +
					devId 				+ ";" +
					c.getCustomerCode()	+ ";" +
					c.getName() 		+ ";" +
					c.getAddress() 		+ ";" +
					c.getCity() 		+ ";" +
					c.getState() 		+ ";" +
					c.getContactNumber() + ";" +
					c.getCplanCode() 	+ ";" +
					sysTime 			+ ";" +
					0;	
			Log.d("SendCustomer",message);
			DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
			
		}
	}	
	
	
	public void saveSetting(Context context, String Key, String Value) {
		abortBroadcast(); 		
	}
}