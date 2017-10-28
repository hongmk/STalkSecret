package com.hongmk.stalksecret;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class EditPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_password_toolbar);
        toolbar.setTitle("비밀번호변경"); //툴바 제목 표시여부
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

    public void verifyEditPassword(View view){
        //비밀번호검증로직 (1. 변경 후 암호 2개가 일치하는지, 2.변경 전 암호가 맞는지) -> 둘 다 확인해서 한번에 결과리턴하기
        Toast.makeText(EditPasswordActivity.this, "verifyEditPassword", Toast.LENGTH_SHORT).show();
    }

    public void goMypage(View view){
        onBackPressed();
        finish();
    }

    public void completeEditPassword(View view){
        //비밀번호 수정 통신
        Toast.makeText(EditPasswordActivity.this, "암호변경 완료", Toast.LENGTH_SHORT).show();
        onBackPressed();
        finish();
    }
}
