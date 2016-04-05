package com.jianhui_zhu.openuseragent.model;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.util.DataValidationUtil;
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
        if(RemoteDatabaseSingleton.getInstance().isUserLoggedIn()){
            return true;
        }
        return false;
    }
    public List<Bookmark> getAllBookmarks(){
        List<Bookmark> bookmarks;
        if(isUserLoggedIn()){
           bookmarks=RemoteDatabaseSingleton.getInstance().getAllBookmarks();
            for(Bookmark bookmark: LocalDatabaseSingleton.getInstance().getAllBookmarks()){
                bookmarks.add(bookmark);
            }
        }else{
            bookmarks=LocalDatabaseSingleton.getInstance().getAllBookmarks();
        }
        return bookmarks;
    }

    public Observable updateBookmark(Bookmark bookmark){
        if(RemoteDatabaseSingleton.getInstance().isUserLoggedIn()&& !DataValidationUtil.isInteger(bookmark.getbID())){
            return RemoteDatabaseSingleton.getInstance().updateBookmark(bookmark);
        }else{
            return LocalDatabaseSingleton.getInstance().updateBookmark(bookmark);
        }
    }

    public void deleteBookmark(Bookmark bookmark){
        if(RemoteDatabaseSingleton.getInstance().isUserLoggedIn() && !DataValidationUtil.isInteger(bookmark.getbID())){
            RemoteDatabaseSingleton.getInstance().deleteBookmark(bookmark);
        }else{
            LocalDatabaseSingleton.getInstance().deleteBookmark(bookmark);
        }
    }
    public Observable<String> addBookmark(Bookmark bookmark){
        if(RemoteDatabaseSingleton.getInstance().isUserLoggedIn()){
            return RemoteDatabaseSingleton.getInstance().saveBookmark(bookmark);
        }else{
            return LocalDatabaseSingleton.getInstance().saveBookmark(bookmark);
        }
    }
}
