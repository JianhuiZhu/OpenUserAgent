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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.presenter.BookmarkPresenter;
import com.jianhui_zhu.openuseragent.presenter.HomePresenter;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.view.BookmarkView;
import com.jianhui_zhu.openuseragent.view.HomeView;
import com.jianhui_zhu.openuseragent.view.dialogs.EditBookmarkDialog;
import com.jianhui_zhu.openuseragent.view.interfaces.BookmarkAdapterInterface;

import java.net.URISyntaxException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder>{
    List<Bookmark> bookmarks;
    Context context;
    private BookmarkView bookmarkView;
    private BookmarkAdapter adapterInterface;
    private BookmarkPresenter bookmarkPresenter;
    private int lastPosition = -1;
    private HomePresenter homePresenter;
    public  BookmarkAdapter(List<Bookmark> bookmarks
            ,Context context
            ,BookmarkPresenter bookmarkPresenter
            ,HomePresenter homePresenter,BookmarkView bookmarkView){
        this.bookmarks=bookmarks;
        this.context=context;
        this.bookmarkPresenter=bookmarkPresenter;
        this.homePresenter=homePresenter;
        this.bookmarkView=bookmarkView;
        adapterInterface=this;
    }

    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_bookmark, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final BookmarkAdapter.ViewHolder holder, int position) {
        Bookmark bookmark=bookmarks.get(position);
        holder.name.setText(bookmark.getName());
        holder.location=position;
        holder.url.setText(bookmark.getUrl());
        try {
            String host = WebUtil.getDomainbyUrl(bookmark.getUrl());
            WebUtil.getInstance().getIconByName(host).subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap bitmap) {
                    holder.avatar.setImageBitmap(bitmap);
                }
            });
            setAnimation(holder.container,position);
        } catch (URISyntaxException e) {
            Log.e(this.getClass().getSimpleName(),e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return bookmarks==null?0:bookmarks.size();
    }

    public void updatedBookmark(Bookmark bookmark) {
        int position = -1;
        for(int count = 0; count<bookmarks.size(); count++){
            if(bookmarks.get(count).equals(bookmark)){
                position = count;
                break;
            }
        }
        if(position != -1) {
            bookmarks.set(position,bookmark);
            this.notifyItemChanged(position);
        }
    }

    public void deletedBookmark(Bookmark bookmark) {
        int position = -1;
        for(int count = 0; count<bookmarks.size(); count++){
            if(bookmarks.get(count).equals(bookmark)){
                position = count;
                break;
            }
        }
        if(position != -1) {
            bookmarks.remove(position);
            this.notifyItemChanged(position);
            this.notifyItemRangeChanged(position,bookmarks.size());
        }
    }

    public void addNewBookmark(Bookmark bookmark) {
        bookmarks.add(bookmark);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener{
        @Bind(R.id.bookmark_container)
        RelativeLayout container;
        int location;
        @Bind(R.id.bookmark_avatar)
        ImageView avatar;
        @Bind(R.id.bookmark_name)
        TextView name;
        @Bind(R.id.bookmark_url)
        TextView url;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
        }


        @Override
        public boolean onLongClick(View v) {
           FragmenUtil.switchToFragment(context, EditBookmarkDialog.newInstance(bookmarks.get(location),bookmarkPresenter));
            return true;
        }

        @Override
        public void onClick(View v) {
            if(homePresenter==null)
                FragmenUtil.switchToFragment(context,HomeView.newInstanceWithUrl(bookmarks.get(location).getUrl()));
            else{
                homePresenter.swapUrl(bookmarks.get(location).getUrl());
                FragmenUtil.backToPreviousFragment(context,bookmarkView);
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
