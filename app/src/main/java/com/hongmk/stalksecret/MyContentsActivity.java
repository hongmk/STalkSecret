package com.hongmk.stalksecret;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyContentsActivity extends AppCompatActivity {

    ListView listView = null;
    class Item {
        String title;
        String text;
        Item( String title, String text) {
            this.title = title;
            this.text = text;
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
                convertView = layoutInflater.inflate(R.layout.my_contents_item, null);
            }
            TextView text1View = (TextView)convertView.findViewById(R.id.title);
            TextView text2View = (TextView)convertView.findViewById(R.id.text);
            Item item = itemList.get(position);
            text1View.setText(item.title);
            text2View.setText(item.text);
            return convertView;
        }

        public ItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
        }
    }
    ItemAdapter itemAdpater = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contents);
        listView = (ListView)findViewById(R.id.listView);
        itemList.add(new Item("Title01", "Text0101"));
        itemList.add(new Item("Title02", "Text0102"));
        itemList.add(new Item("Title03", "Text0103"));
        itemList.add(new Item("Title04", "Text0104"));
        itemList.add(new Item("Title05", "Text0105"));
        itemAdpater = new ItemAdapter(MyContentsActivity.this, R.layout.my_contents_item,
                itemList);
        listView.setAdapter(itemAdpater);
    }
}
