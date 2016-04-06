package com.jianhui_zhu.openuseragent.presenter;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.HistoryModel;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.view.interfaces.HistoryViewInterface;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by jianhuizhu on 2016-02-16.
 */
public class HistoryPresenter {
    Context context;
    HistoryViewInterface view;
    public HistoryPresenter(Context context,HistoryViewInterface view){
        this.context=context;
        this.view=view;
    }
    private HistoryModel model=new HistoryModel(context);
    public void changeGarbageIconStatus(boolean status){
        view.setGarbageIconStatus(status);
    }
    public Observable<List<History>> getAllHistory(){
        return model.getAllHistory();
    }
    public void getHistoryByDate(int year, int month, int day){
        model.getHistoryByDate(year,month, day).subscribe(new Action1<List<History>>() {
            @Override
            public void call(List<History> histories) {
                view.refreshList(histories);
            }
        });
    }
    public Observable<String> deleteSelectedHistories(List<History> histories){
        return model.deleteSelectedHistories(histories);
    }
}
