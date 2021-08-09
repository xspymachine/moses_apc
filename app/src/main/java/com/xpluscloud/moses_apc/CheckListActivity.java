package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.CallChecklistDbManager;
import com.xpluscloud.moses_apc.getset.CustomerCallChecklist;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.DialogManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shirwen on 10/27/2016.
 */

public class CheckListActivity extends Activity {

    String ccode = "";
    String cusName = "";
    String cusAddress = "";
    String devId = "";
    Context context;

    LinearLayout ll;

    LinearLayout[] llx;
    TextView[] tx;
    CheckBox[] cb1;
    CheckBox[] cb2;

    RelativeLayout rl_sccontact;
    EditText etRemarks;
    Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_checklist);

        context = CheckListActivity.this;

        ll = (LinearLayout) findViewById(R.id.mylinear);
        TextView tvCusName = (TextView) findViewById(R.id.tvCName);
        TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
        rl_sccontact = (RelativeLayout) findViewById(R.id.rl_sccontact);
        etRemarks = (EditText) findViewById(R.id.etRemarks);

        Bundle b = getIntent().getExtras();
        ccode = b.getString("customerCode");
        cusName = b.getString("customerName");
        cusAddress = b.getString("customerAddress");
        devId = b.getString("devId");

        tvCusName.setText("Name:		"+cusName);
        tvAddress.setText("Address:	"+cusAddress);

        ImageView ivIcon = (ImageView) findViewById(R.id.bt_scname);
        Drawable dr = context.getResources().getDrawable(R.drawable.assets_clientcall);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        // Scale it to 50 x 50
        Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
        ivIcon.setImageDrawable(d);

        btSubmit = (Button) findViewById(R.id.btSubmit);
        StateListDrawable mStateListDrawable = (StateListDrawable)btSubmit.getBackground();
        DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) mStateListDrawable.getConstantState();
        Drawable[] children = drawableContainerState.getChildren();
        GradientDrawable selectedstateDrawable = (GradientDrawable) children[2];
        selectedstateDrawable.setColorFilter(Color.parseColor("#E88100"), PorterDuff.Mode.MULTIPLY);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallChecklistDbManager db = new CallChecklistDbManager(context);
                db.open();
                db.setCheck(ccode,0,0,3,etRemarks.getText().toString());
                db.close();
                submitData();

            }
        });

        contentSetup();

        final View activityRootView = findViewById(R.id.activityRoot);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(context, 200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    rl_sccontact.setVisibility(View.GONE);
                }
                else rl_sccontact.setVisibility(View.VISIBLE);
            }
        });

    }
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void contentSetup(){
        List<CustomerCallChecklist> checkItem = null;
        CallChecklistDbManager db = new CallChecklistDbManager(context);
        db.open();
        db.addCallChecklist(ccode);
        ArrayList<String> categories = db.getChecklistName("category","");
        db.close();

        for(int i = 0;i<categories.size();i++){
            Log.e("asd",categories.get(i));

            LinearLayout llx = new LinearLayout(context);
            llx.setOrientation(LinearLayout.HORIZONTAL);
            llx.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            llx.setBackgroundColor(Color.BLACK);
            llx.setPadding(5,5,5,5);

            TextView tvCat = new TextView(context);
            tvCat.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.5f));

            tvCat.setTextColor(Color.WHITE);
            tvCat.setTextSize(15);
//            tvCat.setBackgroundColor(Color.BLUE);
            tvCat.setText(categories.get(i));

            TextView tvYes = new TextView(context);
            tvYes.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.06f));
            tvYes.setTextColor(Color.WHITE);
            tvYes.setTextSize(15);
            tvYes.setPadding(2,0,2,0);
            tvYes.setBackgroundColor(Color.GREEN);
            tvYes.setText("YES");

            TextView tvNo = new TextView(context);
            tvNo.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.05f));
            tvNo.setTextColor(Color.WHITE);
            tvNo.setTextSize(15);
            tvNo.setPadding(2,0,2,0);
            tvNo.setBackgroundColor(Color.RED);
            tvNo.setText("NO");

            llx.addView(tvCat);
            llx.addView(tvYes);
            llx.addView(tvNo);
            ll.addView(llx);

            db.open();
            checkItem = db.getCustomerCallCheckList(ccode,categories.get(i));
            db.close();

            for(int j = 0;j<checkItem.size();j++){
                LinearLayout llx3 = new LinearLayout(context);
                llx3.setOrientation(LinearLayout.HORIZONTAL);
                llx3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                llx3.setPadding(5,0,0,0);

                if(j % 2 ==1) llx3.setBackgroundColor(Color.rgb(231, 249, 255));
                else llx3.setBackgroundColor(Color.rgb(195, 240, 255));

                TextView tvCat2 = new TextView(context);
                tvCat2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,0.4f));
                tvCat2.setTextColor(Color.BLACK);
                tvCat2.setTextSize(15);
                tvCat2.setMaxLines(5);
                tvCat2.setSingleLine(false);
                tvCat2.setPadding(0, 0, 0, 14);
                tvCat2.setGravity(Gravity.LEFT);
                tvCat2.setText(checkItem.get(j).getDescription());

                CheckBox cbYes = new CheckBox(context);
                cbYes.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.05f));
//                cbYesSettings(cbYes2,YES,checkItem.get(j).getChkid());
                cbYes.setChecked(checkItem.get(j).getChkYes()==1 ? true : false);

                CheckBox cbNo = new CheckBox(context);
                cbNo.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.05f));
                cbNo.setChecked(checkItem.get(j).getChkNo()==1 ? true : false);
                cbYesSettings(cbYes,cbNo,checkItem.get(j).get_id());

                if(checkItem.get(1).getStatus() == 1){
                    cbYes.setEnabled(false);
                    cbNo.setEnabled(false);
                    etRemarks.setEnabled(false);
                    btSubmit.setEnabled(false);
                    btSubmit.setAlpha(.5f);
                }
                if(checkItem.get(1).getGremarks()!=null){
                    etRemarks.setText(checkItem.get(1).getGremarks());
                }

                llx3.addView(tvCat2);
                llx3.addView(cbYes);
                llx3.addView(cbNo);
                ll.addView(llx3);
            }
        }


    }

//    int YES = 1;
//    int NO = 0;

    private void cbYesSettings(final CheckBox cbYES, final CheckBox cbNO, final int cbId){

        cbYES.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbYES.isChecked()){
                    cbNO.setChecked(false);
                    Log.e("YES",""+cbId);

                    CallChecklistDbManager db = new CallChecklistDbManager(context);
                    db.open();
                    db.setCheck(ccode,cbId,1,1,"");
                    db.close();
                }
                else{
                    CallChecklistDbManager db = new CallChecklistDbManager(context);
                    db.open();
                    db.setCheck(ccode,cbId,0,1,"");
                    db.close();
                }
            }
        });

        cbNO.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbNO.isChecked()){
                    cbYES.setChecked(false);
                    Log.e("NO",""+cbId);

                    CallChecklistDbManager db = new CallChecklistDbManager(context);
                    db.open();
                    db.setCheck(ccode,cbId,1,2,"");
                    db.close();
                }
                else{
                    CallChecklistDbManager db = new CallChecklistDbManager(context);
                    db.open();
                    db.setCheck(ccode,cbId,0,2,"");
                    db.close();
                }
            }
        });
    }

    private void submitData(){
        CallChecklistDbManager db = new CallChecklistDbManager(context);
        db.open();
        List<CustomerCallChecklist> chkItem = db.getCustomerCallCheckListToSubmit(ccode);
        db.setCheck(ccode,0,0,4,"");
        db.close();

        try {

            String message = "CMDCHK " + devId + ";" + ccode + ";";

            for (int i = 0; i < chkItem.size(); i++) {
                String bin = "" + chkItem.get(i).getChkYes() + chkItem.get(i).getChkNo();
                if (bin.equals("00")) message += "" + 0;
                else if (bin.equals("01")) message += "" + 1;
                else if (bin.equals("10")) message += "" + 2;
                else if (bin.equals("11")) message += "" + 3;
            }

            message += ";" + chkItem.get(1).getDate() + " " + chkItem.get(1).getTime();
            message += ";" + chkItem.get(1).getGremarks();

            DbUtil.saveMsg(context, DbUtil.getGateway(context), message);

            Log.e("message", message);

            finish();
        }catch(Exception e){
            ChecklistError();
            return;
        }
    }

    private void ChecklistError() {
        DialogManager.showAlertDialog(CheckListActivity.this,
                "Checklist!",
                "Please download compliance checklist or make changes of the list.", false);
    }
}
