package com.hongmk.stalksecret;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MypageActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_page_toolbar);
        toolbar.setTitle("마이페이지"); //툴바 제목 표시여부
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void editNicname(View view){
        intent = new Intent(MypageActivity.this, EditNicnameActivity.class);
        startActivity(intent);
    }

    public void editPassword(View view){
        intent = new Intent(MypageActivity.this, EditPasswordActivity.class);
        startActivity(intent);
    }

    public void getMyContents (View view){
        intent = new Intent(MypageActivity.this, MyContentsActivity.class);
        startActivity(intent);
    }

    public void getMyComments (View view){
        intent = new Intent(MypageActivity.this, MyCommentsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
