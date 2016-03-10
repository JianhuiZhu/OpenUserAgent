package com.jianhui_zhu.openuseragent.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.WebView;

import com.jianhui_zhu.openuseragent.view.interfaces.OnScollTopInterface;

/**
 * Created by jianhuizhu on 2016-03-09.
 */
public class CustomWebView extends WebView {
    public void setOnScollTopInterface(OnScollTopInterface onScollTopInterface) {
        this.onScollTopInterface = onScollTopInterface;
    }

    OnScollTopInterface onScollTopInterface;

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }

    @Override
    public String getUrl() {
        return super.getUrl();
    }

    @Override
    public void reload() {
        super.reload();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        onScollTopInterface.onSChanged(l,t,oldl,oldt);
    }

}
