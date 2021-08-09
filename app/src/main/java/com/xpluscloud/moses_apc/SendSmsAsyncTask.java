package com.xpluscloud.moses_apc;

import android.content.Context;
import android.os.AsyncTask;

public class SendSmsAsyncTask extends AsyncTask<Integer, Integer, Integer> {
	
	Context _context;
	Integer _id;
	String _recipient;
	String _message;
	
    public SendSmsAsyncTask(Context context, Integer id, String recipient, String message) {
    	_context 	= context;
    	_id 		= id;
    	_recipient 	= recipient;
    	_message 	= message;
    } 

    @Override
	protected Integer doInBackground(Integer... params) {
		// TODO Auto-generated method stub    	
    	SmsSender.send(_context, _id, _recipient, _message);    	    	
		return null;
	} 
 
       
 
    @Override
    protected void onPostExecute(Integer result) {
        if (result != null) { 
            // do something 
        } else { 
            // error occured 
        } 
    }
	
} 