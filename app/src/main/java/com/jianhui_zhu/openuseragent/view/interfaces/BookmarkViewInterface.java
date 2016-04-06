package com.jianhui_zhu.openuseragent.view.interfaces;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;

import rx.Observable;

/**
 * Created by jianhuizhu on 2016-03-06.
 */
public interface BookmarkViewInterface {
    void showTag(String str);
    void addNewBookmark(Bookmark bookmark);
    void updateBookmark(Bookmark bookmark);
    void deleteBookmark(Bookmark bookmark);
}
