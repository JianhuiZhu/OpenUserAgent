package com.jianhui_zhu.openuseragent.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jianhui_zhu.openuseragent.util.Constant;
import com.jianhui_zhu.openuseragent.util.RxBus;
import com.jianhui_zhu.openuseragent.util.SettingSingleton;
import com.jianhui_zhu.openuseragent.util.event.GlobalSettingChangeEvent;
import com.jianhui_zhu.openuseragent.view.HomeView.CustomWebViewClient;
import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.view.HomeView;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

import java.net.CookieHandler;
import java.net.MalformedURLException;
import java.net.URL;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jianhuizhu on 2016-03-09.
 */
public class CustomWebView extends WebView {
    public CustomWebViewClient getClient() {
        return client;
    }
    CookieManager cookieManager = CookieManager.getInstance();
    CompositeSubscription compositeSubscription;
    CustomWebViewClient client;
    public void changeJsSetting(boolean setting){
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(setting);
    }
    private void changeSetting(){
        WebSettings settings=this.getSettings();
        switch (SettingSingleton.getInstance().getCookiePolicy()){
            case Constant.COOKIE_ALLOW_ALL:
                cookieManager.setAcceptCookie(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.setAcceptThirdPartyCookies(this,true);
                }
                break;
            case Constant.COOKIE_ALLOW_FIRST_PARTY:
                cookieManager.setAcceptCookie(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.setAcceptThirdPartyCookies(this,false);
                }
                break;
            case Constant.COOKIE_NONE:
                cookieManager.setAcceptCookie(false);
                break;
        }
        switch (SettingSingleton.getInstance().getThirdPartyPolicy()){
            case Constant.ALLOW_ALL:
                client.changePolicy(Constant.ALLOW_ALL);
                break;
            case Constant.BLOCK_BLACK_LIST:
                client.changePolicy(Constant.BLOCK_BLACK_LIST);
                break;
            case Constant.BLOCK_ALL_THIRD_PARTY:
                client.changePolicy(Constant.BLOCK_ALL_THIRD_PARTY);
                break;
        }
        switch (SettingSingleton.getInstance().getJsPolicy()){
            case Constant.JS_ALLOW:
                settings.setJavaScriptEnabled(true);
                break;
            case Constant.JS_BLOCK:
                settings.setJavaScriptEnabled(false);
                break;
        }
        switch (SettingSingleton.getInstance().getPopupPolicy()){
            case Constant.POPUP_ALLOW_ALL:
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                break;
            case Constant.POPUP_BLOCK_ALL:
                settings.setJavaScriptCanOpenWindowsAutomatically(false);
                break;
        }
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setSaveEnabled(true);
    }
    private boolean allThirdPartyPolicy;
    private void initSettings(){
        if(compositeSubscription == null){
            compositeSubscription = new CompositeSubscription();
            Subscription subscription = RxBus.getInstance().toObserverable().subscribe(new Action1<Object>() {
                @Override
                public void call(Object o) {
                    if(o instanceof GlobalSettingChangeEvent){
                        changeSetting();
                    }
                }
            });
        }
        this.setDrawingCacheEnabled(true);
        changeSetting();
        setWebChromeClient(new CustomWebChrome());
    }


    public void setHomeViewInterface(HomeViewInterface homeViewInterface) {
        this.homeViewInterface = homeViewInterface;
    }

    HomeViewInterface homeViewInterface;

    public CustomWebView(Context context,WebViewClient client){
        super(context);
        this.setWebViewClient(client);
        initSettings();

    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        this.client = (CustomWebViewClient) client;
        super.setWebViewClient(client);
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
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if(homeViewInterface!=null){
            if(scrollY==0&&clampedY){
                homeViewInterface.changeToolBarVisibility(VISIBLE);
            }else if(scrollY>100){
                homeViewInterface.changeToolBarVisibility(GONE);
            }
        }
    }
    private class CustomWebChrome extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            try {
                URL uri=new URL(view.getUrl()) ;
                String host=uri.getHost();
                WebUtil webUtil = WebUtil.getInstance();
                webUtil.setIcon(icon,host);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {
        client.unsubscribe();
        compositeSubscription.unsubscribe();
        super.destroy();
    }
}
