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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.jianhui_zhu.openuseragent.R;
import com.jianhui_zhu.openuseragent.presenter.LoginPresenter;
import com.jianhui_zhu.openuseragent.util.AbstractFragment;
import com.jianhui_zhu.openuseragent.view.interfaces.LoginViewInterface;
import com.google.android.gms.plus.Plus;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jianhui Zhu on 2016-02-05.
 */
public class LoginView extends AbstractFragment implements LoginViewInterface,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private LoginPresenter loginPresenter;
    public static final int RC_GOOGLE_LOGIN = 1;
    private GoogleSignInOptions gso;
    private GoogleApiClient googleApiClient;
    private static final String TAG = LoginView.class.getSimpleName();
    private ConnectionResult mGoogleConnectionResult;
    private boolean mGoogleIntentInProgress;
    private boolean mGoogleLoginClicked;
    @Bind(R.id.google_login_button)
    RelativeLayout loginBtn;

    @OnClick(R.id.google_login_button)
    public void loginGoogle() {
        mGoogleLoginClicked = true;
        if (!googleApiClient.isConnecting()) {
            if (mGoogleConnectionResult != null) {
                Toast.makeText(getActivity(), "Connection error", Toast.LENGTH_SHORT).show();
            } else if (googleApiClient.isConnected()) {
                getGoogleOAuthTokenAndLogin();
            } else {
                    /* connect API now */
                Log.d(TAG, "Trying to connect to Google API");
                googleApiClient.connect();
            }
        }
        login();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        this.googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();


        this.loginPresenter = new LoginPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean isLoginned() {
        return false;
    }

    @Override
    public boolean login() {
        Firebase ref = new Firebase("https://openuseragent.firebaseapp.com");
        //ref.authWithOAuthToken("google","");
        return false;
    }

    @Override
    public String getUsername() {
        return null;
    }

    private void getGoogleOAuthTokenAndLogin() {
        /* Get OAuth token in Background */
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                    token = GoogleAuthUtil.getToken(getActivity(),"jianhuizhu1987@gmail.com", scope);
                } catch (IOException transientEx) {
                    /* Network or server error */
                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
                    /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!mGoogleIntentInProgress) {
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        Toast.makeText(getActivity(), "some problem", Toast.LENGTH_SHORT).show();
                    }
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. */
                    Log.e(TAG, "Error authenticating with Google: " + authEx.getMessage(), authEx);
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }

                return token;
            }
            @Override
            protected void onPostExecute(String token) {
                mGoogleLoginClicked = false;
                Intent resultIntent = new Intent();
                if (token != null) {
                    resultIntent.putExtra("oauth_token", token);
                } else if (errorMessage != null) {
                    resultIntent.putExtra("error", errorMessage);
                }

            }

        };
        task.execute();
    }

    @Override
    public void onConnected(Bundle bundle) {
        getGoogleOAuthTokenAndLogin();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mGoogleIntentInProgress) {
            /* Store the ConnectionResult so that we can use it later when the user clicks on the Google+ login button */
            mGoogleConnectionResult = result;

            if (mGoogleLoginClicked) {
                /* The user has already clicked login so we attempt to resolve all errors until the user is signed in,
                 * or they cancel. */
                resolveSignInError();
            } else {
                Log.e(TAG, result.toString());
            }
        }
    }
    private void resolveSignInError() {
        if (mGoogleConnectionResult.hasResolution()) {
            try {
                mGoogleIntentInProgress = true;
                mGoogleConnectionResult.startResolutionForResult(getActivity(), LoginView.RC_GOOGLE_LOGIN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mGoogleIntentInProgress = false;
                googleApiClient.connect();
            }
        }
    }
}
