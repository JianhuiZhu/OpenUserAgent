package com.jianhui_zhu.openuseragent.view.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.presenter.BookmarkPresenter;
import com.jianhui_zhu.openuseragent.util.AbstractDialogFragment;
import com.jianhui_zhu.openuseragent.util.activity.MainActivity;
import com.jianhui_zhu.openuseragent.view.interfaces.BookmarkAdapterInterface;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jianhuizhu on 2016-04-05.
 */
public class NewBookmarkDialog extends AbstractDialogFragment {
    CoordinatorLayout container;
    BookmarkPresenter presenter;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.url)
    EditText url;
    @OnClick({R.id.done_bookmark,R.id.cancel_bookmark})
    public void onClick(View view){
        if(view.getId()==R.id.done_bookmark){
            if(name.getText().equals("")||url.getText().equals("")){
                Snackbar.make(container,"Please fill both name and url",Snackbar.LENGTH_SHORT).show();
                return;
            }
            Bookmark bookmark = new Bookmark();
            bookmark.setName(name.getText().toString());
            bookmark.setUrl(url.getText().toString());
             presenter.addBookmark(bookmark);
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
    public static NewBookmarkDialog newInstance(BookmarkPresenter presenter){
        NewBookmarkDialog dialog = new NewBookmarkDialog();
        dialog.presenter = presenter;
        return dialog;
    }
}
