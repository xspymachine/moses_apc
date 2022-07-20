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
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.xpluscloud.moses_apc.dbase.CallSheetDbManager;
import com.xpluscloud.moses_apc.dbase.CompetitorDbManager2;
import com.xpluscloud.moses_apc.getset.CmpItem;
import com.xpluscloud.moses_apc.getset.Customer;
import com.xpluscloud.moses_apc.getset.Inventory;
import com.xpluscloud.moses_apc.util.DateUtil;
import com.xpluscloud.moses_apc.util.DbUtil;
import com.xpluscloud.moses_apc.util.DialogManager;
import com.xpluscloud.moses_apc.util.LayoutUtil;
import com.xpluscloud.moses_apc.util.Master;
import com.xpluscloud.moses_apc.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class CompetitorActivity2 extends AppCompatActivity implements ItemOptionDialogFragment2.OptionDialogListener {
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
        context = CompetitorActivity2.this;

        Bundle extras = getIntent().getExtras();
        devId 			= extras.getString("devId");
        csCode			= extras.getString("csCode");
        cCashSales      = extras.getInt("cCashSales");
        cCode           = extras.getString("customerCode");
        cName			= extras.getString("customerName");
        cAddress		= extras.getString("customerAddress");
        tvDate			= (TextView) findViewById(R.id.tvDate);

        if (csCode==null || csCode.equals("")) mode = MODE_ADD;
        else mode = MODE_EDIT;

        btAddSku 		= (Button) findViewById(R.id.btAddSku);
        table = (TableLayout) findViewById(R.id.callsheetitem_table);
        table.setStretchAllColumns(true);
        etRemarks		= (EditText) findViewById(R.id.etRemarks);
        etINNo			= (EditText) findViewById(R.id.etSONo);
        tvCname 		= (TextView) findViewById(R.id.tvCName);
        tvCaddress 		= (TextView) findViewById(R.id.tvAddress);

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
                CompetitorDbManager2 db = new CompetitorDbManager2(context);
                db.open();
                int qty = db.getTotalPrice(csCode);
                db.close();
                if(qty>0)  {
//                    submitCallSheet();
                    sendCallSheet();
                }
                else {
                    DialogManager.showAlertDialog(context, "No Actual Competitor Price!", "Enter at least 1 item with actual Competitor Price.", null);
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
        CompetitorDbManager2 db = new CompetitorDbManager2(context);
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

            CompetitorDbManager2 db = new CompetitorDbManager2(context);
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

//        createEtDialog();
        createDialog();

//        DialogFragment newFragment = new ItemOptionDialogFragment2();//("title",R.array.callsheet_options);
//        Bundle args = new Bundle();
//        args.putInt("opt", opt);
//        args.putInt("catid", catid);
//        newFragment.setArguments(args);
//        newFragment.show(getFragmentManager(), "AddItems");
    }

    String forOthers = "";
    @Override
    public void onSelectOption(Integer position, String strValue, int opt) {
        CompetitorDbManager2 db = new CompetitorDbManager2(context);
        db.open();
        int _id = db.getCatId(strValue,1);
        if(opt==1){
            catid = _id;
            addItems(2);
            if(strValue.contains("Others")) forOthers = strValue;
        }
        else {
            icatid=db.getCatId(strValue,2);
//            if(forOthers.isEmpty()) selectSingleItem(catid,icatid);
//            else createEtDialog();
            createDialog();
            forOthers="";
        }


        db.close();
    }

    private void createDialog(){
        AlertDialog.Builder d = new AlertDialog.Builder(context)
                .setTitle("Specify the item name.")
                .setMessage("Complete the data and please don't use special characters.");
        d.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        final View dv = inflater.inflate(R.layout.dialog_custom_cmp, null);

        final Spinner spCat = dv.findViewById(R.id.cmp_category);
        final Spinner spSubCat = dv.findViewById(R.id.cmp_subcategory);
        final AutoCompleteTextView atCompany = dv.findViewById(R.id.cmp_company);
        final AutoCompleteTextView atBrand = dv.findViewById(R.id.cmp_brand);
        final Spinner spPackaging = dv.findViewById(R.id.cmp_packaging);
        final EditText etPPPack = dv.findViewById(R.id.cmp_priceperpack);
        final Spinner spUOM = dv.findViewById(R.id.cmp_uom);
        final EditText etQPPack = dv.findViewById(R.id.cmp_quantityperpack);
        final EditText etTDPrice = dv.findViewById(R.id.cmp_transfer_price);
        final EditText etSRP = dv.findViewById(R.id.cmp_srp);
        Button btCancel = dv.findViewById(R.id.cmp_cancel);
        Button btSave = dv.findViewById(R.id.cmp_save);

        ArrayAdapter catAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, populateSpinner(1,0));
        spCat.setAdapter(catAdapter);
        spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter subcatAdapter = new ArrayAdapter(context,
                        android.R.layout.simple_spinner_dropdown_item, populateSpinner(2,getIdCategory(spCat.getSelectedItem().toString(),1)));
                spSubCat.setAdapter(subcatAdapter);
                ArrayAdapter companyAdapter = new ArrayAdapter(context,
                        android.R.layout.simple_spinner_dropdown_item, populateSpinner(3,getIdCategory(spCat.getSelectedItem().toString(),1)));
                atCompany.setAdapter(companyAdapter);
                ArrayAdapter brandAdapter = new ArrayAdapter(context,
                        android.R.layout.simple_spinner_dropdown_item, populateSpinner(4,getIdCategory(spCat.getSelectedItem().toString(),1)));
                atBrand.setAdapter(brandAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        d.setView(dv);
        final AlertDialog ad = d.show();

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> strValue = new ArrayList<>();
                strValue.add(spCat.getSelectedItem().toString()); //category
                strValue.add(spSubCat.getSelectedItem().toString()); //subcategory
                strValue.add(atCompany.getText().toString()); //company
                strValue.add(atBrand.getText().toString()); //brand
                strValue.add(spPackaging.getSelectedItem().toString()); //packaging
                strValue.add(etPPPack.getText().toString()); //price per pack
                strValue.add(spUOM.getSelectedItem().toString()); //uom
                strValue.add(etQPPack.getText().toString()); //quantity per pack
                strValue.add(etTDPrice.getText().toString()); //transfer (dealer) price
                strValue.add(etSRP.getText().toString()); //SRP

                int flag = 0;
                for (int i = 0; i < strValue.size(); i++) {
                    if (strValue.get(i).isEmpty()) {
                        flag = 1;
                        break;
                    }
                }

                if (flag == 1) {
                    DbUtil.makeToast(LayoutInflater.from(context), "Please complete the data needed.", context, null, 1);
                } else {
                    CompetitorActivity2.this.addToItems2(strValue);
                    ad.dismiss();
                }
            }
        });
    }

    private ArrayList<String> getCmpItem(String cat, String subcat, String brand){
        CompetitorDbManager2 db = new CompetitorDbManager2(context);
        db.open();
        ArrayList<String> items = db.getCmpFilterList(cat,subcat,brand);
        db.close();


        return items;
    }

    private ArrayList<String> populateSpinner(int opt, int catid){
        CompetitorDbManager2 db = new CompetitorDbManager2(context);
        db.open();
        ArrayList<String> category = db.getCategoryList(opt,catid);
        db.close();

        return category;
    }

    private void addToItems2(ArrayList<String> strvalue){
//        String itemName = brand+" - "+variant;
        CompetitorDbManager2 db = new CompetitorDbManager2(context);
        db.open();

        int category_id = getIdCategory(strvalue.get(0), 1);
        int subcategory_id = getIdCategory(strvalue.get(1), 2);
//        if(getIdCategory(brand,2)==0){
//            icatid = (int) db.addcatsubcat(brand,2);
//        }
//        if(getIdCategory(variant,1)==0){
//            catid = (int) db.addcatsubcat(brand,1);
//        }

        String itemcode = StringUtil.randomText(20);
        CmpItem i = new CmpItem();
        i.setItemCode(itemcode);
        i.setDescription(strvalue.get(3));
        i.setCategory_id(category_id);
        i.setSubcategory_id(subcategory_id);
        i.setBpname(strvalue.get(3));
        i.setStatus(1);

        String cmpItemcode = db.getcmpItemcode(strvalue.get(3));
        if(cmpItemcode.isEmpty()) cmpItemcode = db.getcmpItemcode(strvalue.get(3));
        if(cmpItemcode.isEmpty()){
            db.AddOtherItem(i);
            addCsItem(itemcode,strvalue);
        }
        else addCsItem(cmpItemcode,strvalue);
        refreshList();
        db.close();
    }

    private int getIdCategory(String str, int opt){
        int id = 0;
        CompetitorDbManager2 db = new CompetitorDbManager2(context);
        db.open();
        id = db.getCatId(str,opt);
        db.close();
        return id;
    }
    private String getStrCategory(int id, int opt){
        String cat = "";
        CompetitorDbManager2 db = new CompetitorDbManager2(context);
        db.open();
        cat = db.getCatStr(id,opt);
        db.close();
        return cat;
    }

    private void selectSingleItem(int catid, int icatid) {
        Intent intent = new Intent(context, ItemListActivity2.class);
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

        CompetitorDbManager2 db = new CompetitorDbManager2(this);
        db.open();

        Inventory cs = db.getInfo(csCode);
        List<Inventory> csi = db.getList(csCode);
        if(csi.size() < 1){
            DialogManager.showAlertDialog(CompetitorActivity2.this,
                    "Minimum Competitor",
                    "Please input at least 1 competitor prices.", false);
            return;
        }else if(etRemarks.getText().toString().isEmpty()){
            DialogManager.showAlertDialog(CompetitorActivity2.this,
                    "Volume per month",
                    "What is the volume/month in bags or kg or packs?", false);
            return;
        }

        String message="";

        devId = DbUtil.getSetting(context, Master.DEVID);

        message = Master.CMD_CMP6 				+ " " +
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


        for (Inventory item : csi) 	{
            if(item.getStatus()==0 && item.getPrice()>0) {
                message = Master.CMD_CMPI7 + " " +
                        devId 					+ ";" +
                        item.getINCode() 		+ ";" +
                        item.getItemCode()		+ ";" +
                        item.getPckg() 			+ ";" +
                        item.getDescription()	+ ";" +
                        item.getPrice()			+ ";" +
                        item.getId()			+ ";" +
                        item.getSubcategory()	+ ";" +
                        item.getCategory()		+ ";" +
                        item.getCompany();

                DbUtil.saveMsg(context,DbUtil.getGateway(context), message);

                db.updateStatus(item.getId(), 1); //1=Transmitted

            }
        }

        db.close();


       Toast.makeText(getApplicationContext(), "Competitor Pricing has been submitted to the central server!" , Toast.LENGTH_SHORT).show();

        finish();

    }

    private void updateList() {

        CompetitorDbManager2 db = new CompetitorDbManager2(this);

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
        hOrderQty.setText("Dealer Price");
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
            String desc = item.getDescription()+"\n";
            if(item.getDescription().contains("_")) desc = item.getDescription().replace("_"," ")+"\n";
            itemDescription.setText(desc);
            itemDescription.setLayoutParams(new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            itemDescription.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            itemDescription.setSingleLine(false);
            itemDescription.setMaxLines(10);
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
            String[] pckg_opt = getResources().getStringArray(R.array.uom_array);

            @SuppressWarnings({ "unchecked", "rawtypes" })
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                    R.layout.csi_spinner_phone, pckg_opt);
            etPckg.setAdapter(spinnerArrayAdapter);
            int spinnerPosition2 = spinnerArrayAdapter.getPosition(pckg);
            etPckg.setSelection(spinnerPosition2);
//            etPckg.setSelection(0);
//			if(pckg.equalsIgnoreCase(Master.PACKS)) {
//				etPckg.setSelection(Master.PCKG_PACK);
//			}
//			else etPckg.setSelection(Master.PCKG_UNIT);
            etPckg.setEnabled(false);
            etPckg.setId(SP_PCKG);
            tr.addView(etPckg);

            //Actual Order  Column
            final EditText orderqty = new EditText(this);
            orderqty.setText(String.valueOf(item.getPrice()));
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
            orderqty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            tr.addView(orderqty);

            orderqty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    String qtyString = orderqty.getText().toString().trim();
                    Float qty = qtyString.equals("") ? 0:Float.valueOf(qtyString);

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
                Float quantity = qtyString.equals("") ? 0:Float.valueOf(qtyString);


                if(!Init){
                    //Update CallSheetItem OrderQTy
                    TextView csiId = view.findViewById(TV_CSI_ID);
                    //Save CS_ITEM
                    Inventory csi = new Inventory();

                    csi.setId(Integer.valueOf(csiId.getText().toString()));
                    csi.setPrice(quantity);

                    CompetitorDbManager2 db = new CompetitorDbManager2(context);
                    db.open();
                    db.UpdateOrderPrice(csi);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String itemCode = data.getStringExtra("itemCode");
                //String itemDescription=data.getStringExtra("itemDescription");
                Double ppPack = data.getDoubleExtra("ppPack", 0.0);
                //Double ppUnit=data.getDoubleExtra("ppUnit",0.0);
//                addCsItem(itemCode, "" + ppPack, Master.UNIT);
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
    private void addCsItem(String itemCode, ArrayList<String> s) {
        CompetitorDbManager2 db = new CompetitorDbManager2(context);
        db.open();

        Inventory ini = new Inventory();

        ini.setINCode(csCode);
        ini.setItemCode(itemCode);
        ini.setPckg(s.get(4));
        ini.setPriceperpack(Float.parseFloat(s.get(5)));
        ini.setUom(s.get(6));
        ini.setQty(Integer.parseInt(s.get(7)));
        ini.setPrice(Float.parseFloat(s.get(8)));
        ini.setSrp(Float.parseFloat(s.get(9)));
        ini.setCategory(s.get(0));
        ini.setSubcategory(s.get(1));
        ini.setCompany(s.get(2));

        db.AddInventorySheetItem(ini);

        db.close();

    }

    private void getSignature() {
        Intent intent = new Intent(context, GetSignatureActivity.class);
        Bundle b = new Bundle();

        b.putString("devId", devId);
        b.putString("txNo", "Inventory");
        b.putString("customerCode", cCode);
        b.putString("customerName", cName);
        b.putString("customerAddress", cAddress);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.history:
                Intent intent = new Intent(context, CompetitorActivityList2.class);
                Bundle b = new Bundle();

                b.putString("devId", devId);
                b.putString("ccode", null);
                intent.putExtras(b);
                startActivity(intent);
                break;

            case R.id.delete:
                //Log.w("Outbox","Aabout to empty outbox!");
//                confirmDialog("Delete Sales Order!", "Are you sure you want to delete this sales order and all of the items?", 1,0);
                break;

        }
        return true;
    }


    TableRow selectedRow;
    final int DELETE_CALLSHEET_ITEM 	= 0;
    final int DELETE_CALLSHEET			= 1;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        selectedRow = (TableRow) v;

        menu.setHeaderIcon(R.drawable.delete32);
        menu.setHeaderTitle("Delete Competitor Sheet");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_delete_competitor, menu);

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
                    "This Competitor Sheet had already been transmitted to the central server and cannot be modified!", true);
            return false;
        }

        switch (mItem.getItemId()) {
            case R.id.delete_callsheet_item:

                title = "Delete Competitor Item";
                message = "Are you sure you want delete " + itemDescription.getText().toString() + "?";
                confirmDialog(title,  message, DELETE_CALLSHEET_ITEM,Id,csCode);
                return true;

            case R.id.delete_callsheet:
                title = "Delete Entire Competitor Sheet";
                message = "Are you sure you want delete the Competitor Sheet?";
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
        CompetitorDbManager2 db = new CompetitorDbManager2(this);
        db.open();
        db.delete(Id);
        db.close();

        refreshList();
    }


    private void confirmedDeleteCallSheet(String csCode) {
        CompetitorDbManager2 db = new CompetitorDbManager2(this);
        db.open();
        db.deleteCallSheet(csCode);
        db.deleteCallSheetItems(csCode);
        db.close();

        finish();
    }

}
