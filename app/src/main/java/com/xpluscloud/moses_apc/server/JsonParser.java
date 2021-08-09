package com.xpluscloud.moses_apc.server;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.xpluscloud.moses_apc.dbase.DataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Iterator;


public class JsonParser {
	
	public static void InsertData(Context context, String Table, String data)
			throws JSONException {
		
		Object json = new JSONTokener(data).nextValue();
		
		if (json instanceof JSONObject) {
			JSONObject jo = (JSONObject) new JSONTokener(data).nextValue();
			InsertJson(context,Table,jo);
		}
		else if (json instanceof JSONArray){
			JSONArray ja = new JSONArray(data);
			
			try {
				DataManager db = new DataManager(context);
				db.open();
				db.beginTransaction();
				for(int i = 0; i < ja.length(); i++) {
					JSONObject jo = ja.getJSONObject(i);
					ContentValues cv = InsertJson(context,Table,jo);
//                    InsertJson(context,Table,jo);
					Long id = db.InsertData(Table, cv);
					Log.w("InsertData ID",""+id);
				}
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static ContentValues InsertJson(Context context, String Table, JSONObject jo) throws JSONException {
		
		
		Iterator<?> keys = jo.keys();
		
		ContentValues cv = new ContentValues();
        while( keys.hasNext() ){
            String key = (String)keys.next();
           	String value = jo.getString(key);
			if(Table.equals("customers") && key.equals("status") && value.equals("0")) value = "1";
            cv.put(key, value);
            Log.d("Key/Value",key +"/"+ value);
        }
        return cv;
//        Log.d("Table",Table);
       
//        DataManager db = new DataManager(context);
//		db.open();
//        Long id = db.InsertData(Table, cv);
//        db.close();
        
        
        
//       Log.w("InsertData ID",""+id);
	}
			
	
}
