package com.jianhui_zhu.openuseragent.view.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.util.AbstractDialogFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianhuizhu on 2016-03-04.
 */
public class BookmarkDialog extends AbstractDialogFragment {
    Bookmark bookmark;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.url)
    EditText url;
    @OnClick(R.id.done_bookmark)
    public void done(){
        //use id to update
    }
    @OnClick(R.id.delete_bookmark)
    public void delete(){
        //use id to delete
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookmark=getArguments().getParcelable("bookmark");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_bookmark,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name.setText(bookmark.getName());
        url.setText(bookmark.getUrl());
    }

    public static BookmarkDialog newInstance(Bookmark bookmark) {

        Bundle args = new Bundle();
        args.putParcelable("bookmark",bookmark);
        BookmarkDialog fragment = new BookmarkDialog();
        fragment.setArguments(args);
        return fragment;
    }
}
