package com.hongmk.stalksecret.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hongmk.stalksecret.R;
import com.hongmk.stalksecret.ViewBoardActivity;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

public class HomeListFragment extends Fragment {

    private WebView webview;
    private static final String CONTENT_LIST_URL = "http://192.168.0.4:9000/#!/contents/list";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home_list, container, false);
        webview= (WebView) rootView.findViewById(R.id.home_webView);


        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webview.setWebChromeClient(new WebBrowserClient());
        webview.setWebViewClient(new MyWebViewClient());
        webview.loadUrl(CONTENT_LIST_URL);

        //Toast.makeText(getActivity(), "selected" + pos , Toast.LENGTH_SHORT).show();
        /*현재 생성되는 Fragment의 인덱스를 가져옴
        * 최초 생성 시 INDEX[0, 1]생성 -> 탭 1로 이동 시 INDEX[2]생성 하는 방식으로 동작함(선택된 다음 탭의 내용을 미리 생성함)
        * */
        int position = FragmentPagerItem.getPosition(getArguments());

        rootView.setTag(position);

        return rootView;

    }

    //webview 페이지의 동작을 후킹하여 앱에서 동작을 시키고 싶을 때 사용
    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("[URL]=", url);

//            view.loadUrl(url);
//            return true;
//
            String urls[] = url.split("&");

            if (urls[0].equals("http://192.168.0.4:9000/contents/content")) {
                Log.i("[URL1]=", urls[1]);
                Intent intent = new Intent(getActivity(), ViewBoardActivity.class);
                startActivity(intent);

                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    }


}
