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

public class MyCommentsActivity extends AppCompatActivity {

    ListView listView = null;
    class Item {
        String cmTitle;
        String cmText;
        Item( String cmTitle, String cmText) {
            this.cmTitle = cmTitle;
            this.cmText = cmText;
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
                convertView = layoutInflater.inflate(R.layout.my_comments_item, null);
            }
            TextView cmText1View = (TextView)convertView.findViewById(R.id.cmTitle);
            TextView cmText2View = (TextView)convertView.findViewById(R.id.cmText);
            Item item = itemList.get(position);
            cmText1View.setText(item.cmTitle);
            cmText2View.setText(item.cmText);
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
        setContentView(R.layout.activity_my_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_comments_toolbar);
        toolbar.setTitle("내댓글 관리"); //툴바 제목 표시여부
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.cmListView);
        itemList.add(new Item("cmTitle01", "cmText0101"));
        itemList.add(new Item("cmTitle02", "cmText0102"));
        itemList.add(new Item("cmTitle03", "cmText0103"));
        itemList.add(new Item("cmTitle04", "cmText0104"));
        itemList.add(new Item("cmTitle05", "cmText0105"));
        itemAdpater = new ItemAdapter(MyCommentsActivity.this, R.layout.my_comments_item, itemList);
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
