package com.jianhui_zhu.openuseragent.model;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.util.LocalDatabaseSingleton;

import java.util.List;

import rx.Observable;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HistoryModel {
    Context context;
    public HistoryModel(Context context){
        this.context=context;
    }
    public Observable<List<History>> getAllHistory(){
        return LocalDatabaseSingleton.getInstance().getAllHistories();
    }
}
