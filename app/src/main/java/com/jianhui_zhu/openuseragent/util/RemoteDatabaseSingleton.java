package com.jianhui_zhu.openuseragent.util;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jianhuizhu on 2016-02-19.
 */
public class RemoteDatabaseSingleton {
    private static List<Record> records;
    private static List<Bookmark> bookmarks;

    private RemoteDatabaseSingleton(String uID) {
        RemoteDatabaseSingleton.uID = uID;
    }

    private static String uID;
    private static RemoteDatabaseSingleton remoteDB = null;

    public static RemoteDatabaseSingleton getInstance(String uID) {
        if (remoteDB == null) {
            RemoteDatabaseSingleton.remoteDB = new RemoteDatabaseSingleton(uID);
        }
        return remoteDB;
    }

    public Observable<String> saveBookmark(final Bookmark bookmark) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Firebase recordRef = new Firebase(Constant.urlRoot).child(uID).child("bookmarks").push();
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
                Firebase recordRef = new Firebase(Constant.urlRoot).child(uID).child("histories").push();
                record.setrID(recordRef.getKey());
                recordRef.setValue(record);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public List<Record> getAllHistories() {
        Firebase recordRef = new Firebase(Constant.urlRoot).child(uID).child("histories");
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
        Firebase recordRef = new Firebase(Constant.urlRoot).child(uID).child("bookmarks");
        recordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookmarks = new ArrayList<>((int) dataSnapshot.getChildrenCount());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Record record = child.getValue(Record.class);
                    records.add(record);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return bookmarks;
    }
}
