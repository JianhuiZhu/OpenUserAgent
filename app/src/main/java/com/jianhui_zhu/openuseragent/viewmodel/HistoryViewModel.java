package com.jianhui_zhu.openuseragent.viewmodel;

import android.graphics.PorterDuff;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.ImageView;

import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.view.adapter.HistoryAdapter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by jianhuizhu on 2016-04-29.
 */
public class HistoryViewModel {
    public void changeGarbageIconStatus(boolean status, ImageView deleteBtn){
        if(status){
            deleteBtn.clearColorFilter();
        }else {
            deleteBtn.setColorFilter(R.color.mdtp_light_gray, PorterDuff.Mode.MULTIPLY);
        }
    }

    public void inflateAllHistories(Observable<List<History>> observable, final HistoryAdapter adapter){
        observable.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<History>>() {
                    @Override
                    public void call(List<History> histories) {
                        adapter.addAllHistories(histories);
                    }
                });
    }
    public void inflateHistoriesByDate(Observable<List<History>>observable, final HistoryAdapter adapter){
        observable.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<History>>() {
                    @Override
                    public void call(List<History> histories) {
                        adapter.changeHistoriesDataSet(histories);
                    }
                });
    }
    public void deleteSelectedHistories(Observable<String> observable, final CoordinatorLayout container, final HistoryAdapter adapter){
        observable.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        adapter.deleteSelected();
                        Snackbar.make(container,s,Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}
