package com.xpluscloud.mosesshell_davao.dbase;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class DbManager {
	
	protected DesDbHelper dbHelper;
	protected SQLiteDatabase db;
	protected Context context;
	
	public DbManager(Context context) {
		this.context = context;
	}
	
	public DbManager open() throws SQLException {
		dbHelper = new DesDbHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void beginTransaction(){
		db.beginTransaction();
	}
	public void setTransactionSuccessful(){
		db.setTransactionSuccessful();
	}
	public void endTransaction(){
		db.endTransaction();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Cursor retrieveCursor(long Id, String table, String[] projection) {
		return db.query(true, table, projection, projection[0] + " = " + Id, null, null, null, null, null);
	}
	
	public Cursor retrieveCursor(String table, String[] projection) {
		return db.query(table, projection, null, null, null, null, null);
	}
	
	public Cursor retrieveCursor(String table, String[] projection, String orderby, String limit) {
		return db.query(table, projection, null, null, null, null, orderby, limit);		
	}
	
	public Cursor retrieveCursor(String table, String[] projection, String orderby) {
		return db.query(true, table, projection, null, null, null, null, orderby, null);
	}
	
	public Cursor getWhere(String table, String[] projection, String where, String orderby) {
		return db.query(table, projection, where, null, null, null, orderby);		
	}
	
	
}
