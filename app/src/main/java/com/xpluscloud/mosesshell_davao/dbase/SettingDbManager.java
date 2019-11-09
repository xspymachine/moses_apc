package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.xpluscloud.mosesshell_davao.getset.Setting;

import java.util.ArrayList;
import java.util.List;

public class SettingDbManager extends DbManager {
	
	public static final String[] Setting_PROJECTION = new String[] {
		DesContract.Setting._ID,
		DesContract.Setting.KEY,
		DesContract.Setting.VALUE,				
		DesContract.Setting.STATUS
	};
	
	public SettingDbManager(Context context) {
		super(context);
	}
	
	public long setKeyValue(String key, String value, Integer status) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Setting.KEY			, key);
		cv.put(DesContract.Setting.VALUE		, value);		
		cv.put(DesContract.Setting.STATUS 		, status);		
		return db.insert(DesContract.Setting.TABLE_NAME, null, cv);
	}
	
	public void insert(String key, String value, Integer status) {
		String sql="REPLACE INTO " +
				DesContract.Setting.TABLE_NAME + 
				"(" + DesContract.Setting.KEY + "," 
					+ DesContract.Setting.VALUE + ") " +
				"VALUES ('" + key + "', '"+ value +"')";
		db.execSQL(sql);
		
	}
		
	
	public long AddSetting(Setting s) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Setting.KEY			, s.getKey());
		cv.put(DesContract.Setting.VALUE		, s.getValue());		
		cv.put(DesContract.Setting.STATUS 		, s.getStatus());
		
		return db.insertWithOnConflict(DesContract.Setting.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		//return db.insert(DesContract.Setting.TABLE_NAME, null, cv);
	}
	
	public void AddSettings(List<Setting> list) {
		for(int i = 0; i < list.size(); i++) {
			AddSetting(list.get(i));
		}
	}
	
	public String getSetting(String key) {
		String sql = "SELECT * FROM settings WHERE key='" + key + "' LIMIT 1";
		String setValue;
		
		Cursor c = db.rawQuery(sql, null);
		
		Setting setting;
		
		if(c!=null && c.moveToFirst()) {
			setting = createSetting(c);
			setValue = setting.getValue();
		} else {
			setValue = null;
		}
		
		c.close();
		
		return setValue;
		
	}
	
	
	
	public Boolean isRegistered2(String value) {
	
		String sql = "SELECT * FROM settings WHERE key='password' AND value!='' LIMIT 1";
		
		Cursor c = db.rawQuery(sql, null);
		int numRows = c.getCount();
		c.close();
		
		if (numRows>0) return true;
		else return false;
		
	}
	
	
	public List<Setting> getAllSettings() {
		
		List<Setting> list = new ArrayList<Setting>();
		Cursor c = retrieveCursor(DesContract.Setting.TABLE_NAME, Setting_PROJECTION);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Setting cplan = createSetting(c);
				list.add(cplan);
			}
		}
		
		c.close();
		
		return list;
	}
	
	
	private Setting createSetting(Cursor c) {
		Setting Setting = new Setting();
		
		Setting.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		
		Setting.setKey(c.getString(
				c.getColumnIndex(DesContract.Setting.KEY)));		
		Setting.setValue(c.getString(
				c.getColumnIndex(DesContract.Setting.VALUE)));				
		Setting.setStatus(c.getInt(
				c.getColumnIndex(DesContract.Setting.STATUS)));
		return Setting;
	}
	
	public void updatePassword(){
		int id = 0;
		String sql = "SELECT _id FROM "+DesContract.Setting.TABLE_NAME+" where "+DesContract.Setting.KEY+"='password'";
		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst()) id = c.getInt(0);
		
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Setting.KEY, id+"oldpassword");
		db.update(DesContract.Setting.TABLE_NAME, cv, DesContract.Setting.KEY+"='password'", null);
		
		ContentValues cv2 = new ContentValues();
		cv2.put(DesContract.Setting.KEY, id+"oldlastfour");
		db.update(DesContract.Setting.TABLE_NAME, cv2, DesContract.Setting.KEY+"='lastFour'", null);
		
		ContentValues cv3 = new ContentValues();
		cv3.put(DesContract.Setting.KEY, id+"oldHint");
		db.update(DesContract.Setting.TABLE_NAME, cv3, DesContract.Setting.KEY+"='hint'", null);
	}
	
	public Boolean getLastFour(String lastFour){
		String sql = "SELECT "+DesContract.Setting.VALUE+" FROM "+DesContract.Setting.TABLE_NAME+" where "+DesContract.Setting.VALUE+"='"+lastFour+"'";
		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst()) {
			return true;
		}
		else return false;
	}
}
