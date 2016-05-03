package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.WebViewInfoHolder;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.view.HomeView;
import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;
import com.jianhui_zhu.openuseragent.view.dialogs.TabStackDialog;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianhuizhu on 2016-04-29.
 */
public class WebViewAdapter extends RecyclerView.Adapter<WebViewAdapter.ViewHolder> {
    private Context context;
    private HomeViewInterface home;
    private ArrayList<WebViewInfoHolder> webViews=new ArrayList<>();
    private static WebViewAdapter instance;
    public void destoryAll(){
        for(WebViewInfoHolder holder: webViews){
            holder.getWebView().destroy();
            webViews.remove(holder);
        }
    }
    public TabStackDialog getTabStackDialog() {
        return tabStackDialog;
    }

    public void setTabStackDialog(TabStackDialog tabStackDialog) {
        this.tabStackDialog = tabStackDialog;
    }

    private TabStackDialog tabStackDialog = null;
    public static synchronized WebViewAdapter getInstance(HomeViewInterface viewInterface){
        if(WebViewAdapter.instance==null){
            instance = new WebViewAdapter();
            instance.home = viewInterface;
        }
        return instance;
    }
    public void addWebView(final CustomWebView webView){
        if(isWebViewExists(webView)==false) {
            WebViewInfoHolder holder = new WebViewInfoHolder();
            holder.setWebView(webView);
            String title = webView.getTitle();
            String domain ="";
            try {
                domain = WebUtil.getDomainByUrl(webView.getUrl());
            } catch (URISyntaxException e) {
                Log.d(this.getClass().getSimpleName(),e.toString());
            }
            holder.setTitle(title.equals("") ? domain : title);
            webViews.add(0,holder);
            notifyDataSetChanged();
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context==null) {
            this.context = parent.getContext();
        }
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_webview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final WebViewInfoHolder webviewHolder = webViews.get(position);
        String title = webViews.get(position).getTitle();
        holder.title.setText(title);
        Bitmap bitmap = webviewHolder.getWebView().getDrawingCache();
        holder.snapshot.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return webViews == null ? 0 : webViews.size();
    }
    private boolean isWebViewExists(final CustomWebView webView){
        for(WebViewInfoHolder holder : webViews){
            if(holder.getWebView().equals(webView))
                return true;
        }
        return false;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.snapshot)
        ImageView snapshot;
        @OnClick({R.id.close_btn,R.id.title,R.id.snapshot})
        public void click(View view){
            if(view.getId()==R.id.close_btn){
                webViews.remove(getAdapterPosition());
                if(webViews.isEmpty()){
                    if(home!=null&&tabStackDialog!=null){
                        home.clearWebHolder();
                        tabStackDialog.dismiss();
                    }
                }
                notifyDataSetChanged();
            }else{
                if(home!=null&&tabStackDialog!=null){
                    home.changeWebView(webViews.get(getAdapterPosition()).getWebView());
                    tabStackDialog.dismiss();
                }else{
                    FragmenUtil.switchToFragment(context, HomeView.newInstanceWithUrl(webViews.get(getAdapterPosition()).getWebView().getUrl()));
                }
            }
        }
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
