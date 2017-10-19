package com.hongmk.stalksecret;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HomeActivity extends AppCompatActivity {

    Intent intent;
    private WebView mWebView;
    private String myUrl = "http://192.168.0.4:9000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        toolbar.setTitle("금융개발부"); //툴바 제목 표시여부
        setSupportActionBar(toolbar);

        loadURL();


    }

    public void onMyPage(View view){
        intent = new Intent(HomeActivity.this, MypageActivity.class);
        startActivity(intent);
    }

    public void onNoti(View view){
        intent = new Intent(HomeActivity.this, NoticeActivity.class);
        startActivity(intent);
    }

    public void makeContent(View view) {
        intent = new Intent(HomeActivity.this, CreateBoardActivity.class);
        startActivity(intent);
    }



    public void loadURL() {
        mWebView = (WebView) findViewById(R.id.home_webView);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.loadUrl(myUrl); // 접속 URL
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClientClass());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class WebViewClientClass extends WebViewClient {
        @SuppressWarnings("deprecation")
        // 새로운 URL이 webview에 로드되려 할 경우 컨트롤을 대신할 기회를 줌
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }



}
