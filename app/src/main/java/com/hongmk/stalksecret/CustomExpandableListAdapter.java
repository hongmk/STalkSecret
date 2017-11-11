package com.hongmk.stalksecret;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<ChildItem> childItem;

    public CustomExpandableListAdapter(Context context, ArrayList<ChildItem> item){
        mContext = context;
        childItem = item;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childItem.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return childItem.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group, null);
        } else {
            view = convertView;
        }
        TextView text = (TextView)view.findViewById(R.id.listTitle);
        text.setText("");

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        View view;
        if(convertView == null) {
            //view = getChildGenericView();
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expand_list_item, parent, false);
        } else {
            view = convertView;
        }

        TextView nicname = (TextView)view.findViewById(R.id.comment_child_nicname);
        TextView date = (TextView)view.findViewById(R.id.comment_child_date);
        TextView comment = (TextView)view.findViewById(R.id.comment_child_text);

        nicname.setText(childItem.get(childPosition).item_nicname);
        date.setText(childItem.get(childPosition).item_date);
        comment.setText(childItem.get(childPosition).item_text);

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}