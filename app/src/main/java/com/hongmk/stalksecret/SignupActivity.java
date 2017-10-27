package com.hongmk.stalksecret;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_page_toolbar);
        toolbar.setTitle("회원가입"); //툴바 제목 표시여부
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
    public void verifyEmail(View view) {
        //이메일 인증 통신
        Toast.makeText(SignupActivity.this, "verifyEmail", Toast.LENGTH_SHORT).show();
    }

    public void verifyUserId(View view){
        //아이디 중복확인 통신
        Toast.makeText(SignupActivity.this, "verifyUserId", Toast.LENGTH_SHORT).show();
    }

    public void verifyNicname(View view){
        //닉네임 중복확인 통신
        Toast.makeText(SignupActivity.this, "verifyNicname", Toast.LENGTH_SHORT).show();
    }

    public void goBackSignin(View view){
        onBackPressed();
        finish();
    }

    public void completeSignup(View view){
        //회원가입 통신
        Toast.makeText(SignupActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
        onBackPressed();
        finish();

    }



}
