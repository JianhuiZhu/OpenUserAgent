package com.jianhui_zhu.openuseragent.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jianhui_zhu.openuseragent.util.Setting;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.RxBus;
import com.jianhui_zhu.openuseragent.util.event.GlobalSettingChangeEvent;
import com.jianhui_zhu.openuseragent.view.HomeView.CustomWebViewClient;
import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.view.dialogs.AlertDialog;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

import java.net.MalformedURLException;
import java.net.URL;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jianhuizhu on 2016-03-09.
 */
public class CustomWebView extends WebView {
    CustomWebView instance;
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
    private void initSetting(){
        WebSettings settings=this.getSettings();
        switch (Setting.getInstance().getCookiePolicy()){
            case Setting.COOKIE_ALLOW_ALL:
                cookieManager.setAcceptCookie(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.setAcceptThirdPartyCookies(this,true);
                }
                break;
            case Setting.COOKIE_ALLOW_FIRST_PARTY:
                cookieManager.setAcceptCookie(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.setAcceptThirdPartyCookies(this,false);
                }
                break;
            case Setting.COOKIE_NONE:
                cookieManager.setAcceptCookie(false);
                break;
        }
        switch (Setting.getInstance().getThirdPartyPolicy()){
            case Setting.ALLOW_ALL:
                client.changePolicy(Setting.ALLOW_ALL);
                break;
            case Setting.BLOCK_BLACK_LIST:
                client.changePolicy(Setting.BLOCK_BLACK_LIST);
                break;
            case Setting.BLOCK_ALL_THIRD_PARTY:
                client.changePolicy(Setting.BLOCK_ALL_THIRD_PARTY);
                break;
        }
        switch (Setting.getInstance().getJsPolicy()){
            case Setting.JS_ALLOW:
                settings.setJavaScriptEnabled(true);
                break;
            case Setting.JS_BLOCK:
                settings.setJavaScriptEnabled(false);
                break;
        }
        switch (Setting.getInstance().getPopupPolicy()){
            case Setting.POPUP_ALLOW_ALL:
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                break;
            case Setting.POPUP_BLOCK_ALL:
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
                        initSetting();
                    }
                }
            });
        }
        this.setDrawingCacheEnabled(true);
        initSetting();
        setWebChromeClient(new CustomWebChrome());
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                HitTestResult result = getHitTestResult();
                AlertDialog dialog;
                if(result.getExtra()==null){
                    if(result.getType()==HitTestResult.UNKNOWN_TYPE) {
                        Message msg = new Message();
                        msg.setTarget(new MyHandler());
                        instance.requestFocusNodeHref(msg);
                    }else{
                        return true;
                    }
                }else {
                    dialog = AlertDialog.newInstance(result.getExtra(), client.getTabPolicy().get(WebUtil.getDomain(result.getExtra())), client.getCurHost(), client);
                    FragmenUtil.switchToFragment(v.getContext(), dialog);
                }

                return true;
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HitTestResult result = getHitTestResult();
                homeViewInterface.changeTextInSearchBar(result.getExtra());
            }
        });
    }


    public void setHomeViewInterface(HomeViewInterface homeViewInterface) {
        this.homeViewInterface = homeViewInterface;
    }

    HomeViewInterface homeViewInterface;

    public CustomWebView(Context context,WebViewClient client){
        super(context);
        this.setWebViewClient(client);
        initSettings();
        this.instance = this;

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
        ViewGroup v =(ViewGroup)this.getParent();
        if(v!=null){
            v.removeView(this);
        }
        client.unsubscribe();
        compositeSubscription.unsubscribe();
        super.destroy();
    }
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AlertDialog dialog;
            String src = msg.getData().getString("src");
            if(src==null||src.equals("")){
                dialog = AlertDialog.newInstanceWithWarning("CANNOT GET THE RESOURCE LINK");
            }else {
                dialog = AlertDialog.newInstance(src, client.getTabPolicy().get(WebUtil.getDomain(src)), client.getCurHost(), client);
            }
            FragmenUtil.switchToFragment(getContext(), dialog);
        }
    }
}
