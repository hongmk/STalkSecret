package com.hongmk.stalksecret.fragment;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hongmk.stalksecret.R;

import java.util.ArrayList;
import java.util.List;

public class HomeListItem extends AppCompatActivity {

    String row_id;
    String title;
    String text;

    //글의 row_id, 제목, 내용  -->추후 시간정보등도 추가필요
    HomeListItem(String row_id, String title, String text) {
        this.row_id = row_id;
        this.title = title;
        this.text = text;
    }

    ArrayList<HomeListItem> itemList = new ArrayList<HomeListItem>();

    class ItemAdapter extends ArrayAdapter {
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.home_list_item, null);
            }
            TextView rowIdText = (TextView)convertView.findViewById(R.id.home_item_row_id);
            TextView text1View = (TextView)convertView.findViewById(R.id.title);
            TextView text2View = (TextView)convertView.findViewById(R.id.text);
            HomeListItem item = itemList.get(position);
            rowIdText.setText(item.row_id);
            text1View.setText(item.title);
            text2View.setText(item.text);
            return convertView;
        }

        public ItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
        }
    }

    // 입력받은 숫자의 리스트생성
    public static ArrayList<HomeListItem> getList(int count, int position) {

        ArrayList<HomeListItem> itemList = new ArrayList<HomeListItem>();
        for(int i = 0; i< count; i++) {
            itemList.add(new HomeListItem(""+i, position+"_"+"Title"+i, "Text"+i));
        }

        return itemList;
    }

}