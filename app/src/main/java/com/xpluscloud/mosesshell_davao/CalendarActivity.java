package com.xpluscloud.mosesshell_davao;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.dbase.CoveragePlanDbManager;
import com.xpluscloud.mosesshell_davao.dbase.DbManager;
import com.xpluscloud.mosesshell_davao.dbase.PeriodDbManager;
import com.xpluscloud.mosesshell_davao.getset.Customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CalendarActivity extends ListActivity {
	
	public Calendar month;
	public CalendarAdapter adapter;
	public Handler handler;
	public ArrayList<String> items;
	public int Day;
	public int Week;
	Calendar selectedDate;
	Context context;
	public String[] week;
	String currentDate;
	String oldDate;
	View selectedView = null;
	Drawable oldBackground;
	CustomerListAdapter ListAdapter;   
	
	public int temp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_view);
		
		context = CalendarActivity.this;		
				
		
		header_days();
		
		month = Calendar.getInstance();
	    selectedDate = Calendar.getInstance();
	    Day = month.get(Calendar.DAY_OF_WEEK)-1;
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US); // For dbase query
	    String qDate = df.format(month.getTime());
	    Week=get_nWeek(qDate);
	                             
	    /*String strDate = selectedDate.get(Calendar.YEAR)+"-" + 
				 		 (selectedDate.get(Calendar.MONTH)+1)+"-" + 
				 		 selectedDate.get(Calendar.DAY_OF_MONTH);*/	    					
		
	    
	    //week_number();
	    updateList();
	    items = new ArrayList<String>();
	    adapter = new CalendarAdapter(this, month);	 
	    GridView gridview = (GridView) findViewById(R.id.gridview2);
	    gridview.setAdapter(adapter);
	    
	    handler = new Handler();
	    handler.post(calendarUpdater);
	    
	    TextView title  = (TextView) findViewById(R.id.title);
	    title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	    
	    ImageView previous  = (ImageView) findViewById(R.id.previous);
	    previous.setOnClickListener(new OnClickListener() {
			
		@Override
		public void onClick(View v) {
			if(month.get(Calendar.MONTH)== month.getActualMinimum(Calendar.MONTH)) {
					month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),1);
				} else {
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)-1);
				}
				//week_number();
				refreshCalendar();	
				
			}
		});
	    
	    ImageView next  = (ImageView) findViewById(R.id.next);
	    next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(month.get(Calendar.MONTH)== month.getActualMaximum(Calendar.MONTH)) {
					month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),1);
				} else {
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)+1);
				}
				//week_number();
				refreshCalendar();
				
			}
		});
	    
	    gridview.setOnItemClickListener(new OnItemClickListener() {
		    @SuppressLint("NewApi")
			@SuppressWarnings("deprecation")
			//@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		    	TextView date = (TextView)v.findViewById(R.id.date);
		        if(date instanceof TextView && !date.getText().equals("")) {
		        	
		        	int dayNumber = Integer.valueOf(date.getText().toString());
		        	
		        	int selectedMonth=month.get(Calendar.MONTH);
		        	int selectedYear=month.get(Calendar.YEAR);
		        	
		        	if (position<7 && dayNumber > 7) {
		        		if(month.get(Calendar.MONTH)==month.getActualMinimum(Calendar.MONTH)) {
		        			selectedMonth=month.getActualMaximum(Calendar.MONTH);
		        			selectedYear = month.get(Calendar.YEAR)-1;
		        		}
		        		else {		        		
		        			selectedMonth = month.get(Calendar.MONTH) - 1;
		        			selectedYear = month.get(Calendar.YEAR);
		        		}
		        	}
		        	else if (position>20 && dayNumber < 7) {
		        		if(month.get(Calendar.MONTH)==month.getActualMaximum(Calendar.MONTH)) {
		        			selectedMonth=month.getActualMinimum(Calendar.MONTH);
		        			selectedYear = month.get(Calendar.YEAR)+1;
		        		}
		        		else {		        		
		        			selectedMonth = month.get(Calendar.MONTH) + 1;
		        			selectedYear = month.get(Calendar.YEAR);
		        		}		        		
		        	}		        	
		        		        	
		        	selectedDate.set(selectedYear, selectedMonth, dayNumber);		
		        	int month = selectedDate.get(Calendar.MONTH)+1;
		        	currentDate = selectedDate.get(Calendar.YEAR)+"-" +
		        					 String.valueOf(month)+"-" +
		        					 selectedDate.get(Calendar.DAY_OF_MONTH);
		        	
		        	//String strDate = selectedYear + "-" + selectedMonth + "-" + dayNumber;         	
		        	
		        	//String selDate = DateUtil.phLongDate(selectedDate.getTimeInMillis());
		        			        	
						//if(selDate.equals(oldDate)){
						//	try{
						//	createNewItinerary();}catch(Exception e){}
							oldDate="";
						//}
						//else {
						//	oldDate = selDate;
						//}					
		        	
		        	//Toast.makeText(context, strDate, Toast.LENGTH_SHORT).show();
		        	dayClick(currentDate);
		        	
		        	if(selectedView!=null) {
		        		try {
		        			//selectedView.setBackground(oldBackground);
		        			selectedView.setBackgroundDrawable(oldBackground);
		        		} catch(Exception e) {
		        			selectedView.setBackgroundDrawable(oldBackground);
		        		}
		        	}
		        	
		        	selectedView = v;
		        	oldBackground = v.getBackground();
		        	v.setBackgroundResource(R.drawable.cell_background_selected);
		        	
		        }              
		        
		    }
		});	
	    
	    ListView listView = getListView();
        registerForContextMenu(listView);
	}
	
	private int get_nWeek(String date) {
		PeriodDbManager db = new PeriodDbManager(this);
		db.open();		
		Integer nWeek = db.getNweek(date);
		db.close();
		return nWeek;
	}
	
	private void updateList() {
		CoveragePlanDbManager db = new CoveragePlanDbManager(this);
		db.open();
		
		List<Customer> customerList = db.getItinerary(Week,Day);
		ListAdapter = new CustomerListAdapter(this, customerList); 
		setListAdapter(ListAdapter);
		
		db.close();
	}
	
	protected void dayClick(String dateClick) {
		// TODO Auto-generated method stub
		Log.e("dateClick", dateClick);
		Day = selectedDate.get(Calendar.DAY_OF_WEEK)-1;
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US); // For dbase query
	    String qDate = df.format(selectedDate.getTime());
	    Week=get_nWeek(qDate);
	    updateList();
	}

	private void header_days() {
		final String[] hDays = new String[] {
			"Wk#","Sun", "Mon", "Tue", "Wed", "Thu","Fri", "Sat"};
		
		GridView headerGridView = (GridView) findViewById(R.id.header_gridview);
		 
		ArrayAdapter<String> hAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, hDays);
 
		headerGridView.setAdapter(hAdapter);

	}		
	
	/*private void week_number() {
		week = new ArrayList<String>();
		
		String yMonth = ""+android.text.format.DateFormat.format("yyyy-MM", month);
		
		weekDbManager db = new weekDbManager(context);
		db.open();
		db.weekNumber(yMonth);
		db.close();
		//Log.e("yMonth", yMonth);
		//Log.e("yMonth", ""+week);
		GridView headerGridView = (GridView) findViewById(R.id.gridview1);
		 
		ArrayAdapter<String> hAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, week);
 
		headerGridView.setAdapter(hAdapter);
                                                  
	}		*/
	
	public void refreshCalendar(){
		TextView title  = (TextView) findViewById(R.id.title);
		//miscs();
		adapter.refreshDays();
		adapter.notifyDataSetChanged();				
		handler.post(calendarUpdater); // generate some random calendar items				
		
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}
	
	public Runnable calendarUpdater = new Runnable() {
		@Override
		public void run() {
			items.clear();
			
			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};	
	
	/**
	 * 
	 * @author XPlus
	 * Calendar view adapter for calendar
	 *
	 */
	public class CalendarAdapter extends BaseAdapter {
		private Context mContext;
		private java.util.Calendar adapterMonth;
		private Calendar selectedDate;
		private ArrayList<String> items;
		private java.util.Calendar tempCalendar;
		String cDate;
		
		static final int FIRST_DAY_OF_WEEK =0;
		
		public CalendarAdapter(Context c, Calendar monthCalendar) {
			adapterMonth = monthCalendar;
			tempCalendar = monthCalendar;
	    	selectedDate = (Calendar)monthCalendar.clone();
	    	mContext = c;
	    	adapterMonth.set(Calendar.DAY_OF_MONTH, 1);
	        this.items = new ArrayList<String>();
	        refreshDays();
	    }       
		
		public void setItems(ArrayList<String> items) {
	    	for(int i = 0;i != items.size();i++){
	    		if(items.get(i).length()==1) {
	    			items.set(i, "0" + items.get(i));
	    		
	    		}
	    		//Log.e("setItem",""+ items.get(i));
	    	}
	    	this.items = items;
	    }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return days.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;
	    	TextView dayView;
	    	TextView tView1,tView2;
	    	
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	        	LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.calendar_item, null);
	        	             
	        }
	        
	        dayView = (TextView) v.findViewById(R.id.date);
	        tView2  = (TextView) v.findViewById(R.id.textView3);
	        tView2.setText("");
	        
	        // mark current day as focused
	    	/*if(month.get(Calendar.YEAR)== selectedDate.get(Calendar.YEAR) 
	    			&& month.get(Calendar.MONTH)== selectedDate.get(Calendar.MONTH) 
	    			&& days[position].equals(""+selectedDate.get(Calendar.DAY_OF_MONTH))) {
	    		v.setBackgroundResource(R.drawable.cell_background_focused);
	    	}
	    	*/
	        
	        
	        int nDay = selectedDate.get(Calendar.DAY_OF_MONTH);
	    	int cYear = (adapterMonth.get(Calendar.YEAR));
	    	String monthStr = ""+(adapterMonth.get(Calendar.MONTH)+1);
	        String date = days[position];
	        
	       
	        if ((days[position].equals(""+nDay)) 
	        	&& ((position<20 && nDay<20) || (position>20 && nDay>21) || (nDay>=14 && nDay<=21)) && mod(position,8) > 0) {
	    		v.setBackgroundResource(R.drawable.cell_background_focused);   
	    		//calViewer(cDate,v);                          
	    		//Log.e("current", "date"); 
	    		 //tView1.setText(""+totalList);
	    		 //tView2.setText("Customers");
	    	}
	    	else {    		
	    		// Set Background for non-current month days
	            int dayNumber = Integer.valueOf(days[position]);
	        
	            if ((position<7 && position >0 && dayNumber > 7) || (position>20 && dayNumber < 7)) {
	            	v.setBackgroundResource(R.drawable.cell_background_non_month);            		
	            }            
		    	else {
		    		v.setBackgroundResource(R.drawable.list_item_background);
		    		//calViewer(cDate,v); 	
		    		 //tView1.setText(""+totalList);
		    		 //tView2.setText("Customers");
		    	}
	    	}                        
	        
	        String yMonth = ""+android.text.format.DateFormat.format("yyyy-MM", month);
	        
	        Log.e("CalendarActivity", mod(position,8)+"--"+yMonth+"--"+temp);
	        
	        weekDbManager db = new weekDbManager(context);
			db.open();
			int row = db.weekNumber(yMonth);
			db.close();	 
			
			
			
			if(mod(position,8) > 0 ) {
				dayView.setText(days[position]);
			}
			if (mod(position,8)==1) {
	        	dayView.setTextColor(Color.RED);
	        	temp++;
	        }
			
			else if(mod(position,8)==0 && temp < row) {				
				tView2.setText(week[temp]);
			}
			else if(temp == row) temp = 0;
			
	        
	        // create date string for comparison    	
	        if(date.length()==1) {
	    		date = "0"+date;
	    	}    	
	    
	    	if(monthStr.length()==1) {
	    		monthStr = "0"+monthStr;
	    	}    	    	  	
	    	    	    	
	    	 
	        // show icon if date is not empty and it exists in the items array
	    	TextView iw = (TextView)v.findViewById(R.id.date_icon);
	        if(date.length()>0 && items!=null && items.contains(date)) {  
	        	iw.setText("test");
	        	iw.setVisibility(View.VISIBLE);
	        }
	        else {
	        	iw.setVisibility(View.INVISIBLE);
	        }
	        return v;
		}
		
		public void refreshDays()
	    {
	    	// clear items
	    	items.clear();
	    	int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
	        int firstDay = (int)month.get(Calendar.DAY_OF_WEEK);
	        
	        // figure size of the array
	        if(firstDay==1){
	        	//days = new String[lastDay+(FIRST_DAY_OF_WEEK*6)];
	        	if (lastDay==28) days = new String[32];
	        	else days = new String[40];
	        }
	        else {
	        	//days = new String[lastDay+firstDay-(FIRST_DAY_OF_WEEK+1)];
	        	if ((firstDay+lastDay-1)>35) days = new String[48];
	        	else days = new String[40];
	        }
	        
	        int j=FIRST_DAY_OF_WEEK;
	        int dayNumber;
	        // populate empty days before first real day
	        if(firstDay>1) {
	        	
	        	Calendar prevMonth = (Calendar) month.clone();
		        
		        if(prevMonth.get(Calendar.MONTH)== prevMonth.getActualMinimum(Calendar.MONTH)) {
		        	prevMonth.set((prevMonth.get(Calendar.YEAR)-1),prevMonth.getActualMaximum(Calendar.MONTH),1);
				} else {
					prevMonth.set(Calendar.MONTH,prevMonth.get(Calendar.MONTH)-1);
				}
	        
		        int prevMonthLastDay =  prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
		        
		        int firstEntry = prevMonthLastDay - firstDay + 2;	        
		        dayNumber=firstEntry;	                	
		        for(j=0;j<firstDay-FIRST_DAY_OF_WEEK;j++) {
		        	days[j] = ""+dayNumber;
		        	if(mod(j,8) != 0 && dayNumber < prevMonthLastDay){
		        		dayNumber++;
		        	}
		        	Log.e("daynumber", j+"--"+dayNumber);
		        }
	        }
		    else {
		    	for(j=0;j<FIRST_DAY_OF_WEEK*6;j++) {
		        	days[j] = "";
		        }
		    	j=FIRST_DAY_OF_WEEK*6+2; // sunday => 1, monday => 7
		    }
	        
	        // populate days
	        dayNumber = 1;
	        for(int i=j;i<days.length;i++) {
		        	days[i] = ""+dayNumber;
		        	if(mod(i,8) != 0){
		        		dayNumber++;
		        	}
		        	if (dayNumber>lastDay) dayNumber = 1;
	        }
	        
	        //Fill in remaining grid
	        
	    }
		private int mod(int x, int y){
	        int result = x % y;
	        return result < 0? result + y : result;
	    }
		public String[] days;
	}
	
	/**
	 * 
	 * @author XPlus
	 * Database manager to get the weekNumber for the whole selected month 
	 */
	
	private class weekDbManager extends DbManager {

		public weekDbManager(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		/*public int totalWeek(String yMonth){
			String sql = "SELECT *from periods where week_start LIKE '%"+yMonth+"%'";
			int count;
			
			Cursor c = db.rawQuery(sql, null);
			
			if(c != null) count = c.getCount();
			else count = 0;
			
			return count;
		}*/
		
		public int weekNumber(String yMonth){
			String sql = "SELECT * from periods where week_start LIKE '%"+yMonth+"%' OR week_end LIKE '%"+yMonth+"%'";
			int row = 0;
			int i = 0;
			
			Cursor c = db.rawQuery(sql, null);
			
			if(c != null){
				row = c.getCount();
				week = new String[c.getCount()];
				c.moveToFirst();
				while(c.isAfterLast() == false){
					//week.add("\nwk"+c.getInt(2)+"\n\n");
					week[i] = "wk "+ c.getInt(2);
					i++;
					c.moveToNext();
				}
			}
			c.close();
			return row;
		}
		
		/*private int getTotalCustomers(Integer week,Integer day) {
			int totalList = 0;
			
			String where="WHERE cp.week_schedule LIKE '%" + week + "%' AND " +
					"cp.day_schedule LIKE '%" + day + "%'";
			
			//String where="WHERE " + week + " LIKE cp.week_schedule ||'%' AND " + day + " LIKE  cp.day_schedule || '%'";
			
			String sql="SELECT cus._id,ccode,name,address,cus.cplan_code " +
					"FROM customers cus " +
					"INNER JOIN coverageplans cp " +
						"ON cus.cplan_code=cp.cplan_code " 
					+ where ;					
					
			Cursor c = db.rawQuery(sql, null);	
			
			if (c != null) totalList = c.getCount();
				
			c.close();			
			//Log.e("getTotalCustomers",""+totalList);
			return totalList;
		}*/
		
	}	
	/**
	 * 
	 * @author XPlus
	 * Customer list adapter to show the list of customers when specific day is click
	 */
	
	private static class CustomerListAdapter extends BaseAdapter implements Filterable {
		private LayoutInflater inflater;
		private List<Customer> originalList;
		private List<Customer> filteredList;
		
		public CustomerListAdapter(Context context, List<Customer> list) {
			inflater = LayoutInflater.from(context);
			originalList = list;
			filteredList = list;			
		}
		
		public void setValue(List<Customer> list) {
		     this.filteredList = list;
		     this.notifyDataSetChanged();
		}

		
		@Override
		public int getCount() {
			return filteredList.size();
		}
		
		@Override
		public Object getItem(int position) {
			return filteredList.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return filteredList.get(position).getId();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			if(convertView == null) {
				convertView = inflater.inflate(R.layout.list_customer, null);
				
				holder = new ViewHolder();
				holder.customerName = (TextView) convertView.findViewById(R.id.customer_name);
			    holder.customerAddress = (TextView) convertView.findViewById(R.id.customer_address);
			    holder.contactNumber = (TextView) convertView.findViewById(R.id.contactNumber);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.customerName.setText(filteredList.get(position).getName() +" (" + filteredList.get(position).getCustomerCode() + ")");
			holder.customerAddress.setText(filteredList.get(position).getAddress() + " " + filteredList.get(position).getCity());
			holder.contactNumber.setText(filteredList.get(position).getContactNumber());
			
			return convertView;
		}
		
		private static class ViewHolder {
			public TextView customerName;
			public TextView customerAddress;
			public TextView contactNumber;
		}
				
		
		@Override
	    public Filter getFilter() {
	        return new Filter() {
	            @SuppressWarnings("unchecked")
	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	            	filteredList = (List<Customer>) results.values;
	            	CustomerListAdapter.this.notifyDataSetChanged();
	            }

	            
				@Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	            	FilterResults results = new FilterResults();
            		// We implement here the filter logic 
	            	if (constraint == null || constraint.length() == 0) { 
	            		// No filter implemented we return all the list 
		            	results.values = originalList; 
		            	results.count = originalList.size();
		            	return results;

	            	} 
	            	else { 
		            	// We perform filtering operation 
		            	List<Customer> ncustomerList = new ArrayList<Customer>();
		            	for (Customer c : originalList) { 
		            		if (c.getName().toUpperCase().contains(constraint.toString().toUpperCase()) 
		            				|| c.getCustomerCode().toUpperCase().contains(constraint.toString().toUpperCase()) ) 
		            			ncustomerList.add(c); 
		            	} 
		            	results.values = ncustomerList; 
		            	results.count = ncustomerList.size(); 
	            	} 
	            	return results; 
            	}
            
	        };
	    }
	}

}
