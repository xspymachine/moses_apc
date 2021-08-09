package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.xpluscloud.moses_apc.getset.MarkedLocation;
import com.xpluscloud.moses_apc.getset.MyList;

import java.util.ArrayList;
import java.util.List;

public class MarkedLocationDbManager extends DbManager {
	
	public static final String[] MarkedLocation_PROJECTION = new String[] {
		DesContract.MarkedLocation._ID,
		DesContract.MarkedLocation.GPSTIME,
		DesContract.MarkedLocation.CCODE,
		DesContract.MarkedLocation.LATITUDE,
		DesContract.MarkedLocation.LONGITUDE,
		DesContract.MarkedLocation.ACCURACY,
		DesContract.MarkedLocation.PROVIDER,
		DesContract.MarkedLocation.STATUS
	};
	
	public MarkedLocationDbManager(Context context) {
		super(context);
	}
	
	public long Add(MarkedLocation c) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.MarkedLocation.GPSTIME	, c.getGpstime());
		cv.put(DesContract.MarkedLocation.CCODE     , c.getCustomerCode());		
		cv.put(DesContract.MarkedLocation.LATITUDE 	, c.getLatitude());
		cv.put(DesContract.MarkedLocation.LONGITUDE , c.getLongitude());
		cv.put(DesContract.MarkedLocation.ACCURACY 	, c.getAccuracy());
		cv.put(DesContract.MarkedLocation.PROVIDER  , c.getProvider());
		cv.put(DesContract.MarkedLocation.STATUS 	, c.getStatus());
		
		return db.insert(DesContract.MarkedLocation.TABLE_NAME, null, cv);
	}
	
	
	public MarkedLocation getRow(int rowId) {
		MarkedLocation cust;
		Cursor c = retrieveCursor(rowId, DesContract.MarkedLocation.TABLE_NAME, MarkedLocation_PROJECTION);
		
		if(c.moveToFirst()) {
			cust = createMarkedLocation(c);
		} else {
			cust = null;
		}
		
		c.close();
		
		return cust;
	}
	
	public void delete(String dateBefore) {
		 db.delete(DesContract.MarkedLocation.TABLE_NAME, "datetime < ?", new String[] {dateBefore});
	}
	
	public List<MyList> getAll() {
		List<MyList> list = new ArrayList<MyList>();
		
		String sql = "SELECT t1._id AS id," +
				"strftime('%m/%d/%Y %H:%M:%S',t1.gpstime,'unixepoch','localtime')  AS dtime," +				
				"t1.ccode AS cuscode," + 
				"t2.name AS cusname," +	
				"t2.address," +	 
				"t1.latitude," +
				"t1.longitude," +
				"t1.gpstime," +
				"t1.accuracy," +
				"t1.provider" +
				" FROM MarkedLocations AS t1" +
				" LEFT JOIN customers AS t2" +
				" ON t1.ccode=t2.ccode" +
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
	
	public void delete_old() {
		long past = System.currentTimeMillis()-1000*60*60*24*7l; //24hours ago.
		String sql = "DELETE FROM outbox WHERE datetime < '"+ past +"' ";
		db.rawQuery(sql,null);
	}
	
	
	public int getLastId() {
		int lastId = 0;
		
		String sql = "SELECT _id FROM MarkedLocation ORDER BY _id DESC LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
		    lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		
		return lastId;
	}
	
	
	public void updateStatus(Integer id, Integer status) {
		String sql="UPDATE MarkedLocation SET " +
				" status=" + status + 
				", priority=priority + 1 "  + 
				" WHERE _id=" + id;
		
		db.execSQL(sql);
	}
	
	
	private MarkedLocation createMarkedLocation(Cursor c) {
		MarkedLocation MarkedLocation = new MarkedLocation();		
		MarkedLocation.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		MarkedLocation.setCustomerCode(c.getString(
				c.getColumnIndex(DesContract.MarkedLocation.CCODE)));
		MarkedLocation.setGpstime(c.getString(
				c.getColumnIndex(DesContract.MarkedLocation.GPSTIME)));				
		MarkedLocation.setLatitude(c.getDouble(
				c.getColumnIndex(DesContract.MarkedLocation.LATITUDE)));
		MarkedLocation.setLongitude(c.getDouble(
				c.getColumnIndex(DesContract.MarkedLocation.LONGITUDE)));
		MarkedLocation.setAccuracy(c.getFloat(
				c.getColumnIndex(DesContract.MarkedLocation.ACCURACY)));
		MarkedLocation.setProvider(c.getString(
				c.getColumnIndex(DesContract.MarkedLocation.PROVIDER)));
		MarkedLocation.setStatus(c.getInt(
				c.getColumnIndex(DesContract.MarkedLocation.STATUS)));
		return MarkedLocation;
	}
	
	private MyList createMyList(Cursor c) {
		MyList list = new MyList();
		
		list.setId(c.getInt(0));
		list.setDateTime(c.getString(1));
		list.setCustomerCode(c.getString(2));
		list.setCustomerName(c.getString(3));
		list.setAddress(c.getString(4));		
	
		String transaction = "LAT: " + c.getString(5) +"\n" +
				"LNG: " + c.getString(6) +"\n" +
				"TIME: " + c.getString(7) +"\n" +
				"ACCURACY: " + c.getString(8) +"\n" +
				"PROVIDER: " + c.getString(9) +"\n";
		
		list.setTransaction(transaction);
		return list;
	
	}
		
	
}
