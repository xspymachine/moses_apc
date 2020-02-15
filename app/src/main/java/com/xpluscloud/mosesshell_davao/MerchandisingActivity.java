package com.xpluscloud.mosesshell_davao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.dbase.MerchandisingDbManager;
import com.xpluscloud.mosesshell_davao.dbase.RetailDataDbManager;
import com.xpluscloud.mosesshell_davao.getset.Retail;
import com.xpluscloud.mosesshell_davao.util.DbUtil;
import com.xpluscloud.mosesshell_davao.util.LayoutUtil;
import com.xpluscloud.mosesshell_davao.util.Master;

import java.util.ArrayList;
import java.util.List;

public class MerchandisingActivity extends FragmentActivity implements RetailFragA.onSomeEventListener,RetailFragB.onSomeEventListener,
        RetailFragC.onSomeEventListener,RetailFragD.onSomeEventListener{

    Context context;

    String ccode ="";
    String cusName = "";
    String cusAddress = "";
    String devId = "";

    EditText etRemarks;

    String selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.viewpager);

        context = MerchandisingActivity.this;

        TextView tvCusName = findViewById(R.id.tvCName);
        TextView tvAddress = findViewById(R.id.tvAddress);
        etRemarks = findViewById(R.id.etRemarks_pg);

        Bundle b = getIntent().getExtras();
        ccode = b.getString("customerCode");
//        cusName = b.getString("customerName");
//        cusAddress = b.getString("customerAddress");
        devId = b.getString("devId");
//		selected = b.getString("selected");

//        tvCusName.setText(cusName);
//        tvAddress.setText(cusAddress);

//        ImageView ivIcon = findViewById(R.id.bt_scname);
//		Drawable dr = context.getResources().getDrawable(R.drawable.assets_clientcall);
//		Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
//		// Scale it to 50 x 50
//		Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
//        Drawable d = new BitmapDrawable(context.getResources(), LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.assets_clientcall, 60, 60));
//        ivIcon.setImageDrawable(d);

//        RetailDataDbManager db = new RetailDataDbManager(context);
//        db.open();
//        db.addAllItems(ccode, devId,selected);
//        etRemarks.setText(db.getRemarks(ccode));
//        db.close();

        ViewPager pager = findViewById(R.id.viewPager);
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));


        Button btSubmit = findViewById(R.id.btSubmit);
        LayoutUtil.changeChildDrawable(btSubmit);
//        final RelativeLayout layout = findViewById(R.id.rl_sccontact);
//        final View activityRootView = findViewById(R.id.activityRetailRoot);
//        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
//                if (heightDiff > dpToPx(context, 200)) { // if more than 200 dp, it's probably a keyboard...
//                    // ... do something here
//
//                    layout.setVisibility(View.GONE);
//                }
//                else{
//                    layout.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    ArrayList<String> promoCheck = new ArrayList<>();

    @Override
    public void someEvent(String s, boolean isCheck) {
//        Log.e("eventListener",s);
        if(isCheck) promoCheck.add(s);
        else promoCheck.remove(s);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 1: return RetailFragB.newInstance(ccode, devId);
                case 2: return RetailFragC.newInstance(ccode, devId);
                case 3: return RetailFragD.newInstance(ccode, devId);
                default: return RetailFragA.newInstance(ccode, devId);

            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public void preSubmit(View v){
//        for (String num : promoCheck) {
//            System.out.println(num);
//        }
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RetailSubmitToServer();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void RetailSubmitToServer(){
        String sysTime = String.valueOf(System.currentTimeMillis()/1000);
        String Remarks = etRemarks.getText().toString();

        for (String num : promoCheck) {
            String message =  Master.CMD_MSTAT + " "
                    + devId  				  + ";"
                    + sysTime		  		  + ";"
                    + ccode			          + ";"
                    + num					  + ";"
                    + 1;

            DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
        }



        String message =  Master.CMD_MERCH + " "
                + devId  				  + ";"
                + ccode			  + ";"
                + Remarks				  + ";"
                + sysTime;

        DbUtil.saveMsg(context,DbUtil.getGateway(context), message);

        MerchandisingDbManager db = new MerchandisingDbManager(context);
        db.open();
        db.addData(ccode, Remarks);
        db.close();

        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    private void RetailSubmitToServerxxx(){
        Log.e("btSubmit", "onClick merch");
        String remarks = etRemarks.getText().toString();

        RetailDataDbManager db = new RetailDataDbManager(context);
        db.open();
        List<Retail> list = db.getPending(ccode);


        String message;
        devId = DbUtil.getSetting(context, Master.DEVID);

        String getccode = "";
        String getcomcode="";

        for(int i=0; i<list.size(); i++){
            if(getccode.isEmpty()) getccode = list.get(i).getCcode();
            if(getcomcode.isEmpty()) getcomcode = list.get(i).getComcode();
            message = Master.COMPETITORS_PRICE 	 + " " +
                    devId 						 + ";" +
                    list.get(i).getCcode()  	 + ";" +
                    list.get(i).getItemcode() 	 + ";" +
                    list.get(i).getComcode() 	 + ";" +
                    "" 							 + ";" +
                    list.get(i).getPrice()  	 + ";" +
                    list.get(i).getPromo() 		 + ";" +
                    list.get(i).getIoh()		 + ";" +
                    ""							 + ";" +
                    ""							 + ";" +
                    list.get(i).getSOS()		 + ";" +
                    list.get(i).getPlanogram()	 + ";" +
                    list.get(i).getDatetime() 	 + ";" +
                    ""							 + ";" +
                    1;
            DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
            db.updateRemarks(list.get(i).getComcode(), remarks);

        }
        if(!getccode.isEmpty() && !getcomcode.isEmpty()) {
            String message2 = Master.CMDCMPREM + " " +
                    devId + ";" +
                    list.get(0).getCcode() + ";" +
                    list.get(0).getComcode() + ";" +
                    remarks;
            DbUtil.saveMsg(context, DbUtil.getGateway(context), message2);
            Log.e("msg",message2);
        }
        db.close();
        finish();
    }
}