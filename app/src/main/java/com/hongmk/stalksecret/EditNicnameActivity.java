package com.hongmk.stalksecret;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class EditNicnameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nicname);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_nickname_toolbar);
        toolbar.setTitle("닉네임변경"); //툴바 제목 표시여부
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void verifyEditNicname(View view){
        //닉네임 검증 통신(signup페이지와 동일로직수행)
        Toast.makeText(EditNicnameActivity.this, "verifyEditNicname", Toast.LENGTH_SHORT).show();
    }

    public void goMypage(View view){
        onBackPressed();
        finish();
    }

    public void completeEditNicname(View view){
        //닉네임 수정 통신
        Toast.makeText(EditNicnameActivity.this, "닉네임변경 완료", Toast.LENGTH_SHORT).show();
        onBackPressed();
        finish();
    }
}
