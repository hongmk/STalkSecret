package com.hongmk.stalksecret;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class EditNicnameActivity extends AppCompatActivity {

    String restful_ip;
    String user_id;
    String user_nicname;
    TextView nicname;
    Button complete;
    EditText newNicname;
    int isVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nicname);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_nickname_toolbar);
        toolbar.setTitle("닉네임변경"); //툴바 제목 표시여부
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences restful_ip_pref = getSharedPreferences("restful_ip", Activity.MODE_PRIVATE);
        restful_ip = restful_ip_pref.getString("restful_ip", "");

        SharedPreferences user_id_pref = getSharedPreferences("user_id", Activity.MODE_PRIVATE);
        user_id = user_id_pref.getString("user_id", "");

        SharedPreferences user_nicname_pref = getSharedPreferences("user_nicname", Activity.MODE_PRIVATE);
        user_nicname = user_nicname_pref.getString("user_nicname", "");

        nicname = (TextView)findViewById(R.id.edit_nicname_current);
        nicname.setText(user_nicname);

        complete = (Button)findViewById(R.id.edit_nicname_complete);
        complete.setEnabled(false);
        complete.setBackgroundColor(getResources().getColor(R.color.shb_light_gray));

        newNicname = (EditText) findViewById(R.id.edit_nicname_new);
        newNicname.addTextChangedListener(new nicnameWatcher());
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
    public class nicnameWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            // Checking editable.hashCode() to understand which edittext is using right now
            if (newNicname.getText().hashCode() == editable.hashCode()){
                complete.setEnabled(false);
                complete.setBackgroundColor(getResources().getColor(R.color.shb_light_gray));
            }
        }
    }


    public void verifyEditNicname(View view){
        //닉네임 검증 통신(signup페이지와 동일로직수행)
        if(TextUtils.isEmpty(newNicname.getText().toString())) {
            Toast.makeText(EditNicnameActivity.this, "변경할 닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();

        } else if(TextUtils.equals(newNicname.getText().toString(), nicname.getText().toString() ) ) {
            Toast.makeText(EditNicnameActivity.this, "기존 닉네임과 동일합니다", Toast.LENGTH_SHORT).show();

        } else {
            new Verify().execute(restful_ip+"/users/auth/nicname?nicname="+newNicname.getText().toString());

        }

    }

    public void completeEditNicname(View view){
        //닉네임 수정 통신
        new EditNicname().execute(restful_ip+"/users/nicname",
                user_id,
                nicname.getText().toString(),
                newNicname.getText().toString());
    }

    class Verify extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(EditNicnameActivity.this);

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
                    Toast.makeText(EditNicnameActivity.this, "변경가능한 닉네임입니다", Toast.LENGTH_SHORT).show();
                    complete.setEnabled(true);
                    complete.setBackgroundColor(getResources().getColor(R.color.shb_button));
                } else {//인증실패
                    complete.setEnabled(false);
                    complete.setBackgroundColor(getResources().getColor(R.color.shb_gray));
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


    class EditNicname extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(EditNicnameActivity.this);

        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();

            try{
                URL url = new URL(params[0]);

                //서버로 보낼 데이터를 JSON형태로 wrapping
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id", params[1]);
                postDataParams.put("nicname", params[2]);
                postDataParams.put("new_nicname", params[3]);

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
                if (json.getBoolean("result") == true) {//회원가입 성공

                    Toast.makeText(EditNicnameActivity.this, "닉네임변경 완료", Toast.LENGTH_SHORT).show();

                    //shared변수 닉네임 변경
                    SharedPreferences user_nicname_pref= getSharedPreferences("user_nicname", MODE_PRIVATE);
                    SharedPreferences.Editor user_nicname_editor = user_nicname_pref.edit();
                    user_nicname_editor.putString("user_nicname", newNicname.getText().toString());
                    user_nicname_editor.commit();

                    nicname.setText(newNicname.getText().toString());

                } else {//회원가입 실패
                    Toast.makeText(EditNicnameActivity.this,
                            "닉네임변경 중 오류발생. 잠시 후 다시 변경해주세요.",
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
