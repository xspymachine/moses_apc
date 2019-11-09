package com.xpluscloud.mosesshell_davao.dbase;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class SyncDbManager extends DbManager{

	public SyncDbManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public JSONArray sync_server(String table_name, int limit, int status) {
		String sql = "SELECT * FROM "+table_name+" WHERE status="+status+" OR status=9 ORDER BY _id ASC LIMIT "+limit;
		String sourceTable = table_name;
		Cursor c = db.rawQuery(sql,null);
		 
		JSONArray resultSet 	= new JSONArray();
		//JSONObject returnObj 	= new JSONObject();
		 
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			int totalColumn = c.getColumnCount();
			int _id = c.getInt(c.getColumnIndex(DesContract.Outbox._ID));
			
			//Log.d("TAG_NAME", c.getColumnName(totalColumn-1));
			
			UtilDbManager udb = new UtilDbManager(context);						
			udb.open();
			udb.updateStatus(sourceTable, _id, 9);
			udb.close();
			
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
						Log.d("Exception", e.getMessage() );
					}
				}
					 
			}
					 
			resultSet.put(rowObject);
			c.moveToNext();
		}
					 
		c.close(); 
		//Log.d("Get All unsent", resultSet.toString() );
		return resultSet; 
	}
	
	public JSONArray sync_server_range(String table_name, String where) {
		String sql = "SELECT * FROM "+table_name+" WHERE "+where;
//		String sourceTable = table_name;		
		Cursor c = db.rawQuery(sql,null);
		
		Log.e("sql",sql);
		 
		JSONArray resultSet 	= new JSONArray();
		//JSONObject returnObj 	= new JSONObject();
		 
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			int totalColumn = c.getColumnCount();
//			int _id = c.getInt(c.getColumnIndex(DesContract.Outbox._ID));
			
			//Log.d("TAG_NAME", c.getColumnName(totalColumn-1));
			
//			UtilDbManager udb = new UtilDbManager(context);						
//			udb.open();
//			udb.updateStatus(sourceTable, _id, 9);
//			udb.close();
			
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
						Log.d("Exception", e.getMessage() );
					}
				}
					 
			}
					 
			resultSet.put(rowObject);
			c.moveToNext();
		}
					 
		c.close(); 
		Log.d("Get All unsent", resultSet.toString() );
		return resultSet; 
	}
	
	public JSONArray sync_server_select(String _sql) {
		String sql = _sql;
//		String sourceTable = table_name;		
		Cursor c = db.rawQuery(sql,null);
		
		Log.e("sql",sql);
		 
		JSONArray resultSet 	= new JSONArray();
		//JSONObject returnObj 	= new JSONObject();
		 
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			int totalColumn = c.getColumnCount();
//			int _id = c.getInt(c.getColumnIndex(DesContract.Outbox._ID));
			
			//Log.d("TAG_NAME", c.getColumnName(totalColumn-1));
			
//			UtilDbManager udb = new UtilDbManager(context);						
//			udb.open();
//			udb.updateStatus(sourceTable, _id, 9);
//			udb.close();
			
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
						Log.d("Exception", e.getMessage() );
					}
				}
					 
			}
					 
			resultSet.put(rowObject);
			c.moveToNext();
		}
					 
		c.close(); 
		Log.d("Get All unsent", resultSet.toString() );
		return resultSet; 
	}

}
