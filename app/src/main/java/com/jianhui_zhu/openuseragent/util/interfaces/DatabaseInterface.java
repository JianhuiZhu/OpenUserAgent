package com.jianhui_zhu.openuseragent.util.interfaces;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.History;

import java.util.List;

import rx.Observable;

/**
 * Created by jianhuizhu on 2016-03-13.
 */
public interface DatabaseInterface {
    List<Bookmark> getAllBookmarks();
    Observable<List<History>> getAllHistories();
    Observable<String> saveBookmark(final Bookmark bookmark);
    Observable<String> saveHistory(final History history);
    Observable<String> deleteBookmark(final Bookmark bookmark);
    Observable<Bookmark> updateBookmark(final Bookmark bookmark);
    Observable<String> deleteHistory(final History history);
    Observable<String> deleteAllBookmark();
    Observable<String> deleteAllHistories();
}
