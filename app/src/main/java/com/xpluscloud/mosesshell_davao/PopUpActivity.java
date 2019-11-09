package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;


public class PopUpActivity extends Activity {
	PopupWindow popUp;
    LinearLayout layout;
    TextView tv;
    LayoutParams params;
    LinearLayout mainLayout;
    Button but;
    boolean click = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     popUp = new PopupWindow(this);
     layout = new LinearLayout(this);
     mainLayout = new LinearLayout(this);
     tv = new TextView(this);
     but = new Button(this);
     but.setText("Click Me");
     
     
     but.setOnClickListener(new OnClickListener() {
	      @Override
		public void onClick(View v) {
	    	  if (click) {
					popUp.showAtLocation(layout, Gravity.BOTTOM, 20, 20);
					popUp.update(200, 200, 600, 180);
					click = false;
	    	  } else {
		        popUp.dismiss();
		        click = true;
	    	  }
	      }
     });
     
     params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    		 android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
     layout.setOrientation(LinearLayout.VERTICAL);
     tv.setText("Hi this is a sample text for popup window");
     layout.addView(tv, params);
     popUp.setContentView(layout);
     // popUp.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
     mainLayout.addView(but, params);
     setContentView(mainLayout);
    }
}