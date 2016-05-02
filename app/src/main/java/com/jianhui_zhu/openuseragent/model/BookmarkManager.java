package com.jianhui_zhu.openuseragent.model;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;

import java.util.List;

import rx.Observable;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class BookmarkManager {
    public Observable<List<Bookmark>> getAllBookmarks(){
            return LocalDatabaseSingleton.getInstance().getAllBookmarks();
    }

    public Observable<Bookmark> updateBookmark(Bookmark bookmark){
            return LocalDatabaseSingleton.getInstance().updateBookmark(bookmark);

    }

    public Observable<String> deleteBookmark(Bookmark bookmark){
            return LocalDatabaseSingleton.getInstance().deleteBookmark(bookmark);
    }
    public Observable<String> addBookmark(Bookmark bookmark){
            return LocalDatabaseSingleton.getInstance().saveBookmark(bookmark);
    }
    public Observable<String> saveBookmark(String url, String name) {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(url);
        bookmark.setName(name);
        return LocalDatabaseSingleton.getInstance().saveBookmark(bookmark);

    }
}
