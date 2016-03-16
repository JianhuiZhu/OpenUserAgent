package com.jianhui_zhu.openuseragent.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.presenter.BookmarkPresenter;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.WebIconUtil;
import com.jianhui_zhu.openuseragent.view.HomeView;
import com.jianhui_zhu.openuseragent.view.dialogs.BookmarkDialog;
import com.jianhui_zhu.openuseragent.view.interfaces.BookmarkAdapterInterface;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> implements BookmarkAdapterInterface{
    List<Bookmark> bookmarks;
    Context context;
    RecyclerView recyclerView;
    private BookmarkAdapter adapterInterface;
    private BookmarkPresenter bookmarkPresenter;
    public  BookmarkAdapter(List<Bookmark> bookmarks,Context context,RecyclerView recyclerView,BookmarkPresenter bookmarkPresenter){
        this.bookmarks=bookmarks;
        this.context=context;
        this.recyclerView=recyclerView;
        this.bookmarkPresenter=bookmarkPresenter;
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
        try {
            URI uri=new URI(bookmark.getUrl());
            WebIconUtil.getInstance().getIconByName(uri.getHost()).subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap bitmap) {
                    holder.avatar.setImageBitmap(bitmap);
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return bookmarks==null?0:bookmarks.size();
    }

    @Override
    public void updatedBookmark(int position, Bookmark bookmark) {
        Bookmark cur=bookmarks.get(position);
        bookmarkPresenter.updateBookmark(bookmark);
        cur.setName(bookmark.getName());
        cur.setUrl(bookmark.getUrl());
        this.notifyItemChanged(position);
    }

    @Override
    public void deletedBookmark(int position) {
        Bookmark bookmark=bookmarks.get(position);
        bookmarkPresenter.deleteBookmark(bookmark);

        bookmarks.remove(position);
        recyclerView.removeViewAt(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position,bookmarks.size());

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener{
        int location;
        @Bind(R.id.bookmark_avatar)
        ImageView avatar;
        @Bind(R.id.bookmark_name)
        TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
        }


        @Override
        public boolean onLongClick(View v) {
           FragmenUtil.switchToFragment(context,BookmarkDialog.newInstance(bookmarks.get(location),location,adapterInterface));
            return true;
        }

        @Override
        public void onClick(View v) {
            FragmenUtil.switchToFragment(context,HomeView.newInstanceWithUrl(bookmarks.get(location).getUrl()));
        }
    }

}
