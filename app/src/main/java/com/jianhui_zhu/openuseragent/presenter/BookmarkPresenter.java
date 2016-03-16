package com.jianhui_zhu.openuseragent.presenter;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.BookmarkModel;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.view.BookmarkView;
import com.jianhui_zhu.openuseragent.view.interfaces.BookmarkViewInterface;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class BookmarkPresenter {
    private Context context;
    private BookmarkModel bookmarkModel;
    private BookmarkViewInterface view;
    public BookmarkPresenter(Context context,BookmarkViewInterface view){
        this.context=context;
        bookmarkModel=new BookmarkModel(context);
        this.view=view;
    }
    public List<Bookmark> getAllBookmarks(){
        return bookmarkModel.getAllBookmarks();
    }

    public void updateBookmark(Bookmark bookmark){
        bookmarkModel.updateBookmark(bookmark).subscribe(new Action1() {
            @Override
            public void call(Object o) {
                view.showTag("bookmark updated");
            }
        });
    }

    public void deleteBookmark(Bookmark bookmark){

        bookmarkModel.deleteBookmark(bookmark);
    }
}
