package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class DemandAnalysisActivity extends AppCompatActivity {

    WebView mWebView;
    final Activity mActivity = this;
    Context context;

    String devId = "";
    String ccode = "";
    String apikey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_summary_web);
        context = DemandAnalysisActivity.this;

        Bundle b = getIntent().getExtras();
        devId = b.getString("devId");
        ccode = b.getString("ccode");

        apikey = context.getResources().getString(R.string.apik);

//		ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        mWebView = (WebView) findViewById( R.id.webview );
        final ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar1);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://moses.xpluscloud.com/monthly_demand_apc/index/"+devId+"/"+apikey+"/"+ccode);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                mActivity.setTitle("Loading...");
                mActivity.setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress == 100)
                {
                    mActivity.setTitle(getResources().getString(R.string.app_name));
                    pBar.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                }
            }
        });

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
