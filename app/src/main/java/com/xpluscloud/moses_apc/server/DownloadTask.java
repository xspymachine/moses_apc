package com.xpluscloud.moses_apc.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xpluscloud.moses_apc.GifCustomProgressDialog;
import com.xpluscloud.moses_apc.R;
import com.xpluscloud.moses_apc.dbase.UtilDbManager;
import com.xpluscloud.moses_apc.util.DbUtil;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
			client = getUnsafeOkHttpClient();
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
		} catch (Throwable throwable) {
			throwable.printStackTrace();
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
				new DownloadTaskFile(context, rootView, "Sales Materials").execute(url, strUrl.get(i).split("/")[1]);
			}
		}


//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	private OkHttpClient getUnsafeOkHttpClient() throws Throwable {

		try {
			TrustManager[] trustAllCerts = new TrustManager[]{(TrustManager)(new X509TrustManager(){

				@Override
				public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

				}

				@Override
				public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			})
			};

			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null,trustAllCerts, new SecureRandom());
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
			builder.hostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String s, SSLSession sslSession) {
					return true;
				}
			});

			builder.connectTimeout(60, TimeUnit.SECONDS);
			builder.readTimeout(60, TimeUnit.SECONDS);
			builder.writeTimeout(60, TimeUnit.SECONDS);

			return builder.build();


		} catch (Exception e){
			throw (Throwable)(new RuntimeException((Throwable) e));
		}

	}


}

