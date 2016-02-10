package com.jianhui_zhu.openuseragent.presenter;

import com.jianhui_zhu.openuseragent.model.User;
import com.jianhui_zhu.openuseragent.view.interfaces.HomeViewInterface;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class HomePresenter {
    private User user;
    private HomeViewInterface homeView;
    public HomePresenter(HomeViewInterface homeView){
        this.homeView=homeView;
    }
}
