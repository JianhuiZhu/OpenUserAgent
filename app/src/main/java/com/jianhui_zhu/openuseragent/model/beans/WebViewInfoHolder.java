package com.jianhui_zhu.openuseragent.model.beans;

import android.graphics.Bitmap;

import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;

/**
 * Created by jianhuizhu on 2016-03-23.
 */
public class WebViewInfoHolder {
    private CustomWebView webView;
    private Bitmap bitmap;

    public CustomWebView getWebView() {
        return webView;
    }

    public void setWebView(CustomWebView webView) {
        this.webView = webView;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
