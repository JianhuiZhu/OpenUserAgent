package com.jianhui_zhu.openuseragent.util;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.Record;

import java.util.ArrayList;
import java.util.List;

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

    public void saveBookmark(Bookmark bookmark) {
        Firebase recordRef = new Firebase(Constant.urlRoot).child(uID).child("bookmarks").push();
        bookmark.setbID(recordRef.getKey());
        recordRef.setValue(bookmark);
    }

    public void saveHistory(Record record) {
        Firebase recordRef = new Firebase(Constant.urlRoot).child(uID).child("histories").push();
        record.setrID(recordRef.getKey());
        recordRef.setValue(record);
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
