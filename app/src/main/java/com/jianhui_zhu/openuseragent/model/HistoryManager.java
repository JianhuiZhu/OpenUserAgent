package com.jianhui_zhu.openuseragent.model;


import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;
import java.util.List;

import rx.Observable;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HistoryManager {

    public Observable<List<History>> getAllHistory(){
        return LocalDatabaseSingleton.getInstance().getAllHistories();
    }
    public Observable<List<History>> getHistoryByDate(int year, int month, int day){
        return LocalDatabaseSingleton.getInstance().getHistoryByDate(year,month,day);
    }
    public Observable<String> deleteSelectedHistories(List<History> histories){
        return LocalDatabaseSingleton.getInstance().deleteSelectedHistory(histories);
    }
    public Observable<String> saveHistoryLocal(String url,String name){
        History history =new History();
        history.setName(name);
        history.setUrl(url);
        history.setTimestamp(System.currentTimeMillis());
        return LocalDatabaseSingleton.getInstance().saveHistory(history);
    }
    public Observable<List<History>> getTop8FrequentWebPage(){
        return LocalDatabaseSingleton.getInstance().getNavigationPages();
    }
}
