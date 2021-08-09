package com.xpluscloud.moses_apc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xpluscloud.moses_apc.util.DbUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class UploadPictureActivity extends AppCompatActivity {
   
	public static String FilePath;
    public static String currentPath;
    private ArrayList<String> Files;
    private String uploadURL;
    
	public Context context;

	Menu menu;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.uppicture);
        context = UploadPictureActivity.this;
        try {
        	//Resources res = getResources(); 

        	uploadURL = getString(R.string.pic_file_upload);
	        FilePath = Environment.getExternalStorageDirectory() + "/"
	  	            + getResources().getString(R.string.pictures_dir) + "/";
	  	    
	  	   // sFolder +=  folder +"/";
	        currentPath = FilePath;
	       fileList(currentPath);        
        }catch (Exception e) {
        	Toast.makeText(getApplicationContext(), "ERROR On Create... " +  Environment.getExternalStorageDirectory() + "/"
      	            + getResources().getString(R.string.pictures_dir) + "/" , Toast.LENGTH_LONG ).show();
        }
    }
    
    private void fileList(String currentpath) {
    	ListView listView = (ListView) findViewById(R.id.file_list);
    	
        File path = new File(currentpath);
        Files = new ArrayList<String>();
        File[] dirList = path.listFiles();
        if (dirList != null) { 
    	   List<File> dirListing = new ArrayList<File>();
    	   dirListing.addAll(Arrays.asList(dirList));
    	   Collections.sort(dirListing, new descSortFileName());
    	   Collections.sort(dirListing, new descSortFolder());
    	   
    	   for (File file : dirListing) {
    		   if (file.getName().matches("[0-9]+") && file.getName().length() > 2) {
    			   Files.add(file.getName());              
    		   }
               Log.i("File : Size", file.getAbsolutePath() + " : " + String.valueOf(file.length()));
           } 
    	   try {
    		   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
    		    		R.layout.detail_picture_file_list, R.id.filename, Files);
    		    listView.setAdapter(adapter);// set all the file in the list 
    	   }
           catch(Exception e) {
        	   Log.e("ERROR: ","LST");
           }
    	   currentPath = currentpath;
    	}
        else {
        	Log.e("Directory : ","No! " + currentpath);
        }
    } 
  
    
    
    
  //sorts based on the files name 
    public class SortFileName implements Comparator<File> {
        @Override
		public int compare(File f1, File f2) {
              return f1.getName().compareTo(f2.getName()); 
        } 
    }
    
    public class descSortFileName implements Comparator<File> {
        @Override
		public int compare(File f1, File f2) {
              return f2.getName().compareTo(f1.getName()); 
        } 
    } 
     
    //sorts based on a file or folder. folders will be listed first 
    public class SortFolder implements Comparator<File> {
        @Override
		public int compare(File f1, File f2) {
             if (f1.isDirectory() == f2.isDirectory()) 
                return 0; 
             else if (f1.isDirectory() && !f2.isDirectory()) 
                return -1; 
             else 
                return 1; 
        } 
    } 
    
    public class descSortFolder implements Comparator<File> {
        @Override
		public int compare(File f1, File f2) {
             if (f1.isDirectory() == f2.isDirectory()) 
                return 0; 
             else if (f1.isDirectory() && !f2.isDirectory()) 
                return 1; 
             else 
                return -1; 
        } 
    }
    
    
    ///Click handlers   
    public void fileItemClicked(View v) {
    	TextView fileitem = (TextView) v.findViewById(R.id.filename);
    	String clickedItem = fileitem.getText().toString();
    	File targetPath = new File(currentPath + clickedItem);
    	
//    	if (targetPath.isDirectory()) {
//    		String path = currentPath + clickedItem +"/";
//    		Log.e("New Path: ", path );
//    		fileList(path);
//    	}
//    	else {
    		//check/un-check the box
    		CheckBox cbItem = (CheckBox) v.findViewById(R.id.cb);
    		if (cbItem.isChecked()) cbItem.setChecked(false);
    		else cbItem.setChecked(true);
//    	}
    }
    
    
    public void deletePicture(View v) {
    	//Insert Confirmation Dialog    	  	
    	confirmDelete(v);
    }
    
    public void uploadButtonClicked(View button) {
    	if (isConnected()) {
	    	ListView fileList = (ListView) findViewById(R.id.file_list);
	
	    	
	    	 File Path=null;
	         Files = new ArrayList<String>();
	         //File[] dirList; 
	    	
	    	ArrayList<File> filePathArray =  new ArrayList<File>();
	    	
	    	HashMap<String, String> selectedFiles = new HashMap<String, String>();
			Integer listSize = fileList.getChildCount();
		
	    	Boolean selected = false;
	    	Toast.makeText(getApplicationContext(), "Uploading Picture... "  , Toast.LENGTH_LONG ).show();
	    	for (int i = 0; i < listSize; i++) { 
	    	    View v = fileList.getChildAt(i);
	    	    CheckBox cb = (CheckBox) v.findViewById(R.id.cb);
	    	    if (cb.isChecked()) {
	    	    	selected=true;
	    	    	TextView fileitem = (TextView) v.findViewById(R.id.filename);
	    	    	String selectedItem = fileitem.getText().toString();

	    	    	
	    	    	Path = new File(currentPath + selectedItem);
                    Log.e("Selected Item:  ", currentPath+selectedItem );
	    	    	//Check if Filepath is Directory
	    	    	
	    	    	if(Path.isDirectory()) 	selectedFiles = dirList(Path);	    	    	
	    	    	else {
		    	    	Log.v("UpButtonClicked: ", currentPath + selectedItem);
		    	    	filePathArray.add(Path);
		    	    	//Image_Uploader asyncHttpPost =  new Image_Uploader(); 
						//asyncHttpPost.execute();  
		    	    	
		    	    	//	uploadFile(currentPath + selectedItem,selectedItem);
		    	    	selectedFiles.put(selectedItem, currentPath+selectedItem);
	    	    	}
	    	    }    	    
	    	}
	    	
	    	if (!selected) Toast.makeText( getApplicationContext(), "Select a file or files to upload..."  , Toast.LENGTH_SHORT ).show();
	    	else {
	    		ImageUploader mFileUploader = new ImageUploader(selectedFiles);
	    		mFileUploader.execute(uploadURL);
	    	}	    	
    	}
    	else {
    		Toast.makeText( getApplicationContext(), "Unable to connect to Internet."  , Toast.LENGTH_SHORT ).show();
    	}
    }
    
    private HashMap<String, String> dirList(File path) {
    	HashMap<String, String> dirFiles = new HashMap<String, String>();
    	if( path.exists() ) { 
            File[] files = path.listFiles();
            
            for(int i=0; i<files.length; i++) { 
               if(files[i].isDirectory()) { 
                 dirFiles = dirList(files[i]); 
               } 
               else { 
            	 dirFiles.put(files[i].getName(), files[i].getAbsolutePath());  
                  
               } 
            } 
          }    	
    	return dirFiles;
    }
    
   

    public void confirmDelete(View v) {
 	   
    	//final View fileList; 
 	   //final Integer Option = option;
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Confirm delete.") 
	       .setCancelable(false) 
	       .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
	           @Override
			public void onClick(DialogInterface dialog, int id) {
	        	   ListView fileList = (ListView) findViewById(R.id.file_list);
	            	Integer listSize = fileList.getChildCount();
	    	    	for (int i = 0; i < listSize; i++) { 
	    	    		View listView = fileList.getChildAt(i);
	    	    		CheckBox cb = (CheckBox) listView.findViewById(R.id.cb);
	    	    	    if (cb.isChecked()) {
	    	    	    	TextView fileitem = (TextView) listView.findViewById(R.id.filename);
	    	    	    	String selectedItem = fileitem.getText().toString();
	    	    	    	Log.e("Selected Item:  ", selectedItem );
	    	    	    	Toast.makeText(getApplicationContext(), "Deleting Signature... " + selectedItem  , Toast.LENGTH_LONG ).show();
	    	    	    	try {
	    	    	    		File file = new File(currentPath + selectedItem);
	    	    	    		if (file.isDirectory()) deleteDirectory(file);
	    	    	    		else {
	    	    	    			boolean deleted = file.delete(); 
	    	    	    			if (deleted) Toast.makeText( getApplicationContext(), selectedItem+ " deleted!"  , Toast.LENGTH_LONG ).show();
	    	    	    		}
	    	    	    	}catch (Exception e) {
	    	    	    		Log.e("Error: ******", selectedItem +" File not deleted!");
	    	    	    	}
	    	    	    }
	    	    	}  
	            	
	            	finish(); 
	           } 
	       }) 
	       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	           @Override
			public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel(); 
	           } 
	       }); 
    	AlertDialog alert = builder.create();
    	alert.show();     	
    }
    
    
    public void deleteDirectory(File path) {
        if( path.exists() ) { 
          File[] files = path.listFiles();
          
          for(int i=0; i<files.length; i++) { 
             if(files[i].isDirectory()) { 
               deleteDirectory(files[i]); 
             } 
             else { 
               files[i].delete(); 
             } 
          } 
        } 
        path.delete(); 
      } 

    

	//********************************************************
    //**** Multi-File Uploader
    //********************************************************
    public class ImageUploader extends AsyncTask<String, String, ArrayList<String>> {
    	private HashMap<String, String> mData = null;// post data
        private ProgressDialog progressDialog;
    	
    	private ArrayList<String> Responses;
    	
    	public ImageUploader(HashMap<String, String> data) {
            mData = data; 
        }     		
    	
    	@Override
    	protected void onPreExecute() {
            progressDialog = new GifCustomProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } 
    	
    	@Override
    	protected void onProgressUpdate(String... values) {
    		
    	}
    	
    	@Override
    	protected ArrayList<String> doInBackground(String... params) {
    		String strURL = params[0];
            Iterator<String> it = mData.keySet().iterator();
            String serverFilename ="";
            String fullPath ="";
            Responses = new ArrayList<String>();
            
            while (it.hasNext()) { 
                String key = it.next();
                serverFilename = key;
                fullPath = mData.get(key);                
                Log.w("*** File ***:",fullPath);
                try {
                	
                	File inputFile = new File(fullPath);
                	FileInputStream fis = new FileInputStream(inputFile);
        	    	HttpFileUploader htfu = new HttpFileUploader(strURL,"noparamshere", serverFilename);
        	    	String Response = htfu.doStart(fis,context);
        	    	Responses.add(serverFilename + " : " + Response);
        	    	Log.e("*** File upload ***:", serverFilename + " " + Response);
                	//uploadImage(fullPath,serverFilename);          	    	
        	    	
                } catch (Exception e) {
        	    	Log.e("*** ERROR ***:","File Not Found!");
        	    	e.printStackTrace();
        	   	} 
            }
    		return Responses;
    	}
    	
        @Override
        protected void onPostExecute(ArrayList<String> Responses) {
        	//Toast.makeText( getApplicationContext(), "Upload completed!"  ,Toast.LENGTH_LONG ).show();
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        	if(!Responses.isEmpty()) uploadResult(Responses);        	
        }
        
    }
    
    public void uploadResult(ArrayList<String> Responses) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	TextView myMsg = new TextView(this);
    	int i = 0; 
    	String strResponses ="";
        while( i < Responses.size() ) { 
        	strResponses += Responses.get(i) + "\n";
            i++; 
        }
        
        myMsg.setText(strResponses); 
    	myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
    	myMsg.setTextSize(20f);
    	builder.setView(myMsg) 
    		.setTitle("File Upload Result")
    		.setCancelable(false) 
    		.setPositiveButton("Close", new DialogInterface.OnClickListener() {
	           @Override
			public void onClick(DialogInterface dialog, int id) {
	        	   
	            	finish(); 
	           } 
	       }); 	      
    	
    	AlertDialog dialog = builder.create();
    	dialog.show();     	
    }
    
    private boolean isConnected() { 
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) { 
				// There are no active networks. 
			return false; 
		} else return true; //ping(); 
   }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload, menu);

		this.menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.sdcard:
				String devPath="";
                MenuItem strMenu = menu.findItem(R.id.sdcard);
                String title = strMenu.getTitle().toString();
				File[] extMounts = getApplicationContext().getExternalFilesDirs(null);
				File sdRoot = extMounts[1];
				if(title.contains("Hide")){
					devPath = Environment.getExternalStorageDirectory() + "/"
							+ getResources().getString(R.string.pictures_dir) + "/";
                    title = "Show SDCard";
				}else{
				    if(sdRoot != null) {
                        devPath = sdRoot.getAbsolutePath() + "/" + getResources().getString(R.string.pictures_dir) + "/";
                        title = "Hide SDCard";
                    }else {
						DbUtil.makeToast(LayoutInflater.from(context), "SDCard is not available", context, null, 0);
					}
				}
				File file = new File(devPath);
				if(file.isDirectory()) {
                    currentPath = devPath;
                    fileList(currentPath);
                    strMenu.setTitle(title);
                }else{
					DbUtil.makeToast(LayoutInflater.from(context),  "SDCard is not available" , context,null,0);
                }
				break;

		}
		return true;
	}
  
}
