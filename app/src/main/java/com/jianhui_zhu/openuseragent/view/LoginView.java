package com.jianhui_zhu.openuseragent.view;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.model.beans.User;
import com.jianhui_zhu.openuseragent.presenter.LoginPresenter;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.util.FragmenUtil;
import com.jianhui_zhu.openuseragent.view.interfaces.LoginViewInterface;
import com.google.android.gms.plus.Plus;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class LoginView extends AbstractFragment implements LoginViewInterface{
    private LoginPresenter loginPresenter;
    private static final String TAG = LoginView.class.getSimpleName();
    private boolean mGoogleLoginClicked;
    @Bind(R.id.google_login_button)
    RelativeLayout loginBtn;

    @OnClick(R.id.google_login_button)
    public void loginGoogle() {
        mGoogleLoginClicked = true;
        loginPresenter.login();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loginPresenter = new LoginPresenter(this,getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void showTag(String s) {
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void switchFragmentWithUser(User user) {
        FragmenUtil.switchToFragment(getActivity(), HomeView.newInstanceWithUser(user));
    }
}
