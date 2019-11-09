package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.xpluscloud.mosesshell_davao.getset.CoveragePlan;
import com.xpluscloud.mosesshell_davao.getset.Customer;

import java.util.ArrayList;
import java.util.List;

public class CoveragePlanDbManager extends DbManager {
	
	public static final String[] COVERAGEPLAN_PROJECTION = new String[] {
		DesContract.CoveragePlan._ID,
		DesContract.CoveragePlan.CPID,
		DesContract.CoveragePlan.CPLAN_CODE,
		DesContract.CoveragePlan.DESCRIPTION,
		DesContract.CoveragePlan.WEEK_SCHEDULE,
		DesContract.CoveragePlan.DAY_SCHEDULE,		
		DesContract.CoveragePlan.STATUS
	};
	
	public CoveragePlanDbManager(Context context) {
		super(context); 
	}
	
	public long AddCoveragePlan(CoveragePlan cp) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.CoveragePlan.CPID 			, cp.getCpId());
		cv.put(DesContract.CoveragePlan.CPLAN_CODE		, cp.getCplanCode());
		cv.put(DesContract.CoveragePlan.DESCRIPTION    	, cp.getDescription());
		cv.put(DesContract.CoveragePlan.WEEK_SCHEDULE 	, cp.getWeekSchedule());
		cv.put(DesContract.CoveragePlan.DAY_SCHEDULE   	, cp.getDaySchedule());	
		cv.put(DesContract.CoveragePlan.STATUS 			, cp.getStatus());
		
		//return db.insert(DesContract.CoveragePlan.TABLE_NAME, null, cv);
		return db.insertWithOnConflict(DesContract.CoveragePlan.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void AddCoveragePlans(List<CoveragePlan> list) {
		for(int i = 0; i < list.size(); i++) {
			AddCoveragePlan(list.get(i));
		}
	}
	
	public CoveragePlan getCoveragePlan(int rowId) {
		CoveragePlan cplan;
		Cursor c = retrieveCursor(rowId, DesContract.CoveragePlan.TABLE_NAME, COVERAGEPLAN_PROJECTION);
		
		if(c.moveToFirst()) {
			cplan = createCoveragePlan(c);
		} else {
			cplan = null;
		}
		
		c.close();
		
		return cplan;
	}
	
	
	public Integer getCpId(String cpcode) {
		String sql="SELECT cpid FROM coverageplans WHERE cplan_code ='" + cpcode + "'";
		
		int cpid=0;
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			cpid = c.getInt(0); 
		}				
		c.close();
		
		return cpid;
		
	}
	
	public String getStrCp(int cpid) {
		String sql="SELECT cplan_code FROM coverageplans WHERE cpid ='" + cpid + "'";
		
		String code="";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			code = c.getString(0); 
		}				
		c.close();
		
		return code;
		
	}
	
	public List<Customer> getItinerary(Integer week, Integer day) {
		List<Customer> list = new ArrayList<Customer>();
		
		String where="WHERE cp.week_schedule LIKE '%" + week + "%' AND " +
				"cp.day_schedule LIKE '%" + day + "%'";
		
		//String where="WHERE " + week + " LIKE cp.week_schedule ||'%' AND " + day + " LIKE  cp.day_schedule || '%'";
		
		String sql="SELECT cus._id,ccode,name,address,brgy,city,state,contact_number,cus.cplan_code,cus.status,cus.latitude,cus.longitude " +
				"FROM customers cus " +
				"INNER JOIN coverageplans cp " +
					"ON cus.cplan_code=cp.cplan_code " 
				+ where ;
				
//		Log.w("getItinerary",sql);
		Cursor c = db.rawQuery(sql, null);
		
		
		
		if (c != null && c.moveToFirst()) {			
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					Customer cplan = createCustomer(c);
					list.add(cplan);
				}				
			}	     
		}	
		c.close();			
		return list;
	}
	
	/*
	public List<Customer> getNonSchedCPlan(Integer week,Integer day) {
		List<Customer> list = new ArrayList<Customer>();
		
		String where="WHERE (cp.week_schedule NOT LIKE '%" + week + "%' AND " +
				"cp.day_schedule NOT LIKE '%" + day + "%') OR (cp.week_schedule='')";
		
		//String where="WHERE " + week + " LIKE cp.week_schedule ||'%' AND " + day + " LIKE  cp.day_schedule || '%'";
		
		String sql="SELECT cus._id,name,address,cus.cplan_code " +
				"FROM customers cus " +
				"INNER JOIN coverageplans cp " +
					"ON cus.cplan_code=cp.cplan_code " 
				+ where ;
				
				
		Cursor c = db.rawQuery(sql, null);
		if (c != null && c.moveToFirst()) {			
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					Customer cplan = createCustomer(c);
					list.add(cplan);
				}				
			}	     
		}	
		c.close();			
		return list;
	}
	*/
	public ArrayList<String> getArrayList(){
		ArrayList<String> cpcodes = new ArrayList<String>();
		
		String sql="SELECT cplan_code FROM coverageplans ORDER BY cplan_code";
		
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					cpcodes.add(c.getString(0));
					//Log.d("getArrayList",""+c.getString(0));
				}
			}
		}	
		c.close();
		
		return cpcodes;
		
	}
	
/*	
public List<CoveragePlan> getAllCoveragePlans() {
		
		List<CoveragePlan> list = new ArrayList<CoveragePlan>();
		Cursor c = retrieveCursor(DesContract.CoveragePlan.TABLE_NAME, COVERAGEPLAN_PROJECTION);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				CoveragePlan cplan = createCoveragePlan(c);
				list.add(cplan);
			}
		}
		
		c.close();
		
		return list;
	}
	
*/
	
	private CoveragePlan createCoveragePlan(Cursor c) {
		CoveragePlan CoveragePlan = new CoveragePlan();
		
		CoveragePlan.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		
		CoveragePlan.setCpId(c.getInt(
				c.getColumnIndex(DesContract.CoveragePlan.CPID)));		
		CoveragePlan.setCplanCode(c.getString(
				c.getColumnIndex(DesContract.CoveragePlan.CPLAN_CODE)));
		CoveragePlan.setDescription(c.getString(
				c.getColumnIndex(DesContract.CoveragePlan.DESCRIPTION)));
		CoveragePlan.setWeekSchedule(c.getString(
				c.getColumnIndex(DesContract.CoveragePlan.WEEK_SCHEDULE)));
		CoveragePlan.setDaySchedule(c.getString(
				c.getColumnIndex(DesContract.CoveragePlan.DAY_SCHEDULE)));
		CoveragePlan.setStatus(c.getInt(
				c.getColumnIndex(DesContract.CoveragePlan.STATUS)));
		return CoveragePlan;
	}
	
	private Customer createCustomer(Cursor c) {
		Customer Customer = new Customer();
		
		Customer.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		Customer.setCustomerCode(c.getString(
				c.getColumnIndex(DesContract.Customer.CCODE)));
		Customer.setName(c.getString(
				c.getColumnIndex(DesContract.Customer.NAME)));		
		Customer.setAddress(c.getString(
				c.getColumnIndex(DesContract.Customer.ADDRESS)));
		Customer.setCplanCode(c.getString(
				c.getColumnIndex(DesContract.Customer.CPLAN_CODE)));
		Customer.setContactNumber(c.getString(
				c.getColumnIndex(DesContract.Customer.CONTACT_NUMBER)));
		Customer.setCity(c.getString(
				c.getColumnIndex(DesContract.Customer.CITY)));
		Customer.setState(c.getString(
				c.getColumnIndex(DesContract.Customer.STATE)));
		Customer.setBrgy(c.getString(
				c.getColumnIndex(DesContract.Customer.BRGY)));
		Customer.setLatitude(c.getDouble(
				c.getColumnIndex(DesContract.Customer.LATITUDE)));
		Customer.setLongitude(c.getDouble(
				c.getColumnIndex(DesContract.Customer.LONGITUDE)));
		Customer.setStatus(c.getInt(c.getColumnIndex(DesContract.Customer.STATUS)));
		return Customer;
	}
	
}
