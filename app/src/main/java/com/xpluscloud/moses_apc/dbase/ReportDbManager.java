package com.xpluscloud.moses_apc.dbase;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import java.util.Locale;


@SuppressLint("DefaultLocale")
public class ReportDbManager extends DbManager {	

	private enum Table {
		CUSTOMERS,ITEMS,COVERAGEPLANS,PERIODS,DELIVERIES,COLLECTIBLES,INVENTORY,PULLOUTS;
	}
	
	
	public ReportDbManager(Context context) {
		super(context);
	}
	
	public Cursor getCursor(String TableName) {
		String sql="SELECT * FROM " + TableName;
		return db.rawQuery(sql,null);
	}

	
	public Cursor getCursor(String TableName, String cCode, String date1, String date2) {
		
		Table cmd = Table.valueOf(TableName.toUpperCase(Locale.getDefault()));
		String sql = "";
		
		switch(cmd) {
			case DELIVERIES:
				sql = this.sqlDelivery(cCode, date1, date2); 
				break;
			
			case COLLECTIBLES:
				sql = this.sqlCollectible(cCode); 
				break;
				
			case INVENTORY:
				sql=this.sqlInventory();
				break;
				
			case COVERAGEPLANS:
				sql=this.sqlCplan();
				break;	
			
			case PERIODS:
				sql=this.sqlPeriod();
				break;		
				
			default:
				sql="";
				break;		
		}
		
		return db.rawQuery(sql,null);
	}
	
	
	
	
	private String sqlDelivery(String cCode, String date1, String date2) {
		
		  String sql = "SELECT A._id, " +
				"A.date," +
				"A.invoiceno," +
				"A.sono," +
				"B.description," +
				"A.pckg," +
				"A.price," +
				"A.qty " +
			" FROM deliveries A " +
			" LEFT JOIN items B " +
			" ON A.itemcode=B.itemcode " +
			" WHERE A.ccode LIKE '" + cCode + "' ";
		  /*+
					" AND A.date BETWEEN '" + date1 + "' " +
							" AND '" + date2 + "' ";*/
		
		
		return sql;
	}
	
	private String sqlCollectible(String cCode) {
		
		String sql="SELECT _id,date,invoiceno,amount  " +
				" FROM collectibles  " +
				" WHERE ccode LIKE '" + cCode + "' " +
				" ORDER BY date ASC " ;
		return sql;
	}

	
	private String sqlInventory() {
		
		String sqlSold="(SELECT SUM(order_qty) " +
				" FROM callsheetitems C " +
				" WHERE C.itemcode=A.itemcode " +
					" AND C.pckg LIKE A.pckg " +
					" AND C.date <= A.date" +
				"GROUP BY C.itemcode) as qtysold";
		
		
		String sql = "SELECT A._id, " +
				"A.date," +
				"B.description," +
				"A.pckg," +
				"A.price," +
				"A.qty AS BegBal" + sqlSold +				
			" FROM inventory A " +
			" LEFT JOIN items B " +
			" ON A.itemcode=B.itemcode ";
		return sql;
	}
	
	private String sqlCplan() {
		
		String sql = "SELECT cplan_code 'CPLAN CODE', week_schedule WEEK,day_schedule DAY" +
				" FROM coverageplans ORDER BY cplan_code ";
		return sql;
	}
	
	private String sqlPeriod() {
		
		String sql = "SELECT  week_start,week_end, nweek 'WEEK#' " +
				" FROM periods ORDER BY week_start ";
		return sql;
	}
	
}
