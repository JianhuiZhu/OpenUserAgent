package com.jianhui_zhu.openuseragent.presenter;

import android.content.Context;

import com.jianhui_zhu.openuseragent.model.BookmarkModel;
import com.jianhui_zhu.openuseragent.model.beans.Bookmark;
import com.jianhui_zhu.openuseragent.util.RemoteDatabaseSingleton;

import java.util.List;

/**
 * Created by Jianhui Zhu on 2016-02-06.
 */
public class BookmarkPresenter {
    private Context context;
    private BookmarkModel bookmarkModel;
    public BookmarkPresenter(Context context){
        this.context=context;
        bookmarkModel=new BookmarkModel(context);
    }
    public List<Bookmark> getAllBookmarks(){
        return bookmarkModel.getAllBookmarks();
    }

}
