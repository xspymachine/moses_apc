package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xpluscloud.moses_apc.getset.SearchList;

import java.util.ArrayList;
import java.util.List;


public class DataManager extends DbManager {
	
	public DataManager(Context context) {
		super(context);
	}
	
	public Long InsertData(String Table, ContentValues cv) {
		try {
			return  db.insertWithOnConflict(Table, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		}catch (Exception e) {
			e.printStackTrace();
			return 0l;
		}
		
	}
	
	public Boolean delete(String Table, Integer id) {
		return db.delete(Table, "_id" + "=" + id, null) > 0;
	}
	
	public Boolean deleteAll(String Table) {
		return db.delete(Table,null, null) > 0;
	}
	
	
	public Cursor getAll(String Table) {
		return db.query(true, Table, null, null, null, null, null, null, null);
	}
	
	
	
	
	public List<SearchList> getList(String Table, String[] Projection, String Where, String OrderBy) {
		List<SearchList> res = new ArrayList<SearchList>();
		Cursor c = getWhere(Table, Projection, Where, OrderBy);
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					SearchList item = setList(c);
					res.add(item);
				}
			}
		}
				
		c.close();		
		return res;		
	}
	
	
	
	private SearchList setList(Cursor c) {
		SearchList sList = new  SearchList();		
		sList.setId(c.getInt(0));
		sList.setRefid(c.getInt(1));		
		sList.setTitle(c.getString(2));
		sList.setDescription(c.getString(3));	
		sList.setPicture(c.getString(4));
		return sList;
		
	}
	
}
