package com.jianhui_zhu.openuseragent.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

/**
 * Created by jianhuizhu on 2016-03-09.
 */
public class CustomWebView extends WebView {
    public void setHomeViewInterface(HomeViewInterface homeViewInterface) {
        this.homeViewInterface = homeViewInterface;
    }

    HomeViewInterface homeViewInterface;


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
}
