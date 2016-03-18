package com.jianhui_zhu.openuseragent.model;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.model.beans.User;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;
import com.jianhui_zhu.openuseragent.util.RemoteDatabaseSingleton;

import rx.Observable;
import rx.functions.Action1;

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


    public void saveBookmark(String url, String name, String uID) {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(url);
        bookmark.setName(name);

        if (uID != null) {
            RemoteDatabaseSingleton.getInstance().saveBookmark(bookmark);
        }
        LocalDatabaseSingleton.getInstance().saveBookmark(bookmark);

    }

    public void saveHistory(String url, String uID) {
        History history = new History();
        history.setUrl(url);

        history.setTimestamp(System.currentTimeMillis());
        if (uID != null) {
            RemoteDatabaseSingleton.getInstance().saveHistory(history);
        }
        LocalDatabaseSingleton.getInstance().saveHistory(history);
    }
    public void saveHistoryLocal(String url,String name){
        History history =new History();
        history.setName(name);
        history.setUrl(url);
        history.setTimestamp(System.currentTimeMillis());
        LocalDatabaseSingleton.getInstance().saveHistory(history);
    }
    public void incrementRecordLocally(int id,int count){
        LocalDatabaseSingleton.getInstance().incrementQueryRecordCount(id,count);
    }
    public Observable<Cursor> queryText(String text){
        return LocalDatabaseSingleton.getInstance().getQueryRecord(text);
    }
    public void saveQueryText(String text){
        LocalDatabaseSingleton.getInstance()
                .saveQueryRecord(text);
    }
}
