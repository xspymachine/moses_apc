package com.xpluscloud.moses_apc.dbase;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;

public class DeliveryDateDbManager extends DbManager{

	public DeliveryDateDbManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void AddColumn(){
//		String sqlSelect = "SELECT delivery_date FROM callsheetitems LIMIT 1";
//		String sqlAddCol = "ALTER TABLE callsheetitems ADD COLUMN delivery_date text";
//		
//		Cursor c = db.rawQuery(sqlSelect, null);
//		
//		if(!c.moveToFirst()) db.execSQL(sqlAddCol);		
		
		String sqlSelect = "PRAGMA table_info(callsheetitems)";
		Cursor c = db.rawQuery(sqlSelect, null);
		c.moveToFirst();
		String[] colNames = new String[50];
		int i =0;
		while(c.moveToNext()){
			Log.e("names",c.getString(1));
			colNames[i] = c.getString(1);
					i++;
		}				
		if (Arrays.asList(colNames).contains("delivery_date")) {
		    // Do some stuff.
			Log.e("contains","YES");
		}else{
			Log.e("contains","NO");
			String sqlAddCol = "ALTER TABLE callsheetitems ADD COLUMN delivery_date text DEFAULT ''";
			db.execSQL(sqlAddCol);	
		}
		
	}
	
	public void updateDeliveryDate(String date, String cscode, String itemcode){
		
		String sqlUpdate = "UPDATE callsheetitems SET delivery_date = '"+date+"' WHERE cscode ='"+cscode+"' AND itemcode = '"+itemcode+"'";
		db.execSQL(sqlUpdate);
		
	}

}
