package com.jianhui_zhu.openuseragent.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;
import com.jianhui_zhu.openuseragent.util.RemoteDatabaseSingleton;
import com.jianhui_zhu.openuseragent.view.adapter.BookmarkAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class BookmarkView extends AbstractFragment{
    @Bind(R.id.list)
    RecyclerView bookmarkList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_bookmark, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Bookmark> bookmarks=new ArrayList<>();
        if(RemoteDatabaseSingleton.getInstance(getActivity()).isUserLoggedIn()) {
            bookmarks.addAll(RemoteDatabaseSingleton.getInstance(getActivity()).getAllBookmarks());
        }
        bookmarks.addAll(LocalDatabaseSingleton.getInstance(getActivity()).getAllBookmarks());
        bookmarkList.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookmarkList.setItemAnimator(new DefaultItemAnimator());
        bookmarkList.setHasFixedSize(true);
        BookmarkAdapter bookmarkAdapter=new BookmarkAdapter(bookmarks,getActivity());
        bookmarkList.setAdapter(bookmarkAdapter);

    }


}
