package com.jianhui_zhu.openuseragent.model;

import android.content.Context;
import android.database.Cursor;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;

import java.io.BufferedReader;
import java.io.File;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HomeViewManager {
    public static final String BLACK_LIST_DIR = "blacklist";

    public void incrementRecordLocally(int id,int count){
        LocalDatabaseSingleton.getInstance().incrementQueryRecordCount(id,count);
    }
    public Observable<Cursor> queryText(String text){
        return LocalDatabaseSingleton.getInstance().getQueryRecord(text);
    }
    public void saveQueryText(String text){
        LocalDatabaseSingleton.getInstance().saveQueryRecord(text);
    }
    public void getGlobalBlackList(Observable<Set<String>>observable, final Set<String> globalBlackList){
        observable.subscribe(new Action1<Set<String>>() {
            @Override
            public void call(Set<String> strings) {
                globalBlackList.addAll(strings);
            }
        });
    }
}
