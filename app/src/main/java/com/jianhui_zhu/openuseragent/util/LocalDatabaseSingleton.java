package com.jianhui_zhu.openuseragent.util;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.Record;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    public void saveHistory(Record record) {
        LocalDatabaseHelper.getInstance(context)
                .getWritableDatabase()
                .execSQL("INSERT INTO Records (url,timestamp) " +
                        "VALUES("
                        + record.getUrl() + ","
                        + record.getTimestamp() + ")");
    }

    public void saveBookmark(Bookmark bookmark) {
        LocalDatabaseHelper.getInstance(context)
                .getWritableDatabase()
                .execSQL("INSERT INTO Bookmarks (url,name) " +
                        "VALUES("
                        + bookmark.getUrl() + ","
                        + bookmark.getName() + ")");
    }

    public List<Bookmark> getAllBookmarks() {
        String query = "SELECT * FROM Bookmarks";
        List<Bookmark> result = new LinkedList<>();
        SQLiteDatabase db = LocalDatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Bookmark bookmark = new Bookmark();
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
        private static final String dbName = "browsingRecords";
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
                    "id INT NOT NULL AUTO_INCREMENT," +
                    "name VARCHAR(50) NOT NULL," +
                    "url VARCHAR(300) NOT NULL," +
                    "PRIMARY KEY(id))ENGINE=InnoDB";
            String createRecordsTable = "CREATE TABLE IF NOT EXISTS Records(" +
                    "url VARCHAR(300) NOT NULL," +
                    "timestamp INT NOT NULL," +
                    "PRIMARY KEY(timestamp))ENGINE=InnoDB";
            db.execSQL(createBookmarksTable);
            db.execSQL(createRecordsTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
