package com.jianhui_zhu.openuseragent.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.presenter.LoginPresenter;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.view.interfaces.LoginViewInterface;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class LoginView extends AbstractFragment implements LoginViewInterface,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private LoginPresenter loginPresenter;
    private GoogleApiClient googleApiClient;
    @Bind(R.id.google_login_button)
    RelativeLayout loginBtn;
    @OnClick(R.id.google_login_button)
    public void loginGoogle(){
        login();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.googleApiClient=new GoogleApiClient.Builder(getActivity()).
        this.loginPresenter=new LoginPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.view_login,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public boolean isLoginned() {
        return false;
    }

    @Override
    public boolean login() {
        Firebase ref=new Firebase("https://openuseragent.firebaseapp.com");
        //ref.authWithOAuthToken("google","");
        return false;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
