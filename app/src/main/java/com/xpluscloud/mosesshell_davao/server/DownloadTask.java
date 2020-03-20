package com.xpluscloud.mosesshell_davao.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xpluscloud.mosesshell_davao.GifCustomProgressDialog;
import com.xpluscloud.mosesshell_davao.R;
import com.xpluscloud.mosesshell_davao.dbase.UtilDbManager;
import com.xpluscloud.mosesshell_davao.util.DbUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String, Void, Boolean> {
	
	private Context context;
	private ProgressDialog progressDialog;
    private View rootView;
    private String strDownload;
    private String option="";
	OkHttpClient client = new OkHttpClient();
	
	
	public DownloadTask(Context context, View rootView, String strDownload) {
		this.context = context;
        this.rootView=rootView;
        this.strDownload=strDownload;
		progressDialog = new ProgressDialog(context);
	}
	public DownloadTask(Context context, View rootView, String strDownload, String _option) {
		this.context = context;
		this.rootView=rootView;
		this.strDownload=strDownload;
		this.option = _option;
		progressDialog = new ProgressDialog(context);
	}
	
	@Override
	public void onPreExecute() {

        progressDialog = new GifCustomProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
	}
	
	@Override
	public Boolean doInBackground(String... params) {
		Boolean completed = Boolean.FALSE;
		String url = params[0];
		String Table = params[1];

		RequestBody body = new  FormBody.Builder()
				.add("devid", params[2])
				.add("apik", params[3]).build();
		Request request = new Request.Builder()
				.url(url).post(body).build();
	
		try {

			Log.e("link_url",url);
			Response response = client.newCall(request).execute();
			String result = response.body().string();
			Log.e("result",result);
//			JsonParser.InsertData(context,Table,result);
			if (!response.isSuccessful()) throw new IOException("Unexpected code" + response.toString());
			else {
				if(result.length()> 5 && !result.contains("A PHP Error was encountered")) {
					JsonParser.InsertData(context, Table, result);
					completed = Boolean.TRUE;
				}
			}
		} catch(IOException ioe) {
			
		} catch(IllegalStateException ise) {
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	
		return completed;
	}
	
	@Override
	public void onPostExecute(Boolean completed) {
		String msg = "";
		
		if(progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

		if(completed){
			msg = "Synchronizing "+strDownload+" Complete";
			DbUtil.makeToast(LayoutInflater.from(context),  msg, context,
					(ViewGroup) rootView.findViewById(R.id.custom_toast_layout),0);
		}
		else {
			msg = "Synchronizing "+strDownload+" Incomplete - Empty Result";
			DbUtil.makeToast(LayoutInflater.from(context),  msg, context,
					(ViewGroup) rootView.findViewById(R.id.custom_toast_layout),0,android.R.color.holo_red_light);
		}

		if(option.contains("BROCHURE")){

			UtilDbManager db = new UtilDbManager(context);
			db.open();
			ArrayList<String> strUrl = db.getBrochureURL();
			db.close();
			String url="";
			for(int i=0;i<strUrl.size();i++){
				if(strUrl.get(i).equals("")) return;
				url = "https://shellpromo.storage.googleapis.com/"+strUrl.get(i);
				new DownloadTaskFile(context, null, "Current Promotions").execute(url, strUrl.get(i).split("/")[1]);
			}
		}


//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}

