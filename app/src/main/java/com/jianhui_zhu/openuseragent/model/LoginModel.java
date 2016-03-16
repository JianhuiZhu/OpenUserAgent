package com.jianhui_zhu.openuseragent.model;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jianhui_zhu.openuseragent.model.beans.User;
import com.jianhui_zhu.openuseragent.util.RemoteDatabaseSingleton;

import rx.functions.Action1;

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

        RemoteDatabaseSingleton.getInstance().login().subscribe(new Action1<User>() {
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
