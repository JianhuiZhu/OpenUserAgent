package com.jianhui_zhu.openuseragent.presenter;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.BookmarkModel;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.view.BookmarkView;
import com.jianhui_zhu.openuseragent.view.interfaces.BookmarkAdapterInterface;
import com.jianhui_zhu.openuseragent.view.interfaces.BookmarkViewInterface;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class BookmarkPresenter {
    private static BookmarkPresenter instance;
    private Context context;
    private BookmarkModel bookmarkModel;
    private BookmarkViewInterface view;
    private BookmarkPresenter(Context context, BookmarkViewInterface view){
        if(instance==null) {
            this.context = context;
            bookmarkModel = new BookmarkModel(context);
            this.view = view;
            instance = this;
        }
    }

    public static BookmarkPresenter getInstance(Context context, BookmarkViewInterface view){
        if(instance==null){
            new BookmarkPresenter(context,view);
        }
        return instance;
    }
    public List<Bookmark> getAllBookmarks(){
        return bookmarkModel.getAllBookmarks();
    }

    public void updateBookmark(final Bookmark bookmark){
        bookmarkModel.updateBookmark(bookmark).subscribe(new Action1() {
            @Override
            public void call(Object o) {
                view.updateBookmark(bookmark);
                view.showTag("bookmark updated");
            }
        });
    }

    public void deleteBookmark(Bookmark bookmark){
        view.deleteBookmark(bookmark);
        bookmarkModel.deleteBookmark(bookmark);
    }
    public void addBookmark(final Bookmark bookmark){
        bookmarkModel.addBookmark(bookmark).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.addNewBookmark(bookmark);
                view.showTag(s);

            }
        });
    }
}
