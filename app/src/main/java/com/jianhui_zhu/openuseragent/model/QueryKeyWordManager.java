package com.jianhui_zhu.openuseragent.model;

import android.database.Cursor;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;

import rx.Observable;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class QueryKeyWordManager {


    public void incrementRecordLocally(int id,int count){
        LocalDatabaseSingleton.getInstance().incrementQueryRecordCount(id,count);
    }
    public Observable<Cursor> queryText(String text){
        return LocalDatabaseSingleton.getInstance().getQueryRecord(text);
    }
    public void saveQueryText(String text){
        LocalDatabaseSingleton.getInstance().saveQueryRecord(text);
    }
}
