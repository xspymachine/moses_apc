package com.xpluscloud.moses_apc.dbase;

import android.content.Context;
import android.database.Cursor;

public class TargetDbManager extends DbManager{
	
	private static final String CREATE_TABLE_SALESTARGET =
			"CREATE TABLE " + DesContract.SalesTarget.TABLE_NAME + " ("
			+ DesContract.SalesTarget._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.SalesTarget.STID			+ " INTEGER UNIQUE, "
			+ DesContract.SalesTarget.VOLUME		+ " TEXT, "
			+ DesContract.SalesTarget.REACH			+ " INTEGER, "
			+ DesContract.SalesTarget.BUYING_ACCTS	+ " INTEGER, "
			+ DesContract.SalesTarget.CALLS			+ " INTEGER, "
			+ DesContract.SalesTarget.PRO_CALLS		+ " INTEGER, "
			+ DesContract.CallSheetServe.STATUS		+ " INTEGER);";

	public TargetDbManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	public Boolean tableExist(){
		
		String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+DesContract.SalesTarget.TABLE_NAME+"'";
		Cursor c = db.rawQuery(sql, null);
		
		if(c.moveToFirst()){
			c.close();
			return true;
		}
		else {
			db.execSQL(CREATE_TABLE_SALESTARGET);
			c.close();
			return false;
		}
		
	}
	
	public String getActualInfo(String ccode, String date, int option){
		int actual_count = 0;
		String info = "0";
		String sql = "";
		Cursor c = null;
		
		switch (option) {
			case 0://all calls
				sql = "SELECT count(*) FROM salescalls WHERE strftime('%Y-%m', datetime, 'unixepoch','localtime') LIKE '%"+date+"%'";
				c = db.rawQuery(sql, null);
				if(c.moveToFirst()) info = ""+c.getInt(0);
				break;
			case 1://filter productive calls
				sql = "SELECT count(*) FROM callsheets WHERE strftime('%Y-%m', date, 'unixepoch','localtime') LIKE '%"+date+"%' "
					+ "AND ccode IN (SELECT ccode FROM `salescalls` WHERE strftime('%Y-%m', datetime, 'unixepoch','localtime') LIKE '%"+date+"%')";
//				sql = "SELECT count(*) FROM callsheets WHERE strftime('%Y-%m', date, 'unixepoch','localtime') LIKE '%"+date+"%' ";
				c = db.rawQuery(sql, null);
				if(c.moveToFirst()) info = ""+c.getInt(0);
				break;
			case 2:
				int count =0;
				String sql2 = "SELECT count(*) FROM callsheets WHERE strftime('%Y-%m', date, 'unixepoch','localtime') LIKE '%"+date+"%' "
						+ "AND ccode IN (SELECT ccode FROM `salescalls` WHERE strftime('%Y-%m', datetime, 'unixepoch','localtime') LIKE '%"+date+"%')";
				Cursor c2 = db.rawQuery(sql2, null);
				if(c2.moveToFirst()) count = c2.getInt(0);

				sql = "SELECT SUM(order_qty*i.packing)" +
					" FROM callsheetitems csi LEFT JOIN items i ON(i.itemcode=csi.itemcode)";
					c = db.rawQuery(sql, null);
					if(c.moveToFirst()){
					    try {
                            Double volume = c.getDouble(0);
                            Double total = volume / count;
                            info = "" + total;
                        }catch (Exception e) {e.printStackTrace();}
					}
					break;
			
			default:
				break;
		}		
		return info;
	}	
	
	//SELECT count(ccode) FROM citem_price WHERE comcode = '00000BMM7YF6UP600001' AND ioh > 0
	
	public String getTargetInfo(String date, int opt){
		String target_count = "0";
		String sql = "";
		Cursor c = null;
//		Log.e("target date", date);
//		Log.e("target opt", ""+opt);
		switch(opt){
//			case 0: sql = "SELECT target_volume FROM sales_target WHERE targetid = '"+date+"'";
//					c = db.rawQuery(sql, null);
//					if(c.moveToFirst()) target_count = ""+c.getString(0);
//					break;
			case 0: sql = "SELECT target_reach FROM sales_target WHERE targetid = '"+date+"'";
					c = db.rawQuery(sql, null);
					if(c.moveToFirst()) target_count = ""+c.getInt(0);
					break;
//			case 2: sql = "SELECT target_buying_accts FROM sales_target WHERE targetid = '"+date+"'";
//					c = db.rawQuery(sql, null);
//					if(c.moveToFirst()) target_count = ""+c.getInt(0);
//					break;
			case 1: sql = "SELECT target_calls FROM sales_target WHERE targetid = '"+date+"'";
					c = db.rawQuery(sql, null);
					if(c.moveToFirst()) target_count = ""+c.getInt(0);
					break;
			case 2: sql = "SELECT target_pro_calls FROM sales_target WHERE targetid = '"+date+"'";
					c = db.rawQuery(sql, null);
					if(c.moveToFirst()) target_count = ""+c.getInt(0);
					break;
		}

		c.close();
		return target_count;
		
	}

//	public void updateFailData(){
//        String todate = DateUtil.strDateMY(System.currentTimeMillis());
//		String sql = "SELECT datetime,message FROM outbox WHERE  strftime('%Y-%m', datetime, 'unixepoch','localtime') BETWEEN '2017-03' AND '"+todate+"' AND message LIKE '%CMDUCST%'";
//		Cursor c= db.rawQuery(sql,null);
//
//		if (c != null && c.moveToFirst()) {
//			for(int i = 0; i < c.getCount(); i++) {
//				if(c.moveToPosition(i)) {
//					int datetime = c.getInt(0);
//					String message = c.getString(1);
//					String[] splitMsg = message.split(";");
//					String csCode = splitMsg[2];
//					String date   = splitMsg[3];
//
//					ContentValues cv = new ContentValues();
//					cv.put(DesContract.CallSheetServe.DATE, date);
//					cv.put(DesContract.CallSheetServe.LOGDATE, datetime);
//					int af = db.update(DesContract.CallSheetServe.TABLE_NAME, cv,DesContract.CallSheetServe.CSCODE+"='"+csCode+"'" , null);
////                    Log.e("affected",""+af);
//				}
//			}
//		}
//		c.close();
//	}

}
