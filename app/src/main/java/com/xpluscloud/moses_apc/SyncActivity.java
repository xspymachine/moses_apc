	package com.xpluscloud.moses_apc;


    import android.app.ListActivity;
    import android.content.Context;
    import android.os.Bundle;
    import android.os.Environment;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ListView;

    import com.xpluscloud.moses_apc.dbase.DesContract;
    import com.xpluscloud.moses_apc.dbase.UtilDbManager;
    import com.xpluscloud.moses_apc.server.DownloadTask;
    import com.xpluscloud.moses_apc.util.DbUtil;
    import com.xpluscloud.moses_apc.util.DialogManager;
    import com.xpluscloud.moses_apc.util.Master;

    import org.json.JSONArray;

    import java.io.File;


    public class SyncActivity extends ListActivity {
        static final String[] SYNC_LIST =
                new String[] {
                    "Customer List",
                    "Coverage Plan",
//                    "Promo Checklist",
                    "Promo Brochures",
                    "Update Customersâ€™ A/R",
                    "Places",
                    "Competitors Data",
                    "Others"
                };

        Context context=SyncActivity.this;

        public final int CUSTOMER_LIST	= 0;
        public final int CPLAN			= 1;
//        public final int PCHECK         = 2;
        public final int PBROCHURE      = 2;
        public final int CLBAL          = 3;
        public final int PLACES         = 4;
        public final int CMP            = 5;
        public final int MISCS			= 6;


        public String devId;
        public String APIK;

        // Connection detector
        ConnectionDetector cd;
        View header;

        String baseUrl = "https://moses.xpluscloud.com/";

     // Alert dialog manager
       // NotificationsManager alert = new NotificationsManager();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Bundle b = getIntent().getExtras();
            devId = b.getString("devId");

            devId = DbUtil.getSetting(context, Master.DEVID);

//            devId = "353682111371769";
//            devId = "866222030538030";
//            devId = "354669101269581";
//            devId = "97dd3ba4ad67900c";
//            devId= "98c277105e3e1e21";
//            devId = "e93ef37f1b4996ef";
//            devId = "3df3536e41862d5f";  //apc
//            devId = "387402a70cb113b5";
            APIK= getResources().getString(R.string.apik);

     //add header to list

            ListView lv = getListView();
            LayoutInflater inflater = getLayoutInflater();
            header = inflater.inflate(R.layout.sync_header, (ViewGroup) findViewById(R.id.header_layout_root));
            lv.addHeaderView(header, null, false);

            setListAdapter(new SyncArrayAdapter(this, SYNC_LIST));

        }

        @Override
        protected void onListItemClick(ListView l, View v, final int position, long id) {


             cd = new ConnectionDetector(getApplicationContext());

                // Check if Internet present
                if (!cd.isOnline()) {
                    // Internet Connection is not present
                    DialogManager.showAlertDialog(SyncActivity.this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                    // stop executing code by return
                    return;
                }

//            if(NetworkUtils.isAvailableByPing("www.google.com")){
                switch(position-1) {

                    case CUSTOMER_LIST:
                        deleteTable("customers");
                        deleteTable("owner");
//                        deleteTable("cus_credit_limit");
                        downloadCustomers();
                        downloadOwner();
//                        downloadCustomersCLBAL();
                        break;
                    case CLBAL:
                        deleteTable("cus_credit_limit");
                        downloadCustomersCLBAL();
                        break;
                    case CPLAN:
                        deleteTable("coverageplans");
                        deleteTable("periods");
                        downloadCplan();
                        downloadPeriods();
                        break;
                    case MISCS:
                        deleteTable("items");
                        deleteTable("icategory");
                        deleteTable("isubcategory");
                        deleteTable("cus_datas"," where status NOT IN (11,12,13)");
                        deleteTable("checklists");
                        deleteTable("acct_types");
                        downloadItems();
                        downloadItemsCategory();
                        downloadItemsSubCategory();
                        downloadChecklists();
                        downloadAccountTypes();
                        downloadTerms();
                        downloadUnits();
                        break;
//                    case PCHECK:
//                        deleteTable("cus_datas"," where status=11");
//                        deleteTable("cus_datas"," where status=12");
//                        deleteTable("cus_datas"," where status=13");
//                        downloadPromo();
//                        downloadPromotions();
//                        downloadPromoCat();
//                        break;
//                    case PBROCHURE:
//                        deleteFiles();
//                        deleteTable("cus_datas"," where status=12");
//                        deleteTable("cus_datas"," where status=13");
//                        downloadPromotions();
//                        downloadPromoCat();
//                        break;
                    case PLACES:
                        deleteTable("refbrgy");
                        deleteTable("refcitymun");
                        deleteTable("refprovince");
                        deleteTable("refregion");
                        downloadBarangay();
                        downloadCitymun();
                        downloadProvince();
                        downloadRegion();
                        break;
                    case CMP:
                        deleteTable("cmpicategory");
                        deleteTable("cmpisubcategory");
                        deleteTable("cmpicompany");
                        deleteTable("citem");
                        downloadCmpList();
                        downloadItemsCategory2();
                        downloadItemsSubCategory2();
                        downloadItemsCompany();
                        break;
                    case PBROCHURE:
                        deleteTable("cus_datas"," where status=11");
                        deleteTable("cus_datas"," where status=12");
                        deleteTable("cus_datas"," where status=13");
                        downloadPromo();
                        downloadPromotions();
                        downloadPromoCat();
//                        deleteFiles();
                        downloadPromotionsFiles();
                        break;
                    default:
                        //
                        break;
                }
//            }else{
//                DialogManager.showAlertDialog(SyncActivity.this,
//                            "Internet Connection Error",
//                            "Please connect to a working Internet connection", false);
//                    return;
//            }
        }

        private void uploadCustomers(){

            String sourceTable = DesContract.Customer.TABLE_NAME;

            UtilDbManager db = new UtilDbManager(context);
            db.open();
            JSONArray newCustomers 	= new JSONArray();
            newCustomers = db.getJsonArray(sourceTable, "status=0");
            db.close();

            if(newCustomers.length()<=0) return;

            AsyncHttpPost asyncHttp = new AsyncHttpPost(context,newCustomers,devId,sourceTable,"");
            String url=context.getResources().getString(R.string.upload_customers);
            asyncHttp.execute(url);

        }

        private void downloadTerms(){
            String url = baseUrl+"download/terms";
            new DownloadTask(context,header,"Terms").execute(url,"cus_datas",devId,APIK);
        }
        private void downloadPromotions(){
            String url = baseUrl+"download/promotion2";
            new DownloadTask(context,header,"Promo Brochures").execute(url,"cus_datas",devId,APIK);
        }
        private void downloadPromotionsFiles(){
            String url = baseUrl+"download/promotion2_xxx";
            new DownloadTask(context,header,"Promo Brochures","BROCHURE").execute(url,"cus_datas",devId,APIK);
        }
        private void downloadPromoCat(){
            String url = baseUrl+"download/promo_cat";
            new DownloadTask(context,header,"Promo Categories").execute(url,"cus_datas",devId,APIK);
        }
        private void downloadPromo(){
            String url = baseUrl+"download/promo";
            new DownloadTask(context,header,"Promos").execute(url,"cus_datas",devId,APIK);
        }
        private void downloadUnits(){
            String url = baseUrl+"download/units";
            new DownloadTask(context,header,"Units").execute(url,"cus_datas",devId,APIK);
        }
        private void downloadAccountTypes(){
            String url = baseUrl+"download/accttype";
            new DownloadTask(context,header,"Account Types").execute(url,"acct_types",devId,APIK);
        }
        private void downloadCustomers() {		//customer_lpgia
//            String url = baseUrl+"download/customers_lpgia";
            String url = baseUrl+"download/customers_fd2";
            new DownloadTask(context,header,"Clients").execute(url,"customers",devId,APIK);
        }

        private void downloadItems() {
            deleteTable("items");
            String url = baseUrl+"download/items_shell2";
            new DownloadTask(context,header,"Items").execute(url,"items",devId,APIK);
        }

        private void downloadItemsCategory() {
//            deleteTable("items_category");
            String url = baseUrl+"download/items_category";
            new DownloadTask(context,header,"Items - Category").execute(url,"icategory",devId,APIK);
        }

        private void downloadItemsSubCategory() {
//            deleteTable("items_category");
            String url = baseUrl+"download/items_subcategory";
            new DownloadTask(context,header,"Items - Subcategory").execute(url,"isubcategory",devId,APIK);
        }

        private void downloadItemsCategory2() {
//            deleteTable("items_category");
            String url = baseUrl+"download/cmp_items_category";
            new DownloadTask(context,header,"Items - Category2").execute(url,"cmpicategory",devId,APIK);
        }

        private void downloadItemsSubCategory2() {
//            deleteTable("items_category");
            String url = baseUrl+"download/cmp_items_subcategory";
            new DownloadTask(context,header,"Items - Subcategory2").execute(url,"cmpisubcategory",devId,APIK);
        }
        private void downloadItemsCompany() {
//            deleteTable("items_category");
            String url = baseUrl+"download/cmp_items_company";
            new DownloadTask(context,header,"Items - Subcategory2").execute(url,"cmpicompany",devId,APIK);
        }

        private void downloadCplan() {
            String url = baseUrl+"download/coverageplan";
            new DownloadTask(context,header,"Coverage Plan").execute(url,"coverageplans",devId,APIK);
        }

        private void downloadPeriods() {
            String url = baseUrl+"download/period";
            new DownloadTask(context,header,"Coverage Plan (Periods)").execute(url,"periods",devId,APIK);
        }

        private void downloadCmpList(){
            deleteTable("citem");
            String url = baseUrl+"download/cmp_items_sfa";
            new DownloadTask(context,header,"Miscellaneous (Retail Items)").execute(url,"citem",devId,APIK);
        }

        private void downloadChecklists(){
            deleteTable("checklists");
            String url = baseUrl+"download/merch_checklist";
            new DownloadTask(context,header,"Checklist").execute(url,"checklists",devId,APIK);
        }

        private void downloadChecklists2(){
            deleteTable("call_checklist");
            String url = baseUrl+"download/call_checklist";
            new DownloadTask(context,header,"Compliance Checklist").execute(url,"call_checklist",devId,APIK);
        }

        private void downloadOwner(){
            String url = baseUrl+"download/cusowners_shell";
            new DownloadTask(context,header,"Clients Info (Owner)").execute(url,"owner",devId,APIK);
        }

        private void downloadGenProf(){
            String url = baseUrl+"download/cus_gp";
            new DownloadTask(context,header,"Clients Info(General Profile)").execute(url,DesContract.GeneralProfile.TABLE_NAME,devId,APIK);
        }

        private void downloadWorkWith(){
            deleteTable("work_with");
            String url = baseUrl+"download/work_with";
            new DownloadTask(context,header,"Miscellaneous (Work-with)").execute(url,"work_with",devId,APIK);
        }

        private void downloadCustomersCLBAL(){
            String url = baseUrl+"download/a_r";
            new DownloadTask(context,header,"Customers Credit Limit Balance").execute(url,"cus_credit_limit",devId,APIK);
        }

        private void downloadBarangay() {
            String url = baseUrl+"download/brgy";
            new DownloadTask(context,header,"Places Barangay").execute(url,"refbrgy",devId,APIK);
        }

        private void downloadCitymun() {
            String url = baseUrl+"download/city";
            new DownloadTask(context,header,"Places City/Municipality").execute(url,"refcitymun",devId,APIK);
        }

        private void downloadProvince() {
            String url = baseUrl+"download/province";
            new DownloadTask(context,header,"Places Province").execute(url,"refprovince",devId,APIK);
        }

        private void downloadRegion() {
            String url = baseUrl+"download/region";
            new DownloadTask(context,header,"Places Region").execute(url,"refregion",devId,APIK);
        }

        private void cusDataDelete(){
            UtilDbManager db = new UtilDbManager(context);
            db.open();
            db.deleteCusData();
            db.close();
        }
        private void deleteTable(String tableName){
            UtilDbManager db = new UtilDbManager(context);
            db.open();
            db.deleteTable(tableName);
            db.close();
        }
        private void deleteTable(String tableName,String where){
            UtilDbManager db = new UtilDbManager(context);
            db.open();
            db.deleteTable(tableName,where);
            db.close();
        }

        private void deleteFiles(){
//            File dir = new File(Environment.getExternalStorageDirectory().toString()+"/ShellPromo");
//            File dir = new File(context.getFilesDir().toString()+"/apc");
//            dir.delete();
//            if (dir.isDirectory())
//            {
//                String[] children = dir.list();
//                for (String child : children) {
//                    new File(dir, child).delete();
//                }
//            }
        }
    }
