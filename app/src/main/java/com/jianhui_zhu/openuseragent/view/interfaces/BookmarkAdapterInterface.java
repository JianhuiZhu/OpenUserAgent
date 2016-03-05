package com.jianhui_zhu.openuseragent.view.interfaces;

import com.jianhui_zhu.openuseragent.model.beans.Bookmark;

/**
 * Created by jianhuizhu on 2016-03-04.
 */
public interface BookmarkAdapterInterface {
    void updatedBookmark(int position,Bookmark bookmark);
    void deletedBookmark(int position);
}
