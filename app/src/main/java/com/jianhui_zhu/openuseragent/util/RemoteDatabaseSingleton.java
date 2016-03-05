package com.jianhui_zhu.openuseragent.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.plus.Plus;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.Record;
import com.jianhui_zhu.openuseragent.model.beans.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jianhuizhu on 2016-02-19.
 */
public class RemoteDatabaseSingleton implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private static List<Record> records;
    private static List<Bookmark> bookmarks;
    private static User user;
    private Context context;
    private static GoogleApiClient googleApiClient;
    private RemoteDatabaseSingleton(Context context) {
        this.context=context;
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        this.googleApiClient.connect();
    }

    private static RemoteDatabaseSingleton remoteDB = null;

    public static RemoteDatabaseSingleton getInstance(Context context) {
        if (remoteDB == null) {
            RemoteDatabaseSingleton.remoteDB = new RemoteDatabaseSingleton(context);
        }
        return remoteDB;
    }
    public Observable<String> saveBookmark(final Bookmark bookmark) {

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Firebase recordRef = new Firebase(Constant.urlRoot).child(user.getuID()).child("bookmarks").push();
                bookmark.setbID(recordRef.getKey());
                recordRef.setValue(bookmark);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<String> saveHistory(final Record record) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Firebase recordRef = new Firebase(Constant.urlRoot).child(user.getuID()).child("histories").push();
                record.setrID(recordRef.getKey());
                recordRef.setValue(record);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public List<Record> getAllHistories() {
        Firebase recordRef = new Firebase(Constant.urlRoot).child(user.getuID()).child("histories");
        recordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                records = new ArrayList<>((int) dataSnapshot.getChildrenCount());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Record record = child.getValue(Record.class);
                    records.add(record);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return records;
    }

    public List<Bookmark> getAllBookmarks() {
        if(bookmarks==null) {
            Firebase recordRef = new Firebase(Constant.urlRoot).child(user.getuID()).child("bookmarks");
            recordRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    bookmarks = new ArrayList<>((int) dataSnapshot.getChildrenCount());
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Bookmark record = child.getValue(Bookmark.class);
                        bookmarks.add(record);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        return bookmarks;
    }
    public Observable<User> login(){
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(final Subscriber<? super User> subscriber) {
                String TAG=null;
                if(user!=null){
                    subscriber.onNext(user);
                    subscriber.onCompleted();
                }else{
                    if (!googleApiClient.isConnected()) {
                        googleApiClient.connect();
                    } else {
                        try {
                            String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                            String token = GoogleAuthUtil.getToken(context, Plus.AccountApi.getAccountName(googleApiClient), scope);
                            final Firebase ref = new Firebase(Constant.urlRoot);
                            ref.authWithOAuthToken("google", token, new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(final AuthData authData) {
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot currentPath) {
                                            if (currentPath.hasChild(authData.getUid())) {
                                                user = currentPath.child(authData.getUid()).getValue(User.class);
                                            } else {
                                                user = new User();
                                                user.setuID(authData.getUid());
                                                user.setUsername(authData.getProviderData().get(Constant.nameInGoogle).toString());
                                                user.setAvatarUrl(authData.getProviderData().get(Constant.avatarInGoogle).toString());
                                                ref.child(authData.getUid()).setValue(user);
                                            }
                                            getAllBookmarks();
                                            subscriber.onNext(user);
                                            subscriber.onCompleted();
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onAuthenticationError(FirebaseError firebaseError) {

                                }
                            });
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
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
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

    }
    public boolean isUserLoggedIn(){
        if(user==null){
            return false;
        }
        return true;
    }
    public User getUser(){
        return user;
    }

    public Observable<Bookmark> updateBookmark(final Bookmark bookmark){
        return Observable.create(new Observable.OnSubscribe<Bookmark>() {
            @Override
            public void call(final Subscriber<? super Bookmark> subscriber) {
                Firebase ref=new Firebase(Constant.urlRoot).child(user.getuID()).child("bookmarks").child(bookmark.getbID());
                Map<String,Object> updated=new HashMap<String, Object>();
                updated.put("name",bookmark.getName());
                updated.put("url",bookmark.getUrl());
                ref.updateChildren(updated, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        subscriber.onNext(bookmark);
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Record> updateHistory(final Record record){
        return Observable.create(new Observable.OnSubscribe<Record>() {
            @Override
            public void call(final Subscriber<? super Record> subscriber) {
                Firebase ref=new Firebase(Constant.urlRoot).child(user.getuID()).child("histories").child(record.getrID());
                Map<String,Object> updated=new HashMap<String, Object>();
                updated.put("url",record.getUrl());
                ref.updateChildren(updated, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        subscriber.onNext(record);
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public Observable<String> removeBookmark(final Bookmark bookmark){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                Firebase ref=new Firebase(Constant.urlRoot).child(user.getuID()).child("bookmarks").child(bookmark.getbID());
                ref.removeValue(new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        subscriber.onNext("Bookmark was deleted");
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public Observable<String> removeHistory(final Record record){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                Firebase ref=new Firebase(Constant.urlRoot).child(user.getuID()).child("histories").child(record.getrID());
                ref.removeValue(new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        subscriber.onNext("history was deleted");
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
