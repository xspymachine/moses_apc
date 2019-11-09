package com.xpluscloud.mosesshell_davao.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.xpluscloud.mosesshell_davao.GifCustomProgressDialog;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.OkHttpClient;

public class DownloadTaskFile extends AsyncTask<String, Void, Boolean> {

    private Context context;
    private ProgressDialog progressDialog;
    private View rootView;
    private String strDownload;
    private String strRes="";
    OkHttpClient client = new OkHttpClient();


    public DownloadTaskFile(Context context, View rootView, String strDownload) {
        this.context = context;
        this.rootView=rootView;
        this.strDownload=strDownload;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    public void onPreExecute() {
        progressDialog = new GifCustomProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public Boolean doInBackground(String... params) {
        Boolean completed = Boolean.FALSE;
        String linkUrl = params[0];
        String filename = params[1];

        try {
            InputStream input = new java.net.URL(linkUrl).openStream();
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/ShellPromo");
            if(!myDir.isDirectory()) {
                myDir.mkdirs();
            }
            String fname = filename;

            File file = new File(myDir, fname);
            if (file.exists()) file.delete();

            BufferedInputStream bis = new BufferedInputStream(input);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();

            completed = Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return completed;
    }

    @Override
    public void onPostExecute(Boolean completed) {
        String msg = "";

        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if(completed){
            msg = "Synchronizing "+strDownload+" Complete";
//            DbUtil.makeToast(LayoutInflater.from(context),  msg, context,
//                    (ViewGroup) rootView.findViewById(R.id.custom_toast_layout),0);
        }
        else {
            msg = "Synchronizing "+strDownload+" Incomplete - Empty Result "+strRes;
//            DbUtil.makeToast(LayoutInflater.from(context),  msg, context,
//                    (ViewGroup) rootView.findViewById(R.id.custom_toast_layout),0,android.R.color.holo_red_light);
        }

        Log.e("msg",msg);



//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}

