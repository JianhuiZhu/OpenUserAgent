package com.jianhui_zhu.openuseragent.view;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.presenter.HomePresenter;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jianhui Zhu on 2016-01-27.
 */
public class HomeView extends AbstractFragment implements HomeViewInterface{
    @Bind(R.id.web_container) WebView webHolder;
    private HomePresenter presenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter=new HomePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBrowserSettings();
        loadTargetUrl("");
    }

    @Override
    public void initBrowserSettings() {
        WebSettings settings = this.webHolder.getSettings();
        WebViewClient client = new CustomWebView();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);
        this.webHolder.setWebViewClient(client);
    }

    @Override
    public void loadTargetUrl(String url) {
        if(url!=null&&!url.equals("")){
            this.webHolder.loadUrl(url);
        }else{
            this.webHolder.loadUrl("http://www.google.com");
        }
    }

    private class CustomWebView extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

}
