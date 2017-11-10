package com.hongmk.stalksecret;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class EditPasswordActivity extends AppCompatActivity {

    String restful_ip;
    String user_id;
    String user_nicname;

    EditText current_password;
    EditText new_password;
    EditText new_password_re;
    Button complete;
    int passwordVerified;
    int newPasswordVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_password_toolbar);
        toolbar.setTitle("비밀번호변경"); //툴바 제목 표시여부
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences restful_ip_pref = getSharedPreferences("restful_ip", Activity.MODE_PRIVATE);
        restful_ip = restful_ip_pref.getString("restful_ip", "");

        SharedPreferences user_id_pref = getSharedPreferences("user_id", Activity.MODE_PRIVATE);
        user_id = user_id_pref.getString("user_id", "");

        SharedPreferences user_nicname_pref = getSharedPreferences("user_nicname", Activity.MODE_PRIVATE);
        user_nicname = user_nicname_pref.getString("user_nicname", "");


        current_password = (EditText)findViewById(R.id.edit_password_current);
        new_password = (EditText)findViewById(R.id.edit_password_new);
        new_password_re = (EditText)findViewById(R.id.edit_password_re);
        new_password.addTextChangedListener(new passwordWatcher());
        new_password_re.addTextChangedListener(new passwordWatcher());

        complete = (Button)findViewById(R.id.edit_password_complete);
        complete.setEnabled(false);
        complete.setBackgroundColor(getResources().getColor(R.color.shb_light_gray));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //닉네임 재입력 시 완료버튼 비활성화
    public class passwordWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            // Checking editable.hashCode() to understand which edittext is using right now

                if(new_password.getText().toString().equals(new_password_re.getText().toString()) != true) {
                    newPasswordVerified = 0;
                    complete.setEnabled(false);
                    complete.setBackgroundColor(getResources().getColor(R.color.shb_light_gray));
                    new_password_re.setTextColor(getResources().getColor(R.color.darkred));
                } else {
                    newPasswordVerified = 1;
                    if(passwordVerified == 1) {
                        complete.setEnabled(true);
                        complete.setBackgroundColor(getResources().getColor(R.color.shb_button));
                        new_password_re.setTextColor(getResources().getColor(R.color.shb_button));
                    }
                }
            }

    }

    public void verifyEditPassword(View view){
        //기존 암호 검증
        new Verify().execute(restful_ip+"/users/auth/password?user_id="+user_id+"&password="+current_password.getText().toString() );
    }

    public void completeEditPassword(View view){
        //비밀번호 수정 통신
        new EditPassword().execute(restful_ip+"/users/password",
                user_id,
                current_password.getText().toString(),
                new_password_re.getText().toString());
    }


    class Verify extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(EditPasswordActivity.this);

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
                    Toast.makeText(EditPasswordActivity.this, "기존 암호 일치검증 완료", Toast.LENGTH_SHORT).show();
                    if(newPasswordVerified == 1) {
                        complete.setEnabled(true);
                        complete.setBackgroundColor(getResources().getColor(R.color.shb_button));
                    }
                    passwordVerified = 1;
                } else {//인증실패
                    Toast.makeText(EditPasswordActivity.this, "기존 암호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    complete.setEnabled(false);
                    complete.setBackgroundColor(getResources().getColor(R.color.shb_gray));
                    passwordVerified = 0;
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


    class EditPassword extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(EditPasswordActivity.this);

        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();

            try{
                URL url = new URL(params[0]);

                //서버로 보낼 데이터를 JSON형태로 wrapping
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id", params[1]);
                postDataParams.put("password", params[2]);
                postDataParams.put("new_password", params[3]);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn != null ){
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("PUT");
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
                if (json.getBoolean("result") == true) {//성공

                    Toast.makeText(EditPasswordActivity.this, "비밀번호 변경완료", Toast.LENGTH_SHORT).show();
                    current_password.setText("");
                    new_password.setText("");
                    new_password_re.setText("");
                } else {//실패
                    Toast.makeText(EditPasswordActivity.this,
                            "비밀번호 변경 중 오류발생. 잠시 후 다시 변경해주세요.",
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
