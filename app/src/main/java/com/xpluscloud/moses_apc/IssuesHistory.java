package com.xpluscloud.moses_apc;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.xpluscloud.moses_apc.dbase.IssueDbManager;
import com.xpluscloud.moses_apc.getset.MyList;
import com.xpluscloud.moses_apc.util.DialogManager;

import java.util.List;

public class IssuesHistory extends ListActivity {
    final String TAG = "TimeInOutListActivity";

    Context context;

    // Search EditText
    EditText inputSearch;
    ListAdapter adapter;
    MyList selectedRow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        context = IssuesHistory.this;


        Bundle b = getIntent().getExtras();
        String ccode = b.getString("ccode");

        initListView(ccode) ;


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
                IssuesHistory.this.adapter.getFilter().filter(arg0);
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

    public void initListView(String ccode) {
        IssueDbManager db = new IssueDbManager(this);
        db.open();

        List<MyList> list = db.getAllOpenIssue(ccode);
        adapter = new ListAdapter(this, list);
        setListAdapter(adapter);

        db.close();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        selectedRow = (MyList) getListAdapter().getItem(info.position);
        Log.e("length","asdasdasdasd"+selectedRow.getDateTime().length());
        if(selectedRow.getDateTime().length()>30){
            DialogManager.showAlertDialog(IssuesHistory.this,
                    "Closed Issue!",
                    "This issue is already closed. Please select unclosed issues.", false);
            return;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("code_issue", selectedRow.getTransCode());

        setResult(RESULT_OK, returnIntent);
        finish();

    }


}
