package com.jianhui_zhu.openuseragent.model;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.Record;
import com.jianhui_zhu.openuseragent.model.beans.User;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;
import com.jianhui_zhu.openuseragent.util.RemoteDatabaseSingleton;

import rx.Observable;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HomeModel {
    private boolean userLoggedIn = false;
    private User user;
    private Context context;

    public HomeModel(Context context) {
        this.context = context;
    }

    public void setUserLoggedIn(boolean isLoggedIn) {
        this.userLoggedIn = isLoggedIn;
    }


    public Observable<String> saveBookmark(String url, String name, String uID) {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(url);
        bookmark.setName(name);
        if (uID != null) {
            return RemoteDatabaseSingleton.getInstance(context).saveBookmark(bookmark);
        }
        return LocalDatabaseSingleton.getInstance(context).saveBookmark(bookmark);

    }

    public Observable<String> saveHistory(String url, String uID) {
        Record record = new Record();
        record.setUrl(url);
        record.setTimestamp(System.currentTimeMillis());
        if (uID != null) {
            return RemoteDatabaseSingleton.getInstance(context).saveHistory(record);
        }
        return LocalDatabaseSingleton.getInstance(context).saveHistory(record);
    }


}
