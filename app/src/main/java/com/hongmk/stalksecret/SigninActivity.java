package com.hongmk.stalksecret;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SigninActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }

    public void onLogin(View view){
        intent = new Intent(SigninActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void goSignupPage(View view){
        intent = new Intent(SigninActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}
