package com.jianhui_zhu.openuseragent.viewmodel;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.view.adapter.BookmarkAdapter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by jianhuizhu on 2016-04-29.
 */
public class BookmarkViewModel {
    public void addBookmark(Observable<String>observable, final Bookmark bookmark, final CoordinatorLayout container, final BookmarkAdapter adapter){
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        adapter.addNewBookmark(bookmark);
                        Snackbar.make(container,s,Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteBookmark(Observable<String>observable, final Bookmark bookmark, final CoordinatorLayout container, final BookmarkAdapter adapter){
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        adapter.deletedBookmark(bookmark);
                        Snackbar.make(container,s,Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
    public void updateBookmark(Observable<Bookmark>observable,final CoordinatorLayout container,final BookmarkAdapter adapter){
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bookmark>() {
                    @Override
                    public void call(Bookmark bookmark) {
                        adapter.updatedBookmark(bookmark);
                        Snackbar.make(container,bookmark.getName()+" updated",Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
    public void inflateAllBookmarks(Observable<List<Bookmark>> observable, final BookmarkAdapter adapter){
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Bookmark>>() {
                    @Override
                    public void call(List<Bookmark> bookmarks) {
                        adapter.addAllBookmarks(bookmarks);
                    }
                });
    }
}
