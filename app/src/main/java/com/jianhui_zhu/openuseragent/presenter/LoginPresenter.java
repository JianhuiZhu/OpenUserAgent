package com.jianhui_zhu.openuseragent.presenter;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.LoginModel;
import com.jianhui_zhu.openuseragent.view.interfaces.LoginViewInterface;

import rx.Observer;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class LoginPresenter {
    private LoginModel loginModel;
    private LoginViewInterface loginViewInterface;

    public LoginPresenter(LoginViewInterface loginViewInterface, Context context) {
        this.loginViewInterface = loginViewInterface;
        loginModel = new LoginModel(context);
    }

    public void login() {

        loginModel.login().subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                loginViewInterface.showTag(s);
            }
        });
    }
}
