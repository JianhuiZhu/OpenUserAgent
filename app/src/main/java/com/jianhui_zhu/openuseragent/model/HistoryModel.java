package com.jianhui_zhu.openuseragent.model;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;
import com.jianhui_zhu.openuseragent.view.interfaces.HistoryViewInterface;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HistoryModel {
    Context context;
    HistoryViewInterface view;
    public HistoryModel(Context context){
        this.context=context;
    }
    public Observable<List<History>> getAllHistory(){
        return LocalDatabaseSingleton.getInstance().getAllHistories();
    }
    public Observable<List<History>> getHistoryByDate(Date date){
        return LocalDatabaseSingleton.getInstance().getHistoryByDate(date);
    }
}
