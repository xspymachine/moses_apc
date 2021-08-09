package com.xpluscloud.moses_apc.dbase;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class AccountTypeDbManager extends DbManager{

	public AccountTypeDbManager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<String> getAcctTypes(){
		ArrayList<String> acct_types = new ArrayList<String>();
		acct_types.add("-");
		
		String sql="SELECT description FROM acct_types ORDER BY description ASC";
			
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					acct_types.add(c.getString(0));
//					Log.d("getArrayList",""+c.getString(0));
				}
			}
		}	
		c.close();
		
		return acct_types;
		
	}
	
	public int getTypeId(String type){
		int id = 0;
		
		String sql = "SELECT acct_type FROM acct_types WHERE description='"+type+"'";
		
		Cursor c = db.rawQuery(sql, null);
		
		if(c != null && c.moveToFirst()) id= c.getInt(0);
		
		return id;
	}
	
	public String getType(int typeId){
		String type = "";
		
		String sql = "SELECT description FROM acct_types WHERE acct_type="+typeId;
		
		Cursor c = db.rawQuery(sql, null);
		
		if(c != null && c.moveToFirst()) type= c.getString(0);
		
		return type;
	}

	public int getOtherDataId(String type,int status){
		int id = 0;

		String sql = "SELECT dataid FROM cus_datas WHERE description='"+type+"' AND status="+status;

		Cursor c = db.rawQuery(sql, null);

		if(c != null && c.moveToFirst()) id= c.getInt(0);

		return id;
	}

	public String getOtherDataSingle(int dataid , int status){
		String desc = "";

		String sql = "SELECT description FROM cus_datas WHERE dataid="+dataid+" AND status="+status;

		Cursor c = db.rawQuery(sql, null);

		if(c != null && c.moveToFirst()) desc= c.getString(0);

		return desc;
	}

	public String[] getOtherData(int status){
		ArrayList<String> datastr = new ArrayList<>();

		String sql = "SELECT description FROM cus_datas WHERE status="+status;

		Cursor c = db.rawQuery(sql, null);

		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					datastr.add(c.getString(0));
				}
			}
		}
		String[] stockArr = new String[datastr.size()];
		stockArr = datastr.toArray(stockArr);

		Log.e("promo count",""+datastr.size());
		return stockArr;
	}

}
