package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by jianhuizhu on 2016-04-05.
 */
public class NavigationHomeAdapter extends RecyclerView.Adapter<NavigationHomeAdapter.ViewHolder> {
    HomeViewInterface home;
    Context context;
    private List<History> histories = new ArrayList<>();
    public NavigationHomeAdapter(HomeViewInterface viewInterface){
        home = viewInterface;
    }

    public void setFrequentVisitPages(List<History> histories) {
        this.histories.clear();
        this.histories.addAll(histories);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_bookmark, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        History history = histories.get(position);
        holder.position = position;
        holder.url.setText(history.getUrl());
        String host = null;
        try {
            host = WebUtil.getDomainByUrl(history.getUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        holder.title.setText(history.getName());
        WebUtil.getInstance().getIconByName(host).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                holder.icon.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return histories == null ? 0 : histories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int position;
        @Bind(R.id.bookmark_name)
        TextView title;
        @Bind(R.id.bookmark_avatar)
        ImageView icon;
        @Bind(R.id.bookmark_url)
        TextView url;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View v) {
            home.loadTargetUrl(histories.get(position).getUrl(),false);
        }
    }
}
