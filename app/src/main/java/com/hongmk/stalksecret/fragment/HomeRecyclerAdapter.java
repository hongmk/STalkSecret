package com.hongmk.stalksecret.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.hongmk.stalksecret.R;

import java.util.ArrayList;

/**
 * Created by HongMK on 2017-10-07.
 */

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.HomeRecyclerHolder> {

    private Context context;
    private ArrayList<HomeListItem> list = new ArrayList<HomeListItem>();
    private int lastPosition = -1;

    public HomeRecyclerAdapter(Context context, ArrayList<HomeListItem> list) {
        this.context = context;
        this.list = list;
    }

    // ViewHolder 생성
    // row layout을 화면에 뿌려주고 holder에 연결
    @Override
    public HomeRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_item, parent, false);
        HomeRecyclerHolder holder = new HomeRecyclerHolder(view);
        return holder;
    }

    //만들어진 ViewHolder에 data 삽입 ListView의 getView와 동일

    @Override
    public void onBindViewHolder(HomeRecyclerHolder holder, int position) {
        // 각 위치에 문자열 세팅
        int itemposition = position;
        holder.text1View.setText(list.get(itemposition).title);
        holder.text2View.setText(list.get(itemposition).text);
        Log.e("[LOG]", "onBindViewHolder" + itemposition);

    }

    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        return list.size(); // RecyclerView의 size return
    }

    private HomeListItem getItem(int position) {
        int listSize = 0;

        if (list.size() > position) {
            return list.get(position);
        }
        return null;
    }

    // ViewHolder는 하나의 View를 보존하는 역할을 한다
    public class HomeRecyclerHolder extends RecyclerView.ViewHolder{
        public TextView text1View;
        public TextView text2View;

        public HomeRecyclerHolder(View view){
            super(view);
            text1View = (TextView)view.findViewById(R.id.title);
            text2View = (TextView)view.findViewById(R.id.text);
        }
    }

    // 새로 보여지는 뷰라면 애니메이션을 해줍니다
    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        { Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation); lastPosition = position;
        }
    }



}
