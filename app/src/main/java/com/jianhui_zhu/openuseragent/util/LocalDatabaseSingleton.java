package com.jianhui_zhu.openuseragent.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.History;
import com.jianhui_zhu.openuseragent.util.interfaces.DatabaseInterface;

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
public class LocalDatabaseSingleton implements DatabaseInterface {
    private static LocalDatabaseSingleton instance = null;
    private static Context context;
    public static void instantiate(Context context){
        instance=new LocalDatabaseSingleton(context);
    }
    private LocalDatabaseSingleton(Context context){
        LocalDatabaseSingleton.context=context;
    }
    public static synchronized LocalDatabaseSingleton getInstance() {
        return instance;
    }
    @Override
    public Observable<Bookmark> updateBookmark(final Bookmark bookmark){
        return Observable.create(new Observable.OnSubscribe<Bookmark>() {
            @Override
            public void call(Subscriber<? super Bookmark> subscriber) {
                Cursor cursor=LocalDatabaseHelper.getInstance(context)
                        .getReadableDatabase()
                        .rawQuery("SELECT * FROM Bookmarks WHERE id=?", new String[]{bookmark.getbID()});
                if(cursor.getCount()>0){
                    ContentValues newValues = new ContentValues();
                    newValues.put("name",bookmark.getName());
                    newValues.put("url",bookmark.getUrl());
                    LocalDatabaseHelper.getInstance(context).getWritableDatabase()
                            .update("Bookmarks",newValues,"id="+bookmark.getbID(),null);
                    subscriber.onNext(bookmark);
                    subscriber.onCompleted();
                }
                cursor.close();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void deleteHistory(History history) {

    }

    @Override
    public void deleteAllBookmark() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                int result=LocalDatabaseHelper.getInstance(context)
                        .getWritableDatabase()
                        .delete("Bookmarks",null,null);
                subscriber.onNext(String.valueOf(result));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void deleteAllHistories() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                int result=LocalDatabaseHelper.getInstance(context)
                        .getWritableDatabase()
                        .delete("Histories",null,null);
                subscriber.onNext(String.valueOf(result));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void deleteBookmark(final Bookmark bookmark){
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                LocalDatabaseHelper.getInstance(context).getWritableDatabase().delete("Bookmarks","id="+bookmark.getbID(),null);
            }
        }).subscribeOn(Schedulers.io()).subscribe();

    }
    public void saveHistory(final History history) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                LocalDatabaseHelper.getInstance(context)
                        .getWritableDatabase()
                        .execSQL("INSERT INTO Histories (name,url,timestamp) " +
                                "VALUES("
                                +"\""+ history.getName()+"\",\""
                                + history.getUrl() + "\","
                                + history.getTimestamp() + ")");
                subscriber.onNext("DONE");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).subscribe();

    }
    public void saveQueryRecord(final String query){
//        Observable.create(new Observable.OnSubscribe<Object>() {
//            @Override
//            public void call(Subscriber<? super Object> subscriber) {
                ContentValues newValue=new ContentValues();
                newValue.put("query",query);
                long result=LocalDatabaseHelper
                        .getInstance(context)
                        .getWritableDatabase()
                        .insert("QueryRecords",null,newValue);
        Log.d(this.getClass().getSimpleName(),"result is  "+result);
//            }
//        }).subscribeOn(Schedulers.io());
    }
    public Observable<Cursor> getSimilarHistory(final String query){
        return Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                LocalDatabaseHelper.getInstance(context)
                        .getReadableDatabase()
                        .query("Histories",null,"name=?",new String[]{"%"+query+"%"},null,null,"timestamp DESC","3");
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public Observable<Cursor> getQueryRecord(final String query){
        return Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                Cursor c=LocalDatabaseHelper
                        .getInstance(context)
                        .getReadableDatabase()
                        .query("QueryRecords",null,"query LIKE ?",new String[]{"%"+query+"%"},null,null,"count DESC","3");
                int count=c.getCount();
                Log.e(this.getClass().getSimpleName(),"query result is "+count);
                subscriber.onNext(c);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public void incrementQueryRecordCount(int id, int count){
        ContentValues newValue=new ContentValues();
        newValue.put("count",(count+1));
        LocalDatabaseHelper.getInstance(context).getWritableDatabase()
                .update("QueryRecords",newValue,"_id=?",new String[]{id+""});
    }
    @Override
    public void saveBookmark(final Bookmark bookmark) {
        Observable.create(new Observable.OnSubscribe<String>() {
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
        }).subscribeOn(Schedulers.io()).subscribe();

    }
    @Override
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
            String createRecordsTable = "CREATE TABLE IF NOT EXISTS Histories(" +
                    "_id INTEGER NOT NULL PRIMARY KEY,"+
                    "name VARCHAR(50) NOT NULL,"+
                    "url VARCHAR(300) NOT NULL," +
                    "timestamp INT NOT NULL)";
            String createQueryTable="CREATE TABLE IF NOT EXISTS QueryRecords(" +
                    "_id INTEGER NOT NULL PRIMARY KEY,"+
                    "query VARCHAR(400) NOT NULL," +
                    "count INT NOT NULL DEFAULT 1)";
            db.execSQL(createQueryTable);
            db.execSQL(createBookmarksTable);
            db.execSQL(createRecordsTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
