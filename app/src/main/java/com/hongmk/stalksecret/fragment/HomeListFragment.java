package com.hongmk.stalksecret.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.hongmk.stalksecret.R;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

public class HomeListFragment extends Fragment {

    private WebView webview;
    private static final String CONTENT_LIST_URL = "http://172.16.2.8:9000/#!/contents/list";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home_list, container, false);
        webview= (WebView) rootView.findViewById(R.id.home_webView);


        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webview.setWebChromeClient(new WebBrowserClient());
        //webview.setWebViewClient(new MyWebViewClient());
        webview.loadUrl(CONTENT_LIST_URL);

        //Toast.makeText(getActivity(), "selected" + pos , Toast.LENGTH_SHORT).show();
        /*현재 생성되는 Fragment의 인덱스를 가져옴
        * 최초 생성 시 INDEX[0, 1]생성 -> 탭 1로 이동 시 INDEX[2]생성 하는 방식으로 동작함(선택된 다음 탭의 내용을 미리 생성함)
        * */
        int position = FragmentPagerItem.getPosition(getArguments());

        rootView.setTag(position);

        return rootView;

    }


}
