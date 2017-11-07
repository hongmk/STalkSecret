package com.hongmk.stalksecret;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class GetContentActivity extends AppCompatActivity {

    private String content_id;
    TextView nicnameText;
    TextView timeText;
    TextView titleText;
    TextView contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_content);

        Intent intent = getIntent();
        content_id = intent.getStringExtra("content_id");

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_board_toolbar);
        toolbar.setTitle("글조회"); //툴바 제목 표시여부
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nicnameText = (TextView)findViewById(R.id.get_content_nicname);
        timeText    = (TextView)findViewById(R.id.get_content_time);
        titleText   = (TextView)findViewById(R.id.get_content_title);
        contentText = (TextView)findViewById(R.id.get_content_content);

        new GetContent().execute("http://192.168.0.4:52275/contents/content/"+content_id);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    class GetContent extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(GetContentActivity.this);

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

                    Toast.makeText(GetContentActivity.this,
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



}
