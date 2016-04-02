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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.WebViewInfoHolder;
import com.jianhui_zhu.openuseragent.view.TabView;
import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jianhuizhu on 2016-03-23.
 */
public class WebViewAdapter extends ArrayAdapter<WebViewInfoHolder> {
    private static WebViewAdapter instance;

    private WebViewAdapter(Context context, int resource) {
        super(context, resource);
    }

    public static WebViewAdapter getInstance(Context context){
        if(instance==null){
            instance = new WebViewAdapter(context,R.layout.item_webview);
        }
        return instance;
    }
    ArrayList<WebViewInfoHolder> webViews=new ArrayList<>();


    private WebViewInfoHolder getWebViewInfoHolderByWebView(final CustomWebView webView){
        for(WebViewInfoHolder holder : webViews){
            if(holder.getWebView()==webView)
                return holder;

        }
        return null;
    }
    public void addWebView(final CustomWebView webView){
        if(isWebViewExists(webView)==false) {
            WebViewInfoHolder holder = new WebViewInfoHolder();
            holder.setWebView(webView);
            holder.setTitle(webView.getTitle());
            webViews.add(0,holder);
        }
    }


    @Override
    public int getCount() {
        return webViews == null ? 0 : webViews.size();
    }

    private void swapWebViews(int position){
        WebViewInfoHolder temp = webViews.get(position);
        webViews.remove(position);
        webViews.add(temp);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final WebViewInfoHolder holder = webViews.get(position);
        String title = holder.getTitle();
        Bitmap snapshot = holder.getBitmap();
        if(convertView==null){
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=vi.inflate(R.layout.item_webview, null);
        }
        TextView titleView = (TextView)convertView.findViewById(R.id.title);
        final ImageView snapshotView = (ImageView)convertView.findViewById(R.id.snapshot);

        titleView.setText(title);
        if(holder.getBitmap()==null) {
            Observable.create(new Observable.OnSubscribe<WebViewInfoHolder>() {
                @Override
                public void call(Subscriber<? super WebViewInfoHolder> subscriber) {
                    Picture picture = holder.getWebView().capturePicture();
                    Bitmap bitmap = Bitmap.createBitmap(picture.getWidth(),
                            picture.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    picture.draw(canvas);
                    holder.setBitmap(bitmap);
                    subscriber.onNext(holder);
                    subscriber.onCompleted();
                }
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<WebViewInfoHolder>() {
                @Override
                public void call(WebViewInfoHolder webViewInfoHolder) {
                    snapshotView.setImageBitmap(webViewInfoHolder.getBitmap());
                    notifyDataSetChanged();
                }
            });
        }else{
            snapshotView.setImageBitmap(holder.getBitmap());
        }
        return convertView;
    }

    private boolean isWebViewExists(final CustomWebView webView){
        for(WebViewInfoHolder holder : webViews){
            if(holder.getWebView().equals(webView))
                return true;
        }
        return false;
    }
}
