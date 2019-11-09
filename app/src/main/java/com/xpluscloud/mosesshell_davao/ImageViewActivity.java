package com.xpluscloud.mosesshell_davao;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class ImageViewActivity extends Activity {
	
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imageviewer);
		context = ImageViewActivity.this;
		
		Bundle b = getIntent().getExtras();
		final int resource =b.getInt("resource");
		
		TouchImageView iView = (TouchImageView) findViewById(R.id.ImageViewer);
		iView.setImageResource(resource);
		iView.setPadding(5, 0, 5, 5);
	}

}
