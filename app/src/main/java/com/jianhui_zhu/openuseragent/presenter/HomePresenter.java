package com.jianhui_zhu.openuseragent.presenter;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.HomeModel;
import com.jianhui_zhu.openuseragent.model.LoginModel;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class HomePresenter {
    private HomeViewInterface homeView;

    private HomeModel homeModel;

    public HomePresenter(HomeViewInterface homeView, Context context) {
        this.homeView=homeView;
        this.homeModel = new HomeModel(context);
    }

    public void saveBookmark(String url, String name, String uID) {
        Observable<String> observable;
        if (uID != null) {
            observable = homeModel.saveBookmark(url, name, uID);
        } else {
            observable = homeModel.saveBookmark(url, name, null);
        }
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                homeView.showTag(s);
            }
        });
    }

}
