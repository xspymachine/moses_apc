package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.xpluscloud.mosesshell_davao.getset.CallSheet;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.Master;

import java.util.ArrayList;
import java.util.List;


public class CallSheetDbManager extends DbManager {
	
	public final String TAG = "CallSheetDbManager";
	
	public static final String[] CallSheet_PROJECTION = new String[] {
		DesContract.CallSheet._ID,
		DesContract.CallSheet.DATE,
		DesContract.CallSheet.CSCODE,
		DesContract.CallSheet.SONO,
		DesContract.CallSheet.CCODE,
		DesContract.CallSheet.BUFFER,
		DesContract.CallSheet.CASH_SALES,
		DesContract.CallSheet.PAYMENT,
		DesContract.CallSheet.STATUS
		
	};
	
	public CallSheetDbManager(Context context) {
		super(context);
	}
	
	public long AddCallSheet(CallSheet ro) {// ro => record object
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.CallSheet.DATE 		, ro.getDate());
		cv.put(DesContract.CallSheet.CSCODE		, ro.getCscode());
		cv.put(DesContract.CallSheet.SONO 		, ro.getSono());
		cv.put(DesContract.CallSheet.CCODE    	, ro.getCcode());
		cv.put(DesContract.CallSheet.BUFFER 	, ro.getBuffer());
		cv.put(DesContract.CallSheet.CASH_SALES , ro.getCash_sales());
		cv.put(DesContract.CallSheet.SUPPLIER	, ro.getSupplier());
		cv.put(DesContract.CallSheet.STATUS 	, ro.getStatus());
		
		if (ro.getId() > 0) cv.put(BaseColumns._ID, ro.getId());
		
		return db.insertWithOnConflict(
				DesContract.CallSheet.TABLE_NAME, 
				null, 
				cv, 
				SQLiteDatabase.CONFLICT_REPLACE
				);
	
	}
	
	public int getLastId() {
		int lastId = 0;
		
		String sql = "SELECT _id FROM callsheets ORDER BY _id DESC LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
		    lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		
		return lastId;
	}
	
	public int getLastSOno() {
		int lastSOno=0;
		
		int lastid= this.getLastId();
		if(lastid>0) {
			String sql = "SELECT sono FROM callsheets WHERE _id = "+lastid+" LIMIT 1";
			Cursor c = db.rawQuery(sql,null);
			if (c != null && c.moveToFirst()) {
				lastSOno = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
			}
		}
		return lastSOno;
	}
	
	public CallSheet getInfo(String csCode) {
		CallSheet cs=null;
		String sql = "SELECT * FROM callsheets WHERE cscode = '" + csCode + "' LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		
		if(c.getCount()>0) {
			if(c.moveToFirst()) {
				cs = createCallSheetInfo(c);
			} 
		}
		
		c.close();
		
		return cs;
	}
	
	
	
	
	public List<CallSheet> getAll(String cCode) {
		List<CallSheet> list = new ArrayList<CallSheet>();
		String where;
		
		if (cCode==null) where =""; 
		else {
			where = " WHERE ccode LIKE '" + cCode +"' ";
		}
		
		String sql = "SELECT t1._id," +
				"strftime('%m/%d/%Y %H:%M:%S',t1.date,'unixepoch','localtime')  AS date," +					
				"t1.cscode," + 	
				"t1.ccode," +
				"t1.sono," + 		
				"t2.name," +	
				"t1.buffer," +
				"t1.cash_sales," +
				"t1.payment," +
				"t1.status," +
				"(SELECT sum(order_qty) FROM callsheetitems WHERE cscode = t1.cscode)," +
				"t3.qty" +
				" FROM callsheets AS t1" +
				" LEFT JOIN so_serve AS t3" +
				" ON t1._id = t3.soid" +				
				" LEFT JOIN customers AS t2" +
				" ON t1.ccode=t2.ccode " + where +
				" ORDER BY  t1.date DESC, t1._id DESC";
		
		Cursor c = db.rawQuery(sql,null);
		
		if (c.getCount()>0) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					CallSheet obj = createCallSheet2(c);
					list.add(obj);
				}
			}
		}
		
		c.close();
		
		return list;
	}
	
	private CallSheet createCallSheet2(Cursor c) {
		CallSheet ro = new CallSheet();
		
		int qty = 0;
		try {
			qty = c.getInt(11);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		ro.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		ro.setDate(c.getString(1));
		ro.setCscode(c.getString(2));
		ro.setCcode(c.getString(3));
		ro.setSono(c.getInt(4));	
		ro.setcusname(c.getString(5)+"--->"+c.getInt(10)+"--->"+qty);
		ro.setBuffer(c.getInt(6));
		ro.setCash_sales(c.getInt(7));
		ro.setPayment(c.getDouble(8));
		ro.setStatus(c.getInt(9));

		return ro;
	}
	
	public void updateStatus(Integer Id, Integer status) {
		
		String sql = "UPDATE " + DesContract.CallSheet.TABLE_NAME
				+ " SET " + DesContract.CallSheet.STATUS + "=" +status
				+ " WHERE " + BaseColumns._ID + "=" + Id ;

		Log.w("Update Status query",sql);
		db.execSQL(sql);
	}
	
	public void updateSCtype(String csCode, int type) {
		
		String sql = "UPDATE " + DesContract.CallSheet.TABLE_NAME
				+ " SET " + DesContract.CallSheet.CASH_SALES + "=" +type
				+ " WHERE " + DesContract.CallSheet.CSCODE + " LIKE '" + csCode +"'" ;
		db.execSQL(sql);
	}
	
	public void updateSonoBuffer(String csCode, int Option, int value) {
		
		String setValue=" SET sono = " + value;
		if(Option!=0) {
			setValue=" SET buffer = " + value;
		}
		String sql = "UPDATE " + DesContract.CallSheet.TABLE_NAME
				+ setValue 				
				+ " WHERE " + DesContract.CallSheet.CSCODE + " LIKE '" + csCode +"'" ;
		db.execSQL(sql);
	}
	
	public void updatePayment(String csCode, Double payment) {
		
		String sql = "UPDATE " + DesContract.CallSheet.TABLE_NAME
				+ " SET " + DesContract.CallSheet.PAYMENT + "=" +payment
				+ " WHERE " + DesContract.CallSheet.CSCODE + " LIKE '" + csCode +"'" ;
		db.execSQL(sql);
	}

	
	public void deleteCallSheet(String csCode) {
		String where = "cscode LIKE '" + csCode +"' AND status=0";
		db.delete(DesContract.CallSheet.TABLE_NAME, where, null);
		
	}
	
	private CallSheet createCallSheet(Cursor c) {
		CallSheet ro = new CallSheet();
		
		ro.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		ro.setDate(c.getString(
				c.getColumnIndex(DesContract.CallSheet.DATE)));
		ro.setCscode(c.getString(
				c.getColumnIndex(DesContract.CallSheet.CSCODE)));
		ro.setCcode(c.getString(
				c.getColumnIndex(DesContract.CallSheet.CCODE)));
		ro.setcusname(c.getString(
				c.getColumnIndex(DesContract.Customer.NAME)));
		ro.setSono(c.getInt(
				c.getColumnIndex(DesContract.CallSheet.SONO)));	
		ro.setCash_sales(c.getInt(
				c.getColumnIndex(DesContract.CallSheet.CASH_SALES)));
		ro.setPayment(c.getDouble(
				c.getColumnIndex(DesContract.CallSheet.PAYMENT)));
		ro.setBuffer(c.getInt(
				c.getColumnIndex(DesContract.CallSheet.BUFFER)));
		ro.setStatus(c.getInt(
				c.getColumnIndex(DesContract.CallSheet.STATUS)));
		return ro;
	}
	
	private CallSheet createCallSheetInfo(Cursor c) {
		CallSheet ro = new CallSheet();
		
		ro.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		ro.setDate(c.getString(
				c.getColumnIndex(DesContract.CallSheet.DATE)));
		ro.setCscode(c.getString(
				c.getColumnIndex(DesContract.CallSheet.CSCODE)));
		ro.setCcode(c.getString(
				c.getColumnIndex(DesContract.CallSheet.CCODE)));
		ro.setSono(c.getInt(
				c.getColumnIndex(DesContract.CallSheet.SONO)));	
		ro.setCash_sales(c.getInt(
				c.getColumnIndex(DesContract.CallSheet.CASH_SALES)));
		ro.setPayment(c.getDouble(
				c.getColumnIndex(DesContract.CallSheet.PAYMENT)));
		ro.setBuffer(c.getInt(
				c.getColumnIndex(DesContract.CallSheet.BUFFER)));
		ro.setStatus(c.getInt(
				c.getColumnIndex(DesContract.CallSheet.STATUS)));
		return ro;
	}
	
	public List<CallSheet> getCallSheets() {
		List<CallSheet> list = new ArrayList<CallSheet>();
		String sql = "SELECT * FROM callsheets ORDER BY  _id DESC";
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					CallSheet box = createCallSheetInfo(c);
					list.add(box);
				}
			}
		}
		c.close();		
		return list;
	}
	
	public void updateDeliverStatus(int soid, String devId, String cscode, String ccode, int qty){
		ContentValues cv = new ContentValues();
		
		cv.put(DesContract.CallSheet.STATUS, 99);
		
		db.update(DesContract.CallSheet.TABLE_NAME, cv,"_id="+soid , null);	
		
		String message="";
		devId = DbUtil.getSetting(context, Master.DEVID);
		message = Master.CMD_UCST 	+ " " +
				devId 				+ ";" +
				ccode 				+ ";" +
				cscode				+ ";" +
				System.currentTimeMillis()/1000 + ";" +
				qty;
		
		DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
		
		ContentValues cv2 = new ContentValues();
		cv2.put(DesContract.CallSheetServe.SOID, soid);
		cv2.put(DesContract.CallSheetServe.DEVID, devId);
		cv2.put(DesContract.CallSheetServe.CCODE, ccode);
		cv2.put(DesContract.CallSheetServe.CSCODE, cscode);
		cv2.put(DesContract.CallSheetServe.DATE, System.currentTimeMillis()/1000);
		cv2.put(DesContract.CallSheetServe.QTY, qty);
		
		db.insert(DesContract.CallSheetServe.TABLE_NAME, null, cv2);
	}
		
}
