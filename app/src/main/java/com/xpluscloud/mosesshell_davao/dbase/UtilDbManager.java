package com.xpluscloud.mosesshell_davao.dbase;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UtilDbManager extends DbManager {
	
	public UtilDbManager(Context context) {
		super(context);
	}
	
	public void updateStatus(String table, Integer id, Integer status) {
		String sql="UPDATE " + table + " SET " +
				" status=" + status + 
				" WHERE _id=" + id;
		Log.d("Update sql",sql);
		db.execSQL(sql);
	}
	
	public JSONArray getJsonArray(String table, String where) {
		
		String sql = "SELECT * FROM " + table + " WHERE " + where +" ORDER BY _id ASC";
		Cursor c = db.rawQuery(sql,null);
		 
		JSONArray jaResult 	= new JSONArray();
		 
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			int totalColumn = c.getColumnCount();
			
			//Log.d("TAG_NAME", c.getColumnName(totalColumn-1));
			
	       	JSONObject rowObject = new JSONObject();
	       	for( int i=0 ;  i< totalColumn ; i++ ){
	       		if( c.getColumnName(i) != null ) {
	       			try {
	       				if( c.getString(i) != null ) {
	       					//Log.d("TAG_NAME", c.getString(i) );
	       					rowObject.put(c.getColumnName(i) ,  c.getString(i) );
	       				}
						else{
							rowObject.put( c.getColumnName(i) ,  "" ); 
						}
					}
					catch( Exception e ){
						Log.d("TAG_NAME", e.getMessage() );
					}
				} 
			}
					 
	       	jaResult.put(rowObject);
			c.moveToNext();
		}
					 
		c.close(); 
		Log.d("TAG_NAME", jaResult.toString() );
		return jaResult; 
	}
	
	
	public void deleteCusData(){
		db.delete(DesContract.Customer.TABLE_NAME, null, null);
		db.delete(DesContract.CusOwnerData.TABLE_NAME, null, null);
//		db.delete(DesContract.CusPurchaserData.TABLE_NAME, null, null);
	}

	public void deleteTable(String tableName){
		String sql = "DELETE FROM "+tableName;
		db.execSQL(sql);
	}
	public void deleteTable(String tableName, String where){
		String sql = "DELETE FROM "+tableName+where;
		db.execSQL(sql);
	}

	public ArrayList<String> getBrochureURL(){
		ArrayList<String> strUrl = new ArrayList<>();
		String sql = "SELECT description FROM cus_datas WHERE status = 12";
		Cursor c = db.rawQuery(sql,null);
		for(int i=0;i<c.getCount();i++){
			if(c.moveToPosition(i)) strUrl.add(c.getString(0));
		}
		c.close();
		return strUrl;
	}
	
}
