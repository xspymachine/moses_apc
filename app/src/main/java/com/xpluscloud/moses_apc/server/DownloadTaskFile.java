package com.xpluscloud.moses_apc.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xpluscloud.moses_apc.GifCustomProgressDialog;
import com.xpluscloud.moses_apc.R;
import com.xpluscloud.moses_apc.util.DbUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        Log.e("link_url",linkUrl);
        try {
            Request request = new Request.Builder().url(linkUrl).build();
            Response response = client.newCall(request).execute();
            InputStream pdf = response.body().byteStream();

            String root = context.getFilesDir().toString();
            File myDir = new File(root + "/apc");
            if(!myDir.isDirectory()) {
                myDir.mkdirs();
            }
            String fname = filename;

            File file = new File(myDir, fname);
            if (file.exists()) file.delete();

            BufferedInputStream bis = new BufferedInputStream(pdf);
            FileOutputStream fos = new FileOutputStream(file);

            int current = 0;
            while ((current = bis.read()) != -1) {
                fos.write(current);
            }

            fos.close();

            completed = Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return completed;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    public void onPostExecute(Boolean completed) {
        String msg = "";

        if(progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if(completed){
            msg = "Synchronizing "+strDownload+" Complete";
            DbUtil.makeToast(LayoutInflater.from(context),  msg, context,
                    (ViewGroup) rootView.findViewById(R.id.custom_toast_layout),0);
        }
        else {
            msg = "Synchronizing "+strDownload+" Incomplete - Empty Result "+strRes;
            DbUtil.makeToast(LayoutInflater.from(context),  msg, context,
                    (ViewGroup) rootView.findViewById(R.id.custom_toast_layout),0,android.R.color.holo_red_light);
        }

//        Log.e("msg",msg);



//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}

