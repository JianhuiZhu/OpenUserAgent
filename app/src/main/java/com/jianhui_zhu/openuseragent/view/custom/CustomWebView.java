package com.jianhui_zhu.openuseragent.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jianhuizhu on 2016-03-09.
 */
public class CustomWebView extends WebView {
    private void initSettings(){
        this.setDrawingCacheEnabled(true);
        WebSettings settings=this.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setSaveEnabled(true);
        setWebChromeClient(new CustomWebChrome());
    }

    public void setHomeViewInterface(HomeViewInterface homeViewInterface) {
        this.homeViewInterface = homeViewInterface;
    }

    HomeViewInterface homeViewInterface;

    public CustomWebView(Context context){
        super(context);
        initSettings();

    }
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

}
