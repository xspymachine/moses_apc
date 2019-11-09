package com.xpluscloud.mosesshell_davao.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.xpluscloud.mosesshell_davao.R;
import com.xpluscloud.mosesshell_davao.dbase.OutboxDbManager;
import com.xpluscloud.mosesshell_davao.util.DbUtil;

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

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class ResendTask extends AsyncTask<String, Void, Boolean> {
	
	private Context context;
	private ProgressDialog progressDialog;
	
	private JSONArray data = null;// post data
	private String devid = null;
	private String sourceTable=null;
	private String from = null;
	private String to = null;
	private String cmd = null;
	
	public ResendTask(Context _context, JSONArray _data, String _devid, String _sourceTable, String _from, String _to, String _cmd) {
		data = _data; 
        devid = _devid;
        context = _context;
        sourceTable= _sourceTable;
        from = _from;
        to = _to;
        cmd = _cmd;
        
		progressDialog = new ProgressDialog(context);
		
	}
	
	@Override
	public void onPreExecute() {
		progressDialog.setMessage("Please wait...");
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
	}
	
	@Override
	public Boolean doInBackground(String... params) {
		Boolean completed = Boolean.FALSE;
		String url = params[0];
		int opt = Integer.parseInt(params[1]);
		
		byte[] result = null; 
        String str = "";
        
        HttpPost post = new HttpPost(url);// in this case, params[0] is URL 
	
		try {
			switch(opt){
				case 1: long dateFrom = DbUtil.dateTomilli(from);
					  	long dateTo = 86400000+DbUtil.dateTomilli(to);
					  
					  	OutboxDbManager db2 = new OutboxDbManager(context);
					  	db2.open();
					  	db2.resendDateRange2(cmd,dateFrom,dateTo);
					  	db2.close();
						break;
				case 2:	HttpClient client = new DefaultHttpClient(); 
						ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			            String apik=context.getResources().getString(R.string.apik);
			            nameValuePair.add(new BasicNameValuePair("api", apik)); 
			            nameValuePair.add(new BasicNameValuePair("devid", devid)); 
			            nameValuePair.add(new BasicNameValuePair("sourceTable", sourceTable)); 
			            nameValuePair.add(new BasicNameValuePair("data", data.toString())); 
			            
			            post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
			        	HttpResponse response = client.execute(post); 
			            
			            StatusLine statusLine = response.getStatusLine(); 
			            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
			                result = EntityUtils.toByteArray(response.getEntity()); 
			                str = new String(result, "UTF-8");
			                Log.e("Resend Response: ", str);
			            }
						break;
			}		
			
		} catch(IllegalStateException ise) {
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	
		return completed;
	}
	
	@Override
	public void onPostExecute(Boolean completed) {
		String msg = null;
		
		
		if(progressDialog.isShowing()) {
			progressDialog.dismiss();
			msg = "Resending Message Success";
		}
		
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}

