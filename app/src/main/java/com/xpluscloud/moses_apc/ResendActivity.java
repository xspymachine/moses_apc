package com.xpluscloud.moses_apc;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.xpluscloud.moses_apc.dbase.CompetitorDbManager2;
import com.xpluscloud.moses_apc.dbase.SyncDbManager;
import com.xpluscloud.moses_apc.getset.CallSheet;
import com.xpluscloud.moses_apc.getset.Inventory;
import com.xpluscloud.moses_apc.server.ResendTask;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.DialogManager;
import com.xpluscloud.moses_apc.util.Master;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ResendActivity extends ListActivity {
	static final String[] RESEND_LIST =
            new String[] {
				"Resend Calls ",
				"Resend Sales ",
				"Resend Customers ",
				"Resend Survey ",
//				"Resend Calls (via internet)",
//				"Resend Sales (via internet)",
//				"Resend Customers (via internet)",
//				"Resend Survey (via internet)",
			};
	
	Context context=ResendActivity.this;
	
	public final int CALLST		= 0;
	public final int SALEST     = 1;
	public final int CUSTOMERST	= 2;
	public final int SURVEYT	= 3;
//	public final int CALLS		= 5;
//	public final int SALES      = 6;
//	public final int CUSTOMERS	= 7;
//	public final int SURVEY		= 8;
	
	public String devId;
	public String APIK;
	
	// Connection detector 
    ConnectionDetector cd; 
    EditText etDate1;
    EditText etDate2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_resend);
    	
//    	Bundle b = getIntent().getExtras(); 
//	    devId = b.getString("devId");
    	
    	setTitle(getResources().getString(R.string.app_name) + " " 	+	
//				 getResources().getString(R.string.version )
				 " - " + "Resend Data") ; 
	    
	    devId = DbUtil.getSetting(context, Master.DEVID);
	    APIK= getResources().getString(R.string.apik);
	    
	    etDate1 = findViewById(R.id.inputDate1);
	    etDate2 = findViewById(R.id.inputDate2);

        etDate1.setText(DbUtil.strDate(System.currentTimeMillis()));
        etDate2.setText(DbUtil.strDate(System.currentTimeMillis()));

        setListAdapter(new SyncArrayAdapter(this, RESEND_LIST));
		
		final Calendar myCalendar1 = Calendar.getInstance();
		final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

		    @Override
		    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
		        // TODO Auto-generated method stub
		        myCalendar1.set(Calendar.YEAR, year);
		        myCalendar1.set(Calendar.MONTH, monthOfYear);
		        myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		        String myFormat = "yyyy-MM-dd"; //In which you need put here
			    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
			    etDate1.setText(sdf.format(myCalendar1.getTime()));			    
		    }

		};
		
		etDate1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(context, date1, myCalendar1
	                    .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
	                    myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
		final Calendar myCalendar2 = Calendar.getInstance();
		final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

		    @Override
		    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
		        // TODO Auto-generated method stub
		        myCalendar2.set(Calendar.YEAR, year);
		        myCalendar2.set(Calendar.MONTH, monthOfYear);
		        myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		        String myFormat = "yyyy-MM-dd"; //In which you need put here
			    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
			    etDate2.setText(sdf.format(myCalendar2.getTime()));			    
		    }

		};
		  
		etDate2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(context, date2, myCalendar2
	                    .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
	                    myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	// TODO Auto-generated method stub
    	
    	String from = etDate1.getText().toString();
    	String to = etDate2.getText().toString();
    	
    	if(from.isEmpty() && to.isEmpty()){
    		DialogManager.showAlertDialog(ResendActivity.this,
                    "Input Date Empty", 
                    "Inputed date ranges are empty", false);
    		return;  
    	}
    	else if(DbUtil.dateTomilli(from) > DbUtil.dateTomilli(to)){
    		DialogManager.showAlertDialog(ResendActivity.this, 
                    "Input Date Mismatch", 
                    "Inputted daterange 'from' is greater than inputted daterange 'to'", false);
    		return;
    	}
    	            
		Log.e("position",""+position);
		switch(position) {
			
			case CALLST: resendOutbox(from, to, " AND message LIKE '%CMDCCL%'");
						 break;
			case SALEST: resendOutbox(from, to, " AND message LIKE '%CMDCSI%' OR message LIKE '%CMDCST%' ");
						 break;
			case CUSTOMERST: resendOutbox(from, to, " AND message LIKE '%CMDCUA%' OR message LIKE '%CMDCUU%' ");
							 break;
			case SURVEYT:  resendSurvey(DbUtil.dateTomilli(from)/1000,(DbUtil.dateTomilli(to)+86400000)/1000);
//				resendOutbox(from, to, " AND message LIKE '%CMDCUA%' OR message LIKE '%CMDCUU%' ");
				break;
//			case CALLS:
//						if(netCon()){
//							sync_datas("salescalls", " datetime >= "+DbUtil.dateTomilli(from) +" AND datetime <= "+(86400000+DbUtil.dateTomilli(to)));
//						}
//						break;
//			case SALES:
//						if(netCon()){
//				 			String where_range = " c.date >= "+DbUtil.dateTomilli(from) +" AND c.date <= "+(86400000+DbUtil.dateTomilli(to));
//							sync_datas("callsheets", " date >= "+DbUtil.dateTomilli(from) +" AND date <= "+(86400000+DbUtil.dateTomilli(to)));
////							sync_datas2("so_serve","SELECT s.soid,s.devid,s.cscode,s.ccode,s.qty,s.date,s.status FROM callsheets c "
////									  + "LEFT JOIN so_serve s ON(c.cscode = s.cscode) WHERE "+where_range);
//							sync_datas2("callsheetitems","SELECT s.cscode,s.itemcode,s.pckg,s.pastinvt,s.delivery,s.presentinvt,"
//									  + " s.offtake,s.ico,s.suggested,s.order_qty,s.price FROM callsheets c "
//									  + "LEFT JOIN callsheetitems s ON(c.cscode = s.cscode) WHERE "+where_range);
//						}
//						break;
//			case CUSTOMERS:
//							if(netCon()){
//								sync_datas("customers", " status != 1 ");
//							}
//							break;
//			case SURVEY:
//				if(netCon()){
//					sync_datas("customers", " status != 1 ");
//				}
//				break;
			default:
				//
				break;	
		}
    }
     
    private Boolean netCon(){
    	cd = new ConnectionDetector(getApplicationContext()); 
		  
        // Check if Internet present 
        if (!cd.isOnline()) { 
            // Internet Connection is not present 
            DialogManager.showAlertDialog(ResendActivity.this, 
                    "Internet Connection Error", 
                    "Please connect to working Internet connection", false); 
            // stop executing code by return 
            return false; 
        } 	
        else return true;
    }
    
    private void resendOutbox(String from, String to, String where){
    	ResendTask asyncHttp = new ResendTask(context,null,"","",from,to,where);
		String url=context.getResources().getString(R.string.upload_datas);
		asyncHttp.execute(url,"1");
    }
    
    private void sync_datas(String table_name, String where){
    	
		  
		  SyncDbManager db = new SyncDbManager(context);
		  db.open(); 
		  JSONArray sync_server = new JSONArray();
		  sync_server = db.sync_server_range(table_name,where);	
		  db.close();
		  
		  if(sync_server.length()<=0) return;
			Log.e("not empty","asd");
		    String devId = DbUtil.getSetting(context, Master.DEVID);
			String sourceTable = table_name;
			
			ResendTask asyncHttp = new ResendTask(context,sync_server,devId,sourceTable,"","","");
			String url=context.getResources().getString(R.string.upload_datas);
			asyncHttp.execute(url,"2");
	  }	 
    
    private void sync_datas2(String table_name, String sql){
    	
		  
		  SyncDbManager db = new SyncDbManager(context);
		  db.open();
		  JSONArray sync_server = new JSONArray();
		  sync_server = db.sync_server_select(sql);	
		  db.close();
		  
		  if(sync_server.length()<=0) return;
		  Log.e("not empty","asd");
		    String devId = DbUtil.getSetting(context, Master.DEVID);
			String sourceTable = table_name;
			
			ResendTask asyncHttp = new ResendTask(context,sync_server,devId,sourceTable,"","","");
			String url=context.getResources().getString(R.string.upload_datas);
			asyncHttp.execute(url,"2");
	  }

	  private void resendSurvey(long from,long to){
          CompetitorDbManager2 db = new CompetitorDbManager2(context);
          db.open();
          List<CallSheet> inCodeArr = db.getAll(""," WHERE date BETWEEN "+from +" AND "+to);

          for(CallSheet inCode:inCodeArr){
              List<Inventory> csi = db.getList(inCode.getCscode());
              for (Inventory item : csi) 	{
                  if(item.getStatus()==0 && item.getPrice()>0) {
                      String message = Master.CMD_CMPI7 + " " +
                              devId 					+ ";" +
                              item.getINCode() 		    + ";" +
                              item.getItemCode()		+ ";" +
                              item.getPckg() 			+ ";" +
                              item.getDescription()	    + ";" +
                              item.getPrice()			+ ";" +
                              item.getId()				+ ";" +
							  getStrCategory(item.getSubcategoryid(),2)	+ ";" +
							  getStrCategory(item.getCategoryid(),1);

                      DbUtil.saveMsg(context,DbUtil.getGateway(context), message);

                      db.updateStatus(item.getId(), 1); //1=Transmitted
                  }
              }
          }

          db.close();
	  }
	private String getStrCategory(int id, int opt){
		String cat = "";
		CompetitorDbManager2 db = new CompetitorDbManager2(context);
		db.open();
		cat = db.getCatStr(id,opt);
		db.close();
		return cat;
	}
       
}
	