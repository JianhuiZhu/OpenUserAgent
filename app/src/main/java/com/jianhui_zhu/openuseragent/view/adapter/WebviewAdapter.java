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
import com.jianhui_zhu.openuseragent.presenter.HomePresenter;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.view.HomeView;
import com.jianhui_zhu.openuseragent.view.TabView;
import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;
import com.jianhui_zhu.openuseragent.view.dialogs.TabStackDialog;

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
    private Context context;
    private WebViewAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }
    private HomePresenter homePresenter = null;
    private TabStackDialog tabStackDialog = null;
    public static WebViewAdapter getInstance(Context context){
        if(instance==null){
            instance = new WebViewAdapter(context,R.layout.item_webview);
        }
        return instance;
    }
    ArrayList<WebViewInfoHolder> webViews=new ArrayList<>();
    public WebViewAdapter setHomePresenter(HomePresenter homePresenter){
        this.homePresenter = homePresenter;
        return this;
    }
    public WebViewAdapter setTabStackDialog(TabStackDialog tabStackDialog){
        this.tabStackDialog = tabStackDialog;
        return this;
    }
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
//            clear();
//            addAll(webViews);
            notifyDataSetChanged();
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
//        clear();
//        addAll(webViews);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final WebViewInfoHolder holder = webViews.get(position);
        String title = webViews.get(position).getTitle();
        Bitmap snapshot = holder.getBitmap();
        if(convertView==null){
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=vi.inflate(R.layout.item_webview, null);
        }
        ImageView closeIcon = (ImageView)convertView.findViewById(R.id.close_btn);
        TextView titleView = (TextView)convertView.findViewById(R.id.title);
        final ImageView snapshotView = (ImageView)convertView.findViewById(R.id.snapshot);

        titleView.setText(title);
        if(holder.getBitmap()==null) {
                    Picture picture = holder.getWebView().capturePicture();
                    Bitmap bitmap = Bitmap.createBitmap(picture.getWidth(),
                            picture.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    picture.draw(canvas);
                    holder.setBitmap(bitmap);
                    snapshotView.setImageBitmap(holder.getBitmap());
                    notifyDataSetChanged();

        }else{
            snapshotView.setImageBitmap(holder.getBitmap());
        }
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViews.remove(position);
                if(webViews.isEmpty()){
                    if(homePresenter!=null&&tabStackDialog!=null){
                        homePresenter.clearWebHolder();
                    }
                }
                notifyDataSetChanged();
            }
        });
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homePresenter!=null&&tabStackDialog!=null){
                    homePresenter.changeWebView(holder.getWebView());
                    tabStackDialog.dismiss();
                }else{
                    FragmenUtil.switchToFragment(context,HomeView.newInstanceWithUrl(holder.getWebView().getUrl()));
                }
            }
        });
        snapshotView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homePresenter!=null&&tabStackDialog!=null){
                    homePresenter.changeWebView(holder.getWebView());
                    tabStackDialog.dismiss();
                }else{
                    FragmenUtil.switchToFragment(context,HomeView.newInstanceWithUrl(holder.getWebView().getUrl()));
                }
            }
        });
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
