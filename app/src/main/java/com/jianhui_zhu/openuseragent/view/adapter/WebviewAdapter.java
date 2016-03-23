package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.WebViewInfoHolder;
import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jianhuizhu on 2016-03-23.
 */
public class WebviewAdapter extends FragmentPagerAdapter {
    List<WebViewInfoHolder> webViews=new ArrayList<>();

    public WebviewAdapter(FragmentManager fm) {
        super(fm);
    }
    private WebViewInfoHolder getByWebView(final CustomWebView webView){
        for(WebViewInfoHolder holder : webViews){
            if(holder.getWebView()==webView)
                return holder;

        }
        return null;
    }
    public void addWebView(final CustomWebView webView){
        Observable.create(new Observable.OnSubscribe<WebViewInfoHolder>() {
            @Override
            public void call(Subscriber<? super WebViewInfoHolder> subscriber) {
                Picture picture = webView.capturePicture();
                Bitmap bitmap = Bitmap.createBitmap( picture.getWidth(),
                        picture.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                picture.draw(canvas);
                WebViewInfoHolder holder =new WebViewInfoHolder();
                holder.setWebView(webView);
                holder.setBitmap(bitmap);
                subscriber.onNext(holder);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<WebViewInfoHolder>() {
            @Override
            public void call(WebViewInfoHolder webViewInfoHolder) {
                webViews.add(webViewInfoHolder);
            }
        });
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return webViews == null ? 0 : webViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View page = inflater.inflate(R.layout.item_webview, null);

        page.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.i("TAG", "This page was clicked: " + position);
            }
        });
        ((ViewPager) container).addView(page, 0);
        return page;
    }
}
