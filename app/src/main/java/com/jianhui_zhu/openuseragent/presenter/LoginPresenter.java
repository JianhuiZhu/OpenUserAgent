package com.jianhui_zhu.openuseragent.presenter;

import android.content.Context;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jianhui_zhu.openuseragent.model.LoginModel;
import com.jianhui_zhu.openuseragent.model.beans.User;
import com.jianhui_zhu.openuseragent.util.Constant;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.view.HomeView;
import com.jianhui_zhu.openuseragent.view.interfaces.LoginViewInterface;

import rx.functions.Action1;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class LoginPresenter {
    private LoginModel loginModel;
    private LoginViewInterface loginViewInterface;
    private Context context;
    public LoginPresenter(LoginViewInterface loginViewInterface, Context context) {
        this.context=context;
        this.loginViewInterface = loginViewInterface;
        loginModel = new LoginModel(context);
    }

    public void login() {
        User user=loginModel.attemptLogin();
        FragmenUtil.switchToFragment(context, HomeView.newInstance());
    }
}
