package com.jianhui_zhu.openuseragent.util.interfaces;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.Record;

import java.util.List;

import rx.Observable;

/**
 * Created by jianhuizhu on 2016-03-13.
 */
public interface DatabaseInterface {
    List<Bookmark> getAllBookmarks();
    List<Record>   getAllHistories();
    Observable<String> saveBookmark(final Bookmark bookmark);
    Observable<String> saveHistory(final Record record);
    Observable<String> deleteBookmark(final Bookmark bookmark);
    Observable<Bookmark> updateBookmark(final Bookmark bookmark);
    Observable<String> deleteHistory(final Record record);
    Observable<String> deleteAllBookmark();
    Observable<String> deleteAllHistories();
}
