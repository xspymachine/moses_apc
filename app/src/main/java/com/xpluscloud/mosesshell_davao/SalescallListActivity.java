package com.xpluscloud.mosesshell_davao;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.xpluscloud.mosesshell_davao.dbase.SalescallDbManager;
import com.xpluscloud.mosesshell_davao.getset.MyList;
import com.xpluscloud.mosesshell_davao.util.DialogManager;

import java.util.List;

public class SalescallListActivity extends ListActivity {
	final String TAG = "SalescallListActivity";
	
	Context context;
	
	// Search EditText
    EditText inputSearch;
    ListAdapter adapter;
    MyList selectedRow;
    
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_list);		
		context = SalescallListActivity.this;
		
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
				SalescallListActivity.this.adapter.getFilter().filter(arg0); 				
			}         
           
        });	
        
        ListView listView = getListView();
        registerForContextMenu(listView);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
	    	finish();
	        return true; 
	    } 
	    return super.onKeyDown(keyCode, event); 
	} 
	
	@Override
    protected void onStop(){
       super.onStop();
    }
	
	public void initListView() {		
		SalescallDbManager db = new SalescallDbManager(this);
		db.open();		
		
		List<MyList> list = db.getAll();
		adapter = new ListAdapter(this, list); 
		setListAdapter(adapter);
		
		db.close();	
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
	    	    
	    selectedRow = (MyList) getListAdapter().getItem(info.position);	   
	    
	    String title = selectedRow.getCustomerName() +" (" + selectedRow.getCustomerCode() + ")";
	    String message = selectedRow.getDateTime() +": " + selectedRow.getTransaction();
	    DialogManager.showAlertDialog(context, title, message, true);
	    
	}

	
}
