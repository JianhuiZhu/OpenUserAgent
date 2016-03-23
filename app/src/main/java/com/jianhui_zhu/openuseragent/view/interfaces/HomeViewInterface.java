package com.jianhui_zhu.openuseragent.view.interfaces;

import android.database.Cursor;

/**
 * Created by jianhuizhu on 2016-02-09.
 */
public interface HomeViewInterface {
    void loadTargetUrl(String url);
    void showTag(String info);
    void swapCursor(Cursor cursor);
    void searchTargetWord(String word);
    void changeToolBarVisibility(int VIEW_CODE);
    void hideKeyboard();
}
