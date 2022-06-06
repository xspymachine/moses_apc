package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.xpluscloud.moses_apc.getset.MyList;
import com.xpluscloud.moses_apc.getset.Picture;

import java.util.ArrayList;
import java.util.List;

public class PictureDbManager extends DbManager {
	
	public static final String[] Picture_PROJECTION = new String[] {
		DesContract.Picture._ID,
		DesContract.Picture.DATETIME,
		DesContract.Picture.CCODE,
		DesContract.Picture.FILENAME,
		DesContract.Picture.BA,  
		DesContract.Picture.STATUS
	};
	
	public PictureDbManager(Context context) {
		super(context);
	}
	
	public long Add(Picture c) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Picture.DATETIME	, c.getDatetime());
		cv.put(DesContract.Picture.CCODE    , c.getCustomerCode());
		cv.put(DesContract.Picture.FILENAME , c.getFilename());
		cv.put(DesContract.Picture.BA   	, c.getBa());
		cv.put(DesContract.Picture.STATUS 	, c.getStatus());
		
		Long id = db.insert(DesContract.Picture.TABLE_NAME, null, cv);
		
		//Delete Old data
		delete_old();		
		
		return id;
	}
	
	
	public Picture getRow(int rowId) {
		Picture cust;
		Cursor c = retrieveCursor(rowId, DesContract.Picture.TABLE_NAME, Picture_PROJECTION);
		
		if(c.moveToFirst()) {
			cust = createPicture(c);
		} else {
			cust = null;
		}
		
		c.close();
		
		return cust;
	}
	
	public void delete(String dateBefore) {
		 db.delete(DesContract.Picture.TABLE_NAME, "datetime < ?", new String[] {dateBefore});
	}

	public int getFileStatus(String filename){
		int status = 0;
		String sql = "SELECT status FROM pictures WHERE filename = '"+filename+"'";
		Cursor c =db.rawQuery(sql,null);
		if(c.moveToFirst()) status =c.getInt(0);

		c.close();
		return status;
	}

	public void updateFilenameStatus(String fname){
		String where = DesContract.Picture.FILENAME + " = '" + fname + "'";
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Picture.STATUS, 1);
		db.update(DesContract.Picture.TABLE_NAME,cv,where,null);
	}
	
	public List<MyList> getAll() {
		List<MyList> list = new ArrayList<MyList>();
		
		String sql = "SELECT t1._id AS id," +
				"strftime('%m/%d/%Y %H:%M:%S',t1.datetime,'unixepoch','localtime')  AS dtime," +				
				"t1.ccode AS cuscode," + 
				"t2.name AS cusname," +	
				"t2.address," +	
				"t1.filename" + 
				" FROM pictures AS t1" +
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
		long past = System.currentTimeMillis()-1000*60*60*24*15l; //24hours ago.
		String sql = "DELETE FROM pictures WHERE datetime < '"+ past +"' ";
		db.rawQuery(sql,null);
	}
	
	
	public int getLastId() {
		int lastId = 0;
		
		String sql = "SELECT _id FROM pictures ORDER BY _id DESC LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
		    lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		
		return lastId;
	}
	
	
	public void updateStatus(Integer id, Integer status) {
		String sql="UPDATE pictures SET " +
				" status=" + status + 
				", priority=priority + 1 "  + 
				" WHERE _id=" + id;
		
		db.execSQL(sql);
	}
	
	
	private Picture createPicture(Cursor c) {
		Picture Picture = new Picture();		
		Picture.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		Picture.setDatetime(c.getString(
				c.getColumnIndex(DesContract.Picture.DATETIME)));
		Picture.setCustomerCode(c.getString(
				c.getColumnIndex(DesContract.Picture.CCODE)));
		Picture.setFilename(c.getString(
				c.getColumnIndex(DesContract.Picture.FILENAME)));
		Picture.setBa(c.getInt(
				c.getColumnIndex(DesContract.Picture.BA)));
		Picture.setStatus(c.getInt(
				c.getColumnIndex(DesContract.Picture.STATUS)));
		return Picture;
	}
	
	private MyList createMyList(Cursor c) {
		MyList list = new MyList();
		
		list.setId(c.getInt(0));
		list.setDateTime(c.getString(1));
		list.setCustomerCode(c.getString(2));
		list.setCustomerName(c.getString(3));
		list.setAddress(c.getString(4));
		list.setTransaction(c.getString(5));		
		return list;
	
	}
		
	
}
