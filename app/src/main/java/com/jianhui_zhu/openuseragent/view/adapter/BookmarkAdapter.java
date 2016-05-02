package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.CoordinatorLayout;
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
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.WebUtil;
import com.jianhui_zhu.openuseragent.view.BookmarkView;
import com.jianhui_zhu.openuseragent.view.HomeView;
import com.jianhui_zhu.openuseragent.view.dialogs.EditBookmarkDialog;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder>{
    HomeViewInterface home;
    BookmarkAdapter instance;
    CoordinatorLayout container;
    List<Bookmark> bookmarks = new ArrayList<>();
    Context context;
    private BookmarkView bookmarkView;
    private int lastPosition = -1;
    public  BookmarkAdapter(Context context,HomeViewInterface viewInterface,BookmarkView bookmarkView,CoordinatorLayout container){
        this.context=context;
        this.home = viewInterface;
        this.bookmarkView=bookmarkView;
        this.container = container;
        instance = this;
    }
    public void addAllBookmarks(List<Bookmark> bookmarks){
        this.bookmarks.clear();
        this.bookmarks.addAll(bookmarks);
        notifyDataSetChanged();
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
    public void onBindViewHolder(final BookmarkAdapter.ViewHolder holder, final int position) {
        Bookmark bookmark=bookmarks.get(position);
        holder.name.setText(bookmark.getName());
        holder.url.setText(bookmark.getUrl());
        try {
            String host = WebUtil.getDomainByUrl(bookmark.getUrl());
            WebUtil.getInstance().getIconByName(host).subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap bitmap) {
                    holder.avatar.setImageBitmap(bitmap);
                }
            });
            setAnimation(holder.layout,position);
        } catch (URISyntaxException e) {
            Log.e(this.getClass().getSimpleName(),e.getMessage());
        }
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FragmenUtil.switchToFragment(context, EditBookmarkDialog.newInstance(bookmarks.get(position),container,instance));
                return true;
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(home==null)
                    FragmenUtil.switchToFragment(context,HomeView.newInstanceWithUrl(bookmarks.get(position).getUrl()));
                else{
                    home.loadTargetUrl(bookmarks.get(position).getUrl());
                    FragmenUtil.backToPreviousFragment(context,bookmarkView);
                }
            }
        });
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


    public class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.bookmark_container)
        RelativeLayout layout;
        @Bind(R.id.bookmark_avatar)
        ImageView avatar;
        @Bind(R.id.bookmark_name)
        TextView name;
        @Bind(R.id.bookmark_url)
        TextView url;
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
