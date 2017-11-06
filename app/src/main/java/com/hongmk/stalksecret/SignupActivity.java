package com.hongmk.stalksecret;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class SignupActivity extends AppCompatActivity {
    Intent intent;
    //1: 메일인증, 2:ID 인증, 3:닉네임인증
    private int verify_g;
    private int email_verify;
    private int nicname_verify;
    private int user_id_verify;
    private int password_verify;
    private int user_dept;
    private String phoneNumber ="";
    Button compelete_button;
    EditText password ;
    EditText passwordRe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_page_toolbar);
        toolbar.setTitle("회원가입"); //툴바 제목 표시여부
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        compelete_button =  (Button)findViewById(R.id.signup_complete_button);
        compelete_button.setEnabled(false);
        compelete_button.setTextColor(getResources().getColor(R.color.hintColor));

        password = (EditText) findViewById(R.id.signup_password);
        passwordRe = (EditText) findViewById(R.id.signup_passwor_re);

        password.addTextChangedListener(new PasswordWatcher());
        passwordRe.addTextChangedListener(new PasswordWatcher());


    }

    //비밀번호, 재입력 비밀번호 일치여부 검증
    public class PasswordWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

                // Checking editable.hashCode() to understand which edittext is using right now
             if (password.getText().hashCode() == editable.hashCode()){
                    if(password.getText().toString().equals(passwordRe.getText().toString()) != true) {
                        password.setTextColor(getResources().getColor(R.color.darkred));
                        passwordRe.setTextColor(getResources().getColor(R.color.darkred));
                        password_verify = 0;
                        compelete_button.setEnabled(false);
                        compelete_button.setTextColor(getResources().getColor(R.color.hintColor));
                    } else {
                        password.setTextColor(getResources().getColor(R.color.facebook_brand_color));
                        passwordRe.setTextColor(getResources().getColor(R.color.facebook_brand_color));
                        password_verify = 1;
                        if(email_verify ==1 && user_id_verify == 1 && nicname_verify == 1 && password_verify == 1) {
                            compelete_button.setEnabled(true);
                            compelete_button.setTextColor(getResources().getColor(R.color.white));
                        }
                    }
             }
             else if (passwordRe.getText().hashCode() == editable.hashCode()){
                if(passwordRe.getText().toString().equals(password.getText().toString()) != true) {
                    password.setTextColor(getResources().getColor(R.color.darkred));
                    passwordRe.setTextColor(getResources().getColor(R.color.darkred));
                    password_verify = 0 ;
                    compelete_button.setEnabled(false);
                    compelete_button.setTextColor(getResources().getColor(R.color.hintColor));
                } else {
                    password.setTextColor(getResources().getColor(R.color.facebook_brand_color));
                    passwordRe.setTextColor(getResources().getColor(R.color.facebook_brand_color));
                    password_verify = 1;
                    if(email_verify ==1 && user_id_verify == 1 && nicname_verify == 1 && password_verify == 1) {
                        compelete_button.setEnabled(true);
                        compelete_button.setTextColor(getResources().getColor(R.color.white));
                    }

                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void verifyEmail(View view) {
        verify_g = 1;
        //이메일 인증 통신
        //http://127.0.0.1:52275/users/auth/officemail?officemail=testmail2@shinhan.com&phonenumber=01011112222
        EditText email = (EditText) findViewById(R.id.signup_email);
        TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        try {
            if (telephony.getLine1Number() != null) {
                phoneNumber = telephony.getLine1Number();
                phoneNumber = phoneNumber.replace("+82", "0");
            }
            //Toast.makeText(SignupActivity.this, phoneNumber, Toast.LENGTH_SHORT).show();
            //Log.i("[HP=]", phoneNumber);
        } catch(Exception e) {
            e.printStackTrace();
        }
       new Verify().execute("http://192.168.0.4:52275/users/auth/officemail?officemail="+email.getText().toString()+"&phonenumber="+phoneNumber);

    }

    public void verifyUserId(View view){
        verify_g = 2;
        EditText user_id = (EditText) findViewById(R.id.signup_user_id);
        //아이디 중복확인 통신
        new Verify().execute("http://192.168.0.4:52275/users/auth/user_id?user_id="+user_id.getText().toString());
        //Toast.makeText(SignupActivity.this, "verifyUserId", Toast.LENGTH_SHORT).show();
    }

    public void verifyNicname(View view){
        verify_g = 3;
        EditText nicname = (EditText) findViewById(R.id.signup_nicname);
        //닉네임 중복확인 통신
        new Verify().execute("http://192.168.0.4:52275/users/auth/nicname?nicname="+nicname.getText().toString());
        //Toast.makeText(SignupActivity.this, "verifyNicname", Toast.LENGTH_SHORT).show();
    }

    public void goBackSignin(View view){
        onBackPressed();
        finish();
    }

    public void completeSignup(View view){
        //회원가입 통신
        EditText user_id = (EditText) findViewById(R.id.signup_user_id);
        EditText nicname = (EditText) findViewById(R.id.signup_nicname);
        EditText officemail = (EditText) findViewById(R.id.signup_email);


        if(password.getText().toString().equals(passwordRe.getText().toString()) != true) {
            Toast.makeText(SignupActivity.this, "첫번쨰 입력한 비밀번호가 두번쨰와 다릅니다. 다시확인해시기바랍니다.", Toast.LENGTH_SHORT).show();
        } else {
            new Signup().execute("http://192.168.0.4:52275/users",
                    user_id.getText().toString(),
                    nicname.getText().toString(),
                    password.getText().toString(),
                    officemail.getText().toString(),
                    phoneNumber,
                    ""+user_dept);

        }
    }

    class Verify extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(SignupActivity.this);

        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                       conn.setRequestMethod("GET");
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(conn.getInputStream()));
                        String line = null;
                        while(true) {
                               line = reader.readLine();
                                if (line == null) break;
                                output.append(line);
                            }
                        reader.close();
                        conn.disconnect();
                    }
            } catch (Exception e) { e.printStackTrace(); }
            return output.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("인증중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//인증 성공
                     switch (verify_g) {
                         case 1:
                             email_verify = 1 ;
                             user_dept = json.getInt("dept");
                             Toast.makeText(SignupActivity.this, "메일 인증완료", Toast.LENGTH_SHORT).show();
                             if(email_verify ==1 && user_id_verify == 1 && nicname_verify == 1 && password_verify == 1) {
                                 compelete_button.setEnabled(true);
                                 compelete_button.setTextColor(getResources().getColor(R.color.white));
                             }
                             break;
                         case 2:
                             user_id_verify = 1 ;
                             Toast.makeText(SignupActivity.this, "ID 인증완료", Toast.LENGTH_SHORT).show();
                             if(email_verify ==1 && user_id_verify == 1 && nicname_verify == 1 && password_verify == 1 ) {
                                 compelete_button.setEnabled(true);
                                 compelete_button.setTextColor(getResources().getColor(R.color.white));
                             }
                             break;
                         case 3:
                             nicname_verify = 1;
                             Toast.makeText(SignupActivity.this, "닉네임 인증완료", Toast.LENGTH_SHORT).show();
                             if(email_verify ==1 && user_id_verify == 1 && nicname_verify == 1 && password_verify == 1) {
                                 compelete_button.setEnabled(true);
                                 compelete_button.setTextColor(getResources().getColor(R.color.white));
                             }
                             break;
                         default:
                     }

                } else {//인증실패
                    compelete_button.setEnabled(false);
                    compelete_button.setTextColor(getResources().getColor(R.color.hintColor));

                    switch (verify_g) {
                        case 1:
                            email_verify = 0 ;
                            Toast.makeText(SignupActivity.this, "메일 인증실패", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            user_id_verify = 0 ;
                            Toast.makeText(SignupActivity.this, "ID 인증실패", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            nicname_verify = 0;
                            Toast.makeText(SignupActivity.this, "닉네임 인증실패", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                    }

                }
            } catch (Exception e) { e.printStackTrace(); }

        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while(itr.hasNext()){

                String key= itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }


    class Signup extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(SignupActivity.this);

        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();

            try{
                URL url = new URL(params[0]);

                //서버로 보낼 데이터를 JSON형태로 wrapping
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id", params[1]);
                postDataParams.put("nicname", params[2]);
                postDataParams.put("password", params[3]);
                postDataParams.put("officemail", params[4]);
                postDataParams.put("phonenumber", params[5]);
                postDataParams.put("user_dept", params[6]);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn != null ){
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    //서버로 데이터를 전송
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();

                    //결과를 받아옴
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()) );

                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if(line == null ) break;
                        output.append(line);
                    }
                    reader.close();
                    conn.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return output.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("회원가입중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//회원가입 성공

                    Toast.makeText(SignupActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();

                } else {//회원가입 실패
                    Toast.makeText(SignupActivity.this,
                            "회원가입실패. 다시시도하시기바랍니다.",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) { e.printStackTrace(); }

        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while(itr.hasNext()){

                String key= itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }


}
