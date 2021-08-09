package com.xpluscloud.moses_apc.dbase;

import android.provider.BaseColumns;

public class DesContract {
	
	private DesContract() {}
		
	public static abstract class Customer implements BaseColumns {
		public static final String TABLE_NAME 		= "customers";
		public static final String CCODE 			= "ccode";
		public static final String NAME 			= "name";
		public static final String ADDRESS 			= "address";
		public static final String BRGY 			= "brgy";
		public static final String CITY 			= "city";
		public static final String STATE 			= "state";
		public static final String CONTACT_NUMBER 	= "contact_number";
		public static final String ACCT_TYPEID 		= "acct_typeid";
		public static final String LATITUDE 		= "latitude";
		public static final String LONGITUDE 		= "longitude";
		public static final String CPLAN_CODE 		= "cplan_code";
		public static final String DISCOUNT 		= "discount";
		public static final String PICTURE			= "picture";
		public static final String BUFFER			= "buffer";
		public static final String CASH_SALES		= "cash_sales";	//0=>Booking; 1=Cash Sales
		public static final String TARGET			= "target";
		public static final String TERMID			= "termid";	//
		public static final String TYPEID			= "typeid";	//
		public static final String AR				= "a_r";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class ItemCategory implements BaseColumns {
		public static final String TABLE_NAME 		= "item_categories";
		public static final String CATEGORY_CODE 	= "categorycode";
		public static final String CATEGORY_NAME 	= "categoryname";
		public static final String DESCRIPTION 		= "description";
		public static final String PRIORITY 		= "priority";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class Item implements BaseColumns {
		public static final String TABLE_NAME 		= "items";
		public static final String ITEM_CODE 		= "itemcode";
		public static final String DESCRIPTION 		= "description";
		public static final String CATEGORY_CODE	= "categorycode";
		public static final String PACK_BARCODE 	= "pack_barcode";
		public static final String BARCODE 			= "barcode";
		public static final String QTY_PER_PACK 	= "pp_qty";
		public static final String PRICE_PER_PACK 	= "pp_pack";
		public static final String PRICE_PER_UNIT 	= "pp_unit";
		public static final String PRIORITY 		= "priority";
		public static final String PACKING	 		= "packing";
		public static final String ITEM_ID 			= "itemid"; //use for display order
		public static final String CATID 			= "category_id";
		public static final String SUBCATID 		= "subcategory_id";
		public static final String SHAREPTS 		= "share_pts";
		public static final String LITERS	 		= "liters";
		public static final String STATUS 			= "status";
		
	}
	
	public static abstract class Delivery implements BaseColumns {
		public static final String TABLE_NAME 		= "deliveries";
		public static final String DATE 			= "date";
		public static final String INVOICENO		= "invoiceno";
		public static final String SONO				= "sono";
		public static final String CCODE 			= "ccode";
		public static final String ITEM_CODE 		= "itemcode";
		public static final String PCKG				= "pckg";
		public static final String PRICE			= "price";
		public static final String QTY				= "qty";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class Collectible implements BaseColumns {
		public static final String TABLE_NAME 		= "collectibles";
		public static final String DATE 			= "date";
		public static final String INVOICENO		= "invoiceno";
		public static final String CCODE 			= "ccode";
		public static final String AMOUNT			= "amount";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class Collection implements BaseColumns {
		public static final String TABLE_NAME 		= "collections";
		public static final String DATE 			= "date";
		public static final String INVOICENO		= "invoiceno";
		public static final String CCODE 			= "ccode";
		public static final String CASH_AMOUNT		= "cash";
		
		public static final String CHECK_AMOUNT		= "cheque";
		public static final String CHECKNO			= "checkno";
		public static final String CM_AMOUNT		= "cm_amount";
		public static final String CMNO				= "cmno";
		
		public static final String BANKNAME			= "bankname";
		public static final String BANKBRANCH		= "bankbranch";
		public static final String DEPOSIT_TRNO		= "deptrno";
		public static final String DEPOSIT_AMOUNT	= "deposit_amount";
		
		public static final String ORNO				= "orno";
		public static final String NC_REASON		= "ncreason"; //Reason for No Collection.
		public static final String REMARKS			= "remarks";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class Inventory implements BaseColumns {
		public static final String TABLE_NAME 		= "inventory";
		public static final String TABLE_NAME2 		= "inventory_items";
		public static final String INNO 			= "inno";
		public static final String INCODE 			= "incode";
		public static final String CCODE 			= "ccode";
		public static final String DATE 			= "date";
		public static final String ITEM_CODE 		= "itemcode";
		public static final String PCKG				= "pckg";
		public static final String PRICE			= "price";
		public static final String QTY				= "qty";
		public static final String STATUS 			= "status";
	}

	public static abstract class Competitors implements BaseColumns {
		public static final String TABLE_NAME 		= "cmpsheet";
		public static final String TABLE_NAME2 		= "cmpsheetitems";
		public static final String INNO 			= "cmpno";
		public static final String INCODE 			= "cmpcode";
		public static final String CCODE 			= "ccode";
		public static final String DATE 			= "date";
		public static final String ITEM_CODE 		= "itemcode";
		public static final String PCKG				= "pckg";
		public static final String PRICE			= "price";
		public static final String QTY				= "qty";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class Return implements BaseColumns {
		public static final String TABLE_NAME 		= "returns";
		public static final String PRCODE 			= "prcode";
		public static final String DATE 			= "date";
		public static final String PRNO 			= "prno";
		public static final String CCODE 			= "ccode";
		public static final String ITEM_CODE 		= "itemcode";
		public static final String PCKG				= "pckg";
		public static final String PRICE			= "price";
		public static final String QTY				= "qty";
		public static final String STATUS 			= "status";
	}
	
	
	public static abstract class CallSheet implements BaseColumns {
		public static final String TABLE_NAME 		= "callsheets";
		public static final String DATE 			= "date";
		public static final String CSCODE			= "cscode";
		public static final String SONO				= "sono";
		public static final String CCODE 			= "ccode";
		public static final String BUFFER			= "buffer";
		public static final String CASH_SALES		= "cash_sales";	//0=>Sales Order; 1=Cash Sales
		public static final String PAYMENT			= "payment";
		public static final String SUPPLIER			= "supplier";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class CallSheetItem implements BaseColumns {
		public static final String TABLE_NAME 		= "callsheetitems";
		public static final String CSCODE			= "cscode";
		public static final String ITEM_CODE 		= "itemcode";
		public static final String PCKG 			= "pckg";
		
		public static final String PAST_INVT		= "pastinvt";
		public static final String DELIVERY			= "delivery";
		public static final String PRESENT_INVT		= "presentinvt";
		public static final String OFFTAKE			= "offtake";
		public static final String ICO				= "ico";
		public static final String SUGGESTED	 	= "suggested";
		public static final String ORDER_QTY 		= "order_qty";
		public static final String PRICE 			= "price";
		public static final String SHARETPTS 		= "sharetpts";
		public static final String STATUS 			= "status";

		public static final String DELIVERY_DATE	= "delivery_date";
	}

	public static abstract class CoveragePlan implements BaseColumns {
		public static final String TABLE_NAME 		= "coverageplans";
		public static final String CPID 			= "cpid";
		public static final String CPLAN_CODE 		= "cplan_code";
		public static final String DESCRIPTION 		= "description";
		public static final String WEEK_SCHEDULE	= "week_schedule";
		public static final String DAY_SCHEDULE		= "day_schedule";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class Period implements BaseColumns {
		public static final String TABLE_NAME 		= "periods";
		public static final String YEAR 			= "year";
		public static final String NWEEK 			= "nweek";
		public static final String WEEK_START		= "week_start";
		public static final String WEEK_END			= "week_end";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class TimeInOut implements BaseColumns {
		public static final String TABLE_NAME 		= "timeinout";
		public static final String CCODE 			= "ccode";
		public static final String DATETIME 		= "datetime";
		public static final String INOUT 			= "inout";
		public static final String LATITUDE 		= "latitude";
		public static final String LONGITUDE 		= "longitude";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class SalesCall implements BaseColumns {
		public static final String TABLE_NAME 		= "salescalls";
		public static final String DATETIME 		= "datetime";
		public static final String CCODE 			= "ccode";
		public static final String BLOB 			= "blob";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class MarkedLocation implements BaseColumns {
		public static final String TABLE_NAME 		= "markedlocations";
		public static final String CCODE 			= "ccode";
		public static final String GPSTIME 			= "gpstime";
		public static final String LATITUDE 		= "latitude";
		public static final String LONGITUDE 		= "longitude";
		public static final String ACCURACY 		= "accuracy";
		public static final String PROVIDER 		= "provider";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class Signature implements BaseColumns {
		public static final String TABLE_NAME 		= "signatures";
		public static final String CCODE 			= "ccode";
		public static final String DATETIME 		= "datetime";
		public static final String FILENAME			= "filename";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class Picture implements BaseColumns {
		public static final String TABLE_NAME 		= "pictures";
		public static final String CCODE 			= "ccode";
		public static final String DATETIME 		= "datetime";
		public static final String BA 				= "ba"; //0=before; 1=after
		public static final String FILENAME			= "filename";
		public static final String STATUS 			= "status";
	}
	
	public static abstract class Trail implements BaseColumns {
		public static final String TABLE_NAME 		= "trails";
		public static final String GPSTIME 			= "gpstime";
		public static final String LATITUDE 		= "latitude";
		public static final String LONGITUDE 		= "longitude";
		public static final String ACCURACY 		= "accuracy";
		public static final String SPEED 			= "speed";
		public static final String BEARING 			= "bearing";
		public static final String PROVIDER			= "provider";
		public static final String STATUS 			= "status";
	}
	
	
	public static abstract class Inbox implements BaseColumns {
		public static final String TABLE_NAME 		= "inbox";
		public static final String DATETIME 		= "datetime";
		public static final String SENDER 			= "sender";
		public static final String MESSAGE			= "message";
		public static final String STATUS			= "status";
	}
	
	public static abstract class Outbox implements BaseColumns {
		public static final String TABLE_NAME 		= "outbox";
		public static final String DATETIME 		= "datetime";
		public static final String RECIPIENT		= "recipient";
		public static final String MESSAGE			= "message";
		public static final String PRIORITY			= "priority";
		public static final String STATUS			= "status"; //0=que; 1=sent; 2=failed; 3=void; 9=confirmed
	}
	
	public static abstract class Setting implements BaseColumns {
		public static final String TABLE_NAME 		= "settings";
		public static final String KEY 				= "key";
		public static final String VALUE			= "value";
		public static final String STATUS			= "status";
	}

	public static abstract class CustItem implements BaseColumns {
		public static final String TABLE_NAME		= "citem_price";
		public static final String COMCODE			= "comcode";
		public static final String CCODE			= "ccode";
		public static final String ICODE			= "itemcode";
		public static final String DESCRIPTION		= "description";
		public static final String CATEGORYCODE		= "categorycode";
		public static final String DPRICE			= "dtr_price";
		public static final String RPRICE			= "retail_price";
		public static final String QTY				= "quantity";
		public static final String VOL				= "volume";
		public static final String IOH				= "ioh";
		public static final String STATUS			= "status";
				
		public static final String TYPE22SU			= "type22su";
		public static final String TYPE50SU			= "type50su";
		public static final String TYPE5SU			= "type5su";
		public static final String TYPE7SU			= "type7su";
		
		public static final String TYPE22IOH		= "type22ioh";
		public static final String TYPE50IOH		= "type50ioh";
		public static final String TYPE5IOH			= "type5ioh";
		public static final String TYPE7IOH			= "type7ioh";
		
		public static final String REMARKS			= "remarks";
		public static final String DATETIME			= "datetime";
	}
	
	public static abstract class CustItemList implements BaseColumns {
		public static final String TABLE_NAME 		= "citem";
		public static final String ITEMCODE			= "itemcode";
		public static final String DESCRIPTION		= "description";
		public static final String CASE_BARCODE		= "case_barcode";
		public static final String BARCODE			= "barcode";
		public static final String GROUPID			= "groupid";
		public static final String CATID 			= "category_id";
		public static final String SUBCATID 		= "subcategory_id";
		public static final String BPNAME	 		= "bp_name";
		public static final String STATUS			= "status";
	}
	
	public static abstract class AccountTypes implements BaseColumns {
		public static final String TABLE_NAME		= "acct_types";
		public static final String ACCTTYPE			= "acct_type";
		public static final String DESCRIPTION		= "description";
		public static final String STATUS			= "status";
	}
	
	public static abstract class Region implements BaseColumns {
		public static final String TABLE_NAME		= "region";
		public static final String REGIONID			= "regionid";
		public static final String DESCRIPTION		= "description";
		public static final String STATUS			= "status";
	}
	
	public static abstract class CusOwnerData implements BaseColumns {
		public static final String TABLE_NAME		= "owner";
		public static final String CCODE			= "ccode";
		public static final String NAME				= "name";
		public static final String CSTATUS			= "civil_status";
		public static final String BDAY				= "birthdate";
		public static final String HOBINT			= "hobbies_interests";
		public static final String PHONE			= "phone";
		public static final String EMAIL			= "email";
		public static final String STATUS			= "status";
	}
	
//	public static abstract class CusPurchaserData implements BaseColumns {
//		public static final String TABLE_NAME		= "purchaser";
//		public static final String CCODE			= "ccode";
//		public static final String NAME				= "name";
//		public static final String CSTATUS			= "civil_status";
//		public static final String BDAY				= "birthdate";
//		public static final String HOBINT			= "hobbies_interests";
//		public static final String PHONE			= "phone";
//		public static final String EMAIL			= "email";
//		public static final String STATUS			= "status";
//	}
	
	public static abstract class CusPurchaserData implements BaseColumns {
		public static final String TABLE_NAME		= "purchaser";
		public static final String CCODE			= "ccode";
		public static final String TRUCKS			= "trucks";
		public static final String DELIVERY			= "delivery";
		public static final String SUPPLIER			= "supplier";
		public static final String WAREHOUSECAP		= "warehouse_cap";
		public static final String STATUS			= "status";
	}
	
	public static abstract class CusTrucksList implements BaseColumns {
		public static final String TABLE_NAME		= "trucks_list";
		public static final String TRUCKID			= "truckid";
		public static final String TRUCK_NAME		= "name";
		public static final String CAPACITY			= "capacity";
		public static final String STATUS			= "status";
	}
	
	public static abstract class CusTrucksData implements BaseColumns {
		public static final String TABLE_NAME		= "trucks";
		public static final String CCODE			= "ccode";
		public static final String TRUCKID			= "truckid";
		public static final String TYPE				= "type";
		public static final String QUANTITY			= "quantity";
		public static final String STATUS			= "status";
	}
	
	public static abstract class CusWarehouseItems implements BaseColumns {
		public static final String TABLE_NAME		= "customer_whitems";
		public static final String CCODE			= "ccode";
		public static final String ITEMCODE			= "itemcode";
		public static final String QUANTITY			= "quantity";
		public static final String STATUS			= "status";
	}
	
	public static abstract class Checklists implements BaseColumns {
		public static final String TABLE_NAME		= "checklists";
		public static final String MCID				= "mcid";
		public static final String DESCRIPTION		= "description";
		public static final String STATUS			= "status";
	}
	
	public static abstract class Merchandising implements BaseColumns {
		public static final String TABLE_NAME		= "merchandising";
		public static final String CCODE			= "ccode";
		public static final String REMARKS			= "remarks";
		public static final String CALLSID			= "customercall_id";
		public static final String DATE				= "date";
		public static final String STATUS			= "status";
	}
	
	public static abstract class Suppliers implements BaseColumns {
		public static final String TABLE_NAME		= "suppliers";
		public static final String SPID				= "spid";
		public static final String DEALER			= "dealer";
		public static final String AREA				= "area";
		public static final String REGION			= "region";
		public static final String STATUS			= "status";
	}
	
	public static abstract class WorkWith implements BaseColumns {
		public static final String TABLE_NAME		= "work_with";
		public static final String WID				= "wid";
		public static final String DESCRIPTION		= "description";
		public static final String STATUS			= "status";
	}
	
	public static abstract class CallSheetServe implements BaseColumns {
		public static final String TABLE_NAME	= "so_serve";
		public static final String SOID			= "soid";
		public static final String DEVID		= "devid";
		public static final String CSCODE		= "cscode";
		public static final String CCODE		= "ccode";
		public static final String QTY			= "qty";
		public static final String DATE			= "date";
		public static final String STATUS		= "status";
	}
	
	public static abstract class GeneralProfile implements BaseColumns {
		public static final String TABLE_NAME	= "gen_prof";
		public static final String CCODE		= "ccode";
		public static final String LOAN			= "loan";
		public static final String ROUTE		= "route";
		public static final String FREQ			= "freq";
		public static final String AVERAGE		= "average";
		public static final String STATUS		= "status";
	}

	public static abstract class CallChecklist implements BaseColumns {
		public static final String TABLE_NAME		= "call_checklist";
		public static final String CHKL_ID			= "chkl_id";
		public static final String CAT_ID   		= "cat_id";
		public static final String DESCRIPTION		= "description";
		public static final String CATEGORY			= "category";
	}

    public static abstract class CustomerCallChecklist implements BaseColumns {
        public static final String TABLE_NAME       = "ccall_checklist";
        public static final String CCODE            = "ccode";
        public static final String CHKID            = "chkl_id";
        public static final String YES            	= "chk_yes";
		public static final String NO				= "chk_no";
        public static final String DATE	            = "date";
		public static final String TIME				= "time";
        public static final String GREMARKS         = "gremarks";
        public static final String STATUS           = "status";
    }

	public static abstract class CustomerIssues implements BaseColumns {
		public static final String TABLE_NAME		= "customer_issues";
		public static final String CODEISSUE		= "icode";
		public static final String CCODE		 	= "ccode";
		public static final String ISSUE			= "issue";
		public static final String DATEOPEN			= "date_open";
		public static final String DATECLOSE		= "date_close";
		public static final String STATUS			= "status";
	}

	public static abstract class RetailData implements BaseColumns {
		public static final String TABLE_NAME		= "retail";
		public static final String COMCODE			= "comcode";
		public static final String CCODE			= "ccode";
		public static final String ICODE			= "itemcode";
		public static final String DESCRIPTION		= "description";
		public static final String PRICE			= "price";
		public static final String IOH				= "ioh";
		public static final String PROMO			= "promo";
		public static final String STATUS			= "status";
		public static final String REMARKS			= "remarks";
		public static final String SOS				= "sos";
		public static final String PLANOGRAM		= "planogram";
		public static final String DATETIME			= "datetime";
	}
	public static abstract class SurveyData implements BaseColumns {
		public static final String TABLE_NAME		= "survey";
		public static final String SCODE			= "scode";
		public static final String CCODE			= "ccode";
		public static final String ICODE			= "itemcode";
		public static final String DESCRIPTION		= "description";
		public static final String FREQ				= "freq";
		public static final String SOURCE			= "source";
		public static final String PROMO			= "promo";
		public static final String REMARKS			= "remarks";
		public static final String DATETIME			= "datetime";
		public static final String STATUS			= "status";
	}

	/*CONSTANTS CREATED AFTER THE UPGRADE db version = 5*/
	public static abstract class CustomerMoreData implements BaseColumns {
		public static final String TABLE_NAME		= "cus_datas";
		public static final String DATAID			= "dataid";
		public static final String DESCRIPTION		= "description";
		public static final String STATUS			= "status";
	}

	public static abstract class SalesTarget implements BaseColumns {
		public static final String TABLE_NAME	= "sales_target";
		public static final String STID			= "targetid";
		public static final String VOLUME		= "target_volume";
		public static final String REACH		= "target_reach";
		public static final String BUYING_ACCTS	= "target_buying_accts";
		public static final String CALLS		= "target_calls";
		public static final String PRO_CALLS	= "target_pro_calls";
		public static final String STATUS		= "status";
	}

	public static abstract class ItemsCategory implements BaseColumns {
		public static final String TABLE_NAME	= "icategory";
		public static final String CATID		= "category_id";
		public static final String CATEGORY		= "category";
		public static final String STATUS		= "status";
	}

	public static abstract class ItemsSubCategory implements BaseColumns {
		public static final String TABLE_NAME	= "isubcategory";
		public static final String SUBCATID		= "subcategory_id";
		public static final String CATID		= "category_id";
		public static final String SUBCAT		= "subcategory";
		public static final String WEIGHT		= "weight";
		public static final String QTY			= "quantity";
		public static final String LCTN			= "l_ctn";
		public static final String STATUS		= "status";
	}
	public static abstract class CMPItemsCategory implements BaseColumns {
		public static final String TABLE_NAME	= "cmpicategory";
		public static final String CATID		= "category_id";
		public static final String CATEGORY		= "category";
		public static final String STATUS		= "status";
	}

	public static abstract class CMPItemsSubCategory implements BaseColumns {
		public static final String TABLE_NAME	= "cmpisubcategory";
		public static final String SUBCATID		= "subcategory_id";
		public static final String SUBCAT		= "subcategory";
		public static final String STATUS		= "status";
	}
	public static abstract class CustomerPromo implements BaseColumns {
		public static final String TABLE_NAME	= "cus_promo";
		public static final String CCODE		= "ccode";
		public static final String DATETIME		= "datetime";
		public static final String STATUS		= "status";
	}
}
