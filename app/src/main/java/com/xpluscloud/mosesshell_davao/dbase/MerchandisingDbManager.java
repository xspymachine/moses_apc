package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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

}
