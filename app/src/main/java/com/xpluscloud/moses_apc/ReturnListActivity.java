package com.xpluscloud.moses_apc;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.xpluscloud.moses_apc.dbase.ReturnDbManager;
import com.xpluscloud.moses_apc.getset.Pr;
import com.xpluscloud.moses_apc.util.DialogManager;

import java.util.List;

public class ReturnListActivity extends ListActivity {
	private static final int DELETE_PRODUCT_RETURN = 1;
	private static final int DELETE_ALL = 2;

	final String TAG = "ReturnListActivity";
	
	Context context;
	
	// Search EditText
    EditText inputSearch;
    ReturnListAdapter adapter;
    Pr selectedRow;
    
    String cCode;
    
    ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_list);		
		context = ReturnListActivity.this;
		
		Bundle b = getIntent().getExtras();
	   
	    cCode 	= b.getString("ccode");	   
		
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
				ReturnListActivity.this.adapter.getFilter().filter(arg0); 
				
			}   
        });
        
        ListView listView = getListView();
        registerForContextMenu(listView);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    initListView();
	    
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		initListView();
	}
	
	
	public void initListView() {		
		ReturnDbManager db = new ReturnDbManager(this);
		db.open();
		
		List<Pr> oList = db.getPrs(cCode);
		adapter = new ReturnListAdapter(this, oList); 
		setListAdapter(adapter);
		
		db.close();	
		
	}

	public void refreshListView(Context context) {
		ReturnDbManager db = new ReturnDbManager(context);
		db.open();			
		List<Pr> list = db.getPrs(cCode);
		adapter.setValue(list);	
		db.close();
	}
	
	
	
	 @Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		    	    
		    selectedRow = (Pr) getListAdapter().getItem(info.position);	
		    
		    menu.setHeaderIcon(R.drawable.modify);
		    menu.setHeaderTitle("Product Returns ");
		    
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.context_menu_return_option, menu);
		    
		}
		
		@Override
	    public boolean onContextItemSelected(MenuItem item) {
			String title=null;
			String message=null;
			
	        switch (item.getItemId()) {            
	            case R.id.action_edit:	            	
	            	final ProgressDialog progress = ProgressDialog.show(this, "Loading",
	            			  "Please wait...", true);

	            			new Thread(new Runnable() {
	            			  @Override
	            			  public void run(){

				            	Intent intent = new Intent(context, ReturnActivity.class);
				        		Bundle b = new Bundle();
				        		b.putString("prCode", selectedRow.getPrcode());
				        		intent.putExtras(b); 
				        		startActivity(intent);
				        		// do the thing that takes a long time
			
			    			    runOnUiThread(new Runnable() {
			    			      @Override
			    			      public void run() {
			    			        progress.dismiss();
			    			      }
			    			    });
			    			  }
			    			}).start();
	            			
	        		return true;
	            case R.id.action_delete: 	            	
	            	if(selectedRow.getStatus()!=0) {
	            		DialogManager.showAlertDialog(context,"Locked Product Return!",
	        					"This product return had already been transmitted to the central server and cannot be modified!", true);	
	            		return false;
	            	}
	            	
	            	 title = "Delete Product Return";
	            	 message = "Are you sure you want delete the entire product return?";
	            	confirmDialog(title,  message, DELETE_PRODUCT_RETURN,0,selectedRow.getPrcode());
	                return true;
	            case R.id.action_delete_all: 
	            	 title = "Delete All Product Returns";
	            	 message = "Are you sure you want delete all product returns for this customer?";
	            	confirmDialog(title,  message, DELETE_ALL,0,null);
	                return true;    
	        }
	        return false;
	    }
		
		private void confirmedDeleteReturn(String prCode) {
			
			
			ReturnDbManager csdb = new ReturnDbManager(this);
			csdb.open();		
			csdb.deleteReturn(prCode);
			csdb.close();
			
			initListView();
		}
		
private void confirmedDeleteAll() {
			
			
			ReturnDbManager csdb = new ReturnDbManager(this);
			csdb.open();		
			csdb.deleteAllReturn(cCode);
			csdb.close();
			
			initListView();
		}
		
		public void confirmDialog(String title, String message, Integer option, Integer id, String code) {
			   
			   final Integer Option = option;
			   //final String Title = title;
			   //final Integer Id = id;
			   final String Code=code;
			   
			   AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			   
		        // Setting Dialog Title
		        alertDialog.setTitle(title);
		 
		        // Setting Dialog Message
		        alertDialog.setMessage(message);
		 
		        // Setting Icon to Dialog
		        alertDialog.setIcon(R.drawable.delete48);
		 
		        // Setting Positive "Yes" Button
		        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		            @Override
					public void onClick(DialogInterface dialog, int which) {
		            	
		            	switch(Option) {            	   		
		        	   		
		        	   		case DELETE_PRODUCT_RETURN:   
		        	   			confirmedDeleteReturn(Code);
		        	   			break;	
		        	   		case DELETE_ALL:   
		        	   			confirmedDeleteAll();
		        	   			break;		
			        	  }	            
		            	//Toast.makeText(getApplicationContext(), Title + " confirmed!" , Toast.LENGTH_SHORT).show();
		            }
		        });
		 
		        // Setting Negative "NO" Button
		        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		            @Override
					public void onClick(DialogInterface dialog, int which) {
		            
		            dialog.cancel();
		            }
		        });
		 
		        // Showing Alert Message
		        alertDialog.show();
			   
		   }
	
	
}
