package com.hongmk.stalksecret;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences restful_ip = getSharedPreferences("restful_ip", MODE_PRIVATE);
        SharedPreferences.Editor restful_ip_editor = restful_ip.edit();
        restful_ip_editor.putString("restful_ip", "http://172.16.2.8:52275");
        restful_ip_editor.commit();

        SharedPreferences restful_ip_pref1 = getSharedPreferences("restful_ip", Activity.MODE_PRIVATE);
        String restful_ip1 = restful_ip_pref1.getString("restful_ip", "");

        Toast.makeText(MainActivity.this, restful_ip1, Toast.LENGTH_SHORT).show();

        SharedPreferences angular_ip = getSharedPreferences("angular_ip", MODE_PRIVATE);
        SharedPreferences.Editor angular_ip_editor = angular_ip.edit();
        angular_ip_editor.putString("angular_ip", "http://172.16.2.8:9000");
        angular_ip_editor.commit();

        //2초뒤 핸들러 호출
        handler.sendEmptyMessageDelayed(0, 1000*2);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == 0) {
                //로그인 화면으로 전환
                intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
}
