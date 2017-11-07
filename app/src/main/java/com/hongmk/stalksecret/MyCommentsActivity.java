package com.hongmk.stalksecret;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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


    class Item {
        String cmTitle;
        String cmText;
        Item( String cmTitle, String cmText) {
            this.cmTitle = cmTitle;
            this.cmText = cmText;
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
            TextView cmText1View = (TextView)convertView.findViewById(R.id.cmTitle);
            TextView cmText2View = (TextView)convertView.findViewById(R.id.cmText);
            Item item = itemList.get(position);
            cmText1View.setText(item.cmTitle);
            cmText2View.setText(item.cmText);
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

        user_nicname_pref = getSharedPreferences("user_nicname", Activity.MODE_PRIVATE);
        user_nicname = user_nicname_pref.getString("user_nicname", "");

        //Toast.makeText(MyContentsActivity.this, ""+user_nicname, Toast.LENGTH_SHORT).show();
        new GetComments().execute("http://192.168.0.4:52275/comments/commentlist?nicname="+""+user_nicname);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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

                        itemList.add(new Item(json.getString("content_id"), json.getString("comment")));
                    }
                    listView = (ListView)findViewById(R.id.cmListView);
                    itemAdpater = new ItemAdapter(MyCommentsActivity.this, R.layout.my_comments_item, itemList);
                    listView.setAdapter(itemAdpater);


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
    }
}
