package com.xpluscloud.mosesshell_davao;

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

import com.xpluscloud.mosesshell_davao.dbase.CallSheetDbManager;
import com.xpluscloud.mosesshell_davao.dbase.CallSheetItemDbManager;
import com.xpluscloud.mosesshell_davao.dbase.CompetitorDbManager2;
import com.xpluscloud.mosesshell_davao.getset.CallSheet;
import com.xpluscloud.mosesshell_davao.util.DialogManager;

import java.util.List;

public class CompetitorActivityList2 extends ListActivity {
    private static final int DELETE_CALLSHEET = 1;

    final String TAG = "CallSheetListActivity";

    Context context;

    // Search EditText
    EditText inputSearch;
    CallSheetListAdapter adapter;
    CallSheet selectedRow;

    String cCode;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        context = CompetitorActivityList2.this;

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
                CompetitorActivityList2.this.adapter.getFilter().filter(arg0);

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
        CompetitorDbManager2 db = new CompetitorDbManager2(this);
        db.open();

        List<CallSheet> oList = db.getAll(cCode,"");
        adapter = new CallSheetListAdapter(this, oList);
        setListAdapter(adapter);

        db.close();

    }

    public void refreshListView(Context context) {
        CallSheetDbManager db = new CallSheetDbManager(context);
        db.open();
        List<CallSheet> list = db.getAll(cCode);
        adapter.setValue(list);
        db.close();
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        selectedRow = (CallSheet) getListAdapter().getItem(info.position);

        menu.setHeaderIcon(R.drawable.modify);
        menu.setHeaderTitle("Callsheets ");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_callsheet_option, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit:
                final ProgressDialog progress = ProgressDialog.show(this, "Loading",
                        "Please wait...", true);

                new Thread(new Runnable() {
                    @Override
                    public void run(){

                        Intent intent = new Intent(context, CallSheetActivity2.class);
                        Bundle b = new Bundle();
                        b.putString("csCode", selectedRow.getCscode());
                        intent.putExtras(b);
                        startActivity(intent);
                        // do the thing that takes a long time

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                progress.dismiss();
                            }
                        });
                    }
                }).start();
                return true;
            case R.id.action_delete:
                if(selectedRow.getStatus()!=0) {
                    DialogManager.showAlertDialog(context,"Locked Call Sheet!",
                            "This callsheet had already been transmitted to the central server and cannot be modified!", true);
                    return false;
                }

                String title = "Delete Call Sheet";
                String message = "Are you sure you want delete the entire callsheet?";
                confirmDialog(title,  message, DELETE_CALLSHEET,0,selectedRow.getCscode());
                return true;
        }
        return false;
    }

    private void confirmedDeleteCallSheet(String csCode) {
        CallSheetItemDbManager db = new CallSheetItemDbManager(this);
        db.open();
        db.deleteCallSheet(csCode);
        db.close();

        CallSheetDbManager csdb = new CallSheetDbManager(this);
        csdb.open();
        csdb.deleteCallSheet(csCode);
        csdb.close();

        initListView();
    }

    public void confirmDialog(String title, String message, Integer option, Integer id, String cscode) {

        final Integer Option = option;
        //final String Title = title;
        //final Integer Id = id;
        final String csCode=cscode;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.delete32);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch(Option) {

                    case DELETE_CALLSHEET:
                        confirmedDeleteCallSheet(csCode);
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
