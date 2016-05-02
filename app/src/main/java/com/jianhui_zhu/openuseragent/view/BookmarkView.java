package com.jianhui_zhu.openuseragent.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.BookmarkManager;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.util.activity.MainActivity;
import com.jianhui_zhu.openuseragent.view.adapter.BookmarkAdapter;
import com.jianhui_zhu.openuseragent.view.dialogs.NewBookmarkDialog;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;
import com.jianhui_zhu.openuseragent.viewmodel.BookmarkViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class BookmarkView extends Fragment{
    HomeViewInterface viewInterface;
    CoordinatorLayout container;
    @Bind(R.id.list)
    RecyclerView bookmarkList;
    BookmarkAdapter bookmarkAdapter;
    BookmarkManager manager = new BookmarkManager();
    BookmarkViewModel viewModel = new BookmarkViewModel();
    @OnClick({R.id.general_tool_bar_go_back,R.id.add_bookmark_manual_btn})
    public void click(View view){
        if(view.getId()==R.id.general_tool_bar_go_back)
            FragmenUtil.backToPreviousFragment(getActivity(),this);
        else
            FragmenUtil.switchToFragment(getActivity(),NewBookmarkDialog.newInstance(bookmarkAdapter));
    }

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
        container = ((MainActivity)getActivity()).getContainer();
        bookmarkList.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookmarkList.setItemAnimator(new DefaultItemAnimator());
        bookmarkList.setHasFixedSize(true);
        bookmarkAdapter=new BookmarkAdapter(getActivity(),viewInterface,this,container);
        bookmarkList.setAdapter(bookmarkAdapter);
        viewModel.inflateAllBookmarks(manager.getAllBookmarks(),bookmarkAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    public static BookmarkView newInstance(HomeViewInterface viewInterface){
        BookmarkView bookmarkView = new BookmarkView();
        bookmarkView.viewInterface = viewInterface;
        return bookmarkView;
    }
}
