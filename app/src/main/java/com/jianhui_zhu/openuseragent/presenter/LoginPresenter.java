package com.jianhui_zhu.openuseragent.presenter;

import com.jianhui_zhu.openuseragent.view.interfaces.LoginViewInterface;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class LoginPresenter {
    private LoginViewInterface loginViewInterface;
    public LoginPresenter(LoginViewInterface loginViewInterface){
        this.loginViewInterface=loginViewInterface;
    }
}
