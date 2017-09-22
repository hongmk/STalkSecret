package com.hongmk.stalksecret;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MypageActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
    }

    public void onEditNicname(View view){
        intent = new Intent(MypageActivity.this, EditNicnameActivity.class);
        startActivity(intent);
    }

    public void onEditPw(View view){
        intent = new Intent(MypageActivity.this, EditPasswordActivity.class);
        startActivity(intent);
    }

    public void onMyContents (View view){
        intent = new Intent(MypageActivity.this, MyContentsActivity.class);
        startActivity(intent);
    }

    public void onMyComments (View view){
        intent = new Intent(MypageActivity.this, MyCommentsActivity.class);
        startActivity(intent);
    }

    public void onSetting (View view){
        intent = new Intent(MypageActivity.this, SettingActivity.class);
        startActivity(intent);
    }
}
