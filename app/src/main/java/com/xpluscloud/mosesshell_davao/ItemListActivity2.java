package com.xpluscloud.mosesshell_davao;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.xpluscloud.mosesshell_davao.dbase.CompetitorDbManager2;
import com.xpluscloud.mosesshell_davao.dbase.ItemDbManager;
import com.xpluscloud.mosesshell_davao.getset.Item;
import com.xpluscloud.mosesshell_davao.util.Master;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemListActivity2 extends ListActivity {

    final int MODE_ADD = 0;

    //public String csCode;
    public String customerCode;
    public int option;
    int catid;
    int icatid;

    // Search EditText
    EditText inputSearch;
    ItemListAdapter adapter;

    Context context;

    Item selectedRow;

    private void updateList() {
        CompetitorDbManager2 db = new CompetitorDbManager2(this);
        db.open();
        List<Item> ItemList = db.getList1234(catid,icatid);
        adapter = new ItemListAdapter(this, ItemList);
        setListAdapter(adapter);

        db.close();
    }

    private void refreshList() {
        CompetitorDbManager2 db = new CompetitorDbManager2(this);
        db.open();
        List<Item> list = db.getList1234(catid,icatid);
        adapter.setValue(list);
        db.close();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        context = ItemListActivity2.this;

        Bundle b = getIntent().getExtras();
        //csCode			=b.getString("csCode");
        customerCode	=b.getString("csCode");
        option	=b.getInt("option");
        catid	=b.getInt("catid");
        icatid	=b.getInt("icatid");

        inputSearch = (EditText) findViewById(R.id.inputSearch);

        updateList();

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                ItemListActivity2.this.adapter.getFilter().filter(inputSearch.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        ListView listView = getListView();
        registerForContextMenu(listView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }




    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        super.onListItemClick(parent, v, position, id);

        selectedRow = (Item) getListAdapter().getItem(position);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("itemCode",selectedRow.getItemCode());
        returnIntent.putExtra("itemDescription",selectedRow.getDescription());
        returnIntent.putExtra("ppPack",selectedRow.getPricePerPack());
        returnIntent.putExtra("ppUnit",selectedRow.getPricePerUnit());

        setResult(RESULT_OK,returnIntent);
        finish();

    }

    private static class ItemListAdapter extends BaseAdapter implements Filterable {
        private LayoutInflater inflater;
        private List<Item> originalList;
        private List<Item> filteredList;

        public ItemListAdapter(Context context, List<Item> list) {
            inflater = LayoutInflater.from(context);
            originalList = list;
            filteredList = list;
        }

        public void setValue(List<Item> list) {
            this.filteredList = list;
            this.notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return filteredList.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return filteredList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.list_item, null);

                holder = new ViewHolder();
                holder.description = (TextView) convertView.findViewById(R.id.description);
                holder.itemcode = (TextView) convertView.findViewById(R.id.itemcode);
                holder.prices = (TextView) convertView.findViewById(R.id.prices);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.description.setText(filteredList.get(position).getDescription());
            holder.itemcode.setText("("+filteredList.get(position).getItemCode() + ")");
            holder.prices.setText("");

            return convertView;
        }

        private static class ViewHolder {
            public TextView description;
            public TextView itemcode;
            public TextView prices;
        }


        @Override
        public Filter getFilter() {
            return new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredList = (List<Item>) results.values;
                    ItemListAdapter.this.notifyDataSetChanged();
                }


                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    // We implement here the filter logic
                    if (constraint == null || constraint.length() == 0) {
                        // No filter implemented we return all the list
                        results.values = originalList;
                        results.count = originalList.size();
                        return results;

                    }
                    else {
                        // We perform filtering operation
                        List<Item> nItemList = new ArrayList<Item>();
                        for (Item c : originalList) {
                            if (c.getDescription().toUpperCase(Locale.US).contains(constraint.toString().toUpperCase())
                                    || c.getItemCode().toUpperCase(Locale.US).contains(constraint.toString().toUpperCase()) )
                                nItemList.add(c);
                        }
                        results.values = nItemList;
                        results.count = nItemList.size();
                    }
                    return results;
                }

            };
        }
    }

}



