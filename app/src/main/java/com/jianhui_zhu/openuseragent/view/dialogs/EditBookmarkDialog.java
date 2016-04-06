package com.jianhui_zhu.openuseragent.view.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.presenter.BookmarkPresenter;
import com.jianhui_zhu.openuseragent.util.AbstractDialogFragment;
import com.jianhui_zhu.openuseragent.view.interfaces.BookmarkAdapterInterface;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianhuizhu on 2016-03-04.
 */
public class EditBookmarkDialog extends AbstractDialogFragment {
    BookmarkPresenter presenter;
    Bookmark bookmark;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.url)
    EditText url;
    @OnClick(R.id.done_bookmark)
    public void done(){
        bookmark.setName(name.getText().toString());
        bookmark.setUrl(url.getText().toString());
        presenter.updateBookmark(bookmark);
        dismiss();
    }
    @OnClick(R.id.delete_bookmark)
    public void delete(){
        presenter.deleteBookmark(bookmark);
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

    public static EditBookmarkDialog newInstance(Bookmark bookmark, BookmarkPresenter presenter) {

        EditBookmarkDialog fragment = new EditBookmarkDialog();
        fragment.bookmark = bookmark;
        fragment.presenter = presenter;
        return fragment;
    }


}
