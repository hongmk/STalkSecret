package com.hongmk.stalksecret;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void goBackSignin(View view){
        finish();
        onBackPressed();
    }

    public void signupDone(View view){
        Toast.makeText(this, "signupDone", Toast.LENGTH_SHORT).show();
    }
}
