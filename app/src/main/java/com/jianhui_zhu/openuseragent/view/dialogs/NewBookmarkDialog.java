package com.jianhui_zhu.openuseragent.view.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.BookmarkManager;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.util.activity.MainActivity;
import com.jianhui_zhu.openuseragent.view.adapter.BookmarkAdapter;
import com.jianhui_zhu.openuseragent.viewmodel.BookmarkViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianhuizhu on 2016-04-05.
 */
public class NewBookmarkDialog extends DialogFragment {
    BookmarkAdapter adapter;
    CoordinatorLayout container;
    BookmarkManager manager = new BookmarkManager();
    BookmarkViewModel viewModel = new BookmarkViewModel();
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.url)
    EditText url;
    @OnClick({R.id.done_bookmark,R.id.cancel_bookmark})
    public void onClick(View view){
        if(view.getId()==R.id.done_bookmark){
            boolean setFocus = false;
            if(name.getText().length()==0){
                name.setError(getString(R.string.new_bookmark_name_error_msg));
                name.requestFocus();
                setFocus = true;
            }
            if(url.getText().length()==0){
                url.setError(getString(R.string.new_bookmark_url_error_msg));
                if(!setFocus){
                    url.requestFocus();
                    setFocus = true;
                }
            }
            if(setFocus){
                return;
            }
            Bookmark bookmark = new Bookmark();
            bookmark.setName(name.getText().toString());
            bookmark.setUrl(url.getText().toString());
            viewModel.addBookmark(manager.addBookmark(bookmark),bookmark,container,adapter);
            this.dismiss();
        }else{
            this.dismiss();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_add_new_bookmark,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container = ((MainActivity)getActivity()).getContainer();
    }
    public static NewBookmarkDialog newInstance(BookmarkAdapter adapter){
        NewBookmarkDialog dialog = new NewBookmarkDialog();
        dialog.adapter = adapter;
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
