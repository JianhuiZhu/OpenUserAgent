package com.jianhui_zhu.openuseragent.presenter;

import com.jianhui_zhu.openuseragent.model.HomeModel;
import com.jianhui_zhu.openuseragent.model.LoginModel;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class HomePresenter {
    private HomeViewInterface homeView;
    private HomeModel homeModel;
    public HomePresenter(HomeViewInterface homeView){
        this.homeView=homeView;
    }

}
