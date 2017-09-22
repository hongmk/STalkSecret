package com.hongmk.stalksecret;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onMyPage(View view){
        intent = new Intent(HomeActivity.this, MypageActivity.class);
        startActivity(intent);
    }

    public void onNoti(View view){
        intent = new Intent(HomeActivity.this, NoticeActivity.class);
        startActivity(intent);
    }
}
