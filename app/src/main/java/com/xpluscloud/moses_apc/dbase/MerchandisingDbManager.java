package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MerchandisingDbManager extends DbManager{

	public MerchandisingDbManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void addData(String ccode, String remarks){
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.Merchandising.CCODE, ccode);
		cv.put(DesContract.Merchandising.REMARKS, remarks);
		cv.put(DesContract.Merchandising.CALLSID, getLastId());
		cv.put(DesContract.Merchandising.DATE, System.currentTimeMillis());
		
		db.insert(DesContract.Merchandising.TABLE_NAME, null, cv);
	}
	
	//Customercall/Salescall ID
	private long getLastId(){
		long id = 0;
		String sql = "SELECT _id FROM salescalls ORDER BY _id DESC LIMIT 1";
		Cursor c = db.rawQuery(sql, null);
		
		if(c.moveToFirst()) id = c.getLong(0); 
		
		return id;
	}

	public List<HashMap<String,String>> getList(int status) {
		List<HashMap<String,String>> list = new ArrayList<>();

		String where = " where "+DesContract.Checklists.STATUS+" = "+status;
		String sql = "SELECT *FROM "+DesContract.Checklists.TABLE_NAME+where;

		Cursor c = db.rawQuery(sql, null);
//		Log.e("sql", sql);
		for(int i=0; i<c.getCount(); i++){
			if(c.moveToPosition(i)) {
				HashMap<String,String> item = new HashMap<>();
				item.put("description",c.getString(c.getColumnIndex("description")));
				item.put("status",c.getString(c.getColumnIndex("status")));
				item.put("mcid",""+c.getInt(c.getColumnIndex("mcid")));
				list.add(item);
			}
		}

		c.close();
		return list;
	}

}
