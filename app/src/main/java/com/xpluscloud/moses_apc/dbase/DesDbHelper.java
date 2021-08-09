package com.xpluscloud.moses_apc.dbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DesDbHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 2012;
	public static final String DATABASE_NAME = "MosesShellSFA.db";
	
	public DesDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CUSTOMERS);
		db.execSQL(CREATE_TABLE_ITEMS);		
		db.execSQL(CREATE_TABLE_INVENTORY);
		db.execSQL(CREATE_TABLE_INVENTORY_ITEMS);
		db.execSQL(CREATE_TABLE_COMPETITOR);
		db.execSQL(CREATE_TABLE_COMPETITOR_ITEMS);
		db.execSQL(CREATE_TABLE_RETURNS);	
		db.execSQL(CREATE_TABLE_COVERAGE_PLAN);
		db.execSQL(CREATE_TABLE_PERIOD);
		db.execSQL(CREATE_TABLE_TIME_INOUT);
		db.execSQL(CREATE_TABLE_SALESCALLS);
		db.execSQL(CREATE_TABLE_DELIVERIES);
		db.execSQL(CREATE_TABLE_COLLECTIBLES);
		db.execSQL(CREATE_TABLE_COLLECTIONS);		
		db.execSQL(CREATE_TABLE_CALL_SHEETS);
		db.execSQL(CREATE_TABLE_CALL_SHEET_ITEMS);
		db.execSQL(CREATE_TABLE_MARKED_LOCATIONS);
		db.execSQL(CREATE_TABLE_SIGNATURES);
		db.execSQL(CREATE_TABLE_PICTURES);
		db.execSQL(CREATE_TABLE_TRAILS);
		db.execSQL(CREATE_TABLE_INBOX);
		db.execSQL(CREATE_TABLE_OUTBOX);
		db.execSQL(CREATE_TABLE_SETTINGS);
		db.execSQL(CREATE_TABLE_CUSTITEM);
		db.execSQL(CREATE_TABLE_CMPITEMS);
		db.execSQL(CREATE_TABLE_REGION);
		db.execSQL(CREATE_TABLE_OWNER);
		db.execSQL(CREATE_TABLE_PURCHASER);
		db.execSQL(CREATE_TABLE_ACCTTYPES);
		db.execSQL(CREATE_TABLE_CHECKLISTS);
		db.execSQL(CREATE_TABLE_TRUCKS);
		db.execSQL(CREATE_TABLE_TRUCKSLIST);
		db.execSQL(CREATE_TABLE_WAREHOUSEITEMS);
		db.execSQL(CREATE_TABLE_MERCHANDISING);
		db.execSQL(CREATE_TABLE_SUPPLIERS);
		db.execSQL(CREATE_TABLE_WORKWITH);
		db.execSQL(CREATE_TABLE_CALLSHEETSERVED);
		db.execSQL(CREATE_TABLE_GENERALPROFILE);
        db.execSQL(CREATE_TABLE_CALLCHECKLIST);
        db.execSQL(CREATE_TABLE_CCALLCHECKLIST);
        db.execSQL(CREATE_TABLE_CUSTOMERISSUES);
		db.execSQL(CREATE_TABLE_RETAILDATA);
		db.execSQL(CREATE_TABLE_SURVEYDATA);
		db.execSQL(CREATE_TABLE_CUSTOMERMOREDATA);
		db.execSQL(CREATE_TABLE_SALESTARGET);
		db.execSQL(CREATE_TABLE_ITEMSCATEGORY);
		db.execSQL(CREATE_TABLE_ITEMSSUBCATEGORY);
		db.execSQL(CREATE_TABLE_CMPITEMSCATEGORY);
		db.execSQL(CREATE_TABLE_CMPITEMSSUBCATEGORY);
		db.execSQL(CREATE_TABLE_CUSTOMERPROMO);

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		switch (oldVersion){
			case 2007: db.execSQL("ALTER TABLE items ADD COLUMN share_pts INTEGER");
			case 2008: db.execSQL("ALTER TABLE citem ADD COLUMN bp_name TEXT");
			case 2009: db.execSQL("ALTER TABLE items ADD COLUMN liters TEXT");
			case 2010: db.execSQL("ALTER TABLE callsheetitems ADD COLUMN sharetpts INTEGER");
			case 2011: db.execSQL("ALTER TABLE customers ADD COLUMN a_r TEXT");
		}
//		db.execSQL(DROP_TABLE_CUSTOMERS);
//		db.execSQL(DROP_TABLE_ITEMS);
//		db.execSQL(DROP_TABLE_INVENTORY);
//		db.execSQL(DROP_TABLE_INVENTORY_ITEMS);
//		db.execSQL(DROP_TABLE_COMPETITOR);
//		db.execSQL(DROP_TABLE_COMPETITOR_ITEMS);
//		db.execSQL(DROP_TABLE_RETURNS);
//		db.execSQL(DROP_TABLE_DELIVERIES);
//		db.execSQL(DROP_TABLE_COLLECTIBLES);
//		db.execSQL(DROP_TABLE_COLLECTIONS);
//		db.execSQL(DROP_TABLE_CALL_SHEETS);
//		db.execSQL(DROP_TABLE_CALL_SHEET_ITEMS);
//		db.execSQL(DROP_TABLE_COVERAGE_PLAN);
//		db.execSQL(DROP_TABLE_PERIOD);
//		db.execSQL(DROP_TABLE_TIME_INOUT);
//		db.execSQL(DROP_TABLE_SALESCALL);
//		db.execSQL(DROP_TABLE_MARKED_LOCATION);
//		db.execSQL(DROP_TABLE_SIGNATURE);
//		db.execSQL(DROP_TABLE_PICTURE);
//		db.execSQL(DROP_TABLE_TRAIL);
//		db.execSQL(DROP_TABLE_INBOX);
//		db.execSQL(DROP_TABLE_OUTBOX);
//		db.execSQL(DROP_TABLE_SETTINGS);
//		db.execSQL(DROP_TABLE_CUSTITEM);
//		db.execSQL(DROP_TABLE_CMPITEMS);
//		db.execSQL(DROP_TABLE_REGION);
//		db.execSQL(DROP_TABLE_OWNER);
//		db.execSQL(DROP_TABLE_PURCHASER);
//		db.execSQL(DROP_TABLE_ACCTTYPES);
//		db.execSQL(DROP_TABLE_CHECKLISTS);
//		db.execSQL(DROP_TABLE_CHECKLISTS);
//		db.execSQL(DROP_TABLE_TRUCKS);
//		db.execSQL(DROP_TABLE_TRUCKSLIST);
//		db.execSQL(DROP_TABLE_WAREHOUSEITEMS);
//		db.execSQL(DROP_TABLE_MERCHANDISING);
//		db.execSQL(DROP_TABLE_SUPPLIERS);
//		db.execSQL(DROP_TABLE_WORKWITH);
//		db.execSQL(DROP_TABLE_CALLSHEETSERVED);
//		db.execSQL(DROP_TABLE_GENERALPROFILE);
//        db.execSQL(DROP_TABLE_CALLCHECKLIST);
//        db.execSQL(DROP_TABLE_CCALLCHECKLIST);
//        db.execSQL(DROP_TABLE_CUSTOMERISSUES);
//		db.execSQL(DROP_TABLE_RETAILDATA);
//		db.execSQL(DROP_TABLE_SURVEYDATA);
//		db.execSQL(DROP_TABLE_CUSTOMERMOREDATA);
//		db.execSQL(DROP_TABLE_SALESTARGET);
//		db.execSQL(DROP_TABLE_ITEMSCATEGORY);
//		db.execSQL(DROP_TABLE_ITEMSSUBCATEGORY);
//		db.execSQL(DROP_TABLE_CMPITEMSCATEGORY);
//		db.execSQL(DROP_TABLE_CMPITEMSSUBCATEGORY);
//		db.execSQL(DROP_TABLE_CUSTOMERPROMO);
//		onCreate(db);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
	
	private static final String CREATE_TABLE_CUSTOMERS =
		"CREATE TABLE " + DesContract.Customer.TABLE_NAME + " ("
		+ DesContract.Customer._ID            	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.Customer.CCODE  			+ " TEXT NOT NULL UNIQUE, "
		+ DesContract.Customer.NAME           	+ " TEXT NOT NULL, "
		+ DesContract.Customer.ADDRESS        	+ " TEXT, "
		+ DesContract.Customer.BRGY           	+ " TEXT, "
		+ DesContract.Customer.CITY           	+ " TEXT, "
		+ DesContract.Customer.STATE          	+ " TEXT, "
		+ DesContract.Customer.CONTACT_NUMBER 	+ " TEXT, "
		+ DesContract.Customer.ACCT_TYPEID 		+ " INTEGER, "
		+ DesContract.Customer.LATITUDE       	+ " REAL, "
		+ DesContract.Customer.LONGITUDE      	+ " REAL, "
		+ DesContract.Customer.CPLAN_CODE		+ " TEXT, "
		+ DesContract.Customer.DISCOUNT			+ " REAL, "
		+ DesContract.Customer.PICTURE			+ " TEXT, "
		+ DesContract.Customer.BUFFER			+ " INTEGER, "
		+ DesContract.Customer.CASH_SALES		+ " INTEGER, "
		+ DesContract.Customer.TARGET			+ " INTEGER, "
		+ DesContract.Customer.TERMID			+ " INTEGER, "
		+ DesContract.Customer.TYPEID			+ " INTEGER, "
		+ DesContract.Customer.AR				+ " TEXT, "
		+ DesContract.Customer.STATUS		 	+ " INTEGER )";
	
	
	
	private static final String CREATE_TABLE_ITEMS =
		"CREATE TABLE " + DesContract.Item.TABLE_NAME + " ("
		+ DesContract.Item._ID         			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.Item.ITEM_CODE        	+ " TEXT NOT NULL UNIQUE, "
		+ DesContract.Item.DESCRIPTION      	+ " TEXT NOT NULL, "
		+ DesContract.Item.CATEGORY_CODE    	+ " TEXT, "
		+ DesContract.Item.PACK_BARCODE 		+ " TEXT, "
		+ DesContract.Item.BARCODE 				+ " TEXT, "
		+ DesContract.Item.QTY_PER_PACK			+ " REAL, "
		+ DesContract.Item.PRICE_PER_PACK   	+ " REAL, "
		+ DesContract.Item.PRICE_PER_UNIT   	+ " REAL, "
		+ DesContract.Item.PACKING			   	+ " REAL, "
		+ DesContract.Item.PRIORITY				+ " INTEGER, "
		+ DesContract.Item.CATID				+ " INTEGER, "
		+ DesContract.Item.SUBCATID				+ " INTEGER, "
		+ DesContract.Item.SHAREPTS				+ " INTEGER, "
		+ DesContract.Item.LITERS				+ " INTEGER, "
		+ DesContract.Item.ITEM_ID				+ " INTEGER );";
	
	private static final String CREATE_INDEX_ITEMS_ITEM_ID =
		"CREATE INDEX ItemsItemId ON " + DesContract.Item.TABLE_NAME + 
		" (" + DesContract.Item.ITEM_ID +")";
	
	private static final String CREATE_INDEX_ITEMS_PRIORITY =
			"CREATE INDEX ItemsPriority ON " + DesContract.Item.TABLE_NAME + 
			" (" + DesContract.Item.PRIORITY +")";
	
	private static final String CREATE_TABLE_INVENTORY =
		"CREATE TABLE " + DesContract.Inventory.TABLE_NAME + " ("
		+ DesContract.Inventory._ID         		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.Inventory.INNO        		+ " INTEGER NOT NULL UNIQUE, "
		+ DesContract.Inventory.INCODE        		+ " TEXT NOT NULL UNIQUE, "
		+ DesContract.Inventory.CCODE        		+ " TEXT NOT NULL, "
		+ DesContract.Inventory.DATE        		+ " TEXT NOT NULL, "
		+ DesContract.Inventory.STATUS 				+ " INTEGER );";
	private static final String CREATE_TABLE_INVENTORY_ITEMS =
		"CREATE TABLE " + DesContract.Inventory.TABLE_NAME2 + " ("
		+ DesContract.Inventory._ID         		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.Inventory.INCODE        		+ " TEXT NOT NULL, "
		+ DesContract.Inventory.ITEM_CODE   		+ " TEXT NOT NULL, "
		+ DesContract.Inventory.PCKG				+ " TEXT NOT NULL, "
		+ DesContract.Inventory.QTY					+ " INTEGER, "
		+ DesContract.Inventory.PRICE   			+ " REAL,"
		+ DesContract.Inventory.STATUS 				+ " INTEGER );";

	private static final String CREATE_TABLE_COMPETITOR =
			"CREATE TABLE " + DesContract.Competitors.TABLE_NAME + " ("
					+ DesContract.Competitors._ID         		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ DesContract.Competitors.INNO        		+ " INTEGER NOT NULL UNIQUE, "
					+ DesContract.Competitors.INCODE        		+ " TEXT NOT NULL UNIQUE, "
					+ DesContract.Competitors.CCODE        		+ " TEXT NOT NULL, "
					+ DesContract.Competitors.DATE        		+ " TEXT NOT NULL, "
					+ DesContract.Competitors.STATUS 				+ " INTEGER );";
	private static final String CREATE_TABLE_COMPETITOR_ITEMS =
			"CREATE TABLE " + DesContract.Competitors.TABLE_NAME2 + " ("
					+ DesContract.Competitors._ID         		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ DesContract.Competitors.INCODE        		+ " TEXT NOT NULL, "
					+ DesContract.Competitors.ITEM_CODE   		+ " TEXT NOT NULL, "
					+ DesContract.Competitors.PCKG				+ " TEXT NOT NULL, "
					+ DesContract.Competitors.QTY					+ " INTEGER, "
					+ DesContract.Competitors.PRICE   			+ " REAL,"
					+ DesContract.Competitors.STATUS 				+ " INTEGER );";
	
	private static final String CREATE_TABLE_RETURNS =
		"CREATE TABLE " + DesContract.Return.TABLE_NAME + " ("
		+ DesContract.Return._ID         			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.Return.DATE        			+ " TEXT NOT NULL, "
		+ DesContract.Return.PRNO   				+ " TEXT NOT NULL , "
		+ DesContract.Return.PRCODE   				+ " TEXT NOT NULL , "
		+ DesContract.Return.CCODE   				+ " TEXT NOT NULL , "
		+ DesContract.Return.ITEM_CODE   			+ " TEXT NOT NULL , "
		+ DesContract.Return.PCKG					+ " TEXT NOT NULL, "
		+ DesContract.Return.QTY					+ " INTEGER, "
		+ DesContract.Return.PRICE   				+ " REAL," 
		+ DesContract.Return.STATUS 				+ " INTEGER );";

	
	private static final String CREATE_TABLE_DELIVERIES =
		"CREATE TABLE " + DesContract.Delivery.TABLE_NAME + " ("
		+ DesContract.Delivery._ID          		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.Delivery.DATE         		+ " TEXT NOT NULL, "
		+ DesContract.Delivery.INVOICENO 			+ " TEXT NOT NULL, "
		+ DesContract.Delivery.SONO 				+ " TEXT NOT NULL, "
		+ DesContract.Delivery.CCODE  				+ " TEXT NOT NULL, "
		+ DesContract.Delivery.ITEM_CODE			+ " TEXT NOT NULL, "
		+ DesContract.Delivery.PCKG					+ " TEXT NOT NULL, " 
		+ DesContract.Delivery.PRICE				+ " REAL, " 
		+ DesContract.Delivery.QTY    				+ " INTEGER, " 
		+ DesContract.Delivery.STATUS 				+ " INTEGER );";
	
	private static final String CREATE_TABLE_COLLECTIBLES =
		"CREATE TABLE " + DesContract.Collectible.TABLE_NAME + " ("
		+ DesContract.Collectible._ID          	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.Collectible.DATE         	+ " TEXT NOT NULL, "
		+ DesContract.Collectible.INVOICENO 	+ " TEXT NOT NULL, "
		+ DesContract.Collectible.CCODE  		+ " TEXT NOT NULL, "
		+ DesContract.Collectible.AMOUNT		+ " REAL, " 
		+ DesContract.Collectible.STATUS 		+ " INTEGER );";
	
	private static final String CREATE_TABLE_COLLECTIONS =
			"CREATE TABLE " + DesContract.Collection.TABLE_NAME + " ("
			+ DesContract.Collection._ID          	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.Collection.DATE         	+ " TEXT NOT NULL, "
			+ DesContract.Collection.INVOICENO 		+ " TEXT NOT NULL, "
			+ DesContract.Collection.CCODE  		+ " TEXT NOT NULL, "
			+ DesContract.Collection.CASH_AMOUNT	+ " REAL, " 
			+ DesContract.Collection.CHECK_AMOUNT	+ " REAL, " 
			+ DesContract.Collection.CHECKNO  		+ " TEXT, "
			+ DesContract.Collection.CM_AMOUNT		+ " REAL, " 
			+ DesContract.Collection.CMNO  			+ " TEXT, "
			+ DesContract.Collection.DEPOSIT_AMOUNT	+ " REAL, " 
			+ DesContract.Collection.BANKNAME  		+ " TEXT, "
			+ DesContract.Collection.BANKBRANCH 	+ " TEXT, "
			+ DesContract.Collection.DEPOSIT_TRNO  	+ " TEXT, "
			+ DesContract.Collection.ORNO  			+ " TEXT, "			
			+ DesContract.Collection.NC_REASON		+ " INTEGER, "
			+ DesContract.Collection.REMARKS		+ " TEXT, "
			+ DesContract.Collection.STATUS 		+ " INTEGER );";
	
	private static final String CREATE_TABLE_CALL_SHEETS =
		"CREATE TABLE " + DesContract.CallSheet.TABLE_NAME + " ("
		+ DesContract.CallSheet._ID          		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.CallSheet.DATE         		+ " TEXT NOT NULL, "
		+ DesContract.CallSheet.CSCODE  			+ " TEXT NOT NULL UNIQUE, "
		+ DesContract.CallSheet.SONO 				+ " INTEGER, "
		+ DesContract.CallSheet.CCODE  				+ " TEXT NOT NULL, "
		+ DesContract.CallSheet.BUFFER				+ " INTEGER, " 
		+ DesContract.CallSheet.CASH_SALES    		+ " INTEGER, " 
		+ DesContract.CallSheet.PAYMENT    			+ " REAL, " 
		+ DesContract.CallSheet.SUPPLIER			+ " TEXT, "
		+ DesContract.CallSheet.STATUS 				+ " INTEGER );";
	
	private static final String CREATE_INDEX_CST_DATE =
			"CREATE INDEX CST_DateIndex ON " + DesContract.CallSheet.TABLE_NAME + 
			" (" + DesContract.CallSheet.DATE +")";
	
	private static final String CREATE_INDEX_CST_CCODE =
			"CREATE INDEX CST_CCodeIndex ON " + DesContract.CallSheet.TABLE_NAME + 
			" (" + DesContract.CallSheet.CCODE +")";
	
	private static final String CREATE_INDEX_CST_CASH_SALES =
			"CREATE INDEX CST_Cash_Sales_Index ON " + DesContract.CallSheet.TABLE_NAME + 
			" (" + DesContract.CallSheet.CASH_SALES +")";
	
	private static final String CREATE_TABLE_CALL_SHEET_ITEMS =
		"CREATE TABLE " + DesContract.CallSheetItem.TABLE_NAME + " ("
		+ DesContract.CallSheetItem._ID          	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.CallSheetItem.CSCODE 			+ " TEXT NOT NULL, "
		+ DesContract.CallSheetItem.ITEM_CODE 		+ " TEXT NOT NULL, "
		+ DesContract.CallSheetItem.PCKG         	+ " TEXT NOT NULL, "			
		+ DesContract.CallSheetItem.PAST_INVT		+ " INTEGER, "
		+ DesContract.CallSheetItem.DELIVERY		+ " INTEGER, " 
		+ DesContract.CallSheetItem.PRESENT_INVT	+ " INTEGER, " 
		+ DesContract.CallSheetItem.OFFTAKE	 		+ " INTEGER, "
		+ DesContract.CallSheetItem.ICO				+ " INTEGER, "
		+ DesContract.CallSheetItem.SUGGESTED		+ " INTEGER, " 
		+ DesContract.CallSheetItem.ORDER_QTY    	+ " INTEGER, "
		+ DesContract.CallSheetItem.PRICE 			+ " REAL, "
		+ DesContract.CallSheetItem.SHARETPTS		+ " INTEGER, "
		+ DesContract.CallSheetItem.STATUS 			+ " INTEGER );";
	
	private static final String CREATE_INDEX_CSI_CSCODE =
			"CREATE INDEX CSI_CSCode_Index ON " + DesContract.CallSheetItem.TABLE_NAME + 
			" (" + DesContract.CallSheetItem.CSCODE +")";
	
	private static final String CREATE_INDEX_CSI_ITEMCODE =
			"CREATE INDEX CSI_ItemCode_Index ON " + DesContract.CallSheetItem.TABLE_NAME + 
			" (" + DesContract.CallSheetItem.ITEM_CODE +")";
	
	private static final String CREATE_INDEX_CSI_PCKG =
			"CREATE INDEX CSI_Pckg_Index ON " + DesContract.CallSheetItem.TABLE_NAME + 
			" (" + DesContract.CallSheetItem.PCKG +")";

	
	private static final String CREATE_TABLE_COVERAGE_PLAN =
		"CREATE TABLE " + DesContract.CoveragePlan.TABLE_NAME + " ("
		+ DesContract.CoveragePlan._ID          	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.CoveragePlan.CPID  			+ " INTEGER NOT NULL UNIQUE, "
		+ DesContract.CoveragePlan.CPLAN_CODE  		+ " TEXT NOT NULL UNIQUE, "
		+ DesContract.CoveragePlan.DESCRIPTION  	+ " TEXT, "
		+ DesContract.CoveragePlan.WEEK_SCHEDULE	+ " TEXT, "
		+ DesContract.CoveragePlan.DAY_SCHEDULE 	+ " TEXT, " 				
		+ DesContract.CoveragePlan.STATUS 			+ " INTEGER );";
	
	private static final String CREATE_TABLE_PERIOD =
			"CREATE TABLE " + DesContract.Period.TABLE_NAME + " ("
			+ DesContract.Period._ID          	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.Period.YEAR  			+ " INTEGER, "
			+ DesContract.Period.NWEEK  		+ " INTEGER NOT NULL, "
			+ DesContract.Period.WEEK_START 	+ " TEXT NOT NULL, "
			+ DesContract.Period.WEEK_END		+ " TEXT NOT NULL, "			 				
			+ DesContract.Period.STATUS 		+ " INTEGER );";	
	
	private static final String CREATE_TABLE_TIME_INOUT =
		"CREATE TABLE " + DesContract.TimeInOut.TABLE_NAME + " ("
		+ DesContract.TimeInOut._ID          	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.TimeInOut.CCODE  			+ " TEXT NOT NULL, "
		+ DesContract.TimeInOut.DATETIME  		+ " TEXT, "
		+ DesContract.TimeInOut.INOUT			+ " TEXT NOT NULL, "
		+ DesContract.TimeInOut.LATITUDE 		+ " REAL, " 	
		+ DesContract.TimeInOut.LONGITUDE 		+ " REAL, "
		+ DesContract.TimeInOut.STATUS 			+ " INTEGER );";
	
	private static final String CREATE_TABLE_SALESCALLS =
		"CREATE TABLE " + DesContract.SalesCall.TABLE_NAME + " ("
		+ DesContract.SalesCall._ID          	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.SalesCall.CCODE  			+ " TEXT NOT NULL, "
		+ DesContract.SalesCall.DATETIME  		+ " TEXT, "
		+ DesContract.SalesCall.BLOB  			+ " TEXT, "			 				
		+ DesContract.SalesCall.STATUS 			+ " INTEGER );";
	
	
	private static final String CREATE_TABLE_MARKED_LOCATIONS =
		"CREATE TABLE " + DesContract.MarkedLocation.TABLE_NAME + " ("
		+ DesContract.MarkedLocation._ID          	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.MarkedLocation.CCODE  		+ " TEXT NOT NULL, "
		+ DesContract.MarkedLocation.GPSTIME  		+ " TEXT NOT NULL, "			
		+ DesContract.MarkedLocation.LATITUDE 		+ " REAL NOT NULL, " 	
		+ DesContract.MarkedLocation.LONGITUDE 		+ " REAL NOT NULL, "
		+ DesContract.MarkedLocation.ACCURACY 		+ " REAL, "
		+ DesContract.MarkedLocation.PROVIDER 		+ " TEXT, "
		+ DesContract.MarkedLocation.STATUS 		+ " INTEGER );";
	
	
	private static final String CREATE_TABLE_SIGNATURES =
		"CREATE TABLE " + DesContract.Signature.TABLE_NAME + " ("
		+ DesContract.Signature._ID          	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.Signature.CCODE  			+ " TEXT NOT NULL, "
		+ DesContract.Signature.DATETIME  		+ " TEXT NOT NULL, "			
		+ DesContract.Signature.FILENAME 		+ " TEXT NOT NULL, " 				
		+ DesContract.Signature.STATUS 			+ " INTEGER );";
	
	
	private static final String CREATE_TABLE_PICTURES =
		"CREATE TABLE " + DesContract.Picture.TABLE_NAME + " ("
		+ DesContract.Picture._ID          		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.Picture.CCODE  			+ " TEXT NOT NULL, "
		+ DesContract.Picture.DATETIME  		+ " TEXT NOT NULL, "
		+ DesContract.Picture.BA  				+ " INTEGER NOT NULL, "
		+ DesContract.Picture.FILENAME 			+ " TEXT NOT NULL, " 				
		+ DesContract.Picture.STATUS 			+ " INTEGER );";
	
	
	private static final String CREATE_TABLE_TRAILS =
		"CREATE TABLE " + DesContract.Trail.TABLE_NAME + " ("
		+ DesContract.Trail._ID          	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "			
		+ DesContract.Trail.GPSTIME  		+ " TEXT NOT NULL, "			
		+ DesContract.Trail.LATITUDE 		+ " REAL NOT NULL, " 	
		+ DesContract.Trail.LONGITUDE 		+ " REAL NOT NULL, "
		+ DesContract.Trail.ACCURACY 		+ " REAL, "
		+ DesContract.Trail.SPEED	 		+ " REAL, "
		+ DesContract.Trail.BEARING 		+ " REAL, "			
		+ DesContract.Trail.PROVIDER 		+ " TEXT, "
		+ DesContract.Trail.STATUS 			+ " INTEGER );";
	
	

	private static final String CREATE_TABLE_INBOX =
		"CREATE TABLE " + DesContract.Inbox.TABLE_NAME + " ("
		+ DesContract.Inbox._ID          		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.Inbox.DATETIME  			+ " TEXT NOT NULL, "
		+ DesContract.Inbox.SENDER   			+ " TEXT NOT NULL, "
		+ DesContract.Inbox.MESSAGE 			+ " TEXT NOT NULL, "							
		+ DesContract.Inbox.STATUS 				+ " INTEGER);";
	
	
	private static final String CREATE_TABLE_OUTBOX =
		"CREATE TABLE " + DesContract.Outbox.TABLE_NAME + " ("
		+ DesContract.Outbox._ID          	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ DesContract.Outbox.DATETIME  		+ " TEXT NOT NULL, "
		+ DesContract.Outbox.RECIPIENT		+ " TEXT NOT NULL, "
		+ DesContract.Outbox.MESSAGE 		+ " TEXT NOT NULL, "	
		+ DesContract.Outbox.PRIORITY 		+ " INTEGER, "
		+ DesContract.Outbox.STATUS 		+ " INTEGER);";	
	
	private static final String CREATE_TABLE_SETTINGS =
			"CREATE TABLE " + DesContract.Setting.TABLE_NAME + " ("
			+ DesContract.Setting._ID          	+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.Setting.KEY  			+ " TEXT NOT NULL UNIQUE, "
			+ DesContract.Setting.VALUE			+ " TEXT NOT NULL, "			
			+ DesContract.Setting.STATUS 		+ " INTEGER);";	
	
	private static final String CREATE_TABLE_CUSTITEM =
			"CREATE TABLE " + DesContract.CustItem.TABLE_NAME + " ("
			+ DesContract.CustItem._ID				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.CustItem.COMCODE			+ " TEXT NOT NULL UNIQUE, "
			+ DesContract.CustItem.CCODE			+ " TEXT NOT NULL, "
			+ DesContract.CustItem.DATETIME			+ " TEXT NOT NULL, "
			+ DesContract.CustItem.ICODE			+ " TEXT NOT NULL, "
			+ DesContract.CustItem.DESCRIPTION		+ " TEXT NOT NULL, "
			+ DesContract.CustItem.CATEGORYCODE		+ " TEXT, "
			+ DesContract.CustItem.DPRICE			+ " TEXT, "
			+ DesContract.CustItem.RPRICE			+ " TEXT, "
			+ DesContract.CustItem.QTY				+ " INTEGER, "
			+ DesContract.CustItem.VOL				+ " INTEGER, "
			+ DesContract.CustItem.IOH				+ " INTEGER, "
			
			+ DesContract.CustItem.TYPE50SU			+ " INTEGER, "
			+ DesContract.CustItem.TYPE22SU			+ " INTEGER, "
			+ DesContract.CustItem.TYPE5SU			+ " INTEGER, "
			+ DesContract.CustItem.TYPE7SU			+ " INTEGER, "
			+ DesContract.CustItem.TYPE50IOH		+ " TEXT , "
			+ DesContract.CustItem.TYPE22IOH		+ " INTEGER, "
			+ DesContract.CustItem.TYPE5IOH			+ " INTEGER, "
			+ DesContract.CustItem.TYPE7IOH			+ " INTEGER, "
			
			+ DesContract.CustItem.REMARKS			+ " TEXT, "
			
			+ DesContract.CustItem.STATUS			+ " INTEGER);";
	
	private static final String CREATE_TABLE_CMPITEMS =
			"CREATE TABLE " + DesContract.CustItemList.TABLE_NAME + " ("
			+ DesContract.CustItemList._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.CustItemList.ITEMCODE		+ " TEXT NOT NULL UNIQUE, "
			+ DesContract.CustItemList.DESCRIPTION	+ " TEXT NOT NULL, "
			+ DesContract.CustItemList.CASE_BARCODE	+ " TEXT, "
			+ DesContract.CustItemList.BARCODE		+ " TEXT, "
			+ DesContract.CustItemList.GROUPID		+ " TEXT, "
			+ DesContract.CustItemList.SUBCATID		+ " INTEGER,"
			+ DesContract.CustItemList.CATID		+ " INTEGER,"
			+ DesContract.CustItemList.BPNAME		+ " TEXT,"
			+ DesContract.CustItemList.STATUS		+ " INTEGER);";
	
	private static final String CREATE_TABLE_ACCTTYPES =
			"CREATE TABLE " + DesContract.AccountTypes.TABLE_NAME + " ("
			+ DesContract.AccountTypes._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.AccountTypes.ACCTTYPE		+ " INTEGER NOT NULL UNIQUE, "
			+ DesContract.AccountTypes.DESCRIPTION 	+ " TEXT NOT NULL, "
			+ DesContract.AccountTypes.STATUS		+ " INTEGER);";
	
	private static final String CREATE_TABLE_REGION =
			"CREATE TABLE " + DesContract.Region.TABLE_NAME + " ("
			+ DesContract.Region._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.Region.REGIONID		+ " INTEGER NOT NULL UNIQUE, "
			+ DesContract.Region.DESCRIPTION 	+ " TEXT NOT NULL, "
			+ DesContract.Region.STATUS			+ " INTEGER);";
	
	private static final String CREATE_TABLE_OWNER =
			"CREATE TABLE " + DesContract.CusOwnerData.TABLE_NAME + " ("
			+ DesContract.CusOwnerData._ID		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.CusOwnerData.CCODE 	+ " TEXT NOT NULL, "
			+ DesContract.CusOwnerData.NAME		+ " TEXT, "
			+ DesContract.CusOwnerData.CSTATUS	+ " TEXT, "
			+ DesContract.CusOwnerData.BDAY		+ " TEXT, "
			+ DesContract.CusOwnerData.HOBINT	+ " TEXT, "
			+ DesContract.CusOwnerData.PHONE	+ " TEXT, "
			+ DesContract.CusOwnerData.EMAIL	+ " TEXT, "
			+ DesContract.CusOwnerData.STATUS	+ " INTEGER,"
			+ "UNIQUE("+DesContract.CusOwnerData.CCODE+","+DesContract.CusOwnerData.STATUS+"));";
	
	private static final String CREATE_TABLE_PURCHASER =
			"CREATE TABLE " + DesContract.CusPurchaserData.TABLE_NAME + " ("
			+ DesContract.CusPurchaserData._ID		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.CusPurchaserData.CCODE 	+ " TEXT NOT NULL UNIQUE, "
			+ DesContract.CusPurchaserData.TRUCKS	+ " TEXT, "
			+ DesContract.CusPurchaserData.DELIVERY	+ " TEXT, "
			+ DesContract.CusPurchaserData.SUPPLIER	+ " TEXT, "
			+ DesContract.CusPurchaserData.WAREHOUSECAP	+ " INTEGER, "
			+ DesContract.CusPurchaserData.STATUS	+ " INTEGER);";
	
	private static final String CREATE_TABLE_TRUCKS =
			"CREATE TABLE " + DesContract.CusTrucksData.TABLE_NAME + " ("
			+ DesContract.CusTrucksData._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.CusTrucksData.CCODE 		+ " TEXT NOT NULL, "
			+ DesContract.CusTrucksData.TRUCKID		+ " INTEGER NOT NULL,"
			+ DesContract.CusTrucksData.TYPE		+ " TEXT, "
			+ DesContract.CusTrucksData.QUANTITY	+ " INTEGER, "
			+ DesContract.CusTrucksData.STATUS		+ " INTEGER);";
	
	private static final String CREATE_TABLE_TRUCKSLIST =
			"CREATE TABLE " + DesContract.CusTrucksList.TABLE_NAME + " ("
			+ DesContract.CusTrucksList._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.CusTrucksList.TRUCKID		+ " TEXT NOT NULL UNIQUE, "
			+ DesContract.CusTrucksList.TRUCK_NAME	+ " TEXT, "
			+ DesContract.CusTrucksList.CAPACITY	+ " TEXT, "
			+ DesContract.CusTrucksList.STATUS		+ " INTEGER);";
	
	private static final String CREATE_TABLE_CHECKLISTS =
			"CREATE TABLE " + DesContract.Checklists.TABLE_NAME + " ("
			+ DesContract.Checklists._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.Checklists.MCID			+ " INTEGER NOT NULL UNIQUE, "
			+ DesContract.Checklists.DESCRIPTION	+ " TEXT, "
			+ DesContract.Checklists.STATUS			+ " INTEGER);";
	
	private static final String CREATE_TABLE_WAREHOUSEITEMS =
			"CREATE TABLE " + DesContract.CusWarehouseItems.TABLE_NAME + " ("
			+ DesContract.CusWarehouseItems._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.CusWarehouseItems.CCODE		+ " TEXT NOT NULL, "
			+ DesContract.CusWarehouseItems.ITEMCODE	+ " TEXT NOT NULL, "
			+ DesContract.CusWarehouseItems.QUANTITY	+ " INTEGER, "
			+ DesContract.CusWarehouseItems.STATUS		+ " INTEGER);";		
	
	private static final String CREATE_TABLE_MERCHANDISING =
			"CREATE TABLE " + DesContract.Merchandising.TABLE_NAME + " ("
			+ DesContract.Merchandising._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.Merchandising.CCODE		+ " TEXT NOT NULL, "
			+ DesContract.Merchandising.REMARKS		+ " TEXT NOT NULL, "
			+ DesContract.Merchandising.CALLSID		+ " INTEGER, "
			+ DesContract.Merchandising.DATE		+ " INTEGER, "
			+ DesContract.Merchandising.STATUS		+ " INTEGER);";		
	
	private static final String CREATE_TABLE_SUPPLIERS =
			"CREATE TABLE " + DesContract.Suppliers.TABLE_NAME + " ("
			+ DesContract.Suppliers._ID				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.Suppliers.SPID			+ " INTEGER NOT NULL UNIQUE, "
			+ DesContract.Suppliers.DEALER			+ " TEXT, "
			+ DesContract.Suppliers.AREA			+ " TEXT, "
			+ DesContract.Suppliers.REGION			+ " TEXT, "
			+ DesContract.Suppliers.STATUS			+ " INTEGER);";
	
	private static final String CREATE_TABLE_WORKWITH =
			"CREATE TABLE " + DesContract.WorkWith.TABLE_NAME + " ("
			+ DesContract.WorkWith._ID				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.WorkWith.WID				+ " INTEGER NOT NULL UNIQUE, "
			+ DesContract.WorkWith.DESCRIPTION		+ " TEXT, "
			+ DesContract.WorkWith.STATUS			+ " INTEGER);";
	
	private static final String CREATE_TABLE_CALLSHEETSERVED =
			"CREATE TABLE " + DesContract.CallSheetServe.TABLE_NAME + " ("
			+ DesContract.CallSheetServe._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.CallSheetServe.SOID			+ " INTEGER UNIQUE, "
			+ DesContract.CallSheetServe.DEVID			+ " TEXT, "
			+ DesContract.CallSheetServe.CSCODE			+ " TEXT, "
			+ DesContract.CallSheetServe.CCODE			+ " TEXT, "
			+ DesContract.CallSheetServe.QTY			+ " INTEGER, "
			+ DesContract.CallSheetServe.DATE			+ " INTEGER, "
			+ DesContract.CallSheetServe.STATUS			+ " INTEGER);";
	
	private static final String CREATE_TABLE_GENERALPROFILE =
			"CREATE TABLE " + DesContract.GeneralProfile.TABLE_NAME + " ("
			+ DesContract.GeneralProfile._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.GeneralProfile.CCODE			+ " TEXT NOT NULL UNIQUE,"
			+ DesContract.GeneralProfile.LOAN			+ " TEXT,"
			+ DesContract.GeneralProfile.ROUTE			+ " TEXT,"
			+ DesContract.GeneralProfile.FREQ			+ " INTEGER,"
			+ DesContract.GeneralProfile.AVERAGE		+ " TEXT,"
			+ DesContract.GeneralProfile.STATUS			+ " INTEGER);";

	private static final String CREATE_TABLE_CALLCHECKLIST =
			"CREATE TABLE " + DesContract.CallChecklist.TABLE_NAME + " ("
					+ DesContract.CallChecklist._ID			    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ DesContract.CallChecklist.CHKL_ID		    + " INTEGER NOT NULL UNIQUE, "
					+ DesContract.CallChecklist.CAT_ID			+ " INTEGER NOT NULL, "
					+ DesContract.CallChecklist.DESCRIPTION	    + " TEXT NOT NULL,"
					+ DesContract.CallChecklist.CATEGORY		+ " TEXT NOT NULL);";

    private static final String CREATE_TABLE_CCALLCHECKLIST =
            "CREATE TABLE " + DesContract.CustomerCallChecklist.TABLE_NAME + " ("
                    + DesContract.CustomerCallChecklist._ID			    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DesContract.CustomerCallChecklist.CCODE		    + " TEXT NOT NULL, "
                    + DesContract.CustomerCallChecklist.CHKID			+ " INTEGER NOT NULL, "
                    + DesContract.CustomerCallChecklist.YES		   	    + " INTEGER NOT NULL, "
					+ DesContract.CustomerCallChecklist.NO		   	    + " INTEGER NOT NULL, "
                    + DesContract.CustomerCallChecklist.GREMARKS		+ " TEXT, "
                    + DesContract.CustomerCallChecklist.DATE	  		+ " TEXT NOT NULL, "
					+ DesContract.CustomerCallChecklist.TIME	  		+ " TEXT NOT NULL, "
                    + DesContract.CustomerCallChecklist.STATUS  		+ " INTEGER, "
                    + "UNIQUE("+DesContract.CustomerCallChecklist.CCODE+","+DesContract.CustomerCallChecklist.CHKID+","+
                    DesContract.CustomerCallChecklist.DATE+"));";

	private static final String CREATE_TABLE_CUSTOMERISSUES =
			"CREATE TABLE " + DesContract.CustomerIssues.TABLE_NAME	+ " ("
			+ DesContract.CustomerIssues._ID		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.CustomerIssues.CODEISSUE	+ " TEXT NOT NULL UNIQUE, "
			+ DesContract.CustomerIssues.CCODE		+ " TEXT NOT NULL, "
			+ DesContract.CustomerIssues.ISSUE		+ " TEXT NOT NULL, "
			+ DesContract.CustomerIssues.DATEOPEN	+ " TEXT NOT NULL, "
			+ DesContract.CustomerIssues.DATECLOSE	+ " TEXT, "
			+ DesContract.CustomerIssues.STATUS		+ " INTEGER);";

	private static final String CREATE_TABLE_RETAILDATA =
			"CREATE TABLE " + DesContract.RetailData.TABLE_NAME + " ("
			+ DesContract.RetailData._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DesContract.RetailData.COMCODE		+ " TEXT NOT NULL UNIQUE, "
			+ DesContract.RetailData.CCODE			+ " TEXT NOT NULL, "
			+ DesContract.RetailData.DATETIME		+ " TEXT NOT NULL, "
			+ DesContract.RetailData.ICODE			+ " TEXT NOT NULL, "
			+ DesContract.RetailData.DESCRIPTION	+ " TEXT NOT NULL, "
			+ DesContract.RetailData.PRICE			+ " TEXT, "
			+ DesContract.RetailData.PROMO			+ " TEXT, "
			+ DesContract.RetailData.IOH			+ " INTEGER, "
			+ DesContract.RetailData.REMARKS		+ " TEXT, "
			+ DesContract.RetailData.SOS			+ " TEXT, "
			+ DesContract.RetailData.PLANOGRAM		+ " TEXT, "
			+ DesContract.RetailData.STATUS			+ " INTEGER);";

	private static final String CREATE_TABLE_SURVEYDATA =
			"CREATE TABLE " + DesContract.SurveyData.TABLE_NAME + " ("
					+ DesContract.SurveyData._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ DesContract.SurveyData.SCODE			+ " TEXT NOT NULL UNIQUE, "
					+ DesContract.SurveyData.CCODE			+ " TEXT NOT NULL, "
					+ DesContract.SurveyData.DATETIME		+ " TEXT NOT NULL, "
					+ DesContract.SurveyData.ICODE			+ " TEXT NOT NULL, "
					+ DesContract.SurveyData.DESCRIPTION	+ " TEXT NOT NULL, "
					+ DesContract.SurveyData.FREQ			+ " TEXT, "
					+ DesContract.SurveyData.SOURCE			+ " TEXT, "
					+ DesContract.SurveyData.PROMO			+ " TEXT, "
					+ DesContract.SurveyData.REMARKS		+ " TEXT, "
					+ DesContract.SurveyData.STATUS			+ " INTEGER);";

	/*TABLE CREATED AFTER THE UPGRADE*/
	private final String CREATE_TABLE_CUSTOMERMOREDATA =
			"CREATE TABLE " + DesContract.CustomerMoreData.TABLE_NAME + " ("
					+ DesContract.CustomerMoreData._ID				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ DesContract.CustomerMoreData.DATAID			+ " INTEGER, "
					+ DesContract.CustomerMoreData.DESCRIPTION		+ " TEXT UNIQUE, "
					+ DesContract.CustomerMoreData.STATUS			+ " INTEGER, "
					+ "UNIQUE("+DesContract.CustomerMoreData.DATAID+","+DesContract.CustomerMoreData.DESCRIPTION+","+
					DesContract.CustomerMoreData.STATUS+"));";

	private static final String CREATE_TABLE_SALESTARGET =
			"CREATE TABLE " + DesContract.SalesTarget.TABLE_NAME + " ("
					+ DesContract.SalesTarget._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ DesContract.SalesTarget.STID			+ " INTEGER UNIQUE, "
					+ DesContract.SalesTarget.VOLUME		+ " TEXT, "
					+ DesContract.SalesTarget.REACH			+ " INTEGER, "
					+ DesContract.SalesTarget.BUYING_ACCTS	+ " INTEGER, "
					+ DesContract.SalesTarget.CALLS			+ " INTEGER, "
					+ DesContract.SalesTarget.PRO_CALLS		+ " INTEGER, "
					+ DesContract.SalesTarget.STATUS		+ " INTEGER);";


	private static final String CREATE_TABLE_ITEMSCATEGORY =
			"CREATE TABLE " + DesContract.ItemsCategory.TABLE_NAME + " ("
					+ DesContract.ItemsCategory._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ DesContract.ItemsCategory.CATID		+ " INTEGER UNIQUE, "
					+ DesContract.ItemsCategory.CATEGORY	+ " TEXT, "
					+ DesContract.ItemsCategory.STATUS		+ " INTEGER);";


	private static final String CREATE_TABLE_ITEMSSUBCATEGORY =
			"CREATE TABLE " + DesContract.ItemsSubCategory.TABLE_NAME + " ("
					+ DesContract.ItemsSubCategory._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ DesContract.ItemsSubCategory.SUBCATID		+ " INTEGER UNIQUE, "
					+ DesContract.ItemsSubCategory.CATID		+ " INTEGER, "
					+ DesContract.ItemsSubCategory.WEIGHT		+ " REAL, "
					+ DesContract.ItemsSubCategory.QTY			+ " INTEGER, "
					+ DesContract.ItemsSubCategory.LCTN			+ " REAL, "
					+ DesContract.ItemsSubCategory.SUBCAT		+ " TEXT, "
					+ DesContract.ItemsSubCategory.STATUS		+ " INTEGER);";
	private static final String CREATE_TABLE_CMPITEMSCATEGORY =
			"CREATE TABLE " + DesContract.CMPItemsCategory.TABLE_NAME + " ("
					+ DesContract.CMPItemsCategory._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ DesContract.CMPItemsCategory.CATID		+ " INTEGER UNIQUE, "
					+ DesContract.CMPItemsCategory.CATEGORY	+ " TEXT, "
					+ DesContract.CMPItemsCategory.STATUS		+ " INTEGER);";


	private static final String CREATE_TABLE_CMPITEMSSUBCATEGORY =
			"CREATE TABLE " + DesContract.CMPItemsSubCategory.TABLE_NAME + " ("
					+ DesContract.CMPItemsSubCategory._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ DesContract.CMPItemsSubCategory.SUBCATID		+ " INTEGER UNIQUE, "
					+ DesContract.CMPItemsSubCategory.SUBCAT		+ " TEXT, "
					+ DesContract.CMPItemsSubCategory.STATUS		+ " INTEGER);";

	private static final String CREATE_TABLE_CUSTOMERPROMO =
			"CREATE TABLE " + DesContract.CustomerPromo.TABLE_NAME + " ("
					+ DesContract.CustomerPromo._ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ DesContract.CustomerPromo.CCODE		+ " TEXT, "
					+ DesContract.CustomerPromo.DATETIME	+ " TEXT, "
					+ DesContract.CustomerPromo.STATUS		+ " INTEGER);";
	
	private static final String DROP_TABLE_ITEMS 			= "DROP TABLE IF EXISTS " + DesContract.Item.TABLE_NAME;
	private static final String DROP_TABLE_INVENTORY 		= "DROP TABLE IF EXISTS " + DesContract.Inventory.TABLE_NAME;
	private static final String DROP_TABLE_INVENTORY_ITEMS	= "DROP TABLE IF EXISTS " + DesContract.Inventory.TABLE_NAME2;
	private static final String DROP_TABLE_RETURNS 			= "DROP TABLE IF EXISTS " + DesContract.Return.TABLE_NAME;
	private static final String DROP_TABLE_CUSTOMERS 		= "DROP TABLE IF EXISTS " + DesContract.Customer.TABLE_NAME;
	private static final String DROP_TABLE_COVERAGE_PLAN 	= "DROP TABLE IF EXISTS " + DesContract.CoveragePlan.TABLE_NAME;
	private static final String DROP_TABLE_PERIOD 			= "DROP TABLE IF EXISTS " + DesContract.Period.TABLE_NAME;
	private static final String DROP_TABLE_TIME_INOUT	 	= "DROP TABLE IF EXISTS " + DesContract.TimeInOut.TABLE_NAME;
	private static final String DROP_TABLE_SALESCALL 		= "DROP TABLE IF EXISTS " + DesContract.SalesCall.TABLE_NAME;
	private static final String DROP_TABLE_DELIVERIES 		= "DROP TABLE IF EXISTS " + DesContract.Delivery.TABLE_NAME;
	private static final String DROP_TABLE_COLLECTIBLES 	= "DROP TABLE IF EXISTS " + DesContract.Collectible.TABLE_NAME;
	private static final String DROP_TABLE_COLLECTIONS 		= "DROP TABLE IF EXISTS " + DesContract.Collection.TABLE_NAME;
	private static final String DROP_TABLE_CALL_SHEETS 		= "DROP TABLE IF EXISTS " + DesContract.CallSheet.TABLE_NAME;
	private static final String DROP_TABLE_CALL_SHEET_ITEMS = "DROP TABLE IF EXISTS " + DesContract.CallSheetItem.TABLE_NAME;
	private static final String DROP_TABLE_MARKED_LOCATION 	= "DROP TABLE IF EXISTS " + DesContract.MarkedLocation.TABLE_NAME;
	private static final String DROP_TABLE_SIGNATURE	 	= "DROP TABLE IF EXISTS " + DesContract.Signature.TABLE_NAME;
	private static final String DROP_TABLE_PICTURE		 	= "DROP TABLE IF EXISTS " + DesContract.Picture.TABLE_NAME;
	private static final String DROP_TABLE_TRAIL 			= "DROP TABLE IF EXISTS " + DesContract.Trail.TABLE_NAME;
	private static final String DROP_TABLE_INBOX 			= "DROP TABLE IF EXISTS " + DesContract.Inbox.TABLE_NAME;
	private static final String DROP_TABLE_OUTBOX			= "DROP TABLE IF EXISTS " + DesContract.Outbox.TABLE_NAME;
	private static final String DROP_TABLE_SETTINGS			= "DROP TABLE IF EXISTS " + DesContract.Setting.TABLE_NAME;
	private static final String DROP_TABLE_CUSTITEM			= "DROP TABLE IF EXISTS " + DesContract.CustItem.TABLE_NAME;
	private static final String DROP_TABLE_CMPITEMS			= "DROP TABLE IF EXISTS " + DesContract.CustItemList.TABLE_NAME;
	private static final String DROP_TABLE_ACCTTYPES		= "DROP TABLE IF EXISTS " + DesContract.AccountTypes.TABLE_NAME;
	private static final String DROP_TABLE_REGION			= "DROP TABLE IF EXISTS " + DesContract.Region.TABLE_NAME;
	private static final String DROP_TABLE_OWNER			= "DROP TABLE IF EXISTS " + DesContract.CusOwnerData.TABLE_NAME;
	private static final String DROP_TABLE_PURCHASER		= "DROP TABLE IF EXISTS " + DesContract.CusPurchaserData.TABLE_NAME;
	private static final String DROP_TABLE_CHECKLISTS		= "DROP TABLE IF EXISTS " + DesContract.Checklists.TABLE_NAME;
	private static final String DROP_TABLE_TRUCKS			= "DROP TABLE IF EXISTS " + DesContract.CusTrucksData.TABLE_NAME;
	private static final String DROP_TABLE_TRUCKSLIST		= "DROP TABLE IF EXISTS " + DesContract.CusTrucksList.TABLE_NAME;
	private static final String DROP_TABLE_WAREHOUSEITEMS	= "DROP TABLE IF EXISTS " + DesContract.CusWarehouseItems.TABLE_NAME;
	private static final String DROP_TABLE_MERCHANDISING	= "DROP TABLE IF EXISTS " + DesContract.Merchandising.TABLE_NAME;
	private static final String DROP_TABLE_SUPPLIERS		= "DROP TABLE IF EXISTS " + DesContract.Suppliers.TABLE_NAME;
	private static final String DROP_TABLE_WORKWITH			= "DROP TABLE IF EXISTS " + DesContract.WorkWith.TABLE_NAME;
	private static final String DROP_TABLE_CALLSHEETSERVED	= "DROP TABLE IF EXISTS " + DesContract.CallSheetServe.TABLE_NAME;
	private static final String DROP_TABLE_GENERALPROFILE	= "DROP TABLE IF EXISTS " + DesContract.GeneralProfile.TABLE_NAME;
	private static final String DROP_TABLE_CALLCHECKLIST	= "DROP TABLE IF EXISTS " + DesContract.CallChecklist.TABLE_NAME;
    private static final String DROP_TABLE_CCALLCHECKLIST	= "DROP TABLE IF EXISTS " + DesContract.CustomerCallChecklist.TABLE_NAME;
    private static final String DROP_TABLE_CUSTOMERISSUES	= "DROP TABLE IF EXISTS " + DesContract.CustomerIssues.TABLE_NAME;
	private static final String DROP_TABLE_RETAILDATA		= "DROP TABLE IF EXISTS " + DesContract.RetailData.TABLE_NAME;
	private static final String DROP_TABLE_SURVEYDATA		= "DROP TABLE IF EXISTS " + DesContract.SurveyData.TABLE_NAME;
	private static final String DROP_TABLE_CUSTOMERMOREDATA	= "DROP TABLE IF EXISTS " + DesContract.CustomerMoreData.TABLE_NAME;
    private static final String DROP_TABLE_SALESTARGET   	= "DROP TABLE IF EXISTS " + DesContract.SalesTarget.TABLE_NAME;
	private static final String DROP_TABLE_ITEMSCATEGORY	= "DROP TABLE IF EXISTS " + DesContract.ItemsCategory.TABLE_NAME;
	private static final String DROP_TABLE_ITEMSSUBCATEGORY   	= "DROP TABLE IF EXISTS " + DesContract.ItemsSubCategory.TABLE_NAME;
	private static final String DROP_TABLE_CMPITEMSCATEGORY		= "DROP TABLE IF EXISTS " + DesContract.CMPItemsCategory.TABLE_NAME;
	private static final String DROP_TABLE_CMPITEMSSUBCATEGORY  = "DROP TABLE IF EXISTS " + DesContract.CMPItemsSubCategory.TABLE_NAME;
	private static final String DROP_TABLE_COMPETITOR			= "DROP TABLE IF EXISTS " + DesContract.Competitors.TABLE_NAME;
	private static final String DROP_TABLE_COMPETITOR_ITEMS  	= "DROP TABLE IF EXISTS " + DesContract.Competitors.TABLE_NAME2;
	private static final String DROP_TABLE_CUSTOMERPROMO	  	= "DROP TABLE IF EXISTS " + DesContract.CustomerPromo.TABLE_NAME;
}

