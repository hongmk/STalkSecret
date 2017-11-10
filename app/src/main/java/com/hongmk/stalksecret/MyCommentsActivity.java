package com.hongmk.stalksecret;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
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
import java.util.List;

public class MyCommentsActivity extends AppCompatActivity {

    ListView listView = null;
    ItemAdapter itemAdpater = null;
    String user_nicname;
    SharedPreferences user_nicname_pref;
    String restful_ip;

    class Item {
        private String item_id;
        private String item_content_id;
        private String item_text;
        private String item_date;
        private Boolean checked;
        Item( String id, String title, String text, String date) {
            this.item_id = id;
            this.item_content_id = title;
            this.item_text = text;
            this.item_date = date;
            this.checked = false;
        }

        public void setCheck(Boolean check){
            checked = check;
        }

        public Boolean getCheck(){
            return this.checked;
        }

        public String getItemId(){
            return this.item_id;
        }

        public String getItemContentId() {
            return this.item_content_id;
        }


    }

    ArrayList<Item> itemList = new ArrayList<Item>();
    class ItemAdapter extends ArrayAdapter {
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.my_comments_item, null);
            }

            TextView comment_id = (TextView)convertView.findViewById(R.id.mycomment_id);
            TextView comment_content_id = (TextView)convertView.findViewById(R.id.mycomment_content_id);
            TextView comment_text = (TextView)convertView.findViewById(R.id.mycomment_text);
            TextView comment_date = (TextView)convertView.findViewById(R.id.mycomment_date);
            CheckBox comment_check=(CheckBox)convertView.findViewById(R.id.myCommentsCheck);

            final Item item = itemList.get(position);
            comment_id.setText(item.item_id);
            comment_content_id.setText(item.item_content_id);
            comment_text.setText(item.item_text);
            comment_date.setText(item.item_date);

            if (item.checked == true){
                comment_check.setChecked(true);
            } else {
                comment_check.setChecked(false);
            }

            comment_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Toast.makeText(MyCommentsActivity.this,"isChecked"+isChecked,Toast.LENGTH_SHORT).show();
                    item.checked = isChecked;
                }
            });
            return convertView;
        }

        public ItemAdapter(@NonNull Context conText, @LayoutRes int resource, @NonNull List objects) {
            super(conText, resource, objects);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_comments_toolbar);
        toolbar.setTitle("내댓글 관리"); //툴바 제목 표시여부
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences restful_ip_pref = getSharedPreferences("restful_ip", Activity.MODE_PRIVATE);
        restful_ip = restful_ip_pref.getString("restful_ip", "");

        user_nicname_pref = getSharedPreferences("user_nicname", Activity.MODE_PRIVATE);
        user_nicname = user_nicname_pref.getString("user_nicname", "");

        //Toast.makeText(MyContentsActivity.this, ""+user_nicname, Toast.LENGTH_SHORT).show();
        new GetComments().execute(restful_ip+"/comments/commentlist?nicname="+""+user_nicname);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkAll(View view){
        listView = (ListView)findViewById(R.id.cmListView);
        Toast.makeText(MyCommentsActivity.this, "checkAll"+ listView.getAdapter().getCount(),Toast.LENGTH_SHORT).show();
        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            itemList.get(i).setCheck(true);
        }
        itemAdpater.notifyDataSetInvalidated();
    }

    public void unCheckAll(View view){
        listView = (ListView)findViewById(R.id.cmListView);
        Toast.makeText(MyCommentsActivity.this, "uncheckAll"+listView.getAdapter().getCount() ,Toast.LENGTH_SHORT).show();
        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            itemList.get(i).setCheck(false);
        }
        itemAdpater.notifyDataSetInvalidated();
    }

    public void deleteCheckedItem(View view){
        listView = (ListView)findViewById(R.id.cmListView);
        int objCnt = listView.getAdapter().getCount();
        new DeleteComment().execute(restful_ip+"/comments", ""+objCnt);
    }

    class GetComments extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(MyCommentsActivity.this);

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

                        itemList.add(new Item(json.getString("_id"), json.getString("content_id"), json.getString("comment"), json.getString("create_date")));
                    }
                    listView = (ListView)findViewById(R.id.cmListView);
                    itemAdpater = new ItemAdapter(MyCommentsActivity.this, R.layout.my_comments_item, itemList);
                    listView.setAdapter(itemAdpater);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView content_id= (TextView)view.findViewById(R.id.mycomment_content_id);
                            getContent(content_id.getText().toString());
                        }
                    });

                } else {//인증실패

                    Toast.makeText(MyCommentsActivity.this,
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

        private void getContent(String content_id) {
            Intent intent = new Intent(MyCommentsActivity.this, GetContentActivity.class);
            intent.putExtra("content_id", content_id);
            startActivity(intent);
        }
    }


    class DeleteComment extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(MyCommentsActivity.this);
        int objCnt =0;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            listView = (ListView)findViewById(R.id.cmListView);
            Boolean isChecked;
            String item_id ="";
            String item_content_id="";
            objCnt = Integer.parseInt(params[1]);

            for (int i = 0; i < objCnt; i++) {

                isChecked = itemList.get(i).getCheck();
                if(isChecked == true){
                    item_id = itemList.get(i).getItemId();
                    item_content_id = itemList.get(i).getItemContentId();
                }

                try{
                    URL url = new URL(params[0]);

                    //서버로 보낼 데이터를 JSON형태로 wrapping
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("comment_id", ""+item_id);
                    postDataParams.put("content_id", ""+item_content_id);

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
            }

            return output.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("댓글 삭제중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//성공
                    Toast.makeText(MyCommentsActivity.this,
                            "댓글 삭제 완료"+objCnt,
                            Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < objCnt; i++) {
                        Boolean isChecked;
                        isChecked = itemList.get(i).getCheck();
                        if(isChecked == true){
                            //itemList.get(i).setCheck(false);
                            Toast.makeText(MyCommentsActivity.this, "remove"+itemList.get(i).getItemId(),Toast.LENGTH_SHORT).show();
                            itemList.remove(i);

                        }
                        itemAdpater.notifyDataSetInvalidated();
                    }
                    itemAdpater.notifyDataSetInvalidated();


                } else {//로그인 실패
                    Toast.makeText(MyCommentsActivity.this,
                            "댓글 삭제 중 오류발생. 다시시도해주세요.",
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
