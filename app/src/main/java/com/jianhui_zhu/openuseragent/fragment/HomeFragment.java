package com.jianhui_zhu.openuseragent.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jianhui_zhu.openuseragent.AbstractFragment;
import com.jianhui_zhu.openuseragent.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jianhuizhu on 2016-01-27.
 */
public class HomeFragment extends AbstractFragment {
    @Bind(R.id.web_container) WebView webHolder;
    private WebSettings settings;
    private WebViewClient client;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.settings = this.webHolder.getSettings();
        this.client = new CustomWebView();
        this.settings.setDefaultTextEncodingName("UTF-8");
        this.settings.setJavaScriptEnabled(true);
        this.webHolder.setWebViewClient(this.client);
        this.webHolder.loadUrl("http://www.google.com");
    }
    private class CustomWebView extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

}
