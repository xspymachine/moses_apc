package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.xpluscloud.moses_apc.getset.Customer;
import com.xpluscloud.moses_apc.getset.CustomerData;
import com.xpluscloud.moses_apc.util.DateUtil;

import java.util.ArrayList;
import java.util.List;


public class CustomerDbManager extends DbManager {
	
	public static final String[] CUSTOMER_PROJECTION = new String[] {
		DesContract.Customer._ID,
		DesContract.Customer.CCODE,
		DesContract.Customer.NAME,
		DesContract.Customer.ADDRESS,
		DesContract.Customer.BRGY,
		DesContract.Customer.CITY,
		DesContract.Customer.STATE,
		DesContract.Customer.CONTACT_NUMBER,
		DesContract.Customer.ACCT_TYPEID,
		DesContract.Customer.DISCOUNT,
		DesContract.Customer.LATITUDE,
		DesContract.Customer.LONGITUDE,
		DesContract.Customer.CPLAN_CODE,
		DesContract.Customer.BUFFER,
		DesContract.Customer.PICTURE,
		DesContract.Customer.CASH_SALES,
		DesContract.Customer.TERMID,
		DesContract.Customer.TARGET,
		DesContract.Customer.TYPEID,
		DesContract.Customer.AR,
		DesContract.Customer.STATUS
	};
	
	public CustomerDbManager(Context context) {
		super(context);
	}

	public void updateDeletionPic(int _id) {
		String sql="UPDATE " + DesContract.Customer.TABLE_NAME +
				" SET " + DesContract.Customer.STATUS + " = 9999" +
				" WHERE " + DesContract.Customer._ID + " = " +  _id ;

		db.execSQL(sql);
	}
	
	public long AddNew(Customer c) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Customer.CCODE 			, c.getCustomerCode());
		cv.put(DesContract.Customer.NAME          	, c.getName());
		cv.put(DesContract.Customer.ADDRESS       	, c.getAddress());
		cv.put(DesContract.Customer.BRGY	       	, c.getBrgy());
		cv.put(DesContract.Customer.CITY          	, c.getCity());
		cv.put(DesContract.Customer.STATE         	, c.getState());
		cv.put(DesContract.Customer.CONTACT_NUMBER	, c.getContactNumber());
		cv.put(DesContract.Customer.ACCT_TYPEID		, c.getAcctypid());
		cv.put(DesContract.Customer.DISCOUNT		, c.getDiscount());
		cv.put(DesContract.Customer.LATITUDE      	, c.getLatitude());
		cv.put(DesContract.Customer.LONGITUDE     	, c.getLongitude());
		cv.put(DesContract.Customer.CPLAN_CODE 		, c.getCplanCode());
		cv.put(DesContract.Customer.BUFFER			, c.getBuffer());
		cv.put(DesContract.Customer.CASH_SALES		, c.getCashSales());
		cv.put(DesContract.Customer.TERMID			, c.getTermid());
		cv.put(DesContract.Customer.TARGET 			, c.getTarget());
		cv.put(DesContract.Customer.STATUS 			, c.getStatus());
		return db.insert(DesContract.Customer.TABLE_NAME, null, cv);
		
		//return db.insertWithOnConflict(DesContract.Customer.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		
	}
	
	public void addData() {
	
	}
	
	public long AddCustomer(Customer c) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Customer.CCODE 			, c.getCustomerCode());
		cv.put(DesContract.Customer.NAME          	, c.getName());
		cv.put(DesContract.Customer.ADDRESS       	, c.getAddress());
		cv.put(DesContract.Customer.BRGY       		, c.getBrgy());
		cv.put(DesContract.Customer.CITY          	, c.getCity());
		cv.put(DesContract.Customer.STATE         	, c.getState());
		cv.put(DesContract.Customer.CONTACT_NUMBER	, c.getContactNumber());
		cv.put(DesContract.Customer.ACCT_TYPEID		, c.getAcctypid());
		cv.put(DesContract.Customer.DISCOUNT		, c.getDiscount());
		cv.put(DesContract.Customer.LATITUDE      	, c.getLatitude());
		cv.put(DesContract.Customer.LONGITUDE     	, c.getLongitude());
		cv.put(DesContract.Customer.CPLAN_CODE 		, c.getCplanCode());
		cv.put(DesContract.Customer.BUFFER			, c.getBuffer());
		cv.put(DesContract.Customer.CASH_SALES		, c.getCashSales());
		cv.put(DesContract.Customer.TERMID			, c.getTermid());
		cv.put(DesContract.Customer.TARGET 			, c.getTarget());
		cv.put(DesContract.Customer.STATUS 			, c.getStatus());
		cv.put(DesContract.Customer.TYPEID 			, c.getTypeid());
		
		//return db.insert(DesContract.Customer.TABLE_NAME, null, cv);
		
		return db.insertWithOnConflict(DesContract.Customer.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		
	}
	
	public void AddCustomers(List<Customer> list) {
		for(int i = 0; i < list.size(); i++) {
			AddCustomer(list.get(i));
		}
	}
	
	public Customer getCustomer(long Id) {
		Customer cust;
		Cursor c = retrieveCursor(Id, DesContract.Customer.TABLE_NAME, CUSTOMER_PROJECTION);
//		String sql = "SELECT *FROM "+DesContract.Customer.TABLE_NAME;
		
		if(c.moveToFirst()) {
			cust = createCustomer(c);
		} else {
			cust = null;
		}
		
		c.close();
		
		return cust;
	}
	
	public Customer getCustomer(String cCode) {
		String sql="SELECT * "  +
				" FROM customers " +
				" WHERE ccode LIKE '" + cCode +"'";
		Cursor c = db.rawQuery(sql,null);
		
		Customer cust;
		if(c.moveToFirst()) {
			cust = createCustomer(c);
		} else {
			cust = null;
		}
		
		c.close();
		
		return cust;
	}
	
	
	public Customer getCustomerByCode(String ccode) {
		Customer cust;
		String sql = "SELECT * FROM customers WHERE ccode = '" + ccode + "' LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		
		if(c.moveToFirst()) {
			cust = createCustomer(c);
		} else {
			cust = null;
		}
		
		c.close();
		
		return cust;
	}
	
	public String getCustomerCode(int id) {
		String customerCode="";
		String sql = "SELECT ccode FROM customers WHERE _id= " + id + "  LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			customerCode = c.getString(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		c.close();
		return customerCode;
	}
	
	
	public List<Customer> getAllCustomers() {
		List<Customer> list = new ArrayList<Customer>();
		
		Cursor c = db.query(DesContract.Customer.TABLE_NAME,
				CUSTOMER_PROJECTION, 
				null, null, null,null, DesContract.Customer.NAME + " ASC");
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Customer cust = createCustomer(c);
				list.add(cust);
			}
		}
		
		c.close();
		
		return list;
	}
	
	
	public String getCustomerName(String ccode) {
		String customerName="";
		
		String sql="SELECT name FROM customers WHERE ccode='"+ccode+"'";
		
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			customerName = c.getString(0); 
		}	
		c.close();
		return customerName;
		
	}
	
	public Integer getStatus(String ccode) {
		int status=0;
		String sql = "SELECT status FROM customers WHERE ccode='"+ccode+"' LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			status = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		c.close();
		return status;
	}
	
	public void setStatus(String ccode, Integer status) {
		String sql = "UPDATE customers SET status=" + status +" WHERE ccode='"+ccode+"'";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			status = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		c.close();		
	}
	
	public void setStatusAll(Integer status) {
		String sql = "UPDATE customers SET status=" + status;
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			status = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		c.close();		
	}
	
	public int getLastId() {
		int lastId = 0;
		
		String sql = "SELECT _id FROM customers ORDER BY _id DESC LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
		    lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		
		return lastId;
	}
	
	public int getFrequency(String customerCode) {
		Integer frequency=0;
		String cpCode="";
		Cursor c = db.query(DesContract.Customer.TABLE_NAME,
				new String[] {DesContract.Customer.CPLAN_CODE},
				DesContract.Customer.CCODE + "= '" + customerCode +"'", 
				null, null,null,null );
		if (c != null && c.moveToFirst()) {
		    cpCode = c.getString(0); 
		    
		    if(cpCode.length()>3) {
		    	String splitWD[] = cpCode.split(" ");
		    	try{
		    	frequency = (splitWD[0].length() - 1) * (splitWD[1].length() - 1);
		    	}catch (Exception e){
		    		frequency=0;
		    	}
		    }
		}
		
		return frequency;
	}
	
	public void delete(Integer Id) {
		
		//Log.d("Delete customer",Id+" : " + getCustomerCode(Id));
		
		String where = "_id=" + Id;
		db.delete(DesContract.Customer.TABLE_NAME, where, null);
		
	}
	
	public void deleteAll() {
			
		db.delete(DesContract.Customer.TABLE_NAME, null, null);
		
	}
	
	public void update(Customer c) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Customer.CCODE 			, c.getCustomerCode());
		cv.put(DesContract.Customer.NAME          	, c.getName());
		cv.put(DesContract.Customer.ADDRESS       	, c.getAddress());
		cv.put(DesContract.Customer.BRGY          	, c.getBrgy());
		cv.put(DesContract.Customer.CITY          	, c.getCity());
		cv.put(DesContract.Customer.STATE         	, c.getState());
		cv.put(DesContract.Customer.CONTACT_NUMBER	, c.getContactNumber());
		cv.put(DesContract.Customer.ACCT_TYPEID		, c.getAcctypid());
		cv.put(DesContract.Customer.DISCOUNT		, c.getDiscount());
		cv.put(DesContract.Customer.CPLAN_CODE 		, c.getCplanCode());
		cv.put(DesContract.Customer.BUFFER			, c.getBuffer());
		cv.put(DesContract.Customer.CASH_SALES		, c.getCashSales());
		cv.put(DesContract.Customer.TERMID			, c.getTermid());
		cv.put(DesContract.Customer.TARGET 			, c.getTarget());
		cv.put(DesContract.Customer.TYPEID 			, c.getTypeid());
		String where = "_id=" + c.getId();
		
		db.update(DesContract.Customer.TABLE_NAME, cv, where, null);
	}

	public void updateLatLng(String ccode,Double Latitude, Double Longitude) {
		String sql="UPDATE " + DesContract.Customer.TABLE_NAME +
				" SET " + DesContract.Customer.LATITUDE + " = " + Latitude + "," +
				DesContract.Customer.LONGITUDE + " = " + Longitude +
				" WHERE " + DesContract.Customer.CCODE + " = '" +  ccode +"'";

		db.execSQL(sql);
	}
	
	
	private Customer createCustomer(Cursor c) {
		Customer customer = new Customer();
		
		customer.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		customer.setCustomerCode(c.getString(
				c.getColumnIndex(DesContract.Customer.CCODE)));
		customer.setName(c.getString(
				c.getColumnIndex(DesContract.Customer.NAME)));
		customer.setAddress(c.getString(
				c.getColumnIndex(DesContract.Customer.ADDRESS)));
		customer.setBrgy(c.getString(
				c.getColumnIndex(DesContract.Customer.BRGY)));
		customer.setCity(c.getString(
				c.getColumnIndex(DesContract.Customer.CITY)));
		customer.setState(c.getString(
				c.getColumnIndex(DesContract.Customer.STATE)));
		customer.setContactNumber(c.getString(
				c.getColumnIndex(DesContract.Customer.CONTACT_NUMBER)));
		customer.setAcctypid(c.getInt(
				c.getColumnIndex(DesContract.Customer.ACCT_TYPEID)));
		customer.setDiscount(c.getDouble(
				c.getColumnIndex(DesContract.Customer.DISCOUNT)));
		customer.setLatitude(c.getDouble(
				c.getColumnIndex(DesContract.Customer.LATITUDE)));		
		customer.setLongitude(c.getDouble(
				c.getColumnIndex(DesContract.Customer.LONGITUDE)));
		customer.setCplanCode(c.getString(
				c.getColumnIndex(DesContract.Customer.CPLAN_CODE)));		
		customer.setStatus(c.getInt(
				c.getColumnIndex(DesContract.Customer.STATUS)));		
		customer.setBuffer(c.getInt(
				c.getColumnIndex(DesContract.Customer.BUFFER)));
		customer.setCashSales(c.getInt(
				c.getColumnIndex(DesContract.Customer.CASH_SALES)));
		customer.setTermid(c.getInt(
				c.getColumnIndex(DesContract.Customer.TERMID)));
		customer.setTarget(c.getInt(
				c.getColumnIndex(DesContract.Customer.TARGET)));
		customer.setTypeid(c.getInt(
				c.getColumnIndex(DesContract.Customer.TYPEID)));
		customer.setAr(c.getString(
				c.getColumnIndex(DesContract.Customer.AR)));
		return customer;
	}
	
	public String updateNewCustomer(){
//		ContentValues cv = new ContentValues();
//		cv.put("status", 0);
//		db.update("customers", cv, "ccode='TEMP0001'", null);
		
		String ccode = "";
		String orderBy = " ORDER BY RANDOM() LIMIT 1";
		String where = " WHERE "+DesContract.Customer.STATUS+"=0";
		String sql = "SELECT "+DesContract.Customer.CCODE+" FROM "+DesContract.Customer.TABLE_NAME+where+orderBy;
		
		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst()) ccode = c.getString(0);
		
		return ccode;		
	}
	
	public ArrayList<String> getArrayList(String table_name){
		ArrayList<String> state = new ArrayList<String>();
		String col = "description";
		if(table_name.contains("suppliers")) col = " dealer||' '||area||' '||region as description ";
		String sql="SELECT "+col+" FROM "+table_name+" ORDER BY _id";
		
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					state.add(c.getString(0));
					//Log.d("getArrayList",""+c.getString(0));
				}
			}
		}	
		c.close();
		
		return state;
		
	}
	
	public Integer getRegionId(String region) {
		String sql="SELECT regionid FROM region WHERE description ='" + region + "'";
		
		int regionId=0;
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			regionId = c.getInt(0); 
		}				
		c.close();
		
		return regionId;
		
	}
	
	//DATABASE MANAGER FOR CUSTOMER NEW DATA OWNER and PURCHASER
	
	public void AddNewData(CustomerData c) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.CusOwnerData.CCODE 			, c.getCcode());
		cv.put(DesContract.CusOwnerData.NAME          	, c.getOname());
		cv.put(DesContract.CusOwnerData.CSTATUS       	, c.getOstat());
		cv.put(DesContract.CusOwnerData.BDAY	       	, c.getObday());
		cv.put(DesContract.CusOwnerData.HOBINT         	, c.getOhobInt());
		cv.put(DesContract.CusOwnerData.PHONE         	, c.getOphone());
		cv.put(DesContract.CusOwnerData.EMAIL			, c.getOemail());
		cv.put(DesContract.CusOwnerData.STATUS 			, c.getOstatus());
		db.insertWithOnConflict(DesContract.CusOwnerData.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
//
//		ContentValues cv2 = new ContentValues();
//
//		cv2.put(DesContract.GeneralProfile.CCODE 	, c.getCcode());
//		cv2.put(DesContract.GeneralProfile.AVERAGE 	, c.getAverage());
//		cv2.put(DesContract.GeneralProfile.FREQ 	, c.getFreq());
//		cv2.put(DesContract.GeneralProfile.LOAN		, c.getLoan());
//		cv2.put(DesContract.GeneralProfile.ROUTE	, c.getRoute());
//		cv2.put(DesContract.GeneralProfile.STATUS 	, 1);
//		Log.e("getters",c.getCcode()+"\n"+c.getAverage()+"\n"+c.getFreq()+"\n"+c.getLoan()+"\n"+c.getRoute());
//		db.insertWithOnConflict(DesContract.GeneralProfile.TABLE_NAME, null, cv2, SQLiteDatabase.CONFLICT_REPLACE);
		
		//return db.insertWithOnConflict(DesContract.Customer.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);		
	}
	
	public CustomerData getCustomerDataInfo2(String cCode,int status) {
//		String sql="SELECT * "  + 
//				" FROM customers " +
//				" WHERE ccode LIKE '" + cCode +"'";
		
		String sql = "SELECT o.ccode, o.name as oname, o.civil_status as ocstatus, o.birthdate as obday, " +
				     "o.hobbies_interests as ohi, o.phone as ophone, o.email as oemail, o.status as ostatus " +
				     " FROM owner o " +
				     "where o.ccode='"+cCode+"' AND status="+status;
		
		Cursor c = db.rawQuery(sql,null);
		
		CustomerData cust;
		if(c.moveToFirst()) {
			cust = createCustomerData2(c);
		} else {
			cust = null;
		}
		
		c.close();
		
		return cust;
	}
	
	private CustomerData createCustomerData2(Cursor c) {
		CustomerData customer = new CustomerData();
		
		customer.setOname(c.getString(c.getColumnIndex("oname")));
		customer.setOstat(c.getString(c.getColumnIndex("ocstatus")));
		customer.setObday(c.getString(c.getColumnIndex("obday")));
		customer.setOhobInt(c.getString(c.getColumnIndex("ohi")));
		customer.setOphone(c.getString(c.getColumnIndex("ophone")));
		customer.setOemail(c.getString(c.getColumnIndex("oemail")));
		customer.setCcode(c.getString(c.getColumnIndex("ccode")));
		
//		customer.setAverage(c.getString(c.getColumnIndex(
//				DesContract.GeneralProfile.AVERAGE)));
//		customer.setLoan(c.getString(c.getColumnIndex(
//				DesContract.GeneralProfile.LOAN)));
//		customer.setRoute(c.getString(c.getColumnIndex(
//				DesContract.GeneralProfile.ROUTE)));
//		customer.setFreq(""+c.getInt(c.getColumnIndex(
//				DesContract.GeneralProfile.FREQ)));
		
		return customer;
	}
	
	public int getNoTrucks(String ccode){
		int unit = 0;
		String sql = "SELECT SUM(quantity) FROM trucks WHERE ccode ='"+ccode+"'";
		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst()) unit = c.getInt(0);
		return unit;
	}
	
	public int getSupplierId(String supplier){
		int spid = 0;
		String sql = "SELECT spid FROM suppliers WHERE (dealer||' '||area||' '||region) LIKE '%"+supplier+"%'";
		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst()) spid = c.getInt(0);
		return spid;
	}

	public ArrayList<String> getAddress(int opt){
		ArrayList<String> str = new ArrayList<>();
		str.add("-");
		String sql = "";
		switch (opt){
			case 0: sql = "SELECT DISTINCT city FROM customers WHERE city != '' AND city IS NOT NULL AND city != 'null'";
					break;
			case 1: sql = "SELECT DISTINCT state FROM customers WHERE state != '' AND state IS NOT NULL AND state != 'null'";
					break;
		}
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					str.add(c.getString(0));
				}
			}
		}
		c.close();

		return str;
	}

	public List<CustomerData> getOneTwoBday(int days) {
		long twoDays = System.currentTimeMillis()+172800000; //+ 2 days
		long oneDay = System.currentTimeMillis()+86400000; //+ 1 days
		long today = System.currentTimeMillis();
		List<CustomerData> list = new ArrayList<>();

		switch (days){
			case 2: list = getCustomersBday(DateUtil.phLongDay(twoDays)); break;
			case 1: list = getCustomersBday(DateUtil.phLongDay(oneDay)); break;
			case 0: list = getCustomersBday(DateUtil.phLongDay(today)); break;
		}

		return list;
	}
	private List<CustomerData> getCustomersBday(String days) {
		List<CustomerData> list = new ArrayList<>();

		String sql = "SELECT o.ccode, o.name as oname, o.civil_status as ocstatus, o.birthdate as obday, " +
				"o.hobbies_interests as ohi, o.phone as ophone, o.email as oemail, o.status as ostatus " +
				" FROM owner o " +
				" WHERE birthdate LIKE '%"+days+"%' ";
		Cursor c = db.rawQuery(sql,null);

//		Log.e("sql",sql+">>"+c.getCount()+"<<");

		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				CustomerData cust = createCustomerData2(c);
				list.add(cust);
			}
		}

		c.close();

		return list;
	}

	public ArrayList<String> getCustomerDataInfo3(String cCode, int status) {
		String sql = "SELECT o.name as oname, o.civil_status, o.birthdate as obday, " +
				"o.hobbies_interests as ohi, o.phone as ophone, o.email as oemail " +
				"FROM owner o " +
				"where o.ccode='"+cCode+"' AND status="+status;

		Cursor c = db.rawQuery(sql,null);

		ArrayList<String> values = new ArrayList<>();

//		Log.e("count",""+c.getColumnCount());

		if(c.moveToFirst()) {
			for (int i = 0; i < c.getColumnCount(); i++) {
				try {
					String column_value = c.getString(i).trim();
					if (!column_value.isEmpty() || column_value != null) {
						if (c.getColumnName(i).contains("obday"))
							column_value = DateUtil.reformatDay2(column_value);
					}

					values.add(column_value);
				}catch (Exception e) {
					values.add("");
					e.printStackTrace();}
			}
		}
		else{
			int j=0;
			while(j<c.getColumnCount()){
				values.add("");
				j++;
			}
		}

		c.close();

		return values;
	}
}
