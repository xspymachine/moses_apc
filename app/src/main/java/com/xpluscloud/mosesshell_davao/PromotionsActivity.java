package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xpluscloud.mosesshell_davao.dbase.AccountTypeDbManager;

/**
 * Created by Shirwen on 4/18/2018.
 */

public class PromotionsActivity extends Activity {

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ctx = PromotionsActivity.this;

        TextView brochure = findViewById(R.id.brochure);

        brochure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Intent intent = new Intent(PromotionsActivity.this, Brochure01.class);
//                Bundle b = new Bundle();
//                b.putInt("option", 1);
//                intent.putExtras(b);
//                startActivity(intent);

                AccountTypeDbManager db = new AccountTypeDbManager(ctx);
                db.open();
                final String[] items = db.getOtherData(13);
                db.close();

//                final String[] items = {
//                        "Army Detailer - Starter Pack", "Army Detailer - Trade Promo", "Army Detailer - Consumer Promo","Army - Merch Guidelines"
//                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Current Promotions");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection

                        AccountTypeDbManager db = new AccountTypeDbManager(ctx);
                        db.open();
                        int pcid = db.getOtherDataId(items[item],13);
                        db.close();
                        Intent intent = new Intent(PromotionsActivity.this, PDFViewActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("option", 1);
                        b.putInt("selected",pcid);
                        intent.putExtras(b);
                        startActivity(intent);

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });


        TextView productAVP = (TextView) findViewById(R.id.pAvp);

        productAVP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Intent intent = new Intent(WallmasterInfoActivity.this, VideoPlayerActivity.class);
//                Bundle b = new Bundle();
//                b.putString("vidpath", "android.resource://"+getPackageName()+"/"+R.raw.splash);
//                intent.putExtras(b);
//                startActivity(intent);
                Toast.makeText(PromotionsActivity.this,"PRODUCT AVP EMPTY", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
