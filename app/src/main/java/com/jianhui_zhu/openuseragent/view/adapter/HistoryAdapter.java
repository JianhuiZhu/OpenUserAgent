package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.presenter.HistoryPresenter;
import com.jianhui_zhu.openuseragent.presenter.HomePresenter;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.view.HistoryView;
import com.jianhui_zhu.openuseragent.view.HomeView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private HomePresenter homePresenter;
    private HistoryView historyView;
    private Context context;
    private HistoryPresenter presenter;
    private int lastPosition = -1;
    private List<History> selected = new ArrayList<>();
    public List<History> getHistories() {
        return histories;
    }

    public List<History> getSelected() {
        return selected;
    }

    public void setSelected(List<History> selected) {
        this.selected = selected;
    }
    public void deleteSelected(){
        for(History history : selected){
            histories.remove(history);
        }
        selected.clear();
        notifyDataSetChanged();
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
    public HistoryAdapter(Context context,HistoryPresenter presenter,List<History> histories,HomePresenter homePresenter, HistoryView historyView){
        this.context=context;
        this.presenter=presenter;
        this.histories=histories;
        this.homePresenter=homePresenter;
        this.historyView=historyView;

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
            String host = WebUtil.getDomainbyUrl(history.getUrl());
            Date date=new Date(history.getTimestamp());
            holder.position=position;
            WebUtil.getInstance().getIconByName(host).subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap result) {
                    holder.avatar.setImageBitmap(result);
                }
            });
            //int hour = date.getHours();
            //String  min = date.getMinutes()<10 ? "0"+date.getMinutes() : String.valueOf(date.getMinutes());
            //String time = hour+":"+min;
            holder.title.setText(history.getName());
            //holder.time.setText(time);
            holder.url.setText(history.getUrl());
            setAnimation(holder.container,position);
        } catch (URISyntaxException e) {
            Log.e(this.getClass().getSimpleName(),e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return histories == null ? 0 : histories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.item_history_container)
        RelativeLayout container;
        @Bind(R.id.history_avatar)
        ImageView avatar;
        volatile int position;
        @Bind(R.id.history_title)
        TextView title;
        @OnClick(R.id.history_item_checkbox)
        public void click(CheckBox checkBox){
            History history = histories.get(position);
            if(checkBox.isChecked()){
                selected.add(history);
                    presenter.changeGarbageIconStatus(true);

            }else{
                selected.remove(history);
                if(selected.isEmpty()){
                    presenter.changeGarbageIconStatus(false);
                }


            }
        }
//        @Bind(R.id.history_time)
//        TextView time;
        @Bind(R.id.history_url)
        TextView url;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(homePresenter==null)
                FragmenUtil.switchToFragment(context, HomeView.newInstanceWithUrl(histories.get(position).getUrl()));
            else{
                homePresenter.swapUrl(histories.get(position).getUrl());
                FragmenUtil.backToPreviousFragment(context,historyView);
            }
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
