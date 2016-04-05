package com.jianhui_zhu.openuseragent.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.presenter.BookmarkPresenter;
import com.jianhui_zhu.openuseragent.presenter.HomePresenter;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;
import com.jianhui_zhu.openuseragent.util.RemoteDatabaseSingleton;
import com.jianhui_zhu.openuseragent.view.adapter.BookmarkAdapter;
import com.jianhui_zhu.openuseragent.view.interfaces.BookmarkViewInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class BookmarkView extends AbstractFragment implements BookmarkViewInterface{
    @Bind(R.id.list)
    RecyclerView bookmarkList;
    @OnClick(R.id.general_tool_bar_go_back)
    public void click(){
        FragmenUtil.backToPreviousFragment(getActivity(),this);
    }
    BookmarkPresenter presenter;
    BookmarkAdapter bookmarkAdapter;
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

        bookmarkList.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookmarkList.setItemAnimator(new DefaultItemAnimator());
        bookmarkList.setHasFixedSize(true);
        List<Bookmark> bookmarks=new ArrayList<>();
        HomePresenter homePresenter= null;
        try {
            homePresenter = HomePresenter.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        presenter =new BookmarkPresenter(getActivity(),this,null);
        this.bookmarkAdapter=new BookmarkAdapter(bookmarks,getActivity(),presenter,homePresenter,this);
        presenter.setAdapterInterface(this.bookmarkAdapter);
        bookmarkList.setAdapter(bookmarkAdapter);
        bookmarks.addAll(presenter.getAllBookmarks());
    }


    @Override
    public void showTag(String str) {
        Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();
    }
}
