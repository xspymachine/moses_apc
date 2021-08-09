package com.xpluscloud.moses_apc.server;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

public class CustomHttpClient {
	
    public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds

    private static HttpClient httpClient;

    private static HttpClient getHttpClient() {
        if(httpClient == null) {
            httpClient = new DefaultHttpClient();
            final HttpParams params = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        }
        return httpClient;
    }
	
	public static String executeHttpPost(String url)
    throws Exception {
    	return executeHttpPost(url, null);
    }

    public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters)
    throws Exception {
    	final HttpClient client = getHttpClient();
    	final HttpPost request = new HttpPost(url);
        
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters, "UTF-8");
		request.setEntity(formEntity);
	
    	HttpResponse response = client.execute(request);
            
    	return read(response.getEntity().getContent());
    }

    public static String executeHttpGet(String url)
    throws Exception {
    	final HttpClient client = getHttpClient();
    	final HttpGet request = new HttpGet();
    	request.setURI(new URI(url));
            
    	HttpResponse response = client.execute(request);
       
        return read(response.getEntity().getContent());
    }
    
    private static String read(InputStream in)
    throws IOException {
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            StringBuilder sb = new StringBuilder("");
            String line = "";
            String NL = System.getProperty("line.separator");
            
            while ((line = br.readLine()) != null) {
                sb.append(line + NL);
            }
            
            String result = sb.toString();
            
            return result;
        } finally {
        	br.close();
        }
    }
}

