package com.jianhui_zhu.openuseragent.view.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.BookmarkManager;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.util.AbstractDialogFragment;
import com.jianhui_zhu.openuseragent.view.adapter.BookmarkAdapter;
import com.jianhui_zhu.openuseragent.viewmodel.BookmarkViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianhuizhu on 2016-03-04.
 */
public class EditBookmarkDialog extends AbstractDialogFragment {
    BookmarkViewModel viewModel = new BookmarkViewModel();
    BookmarkManager manager = new BookmarkManager();
    CoordinatorLayout container;
    BookmarkAdapter adapter;
    Bookmark bookmark;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.url)
    EditText url;
    @OnClick(R.id.done_bookmark)
    public void update(){
        bookmark.setName(name.getText().toString());
        bookmark.setUrl(url.getText().toString());
        viewModel.updateBookmark(manager.updateBookmark(bookmark),container,adapter);
        dismiss();
    }
    @OnClick(R.id.delete_bookmark)
    public void delete(){
        viewModel.deleteBookmark(manager.deleteBookmark(bookmark),bookmark,container,adapter);
        dismiss();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_edit_bookmark,container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name.setText(bookmark.getName());
        url.setText(bookmark.getUrl());
    }

    public static EditBookmarkDialog newInstance(Bookmark bookmark, CoordinatorLayout container, BookmarkAdapter adapter) {
        EditBookmarkDialog fragment = new EditBookmarkDialog();
        fragment.bookmark = bookmark;
        fragment.container = container;
        fragment.adapter = adapter;
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
