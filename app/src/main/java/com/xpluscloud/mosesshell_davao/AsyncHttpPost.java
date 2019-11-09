package com.xpluscloud.mosesshell_davao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.xpluscloud.mosesshell_davao.dbase.OutboxDbManager;
import com.xpluscloud.mosesshell_davao.dbase.UtilDbManager;
import com.xpluscloud.mosesshell_davao.getset.Outbox;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class AsyncHttpPost extends AsyncTask<String, String, String> {
	private JSONArray data = null;// post data
	private String devid = null;
	private String sourceTable=null;
	private String version = null;
	
	
	Context context;
	
     public AsyncHttpPost(Context _context, JSONArray _data, String _devid, String _sourceTable, String _version) {
        data = _data; 
        devid = _devid;
        context = _context;
        sourceTable= _sourceTable;
        version = _version;
    } 
	
 
	@Override
    protected String doInBackground(String... params) {
    	
        byte[] result = null; 
        String str = "";
        HttpClient client = new DefaultHttpClient(); 
        HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL 
        try { 
            // set up post data 
            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String apik=context.getResources().getString(R.string.apik);
            nameValuePair.add(new BasicNameValuePair("api", apik)); 
            nameValuePair.add(new BasicNameValuePair("devid", devid)); 
            nameValuePair.add(new BasicNameValuePair("sourceTable", sourceTable)); 
            nameValuePair.add(new BasicNameValuePair("data", data.toString()));
            nameValuePair.add(new BasicNameValuePair("version", version)); 
            
            post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
        	HttpResponse response = client.execute(post); 
            
            StatusLine statusLine = response.getStatusLine(); 
            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
                result = EntityUtils.toByteArray(response.getEntity()); 
                str = new String(result, "UTF-8");
                Log.e("Response: ",params[0]+"-----"+ str);
            }
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace(); 
        } 
        catch (Exception e) {
        	Log.e("AsyncHttpPost ", "Exception error!");
        } 
        return str; 
    } 
    
    @Override
    protected void onPostExecute(String result) {
		if (result != null && result !="") {
			Log.d("PostExecute: ", result);
			
			if(result.contains("Data uploaded successfully!")) {			
				String strId="";
				Integer id=0;
				for(int i = 0; i < data.length(); i++) {
					JSONObject jo;
					try {
						jo = data.getJSONObject(i);
						strId = jo.getString("_id");
						id = Integer.valueOf(strId);
						Log.d("Updating Status:","ID#: "+id);
						
						UtilDbManager udb = new UtilDbManager(context);
						udb.open();
						udb.updateStatus(sourceTable, id, 1);
						udb.close();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
            else{
                Log.e("result", result + "trying to send data via SMS");
                OutboxDbManager db = new OutboxDbManager(context);
                db.open();
                Outbox pendingMsgs = db.getPendingMsg(10);
                db.close();
                if(pendingMsgs == null)
                    return;
                //for (Outbox outbox : pendingMsgs) {
                SendSmsAsyncTask asyncSend = new SendSmsAsyncTask(context, pendingMsgs.getId(), pendingMsgs.getRecipient(), pendingMsgs.getMessage());
                asyncSend.execute();
            }
    	}
		else {
//	    	Log.e("PostExecute: ", "Data upload failed!");
//          Log.e("result", result + "trying to send data via SMS");
            OutboxDbManager db = new OutboxDbManager(context);
            db.open();
            Outbox pendingMsgs = db.getPendingMsg(10);
            db.close();
            if(pendingMsgs == null)
                return;
            //for (Outbox outbox : pendingMsgs) {
            SendSmsAsyncTask asyncSend = new SendSmsAsyncTask(context, pendingMsgs.getId(), pendingMsgs.getRecipient(), pendingMsgs.getMessage());
            asyncSend.execute();
	    }
    }
} 