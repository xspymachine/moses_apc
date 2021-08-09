package com.xpluscloud.moses_apc.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.xpluscloud.moses_apc.getset.Period;

import java.util.ArrayList;
import java.util.List;

public class PeriodDbManager extends DbManager {
	
	public static final String[] PERIOD_PROJECTION = new String[] {
		DesContract.Period._ID,
		DesContract.Period.YEAR,
		DesContract.Period.NWEEK,
		DesContract.Period.WEEK_START,
		DesContract.Period.WEEK_END,			
		DesContract.Period.STATUS
	};
	
	public PeriodDbManager(Context context) {
		super(context);
	}
	
	

	public long AddPeriod(Period p) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Period.YEAR 			, p.getYear());
		cv.put(DesContract.Period.NWEEK			, p.getNweek());
		cv.put(DesContract.Period.WEEK_START    , p.getWeek_start());
		cv.put(DesContract.Period.WEEK_END 		, p.getWeek_end());		
		cv.put(DesContract.Period.STATUS 		, p.getStatus());
		
		//return db.insert(DesContract.Period.TABLE_NAME, null, cv);
		
		return db.insertWithOnConflict(DesContract.Period.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void AddPeriods(List<Period> list) {
		for(int i = 0; i < list.size(); i++) {
			AddPeriod(list.get(i));
		}
	}
	
	public int getNweek(String vdate) {
		Integer nWeek=0;
		
		String where="julianday('"+vdate+ "') BETWEEN julianday(week_start) AND julianday(week_end)";
		
		Cursor c = db.query(DesContract.Period.TABLE_NAME, new String[]{"nweek"},where,
				null, null, null, null);
		if (c != null && c.moveToFirst()) {
		     nWeek=c.getInt(0); //The 0 is the column index of n-week.		     
		}	
		c.close();	
		
		return nWeek;
	}
	

	public Period getPeriod(int rowId) {
		Period cplan;
		Cursor c = retrieveCursor(rowId, DesContract.Period.TABLE_NAME, PERIOD_PROJECTION);
		
		if(c.moveToFirst()) {
			cplan = createPeriod(c);
		} else {
			cplan = null;
		}
		
		c.close();
		
		return cplan;
	}
	
	public List<Period> getAllPeriods() {
		
		List<Period> list = new ArrayList<Period>();
		Cursor c = retrieveCursor(DesContract.Period.TABLE_NAME, PERIOD_PROJECTION);
		
		for(int i = 0; i < c.getCount(); i++) {
			if(c.moveToPosition(i)) {
				Period cplan = createPeriod(c);
				list.add(cplan);
			}
		}
		
		c.close();
		
		return list;
	}
	
	
	private Period createPeriod(Cursor c) {
		Period Period = new Period();
		
		Period.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		
		Period.setYear(c.getInt(
				c.getColumnIndex(DesContract.Period.YEAR)));		
		Period.setNweek(c.getInt(
				c.getColumnIndex(DesContract.Period.NWEEK)));
		Period.setWeek_start(c.getString(
				c.getColumnIndex(DesContract.Period.WEEK_START)));
		Period.setWeek_end(c.getString(
				c.getColumnIndex(DesContract.Period.WEEK_END)));		
		Period.setStatus(c.getInt(
				c.getColumnIndex(DesContract.Period.STATUS)));
		return Period;
	}
}
