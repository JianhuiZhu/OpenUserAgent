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
    private BookmarkAdapterInterface adapterInterface;
    public BookmarkPresenter(Context context, BookmarkViewInterface view, BookmarkAdapterInterface adapterInterface){
        if(instance==null) {
            this.context = context;
            bookmarkModel = new BookmarkModel(context);
            this.view = view;
            this.adapterInterface = adapterInterface;
            instance = this;
        }
    }

    public void setAdapterInterface(BookmarkAdapterInterface adapterInterface) {
        this.adapterInterface = adapterInterface;
    }

    public static BookmarkPresenter getInstance() throws Exception {
        if(instance==null){
            throw new Exception("Bookmark Presenter not instantiated");
        }
        return instance;
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
    public void addBookmark(final Bookmark bookmark){
        bookmarkModel.addBookmark(bookmark).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showTag(s);
                adapterInterface.addNewBookmark(bookmark);
            }
        });
    }
}
