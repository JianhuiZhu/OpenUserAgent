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
    void saveBookmark(final Bookmark bookmark);
    void saveHistory(final History history);
    void deleteBookmark(final Bookmark bookmark);
    Observable<Bookmark> updateBookmark(final Bookmark bookmark);
    void deleteHistory(final History history);
    void deleteAllBookmark();
    void deleteAllHistories();
}
