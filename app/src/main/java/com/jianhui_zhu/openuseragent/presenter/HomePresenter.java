package com.jianhui_zhu.openuseragent.presenter;

import android.content.Context;
import android.database.Cursor;
import android.webkit.URLUtil;
import android.webkit.WebView;

import com.jianhui_zhu.openuseragent.model.HomeModel;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.view.custom.CustomWebView;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class HomePresenter {
    private static HomePresenter instance;
    private HomeViewInterface homeView;

    private HomeModel homeModel;
    public Observable<List<Bookmark>> getNavigationBookmark(){
        return homeModel.getNavigationBookmark();
    }
    public HomePresenter(HomeViewInterface homeView, Context context) {
        if(instance==null) {
            this.homeView = homeView;
            this.homeModel = new HomeModel(context);
            instance = this;
        }
    }
    public static HomePresenter getInstance() throws Exception {
        if(instance==null){
            throw new Exception("home presenter not instantiated");
        }
        return instance;
    }
    public void validateAndLoad(String word){
        if(URLUtil.isValidUrl(word)){
            homeView.loadTargetUrl(word);
        }else{
            homeView.searchTargetWord(word);
        }
    }
    public void saveHistory(String url,String name){
        homeModel.saveHistoryLocal(url,name);
    }
    public void saveBookmark(String url, String name, String uID) {
        Observable<String>  observable;
        if (uID != null) {
            observable=homeModel.saveBookmark(url, name, uID);
        } else {
            observable=homeModel.saveBookmark(url, name, null);
        }
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                homeView.showTag(s);
            }
        });
    }
    public void saveRecordLocally(String url,String name){
        homeModel.saveHistoryLocal(url,name);
    }
    public void incrementRecordLocally(int id,int count){
        homeModel.incrementRecordLocally(id,count);
    }
    public void queryText(String text){
        text=text.toLowerCase();
        homeModel.queryText(text).subscribe(new Action1<Cursor>() {
            @Override
            public void call(Cursor cursor) {
                homeView.swapCursor(cursor);
            }
        });
    }
    public void saveQuery(String query){
        query=query.toLowerCase();
        homeModel.saveQueryText(query);
    }
    public void hideKeyboard(){
        homeView.hideKeyboard();
    }
    public void changeToolbarVisibility(int VIEW_CODE){
        homeView.changeToolBarVisibility(VIEW_CODE);
    }
    public void swapUrl(String url){
        homeView.loadTargetUrl(url);
    }
    public void changeNumTabsIcon(int num){
        homeView.changeNumTabsIcon(num);

    }
    public void changeWebView(CustomWebView webView){
        homeView.changeWebView(webView);
    }
    public void clearWebHolder(){
        homeView.clearWebHolder();
    }
}
