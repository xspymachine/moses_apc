package com.xpluscloud.moses_apc.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.xpluscloud.moses_apc.R;
import com.xpluscloud.moses_apc.dbase.OutboxDbManager;
import com.xpluscloud.moses_apc.util.DbUtil;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

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
		OkHttpClient client = new OkHttpClient();
	
		try {
			switch(opt){
				case 1: long dateFrom = DbUtil.dateTomilli(from);
					  	long dateTo = 86400000+DbUtil.dateTomilli(to);
					  
					  	OutboxDbManager db2 = new OutboxDbManager(context);
					  	db2.open();
					  	db2.resendDateRange2(cmd,dateFrom,dateTo);
					  	db2.close();
						break;
				case 2:	HttpClient client2 = new DefaultHttpClient();
						ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			            String apik=context.getResources().getString(R.string.apik);
//			            nameValuePair.add(new BasicNameValuePair("api", apik));
//			            nameValuePair.add(new BasicNameValuePair("devid", devid));
//			            nameValuePair.add(new BasicNameValuePair("sourceTable", sourceTable));
//			            nameValuePair.add(new BasicNameValuePair("data", data.toString()));
//
//			            post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
//			        	HttpResponse response = client.execute(post);
//
//			            StatusLine statusLine = response.getStatusLine();
//			            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
//			                result = EntityUtils.toByteArray(response.getEntity());
//			                str = new String(result, "UTF-8");
//			                Log.e("Resend Response: ", str);
//			            }

					RequestBody body = new  FormBody.Builder()
							.add("devid", devid)
							.add("sourceTable", sourceTable)
							.add("data", data.toString())
							.add("apik", apik).build();
					Request request = new Request.Builder()
							.url(url).post(body).build();

					client = getUnsafeOkHttpClient();
					Response response = client.newCall(request).execute();
					String result2 = response.body().string();
					Log.e("result",result2);
					str = result2;
//			JsonParser.InsertData(context,Table,result);
					if (!response.isSuccessful()) throw new IOException("Unexpected code" + response.toString());
						break;
			}		
			
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
		String msg = null;
		
		
		if(progressDialog.isShowing()) {
			progressDialog.dismiss();
			msg = "Resending Message Success";
		}
		
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
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

