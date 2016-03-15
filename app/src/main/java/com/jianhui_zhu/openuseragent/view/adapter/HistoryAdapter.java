package com.jianhui_zhu.openuseragent.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;

import butterknife.Bind;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.history_avatar)
        ImageView avatar;
        @Bind(R.id.history_title)
        TextView title;
        @Bind(R.id.history_time)
        TextView time;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
