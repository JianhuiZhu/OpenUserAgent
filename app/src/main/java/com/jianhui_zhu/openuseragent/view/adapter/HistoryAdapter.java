package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.presenter.HistoryPresenter;
import com.jianhui_zhu.openuseragent.util.WebIconUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private Context context;
    private HistoryPresenter presenter;
    private int lastPosition = -1;

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
        notifyDataSetChanged();
    }
    public void changeHistoriesDataSet(List<History> histories){
        this.histories.clear();
        this.histories.addAll(histories);
        notifyDataSetChanged();
    }
    List<History> histories;
    public HistoryAdapter(Context context,HistoryPresenter presenter,List<History> histories){
        this.context=context;
        this.presenter=presenter;
        this.histories=histories;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        History history=histories.get(position);
        try {
            URI uri=new URI(history.getUrl());
            String time=history.getTimestamp();
            WebIconUtil.getInstance().getIconByName(uri.getHost()).subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap result) {
                    holder.avatar.setImageBitmap(result);
                }
            });
            holder.title.setText(history.getName());
            holder.time.setText(time);
            setAnimation(holder.container,position);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return histories == null ? 0 : histories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_history_container)
        RelativeLayout container;
        @Bind(R.id.history_avatar)
        ImageView avatar;
        @Bind(R.id.history_title)
        TextView title;
        @Bind(R.id.history_time)
        TextView time;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
