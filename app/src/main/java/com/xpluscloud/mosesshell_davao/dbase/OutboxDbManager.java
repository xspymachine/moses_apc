package com.xpluscloud.mosesshell_davao.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import com.xpluscloud.mosesshell_davao.getset.Outbox;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OutboxDbManager extends DbManager {
	
	public static final String[] OUTBOX_PROJECTION = new String[] {
		DesContract.Outbox._ID,
		DesContract.Outbox.DATETIME,
		DesContract.Outbox.RECIPIENT,
		DesContract.Outbox.MESSAGE,
		DesContract.Outbox.PRIORITY,
		DesContract.Outbox.STATUS
	};
		
	
	public OutboxDbManager(Context context) {
		super(context);
	}
	
	
	public long AddMessage(Outbox c) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Outbox.DATETIME		, c.getDateTime());
		cv.put(DesContract.Outbox.RECIPIENT     , c.getRecipient());
		cv.put(DesContract.Outbox.MESSAGE       , c.getMessage());
		cv.put(DesContract.Outbox.PRIORITY 		, c.getPriority());
		cv.put(DesContract.Outbox.STATUS 		, c.getStatus());
		
		return db.insert(DesContract.Outbox.TABLE_NAME, null, cv);
	}
	
	public void AddOutbox(List<Outbox> list) {
		for(int i = 0; i < list.size(); i++) {
			AddMessage(list.get(i));
		}
	}
	
	public Outbox getOutbox(int rowId) {
		Outbox cust;
		Cursor c = retrieveCursor(rowId, DesContract.Outbox.TABLE_NAME, OUTBOX_PROJECTION);
		
		if(c.moveToFirst()) {
			cust = createOutbox(c);
		} else {
			cust = null;
		}
		
		c.close();
		
		return cust;
	}
	
	public Outbox getSingleQueMsg(int status) {	
			
		Outbox row = null;
		//String sql = "SELECT * FROM outbox WHERE status=0 OR status=2 OR status=3 ORDER BY priority ASC  LIMIT 1";
		String sql = "SELECT * FROM outbox WHERE status=" + status + " AND priority < 100 ORDER BY datetime ASC  LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			row = createOutbox(c);
			//Set status to sending (=3) right away
			Integer newstat=3;
			if (row.getPriority()>=60) newstat = 2;
			db.execSQL("UPDATE outbox SET status=" + newstat + ", priority=priority+1 WHERE _id=" + row.getId());
		}		
		return row;		
	}
	
	
	public Outbox getPendingMsg(int status) {
		
		Outbox row = null;
		String sql = "SELECT * FROM outbox WHERE status=2 OR status=3 ORDER BY priority ASC  LIMIT 1";
		
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			row = createOutbox(c);
			//Set status to sending (=3) right away
			Integer newstat=3;
			if (row.getPriority()>=100) newstat = 2;
			db.execSQL("UPDATE outbox SET status=" + newstat + ", priority=priority+1 WHERE _id=" + row.getId());
		}		
		return row;		
	}
	
	
	public List<Outbox> getAllMessages() {
		delete_old();	
		List<Outbox> list = new ArrayList<Outbox>();
		String sql = "SELECT * FROM outbox ORDER BY  _id DESC";
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					Outbox box = createOutbox(c);
					list.add(box);
				}
			}
		}
		c.close();		
		return list;
	}
	
	public List<Outbox> getDispMessages() {
		delete_old();	
		List<Outbox> list = new ArrayList<Outbox>();
		String sql = "SELECT * FROM outbox WHERE message NOT LIKE '%XPT911%' AND message NOT LIKE '%CMDLOG%' AND message NOT LIKE '%CMDTOK%' ORDER BY  _id DESC";
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					Outbox box = dispOutbox(c);
					Log.e("outbox_msgs",c.getString(
							c.getColumnIndex(DesContract.Outbox.MESSAGE)));
					list.add(box);
				}
			}
		}
		c.close();		
		return list;
	}
	
	
	public void delete_old() {		
		Calendar now = Calendar.getInstance();
		long ago = now.getTimeInMillis()-1000*60*60*24*30l; //30 days ago.		
				
		String sql = "DELETE FROM outbox WHERE datetime < "+ ago +" AND status=1";
		db.execSQL(sql);
	}
	
	public void deleteAll() {
		String sql = "DELETE FROM outbox WHERE status=1";
		db.execSQL(sql);
	}
	
	
	public List<Outbox> getAllQueue() {
		List<Outbox> list = new ArrayList<Outbox>();
		String sql = "SELECT * FROM outbox WHERE status=0  ORDER BY _id DESC";
		Cursor c = db.rawQuery(sql,null);
		
		if (c != null && c.moveToFirst()) {
			for(int i = 0; i < c.getCount(); i++) {
				if(c.moveToPosition(i)) {
					Outbox box = createOutbox(c);
					list.add(box);
				}
			}
		}
		c.close();
		
		return list;
		
	}
	
	
	public int getLastId() {
		int lastId = 0;
		
		String sql = "SELECT _id FROM outbox ORDER BY _id DESC LIMIT 1";
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
		    lastId = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}	
		
		return lastId;
	}
	
	public JSONArray getAllUnsent() {
		String sql = "SELECT * FROM outbox WHERE status=0 OR status=2 OR status=3 ORDER BY _id ASC LIMIT 50";
		Cursor c = db.rawQuery(sql,null);
		 
		JSONArray resultSet 	= new JSONArray();
		//JSONObject returnObj 	= new JSONObject();
		 
		c.moveToFirst();
		while (c.isAfterLast() == false) {
			int totalColumn = c.getColumnCount();
			
			//Log.d("TAG_NAME", c.getColumnName(totalColumn-1));
			
	       	JSONObject rowObject = new JSONObject();
	       	for( int i=0 ;  i< totalColumn ; i++ ){
	       		if( c.getColumnName(i) != null ) {
	       			try {
	       				if( c.getString(i) != null ) {
	       					//Log.d("TAG_NAME", c.getString(i) );
	       					rowObject.put(c.getColumnName(i) ,  c.getString(i) );
	       				}
						else{
							rowObject.put( c.getColumnName(i) ,  "" ); 
						}
					}
					catch( Exception e ){
						Log.d("Exception", e.getMessage() );
					}
				}
					 
			}
					 
			resultSet.put(rowObject);
			c.moveToNext();
		}
					 
		c.close(); 
		//Log.d("Get All unsent", resultSet.toString() );
		return resultSet; 
	}
	
	
	public void delete(Integer Id) {
		String where = "_id=" + Id;
		db.delete(DesContract.Outbox.TABLE_NAME, where, null);
	}
	
	public void update(Outbox c) {
		ContentValues cv = new ContentValues();
		cv.put(DesContract.Outbox.DATETIME		, c.getDateTime());
		cv.put(DesContract.Outbox.RECIPIENT     , c.getRecipient());
		cv.put(DesContract.Outbox.MESSAGE       , c.getMessage());
		String where = "_id=" + c.getId();
		
		db.update(DesContract.Outbox.TABLE_NAME, cv, where, null);
	}
	
	
	public void updateStatus(Integer id, Integer status) {
		
		String sql="UPDATE outbox SET " +
				" status=" + status + 
				", priority=priority + 1 "  + 
				" WHERE _id=" + id;
		
		db.execSQL(sql);
	}
		
	public void resetPriority(Integer id) {
		String sql="UPDATE outbox SET " +
				" priority=0"  + 
				" WHERE _id=" + id;		
		db.execSQL(sql);
	}
	
	private Outbox createOutbox(Cursor c) {
		Outbox outbox = new Outbox();		
		outbox.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		outbox.setDateTime(c.getString(
				c.getColumnIndex(DesContract.Outbox.DATETIME)));
		outbox.setRecipient(c.getString(
				c.getColumnIndex(DesContract.Outbox.RECIPIENT)));
		outbox.setMessage(c.getString(
				c.getColumnIndex(DesContract.Outbox.MESSAGE)));
		outbox.setPriority(c.getInt(
				c.getColumnIndex(DesContract.Outbox.PRIORITY)));
		outbox.setStatus(c.getInt(
				c.getColumnIndex(DesContract.Outbox.STATUS)));
		
		return outbox;
	}
	
	private Outbox dispOutbox(Cursor c) {
		Outbox outbox = new Outbox();		
		outbox.setId(c.getInt(
				c.getColumnIndex(BaseColumns._ID)));
		outbox.setDateTime(c.getString(
				c.getColumnIndex(DesContract.Outbox.DATETIME)));
		outbox.setRecipient(c.getString(
				c.getColumnIndex(DesContract.Outbox.RECIPIENT)));
		String message = c.getString(
				c.getColumnIndex(DesContract.Outbox.MESSAGE));
		
		String msgToDisplay = getStrCommand(message);
		outbox.setMessage(msgToDisplay);
		
		outbox.setPriority(c.getInt(
				c.getColumnIndex(DesContract.Outbox.PRIORITY)));
		outbox.setStatus(c.getInt(
				c.getColumnIndex(DesContract.Outbox.STATUS)));		
		
		
		return outbox;
	}
	
	
	
	private enum Command {
		CMDREG1, CMDLOC, CMDTIO, CMDMRK, CMDCCL,CMDCCL2,
		CMDCUA2, CMDCUU2,CMDCUA3, CMDCUU3,CMDINV,CMDOWN3,
		CMDCUD, CMDUDT, CMDSOR, CMDSIG,CMPREM,SURREM,CMDPMB,CMDINV2,CMDINVI2,CMDCMP6,CMDCMPI6,
		CMDSET, CMDBOT,CMDCST,CMDCSI,CMDPIC,CMDPIC2,GPSOFF,GPSON,
		CMDGPS,CMDCOL1,CMDRET,CMDCMP2,CMDOWN,CMDPUR,CMDLOG,
		CMDMER,CMDODATA,XPT911,CMDMSTAT,CMDTRUCK,CMDWHCAP,CMDUCST,
		CMDGPDATA,CMDCHK,CMDISS,CMDCMP5,CMDSUR,CMDSCL,CMDISS2
	}
	
	private String getStrCommand(String message) {
		String[] splitCmd = message.split(" ");
				
		String strCommand="";
		
		Command cmd = Command.valueOf(splitCmd[0]);		
		switch(cmd) {
			case CMDREG1:
				strCommand="REGISTER";
				break;
				
			case CMDLOC:
				strCommand="Location Update: ";
				break;
			
			case CMDTIO:
				strCommand="In/Out";
				break;
				
			case CMDMRK:
				strCommand="Mark Location";
				break;

			case CMDSCL:
			case CMDCCL2:
				strCommand="Customer Call";
				break;
				
			case CMDCUA2:
			case CMDCUA3:
				strCommand="Add New Customer";
				break;
				
			case CMDCUU2:
			case CMDCUU3:
				strCommand="Update Customer";
				break;
				
			case CMDUDT:
				strCommand="Location Update: ";
				break;	
				
			case CMDSOR:
				strCommand="Sales Order";
				break;	
				
			case CMDSIG:
				strCommand="Signature Info";
				break;
			
			case CMDPIC:
				strCommand="Picture Info";
				break;		
				
			case CMDSET:
				strCommand="System Settings";
				break;	
			
			case CMDBOT:
				strCommand="System Started";
				break;
			case CMDCST:
				strCommand="Call Sheet";
				break;
			case CMDCSI:
				strCommand="Call Sheet Item";
				break;	
			case GPSOFF:
				strCommand="GPS OFF ";
				break;	
			case GPSON:
				strCommand="GPS ON ";
				break;
			case CMDGPS:
				strCommand="GPS STATUS";
				break;	
				
			case CMDCOL1:
				strCommand="Collection ";
				break;
				
			case CMDRET:
				strCommand="Product Return ";
				break;
				
			case CMDCMP2:
				strCommand="Competitors Item";
				break;
				
			case CMDOWN:
			case CMDOWN3:
				strCommand="Customer Update Owner";
				break;
				
			case CMDPUR:
				strCommand="Customer Update Purchaser";
				break;
				
			case CMDLOG:
				strCommand="Improper Logout/Application suddenly forced closed";
				break;
				
			case CMDMSTAT:
			case CMDMER:
				strCommand="Merchandising Checklist";
				break;
				
			case CMDODATA:
				strCommand = "Customer Other Data";
				break;
				
			case XPT911:
				strCommand = "ALERT";
				break;	
				
			case CMDTRUCK:
				strCommand = "Customer Truck Details";
				break;
				
			case CMDWHCAP:
				strCommand = "Warehouse Capacity Details";
				break;
				
			case CMDUCST:
				strCommand = "Sales Order is served and delivered";
				break;
				
			case CMDGPDATA:
				strCommand = "Customer General Profile";
				break;

			case CMDCHK:
                strCommand = "Compliance Checklist";
				break;

			case CMDISS:
			case CMDISS2:
				strCommand = "Customer Issues/Concern";
				break;
			case CMDCMP5:
			case CMPREM:
				strCommand = "Retail Data";
				break;
			case CMDSUR:
			case SURREM:
				strCommand = "Survey Data";
				break;
			case CMDINVI2:
				strCommand = "Customer Inventory Item";
				break;
			case CMDINV2:
				strCommand = "Customer Inventory";
				break;
			case CMDCMP6:
				strCommand = "Competitor Pricing Item";
				break;
			case CMDCMPI6:
				strCommand = "Competitor Pricing";
				break;
			case CMDINV:
				strCommand = "Call Sheet Inventory";
				break;
			case CMDPMB:
				strCommand = "Customer Promo";
				break;
				
			default:
				strCommand="";
				break;		
		}
		
		String newMsg=message.substring(splitCmd[0].length()+1, message.length());
		
		String[] splitMsg = message.split(";");
		
		if(splitMsg.length>1) {
			Log.d("splitMsg",splitMsg[1] + ":" + message);
			
			String customer = getCustomerName(splitMsg[1]);
			
			
			
			switch(cmd) {
				case CMDTIO:
				case CMDMRK:
				case CMDSCL:
				case CMDCCL2:
				case CMDCUA2:
				case CMDCUU2:
				case CMDCUA3:
				case CMDCUU3:	
				case CMDSOR:
				case CMDSIG:
				case CMDPIC:
				case CMDBOT:	
				case CMDCST:
				case CMDCOL1:
				case CMDLOG:
				case CMDTRUCK:	
				case CMDWHCAP:
				case CMDUCST:
				case CMDINV:
                case CMDCHK:
				case CMDISS:
				case CMDISS2:
				case CMDRET:
					String itemName = getItemName(splitMsg[2]);
					newMsg= strCommand +": " + customer + " - " + itemName;
					break;
				case CMDSUR:
				case CMDCMP5:
					String cmpitemName = getCitemName(splitMsg[2]);
					newMsg= strCommand +": " + customer + " - " + cmpitemName;
					break;
				case CMDGPS:
					String gpsStat = splitMsg[1];
					newMsg= strCommand +": " + gpsStat;
					break;	
				case CMDLOC:
					newMsg = strCommand +": With GPS coordinates!";
					break;
				case CMDUDT:
					newMsg = "Device Update: " + message;
					break;
				case CMDCMPI6:
					String citem = splitMsg[1] + ": " + getItemName2(splitMsg[2]) + "\n PCKG: " + splitMsg[3];
					newMsg= strCommand +": " + citem ;
					break;
				case CMDINVI2:
				case CMDCSI:
					String item = splitMsg[1] + ": " + getItemName(splitMsg[2]) + "\n PCKG: " + splitMsg[3];
					newMsg= strCommand +": " + item ;
					break;
				case GPSOFF:
				case GPSON:	
					newMsg = message;
					break;	
				case CMDGPDATA:
				case CMDODATA:
				case CMDOWN3:
				case CMDOWN:
					String ownName = getName(splitMsg[1], "owner");
					newMsg= strCommand +": " + customer + "\nOwner Name - " + ownName;
					break;
				case CMDPUR:
					String purName = getName(splitMsg[1], "purchaser");
					newMsg= strCommand +": " + customer + "\nPurchaser Name - " + purName;
					break;
				case CMDCMP2:
					String citemName = getCitemName(splitMsg[3]);
					newMsg= strCommand +": " + customer + " - " + citemName;
					break;	
				case CMDMER:
					newMsg= strCommand +": " + customer;
					break;
				case CMDMSTAT:
					newMsg = strCommand +": "+splitMsg[2]+" - "+getMCname(Integer.parseInt(splitMsg[3]));
					break;
				case CMDPMB:
					newMsg = strCommand +": "+getCustomerName(splitMsg[2])+" - "+splitMsg[3];
					break;
				default:	    			
					message=message.substring(splitCmd[0].length()+1, message.length());				
					newMsg = strCommand + ": ";// + message;
					break;		
			}
		}    
		
		return newMsg;
	}
	
	private String getCustomerName(String ccode) {
		String customerName="";
		
		String sql="SELECT name || ' \n' || address FROM customers WHERE ccode='"+ccode+"'";
		
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			customerName = c.getString(0); 
		}	
		c.close();
		return customerName;
		
	}
	
	private String getItemName(String itemcode) {
		String itemName="";
		
		String sql="SELECT description FROM items WHERE itemcode='"+itemcode+"'";
		
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			itemName = c.getString(0); 
		}	
		c.close();
		return itemName;
		
	}
	private String getItemName2(String itemcode) {
		String itemName="";

		String sql="SELECT description FROM citem WHERE itemcode='"+itemcode+"'";

		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			itemName = c.getString(0);
		}
		c.close();
		return itemName;

	}
	
	private String getCitemName(String itemcode) {
		String itemName="";
		
		String sql="SELECT description FROM citem WHERE itemcode='"+itemcode+"'";
		
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			itemName = c.getString(0); 
		}	
		c.close();
		return itemName;
		
	}
	
	private String getName(String ccode, String table) {
		String name="";
		
		String sql="SELECT name FROM "+table+" WHERE ccode='"+ccode+"'";
		
		Cursor c = db.rawQuery(sql,null);
		if (c != null && c.moveToFirst()) {
			name = c.getString(0); 
		}	
		c.close();
		return name;
		
	}
	
	public String getMCname(int mcid) {
		// TODO Auto-generated method stub
		String name="";
		
		String sql = "SELECT description FROM "+DesContract.Checklists.TABLE_NAME+" where mcid ='"+mcid+"'";
		
		Cursor c = db.rawQuery(sql, null);
		
		if(c.moveToFirst()) name = c.getString(0);
		
		return name;
	}
	
	public void resendDateRange(long dateFrom, long dateTo){
		String sql = "UPDATE outbox SET status = 0,priority = 0 WHERE datetime >= "+dateFrom+" AND datetime <= "+dateTo;
		db.execSQL(sql);
		
		Log.e("sql",sql);
	}
	
	public void resendDateRange2(String where, long dateFrom, long dateTo){
		String sql = "UPDATE outbox SET status = 0,priority = 0 WHERE datetime >= "+dateFrom+" AND datetime <= "+dateTo+where;
		db.execSQL(sql);
		
		Log.e("sql",sql);
	}
}
