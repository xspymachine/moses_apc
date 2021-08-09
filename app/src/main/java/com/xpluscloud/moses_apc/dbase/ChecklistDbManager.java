package com.xpluscloud.moses_apc.dbase;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class ChecklistDbManager extends DbManager{

	public ChecklistDbManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<String> getChecklists() {
		ArrayList<String> list = new ArrayList<String>();
				
		String sql = "SELECT description FROM "+DesContract.Checklists.TABLE_NAME+" ORDER BY mcid";
		
		Cursor c = db.rawQuery(sql, null);
		
		for(int i=0; i<c.getCount(); i++){
			if(c.moveToPosition(i)) {
				list.add(c.getString(0));
				Log.e("",c.getString(0));
			}
		}
		
		c.close();
		return list;
	}

	public int getId(String description) {
		// TODO Auto-generated method stub
		int id=0;
		
		String sql = "SELECT mcid FROM "+DesContract.Checklists.TABLE_NAME+" where description ='"+description+"'";
		
		Cursor c = db.rawQuery(sql, null);
		
		if(c.moveToFirst()) id = c.getInt(0);
		
		return id;
	}

}
