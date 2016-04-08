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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        ViewHolder holder;
        final WebViewInfoHolder webviewHolder = webViews.get(position);
        String title = webViews.get(position).getTitle();
        if(convertView==null){
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=vi.inflate(R.layout.item_webview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.position = position;
        holder.title.setText(title);
        Bitmap bitmap = webviewHolder.getWebView().getDrawingCache();
        holder.snapshot.setImageBitmap(bitmap);
        notifyDataSetChanged();
        return convertView;
    }

    private boolean isWebViewExists(final CustomWebView webView){
        for(WebViewInfoHolder holder : webViews){
            if(holder.getWebView().equals(webView))
                return true;
        }
        return false;
    }
    class ViewHolder{
        int position;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.snapshot)
        ImageView snapshot;
        @OnClick({R.id.close_btn,R.id.title,R.id.snapshot})
        public void click(View view){
            if(view.getId()==R.id.close_btn){
                webViews.remove(position);
                if(webViews.isEmpty()){
                    if(homePresenter!=null&&tabStackDialog!=null){
                        homePresenter.clearWebHolder();
                        tabStackDialog.dismiss();
                    }
                }
                notifyDataSetChanged();
            }else{
                if(homePresenter!=null&&tabStackDialog!=null){
                    homePresenter.changeWebView(webViews.get(position).getWebView());
                    tabStackDialog.dismiss();
                }else{
                    FragmenUtil.switchToFragment(context,HomeView.newInstanceWithUrl(webViews.get(position).getWebView().getUrl()));
                }
            }
        }
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
