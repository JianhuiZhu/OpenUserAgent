package com.jianhui_zhu.openuseragent.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.Record;

import java.lang.Override;import java.lang.String;import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jianhuizhu on 2016-02-18.
 */
public class LocalDatabaseSingleton {
    private static LocalDatabaseSingleton instance = null;
    private static Context context;

    public static synchronized LocalDatabaseSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new LocalDatabaseSingleton();
        }
        LocalDatabaseSingleton.context = context;
        return instance;
    }
    public Observable<String> updateBookmark(final Bookmark bookmark){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Cursor cursor=LocalDatabaseHelper.getInstance(context)
                        .getReadableDatabase()
                        .rawQuery("SELECT * FROM Bookmarks WHERE id=?", new String[]{bookmark.getbID()});
                if(cursor.getCount()>0){
                    ContentValues newValues = new ContentValues();
                    newValues.put("name",bookmark.getName());
                    newValues.put("url",bookmark.getUrl());
                    LocalDatabaseHelper.getInstance(context).getWritableDatabase()
                            .update("Bookmarks",newValues,"id="+bookmark.getbID(),null);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public Observable<String> deleteBookmark(final Bookmark bookmark){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                LocalDatabaseHelper.getInstance(context).getWritableDatabase().delete("Bookmark","id="+bookmark.getbID(),null);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }
    public Observable<String> saveHistory(final Record record) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                LocalDatabaseHelper.getInstance(context)
                        .getWritableDatabase()
                        .execSQL("INSERT INTO Records (url,timestamp) " +
                                "VALUES(\""
                                + record.getUrl() + "\","
                                + record.getTimestamp() + ")");
                subscriber.onNext("DONE");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }
    public Observable<String> saveBookmark(final Bookmark bookmark) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                LocalDatabaseHelper.getInstance(context)
                        .getWritableDatabase()
                        .execSQL("INSERT INTO Bookmarks (url,name) " +
                                "VALUES(\""
                                + bookmark.getUrl() + "\",\""
                                + bookmark.getName() + "\")");
                subscriber.onNext("DONE");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public List<Bookmark> getAllBookmarks() {
        String query = "SELECT * FROM Bookmarks";
        List<Bookmark> result = new LinkedList<>();
        SQLiteDatabase db = LocalDatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Bookmark bookmark = new Bookmark();
                bookmark.setbID(cursor.getString(0));
                bookmark.setName(cursor.getString(1));
                bookmark.setUrl(cursor.getString(2));
                result.add(bookmark);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<Record> getAllRecords() {
        String query = "SELECT * FROM Records";
        SQLiteDatabase db = LocalDatabaseHelper.getInstance(context).getReadableDatabase();
        long bookmarkNumber = DatabaseUtils.queryNumEntries(db, "Records");
        List<Record> result = new ArrayList<>((int) bookmarkNumber);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setTimestamp(cursor.getLong(1));
                record.setUrl(cursor.getString(0));
                result.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;

    }

    private static class LocalDatabaseHelper extends SQLiteOpenHelper {
        private static final String dbName = "DBRecords";
        private static final int numberOfDatabase = 1;
        private static LocalDatabaseHelper instance = null;

        private LocalDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public static LocalDatabaseHelper getInstance(Context context) {
            if (instance == null) {
                instance = new LocalDatabaseHelper(context, dbName, null, numberOfDatabase);
            }
            return instance;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createBookmarksTable = "CREATE TABLE IF NOT EXISTS Bookmarks(" +
                    "id INTEGER NOT NULL PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL," +
                    "url VARCHAR(300) NOT NULL)";
            String createRecordsTable = "CREATE TABLE IF NOT EXISTS Records(" +
                    "url VARCHAR(300) NOT NULL," +
                    "timestamp INT NOT NULL," +
                    "PRIMARY KEY(timestamp))";
            db.execSQL(createBookmarksTable);
            db.execSQL(createRecordsTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
