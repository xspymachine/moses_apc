package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.dbase.AccountTypeDbManager;
import com.xpluscloud.mosesshell_davao.dbase.ChecklistDbManager;
import com.xpluscloud.mosesshell_davao.dbase.MerchandisingDbManager;
import com.xpluscloud.mosesshell_davao.dbase.PromoDbManager;
import com.xpluscloud.mosesshell_davao.util.ArrayDef;
import com.xpluscloud.mosesshell_davao.util.DateUtil;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.DialogManager;
import com.xpluscloud.mosesshell_davao.util.Master;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PromoActivity extends Activity {
    public static final String SETTINGS = "MosesSettings";
    public SharedPreferences settings;

    Context context;

    private String sysDate;
    private String setDate;

    private TextView tvDate;
    private EditText et_remarks;
    public Spinner spNpReason;
	/*
	private CheckBox cbRem1;
	private CheckBox cbRem2;
	private CheckBox cbRem3;
	private CheckBox cbRem4;
	private CheckBox cbRem5;
	private CheckBox cbRem6;
	*/

    public Button btSubmit;
    public Button btSubmit2;

    private LinearLayout cbGroup;

    String actName="";
    String devId="";
    String customerCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        context = PromoActivity.this;

        long iDate = System.currentTimeMillis();
        sysDate = DateUtil.strDate(iDate);
        tvDate =  findViewById(R.id.tvDate);
        tvDate.setText(DateUtil.phLongDateTime(iDate));

        btSubmit =  findViewById(R.id.btSubmit);
        cbGroup =  findViewById(R.id.cbGroup);
        et_remarks = findViewById(R.id.et_remarks);
        ImageView nbInfo = findViewById(R.id.nb_info);
        nbInfo.setVisibility(View.INVISIBLE);

        spNpReason = findViewById(R.id.sp_npreason);
        try {
            ArrayAdapter<?> scArrayAdapter = new ArrayAdapter<Object>(this,
                    R.layout.csi_spinner, ArrayDef.REASON_NO_PRODUCTION2);
            spNpReason.setAdapter(scArrayAdapter);

        }catch (Exception e){}

        spNpReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(spNpReason.getSelectedItem().toString().contains("Other")) et_remarks.setVisibility(View.VISIBLE);
                else et_remarks.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        updateChecklist();

        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        setDate = settings.getString("setDate", "");

        try{
            Bundle b 	 = getIntent().getExtras();
            actName	 	 = b.getString("merchandising");
            devId	 	 = b.getString("devid");
            customerCode = b.getString("ccode");

                Log.w("StartActivity","onCreate");
                btSubmit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitMerchandise(v);
                    }
                });

        }catch(Exception e){

//            if (setDate.equalsIgnoreCase(sysDate)) runMain();
            Log.w("StartActivity","onCreate");
//            btSubmit.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    submitMerchandise(v);
//                }
//            });
        }

    }

    private void submitMerchandise(View v) {

        int cbCheckCount=0;
        String Remarks="";
        String sysTime = String.valueOf(System.currentTimeMillis()/1000);
        String checklist="";

        Boolean isCheck = false;
        int status = 0;
        devId = DbUtil.getSetting(context, Master.DEVID);
        String reason = spNpReason.getSelectedItem().toString();
        if(reason.contains("Other") && et_remarks.getText().toString().isEmpty()){
            DialogManager.showAlertDialog(PromoActivity.this,
                    "",
                    "Specify other reason for non-availment.", false);
            return;
        }else if(reason.contains("Select Reason")) reason = "";

        for(int i=0;i<cbGroup.getChildCount();i++){
            View child=cbGroup.getChildAt(i);

            if(child instanceof LinearLayout){

                for(int j = 0; j<((LinearLayout) child).getChildCount(); j++){
                    View child2=((LinearLayout) child).getChildAt(j);

                    if (child2 instanceof CheckBox){
                        isCheck = ((CheckBox) child2).isChecked();
                    }
                    else if(child2 instanceof TextView){
                        if(isCheck){ status = 1;
                        String str_promo = ((TextView) child2).getText().toString();
                        Log.e("PROMOACTIVITY",str_promo);

                            String message =  Master.CMDPMB + " "
                                    + devId  				  + ";"
                                    + customerCode			  + ";"
                                    + str_promo   			  + ";"
                                    + reason                  + ";"
                                    + sysTime				  + ";"
                                    + status;

                            DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
                        }
                        else status = 0;
                    }
                }
            }

        }

        PromoDbManager db = new PromoDbManager(context);
        db.open();
        db.AddCustomerPromo(customerCode,sysTime);
        db.close();

        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();

    }

    ArrayList<String> checklists = new ArrayList<>();
    LinearLayout[] llx ;
    TextView[] tx  ;
    CheckBox[] cbx;
    ImageView[] ivx;

    private void updateChecklist(){
//		Log.e("","check");

        AccountTypeDbManager db = new AccountTypeDbManager(context);
        db.open();
//        checklists = Arrays.asList(db.getOtherData(11));
        Collections.addAll(checklists, db.getOtherData(11));
        db.close();

        llx = new LinearLayout[checklists.size()];
        tx = new TextView[checklists.size()];
        cbx = new CheckBox[checklists.size()];
        ivx = new ImageView[checklists.size()];

        LinearLayout.LayoutParams param1=
                new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        param1.setMargins(15, 0, 0, 0);

        LinearLayout.LayoutParams param2=
                new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        param2.setMargins(10, 0, 0, 0);

        LinearLayout.LayoutParams param4=
                new LinearLayout.LayoutParams
                        (30, 30);
        param2.setMargins(10, 0, 15, 0);

        LinearLayout.LayoutParams param3=
                new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        param3.setMargins(0, 5, 0, 5);

        for(int i=0; i<checklists.size(); i++){
//            llx[i] = new LinearLayout(this);
//            tx[i] = new TextView(this);
//            cbx[i] =new CheckBox(this);
//            ivx[i] =new ImageView(this);
//
//            llx[i].setLayoutParams(param3);
//            llx[i].setVerticalGravity(Gravity.CENTER_VERTICAL);
//
//            tx[i].setLayoutParams(param2);
//            tx[i].setText(checklists.get(i).split(";")[0]);
//            tx[i].setTextSize(20);
//
//            cbx[i].setLayoutParams(param1);
//            cbx[i].setBackgroundResource(R.drawable.custom_checkbox);
//            cbx[i].setTextSize(20);
//
//            ivx[i].setLayoutParams(param4);
//            ivx[i].setImageResource(R.drawable.assets_about);
//            final int finalI = i;
//            ivx[i].setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String str_prm = checklists.get(finalI).split(";")[0];
//                    String str_info = checklists.get(finalI).split(";")[1];
//                    DialogManager.showAlertDialog(PromoActivity.this,
//                            "Promo Info - "+str_prm,
//                            str_info, false);
//                }
//            });
//
//            llx[i].addView(ivx[i]);
//            llx[i].addView(cbx[i]);
//            llx[i].addView(tx[i]);
//
//            cbGroup.addView(llx[i]);

            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.checklist_item, null);
            TextView tvCheck = v.findViewById(R.id.tv_checklist);
            ImageView ivCheck = v.findViewById(R.id.iv_checklist);
            ivCheck.setVisibility(View.VISIBLE);
            tvCheck.setText(checklists.get(i).split(";")[0]);
            final int finalI = i;
            ivCheck.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String str_prm = checklists.get(finalI).split(";")[0];
                    String str_info = checklists.get(finalI).split(";")[1];
                    DialogManager.showAlertDialog(PromoActivity.this,
                            str_prm,
                            str_info, false);
                }
            });

            cbGroup.addView(v);
        }

//        TextView tvRemark = new TextView(this);
//        EditText etRemark = new EditText(this);
//
//        tvRemark.setLayoutParams(param1);
//        tvRemark.setTextSize(14);
//        tvRemark.setText("Remarks");

//        etRemark.setLayoutParams(param3);
//        etRemark.setLines(3);
//        etRemark.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
//        etRemark.setFilters(new InputFilter[] {new InputFilter.LengthFilter(80)});
//        etRemark.setPadding(8, 15, 8, 15);
//        etRemark.setGravity(Gravity.CENTER);
//        etRemark.setBackgroundResource(R.drawable.rounded_border);

//        cbGroup2.addView(tvRemark);
//        cbGroup2.addView(etRemark);

    }

    public void nonbuytype(View v){
        Intent intent = new Intent(context, StoretypeActivity.class);
        Bundle b = new Bundle();
        b.putInt("type", 2);
        intent.putExtras(b);
        startActivity(intent);
    }


}
