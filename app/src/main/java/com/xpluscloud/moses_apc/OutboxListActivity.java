package com.xpluscloud.moses_apc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.xpluscloud.moses_apc.dbase.OutboxDbManager;
import com.xpluscloud.moses_apc.getset.Outbox;

import java.util.List;

public class OutboxListActivity extends AppCompatActivity {
	final String TAG = "OutboxListActivity";
	
	Context context;
	
	// Search EditText
    EditText inputSearch;
    OutboxListAdapter adapter;
    Outbox selectedRow;

	ListView outboxListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outbox_search_list);
		context = OutboxListActivity.this;

		outboxListView = (ListView) findViewById(R.id.outbox_listview);
		
		setTitle(getResources().getString(R.string.app_name) + " " 	+	
//				 getResources().getString(R.string.version )
				 " - " + "Outbox") ; 
		
		initListView() ;
		
		
		inputSearch = (EditText) findViewById(R.id.inputSearch);
	        
        inputSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1,
                                      int arg2, int arg3) {
				OutboxListActivity.this.adapter.getFilter().filter(arg0); 
				
			}         
           
        });

        OutboxDbManager db = new OutboxDbManager(context);
        db.open();
        int count = db.getAllQueue().size();
        db.close();
		CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
				.coordinatorLayout);
		Snackbar snackbar = Snackbar
				.make(coordinatorLayout, count+" - Pending Messages", Snackbar.LENGTH_INDEFINITE);
		View sbView = snackbar.getView();
		sbView.setBackgroundColor(Color.parseColor("#B3000000"));
		snackbar.show();

//        ListView listView = getListView();
        registerForContextMenu(outboxListView);
	}
	
	public void initListView() {		
		OutboxDbManager db = new OutboxDbManager(this);
		db.open();
		
		List<Outbox> OutboxList = db.getDispMessages();
		adapter = new OutboxListAdapter(this, OutboxList);
		outboxListView.setAdapter(adapter);
		
		db.close();	
		
	}

	public void refreshListView(Context context) {
		OutboxDbManager db = new OutboxDbManager(context);
		db.open();			
		List<Outbox> list = db.getDispMessages();
		adapter.setValue(list);	
		db.close();
		
	}
	
	
	
	 @Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		    	    
		    selectedRow = (Outbox) outboxListView.getAdapter().getItem(info.position);
		    
		   menu.setHeaderIcon(R.drawable.modify);
		   menu.setHeaderTitle("Outbox ");
		    
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.context_menu, menu);
		    
		}
		
		@Override
	    public boolean onContextItemSelected(MenuItem item) {
			
	        switch (item.getItemId()) {            
	            case R.id.action_resend:
	            	Integer outid = selectedRow.getId();
	            	OutboxDbManager db = new OutboxDbManager(context);
					db.open();
					db.updateStatus(outid,0);
					db.resetPriority(outid);
					db.close();
					refreshListView(context);
	        		return true;
	            case R.id.action_settings: 	            	
	                return true;
	        }
	        return false;
	    }
	
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.resend_menu, menu);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    switch(item.getItemId()) {
			    case R.id.resend:
			    	Intent i = new Intent(this, ResendActivity.class);
					startActivity(i);
			        break;			        			    	
			    }
			return true;
		}
	
}
