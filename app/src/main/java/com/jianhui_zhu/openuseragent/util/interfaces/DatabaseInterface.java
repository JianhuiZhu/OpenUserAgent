package com.jianhui_zhu.openuseragent.util.interfaces;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.model.beans.History;

import java.util.Date;
import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * Created by jianhuizhu on 2016-03-13.
 */
public interface DatabaseInterface {
    Observable<List<Bookmark>> getAllBookmarks();
    Observable<List<History>> getAllHistories();
    Observable<String> saveBookmark(final Bookmark bookmark);
    Observable<String> saveHistory(final History history);
    Observable<String> deleteBookmark(final Bookmark bookmark);
    Observable<Bookmark> updateBookmark(final Bookmark bookmark);
    Observable<String> deleteAllBookmark();
    Observable<String> deleteAllHistories();
    Observable<List<History>> getHistoryByDate(int year, int month, int day);
    Observable<String> deleteSelectedHistory(List<History> histories);
    Observable<Set<String>> getBlackList();
    Observable<String> addToBlackList(Set<String> domains);
    Observable<String> removeFromGlobalBlackList(Set<String>domains);
    Observable<String> deleteAllFromBlackList();
}
