package com.xpluscloud.moses_apc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class AsyncHttpApproval extends AsyncTask<String, String, String> {
	private JSONArray data = null;// post data
	private String devid = null;
	private String sourceTable=null;
	
	
	Context context;
	
     public AsyncHttpApproval(Context _context, JSONArray _data, String _devid, String _sourceTable) {
        data = _data; 
        devid = _devid;
        context = _context;
        sourceTable= _sourceTable;
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
            nameValuePair.add(new BasicNameValuePair("data", data.toString())); 
            
            post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
        	HttpResponse response = client.execute(post); 
            
            StatusLine statusLine = response.getStatusLine(); 
            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
                result = EntityUtils.toByteArray(response.getEntity()); 
                str = new String(result, "UTF-8");
                Log.e("Response: ", str);
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
} 