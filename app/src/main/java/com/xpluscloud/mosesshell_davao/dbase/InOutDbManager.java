package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.xpluscloud.mosesshell_davao.getset.MyList;
import com.xpluscloud.mosesshell_davao.getset.TimeInOut;
import com.xpluscloud.mosesshell_davao.util.DateUtil;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.Master;

import java.util.ArrayList;
import java.util.List;

public class InOutDbManager extends DbManager {
	
	public static final String[] TimeInOut_PROJECTION = new String[] {
		DesContract.TimeInOut._ID,
		DesContract.TimeInOut.DATETIME,
		DesContract.TimeInOut.CCODE,
		DesContract.TimeInOut.INOUT,
		DesContract.TimeInOut.LATITUDE,
		DesContract.TimeInOut.LONGITUDE,
		DesContract.TimeInOut.STATUS
	};
	
	public InOutDbManager(Context context) {
		super(context);
	}
	
	public long Add(TimeInOut c) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.TimeInOut.DATETIME	, c.getDateTime());
		cv.put(DesContract.TimeInOut.CCODE     	, c.getCustomerCode());
		cv.put(DesContract.TimeInOut.INOUT      , c.getInout());
		cv.put(DesContract.TimeInOut.LATITUDE 	, c.getLatitude());
		cv.put(DesContract.TimeInOut.LONGITUDE 	, c.getLongitude());
		cv.put(DesContract.TimeInOut.STATUS 	, c.getStatus());
		
		return db.insert(DesContract.TimeInOut.TABLE_NAME, null, cv);
	}
	
	
	public TimeInOut getRow(int rowId) {
		TimeInOut dbo;
		Cursor c = retrieveCursor(rowId, DesContract.TimeInOut.TABLE_NAME, TimeInOut_PROJECTION);
		
		if(c.moveToFirst()) {
			dbo = createTimeInOut(c);
		} else {
			dbo = null;
		}
		
		c.close();
		
		return dbo;
	}
	
	
	public TimeInOut getLastTransaction(){
		TimeInOut dbo;
		
		String OrderBy = "_id DESC";
		String Limit = "1";
		
		Cursor c = retrieveCursor(DesContract.TimeInOut.TABLE_NAME, TimeInOut_PROJECTION,OrderBy,Limit);
		if(c.moveToFirst()) {
			dbo = createTimeInOut(c);
		} else {
			dbo = null;
		}
		
		c.close();		
		return dbo;
	}
	
	public void delete(String dateBefore) {
		 db.delete(DesContract.TimeInOut.TABLE_NAME, "datetime < ?", new String[] {dateBefore});
	}
	
	public List<MyList> getAll() {
		List<MyList> list = new ArrayList<MyList>();
		
		String sql = "SELECT tio._id AS id," +
				"strftime('%m/%d/%Y %H:%M:%S',tio.datetime,'unixepoch','localtime')  AS dtime," +
				"tio.ccode AS cuscode," + 				
				"cus.name AS cusname," +
				"cus.address," +	
				"tio.inout AS io" + 
				" FROM timeinout AS tio" +
				" LEFT JOIN customers AS cus" +
				" ON tio.ccode=cus.ccode" +
				" ORDER BY  tio._id DESC";
		
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					MyList item = createMyList(c);
					list.add(item);
				}
			}
		}
		c.close();		
		return list;
	}
	
	public void delete_old() {
		long past = System.currentTimeMillis()-1000*60*60*24*7l; //7days.
		String sql = "DELETE FROM timeinout WHERE datetime < '"+ past +"' ";
		db.rawQuery(sql,null);
	}
	
	
	public int getLastId() {
		int lastId = 0;
		
		String sql = "SELECT _id FROM timeinout ORDER BY _id DESC LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
		    lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		
		return lastId;
	}
	
	
	public void updateStatus(Integer id, Integer status) {
		String sql="UPDATE timeinout SET " +
				" status=" + status + 
				", priority=priority + 1 "  + 
				" WHERE _id=" + id;
		
		db.execSQL(sql);
	}
	
	
	private TimeInOut createTimeInOut(Cursor c) {
		TimeInOut TimeInOut = new TimeInOut();		
		TimeInOut.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		TimeInOut.setDateTime(c.getString(
				c.getColumnIndex(DesContract.TimeInOut.DATETIME)));
		TimeInOut.setCustomerCode(c.getString(
				c.getColumnIndex(DesContract.TimeInOut.CCODE)));
		TimeInOut.setInout(c.getInt(
				c.getColumnIndex(DesContract.TimeInOut.INOUT)));
		TimeInOut.setLatitude(c.getDouble(
				c.getColumnIndex(DesContract.TimeInOut.LATITUDE)));
		TimeInOut.setLongitude(c.getDouble(
				c.getColumnIndex(DesContract.TimeInOut.LONGITUDE)));
		TimeInOut.setStatus(c.getInt(
				c.getColumnIndex(DesContract.TimeInOut.STATUS)));
		return TimeInOut;
	}
	
	private MyList createMyList(Cursor c) {
		MyList list = new MyList();
		
		list.setId(c.getInt(0));
		list.setDateTime(c.getString(1));
		list.setCustomerCode(c.getString(2));
		list.setCustomerName(c.getString(3));
		list.setAddress(c.getString(4));
		if (c.getInt(5) == 0) list.setTransaction("Time Out");
		else list.setTransaction("Time In");
		return list;
	
	}
	
	public void checkIO(String devId){
		String ccode = "";
		long datetime = 0;
		int inout = 0;
		
		long millis = System.currentTimeMillis();
		String sql = "SELECT ccode,datetime,inout FROM timeinout ORDER BY _id DESC LIMIT 1 ";
		Cursor cur = db.rawQuery(sql, null);
//		Log.e("asd",sql);
		if(cur.moveToFirst()) {
			ccode = cur.getString(0);
			datetime = cur.getLong(1);
			inout = cur.getInt(2);
			datetime = datetime*1000+36000000;
			
			if(inout == 1 && datetime<millis){
				TimeInOut c = new TimeInOut();
				
				c.setDateTime(""+(millis/1000));
				c.setCustomerCode(ccode);
				c.setInout(0);
				c.setStatus(0);
				
				long id = Add(c);
				long sysTime = millis/1000;
				
				String message = Master.CMD_INOUT + " " +
							devId 	+ ";" +
							ccode 	+ ";" +
							"OUT"	+ ";" +
							"" 		+ ";" + 
							"" 		+ ";" + 
							sysTime + ";" + 
							sysTime + ";" + 
							id;
					
				DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
			}
		}			
	}
	
	public int getLastInByCode(String ccode){
		long date=0;
//		String sql = " SELECT "+DesContract.TimeInOut.DATETIME+" FROM "+DesContract.TimeInOut.TABLE_NAME+
//					 " WHERE "+DesContract.TimeInOut.CCODE+"='"+ccode+"' ORDER BY _id DESC LIMIT 1";

        int count=0;
		String sql = " SELECT "+DesContract.TimeInOut.DATETIME+" FROM "+DesContract.TimeInOut.TABLE_NAME+
					 " WHERE "+DesContract.TimeInOut.CCODE+"='"+ccode+"' AND "+
					 " strftime('%Y-%m-%d', "+DesContract.TimeInOut.DATETIME+",'unixepoch') = '"+
					 DateUtil.strDate(System.currentTimeMillis())+"'";

		Cursor c = db.rawQuery(sql, null);
//		if(c.moveToFirst()) {
//			date=c.getLong(0);
//		}
		count = c.getCount();
		c.close();		
		return count;
	}
	
	public int getCount() {		
		String sql = "SELECT *FROM "+DesContract.Customer.TABLE_NAME;
		Cursor c = db.rawQuery(sql, null);
		
		int count = c.getCount();
		c.close();
		
		return count;
	}
		
	
}
