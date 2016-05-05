package com.jianhui_zhu.openuseragent.viewmodel;

import android.database.Cursor;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.webkit.URLUtil;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.view.HomeView;
import com.jianhui_zhu.openuseragent.view.adapter.NavigationHomeAdapter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by jianhuizhu on 2016-04-29.
 */
public class HomeViewModel {


    public void validateAndLoad(HomeView homeView,String word){
        if(URLUtil.isValidUrl(word)){
            homeView.loadTargetUrl(word);
        }else{
            homeView.searchTargetWord(word);
        }
    }
    public void saveHistory(Observable<String>observable){
        observable.subscribe();
    }
    public void saveBookmark(Observable<String>observable, final CoordinatorLayout container) {
        observable.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Snackbar.make(container,s,Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
    public void queryText(Observable<Cursor> observable,final HomeView homeView){
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cursor>() {
                    @Override
                    public void call(Cursor cursor) {
                        homeView.swapCursor(cursor);
                    }
                });
    }

    public void inflatePanelView(Observable<List<History>>observable, final NavigationHomeAdapter adapter, final RecyclerView view){
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<History>>() {
                    @Override
                    public void call(List<History> histories) {
                        adapter.setFrequentVisitPages(histories);
                        view.setAdapter(adapter);
                    }
                });
    }
    public void modifyGlobalBlackList(Observable<String> observable, final CoordinatorLayout container){
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Snackbar.make(container,s,Snackbar.LENGTH_SHORT).show();
            }
        });
    }

}
