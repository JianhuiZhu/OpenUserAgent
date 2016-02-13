package com.jianhui_zhu.openuseragent.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.jianhui_zhu.openuseragent.model.LoginModel;
import com.jianhui_zhu.openuseragent.view.interfaces.LoginViewInterface;

import rx.Observer;
import rx.functions.Action1;

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

        loginModel.attemptLogin().subscribe(new Action1<String>() {
            @Override
            public void call(String token) {
                //loginViewInterface.showTag(token);
                Firebase ref = new Firebase("https://openuseragent.firebaseio.com");
                ref.authWithOAuthToken("google", token, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        // the Google user is now authenticated with your Firebase app
                        loginViewInterface.showTag("Done");
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        // there was an error
                    }
                });
            }
        });
    }
}
