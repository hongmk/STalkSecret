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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class GetContentActivity extends AppCompatActivity {

    private String content_id;
    TextView nicnameText;
    TextView timeText;
    TextView titleText;
    TextView contentText;
    EditText comment_text;

    Button comment_cnt;
    ExpandableListView comment_list;
    ExpandableListView list;
    ArrayList<ChildItem> childItems = new ArrayList<ChildItem>();

    String restful_ip;
    String user_nicname;


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

        SharedPreferences restful_ip_pref = getSharedPreferences("restful_ip", Activity.MODE_PRIVATE);
        restful_ip = restful_ip_pref.getString("restful_ip", "");

        SharedPreferences user_nicname_pref = getSharedPreferences("user_nicname", Activity.MODE_PRIVATE);
        user_nicname = user_nicname_pref.getString("user_nicname", "");


        nicnameText = (TextView)findViewById(R.id.get_content_nicname);
        timeText    = (TextView)findViewById(R.id.get_content_time);
        titleText   = (TextView)findViewById(R.id.get_content_title);
        contentText = (TextView)findViewById(R.id.get_content_content);
        comment_text= (EditText)findViewById(R.id.comment_text);
        comment_cnt = (Button)findViewById(R.id.comment_cnt);
        comment_list = (ExpandableListView) findViewById(R.id.get_content_listview);

        comment_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                if(parent.isGroupExpanded(groupPosition))
                {
                    comment_list.setVisibility(View.INVISIBLE);
                    comment_list.collapseGroup(0);

                    comment_cnt.setVisibility(View.VISIBLE);
                }
                else{

                    // Expanded ,Do your Staff

                }


                return false;
            }
        });

        new GetContent().execute(restful_ip+"/contents/content/"+content_id);
        new GetComments().execute(restful_ip+"/comments/list/"+content_id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void expandComment(View view){

        if((int)view.getTag() >0 ) {

            comment_list.setVisibility(View.VISIBLE);
            comment_list.expandGroup(0);

            view.setVisibility(View.INVISIBLE);
        }

    }

    public void createComment(View view){
        new CreateComment().execute(restful_ip+"/comments",
                content_id,
                user_nicname,
                comment_text.getText().toString());
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
                    comment_cnt.setText("댓글 "+json.getInt("comment_cnt")+"개");
                    comment_cnt.setTag(json.getInt("comment_cnt"));
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


    class GetComments extends AsyncTask<String, String, String> {
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
                JSONArray jsonArray = new JSONArray(s);
                if (jsonArray.length() > 0) {

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject json = jsonArray.getJSONObject(i);

                        childItems.add(new ChildItem(json.getString("nicname"), json.getString("comment")));
                    }
                    list = (ExpandableListView)findViewById(R.id.get_content_listview);
                    list.setAdapter(new CustomExpandableListAdapter(GetContentActivity.this, childItems));

                } else {//인증실패

                    Toast.makeText(GetContentActivity.this,
                            "댓글 목록이 없습니다.",
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


    class CreateComment extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(GetContentActivity.this);

        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();

            try{
                URL url = new URL(params[0]);

                //서버로 보낼 데이터를 JSON형태로 wrapping
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("content_id", params[1]);
                postDataParams.put("nicname", params[2]);
                postDataParams.put("comment", params[3]);

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
            dialog.setMessage("댓글 작성 중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//성공

                    Toast.makeText(GetContentActivity.this, "댓글작성 완료", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();

                } else {//실패
                    Toast.makeText(GetContentActivity.this,
                            "댓글작성 실패. 다시시도하시기바랍니다.",
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
