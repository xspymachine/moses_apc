package com.xpluscloud.moses_apc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.xpluscloud.moses_apc.dbase.OutboxDbManager;
import com.xpluscloud.moses_apc.dbase.UtilDbManager;
import com.xpluscloud.moses_apc.getset.Outbox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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

public class AsyncHttpPost extends AsyncTask<String, String, String> {
	private JSONArray data = null;// post data
	private String devid = null;
	private String sourceTable=null;
	private String version = null;
    OkHttpClient client = new OkHttpClient();
	
	
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
        try { 
            // set up post data
            String apik=context.getResources().getString(R.string.apik);

            RequestBody body = new  FormBody.Builder()
                    .add("devid", devid)
                    .add("sourceTable", sourceTable)
                    .add("version", version)
                    .add("data", data.toString())
                    .add("apik", apik).build();
            Request request = new Request.Builder()
                    .url(params[0]).post(body).build();

            client = getUnsafeOkHttpClient();
            Response response = client.newCall(request).execute();
            String result2 = response.body().string();
            Log.e("result",result2);
            str = result2;
            if (!response.isSuccessful()) throw new IOException("Unexpected code" + response.toString());
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace(); 
        } 
        catch (Exception e) {
        	Log.e("AsyncHttpPost ", "Exception error!");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
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