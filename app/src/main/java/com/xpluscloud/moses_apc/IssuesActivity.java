package com.xpluscloud.moses_apc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.xpluscloud.moses_apc.dbase.IssueDbManager;
import com.xpluscloud.moses_apc.getset.Issue;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.StringUtil;

/**
 * Created by Shirwen on 11/9/2016.
 */

public class IssuesActivity extends AppCompatActivity {

    EditText etIssue;
    Button btOpen;
    Context context;

    String ccode;
    String cusName;
    String cusAddress;
    String devId;

    String issue = "";
    String code_issue = "";

    Issue issueInfo;

    FloatingActionButton mFab;
    TextView tvTicketNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        context = IssuesActivity.this;

        Bundle b = getIntent().getExtras();
        ccode = b.getString("customerCode");
        cusName = b.getString("customerName");
        cusAddress = b.getString("customerAddress");
        devId = b.getString("devId");

        TextView tvCustomer = (TextView) findViewById(R.id.tvCName);
        TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvCustomer.setText(cusName);
        tvAddress.setText(cusAddress);

        ImageView ivIcon = (ImageView) findViewById(R.id.bt_scname);
//        Drawable d = new BitmapDrawable(context.getResources(), LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.assets_issue, 60, 60));
//        ivIcon.setImageDrawable(d);

        mFab = (FloatingActionButton) findViewById(R.id.show);
        tvTicketNo = (TextView) findViewById(R.id.tnumber);

        etIssue = (EditText) findViewById(R.id.etIssue);
        btOpen  = (Button) findViewById(R.id.btOpen);
        btOpen.setText("Open Issue");
        StateListDrawable mStateListDrawable = (StateListDrawable)btOpen.getBackground();
        DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) mStateListDrawable.getConstantState();
        Drawable[] children = drawableContainerState.getChildren();
        GradientDrawable selectedstateDrawable = (GradientDrawable) children[2];
        selectedstateDrawable.setColorFilter(Color.parseColor("#199F34"), PorterDuff.Mode.MULTIPLY);

//        if(!getOpenIssue().isEmpty()){
//            etIssue.setText(issue);
//            btOpen.setText("Close Issue");
//            mStateListDrawable = (StateListDrawable)btOpen.getBackground();
//            drawableContainerState = (DrawableContainer.DrawableContainerState) mStateListDrawable.getConstantState();
//            children = drawableContainerState.getChildren();
//            selectedstateDrawable = (GradientDrawable) children[2];
//            selectedstateDrawable.setColorFilter(Color.parseColor("#FDAF2C"), PorterDuff.Mode.MULTIPLY);
//        }

        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issue = etIssue.getText().toString();
                if(!issue.isEmpty()){
                    if(btOpen.getText().toString().contains("Open")) submitIssue(1);
                    else submitIssue(2);
                }
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View customV = inflater.inflate(R.layout.dialog_layout, null);
                TextView etIssue = (TextView) customV.findViewById(R.id.txtdialog);

                etIssue.setText(issue);

                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(customV))
                        .setPadding(50, 50, 50, 50)
                        .setMargin(20,20,20,20)
                        .create();
                dialog.show();
            }
        });

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Customer issue activity");
        alertDialog.setMessage("Do you want to create a new issue?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IssueDbManager db = new IssueDbManager(context);
                db.open();
                int lastId = db.getLastId();
                db.close();
                tvTicketNo.setText(StringUtil.get_Code(ccode,lastId,context));
                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!getOpenIssue("").isEmpty()){
                    history();
                }
                else finish();
            }
        });
        alertDialog.setNeutralButton("Quit",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        alertDialog.show();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Drawable d = new BitmapDrawable(context.getResources(), LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.assets_issue, 20, 20));
//
//        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setCancelable(false);
//        alertDialog.setTitle("Customer issue activity");
//        alertDialog.setMessage("Do you want to create a new issue?");
//        alertDialog.setIcon(d);
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog,int which) {
//                dialog.dismiss();
//            }
//        });
//
//        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(!getOpenIssue("").isEmpty()){
//                    history();
//                }
//                else finish();
//            }
//        });
//        alertDialog.show();
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.context_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.history:
//                history();
//                break;
//        }
//        return true;
//    }

    private void history(){
        Intent i = new Intent(this, IssuesHistory.class);
        Bundle b = new Bundle();
        b.putString("ccode", ccode);
        i.putExtras(b);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    String code = data.getStringExtra("code_issue");
                    getOpenIssue(code);
                    etIssue.setText("");
                    etIssue.setHint("Type here remarks for solving the issue, tap the floating button for information about the issue");
                    tvTicketNo.setText(code);
                    btOpen.setText("Close Issue");
                    StateListDrawable mStateListDrawable = (StateListDrawable)btOpen.getBackground();
                    DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) mStateListDrawable.getConstantState();
                    Drawable[] children = drawableContainerState.getChildren();
                    GradientDrawable selectedstateDrawable = (GradientDrawable) children[2];
                    selectedstateDrawable.setColorFilter(Color.parseColor("#FDAF2C"), PorterDuff.Mode.MULTIPLY);

                    mFab.setVisibility(View.VISIBLE);
                }
                if (resultCode == RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;

        }
    }

    private void submitIssue(int swOpt){
        btOpen.setEnabled(false);
        String ticketNo = tvTicketNo.getText().toString();

        IssueDbManager db = new IssueDbManager(context);

        Issue i = new Issue();
        i.setCcode(ccode);
        i.setIssue(issue);
        i.setCode_issue(ticketNo);

        db.open();
        switch(swOpt){
            case 1: code_issue = ticketNo;
                    db.createIssue(i);
                    break;

            case 2: i.setCode_issue(code_issue);
                    db.updateCloseIssue(i);
                    break;
        }
        db.close();

        submitToServer();
        finish();
    }

    private String getOpenIssue(String code_issue){
        IssueDbManager db = new IssueDbManager(context);
        db.open();
        issueInfo = db.getOpenIssue(ccode,code_issue);
        db.close();

        try {
            issue = issueInfo.getIssue();
            this.code_issue = issueInfo.getCode_issue();
        }catch (Exception e){}

        return issue;
    }

    private void submitToServer(){
        Log.e("codeissue","issue2"+code_issue);
        IssueDbManager db = new IssueDbManager(context);
        db.open();
        issueInfo = db.getCustomerIssue(code_issue);
        db.close();

        String open  = issueInfo.getDate_open();
        String close = issueInfo.getDate_close();
        if(!open.isEmpty()) open = ""+(Long.parseLong(open)/1000);
        if(!close.isEmpty()) close = ""+(Long.parseLong(close)/1000);

        String msg = "CMDISS2 " + devId + ";" + ccode + ";"
                     + issueInfo.getCode_issue()    + ";"
                     + open                         + ";"
                     + issueInfo.getIssue()         + ";"
                     + close;

        DbUtil.saveMsg(context,DbUtil.getGateway(context),msg);

    }
}
