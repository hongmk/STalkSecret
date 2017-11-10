package com.hongmk.stalksecret;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class SigninActivity extends AppCompatActivity {
    private Intent intent;
    private EditText userIdText;
    private EditText passwordText;
    private String restful_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        SharedPreferences restful_ip_pref = getSharedPreferences("restful_ip", Activity.MODE_PRIVATE);
        restful_ip = restful_ip_pref.getString("restful_ip", "");
    }

    public void onLogin(View view){
        userIdText = (EditText) findViewById(R.id.signin_id);
        passwordText = (EditText) findViewById(R.id.signin_password);

        new Login().execute(restful_ip+"/users/login",
                userIdText.getText().toString(),
                passwordText.getText().toString());
    }

    public void goSignup(View view){
        intent = new Intent(SigninActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    class Login extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(SigninActivity.this);

        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();

            try{
                URL url = new URL(params[0]);

                //서버로 보낼 데이터를 JSON형태로 wrapping
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id", params[1]);
                postDataParams.put("password", params[2]);

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
            dialog.setMessage("Login...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//로그인 성공

                    //로그인토큰 저장
                    String token = json.getString("token");
                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("token", token);
                    editor.commit();

                    //사용자 부서 저장
                    int user_dept = json.getInt("user_dept");
                    SharedPreferences user_dept_pref= getSharedPreferences("user_dept", MODE_PRIVATE);
                    SharedPreferences.Editor user_dept_editor = user_dept_pref.edit();
                    user_dept_editor.putInt("user_dept", user_dept);
                    user_dept_editor.commit();


                    //사용자 id 저장
                    SharedPreferences user_id_pref= getSharedPreferences("user_id", MODE_PRIVATE);
                    SharedPreferences.Editor user_id_editor = user_id_pref.edit();
                    user_id_editor.putString("user_id", userIdText.getText().toString());
                    user_id_editor.commit();

                    //사용자 닉네임 저장
                    String user_nicname = json.getString("nicname");
                    SharedPreferences user_nicname_pref= getSharedPreferences("user_nicname", MODE_PRIVATE);
                    SharedPreferences.Editor user_nicname_editor = user_nicname_pref.edit();
                    user_nicname_editor.putString("user_nicname", user_nicname);
                    user_nicname_editor.commit();

                    Toast.makeText(SigninActivity.this, user_nicname+"님 환영합니다.", Toast.LENGTH_SHORT).show();

                    intent = new Intent(SigninActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {//로그인 실패
                    Toast.makeText(SigninActivity.this,
                            "아이디가 없거나 암호가 틀렸습니다.",
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
