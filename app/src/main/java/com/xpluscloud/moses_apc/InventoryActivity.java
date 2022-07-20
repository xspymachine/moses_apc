package com.xpluscloud.moses_apc;

/**
 * Date 2014-01-17
 * 
 * This class activity is created for inventory() function
 * only the layout
 * still have no function database is not yet created
 * 
 */

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.xpluscloud.moses_apc.dbase.CallSheetDbManager;
import com.xpluscloud.moses_apc.dbase.InventoryDbManager;
import com.xpluscloud.moses_apc.dbase.ItemDbManager;
import com.xpluscloud.moses_apc.getset.Customer;
import com.xpluscloud.moses_apc.getset.Inventory;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.DialogManager;
import com.xpluscloud.moses_apc.util.LayoutUtil;
import com.xpluscloud.moses_apc.util.Master;
import com.xpluscloud.moses_apc.util.StringUtil;

import java.util.List;

public class InventoryActivity extends AppCompatActivity implements ItemOptionDialogFragment.OptionDialogListener {
    Context context;

	TableLayout table;
	Button btAddSku;
    Button btSignature;
    EditText etRemarks;
    EditText etINNo;
    Button btSubmit;
    TextView tvCname;
    TextView tvCaddress;

	Boolean SUBMITTED=false;
    Boolean Init=true;

    String devId;
    String csCode;
    String cCode;
    String cName;
    String cAddress;
    Integer cCashSales;
    Integer iNNo;
    public Long dateTime;
    TextView tvDate;

    final int TV_CSI_ID		= 100001;
    final int TV_ITEM_CODE	= 100002;
    final int TV_DESCR 		= 100003;
    final int SP_PCKG		= 100004;
    final int TV_DELIVERY	= 100005;
    final int TV_PASTINVT	= 100006;
    final int ET_PRESENTINVT= 100006;
    final int TV_OFFTAKE	= 100007;
    final int TV_ICO		= 100008;
    final int TV_SUGGEST	= 100009;
    final int ET_ORDER		= 100010;
    final int TV_PRICE		= 100011;
    final int TV_AMOUNT		= 100012;
    final int TV_STATUS		= 100013;

    final int MODE_ADD = 0;
    final int MODE_EDIT = 1;
    int mode;
    int catid=0;
    int icatid=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory2);
		context = InventoryActivity.this;

        Bundle extras = getIntent().getExtras();
        devId 			= extras.getString("devId");
        csCode			= extras.getString("csCode");
        cCashSales      = extras.getInt("cCashSales");
        cCode           = extras.getString("customerCode");
        cName			= extras.getString("customerName");
        cAddress		= extras.getString("customerAddress");

        if (csCode==null || csCode.equals("")) mode = MODE_ADD;
        else mode = MODE_EDIT;

		btAddSku 		= (Button) findViewById(R.id.btAddSku);
		table = (TableLayout) findViewById(R.id.callsheetitem_table);
		table.setStretchAllColumns(true);
        etRemarks		= (EditText) findViewById(R.id.etRemarks);
        etINNo			= (EditText) findViewById(R.id.etSONo);
        tvCname 		= (TextView) findViewById(R.id.tvCName);
        tvCaddress 		= (TextView) findViewById(R.id.tvAddress);
        tvDate			= (TextView) findViewById(R.id.tvDate);

        tvCname.setText(cName);
        tvCaddress.setText(cAddress);

        btSubmit 		= (Button) findViewById(R.id.btSubmit);
        btSubmit.setEnabled(false);

//        btSignature		= (Button) findViewById(R.id.btSignature);
//        btSignature.setEnabled(false);

		btAddSku.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btAddSku.setEnabled(false);
				iNNo = Integer.valueOf(etINNo.getText().toString());
				if(iNNo<=0) {
					DialogManager.showAlertDialog(context, "Valid SO Number Required!", "Enter SO number.", null);
					btAddSku.setEnabled(true);
                    etINNo.requestFocus();
				}
				else {
					createCallSheet(0);
				}
				btAddSku.setEnabled(true);
			}
		});

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Button Clicked!", "btSubmitIO");

                disableSubmitButton();
                InventoryDbManager db = new InventoryDbManager(context);
                db.open();
                int qty = db.getTotalQuantity(csCode);
                db.close();
                if(qty>0)  {
                    submitCallSheet();
//                    sendCallSheet();
                }
                else {
                    DialogManager.showAlertDialog(context, "No Actual Inventory!", "Enter at least 1 item with actual inventory.", null);
                }
                enableSubmitButton();
            }
        });

//        btSignature.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                disableSignatureButton();
//                getSignature();
//            }
//        });

        initInfo();
		
	}
    private int generateSONo(){
        int sono=0;
        InventoryDbManager db = new InventoryDbManager(context);
        db.open();
        sono=db.getLastSOno();
        db.close();

        sono +=1;

        return sono;
    }

    private void createCallSheet(int past){
        if(mode==MODE_ADD) {
            csCode = createCsCode();

            Inventory in = new Inventory();
            in.setINno(iNNo);
            in.setINCode(csCode);
            in.setCcode(cCode);
            in.setDate(String.valueOf(dateTime/1000));

            InventoryDbManager db = new InventoryDbManager(context);
            db.open();
            db.AddInventorySheet(in);
            db.close();

            mode=MODE_EDIT;

            updateList();
        }
        //Create call sheet items
        //createCallSheetItem();
        if(past!=1) addItems(1);
//		if(past!=1) selectSingleItem(BOOKING);

    }

    private String createCsCode(){
        CallSheetDbManager db = new CallSheetDbManager(context);
        db.open();
        Integer lastId=db.getLastId()+1;
        db.close();

        String strCode = ("00000" + lastId).substring(String.valueOf(lastId).length());
        String strRandom = StringUtil.randomText(10);
        strCode = devId.substring(devId.length() - 5) + strRandom + strCode ;

        return strCode;
    }

    private void addItems(int opt) {
//		selectSingleItem();
        DialogFragment newFragment = new ItemOptionDialogFragment();//("title",R.array.callsheet_options);
        Bundle args = new Bundle();
        args.putInt("opt", opt);
        args.putInt("catid", catid);
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "AddItems");
    }

    @Override
    public void onSelectOption(Integer position, String strValue) {
        ItemDbManager db = new ItemDbManager(context);
        db.open();
        int _id = db.getCatId(strValue,1);
        if(_id>0){
            catid = _id;
            addItems(2);
        }
        else {
            icatid=db.getCatId(strValue,2);
            Log.e("catid",""+catid);
            Log.e("icatid",""+icatid);
            selectSingleItem(catid,icatid);
        }


        db.close();
    }

    private void selectSingleItem(int catid, int icatid) {
        Intent intent = new Intent(context, ItemListActivity.class);
        Bundle b = new Bundle();

        b.putString("csCode", csCode);
        b.putInt("catid", catid);
        b.putInt("icatid", icatid);
        intent.putExtras(b);
        startActivityForResult(intent, 1);
    }

    private void initInfo(){
        Customer cust 	= DbUtil.getCustomerInfo(context, cCode);
        iNNo            = generateSONo();
        cName 			= cust.getName();
        cAddress		= cust.getAddress();
        dateTime 		= System.currentTimeMillis();
        String strDate = DateUtil.phShortDate(dateTime);
        tvDate.setText(strDate);
        etINNo.setText(String.valueOf(iNNo));
    }

    private void submitCallSheet() {

        final ProgressDialog progress = ProgressDialog.show(this, "Submitting",
                "Please wait...", true);

        new Thread(new Runnable() {
            @Override
            public void run(){
                sendCallSheet();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run(){
                        progress.dismiss();
                    }
                });
            }
        }).start();

    }

    private void sendCallSheet() {
        //Boolean submitted=false;
        Long sysTime = System.currentTimeMillis();
        String trDate =  String.valueOf(sysTime/1000);

        InventoryDbManager db = new InventoryDbManager(this);
        db.open();

        Inventory cs = db.getInfo(csCode);

        String message="";

        devId = DbUtil.getSetting(context, Master.DEVID);

        message = Master.CMD_INV 				+ " " +
                devId 								+ ";" +
                cs.getCcode()           			+ ";" +
                cs.getINCode()          			+ ";" +
                cs.getDate()            			+ ";" +
                cs.getINno().toString()				+ ";" +
                cCashSales							+ ";" +
                trDate								+ ";" +
                cs.getId()							+ ";" +
                etRemarks.getText().toString();

        DbUtil.saveMsg(context,DbUtil.getGateway(context), message);

        db.updateStatus(cs.getId(), 1); //1=Transmitted

//        db.close();
        //submitted=true;

//        InventoryDbManager dbi = new InventoryDbManager(this);
//        dbi.open();
        List<Inventory> csi = db.getList(csCode);
//        dbi.close();

        for (Inventory item : csi) 	{
            if(item.getStatus()==0 && item.getQty()>0) {
                message = Master.CMD_INVI + " " +
                        devId 					+ ";" +
                        item.getINCode() 		+ ";" +
                        item.getItemCode()		+ ";" +
                        item.getPckg() 			+ ";" +
                        item.getQty()			+ ";" +
                        item.getPrice()			+ ";" +
                        item.getId();

                DbUtil.saveMsg(context,DbUtil.getGateway(context), message);

//                dbi.open();
                db.updateStatus(item.getId(), 1); //1=Transmitted
                db.close();

            }
        }


        //if (submitted) Toast.makeText(getApplicationContext(), "Callsheet has been submitted to the central server!" , Toast.LENGTH_SHORT).show();

        finish();

    }

	private void updateList() {

		InventoryDbManager db = new InventoryDbManager(this);

		db.open();
		List<Inventory> csItems = db.getList(csCode);
		db.close();


		table = (TableLayout) findViewById(R.id.callsheetitem_table);
		table.setStretchAllColumns(true);

		TableRow header = new TableRow(this);
		header.setLayoutParams(new TableRow.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		header.setBackgroundColor(Color.BLACK);

		//Item ID
		TextView HItemId = new TextView(this);
		HItemId.setText("");
		HItemId.setLayoutParams(new TableRow.LayoutParams(1,1));
		HItemId.setVisibility(View.GONE);
		header.addView(HItemId);

		//Status
		TextView hStatus = new TextView(this);
		hStatus.setText("");
		hStatus.setLayoutParams(new TableRow.LayoutParams(1,1));
		hStatus.setVisibility(View.GONE);
		header.addView(hStatus);


		//Item Description Column
		TextView HitemDescription = new TextView(this);
		HitemDescription.setText("Description");
		HitemDescription.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		HitemDescription.setBackgroundColor(Color.BLACK);
		//HitemDescription.setMinWidth(200);

		header.addView(HitemDescription);

		//PCKG Column
		TextView hPckg = new TextView(this);
		hPckg.setText("UNIT");
		hPckg.setBackgroundColor(Color.BLACK);
		hPckg.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		hPckg.setVisibility(View.VISIBLE);
		header.addView(hPckg);

		//Actual Order Column
		TextView hOrderQty = new TextView(this);
		hOrderQty.setText("Quantity");
		hOrderQty.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hOrderQty.setBackgroundColor(Color.BLACK);
		//hOrderQty.setMinimumWidth(120);
		hOrderQty.setVisibility(View.VISIBLE);
		header.addView(hOrderQty);

		//Item Code Column
		TextView hitemCode = new TextView(this);
		hitemCode.setText("ItemCode");
		hitemCode.setLayoutParams(new TableRow.LayoutParams(1,1));
		//hitemCode.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		hitemCode.setVisibility(View.GONE);
		header.addView(hitemCode);


		table.addView(header);
		setupView(header);
		setRowColor(header,Color.WHITE,Typeface.BOLD);


		int row=0;
		Init=true;


		for (Inventory item : csItems) 	{
			row++;
			final TableRow tr = new TableRow(this);
			tr.setLayoutParams(new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

			if(row % 2 == 0){
				if(SUBMITTED) tr.setBackgroundColor(0xFFDBDBEA);
				else tr.setBackgroundColor(0xFFE4FFCA);
			}
			else {
				tr.setBackgroundResource(R.drawable.cell_border);
			}

			//Item ID
			final TextView csiId = new TextView(this);
			csiId.setText(String.valueOf(item.getId()));
			csiId.setLayoutParams(new TableRow.LayoutParams(1,1));
			csiId.setVisibility(View.GONE);
			csiId.setId(TV_CSI_ID);
			tr.addView(csiId);

			//Status
			TextView status = new TextView(this);
			status.setText(String.valueOf(item.getStatus()));
			status.setLayoutParams(new TableRow.LayoutParams(1,1));
			status.setVisibility(View.GONE);
			tr.addView(status);

			//Item Description Column
			TextView itemDescription = new TextView(this);
			itemDescription.setText(item.getDescription()+"\n");
			itemDescription.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			itemDescription.setGravity(Gravity.LEFT | Gravity.BOTTOM);
			itemDescription.setSingleLine(false);
			itemDescription.setMaxLines(4);
			//itemDescription.setMinWidth(140);
			itemDescription.setMaxWidth(120);
			tr.addView(itemDescription);


			//PCKG Spinner Column
			final Spinner etPckg = new Spinner(this);
			String pckg =  item.getPckg();
			etPckg.setVisibility(View.VISIBLE);
			etPckg.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			//etPckg.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			etPckg.setPadding(1, 1, 1, 1);
			if(SUBMITTED) {
				etPckg.setEnabled(false);
				etPckg.setClickable(false);
			}

//			AccountTypeDbManager datadb = new AccountTypeDbManager(this);
//			datadb.open();
//			String[] pckg_opt= datadb.getOtherData(10);
//			datadb.close();

			String[] pckg_opt = Master.pckg_option;
			etPckg.setEnabled(true);
			String[] drm = {"DRUM"};
			String[] pl = {"PAIL"};
			ItemDbManager dbItem = new ItemDbManager(context);
			dbItem.open();
			int pack = dbItem.getPacking(item.getItemCode());
			dbItem.close();
			if(pack==1000) {
				etPckg.setEnabled(false);
				pckg_opt=pl;
			}
			else if(pack==2000) {
				etPckg.setEnabled(false);
				pckg_opt = drm;
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
					R.layout.csi_spinner_phone, pckg_opt);
			etPckg.setAdapter(spinnerArrayAdapter);
			etPckg.setSelection(0);
//			if(pckg.equalsIgnoreCase(Master.PACKS)) {
//				etPckg.setSelection(Master.PCKG_PACK);
//			}
//			else etPckg.setSelection(Master.PCKG_UNIT);
			etPckg.setId(SP_PCKG);
			tr.addView(etPckg);

			//Actual Order  Column
			final EditText orderqty = new EditText(this);
			orderqty.setText(String.valueOf(item.getQty()));
			orderqty.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			orderqty.setVisibility(View.VISIBLE);
			orderqty.setMinimumWidth(30);
			if(SUBMITTED) {
				orderqty.setBackgroundColor(0xFFEAEAEE);
				orderqty.setClickable(false);
				orderqty.setFocusable(false);
			}
			else orderqty.setBackgroundColor(0xFFA9FF53);
			orderqty.setId(ET_ORDER);
			orderqty.addTextChangedListener(new qtyTextWatcher(tr));
			orderqty.setInputType(InputType.TYPE_CLASS_NUMBER);
			tr.addView(orderqty);

			orderqty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					String qtyString = orderqty.getText().toString().trim();
					int qty = qtyString.equals("") ? 0:Integer.valueOf(qtyString);

					if(hasFocus){
						orderqty.setBackgroundColor(0xFFFFF3CE);
						if(qty==0) orderqty.setText("");
					}else {
						orderqty.setBackgroundColor(0xFFA9FF53);
						if(qty==0) orderqty.setText("0");

					}
				}

			});


			registerForContextMenu(tr);

			table.addView(tr);
			setupView(tr);

			if(!SUBMITTED) {
				setRowColor(tr,Color.BLACK,Typeface.NORMAL);
				enableSubmitButton();
			}
			else {
				setRowColor(tr,Color.DKGRAY,Typeface.NORMAL);
				disableSubmitButton();
//				disableSignatureButton();
			}
		}

		Init=false;
		if(row>0 && !SUBMITTED) {
			enableSubmitButton();

//			enableSignatureButton();
		}
	}

    private class qtyTextWatcher implements TextWatcher {

        private View view;
        private qtyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //do nothing
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //do nothing
        }
        @Override
        public void afterTextChanged(Editable s) {

            try{
                String qtyString = s.toString().trim();
                int quantity = qtyString.equals("") ? 0:Integer.valueOf(qtyString);


                if(!Init){
                    //Update CallSheetItem OrderQTy
                    TextView csiId = view.findViewById(TV_CSI_ID);
                    //Save CS_ITEM
                    Inventory csi = new Inventory();

                    csi.setId(Integer.valueOf(csiId.getText().toString()));
                    csi.setQty(quantity);

                    InventoryDbManager db = new InventoryDbManager(context);
                    db.open();
                    db.UpdateOrderQty(csi);
                    db.close();
                }
            }catch(Exception e){e.printStackTrace();}
        }
    }

    private void enableSubmitButton() {
        btSubmit.setBackgroundResource(R.drawable.btn_ripple2);
        btSubmit.setTextColor(Color.BLUE);
        btSubmit.setEnabled(true);
        btSubmit.setVisibility(View.VISIBLE);
    }
    private void disableSubmitButton() {
        btSubmit.setBackgroundResource(R.drawable.rounded_gray_button);
        btSubmit.setEnabled(false);
        btSubmit.setTextColor(Color.LTGRAY);
    }
//    private void disableSignatureButton() {
//        btSignature.setBackgroundResource(R.drawable.rounded_gray_button);
//        btSignature.setEnabled(false);
//        btSignature.setTextColor(Color.LTGRAY);
//        btSignature.setVisibility(View.INVISIBLE);
//    }
//    private void enableSignatureButton() {
//        btSignature.setBackgroundResource(R.drawable.btn_ripple2);
//        btSignature.setTextColor(Color.BLUE);
//        btSignature.setEnabled(true);
//        btSignature.setVisibility(View.VISIBLE);
//    }

	private void setRowColor(TableRow tr, Integer color, Integer typeface) {
		for(int i=2;i<tr.getChildCount();i++){
			View child=tr.getChildAt(i);

			try {
				((TextView) child).setTextColor(color);
				((TextView) child).setTypeface(null,typeface);
			}catch (Exception e){}
		}
	}

	private void setupView(TableRow tr) {
		for(int i=2;i<tr.getChildCount();i++){
			View child=tr.getChildAt(i);
			child.setPadding(3, 3, 3, 3);

			child.setLayoutParams(new TableRow.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			if (child instanceof TextView){
				((TextView) child).setTextSize(14);
			}else if (child instanceof EditText){
				((EditText) child).setGravity(Gravity.CENTER | Gravity.BOTTOM);
				((EditText) child).setTextSize(14);
				((EditText) child).setInputType(InputType.TYPE_CLASS_NUMBER);
			}else if (child instanceof Spinner){
				LayoutUtil.setMargins((Spinner) child, 0, 0, 6, 0);
			}
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String itemCode=data.getStringExtra("itemCode");
                //String itemDescription=data.getStringExtra("itemDescription");
                Double ppPack=data.getDoubleExtra("ppPack",0.0);
                //Double ppUnit=data.getDoubleExtra("ppUnit",0.0);
                addCsItem (itemCode);
                refreshList();

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

	private void refreshList() {
		try {
			table.removeAllViews();
		} catch (Exception e) {
			e.printStackTrace();
		}
		updateList();
		if(!SUBMITTED) btAddSku.setEnabled(true);

	}
    private void addCsItem(String itemCode) {
        InventoryDbManager db = new InventoryDbManager(context);
        db.open();

        Inventory ini = new Inventory();

        ini.setINCode(csCode);
        ini.setItemCode(itemCode);
        ini.setPckg(Master.PACKS);
        ini.setQty(0);

        db.AddInventorySheetItem(ini);

        db.close();

    }

    TableRow selectedRow;
    final int DELETE_CALLSHEET_ITEM 	= 0;
    final int DELETE_CALLSHEET			= 1;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        selectedRow = (TableRow) v;

        menu.setHeaderIcon(R.drawable.delete32);
        menu.setHeaderTitle("Delete Inventory Sheet");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_delete_inventory, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem mItem) {
        String title;
        String message;

        TextView strSO = (TextView) selectedRow.getChildAt(0);
        TextView status = (TextView) selectedRow.getChildAt(1);
        TextView itemDescription = (TextView) selectedRow.getChildAt(2);


        Integer Id = Integer.valueOf(strSO.getText().toString());

        if (Integer.valueOf(status.getText().toString())!=0) {
            DialogManager.showAlertDialog(context,"Locked Callsheet!",
                    "This Inventory Sheet had already been transmitted to the central server and cannot be modified!", true);
            return false;
        }

        switch (mItem.getItemId()) {
            case R.id.delete_callsheet_item:

                title = "Delete Inventory Item";
                message = "Are you sure you want delete " + itemDescription.getText().toString() + "?";
                confirmDialog(title,  message, DELETE_CALLSHEET_ITEM,Id,csCode);
                return true;

            case R.id.delete_callsheet:
                title = "Delete Entire Inventory Sheet";
                message = "Are you sure you want delete the Inventory Sheet?";
                confirmDialog(title,  message, DELETE_CALLSHEET,Id,csCode);
                return true;


        }
        return false;
    }

    public void confirmDialog(String title, String message, Integer option,Integer id,String cscode) {

        final Integer Option = option;
        final Integer Id = id;
        final String csCode=cscode;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.delete32);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int which) {

                switch(Option) {
                    case DELETE_CALLSHEET_ITEM:
                        confirmedDeleteCallSheetItem(Id);
                        break;
                    case DELETE_CALLSHEET:
                        confirmedDeleteCallSheet(csCode);
                        break;
                }
                //Toast.makeText(getApplicationContext(), Title + " confirmed!" , Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    private void confirmedDeleteCallSheetItem(int Id) {
        InventoryDbManager db = new InventoryDbManager(this);
        db.open();
        db.delete(Id);
        db.close();

        refreshList();
    }


    private void confirmedDeleteCallSheet(String csCode) {
        InventoryDbManager db = new InventoryDbManager(this);
        db.open();
        db.deleteCallSheet(csCode);
        db.deleteCallSheetItems(csCode);
        db.close();

        finish();
    }

}
