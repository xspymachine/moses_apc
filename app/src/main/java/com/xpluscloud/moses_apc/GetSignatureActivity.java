package com.xpluscloud.moses_apc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xpluscloud.moses_apc.dbase.SignatureDbManager;
import com.xpluscloud.moses_apc.getset.Signature;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.Master;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GetSignatureActivity extends Activity {
	
	private String fileSuffix;
	private String customerCode;
	private String customerName;
	private String customerAddress;
	private String csCode;
//	private String txNo;
	private String devId;

	public boolean canvasDirty = false;
	
	private String DateSigned;
	private String devPath;
	signature mSignature;
	
	LinearLayout mContent;
	
	Button mClear, mGetSign, mBack;
	private Bitmap mBitmap;
	
	View mView;
	String ss;
	
	Context context;
	
	public Integer signatureSerial;
		
	String cbSuccess = "Signature text info successfully sent!";
	String cbFailed = "Signature text info failed!";
	
	public boolean submitted;
	Bitmap logo;
	String txNo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState); 
		    setContentView(R.layout.signature);		    
		    
		    context = GetSignatureActivity.this;
		   	submitted=false;
		    
		    
		   	Bundle b = getIntent().getExtras();
			devId				=b.getString("devId");
			customerCode		=b.getString("customerCode");
			customerName		=b.getString("customerName");
			customerAddress		=b.getString("customerAddress");
			csCode				=b.getString("csCode");
			txNo				=b.getString("txNo");
			logo = BitmapFactory.decodeResource(getResources(),R.drawable.apc_logo2);
			logo = Bitmap.createScaledBitmap(logo, 150, 150, true);
			
			if(csCode==null || csCode=="") {
				fileSuffix = customerCode;
			}
			else fileSuffix=csCode;
		    
		    canvasDirty=false;
		    getSignature();	
	    
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.action_menu, menu);
       return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {		    
	    	case R.id.history:
	    		history();
	    		break;	    
	    }
	    return true;
	}

	
	@Override
    protected void onStop(){
       super.onStop();
      
    }

	@Override
	public void onBackPressed() {

	}

	private void history(){
		Intent i = new Intent(this, GetSignatureListActivity.class);
		startActivity(i);		
	}
	
	
	
	 @Override
     public void onConfigurationChanged(Configuration newConfig) {
       super.onConfigurationChanged(newConfig); 
       canvasDirty=false;
     } 

	
	
	public void getSignature() {  	 
		setContentView(R.layout.signature);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
	    DateSigned = df.format(c.getTime());      
		
	      
	    SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd", Locale.US);
	    String folder = df2.format(c.getTime());
	      
		devPath = Environment.getExternalStorageDirectory() + "/"
		            + getResources().getString(R.string.signatures_dir) + "/"
		            + folder +"/"; 
		prepareDirectory(); 
		 			
		mContent = (LinearLayout) findViewById(R.id.canvas);
		mSignature = new signature(this, null); 
		mSignature.setBackgroundColor(Color.WHITE);
		mContent.addView(mSignature, LayoutParams.MATCH_PARENT,
		        LayoutParams.MATCH_PARENT);
		
		mClear = (Button) findViewById(R.id.sig_clear);
		mGetSign = (Button) findViewById(R.id.sig_save);
		mView = mContent; 
		
		//EditText mContact = (EditText)  findViewById(R.id.tv_contactname);
		
		//mContact.setText(contact);
		        
		DbUtil.changeDrawableColor("#E88100", mClear,2);
		DbUtil.changeDrawableColor("#E88100", mGetSign,2);
        		
		mClear.setOnClickListener(new OnClickListener() {
		    @Override
			public void onClick(View v) {
		        Log.v("log_tag", "Panel Cleared");
		        mSignature.clear();
		        canvasDirty=false;
		    } 
		}); 
		
		mGetSign.setOnClickListener(new OnClickListener() {
		    @Override
			public void onClick(View v) {
		    	Toast.makeText( getApplicationContext(), "Saving signature..."  , Toast.LENGTH_SHORT ).show();
		    Log.v("log_tag", "Panel Saved");
		    	mGetSign.setEnabled(false);
		        mView.setDrawingCacheEnabled(true); 
		        mSignature.save(mView);	             
		    } 
		});
	  }
	  

	   private boolean prepareDirectory() { 
		    try { 
		        if (makedirs()) { 
		            return true; 
		        } else { 
		            return false; 
		        } 
		    } catch (Exception e) {
		        e.printStackTrace(); 
		        Toast.makeText(
		                this, 
		                "Could not initiate File System.. Is Sdcard mounted properly?", Toast.LENGTH_LONG).show();
		        return false; 
		    } 
		} 
		
		private boolean makedirs() { 
		    File tempdir = new File(devPath);
		    if (!tempdir.exists()) 
		        tempdir.mkdirs(); 
		    /*
		    if (tempdir.isDirectory()) { 
		        File[] files = tempdir.listFiles(); 
		        for (File file : files) { 
		            if (!file.delete()) { 
		                System.out.println("Failed to delete " + file); 
		            } 
		        } 
		    } 
		    */
		    return (tempdir.isDirectory()); 
		} 
		
		public class signature extends View {
		    private static final float STROKE_WIDTH = 5f; 
		    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2; 
		    private Paint paint = new Paint();
		    private Path path = new Path();
		
		    private float lastTouchX; 
		    private float lastTouchY; 
		    private final RectF dirtyRect = new RectF();
		
		    public signature(Context context, AttributeSet attrs) {
		        super(context, attrs); 
		        paint.setAntiAlias(true); 
		        paint.setColor(Color.BLACK);
		        paint.setStyle(Paint.Style.STROKE);
		        paint.setStrokeJoin(Paint.Join.BEVEL);
		        paint.setStrokeWidth(STROKE_WIDTH);
		        
		        //canvas= new Canvas(); 
		        
		    } 
		    
		    public void save(View v) {
		    	canvasDirty = false;
		        Log.v("log_tag", "Width: " + v.getWidth());
		        Log.v("log_tag", "Height: " + v.getHeight());
		
		        if (mBitmap == null) { 		            
		           mBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565); //Working script
		          
		          //mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		           //mBitmap = Bitmap.createBitmap(480, 320, Bitmap.Config.RGB_565);
		        } 
		        Canvas canvas = new Canvas(mBitmap);
		       /* if (contact!=null) {
		        	canvas.drawText(contact,5,120,paint);
		        	canvas.drawText(DateSigned,5,150,paint);
		        }*/
		        
		        Calendar cal = Calendar.getInstance();
		        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
		        String dateStamp =  df.format(cal.getTime());
	                      
		        String filename = dateStamp +"_" + fileSuffix  +".jpg";
		        
		        String FtoSave = devPath + filename;
		        File file = new File(FtoSave);
		        
		        try { 
		            FileOutputStream mFileOutStream = new FileOutputStream(file);
		            v.draw(canvas); 
		            mBitmap.compress(Bitmap.CompressFormat.JPEG,90, mFileOutStream);
		           
		            mFileOutStream.flush(); 
		            mFileOutStream.close(); 
		            
//		            String url = Images.Media.insertImage(getContentResolver(),
//		                    mBitmap, "title", null);
//		            Log.v("log_tag", "url" + url);
		            Toast.makeText( getApplicationContext(), "Signature Saved!"  , Toast.LENGTH_SHORT ).show();
		            
			        mSignature.clear();
			        finish();
		
		        } catch (Exception e) {
		            Log.v("log_tag", e.toString());
		            Toast.makeText( getApplicationContext(), "Error saving signature!"  , Toast.LENGTH_LONG ).show();
		        }
		        
		      	        
		        String sysTime = String.valueOf(System.currentTimeMillis()/1000);
		        
		        SignatureDbManager db = new SignatureDbManager(context);
				db.open();
				
				Signature c = new Signature();
		    	c.setDatetime(sysTime);
		    	c.setCustomerCode(customerCode);
		    	c.setFilename(filename);
		    	c.setStatus(0);
		    	
		    	Long id = db.Add(c);
		    	db.close();
		        
		    	devId = DbUtil.getSetting(context, Master.DEVID);
		       	 
			   	String message = Master.CMD_SIGNATURE + " " +
			   			devId + ";" +
			   			customerCode + ";" + 
			   			filename + ";" + 
			   			dateStamp + ";"  + 
			   			sysTime		+";" +
			   			id; 
			   	
			   	DbUtil.saveMsg(context,DbUtil.getGateway(context), message);
			   	
			   	String task = "Sending Signature text info.";
			   	Toast.makeText(getApplicationContext(), task +".."  , Toast.LENGTH_SHORT ).show();
				submitted=true;
		    } 
		    
		
		    public void clear() { 
		        path.reset(); 
		        invalidate(); 
		    } 
		
		    @Override
		    protected void onDraw(Canvas canvas) {
		    	paint.setStyle(Paint.Style.FILL);
				paint.setAntiAlias(true);
				
				//if (!canvasDirty) {
					
					canvas.drawBitmap(logo,10,10,null);
					paint.setTextSize(15);
					canvas.drawText("RefNo: "+txNo,10,200,paint);
					canvas.drawText(customerName,10,220,paint);
					canvas.drawText(customerAddress,10,240,paint);
		        	canvas.drawText(DateSigned,10,260,paint);
					canvasDirty = true;
					//Log.w("On Draw: ","Drawing!");
				//}
				
				
		    	paint.setStyle(Paint.Style.STROKE);
		        canvas.drawPath(path, paint); 
		    } 
		
		    @Override
		    public boolean onTouchEvent(MotionEvent event) {
		        float eventX = event.getX(); 
		        float eventY = event.getY(); 
		
		        switch (event.getAction()) { 
		        case MotionEvent.ACTION_DOWN:
		            path.moveTo(eventX, eventY); 
		            lastTouchX = eventX; 
		            lastTouchY = eventY; 
		            return true; 
		
		        case MotionEvent.ACTION_MOVE:
		
		        case MotionEvent.ACTION_UP:
		            resetDirtyRect(eventX, eventY); 
		            int historySize = event.getHistorySize(); 
		            for (int i = 0; i < historySize; i++) { 
		                float historicalX = event.getHistoricalX(i); 
		                float historicalY = event.getHistoricalY(i); 
		                expandDirtyRect(historicalX, historicalY); 
		                path.lineTo(historicalX, historicalY); 
		            } 
		            path.lineTo(eventX, eventY); 
		            break; 
		
		        default: 
		            debug("Ignored touch event: " + event.toString()); 
		            return false; 
		        } 
		
		        invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH), 
		                (int) (dirtyRect.top - HALF_STROKE_WIDTH), 
		                (int) (dirtyRect.right + HALF_STROKE_WIDTH), 
		                (int) (dirtyRect.bottom + HALF_STROKE_WIDTH)); 
		
		        lastTouchX = eventX; 
		        lastTouchY = eventY; 
		        mGetSign.setEnabled(true);
		        return true; 
		    } 
		
		    private void debug(String string) {
		    } 
		
		    private void expandDirtyRect(float historicalX, float historicalY) { 
		        if (historicalX < dirtyRect.left) { 
		            dirtyRect.left = historicalX; 
		        } else if (historicalX > dirtyRect.right) { 
		            dirtyRect.right = historicalX; 
		        } 
		
		        if (historicalY < dirtyRect.top) { 
		            dirtyRect.top = historicalY; 
		        } else if (historicalY > dirtyRect.bottom) { 
		            dirtyRect.bottom = historicalY; 
		        } 
		    } 
		
		    private void resetDirtyRect(float eventX, float eventY) { 
		        dirtyRect.left = Math.min(lastTouchX, eventX);
		        dirtyRect.right = Math.max(lastTouchX, eventX);
		        dirtyRect.top = Math.min(lastTouchY, eventY);
		        dirtyRect.bottom = Math.max(lastTouchY, eventY);
		    } 
		} 
	  /********************** End Signature ****************************/   
	
}
