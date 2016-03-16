package com.jianhui_zhu.openuseragent.presenter;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.HistoryModel;
import com.jianhui_zhu.openuseragent.model.beans.History;

import java.util.List;

import rx.Observable;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HistoryPresenter {
    Context context;
    public HistoryPresenter(Context context){
        this.context=context;
    }
    private HistoryModel model=new HistoryModel(context);
    public Observable<List<History>> getAllHistory(){
        return model.getAllHistory();
    }
}
