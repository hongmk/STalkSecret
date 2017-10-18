package com.hongmk.stalksecret;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {

    ListView listView = null;
    class Item {
        String ntTitle;
        String ntText;
        Item( String ntTitle, String ntText) {
            this.ntTitle = ntTitle;
            this.ntText = ntText;
        }
    }

    ArrayList<Item> itemList = new ArrayList<Item>();
    class ItemAdapter extends ArrayAdapter {
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.noti_item, null);
            }
            TextView ntText1View = (TextView)convertView.findViewById(R.id.ntTitle);
            TextView ntText2View = (TextView)convertView.findViewById(R.id.ntText);
            Item item = itemList.get(position);
            ntText1View.setText(item.ntTitle);
            ntText2View.setText(item.ntText);
            return convertView;
        }

        public ItemAdapter(@NonNull Context conText, @LayoutRes int resource, @NonNull List objects) {
            super(conText, resource, objects);
        }
    }
    ItemAdapter itemAdpater = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.notice_page_toolbar);
        toolbar.setTitle("알림"); //툴바 제목 표시여부
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.ntListView);
        itemList.add(new Item("ntTitle01", "ntText0101"));
        itemList.add(new Item("ntTitle02", "ntText0102"));
        itemList.add(new Item("ntTitle03", "ntText0103"));
        itemList.add(new Item("ntTitle04", "ntText0104"));
        itemList.add(new Item("ntTitle05", "ntText0105"));
        itemAdpater = new ItemAdapter(NoticeActivity.this, R.layout.my_comments_item, itemList);
        listView.setAdapter(itemAdpater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
