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

public class EditContentActivity extends AppCompatActivity {

    String restful_ip;
    private String content_id;
    TextView nicnameText;
    TextView timeText;
    TextView titleText;
    TextView contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_content_toolbar);
        toolbar.setTitle("글 수정/삭제"); //툴바 제목 표시여부
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        content_id = intent.getStringExtra("content_id");

        SharedPreferences restful_ip_pref = getSharedPreferences("restful_ip", Activity.MODE_PRIVATE);
        restful_ip = restful_ip_pref.getString("restful_ip", "");

        nicnameText = (TextView)findViewById(R.id.edit_content_nicname);
        timeText    = (TextView)findViewById(R.id.edit_content_time);
        titleText   = (TextView)findViewById(R.id.edit_content_title);
        contentText = (TextView)findViewById(R.id.edit_content_content);

        new GetContent().execute(restful_ip+"/contents/content/"+content_id);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteContent(View view){
        new DeleteContent().execute(restful_ip+"/contents/content",
                content_id);
    }

    public void editContent(View view){
        new EditContent().execute(restful_ip+"/contents/content",
                content_id,
                titleText.getText().toString(),
                contentText.getText().toString());
    }


    class GetContent extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(EditContentActivity.this);

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
            dialog.setMessage("글 조회중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//인증 성공

                    nicnameText.setText(json.getString("nicname"));
                    timeText.setText(json.getString("last_modify_date").replace("T", " ").replace(".000Z",""));
                    titleText.setText(json.getString("title"));
                    contentText.setText(json.getString("content"));

                } else {//인증실패

                    Toast.makeText(EditContentActivity.this,
                            "글 불러오기중 오류발생, 다시 시도해주세요.",
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


    class EditContent extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(EditContentActivity.this);

        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();

            try{
                URL url = new URL(params[0]);

                //서버로 보낼 데이터를 JSON형태로 wrapping
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("content_id", params[1]);
                postDataParams.put("title", params[2]);
                postDataParams.put("content", params[3]);

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
            dialog.setMessage("글 수정중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//성공
                    Toast.makeText(EditContentActivity.this,
                            "글 수정완료",
                            Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();

                } else {//로그인 실패
                    Toast.makeText(EditContentActivity.this,
                            "글 수정 중 오류발생. 다시시도해주세요.",
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


    class DeleteContent extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(EditContentActivity.this);

        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();

            try{
                URL url = new URL(params[0]);

                //서버로 보낼 데이터를 JSON형태로 wrapping
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("content_id", params[1]);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn != null ){
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("DELETE");
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
            dialog.setMessage("글 삭제중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//성공
                    Toast.makeText(EditContentActivity.this,
                            "글 삭제 완료",
                            Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();

                } else {//로그인 실패
                    Toast.makeText(EditContentActivity.this,
                            "글 삭제 중 오류발생. 다시시도해주세요.",
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
