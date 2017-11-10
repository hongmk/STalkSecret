package com.hongmk.stalksecret.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hongmk.stalksecret.GetContentActivity;
import com.hongmk.stalksecret.R;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

public class HomeListFragment extends Fragment {

    private WebView webview;
    //private static final String CONTENT_LIST_URL = "http://192.168.0.4:9000/#!/contents/list";
    String angular_ip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home_list, container, false);
        webview= (WebView) rootView.findViewById(R.id.home_webView);

        int position = FragmentPagerItem.getPosition(getArguments());

        SharedPreferences restful_ip_pref = this.getActivity().getSharedPreferences("angular_ip", Activity.MODE_PRIVATE);
        angular_ip = restful_ip_pref.getString("angular_ip", "");

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webview.setWebChromeClient(new WebBrowserClient());
        webview.setWebViewClient(new MyWebViewClient());
        webview.loadUrl(angular_ip+"/#!/contents/list" + "?board_id="+ position);


        //Toast.makeText(getActivity(),CONTENT_LIST_URL + "?board_id="+ position, Toast.LENGTH_SHORT).show();
        /*현재 생성되는 Fragment의 인덱스를 가져옴
        * 최초 생성 시 INDEX[0, 1]생성 -> 탭 1로 이동 시 INDEX[2]생성 하는 방식으로 동작함(선택된 다음 탭의 내용을 미리 생성함)
        * */

        rootView.setTag(position);

        return rootView;

    }

    //webview 페이지의 동작을 후킹하여 앱에서 동작을 시키고 싶을 때 사용
    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("[URL]=", url);

            String urls[] = url.split("&");

            if(urls[1].equals("getContent")) {
                Log.i("[URL1]=", urls[1]);
                Intent intent = new Intent(getActivity(), GetContentActivity.class);
                intent.putExtra("content_id", urls[2]);
                startActivity(intent);

                return true;

            } else if(urls[1].equals("share")) {
                Toast.makeText(getActivity(), "공유기능은 준비중입니다", Toast.LENGTH_SHORT).show();

                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    }


}
