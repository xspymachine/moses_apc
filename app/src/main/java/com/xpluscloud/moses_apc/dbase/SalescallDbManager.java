package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.xpluscloud.moses_apc.getset.MyList;
import com.xpluscloud.moses_apc.getset.SalesCall;

import java.util.ArrayList;
import java.util.List;

public class SalescallDbManager extends DbManager {
	
	public static final String[] SalesCall_PROJECTION = new String[] {
		DesContract.SalesCall._ID,
		DesContract.SalesCall.DATETIME,
		DesContract.SalesCall.CCODE,
		DesContract.SalesCall.BLOB,		
		DesContract.SalesCall.STATUS
	};
	
	public SalescallDbManager(Context context) {
		super(context);
	}
	
	public long Add(SalesCall c) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.SalesCall.DATETIME	, c.getDatetime());
		cv.put(DesContract.SalesCall.CCODE     	, c.getCustomerCode());
		cv.put(DesContract.SalesCall.BLOB       , c.getBlob());		
		cv.put(DesContract.SalesCall.STATUS 	, c.getStatus());
		
		Long id = db.insert(DesContract.SalesCall.TABLE_NAME, null, cv);
		
		//Delete Old data
//		delete_old();		
		
		return id;
	}
	
	
	public SalesCall getRow(int rowId) {
		SalesCall cust;
		Cursor c = retrieveCursor(rowId, DesContract.SalesCall.TABLE_NAME, SalesCall_PROJECTION);
		
		if(c.moveToFirst()) {
			cust = createSalesCall(c);
		} else {
			cust = null;
		}
		
		c.close();
		
		return cust;
	}
	
	public void delete(String dateBefore) {
		 db.delete(DesContract.SalesCall.TABLE_NAME, "datetime < ?", new String[] {dateBefore});
	}
	
	public List<MyList> getAll() {
		List<MyList> list = new ArrayList<MyList>();
		
		String sql = "SELECT t1._id AS id," +
				"strftime('%m/%d/%Y %H:%M:%S',t1.datetime,'unixepoch','localtime')  AS dtime," +
				"t1.ccode AS cuscode," + 
				"t2.name AS cusname," +	
				"t2.address," +	
				"t1.blob," +
				"t3.remarks" + 
				" FROM salescalls AS t1" +
				" LEFT JOIN customers AS t2" +
				" ON t1.ccode=t2.ccode" +
				" LEFT JOIN merchandising AS t3" +
				" ON t1._id=t3.customercall_id" +
				" ORDER BY  t1._id DESC";
		
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
	
	public List<MyList> getByRange(long date1, String customerName) {
		List<MyList> list = new ArrayList<MyList>();
//		date2 = date2+86400000;
//		String where1 = " where t1.datetime >= "+date1+" AND t1.datetime <= "+date2+" AND t2.name = '"+customerName+"' ";
//		String where2 = " where t1.datetime >= "+date1+" AND t1.datetime <= "+date2+"";
		String where1 = " where t1.datetime LIKE %"+date1+"% AND t2.name = '"+customerName+"' ";
		String where2 = " where t1.datetime LIKE %"+date1+"%";
		String where = customerName.equals("") ? where2 : where1;
		String sql = "SELECT t1._id AS id," +
				"strftime('%m/%d/%Y %H:%M:%S',t1.datetime,'unixepoch','localtime')  AS dtime," +
				"t1.ccode AS cuscode," + 
				"t2.name AS cusname," +	
				"t2.address," +	
				"t1.blob," + 
				"t3.remarks" + 
				" FROM salescalls AS t1" +
				" LEFT JOIN customers AS t2" +
				" ON t1.ccode=t2.ccode" +
				" LEFT JOIN merchandising AS t3" +
				" ON t1._id=t3.customercall_id"  + where+
				" ORDER BY  t1._id DESC";
		
//		Log.e("where",where2);
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					MyList item = createMyList(c);
					list.add(item);
				}
			}
//			Log.e("where",c.getString(c.getColumnIndex("dtime")));
		}
		c.close();		
		return list;
	}
	
	public List<MyList> getSummaryCalls(String date1, String customerName) {
		List<MyList> list = new ArrayList<MyList>();
//		String where1 = " where strftime('%Y-%m', t1.datetime, 'unixepoch','localtime') LIKE '%"+date1+"%' AND t2.name = '"+customerName+"' ";
		String where2 = " where strftime('%Y-%m', t1.datetime, 'unixepoch','localtime') LIKE '%"+date1+"%'";
//		String where = customerName.equals("") ? where2 : where1;
		String sql = "SELECT DISTINCT " +
				"strftime('%Y-%m',t1.datetime,'unixepoch','localtime')  AS dtime," +
				"t1.ccode AS cuscode," + 
				"t2.name AS cusname," +	
				"t2.address" +	
//				"t1.blob," + 
//				"t3.remarks" + 
				" FROM salescalls AS t1" +
				" LEFT JOIN customers AS t2" +
				" ON t1.ccode=t2.ccode" + where2+
//				" LEFT JOIN merchandising AS t3" +
//				" ON t1._id=t3.customercall_id"  + where+
				" ORDER BY  t1._id DESC";
		
//		Log.e("where",where2);
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					MyList item = createMyList(c);
					list.add(item);
				}
			}
//			Log.e("where",c.getString(c.getColumnIndex("dtime")));
		}
		c.close();		
		return list;
	}
	
	private MyList createMyList(Cursor c) {
		MyList list = new MyList();
		
//		list.setId(c.getInt(0));
		list.setDateTime(c.getString(0));
		list.setCustomerCode(c.getString(1));
		list.setCustomerName(c.getString(2));
		list.setAddress(c.getString(3));
//		list.setTransaction(c.getString(5)+"--->"+(c.getString(6)==null ? "" : c.getString(6)));
		
		return list;
	
	}
	
	public void delete_old() {
		long past = System.currentTimeMillis()-1000*60*60*24*90l; //24hours ago.
		String sql = "DELETE FROM salescalls WHERE datetime < '"+ past +"' ";
		db.rawQuery(sql,null);
	}
	
	
	public int getLastId() {
		int lastId = 0;
		
		String sql = "SELECT _id FROM SalesCall ORDER BY _id DESC LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
		    lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		
		return lastId;
	}
	
	
	public void updateStatus(Integer id, Integer status) {
		String sql="UPDATE SalesCall SET " +
				" status=" + status + 
				", priority=priority + 1 "  + 
				" WHERE _id=" + id;
		
		db.execSQL(sql);
	}
	
	public SalesCall getLastSaleTransaction(String ccode){
		SalesCall ccall;
		
		String OrderBy = "_id DESC";
		String Limit = "1";
		String where = "ccode = '"+ccode+"' AND strftime('%Y-%m-%d',s."+DesContract.SalesCall.DATETIME+",'unixepoch','localtime') = strftime('%Y-%m-%d', 'now','localtime')";
		String sql =  " SELECT *from "+DesContract.SalesCall.TABLE_NAME +" s "
					 +" LEFT JOIN "+DesContract.Customer.TABLE_NAME+" c "+" USING ("+DesContract.Customer.CCODE+")"
					 +" WHERE "+where;

		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst()) {
			ccall = createSalesCall(c);
		} else {
			ccall = null;
		}
		
		c.close();		
		
		return ccall;		
	}
	
	
	private SalesCall createSalesCall(Cursor c) {
		SalesCall SalesCall = new SalesCall();		
		SalesCall.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		SalesCall.setDatetime(c.getString(
				c.getColumnIndex(DesContract.SalesCall.DATETIME)));
		SalesCall.setCustomerCode(c.getString(
				c.getColumnIndex(DesContract.SalesCall.CCODE)));
		SalesCall.setBlob(c.getString(
				c.getColumnIndex(DesContract.SalesCall.BLOB)));
		SalesCall.setStatus(c.getInt(
				c.getColumnIndex(DesContract.SalesCall.STATUS)));
		return SalesCall;
	}
	
	public ArrayList<String> getWorkWith(){
		ArrayList<String> work = new ArrayList<String>();
		work.add("-");
		
		String sql="SELECT description FROM work_with ORDER BY description ASC";
			
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					work.add(c.getString(0));
//					Log.d("getArrayList",""+c.getString(0));
				}
			}
		}	
		c.close();
		
		return work;
		
	}
	
	public String getInfo(String ccode, String date, int option){
		String info = "";
		String sql = "";
		Cursor c = null;
		
		switch (option) {
		case 1:
			sql = "SELECT count(*) FROM salescalls WHERE strftime('%Y-%m', datetime, 'unixepoch','localtime') LIKE '%"+date+"%' AND ccode = '"+ccode+"'";
			c = db.rawQuery(sql, null);
			if(c.moveToFirst()) info = ""+c.getInt(0);
			break;
		case 2:
			sql = "SELECT count(*) FROM salescalls WHERE strftime('%Y-%m', datetime, 'unixepoch','localtime') LIKE '%"+date+"%' AND ccode = '"+ccode+"' AND  blob NOT LIKE '%Reason ' || X'0A' || '%'";
			c = db.rawQuery(sql, null);
			if(c.moveToFirst()) info = ""+c.getInt(0);
			break;
		case 3:
			sql = "SELECT blob FROM salescalls WHERE strftime('%Y-%m', datetime, 'unixepoch','localtime') LIKE '%"+date+"%' AND ccode = '"+ccode+"' ORDER BY _id DESC LIMIT 1";
			c = db.rawQuery(sql, null);
			if(c.moveToFirst()) info = c.getString(0);
			break;
		case 4:
			String sql1 = "SELECT _id FROM salescalls WHERE strftime('%Y-%m', datetime, 'unixepoch','localtime') LIKE '%"+date+"%' AND ccode = '"+ccode+"' ORDER BY _id DESC LIMIT 1";
			sql = "SELECT remarks FROM merchandising WHERE customercall_id = ("+sql1+")";
			c = db.rawQuery(sql, null);
			if(c.moveToFirst()) info = c.getString(0);
			break;

		default:
			break;
		}
		
		
		return info;
	}
}
