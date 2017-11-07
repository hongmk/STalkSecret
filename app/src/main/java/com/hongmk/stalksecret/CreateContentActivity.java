package com.hongmk.stalksecret;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

public class CreateContentActivity extends AppCompatActivity {

    private Intent intent;
    private int board_id;
    private String user_id;
    private String user_nicname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.make_content_toolbar);
        toolbar.setTitle("글쓰기"); //툴바 제목 표시여부
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences board_id_pref = getSharedPreferences("current_board", Activity.MODE_PRIVATE);
        board_id = board_id_pref.getInt("current_board", 0);

        SharedPreferences user_id_pref = getSharedPreferences("user_id", Activity.MODE_PRIVATE);
        user_id = user_id_pref.getString("user_id", "");

        SharedPreferences user_nicname_pref = getSharedPreferences("user_nicname", Activity.MODE_PRIVATE);
        user_nicname = user_nicname_pref.getString("user_nicname", "");

        //Toast.makeText(CreateContentActivity.this, ""+board_id+user_id+user_nicname, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void createContent(View view) {

        EditText title = (EditText)findViewById(R.id.content_title);
        EditText content = (EditText)findViewById(R.id.content_content);

        new Login().execute("http://192.168.0.4:52275/contents",
                user_id,
                user_nicname,
                ""+board_id,
                title.getText().toString(),
                content.getText().toString());
    }

    class Login extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(CreateContentActivity.this);

        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();

            try{
                URL url = new URL(params[0]);

                //서버로 보낼 데이터를 JSON형태로 wrapping
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id", params[1]);
                postDataParams.put("nicname", params[2]);
                postDataParams.put("board_id", params[3]);
                postDataParams.put("title", params[4]);
                postDataParams.put("content", params[5]);


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
                if (json.getBoolean("result") == true) {//글작성 성공
                    finishAffinity();
                    intent = new Intent(CreateContentActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(CreateContentActivity.this,
                            "글 작성중 오류발생. 다시 시도해주세요.",
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

