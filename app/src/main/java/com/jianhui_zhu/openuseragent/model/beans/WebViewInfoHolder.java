package com.jianhui_zhu.openuseragent.model.beans;

import android.graphics.Bitmap;

import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;

import java.util.HashMap;

/**
 * Created by jianhuizhu on 2016-03-23.
 */
public class WebViewInfoHolder {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    private String title;
    private CustomWebView webView;
    private Bitmap bitmap;

    public HashMap<String, Boolean> getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(HashMap<String, Boolean> thirdParty) {
        this.thirdParty.putAll(thirdParty);
    }

    private HashMap<String,Boolean> thirdParty = new HashMap<>();
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
