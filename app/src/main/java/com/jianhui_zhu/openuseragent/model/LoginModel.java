package com.jianhui_zhu.openuseragent.model;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.jianhui_zhu.openuseragent.model.beans.User;
import com.jianhui_zhu.openuseragent.model.interfaces.LoginModelInterface;
import com.jianhui_zhu.openuseragent.util.Constant;
import com.jianhui_zhu.openuseragent.util.RemoteDatabaseSingleton;

import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class LoginModel {
    private Context context;
    private String TAG;
    private User user;
    private GoogleApiClient googleApiClient;
    private ConnectionResult mGoogleConnectionResult;
    public LoginModel(Context context){
        this.context=context;
    }
    public User attemptLogin() {

        RemoteDatabaseSingleton.getInstance(context).login().subscribe(new Action1<User>() {
            @Override
            public void call(User userIn) {
                user=userIn;
            }
        });
        return user;
    }
    public void logout(){

    }

}
