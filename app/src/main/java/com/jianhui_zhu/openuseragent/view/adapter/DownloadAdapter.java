package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.DownloadedFile;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class DownloadAdapter  extends RecyclerView.Adapter<DownloadAdapter.ViewHolder>{
    Context context;
    private int lastPosition = -1;

    public void setDownloadedFiles(List<DownloadedFile> downloadedFiles) {
        this.downloadedFiles = downloadedFiles;
    }

    List<DownloadedFile> downloadedFiles = new ArrayList<>();
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_download, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DownloadedFile file = downloadedFiles.get(position);
        holder.position = position;
        holder.title.setText(file.getName());
        String filePath = file.getPath();
        if(filePath.length()>25){
            holder.url.setText(filePath.substring(0,22)+"...");
        }else{
            holder.url.setText(filePath);
        }
        setAnimation(holder.container,position);
    }

    @Override
    public int getItemCount() {
        return downloadedFiles==null ? 0 : downloadedFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         volatile int position;
        @Bind(R.id.download_title)
        TextView title;
        @Bind(R.id.download_url)
        TextView url;
        @Bind(R.id.download_delete)
        ImageView delete;
        @Bind(R.id.download_status_action)
        ImageView action;
        @Bind(R.id.download_item_container)
        CardView container;
        @OnClick({R.id.download_status_action,R.id.download_delete})
        public void click(ImageView view){
            switch (view.getId()){
                case R.id.download_delete:
                    downloadedFiles.remove(downloadedFiles.get(position));
                    notifyDataSetChanged();
                    break;
                case R.id.download_status_action:
                    String tag = (String)view.getTag();
                    if(tag.equals("finished")){
                        view.setImageResource(R.drawable.ic_redownload);
                        view.setTag("redownload");
                    }else if(tag.equals("paused")){
                        view.setImageResource(R.drawable.ic_play);
                        view.setTag("play");
                    }else if(tag.equals("play")){
                        view.setImageResource(R.drawable.ic_pause);
                        view.setTag("paused");
                    }else{
                        view.setImageResource(R.drawable.ic_finished);
                        view.setTag("finished");
                    }
                    break;
            }
        }


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            delete.setColorFilter(R.color.mdtp_accent_color, PorterDuff.Mode.MULTIPLY);
            action.setColorFilter(R.color.mdtp_accent_color, PorterDuff.Mode.MULTIPLY);
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
