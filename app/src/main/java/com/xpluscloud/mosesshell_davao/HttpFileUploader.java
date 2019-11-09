package com.xpluscloud.mosesshell_davao;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xpluscloud.mosesshell_davao.util.Master;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpFileUploader implements Runnable {
	
	public final String organization= Master.COMPANY_NAME;
	
	URL connectURL;
	String params;
	String responseString;
	String serverfilename;
	byte[] dataToServer;
	FileInputStream fileInputStream = null;
	
	Context context;
	public String devId;
	
	
	HttpFileUploader(String urlString, String params, String serverfilename ){
		try{
			
			connectURL = new URL(urlString);
			this.params = params+"=";
			this.serverfilename = serverfilename;
		}catch(Exception ex){
			Log.i("URL FORMATION","MALFORMATED URL");
		}
	}


	public String doStart(FileInputStream stream, Context contxt){
		fileInputStream = stream;
		context = contxt;
		return UploadFile();
	} 

	
	private String UploadFile(){
		String Tag="Send File";
		String Response="";
				
		String androidId = (Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
	    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	  	devId = telephonyManager.getDeviceId();
		if (devId=="") devId=androidId;
		try	{
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			
			//------------------ CLIENT REQUEST
		
			Log.e(Tag,"Starting client request");
			// Open a HTTP connection to the URL
		
			HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
		
			// Allow Inputs
			conn.setDoInput(true);
		
			// Allow Outputs
			conn.setDoOutput(true);
		
			// Don't use a cached copy.
			conn.setUseCaches(false);
		
			// Use a post method.
			conn.setRequestMethod("POST");
		
			conn.setRequestProperty("Connection", "Keep-Alive");
		
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
		
			DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );
			
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"org\"" + lineEnd+ lineEnd);
			dos.writeBytes(organization + lineEnd);
			
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"devid\"" + lineEnd+ lineEnd);
			dos.writeBytes(devId + lineEnd);		
			
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + serverfilename +"\"" + lineEnd);
			dos.writeBytes("Content-Type:image/jpg"+ lineEnd);
			dos.writeBytes(lineEnd);
			
			Log.e(Tag,"Headers are written");
		
			// create a buffer of maximum size
		
			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];
		
			// read file and write it into form...
		
			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		
			while (bytesRead > 0){
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
		
			// send multipart form data necesssary after file data...
		
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		
			// close streams
			Log.e(Tag,"File is written");
			fileInputStream.close();
			dos.flush();
		
			InputStream is = conn.getInputStream();
			// retrieve the response from server
			int ch;
		
			StringBuffer b =new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
			b.append( (char)ch );
			}
			 Response=b.toString(); 
			Log.i("Response",Response);
			dos.close();		
		
			}
			catch (MalformedURLException ex){
				Log.e(Tag, "error: " + ex.getMessage(), ex);
			}
		
			catch (IOException ioe)	{
				Log.e(Tag, "error: " + ioe.getMessage(), ioe);
			}
		
		return Response;
		
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
		
}

