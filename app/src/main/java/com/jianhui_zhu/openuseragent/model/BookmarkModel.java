package com.jianhui_zhu.openuseragent.model;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;
import com.jianhui_zhu.openuseragent.util.RemoteDatabaseSingleton;

import java.util.List;

import rx.Observable;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class BookmarkModel {
    private Context context;
    public BookmarkModel(Context context){
        this.context=context;
    }
    private boolean isUserLoggedIn(){
        if(RemoteDatabaseSingleton.getInstance(context).isUserLoggedIn()){
            return true;
        }
        return false;
    }
    public List<Bookmark> getAllBookmarks(){
        List<Bookmark> bookmarks;
        if(isUserLoggedIn()){
           bookmarks=RemoteDatabaseSingleton.getInstance(context).getAllBookmarks();
            for(Bookmark bookmark: LocalDatabaseSingleton.getInstance(context).getAllBookmarks()){
                bookmarks.add(bookmark);
            }
        }else{
            bookmarks=LocalDatabaseSingleton.getInstance(context).getAllBookmarks();
        }
        return bookmarks;
    }

    public Observable updateBookmark(Bookmark bookmark){
        if(RemoteDatabaseSingleton.getInstance(context).isUserLoggedIn()){
            return RemoteDatabaseSingleton.getInstance(context).updateBookmark(bookmark);
        }else{
            return LocalDatabaseSingleton.getInstance(context).updateBookmark(bookmark);
        }
    }

    public Observable deleteBookmark(Bookmark bookmark){
        if(RemoteDatabaseSingleton.getInstance(context).isUserLoggedIn()){
            return RemoteDatabaseSingleton.getInstance(context).removeBookmark(bookmark);
        }else{
            return LocalDatabaseSingleton.getInstance(context).deleteBookmark(bookmark);
        }
    }
}
