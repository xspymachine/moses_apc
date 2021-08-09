package com.xpluscloud.moses_apc.dbase;

import android.content.Context;
import android.content.SharedPreferences;

public class WipeDataDbManager extends DbManager{

	public WipeDataDbManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void dropTables(){
		db.execSQL(DROP_TABLE_CUSTOMERS);
		db.execSQL(DROP_TABLE_ITEMS);
		db.execSQL(DROP_TABLE_INVENTORY);
		db.execSQL(DROP_TABLE_RETURNS);
		db.execSQL(DROP_TABLE_DELIVERIES);
		db.execSQL(DROP_TABLE_COLLECTIBLES);
		db.execSQL(DROP_TABLE_COLLECTIONS);
		db.execSQL(DROP_TABLE_CALL_SHEETS);
		db.execSQL(DROP_TABLE_CALL_SHEET_ITEMS);
		db.execSQL(DROP_TABLE_COVERAGE_PLAN);
		db.execSQL(DROP_TABLE_PERIOD);
		db.execSQL(DROP_TABLE_TIME_INOUT);
		db.execSQL(DROP_TABLE_SALESCALL);
		db.execSQL(DROP_TABLE_MARKED_LOCATION);
		db.execSQL(DROP_TABLE_SIGNATURE);
		db.execSQL(DROP_TABLE_PICTURE);
		db.execSQL(DROP_TABLE_TRAIL);		
		db.execSQL(DROP_TABLE_INBOX);
		db.execSQL(DROP_TABLE_OUTBOX);
		db.execSQL(DROP_TABLE_SETTINGS);
		db.execSQL(DROP_TABLE_CUSTITEM);
		db.execSQL(DROP_TABLE_CMPITEMS);
		
		clearPrefs();
		
	}
	
	private void clearPrefs(){
		SharedPreferences settings = context.getSharedPreferences("MosesSettings", Context.MODE_PRIVATE);
		settings.edit().clear().commit();
	}
	
	private static final String DROP_TABLE_ITEMS 			= "DELETE FROM " + DesContract.Item.TABLE_NAME;
	private static final String DROP_TABLE_INVENTORY 		= "DELETE FROM " + DesContract.Inventory.TABLE_NAME;
	private static final String DROP_TABLE_RETURNS 			= "DELETE FROM " + DesContract.Return.TABLE_NAME;
	private static final String DROP_TABLE_CUSTOMERS 		= "DELETE FROM " + DesContract.Customer.TABLE_NAME;
	private static final String DROP_TABLE_COVERAGE_PLAN 	= "DELETE FROM " + DesContract.CoveragePlan.TABLE_NAME;
	private static final String DROP_TABLE_PERIOD 			= "DELETE FROM " + DesContract.Period.TABLE_NAME;
	private static final String DROP_TABLE_TIME_INOUT	 	= "DELETE FROM " + DesContract.TimeInOut.TABLE_NAME;
	private static final String DROP_TABLE_SALESCALL 		= "DELETE FROM " + DesContract.SalesCall.TABLE_NAME;
	private static final String DROP_TABLE_DELIVERIES 		= "DELETE FROM " + DesContract.Delivery.TABLE_NAME;
	private static final String DROP_TABLE_COLLECTIBLES 	= "DELETE FROM " + DesContract.Collectible.TABLE_NAME;
	private static final String DROP_TABLE_COLLECTIONS 		= "DELETE FROM " + DesContract.Collection.TABLE_NAME;
	private static final String DROP_TABLE_CALL_SHEETS 		= "DELETE FROM " + DesContract.CallSheet.TABLE_NAME;
	private static final String DROP_TABLE_CALL_SHEET_ITEMS = "DELETE FROM " + DesContract.CallSheetItem.TABLE_NAME;
	private static final String DROP_TABLE_MARKED_LOCATION 	= "DELETE FROM " + DesContract.MarkedLocation.TABLE_NAME;
	private static final String DROP_TABLE_SIGNATURE	 	= "DELETE FROM " + DesContract.Signature.TABLE_NAME;
	private static final String DROP_TABLE_PICTURE		 	= "DELETE FROM " + DesContract.Picture.TABLE_NAME;
	private static final String DROP_TABLE_TRAIL 			= "DELETE FROM " + DesContract.Trail.TABLE_NAME;
	private static final String DROP_TABLE_INBOX 			= "DELETE FROM " + DesContract.Inbox.TABLE_NAME;
	private static final String DROP_TABLE_OUTBOX			= "DELETE FROM " + DesContract.Outbox.TABLE_NAME;
	private static final String DROP_TABLE_SETTINGS			= "DELETE FROM " + DesContract.Setting.TABLE_NAME;
	private static final String DROP_TABLE_CUSTITEM			= "DELETE FROM " + DesContract.CustItem.TABLE_NAME;
	private static final String DROP_TABLE_CMPITEMS			= "DELETE FROM " + DesContract.CustItemList.TABLE_NAME;

}
