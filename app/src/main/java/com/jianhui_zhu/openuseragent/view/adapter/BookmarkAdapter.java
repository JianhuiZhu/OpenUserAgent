package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.view.HomeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder>{
    public  BookmarkAdapter(List<Bookmark> bookmarks,Context context){
        this.bookmarks=bookmarks;
        this.context=context;
    }
    List<Bookmark> bookmarks;
    Context context;
    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.card_bookmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookmarkAdapter.ViewHolder holder, int position) {
        Bookmark bookmark=bookmarks.get(position);
        holder.name.setText(bookmark.getName());
        holder.url.setText(bookmark.getUrl());
    }

    @Override
    public int getItemCount() {
        return bookmarks==null?0:bookmarks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.bookmark_name)
        TextView name;
        @Bind(R.id.bookmark_url)
        TextView url;
        @OnClick(R.id.bookmark_url)
        public void click(TextView view){
            FragmenUtil.switchToFragment(context, HomeView.newInstanceWithUrl(view.getText().toString()));
        }
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
