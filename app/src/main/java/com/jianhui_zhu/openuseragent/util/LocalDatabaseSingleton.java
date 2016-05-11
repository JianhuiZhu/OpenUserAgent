package com.jianhui_zhu.openuseragent.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.util.interfaces.DatabaseInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jianhuizhu on 2016-02-18.
 */
public class LocalDatabaseSingleton implements DatabaseInterface {
    private static LocalDatabaseSingleton instance = null;
    private static Context context;
    private LocalDatabaseSingleton(Context context) {
        LocalDatabaseSingleton.context = context;
    }

    public static void instantiate(Context context) {
        instance = new LocalDatabaseSingleton(context);
    }

    public static synchronized LocalDatabaseSingleton getInstance() {
        return instance;
    }

    @Override
    public Observable<Bookmark> updateBookmark(final Bookmark bookmark) {
        return Observable.create(new Observable.OnSubscribe<Bookmark>() {
            @Override
            public void call(Subscriber<? super Bookmark> subscriber) {
                SQLiteDatabase db =LocalDatabaseHelper.getInstance(context)
                        .getReadableDatabase();
                try {
                     Cursor cursor= db.rawQuery("SELECT * FROM Bookmarks WHERE id=?", new String[]{bookmark.getbID()});
                    if (cursor.getCount() > 0) {
                        ContentValues newValues = new ContentValues();
                        newValues.put("name", bookmark.getName());
                        newValues.put("url", bookmark.getUrl());
                        LocalDatabaseHelper.getInstance(context).getWritableDatabase()
                                .update("Bookmarks", newValues, "id=" + bookmark.getbID(), null);
                        cursor.close();
                        subscriber.onNext(bookmark);
                        subscriber.onCompleted();
                    }
                }finally {
                    db.close();
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Observable<String> deleteAllBookmark() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                int result = LocalDatabaseHelper.getInstance(context)
                        .getWritableDatabase()
                        .delete("Bookmarks", null, null);
                subscriber.onNext(String.valueOf(result));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<String> deleteAllHistories() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                int result = LocalDatabaseHelper.getInstance(context)
                        .getWritableDatabase()
                        .delete("Histories", null, null);
                subscriber.onNext(String.valueOf(result));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<History>> getHistoryByDate(final int year, final int month, final int day) {
        return Observable.create(new Observable.OnSubscribe<List<History>>() {
            @Override
            public void call(Subscriber<? super List<History>> subscriber) {
                GregorianCalendar cal = new GregorianCalendar();
                SQLiteDatabase db = LocalDatabaseHelper.getInstance(context)
                        .getReadableDatabase();
                Date date = new Date();
                cal.setTime(date);
                cal.set(year, month, day);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                long startTime = cal.getTimeInMillis();
                long endTime = startTime + 24 * 3600 * 1000;
                Cursor cursor = db
                        .query("Histories", null, "timestamp >=? AND timestamp < ?"
                                , new String[]{String.valueOf(startTime), String.valueOf(endTime)}
                                , null, null, "timestamp DESC");
                ArrayList<History> result = new ArrayList<>(cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        History history = new History();
                        history.setrID(cursor.getColumnName(0));
                        history.setTimestamp(cursor.getLong(3));
                        history.setUrl(cursor.getString(2));
                        result.add(history);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.close();
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> deleteSelectedHistory(final List<History> histories) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                StringBuilder sb = new StringBuilder();
                for (History history : histories) {
                    sb.append(history.getrID() + ",");
                }
                sb = sb.deleteCharAt(sb.length() - 1);

                LocalDatabaseHelper.getInstance(context).getWritableDatabase()
                        .rawQuery("Delete FROM Histories WHERE _id IN(" + sb.toString() + ")", null);
                subscriber.onNext("Selected histories has been deleted");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Set<String>> getBlackList() {
        return Observable.create(new Observable.OnSubscribe<Set<String>>() {
            @Override
            public void call(Subscriber<? super Set<String>> subscriber) {
                Set<String> result = new HashSet<>();
                    Cursor cursor = LocalDatabaseHelper.getInstance(context).getReadableDatabase()
                            .query("BlackList", null, null, null, null, null, null);

                try {
                    if (cursor.moveToFirst()) {
                        do {
                            result.add(cursor.getString(0));
                        } while (cursor.moveToNext());
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    }
                }finally {
                    cursor.close();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<String> addToBlackList(final Set<String> domains) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                SQLiteDatabase db =LocalDatabaseHelper.getInstance(context).getWritableDatabase();
                try {
                    ContentValues newValues = new ContentValues();
                    for (String domain : domains) {
                        newValues.put("domain", domain);
                    }
                    db.insert("BlackList", null, newValues);
                    subscriber.onNext("Added " + domains.size() + " domain(s) into blacklist.");
                    subscriber.onCompleted();
                }finally {
                    db.close();
                }
            }
        }).subscribeOn(Schedulers.io());
    }
    @Override
    public Observable<String> deleteAllFromBlackList(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                SQLiteDatabase db =LocalDatabaseHelper.getInstance(context).getWritableDatabase();
                try {
                    db.delete("BlackList", null, null);
                    subscriber.onNext("Blacklist has been cleared");
                    subscriber.onCompleted();
                }finally {
                    db.close();
                }
            }
        }).subscribeOn(Schedulers.io());
    }
    @Override
    public Observable<String> removeFromGlobalBlackList(final Set<String> domains) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                SQLiteDatabase db = LocalDatabaseHelper.getInstance(context).getWritableDatabase();
                try {

                    String whereClause = String.format("domain" + " in (%s)", TextUtils.join(",", Collections.nCopies(domains.size(), "?")));
                    db.delete("BlackList", whereClause, domains.toArray(new String[0]));
                    subscriber.onNext("Removed " + domains.size() + " domain(s) from blacklist.");
                    subscriber.onCompleted();
                }finally {
                    db.close();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<String> deleteBookmark(final Bookmark bookmark) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                SQLiteDatabase db =LocalDatabaseHelper.getInstance(context).getWritableDatabase();
                int result = db.delete("Bookmarks", "id=" + bookmark.getbID(), null);
                subscriber.onNext(String.valueOf(result));
                subscriber.onCompleted();
                db.close();
            }
        }).subscribeOn(Schedulers.io());

    }

    public Observable<String> saveHistory(final History history) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                Cursor cursor = LocalDatabaseHelper.getInstance(context)
                        .getReadableDatabase()
                        .query("Histories", null, "url = ?", new String[]{history.getUrl()}, null, null, null);
                try {
                    if (cursor.getCount() == 0) {
                        LocalDatabaseHelper.getInstance(context)
                                .getWritableDatabase()
                                .execSQL("INSERT INTO Histories (name,url,timestamp) " +
                                        "VALUES("
                                        + "\"" + history.getName() + "\",\""
                                        + history.getUrl() + "\","
                                        + history.getTimestamp() + ")");
                        subscriber.onNext("DONE");
                        subscriber.onCompleted();
                    } else {
                        if (cursor.moveToFirst()) {
                            int id = cursor.getInt(0);
                            int count = cursor.getInt(3);
                            ContentValues newValue = new ContentValues();
                            newValue.put("frequent", (count + 1));
                            LocalDatabaseHelper.getInstance(context)
                                    .getWritableDatabase()
                                    .update("Histories", newValue, "_id = ?", new String[]{String.valueOf(id)});
                        }
                    }
                }finally {
                    cursor.close();
                }
            }
        }).subscribeOn(Schedulers.io());

    }

    public void saveQueryRecord(final String query) {
        SQLiteDatabase db = LocalDatabaseHelper
                .getInstance(context)
                .getWritableDatabase();
        try {
            ContentValues newValue = new ContentValues();
            newValue.put("query", query);

            db.insert("QueryRecords", null, newValue);
        }finally {
            db.close();
        }

    }

    public Observable<Cursor> getSimilarHistory(final String query) {
        return Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                LocalDatabaseHelper.getInstance(context)
                        .getReadableDatabase()
                        .query("Histories", null, "name=? OR url =?", new String[]{"%" + query + "%","%" + query + "%"}, null, null, "timestamp DESC", "3");
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Cursor> getQueryRecord(final String query) {
        return Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                Cursor c = LocalDatabaseHelper
                        .getInstance(context)
                        .getReadableDatabase()
                        .query("QueryRecords", null, "query LIKE ?", new String[]{"%" + query + "%"}, null, null, "count DESC", "3");
                int count = c.getCount();
                Log.e(this.getClass().getSimpleName(), "query result is " + count);
                subscriber.onNext(c);
                subscriber.onCompleted();
                c.close();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public void incrementQueryRecordCount(int id, int count) {
        ContentValues newValue = new ContentValues();
        newValue.put("count", (count + 1));
        LocalDatabaseHelper.getInstance(context).getWritableDatabase()
                .update("QueryRecords", newValue, "_id=?", new String[]{id + ""});
    }

    @Override
    public Observable<String> saveBookmark(final Bookmark bookmark) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                SQLiteDatabase db =LocalDatabaseHelper.getInstance(context)
                        .getWritableDatabase();

                        db.execSQL("INSERT INTO Bookmarks (url,name) " +
                                "VALUES(\""
                                + bookmark.getUrl() + "\",\""
                                + bookmark.getName() + "\")");
                subscriber.onNext(bookmark.getName() + " saved as bookmark");
                subscriber.onCompleted();
                db.close();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<List<History>> getNavigationPages() {

        return Observable.create(new Observable.OnSubscribe<List<History>>() {
            @Override
            public void call(Subscriber<? super List<History>> subscriber) {
                List<History> result = new ArrayList<>();
                SQLiteDatabase db = LocalDatabaseHelper.getInstance(context).getReadableDatabase();
                Cursor cursor = db.query("Histories", null, null, null, null, null, "frequent DESC", "8");
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            History history = new History();
                            history.setrID(cursor.getString(0));
                            history.setName(cursor.getString(1));
                            history.setUrl(cursor.getString(2));
                            result.add(history);
                        } while (cursor.moveToNext());
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    }
                }finally {
                    cursor.close();
                    db.close();

                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Bookmark>> getAllBookmarks() {

           return Observable.create(new Observable.OnSubscribe<List<Bookmark>>() {
                @Override
                public void call(Subscriber<? super List<Bookmark>> subscriber) {
                    String query = "SELECT * FROM Bookmarks";
                    List<Bookmark> result = new LinkedList<>();
                    SQLiteDatabase db = LocalDatabaseHelper.getInstance(context).getReadableDatabase();
                    Cursor cursor = db.rawQuery(query, null);
                    try {
                        if (cursor.moveToFirst()) {
                            do {
                                Bookmark bookmark = new Bookmark();
                                bookmark.setbID(cursor.getString(0));
                                bookmark.setName(cursor.getString(1));
                                bookmark.setUrl(cursor.getString(2));
                                result.add(bookmark);
                            } while (cursor.moveToNext());
                            subscriber.onNext(result);
                            subscriber.onCompleted();
                        }
                    }finally {
                        cursor.close();
                        db.close();
                    }
                }
            }).subscribeOn(Schedulers.io());

    }

    @Override
    public Observable<List<History>> getAllHistories() {


        return Observable.create(new Observable.OnSubscribe<List<History>>() {
            @Override
            public void call(Subscriber<? super List<History>> subscriber) {
                String query = "SELECT * FROM Histories";
                SQLiteDatabase db = LocalDatabaseHelper.getInstance(context).getReadableDatabase();
                long bookmarkNumber = DatabaseUtils.queryNumEntries(db, "Histories");
                List<History> result = new ArrayList<>((int) bookmarkNumber);
                Cursor cursor = db.rawQuery(query, null);
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            History history = new History();
                            history.setrID(cursor.getString(0));
                            history.setName(cursor.getString(1));
                            history.setTimestamp(cursor.getLong(3));
                            history.setUrl(cursor.getString(2));
                            result.add(history);
                        } while (cursor.moveToNext());
                    }
                }finally {
                    cursor.close();
                    db.close();
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    private static class LocalDatabaseHelper extends SQLiteOpenHelper {
        private static final String dbName = "DBRecords";
        private static final int DBVersion = 2;
        private static LocalDatabaseHelper instance = null;

        private LocalDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public static LocalDatabaseHelper getInstance(Context context) {
            if (instance == null) {
                instance = new LocalDatabaseHelper(context, dbName, null, DBVersion);
            }
            return instance;
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            String createBookmarksTable = "CREATE TABLE IF NOT EXISTS Bookmarks(" +
                    "id INTEGER NOT NULL PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL," +
                    "url VARCHAR(300) NOT NULL)";
            String createRecordsTable = "CREATE TABLE IF NOT EXISTS Histories(" +
                    "_id INTEGER NOT NULL PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL," +
                    "url VARCHAR(300) NOT NULL," +
                    "frequent INTEGER NOT NULL DEFAULT 1,"+
                    "timestamp INTEGER NOT NULL)";
            String createQueryTable = "CREATE TABLE IF NOT EXISTS QueryRecords(" +
                    "_id INTEGER NOT NULL PRIMARY KEY," +
                    "query VARCHAR(400) NOT NULL," +
                    "count INT NOT NULL DEFAULT 1)";
            String createBlackListTable = "CREATE TABLE IF NOT EXISTS BlackList("+
                    "domain VARCHAR(255) NOT NULL PRIMARY KEY)";
            db.execSQL(createQueryTable);
            db.execSQL(createBookmarksTable);
            db.execSQL(createRecordsTable);
            db.execSQL(createBlackListTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                if(oldVersion<newVersion){
                    db.execSQL("DROP TABLE IF EXISTS Bookmarks");
                    db.execSQL("DROP TABLE IF EXISTS Histories");
                    db.execSQL("DROP TABLE IF EXISTS QueryRecords");
                    db.execSQL("DROP TABLE IF EXISTS BlackList");
                    String createBookmarksTable = "CREATE TABLE IF NOT EXISTS Bookmarks(" +
                            "id INTEGER NOT NULL PRIMARY KEY," +
                            "name VARCHAR(50) NOT NULL," +
                            "url VARCHAR(300) NOT NULL)";
                    String createRecordsTable = "CREATE TABLE IF NOT EXISTS Histories(" +
                            "_id INTEGER NOT NULL PRIMARY KEY," +
                            "name VARCHAR(50) NOT NULL," +
                            "url VARCHAR(300) NOT NULL," +
                            "frequent INTEGER NOT NULL DEFAULT 1,"+
                            "timestamp INTEGER NOT NULL)";
                    String createQueryTable = "CREATE TABLE IF NOT EXISTS QueryRecords(" +
                            "_id INTEGER NOT NULL PRIMARY KEY," +
                            "query VARCHAR(400) NOT NULL," +
                            "count INT NOT NULL DEFAULT 1)";
                    String createBlackListTable = "CREATE TABLE IF NOT EXISTS BlackList("+
                            "domain VARCHAR(255) NOT NULL PRIMARY KEY)";
                    db.execSQL(createQueryTable);
                    db.execSQL(createBookmarksTable);
                    db.execSQL(createRecordsTable);
                    db.execSQL(createBlackListTable);
                }
        }
    }
}
