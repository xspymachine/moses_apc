package com.xpluscloud.moses_apc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
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

import com.xpluscloud.moses_apc.dbase.SurveyDbManager;
import com.xpluscloud.moses_apc.getset.Survey;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.LayoutUtil;
import com.xpluscloud.moses_apc.util.Master;

import java.util.List;

/**
 * Created by Shirwen on 8/18/2017.
 */

public class SurveyActivity extends FragmentActivity {

    Context context;

    String ccode ="";
    String cusName = "";
    String cusAddress = "";
    String devId = "";

    EditText etRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.viewpager);

        context = SurveyActivity.this;

        Log.e("btSubmit", "SurveyActivity");
        TextView tvCusName = (TextView) findViewById(R.id.tvCName);
        TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
        etRemarks = (EditText) findViewById(R.id.etRemarks_pg);

        Bundle b = getIntent().getExtras();
        ccode = b.getString("customerCode");
        cusName = b.getString("customerName");
        cusAddress = b.getString("customerAddress");
        devId = b.getString("devId");

        tvCusName.setText(cusName);
        tvAddress.setText(cusAddress);

        ImageView ivIcon = (ImageView) findViewById(R.id.bt_scname);
//		Drawable dr = context.getResources().getDrawable(R.drawable.assets_clientcall);
//		Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
//		// Scale it to 50 x 50
//		Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 60, 60, true));
        Drawable d = new BitmapDrawable(context.getResources(), LayoutUtil.decodeSampledBitmapFromResource(context.getResources(),R.drawable.assets_clientcall, 60, 60));
        ivIcon.setImageDrawable(d);

        SurveyDbManager db = new SurveyDbManager(context);
        db.open();
        db.addAllItems(ccode, devId);
        etRemarks.setText(db.getRemarks(ccode));
        db.close();

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(new asdasdasdMyPagerAdapter(getSupportFragmentManager()));

        Button btSubmit = (Button) findViewById(R.id.btSubmit);
        LayoutUtil.changeChildDrawable(btSubmit);
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.rl_sccontact);
        final View activityRootView = findViewById(R.id.activityRetailRoot);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(context, 200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here

                    layout.setVisibility(View.GONE);
                }
                else{
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private class asdasdasdMyPagerAdapter extends FragmentPagerAdapter {

        public asdasdasdMyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return SurveyA.newInstance(ccode, devId);
                case 1: return SurveyB.newInstance(ccode, devId);
                case 2: return SurveyC.newInstance(ccode, devId);
                default: return SurveyA.newInstance(ccode, devId);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public void preSubmit(View v){
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
        Log.e("btSubmit", "onClick retail");
        String remarks = etRemarks.getText().toString();

        SurveyDbManager db = new SurveyDbManager(context);
        db.open();
        List<Survey> list = db.getPending(ccode);


        String message;
        devId = DbUtil.getSetting(context, Master.DEVID);

        String getccode = "";
        String getScode="";

        for(int i=0; i<list.size(); i++){
            if(getccode.isEmpty()) getccode = list.get(i).getCcode();
            if(getScode.isEmpty()) getScode = list.get(i).getScode();
            message = Master.SURVEY          	 + " " +
                    devId 						 + ";" +
                    list.get(i).getCcode()  	 + ";" +
                    list.get(i).getItemcode() 	 + ";" +
                    list.get(i).getScode() 	     + ";" +
                    ""                           + ";" +
                    ""                           + ";" +
                    list.get(i).getPromo()		 + ";" +
                    ""                           + ";" +
                    list.get(i).getFrequency()   + ";" +
                    list.get(i).getSource()		 + ";" +
                    ""                           + ";" +
                    ""                           + ";" +
                    list.get(i).getDatetime() 	 + ";" +
                    ""  						 + ";" +
                    5;
            DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
            db.updateRemarks(list.get(i).getScode(), remarks);
        }
        if(!getccode.isEmpty() && !getScode.isEmpty()) {
            String message2 = Master.CMDSURREM + " " +
                    devId + ";" +
                    list.get(0).getCcode() + ";" +
                    list.get(0).getScode() + ";" +
                    remarks;
            DbUtil.saveMsg(context, DbUtil.getGateway(context), message2);
        }
        db.close();
        finish();
    }
}
