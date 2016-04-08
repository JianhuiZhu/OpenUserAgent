package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
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
import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.view.HomeView;
import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;
import com.jianhui_zhu.openuseragent.view.dialogs.TabStackDialog;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.util.ArrayList;

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

    public HomePresenter getHomePresenter() {
        return homePresenter;
    }

    public WebViewAdapter setTabStackDialog(TabStackDialog tabStackDialog){
        this.tabStackDialog = tabStackDialog;
        return this;
    }

    public void addWebView(final CustomWebView webView){
        if(isWebViewExists(webView)==false) {
            WebViewInfoHolder holder = new WebViewInfoHolder();
            holder.setWebView(webView);
            String title = webView.getTitle();
            String domain ="";
            try {
                domain = WebUtil.getDomainbyUrl(webView.getUrl());
            } catch (URISyntaxException e) {
                Log.d(this.getClass().getSimpleName(),e.toString());
            }
            holder.setTitle(title.equals("") ? domain : title);
            webViews.add(0,holder);
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean isEmpty() {
        return webViews == null || (webViews.isEmpty());
    }

    @Override
    public int getCount() {
        return webViews == null ? 0 : webViews.size();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final WebViewInfoHolder holder = webViews.get(position);
        String title = webViews.get(position).getTitle();
        if(convertView==null){
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=vi.inflate(R.layout.item_webview, null);
        }
        ImageView closeIcon = (ImageView)convertView.findViewById(R.id.close_btn);
        TextView titleView = (TextView)convertView.findViewById(R.id.title);
        final ImageView snapshotView = (ImageView)convertView.findViewById(R.id.snapshot);

        titleView.setText(title);
                    //Picture picture = holder.getWebView().capturePicture();
                    Bitmap bitmap = holder.getWebView().getDrawingCache();
                    //Canvas canvas = new Canvas(bitmap);
                    //picture.draw(canvas);
                    holder.setBitmap(bitmap);


                    snapshotView.setImageBitmap(holder.getBitmap());
                    notifyDataSetChanged();


        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViews.remove(position);
                if(webViews.isEmpty()){
                    if(homePresenter!=null&&tabStackDialog!=null){
                        homePresenter.clearWebHolder();
                        tabStackDialog.dismiss();
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
