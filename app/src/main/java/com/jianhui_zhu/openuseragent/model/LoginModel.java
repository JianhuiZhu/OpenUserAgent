package com.jianhui_zhu.openuseragent.model;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.jianhui_zhu.openuseragent.model.interfaces.LoginModelInterface;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class LoginModel implements LoginModelInterface,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Context context;
    private String TAG;
    private GoogleApiClient googleApiClient;
    private ConnectionResult mGoogleConnectionResult;
    public LoginModel(Context context){
        this.context=context;
        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    @Override
    public Observable<String> login() {
        Observable<String> tokenEmiter=Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String token;
                if (!googleApiClient.isConnecting()) {
                    if (mGoogleConnectionResult != null) {

                    } else if (googleApiClient.isConnected()) {
                        login();
                    } else {
                    /* connect API now */
                        Log.d(TAG, "Trying to connect to Google API");
                        googleApiClient.connect();
                    }
                }

                try {
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                    token = GoogleAuthUtil.getToken(context, Plus.AccountApi.getAccountName(googleApiClient), scope);
                    subscriber.onNext(token);
                    subscriber.onCompleted();
                } catch (IOException transientEx) {
                    /* Network or server error */
                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. */
                    Log.e(TAG, "Error authenticating with Google: " + authEx.getMessage(), authEx);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return tokenEmiter;
    }

    @Override
    public void logout() {

    }

    @Override
    public void onConnected(Bundle bundle) {
        login();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mGoogleConnectionResult=connectionResult;
    }
}
