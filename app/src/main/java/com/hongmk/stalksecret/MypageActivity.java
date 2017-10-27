package com.hongmk.stalksecret;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

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

        Switch mypageSwitch = (Switch)findViewById(R.id.mypage_switch);
        mypageSwitch.setOnCheckedChangeListener( new switchListener() );

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

    //Switch 버튼의 체크상태를 받는 리스터
    public class switchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //알림수신 여부 user정보에 반영 통신

            if(isChecked == true) {
                Toast.makeText(MypageActivity.this, "알림수신 동의완료", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MypageActivity.this, "알림수신 거부완료", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
