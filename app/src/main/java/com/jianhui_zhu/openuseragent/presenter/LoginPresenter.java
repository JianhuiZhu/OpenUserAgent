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
import com.jianhui_zhu.openuseragent.view.interfaces.LoginViewInterface;

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
            public void call(final String token) {
                //loginViewInterface.showTag(token);
                final Firebase ref = new Firebase(Constant.urlRoot);
                ref.authWithOAuthToken("google", token, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(final AuthData authData) {
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot currentPath) {
                                User user = loginModel.getUserObject(currentPath, authData, ref);
                                loginViewInterface.switchFragmentWithUser(user);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        loginViewInterface.showTag("Problem" + firebaseError.toString());
                    }
                });
            }
        });
    }
}
