package com.xpluscloud.moses_apc;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.xpluscloud.moses_apc.dbase.AccountTypeDbManager;
import com.xpluscloud.moses_apc.util.DbUtil;

import java.io.File;

public class PDFViewActivity extends AppCompatActivity {
    Context ctx;
    PDFView pdfView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = PDFViewActivity.this;
        setContentView(R.layout.pdf_viewer);

        pdfView = (PDFView) findViewById(R.id.pdfView);
        String pdfFileName = "army_cp.pdf";

        Bundle b = getIntent().getExtras();
        int selected = b.getInt("selected");

        Log.e("selected",""+selected);

        pdfView.setBackgroundColor(Color.LTGRAY);

        AccountTypeDbManager db = new AccountTypeDbManager(ctx);
        db.open();
        String filename = db.getOtherDataSingle(selected,12);
        db.close();

        loadFromFile(filename.split("/")[1]);

//        switch (selected){
//            case 0: loadFromFile("Starter.pdf");break;
//            case 1: loadFromFile("Trade.pdf");break;
//            case 2: loadFromFile("Consumer.pdf");break;
//            case 3: loadFromFile("Guidelines.pdf");break;
//        }

    }

    private void loadFromAsset(String pdfFileName){
        pdfView.fromAsset(pdfFileName)
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10)
                .load();
    }

    private void loadFromFile(String filename){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/ShellPromo");
        File pdfFile = new File(root + "/ShellPromo/"+filename);
        if(myDir.isDirectory() && pdfFile.isFile()) {


            pdfView.setBackgroundColor(Color.LTGRAY);

            pdfView.fromFile(pdfFile)
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(10)
                    .load();
        }else{
            finish();
            DbUtil.makeToast(LayoutInflater.from(ctx),  "PDF file not available", ctx,
                    (ViewGroup) findViewById(R.id.custom_toast_layout),0);
        }
    }
}
